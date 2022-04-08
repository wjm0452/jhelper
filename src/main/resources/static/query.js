(function () {

	$(document).ready(function () {

		$('#runSql').click(function (e) {

			var sql = $('#sqlText').val();

			jsql.query(sql).then(function (data, status, e) {
				drawTable($('#resultArea'), data);
				$('#rowcount').text(data.rowcount || 0);
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
					$('#rowcount').text(data.rowcount || 0);
				});

			} else if (e.ctrlKey && e.keyCode == 76) {

				var sql = this.value.substring(this.selectionStart, this.selectionEnd);

				jsql.query(sql).then(function (data, status, e) {
					drawTable($('#resultArea'), data);
				});
			}
		});

		$('#container').on('click', '.selection', function (e) {

			var $this = $(this),
				value = $this.val();

			caret($this, 0, value.length);
		});

		$('#tableArea').find('#tableList').on('click', 'tr td:first-child', function (e) {

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
				drawTable($('#tableList'), data);
			});

		});

		$('#tableArea').find('#txtTableName').on('keydown', function (e) {
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
				drawTable($('#comments'), data);
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

		jsql.loadTemplate('oracle').then(function() {
			$('#tableArea').find('#btnFindTable').trigger('click');
		});
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

		var hasMeta = false;
		var dataList = data;

		if (data.result) {
			var metadata = data.metadata;
			dataList = data.result;

			if (metadata) {
				hasMeta = true;
				dataList.unshift(metadata);
			}

		} else {
			dataList = data;
		}

		var $table = $('<table/>');

		dataList.forEach(function (rowData, i) {

			var $row = $('<tr />');

			if (i == 0 && hasMeta) {
				$row.addClass('metadata');
			} else {
				$row.addClass('data');
			}

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
    	
    	if(element instanceof jQuery) {
    		element = element[0];
    	}
    	
    	if(element.disabled) {
    		return;
    	}
    	
    	if(element.selectionStart != undefined) {
    		
    		if(begin == undefined) {
    			return [ element.selectionStart, element.selectionEnd ];
    		}
    		else {
    			
    			if(end == null) {
    				end = begin;
    			}
    			
    			element.focus();
    			element.setSelectionRange(begin, end);
    		}
    		
    	}
    }

})();