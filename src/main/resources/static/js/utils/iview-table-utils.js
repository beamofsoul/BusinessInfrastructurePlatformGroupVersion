var tableColumnData;
var parseValuesOnTableEachRow = function() {};

var pageTotal = 0;
var pageCurrent = 1;
var pageSize = 10;

var loadPageableDataUrl;//加载分页业务数据用的URL

//iview table binding checkbox 选中事件，selection：当前所有已选中的数据
function tableCheckboxSelectedDataFn(selection){
	this.tableCheckedData = selection;
}
// table row 修改按钮
function rowUpdateButtonFn (index) {
	var _self = this;
	$.iposty('user/single', {'id':_self.tableData[index].id}, 
		function(data){_self.updateForm = data.obj;_self.modalUpdate = true;},
		function(errorMessage){toastError(errorMessage);}
	);
}
//table row 删除按钮
function rowDeleteButtonFn (index) {
	this.modalDelMessage = "是否继续删除此条记录?";
	this.modalDelRowIds = ''+this.tableData[index].id;
	this.modalDel = true;// 显示删除界面
}

// 加载table数据	
function loadPageFn () {
	var _self = this;
//	var _self = getVueObject();
	const msg = toastLoading('正在加载中...',0);
	clearTableCheckedData();
	$.iposty(loadPageableDataUrl, {page: (_self.pageCurrent-1) , size: _self.pageSize,condition:formatQueryFormData(_self.queryForm)}, 
			function(data){
				_self.tableData = formatTableData(data);// 分页数据
				_self.pageTotal = data.pageableData.totalElements;// 总记录数
				setTimeout(msg, 120);//销毁加载提示
			},
			function(errorMessage){
				_self.$Message.error(errorMessage);	
				setTimeout(msg, 120);//销毁加载提示
			}
	);
}

// 翻页
function changePageFn (pageClick) {
	if (this.pageCurrent != pageClick) {
		this.pageCurrent = pageClick;
	}
	this.loadPage();
}

// 取出table选中checkbox的所有记录id
function getTableCheckedDataIds(tableCheckedData) {
	var ids = '';
	for (var i in tableCheckedData){
		ids+=tableCheckedData[i].id+",";
	}
	return (ids == '' ? ids : ids.substring(0, ids.length - 1));
}

// 清空当页选中的table中checkbox
function clearTableCheckedData(){
	getVueObject().tableCheckedData = [];
}

// 格式化服务端返回的table数据
function formatTableData(data){
	var value = [];
	for(var i=0;i<data.pageableData.numberOfElements;i++) {
		value[i] = parseValuesOnTableEachRow(data.pageableData.content[i]);
	}
	return value;
}

// 对table中的数据进行更新
function fresh4NewData(data,callback) {
	// 暂时先请求后台 来重新加载数据
	getVueObject().loadPage();
	callback();
}

// 创建 iview table 行 button
function createTableRowButtons(buttonsOnEachRow,row, column, index){
	var btnStr = '';
	for (var btnIndex in buttonsOnEachRow){
		var btn = buttonsOnEachRow[btnIndex];
		var btnAttributes = btn.split('#');
		btn = btnAttributes[0];
		var btnName = btnAttributes[1] ? btnAttributes[1] : btnAttributes[0];
		btnStr +='<i-button type="text" size="small" @click="'+btn+'('+index+')">'+btnName+'</i-button>';
	}
	return btnStr;
}

//创建 iview table 
function createTable(columnNames,attributeNames,buttonsOnEachRow){
	if(columnNames.length != attributeNames.length) return;
	var tableColumnData = [];
	for (var i=0;i<attributeNames.length;i++) { 
//		//测试 日期
//		if(i==7){tableColumnData[i]={title:columnNames[i],key:attributeNames[i],render (row) {return formatDate(row.createDate,true);}};continue;}
		if('operation'==attributeNames[i]){
			tableColumnData[i]={title:columnNames[i],key:attributeNames[i],
				render (row, column, index) {
					return createTableRowButtons(buttonsOnEachRow,row, column, index);
				}
			};
		}else if('selection'==attributeNames[i]){
			tableColumnData[i] = {type: 'selection',width: 60,align: 'center'};
		}else{
			tableColumnData[i]={title:columnNames[i],key:attributeNames[i]};
		}
	}
	return tableColumnData;
}

function setTableColumnData(columnNames,attributeNames,buttonsOnEachRow,columnData){
	var createTableResult = createTable(columnNames,attributeNames,buttonsOnEachRow);
	if(!columnData) tableColumnData = createTableResult;
	columnData = createTableResult;
}
