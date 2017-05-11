var defaultVueBindTableColumnsData;//table 列数据 data
var defaultVueBindPageTotalData = 0;//记录总数
var defaultVueBindPageCurrentData = 1;//当前页数
var defaultVueBindPageSizeData = 4;//每一页显示条数

var loadPageableDataUrl;//加载分页业务数据用的URL

// table row 修改按钮
function rowUpdateButtonFn (index) {
	var _self = this;
	$.iposty('user/single', {'id':_self.defaultVueBindTableDataData[index].id}, 
		function(data){_self.updateForm = data.obj;_self.modalUpdate = true;},
		function(errorMessage){toastError(errorMessage);}
	);
}
//table row 删除按钮
function rowDeleteButtonFn (index) {
	this.modalDelMessage = "是否继续删除此条记录?";
	this.modalDelRowIds = ''+this.defaultVueBindTableDataData[index].id;
	this.modalDel = true;// 显示删除界面
}



// 取出table选中checkbox的所有记录id
function getTableCheckedDataIds(tableCheckedData) {
	var ids = '';
	for (var i in tableCheckedData){
		ids+=tableCheckedData[i].id+",";
	}
	return (ids == '' ? ids : ids.substring(0, ids.length - 1));
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


/**
 * table 翻页方法
 * @param clickPageNumber 当前点击页数
 * @param vueBindPageCurrentDataName 当前页数vue data name
 * @param vueBindTableDataDataName 数据vue data name 第一个参数null 所有值取默认项
 * @param vueBindPageTotalDataName 总记录数 vue data name
 * @param vueBindPageSizeDataName 每页显示数据条数vue data name
 * @param vueTableCheckedDataName 需要清空的以选择 vue data name
 * @param vueBindFormQueryDataName 查询form的数据 vue data name
 * @returns
 */
function vueBindPageOnChangeMethod (clickPageNumber,vueBindPageCurrentDataName,vueBindTableDataDataName,vueBindPageTotalDataName,vueBindPageSizeDataName,vueTableCheckedDataName,vueBindFormQueryDataName) {
	if(!vueBindPageCurrentDataName) vueBindPageCurrentDataName = 'defaultVueBindPageCurrentData';
	if (this[vueBindPageCurrentDataName] != clickPageNumber) {
		this[vueBindPageCurrentDataName] = clickPageNumber;
	}
	this.vueTableLoadPageMethod(vueBindTableDataDataName,vueBindPageTotalDataName,vueBindPageSizeDataName,vueBindPageCurrentDataName,vueTableCheckedDataName,vueBindFormQueryDataName);
}

/**
 * table 加载分页数据
 * @param vueBindTableDataDataName 数据vue data name 第一个参数null 所有值取默认项
 * @param vueBindPageTotalDataName 总记录数 vue data name
 * @param vueBindPageSizeDataName 每页显示数据条数vue data name
 * @param vueBindPageCurrentDataName 当前页数vue data name
 * @param vueTableCheckedDataName 需要清空的以选择 vue data name
 * @param vueBindFormQueryDataName 查询form的数据 vue data name
 * @returns
 */
function vueTableLoadPageMethod (vueBindTableDataDataName,vueBindPageTotalDataName,vueBindPageSizeDataName,vueBindPageCurrentDataName,vueTableCheckedDataName,vueBindFormQueryDataName) {
	if(!vueBindTableDataDataName){
		vueBindTableDataDataName = 'defaultVueBindTableDataData';
		vueBindPageTotalDataName = 'defaultVueBindPageTotalData';
		vueBindPageSizeDataName = 'defaultVueBindPageSizeData';
		vueBindPageCurrentDataName = 'defaultVueBindPageCurrentData';
		vueTableCheckedDataName = 'defaultVueTableCheckedData';
		vueBindFormQueryDataName = 'defaultVueBindFormQueryData';
	}
	
	var _self = this;
	const msg = toastLoading('正在加载中...',0);
	clearVueTableCheckedDataMethod(_self[vueTableCheckedDataName]);//清空选中数据
	$.iposty(loadPageableDataUrl, {page: (_self[vueBindPageCurrentDataName]-1) , size: _self[vueBindPageSizeDataName],condition:formatQueryFormData(_self[vueBindFormQueryDataName])}, 
			function(data){
				_self[vueBindTableDataDataName] = formatTableData(data);// 分页数据
				_self[vueBindPageTotalDataName] = data.pageableData.totalElements;// 总记录数
				setTimeout(msg, 120);//销毁加载提示
			},
			function(errorMessage){
				_self.$Message.error(errorMessage);	
				setTimeout(msg, 120);//销毁加载提示
			}
	);
}

/**
 * 清空指定 table 以选中的记录 vue data
 * @param vueTableCheckedData vue data
 * @returns
 */
function clearVueTableCheckedDataMethod(vueTableCheckedData){
	vueTableCheckedData = [];
}

/**
 * table checkbox 选中数据 返回给指定vue data
 * @param selection 已选项数据 当前所有被选择的数据
 * @param vueTableCheckedDataName vue 装选中项的data 名字 
 * @returns
 */
function vueBindTableCheckboxSelectMethod(selection,vueTableCheckedDataName){
	this[vueTableCheckedDataName] = selection;
}
/**
 * 设置TableColumnsData
 * @param tableColumnsName 列名 
 * @param tableColumnsKey 列 对应Vue Data 属性名 。全选 加 'selection' 项 , 操作 加 'operation' 项。
 * @param tableButtonsOnEachRow 每行数据后的按钮
 * @param vueTableColumnsData 绑定在i-table的Columns的data引用 (有默认值)
 * @returns
 */
function setVueTableColumnsData(tableColumnsName,tableColumnsKey,tableButtonsOnEachRow,vueBindTableColumnsData){
	var createTableResult = createTable(tableColumnsName,tableColumnsKey,tableButtonsOnEachRow);
	if(!vueBindTableColumnsData) defaultVueBindTableColumnsData = createTableResult;
	vueBindTableColumnsData = createTableResult;
}
