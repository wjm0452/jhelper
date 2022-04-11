function Jsql(options) {

	var _tablePrefix = '';
	var _url = '/api/sql';

	if (options.tablePrefix) {
		_tablePrefix = options.tablePrefix;
	}

	if (options.url) {
		_url = options.url;
	}

	this.query = function (text, params) {

		text = text.trim();

		if (text.endsWith(';')) {
			text = text.substr(0, text.length - 1);
		}

		//sql = window.encodeURIComponent(sql);

		return axios.post(_url, {
			query: text,
			params
		}).then(function (res) {
			return res.data;
		}).catch(function (response) {
			alert('오류가 발생하였습니다.');
			return $.Deferred().reject(response);
		});
	};

	this.loadTemplate = function (vendor) {

		var that = this;

		return axios.get('vendor/' + vendor + ".xml").then(function (res) {
			var sqlNode = $(res.data);
			var tableQuery = sqlNode.find('#table').html(),
				columnQuery = sqlNode.find('#columns').html(),
				indexesQuery = sqlNode.find('#indexes').html();

			that.tableTmpl = function () {
				return tableQuery;
			};
			that.columnsTmpl = function () {
				return columnQuery;
			};
			that.indexesTmpl = function () {
				return indexesQuery;
			};
		});
	};

	this.findTableInfo = function (data) {

		var tmpl = this.tableTmpl();
		var sql = Mustache.render(tmpl, data);

		return this.query(sql);
	};

	this.findColumnInfo = function (data) {

		var tmpl = this.columnsTmpl();
		var sql = Mustache.render(tmpl, data);

		return this.query(sql);
	};

	this.findIndexesInfo = function (data) {

		var tmpl = this.indexesTmpl();
		var sql = Mustache.render(tmpl, data);

		return this.query(sql);
	};

	this.tableTmpl = function () {
		return '';
	};

	this.columnsTmpl = function () {
		return '';
	};

	this.indexesTmpl = function () {
		return '';
	};


	this.selectQuery = function (data) {

		if (!data.owner || !data.tableName) {
			alert('owner, tableName을 입력해주세요.');
			return;
		}

		var that = this;

		return Promise.all([this.findColumnInfo(data), this.findIndexesInfo(data)]).then(function (returnValue) {

			var columns = returnValue[0].result,
				indexes = returnValue[1].result;

			var coltext = columns.map(function (arr, idx) {
				return arr[1] + ' /* ' + arr[2] + ' */';
			}).join(',\n       ');

			var idxtext = indexes.map(function (arr, idx) {
				return arr[0] + ' = #{' + arr[0] + '}';
			}).join('\n   AND ');

			var id = toCamel(data.tableName.replace(_tablePrefix, 'SELECT_'));

			var tmplData = {
				id: id,
				columns: coltext,
				indexes: ' WHERE ' + idxtext,
				tableName: data.tableName
			};

			return Mustache.render(that.selectQueryTmpl(), tmplData);
		});

	};


	this.insertQuery = function (data) {

		if (!data.owner || !data.tableName) {
			alert('owner, tableName을 입력해주세요.');
			return;
		}

		var that = this;

		return this.findColumnInfo(data).then(function (columns) {

			columns = columns.result;

			var coltext = columns.map(function (arr, idx) {
				return arr[1] + ' /* ' + arr[2] + ' */';
			}).join(',\n    ');

			var values = columns.map(function (arr, idx) {
				return '#{' + arr[1] + '}';
			}).join(',\n    ');

			var id = toCamel(data.tableName.replace(_tablePrefix, 'INSERT_'));

			var tmplData = {
				id: id,
				columns: coltext,
				values: values,
				tableName: data.tableName
			};

			return Mustache.render(that.insertQueryTmpl(), tmplData);
		});

	};


	this.updateQuery = function (data) {

		if (!data.owner || !data.tableName) {
			alert('owner, tableName을 입력해주세요.');
			return;
		}

		var that = this;

		return Promise.all([this.findColumnInfo(data), this.findIndexesInfo(data)]).then(function (returnValue) {

			var columns = returnValue[0].result,
				indexes = returnValue[1].result;

			var coltext = columns.map(function (arr, idx) {
				return arr[1] + ' = #{' + arr[1] + '}' + ' /* ' + arr[2] + ' */';
			}).join(',\n       ');

			var idxtext = indexes.map(function (arr, idx) {
				return arr[0] + ' = #{' + arr[0] + '}';
			}).join('\n   AND ');

			var id = toCamel(data.tableName.replace(_tablePrefix, 'UPDATE_'));

			var tmplData = {
				id: id,
				columns: coltext,
				indexes: ' WHERE ' + idxtext,
				tableName: data.tableName
			};

			return Mustache.render(that.updateQueryTmpl(), tmplData);
		});

	};

	this.deleteQuery = function (data) {

		if (!data.owner || !data.tableName) {
			alert('owner, tableName을 입력해주세요.');
			return;
		}

		var that = this;

		return this.findIndexesInfo(data).then(function (indexes) {

			indexes = indexes.result;

			var idxtext = indexes.map(function (arr, idx) {
				return arr[0] + ' = #{' + arr[0] + '}';
			}).join('\n   AND ');

			var id = toCamel(data.tableName.replace(_tablePrefix, 'DELETE_'));

			var tmplData = {
				id: id,
				indexes: ' WHERE ' + idxtext,
				tableName: data.tableName
			};

			return Mustache.render(that.deleteQueryTmpl(), tmplData);
		});

	};


	this.selectQueryTmpl = function () {
		var result =
			`
SELECT /* comment */
       {{&columns}}
  FROM {{tableName}}
{{&indexes}}
`;
		return result;
	};


	this.insertQueryTmpl = function () {
		var result =
			`
INSERT /* comment */
  INTO {{tableName}}
(
    {{&columns}}
)
VALUES 
(
    {{&values}}
)
`;
		return result;
	};


	this.updateQueryTmpl = function () {
		var result =
			`
/* comment */
UPDATE {{tableName}}
   SET {{&columns}}
{{&indexes}}
`;
		return result;
	};

	this.deleteQueryTmpl = function () {

		var result =
			`
DELETE /* comments */
  FROM {{tableName}}
{{&indexes}}
`;
		return result;
	};


	function toCamel(str) {

		str = str.toLowerCase();

		return str.replace(/_\w/g, function (m) {
			return m[1].toUpperCase();
		});
	}

	function toUnderscore(str) {
		return str.replace(/([A-Z])/g, function (m) {
			return "_" + m.toLowerCase();
		});
	}

	function setData(id, value, expires) {

		expires = parseInt((expires != null ? 7 : expires), 10);

		var date = new Date();

		date = new Date(date.getFullYear(), date.getMonth(), date.getDate() + expires);
		document.cookie = id + "=" + window.escape(value) + "; expires=" + date.toGMTString() + "; path=/";
	}

	function getData(sName) {

		var cookies = document.cookie.split("; ");

		for (var i = 0; i < cookies.length; i++) {

			var crumb = cookies[i].split("=");

			if (sName == crumb[0]) {
				return crumb[1] != null ? unescape(crumb[1]) : "";
			}
		}

		return "";
	}

	this.toCamel = toCamel;
	this.toUnderscore = toUnderscore;

	this.setData = setData;
	this.getData = getData;
}