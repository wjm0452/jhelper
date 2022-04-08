(function () {

	var jsql = {};

	jsql.TABLE_PREFIX = 'T_';
	jsql.url = '/api/sql';

	jsql.query = function (text, params) {

		text = text.trim();

		if (text.endsWith(';')) {
			text = text.substr(0, text.length - 1);
		}

		//sql = window.encodeURIComponent(sql);

		return axios.post(jsql.url, {
			query: text,
			params
		}).then(function (res) {
			return res.data;
		}).catch(function (response) {
			alert('오류가 발생하였습니다.');
			return $.Deferred().reject(response);
		});
	};

	jsql.loadTemplate = function (vendor) {
		return axios.get('vendor/' + vendor + ".xml").then(function (res) {
			var sqlNode = $(res.data);
			var tableQuery = sqlNode.find('#table').html(),
				columnQuery = sqlNode.find('#columns').html(),
				indexesQuery = sqlNode.find('#indexes').html();

			jsql.tableTmpl = function () {
				return tableQuery;
			};
			jsql.columnsTmpl = function () {
				return columnQuery;
			};
			jsql.indexesTmpl = function () {
				return indexesQuery;
			};
		});
	};

	jsql.findTableInfo = function (data) {

		var tmpl = jsql.tableTmpl();
		var sql = Mustache.render(tmpl, data);

		return jsql.query(sql);
	};

	jsql.findColumnInfo = function (data) {

		var tmpl = jsql.columnsTmpl();
		var sql = Mustache.render(tmpl, data);

		return jsql.query(sql);
	};

	jsql.findIndexesInfo = function (data) {

		var tmpl = jsql.indexesTmpl();
		var sql = Mustache.render(tmpl, data);

		return jsql.query(sql);
	};

	jsql.tableTmpl = function () {
		return '';
	};

	jsql.columnsTmpl = function () {
		return '';
	};

	jsql.indexesTmpl = function () {
		return '';
	};


	jsql.selectQuery = function (data) {

		if (!data.owner || !data.tableName) {
			alert('owner, tableName을 입력해주세요.');
			return;
		}

		return Promise.all([jsql.findColumnInfo(data), jsql.findIndexesInfo(data)]).then(function (returnValue) {

			var columns = returnValue[0].result,
				indexes = returnValue[1].result;

			var coltext = columns.map(function (arr, idx) {
				return arr[1] + ' /* ' + arr[2] + ' */';
			}).join(',\n       ');

			var idxtext = indexes.map(function (arr, idx) {
				return arr[0] + ' = #{' + arr[0] + '}';
			}).join('\n   AND ');

			var id = toCamel(data.tableName.replace(jsql.TABLE_PREFIX, 'SELECT_'));

			var tmplData = {
				id: id,
				columns: coltext,
				indexes: ' WHERE ' + idxtext,
				tableName: data.tableName
			};

			return Mustache.render(jsql.selectQueryTmpl(), tmplData);
		});

	};


	jsql.insertQuery = function (data) {

		if (!data.owner || !data.tableName) {
			alert('owner, tableName을 입력해주세요.');
			return;
		}

		return jsql.findColumnInfo(data).then(function (columns) {

			columns = columns.result;

			var coltext = columns.map(function (arr, idx) {
				return arr[1] + ' /* ' + arr[2] + ' */';
			}).join(',\n    ');

			var values = columns.map(function (arr, idx) {
				return '#{' + arr[1] + '}';
			}).join(',\n    ');

			var id = toCamel(data.tableName.replace(jsql.TABLE_PREFIX, 'INSERT_'));

			var tmplData = {
				id: id,
				columns: coltext,
				values: values,
				tableName: data.tableName
			};

			return Mustache.render(jsql.insertQueryTmpl(), tmplData);
		});

	};


	jsql.updateQuery = function (data) {

		if (!data.owner || !data.tableName) {
			alert('owner, tableName을 입력해주세요.');
			return;
		}

		return Promise.all([jsql.findColumnInfo(data), jsql.findIndexesInfo(data)]).then(function (returnValue) {

			var columns = returnValue[0].result,
				indexes = returnValue[1].result;

			var coltext = columns.map(function (arr, idx) {
				return arr[1] + ' = #{' + arr[1] + '}' + ' /* ' + arr[2] + ' */';
			}).join(',\n       ');

			var idxtext = indexes.map(function (arr, idx) {
				return arr[0] + ' = #{' + arr[0] + '}';
			}).join('\n   AND ');

			var id = toCamel(data.tableName.replace(jsql.TABLE_PREFIX, 'UPDATE_'));

			var tmplData = {
				id: id,
				columns: coltext,
				indexes: ' WHERE ' + idxtext,
				tableName: data.tableName
			};

			return Mustache.render(jsql.updateQueryTmpl(), tmplData);
		});

	};

	jsql.deleteQuery = function (data) {

		if (!data.owner || !data.tableName) {
			alert('owner, tableName을 입력해주세요.');
			return;
		}

		return jsql.findIndexesInfo(data).then(function (indexes) {

			indexes = indexes.result;

			var idxtext = indexes.map(function (arr, idx) {
				return arr[0] + ' = #{' + arr[0] + '}';
			}).join('\n   AND ');

			var id = toCamel(data.tableName.replace(jsql.TABLE_PREFIX, 'DELETE_'));

			var tmplData = {
				id: id,
				indexes: ' WHERE ' + idxtext,
				tableName: data.tableName
			};

			return Mustache.render(jsql.deleteQueryTmpl(), tmplData);
		});

	};


	jsql.selectQueryTmpl = function () {
		var result =
			`
SELECT /* comment */
       {{&columns}}
  FROM {{tableName}}
{{&indexes}}
`;
		return result;
	};


	jsql.insertQueryTmpl = function () {
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


	jsql.updateQueryTmpl = function () {
		var result =
			`
/* comment */
UPDATE {{tableName}}
   SET {{&columns}}
{{&indexes}}
`;
		return result;
	};

	jsql.deleteQueryTmpl = function () {

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

	jsql.toCamel = toCamel;
	jsql.toUnderscore = toUnderscore;

	window.jsql = jsql;

})();