var tableColumnsName;
var tableColumnsKey;
var tableButtonsOnEachRow;

var currentTableDataName = 'vueTableData';
var currentRecordTotalName = 'vueRecordTotal';
var	currentPageSizeName = 'vuePageSize';
var	currentCurrentPageName = 'vueCurrentPage';
var currentCheckedTableRowName = 'vueCheckedTableRow';
var currentCheckedTableRowIdsName = 'vueCheckedTableRowIds';
var currentQueryFormName = 'vueQueryForm';
var currentTableColumnsName = 'vueTableColumns';

var vueTableColumns={};//table 列数据 data
var vueTableData=[];//table data
var vueCheckedTableRow=[];//checked data
var vueCheckedTableRowIds = '';//checked ids
var vueRecordTotal = 0;//记录总数
var vueCurrentPage = 1;//当前页数
var vuePageSize = 4;//每一页显示条数

var loadPageableDataUrl;//加载分页业务数据用的URL

/**
 * 获取checked的ids
 * @param checkedData 选中的值 data
 */
function getCheckedTableRowIds(checkedData) {
	var ids = '';
	for (var i in checkedData){
		ids+=checkedData[i].id+",";
	}
	return (ids == '' ? ids : ids.substring(0, ids.length - 1));
}

/**
 * 格式化服务端返回的table数据
 * @param data 服务端返回的数据
 * @returns
 */
function formatTableData(data){
	var value = [];
	for(var i=0;i<data.pageableData.numberOfElements;i++) {
		value[i] = parseValuesOnTableEachRow(data.pageableData.content[i]);
	}
	return value;
}

/**
 * 刷新vue table 数据
 * @param data 后台返回变更的数据
 * @param callback 请求后台成功后的回调函数
 * @returns
 */
function fresh4NewData(data,callback) {
	// 暂时先请求后台 来重新加载数据
	getVueObject().doLoadPage();
	callback();
}

/**
 * table 翻页方法
 * @param clickPageNumber 当前点击页数
 */
function doPageTurning (clickPageNumber) {
	if (this[currentCurrentPageName] != clickPageNumber) this[currentCurrentPageName] = clickPageNumber;
	this.doLoadPage();
}

/**
 * table 加载分页数据
 */
function doLoadPage () {
	var _self = this;
	const msg = toastLoading('正在加载中...',0);
	clearDataValue(currentCheckedTableRowName);//清空选中数据
	$.iposty(loadPageableDataUrl, {page: (_self[currentCurrentPageName]-1) , size: _self[currentPageSizeName],condition:formatQueryFormData(_self[currentQueryFormName])}, 
			function(data){
				_self[currentTableDataName] = formatTableData(data);// 分页数据
				_self[currentRecordTotalName] = data.pageableData.totalElements;// 总记录数
				setTimeout(msg, 120);//销毁加载提示
			},
			function(errorMessage){
				_self.$Message.error(errorMessage);	
				setTimeout(msg, 120);//销毁加载提示
			}
	);
}
////////////////////////////////////////////////
/**
 * 清空指定 table 以选中的记录 vue data
 * @param vueTableCheckedData vue data
 * @returns
 */
function clearDataValue(checkedTableRowName){
	if(!checkedTableRowName) checkedTableRowName = currentCheckedTableRowName;
	this[checkedTableRowName]= [];
}

/**
 * table checkbox选中的值   
 * @param selection 已选项数据 当前所有被选择的数据
 * @param checkedTableRowName 装选中项的data名字 
 * @returns
 */
function getCheckedTableRow(selection,checkedTableRowName){
	if(!checkedTableRowName) checkedTableRowName = currentCheckedTableRowName;
	this[checkedTableRowName] = selection;
}

/**
 * 设置TableColumnsData
 * @param tableColumnsName 列名 
 * @param tableColumnsKey 列key （key为 'selection' 全选, 'operation' 操作。
 * @param tableButtonsOnEachRow 每行数据后的按钮 (可为null)
 * @param vueTableColumnsData 在table Columns绑定的data (有默认值)
 * @param vueTableDataName 在table Data绑定的data名称 (有默认值)
 * @returns
 */
function createTableColumns(tableColumnsName,tableColumnsKey,tableButtonsOnEachRow,tableColumnsDataName,tableDataName){
	if(!tableDataName) tableDataName = currentTableDataName;
	var vueTableColumnsDataResult = createTableColumnsData(tableColumnsName,tableColumnsKey,tableButtonsOnEachRow,tableDataName);
	if(!tableColumnsDataName) tableColumnsDataName = currentTableColumnsName;
	this[tableColumnsDataName] = vueTableColumnsDataResult;
}


/**
 * 创建table columns data
 */
function createTableColumnsData(tableColumnsName,tableColumnsKey,tableButtonsOnEachRow,tableDataName){
	var tableColumnsData = [];
	for (var i=0;i<tableColumnsKey.length;i++) { 
		var oneKeyArray = tableColumnsKey[i].split('#');
		var oneKey = oneKeyArray[0];
		var oneKeySortable = false;
		if(oneKeyArray.length>1){
			if(oneKeyArray[1]=='sortable') oneKeySortable = true;
		}
		var oneName = tableColumnsName[i];
		if('operation'==oneKey){
			tableColumnsData[i]={title:oneName,key:oneKey,
				render (row, column, index) {
					return createTableColumnsDataButtons(tableButtonsOnEachRow,row, column, index,tableDataName);
				}
			};
		}else if('selection'==oneKey){
			tableColumnsData[i] = {type: 'selection',width: 60,align: 'center'};
		}else{
			if(oneKeySortable)
				tableColumnsData[i] = {title:oneName,key:oneKey,sortable:true};
			else
				tableColumnsData[i] = {title:oneName,key:oneKey};
		}
	}
	return tableColumnsData;
}

/**
 * 创建 table column data render 按钮
 * @param tableButtonsOnEachRow 要创建的按钮 格式（点击按钮触发vue方法名#按钮名称）
 * @param row 点击按钮的vue data 行数据
 * @param column 点击按钮的vue data 列数据
 * @param index 点击按钮的vue data index
 * @param tableDataName 绑定在i-table的Data的data名称
 * @returns
 */
function createTableColumnsDataButtons(tableButtonsOnEachRow,row, column, index,tableDataName){
	var btnStr = '';
	for (var btnIndex in tableButtonsOnEachRow){
		var btn = tableButtonsOnEachRow[btnIndex];
		var btnAttributes = btn.split('#');
		btn = btnAttributes[0];
		var btnName = btnAttributes[1] ? btnAttributes[1] : btnAttributes[0];
		btnStr +='<i-button type="text" size="small" @click="'+btn+'('+index+',\''+tableDataName+'\')">'+btnName+'</i-button>';
	}
	return btnStr;
}
