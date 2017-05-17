var vueTableColumns={};//table 列数据 data
var defaultVueBindPageTotalData = 0;//记录总数
var defaultVueBindPageCurrentData = 1;//当前页数
var defaultVueBindPageSizeData = 4;//每一页显示条数
var defaultVueBindTableDataDataName = 'vueTableData';

var loadPageableDataUrl;//加载分页业务数据用的URL
var vueCheckedTableRowIds = '';
var vueTableData=[];

/**
 * 获取table check data ids
 * @param vueTableCheckedData vue data object
 * @returns
 */
function getVueTableCheckedDataIds(vueTableCheckedData) {
	var ids = '';
	for (var i in vueTableCheckedData){
		ids+=vueTableCheckedData[i].id+",";
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
 * @param callback table 查询数据成功后的回调函数
 * @param vueBindTableDataDataName 数据vue data name 参数null 以下所有取默认data
 * @param vueBindPageTotalDataName 总记录数 vue data name
 * @param vueBindPageSizeDataName 每页显示数据条数vue data name
 * @param vueBindPageCurrentDataName 当前页数vue data name
 * @param vueTableCheckedDataName 需要清空的以选择 vue data name
 * @param vueBindFormQueryDataName 查询form的数据 vue data name
 * @returns
 */
function fresh4NewData(data,callback,vueBindTableDataDataName,vueBindPageTotalDataName,vueBindPageSizeDataName,vueBindPageCurrentDataName,vueTableCheckedDataName,vueBindFormQueryDataName) {
	// 暂时先请求后台 来重新加载数据
	getVueObject().vueTableLoadPageMethod(vueBindTableDataDataName,vueBindPageTotalDataName,vueBindPageSizeDataName,vueBindPageCurrentDataName,vueTableCheckedDataName,vueBindFormQueryDataName);
	callback();
}

/**
 * 创建 table column data render 内按钮
 * @param tableButtonsOnEachRow 要创建的按钮 格式（点击按钮触发vue方法名#按钮名称）
 * @param row 点击按钮的vue data 行数据
 * @param column 点击按钮的vue data 列数据
 * @param index 点击按钮的vue data index
 * @param vueBindTableDataDataName 绑定在i-table的Data的data名称 (有默认值)
 * @returns
 */
function createVueTableColumnsDataButtons(tableButtonsOnEachRow,row, column, index,vueBindTableDataDataName){
	var btnStr = '';
	for (var btnIndex in tableButtonsOnEachRow){
		var btn = tableButtonsOnEachRow[btnIndex];
		var btnAttributes = btn.split('#');
		btn = btnAttributes[0];
		var btnName = btnAttributes[1] ? btnAttributes[1] : btnAttributes[0];
		btnStr +='<i-button type="text" size="small" @click="'+btn+'('+index+',\''+vueBindTableDataDataName+'\')">'+btnName+'</i-button>';
	}
	return btnStr;
}


/**
 * 创建table columns data
 * @param tableColumnsName 列名
 * @param tableColumnsKey data 属性名
 * @param tableButtonsOnEachRow data 重写操作列 按钮
 * @returns
 */
function createVueTableColumnsData(tableColumnsName,tableColumnsKey,tableButtonsOnEachRow,vueBindTableColumnsDataName){
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
					return createVueTableColumnsDataButtons(tableButtonsOnEachRow,row, column, index,vueBindTableColumnsDataName);
				}
			};
		}else if('selection'==oneKey){
			tableColumnsData[i] = {type: 'selection',width: 60,align: 'center'};
		}else{
			if(oneKeySortable)
			tableColumnsData[i] = {title:oneName,key:oneKey,sortable:'custom'};
			else
				tableColumnsData[i] = {title:oneName,key:oneKey};
		}
	}
	return tableColumnsData;
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
		vueBindTableDataDataName = defaultVueBindTableDataDataName;
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
function vueBindTableCheckedDataMethod(selection,vueTableCheckedDataName){
	this[vueTableCheckedDataName] = selection;
}
/**
 * 设置TableColumnsData
 * @param tableColumnsName 列名 
 * @param tableColumnsKey 列 对应Vue Data 属性名 。全选 加 'selection' 项 , 操作 加 'operation' 项。
 * @param tableButtonsOnEachRow 每行数据后的按钮
 * @param vueTableColumnsData 绑定在i-table的Columns的data引用 (有默认值)
 * @param vueBindTableDataDataName 绑定在i-table的Data的data名称 (有默认值)
 * @returns
 */
function setVueTableColumnsData(tableColumnsName,tableColumnsKey,tableButtonsOnEachRow,vueBindTableColumnsData,vueBindTableDataDataName){
	if(!vueBindTableDataDataName) vueBindTableDataDataName = defaultVueBindTableDataDataName;
	var vueTableColumnsDataResult = createVueTableColumnsData(tableColumnsName,tableColumnsKey,tableButtonsOnEachRow,vueBindTableDataDataName);
	if(!vueBindTableColumnsData) vueTableColumns = vueTableColumnsDataResult;
	vueBindTableColumnsData = vueTableColumnsDataResult;
}
