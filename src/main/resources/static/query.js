(function () {

	var jsql = new Jsql({
		tablePrefix: 'T_',
		url: '/api/sql'
	});

	$(document).ready(function () {

		$('#runSql').click(function (e) {

			var sql = $('#sqlText').val();

			jsql.query(sql).then(function (data, status, e) {
				drawTable($('#resultArea'), data);
				$('#rowCount').text(data.result.length || 0);
			});

		});

		$('#sqlText').keydown(function (e) {

			if (e.ctrlKey && e.keyCode == 13) {

				var sql = this.value;

				var tokenizer = createSQLTokenizer(sql);

				var selection = this.selectionStart,
					start = 0,
					end = 0,
					pos = 0;

				while (tokenizer.hasNext()) {

					var str = tokenizer.next();

					pos += str.length;

					if (str == ';') {

						if (selection <= pos) {
							end = pos;
							break;
						} else if (pos <= selection) {
							start = pos;
						}

					}

					end = pos;
				}

				if (sql.charAt(start) == '\n') {
					start++;
				}

				$('#sqlText')[0].setSelectionRange(start, end);

				jsql.query(sql.substring(start, end)).then(function (data, status, e) {
					drawTable($('#resultArea'), data);
					$('#rowCount').text(data.result.length || 0);
				});

			} else if (e.ctrlKey && e.keyCode == 76) {

				var sql = this.value.substring(this.selectionStart, this.selectionEnd);

				jsql.query(sql).then(function (data, status, e) {
					drawTable($('#resultArea'), data);
				});
			}
		});

		$('#selVendor').on('change', function () {
			jsql.loadTemplate(this.value).then(function () {
				if ($('#txtOwner').val()) {
					$('#tableArea').find('#btnFindTable').trigger('click');
				}
			});
		})

		$('#container').on('click', '.selection', function (e) {

			var $this = $(this),
				value = $this.val();

			caret($this, 0, value.length);
		});

		$('#tableArea').find('#tables').on('click', 'tr td:first-child', function (e) {

			var $this = $(this);
			var tableName = $this.text();
			$('#columnArea').find('#txtTableName').val(tableName);
			$('#columnArea').find('#btnFindColumn').trigger('click');
		});

		$('#tableArea').find('#btnFindTable').on('click', function (e) {

			var data = {
				owner: $('#txtOwner').val(),
				tableName: $('#tableArea').find('#txtTableName').val()
			};

			jsql.findTableInfo(data).then(function (data, status, e) {
				drawTable($('#tables'), data);
			});

		});

		$('#tableArea').find('#txtOwner, #txtTableName').on('keydown', function (e) {
			if (e.keyCode == 13) {
				$('#tableArea').find('#btnFindTable').trigger('click');
			}
		});

		$('#columnArea').find('#btnFindColumn').on('click', function (e) {

			var $columnArea = $('#columnArea');

			var owner = $('#txtOwner').val(),
				tableName = $columnArea.find('#txtTableName').val(),
				columnName = $columnArea.find('#txtColumnName').val();

			if (!tableName && !columnName) {
				alert('테이블명 또는 컬럼명을 입력하세요.');
				return;
			}

			var data = {
				owner: owner,
				tableName: tableName,
				columnName: columnName
			};

			jsql.findColumnInfo(data).then(function (data, status, e) {
				drawTable($('#columns'), data);
				$('#columnArea').find('#txtTableName').val(tableName);
			});


		});

		$('#btnSelect').click(function () {

			var owner = $('#txtOwner').val(),
				tableName = $('#columnArea').find('#txtTableName').val();

			var options = {
				owner: owner,
				tableName: tableName
			};

			jsql.selectQuery(options).then(function (sql) {
				printSql(sql);
			});

		});

		$('#btnUpdate').click(function () {

			var owner = $('#txtOwner').val(),
				tableName = $('#columnArea').find('#txtTableName').val();

			var options = {
				owner: owner,
				tableName: tableName
			};

			jsql.updateQuery(options).then(function (sql) {
				printSql(sql);
			});

		});

		$('#btnInsert').click(function () {

			var owner = $('#txtOwner').val(),
				tableName = $('#columnArea').find('#txtTableName').val();

			var options = {
				owner: owner,
				tableName: tableName
			};

			jsql.insertQuery(options).then(function (sql) {
				printSql(sql);
			});

		});

		$('#btnDelete').click(function () {

			var owner = $('#txtOwner').val(),
				tableName = $('#columnArea').find('#txtTableName').val();

			var options = {
				owner: owner,
				tableName: tableName
			};

			jsql.deleteQuery(options).then(function (sql) {
				printSql(sql);
			});

		});

		$('[caching]').on('input', function () {
			var id = this.id;

			if (!id) {
				return;
			}

			jsql.setData(id, this.value);
		});

		$('[caching]').each(function (e) {

			var id = this.id;

			if (!id) {
				return;
			}

			var value = jsql.getData(id);

			if (value) {
				this.value = value;
			}
		});

		if ($('#selVendor').val()) {
			$('#selVendor').trigger('change');
		}
	});

	function createSQLTokenizer(sql) {

		var cus = {};
		cus["/*"] = "*/";
		cus["/*+"] = "*/";
		cus["//"] = "\n";
		cus["\'"] = "\'";

		return new TextTokenizer(sql, cus);
	}

	function printSql(sql) {
		$('#sqlText').val(sql + '\n' + $('#sqlText').val());
	}

	/**
	 * 
	 */
	function drawTable($area, data) {

		$area.empty();

		var dataList = data;

		var $table = $('<table class="table"/>');
		$table.on('click', 'tr', function () {
			$table.find('tr.active').removeClass('active');
			$(this).addClass('active');
		});

		if (data.result) {
			var columnNames = data.columnNames;
			dataList = data.result;

			var $row = $('<tr />');
			columnNames.forEach(function (value) {
				$row.append('<th>' + value + '</th>');
			});

			$table.append($row);

		} else {
			dataList = data;
		}

		dataList.forEach(function (rowData, i) {
			var $row = $('<tr/>');
			rowData.forEach(function (value, i) {
				$row.append('<td>' + value + '</td>');
			});
			$table.append($row);
		});

		$area.append($table);
	}

	/**
	 * caret selection
	 * 
	 * @memberof cb
	 * @param {element} element
	 * @param {number} begin block할 시작지점
	 * @param {number} end block할 종료지점
	 */
	function caret(element, begin, end) {

		if (element instanceof jQuery) {
			element = element[0];
		}

		if (element.disabled) {
			return;
		}

		if (element.selectionStart != undefined) {

			if (begin == undefined) {
				return [element.selectionStart, element.selectionEnd];
			} else {

				if (end == null) {
					end = begin;
				}

				element.focus();
				element.setSelectionRange(begin, end);
			}

		}
	}

})();