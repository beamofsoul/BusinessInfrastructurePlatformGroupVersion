var defaultVueBindFormAddData;
var defaultVueBindFormQueryData;

var defaultVueBindFormRulesAddData;
var defaultVueBindFormRulesUpdateData;

//这条要删除
var vueBindFormQueryDataName = 'defaultVueBindFormQueryData';
var defaultVueBindFormQueryDataName = 'defaultVueBindFormQueryData';

var defaultVueBindModalAddData = false;//添加form 对话框 显示状态
var defaultVueBindModalUpdateData = false;//修改form 对话框 显示状态
var defaultVueBindModalDelData = false;//删除 对话框 显示状态

var defaultVueBindModalDelLoadingData = false;//删除form 对话框 按钮状态
var defaultVueBindModalDelMessageData = '';

var currentAction = null;// 当前用户操作的行为 - add、update、delete 等

var queryFromRowItemNum = 4;//综合查询 每行放控件的数量
var queryFormItemWidth = 80;//综合查询 控件的宽度 像素
var querySubmitButtonName = 'vueBindButtonClickQueryMethod';//综合查询 提交按钮触发函数名
var defaultVueBindCollapseQueryFormData='-1';
var defaultQueryFormDomId = 'queryFormDomId';//默认的 query form 页面 dom id


var initAddForm;    //初始化添加页面表单业务数据的方法
var initUpdateForm; //初始化更新页面表单业务数据的方法
var initCopyForm;   //初始化复制页面表单业务数据的方法

var beforeAdd;      //执行进入添加按钮单击事件方法首先需要执行的方法
var beforeUpdate;   //执行进入修改按钮单击事件方法首先需要执行的方法
var beforeDelete;   //执行进入删除按钮单击事件方法首先需要执行的方法
var beforeCopy;     //执行进入复制按钮单击事件方法首先需要执行的方法

var updateBefore;   //执行修改后台方法之前需要执行的方法
var copyBefore;     //执行复制后台方法之前需要执行的方法
var deleteBefore;   //执行删除后台方法之前需要执行的方法

var defaultVueBindFormUpdateData;
var updataFormName = 'defaultVueBindFormUpdateData';
var updataFormModalName = 'defaultVueBindModalUpdateData';
var tableCheckDataName = 'defaultVueTableCheckedData';

var addFormName = 'defaultVueBindFormAddData';

/********************  添加按钮  *********************/
/**
 * 添加
 */
function vueBindButtonHeadAddMethod (){
	if(beforeAdd) beforeAdd();
	
	resetVueFormData(addFormName);
	currentAction = actions.add;
	this.defaultVueBindModalAddData = true;
	
	if (initAddForm) initAddForm();
}
/**
 * 添加提交
 */
function vueBindButtonHeadAddSubmitMethod () {
	var _self = this;
	submitFormValidate(currentAction,function(data){
		toastSuccess('提交成功!');
		_self.defaultVueBindModalAddData = false;		
		resetVueFormData(addFormName);
	});
}
/********************  修改按钮  *********************/

// 默认的初始化updateform逻辑
initUpdateForm = function (obj){
	getVueObject()[updataFormName] = obj;
	getVueObject()[updataFormModalName] = true;
	currentAction = actions.update;
}
/**
 * table 上部 修改按钮
 */
function vueBindButtonHeadUpdateMethod(){
	if (beforeUpdate) beforeUpdate();
	var _self = this;
	var checkdata = _self[tableCheckDataName];
	if(checkdata.length!=1){
		toastInfo('请选择1条记录!');
		return;
	}
	currentAction = actions.update;
	resetVueFormData(updataFormName);
	getSingleData(getVueTableCheckedDataIds(checkdata),updateBefore,function(data){initUpdateForm(data);});
}
/**
 * table row 修改按钮
 * @param index 索引
 * @param tableDataName index所在data的名称
 */
function vueBindButtonUpdateMethod(index,tableDataName) {
	if (beforeUpdate) beforeUpdate();
	var _self = this;
	getSingleData(_self[tableDataName][index].id,updateBefore,function(data){initUpdateForm(data);});
}

/**
 * 根据数据表格中复选框选中的记录Id，获取所对应的业务对象实例
 */
var getSingleData = function(idData, successCallbackBefore,successCallback, errorCallback) {
	var result = false;
	$.iposty('single', {'id':idData}, 
			function(data){
				var obj = data.obj;
				if((successCallbackBefore&&successCallbackBefore(obj)) || !successCallbackBefore) {
					if(successCallback) successCallback(obj);
					result = true;
				}
			},
			errorCallback,true
		);
	return result;
}
/**
 * 修改form提交
 * @returns
 */
function vueBindButtonHeadUpdateSubmitMethod(){
	var _self = this;
	submitFormValidate(currentAction,function(data){
		toastSuccess('更新成功!');
		_self[updataFormModalName] = false;
		resetVueFormData(updataFormName);
	});
}

/**
 * 删除
 * @param vueTableCheckedDataName
 * @param actionsType
 * @param vueBindModalDataName
 * @returns
 */
function vueBindButtonHeadDeleteMethod(){
	if (beforeDelete) beforeDelete();
	var _self = this;
	var checkData  = _self[tableCheckDataName];
	
	if(checkData.length!=1){
		toastInfo('至少选中1条记录!');
		return;
	}
	if (deleteBefore) deleteBefore(getVueTableCheckedDataIds(checkData));
	this.defaultVueBindModalDelMessageData = "即将删除"+vueTableCheckedDataLength+"条记录,是否继续删除?";
	this.defaultVueTableDelRowIdsData = getVueTableCheckedDataIds(checkData);//将要删除的id 赋值给data
	currentAction = actions.del;
	
	this.defaultVueBindModalDelData = true;
}

/**
 * table row 删除按钮
 * @param index 索引
 * @param vueBindTableDataDataName index所在data的名称
 * @returns
 */
function vueBindButtonDeleteMethod (index,vueBindTableDataDataName) {
	if (beforeDelete) beforeDelete();
	if (deleteBefore) deleteBefore(''+getVueObject()[vueBindTableDataDataName][index].id);

	getVueObject().defaultVueBindModalDelMessageData = "是否继续删除此条记录?";
	getVueObject().defaultVueTableDelRowIdsData = ''+getVueObject()[vueBindTableDataDataName][index].id;
	getVueObject().defaultVueBindModalDelData = true;// 显示删除界面
	currentAction = actions.del;
}

/**
 * 删除提交
 */
function vueBindButtonHeadDeleteSubmitMethod (){
	var _self = this;
	_self.modalDelSubmitLoading = true;
	submitForm(currentAction,_self.defaultVueTableDelRowIdsData,
		function(data){
			toastSuccess('删除成功');
			_self.defaultVueBindModalDelLoadingData = false;
			_self.defaultVueBindModalDelData = false;
		},
		function(errorMessage){
			toastError(errorMessage);
			_self.defaultVueBindModalDelLoadingData = false;
		}
	);
}

/**
 * 综合 查询 按钮
 * @param vueBindTableDataDataName
 * @param vueBindPageTotalDataName
 * @param vueBindPageSizeDataName
 * @param vueBindPageCurrentDataName
 * @param vueTableCheckedDataName
 * @param vueBindFormQueryDataName
 * @returns
 */ 
function vueBindButtonClickQueryMethod(vueBindTableDataDataName,vueBindPageTotalDataName,vueBindPageSizeDataName,vueBindPageCurrentDataName,vueTableCheckedDataName,vueBindFormQueryDataName){
	this.vueTableLoadPageMethod(vueBindTableDataDataName,vueBindPageTotalDataName,vueBindPageSizeDataName,vueBindPageCurrentDataName,vueTableCheckedDataName,vueBindFormQueryDataName);
}

/**
 * 获得当前vue form data
 * @returns
 */ 
function getCurrentVueFormData() {
	return (!currentAction || !vueContentObject) ? null : currentAction.key == actions.add.key ? getVueObject()['defaultVueBindFormAddData'] : getVueObject()[updataFormName];
}

/**
 * 获得当前vue form data name
 * @returns
 */ 
function getCurrentVueFormDataName() {
	return (!currentAction || !vueContentObject) ? null : currentAction.key == actions.add.key ? addFormName : updataFormName;
}

/**
 * 重置 form data
 * @param formDataName
 * @returns
 */ 
function resetVueFormData (vueFormDataName) {
	getVueRefObject(vueFormDataName).resetFields();
}



/**
 * form 验证 callback"
 * @param callback
 * @param isSuccess
 * @param errorMessage
 * @returns
 */
function formValidateCallback(callback,isSuccess,errorMessage) {
	if (!isSuccess) callback(new Error(errorMessage));
	else callback();
}

/**
 * vue form rules  anys 通用校验方法
 * @param rule 验证规则
 * @param value 值
 * @param callback 验证结果 
 */
function vueFormRulesCommonValidate (rule,value,callback) {
	var form = getCurrentVueFormData();
	
	if(rule.equal){
		var equalValue = form[rule.equal];
		if (value === '') {
			formValidateCallback(callback,false);
		} else if (value !== equalValue) {
			formValidateCallback(callback,false);
		} else {
			formValidateCallback(callback,true);
	    }
	}else if(rule.unique){
		var url = rule.unique;
		$.iposty(url,{'data':value,'id':form.id}, function(data) {
			formValidateCallback(callback,data.isUnique);
	    });
	}else if(rule.otherValidate){
		var filedName= rule.otherValidate;
		if (form[filedName] !== '')
        	getVueRefObject(getCurrentVueFormDataName()).validateField(filedName);
        formValidateCallback(callback,true);
	}
}

/**
 * 提交表单
 * @param currentAction
 * @param data
 * @param callback
 * @param errorCallback
 * @param vueBindTableDataDataName
 * @param vueBindPageTotalDataName
 * @param vueBindPageSizeDataName
 * @param vueBindPageCurrentDataName
 * @param vueTableCheckedDataName
 * @param vueBindFormQueryDataName
 * @returns
 */
function submitForm(currentAction,data, callback,errorCallback,vueBindTableDataDataName,vueBindPageTotalDataName,vueBindPageSizeDataName,vueBindPageCurrentDataName,vueTableCheckedDataName,vueBindFormQueryDataName) {
	
	// 包装请求后 回调函数.data 请求成功后 后台返回的值
	var successCallback = function(data){
		//
//		fresh4NewData(data,function(){callback();});
		fresh4NewData(data,function(){callback();},vueBindTableDataDataName,vueBindPageTotalDataName,vueBindPageSizeDataName,vueBindPageCurrentDataName,vueTableCheckedDataName,vueBindFormQueryDataName);
	}
    if(currentAction.key == actions.del.key) $.idel(currentAction.url,data,successCallback,errorCallback);
	else $.iposty(currentAction.url, data, successCallback,errorCallback);
}

/**
 * form验证并提交
 * @param currentAction
 * @param successCallback
 * @param errorCallback
 * @param vueBindTableDataDataName
 * @param vueBindPageTotalDataName
 * @param vueBindPageSizeDataName
 * @param vueBindPageCurrentDataName
 * @param vueTableCheckedDataName
 * @param vueBindFormQueryDataName
 * @returns
 */
function submitFormValidate(currentAction,successCallback,errorCallback,vueBindTableDataDataName,vueBindPageTotalDataName,vueBindPageSizeDataName,vueBindPageCurrentDataName,vueTableCheckedDataName,vueBindFormQueryDataName){
	getVueRefObject(getCurrentVueFormDataName()).validate((valid) => {
		if (valid) submitForm(currentAction,getCurrentVueFormData(),successCallback,errorCallback,vueBindTableDataDataName,vueBindPageTotalDataName,vueBindPageSizeDataName,vueBindPageCurrentDataName,vueTableCheckedDataName,vueBindFormQueryDataName);
		else toastError('表单验证失败!');
	})
};

/**
 * 格式化查询form的数据 加“\“ \”” 后台需要
 * @param vueBindFormQueryData query form data
 * @returns
 */
function formatQueryFormData(vueBindFormQueryData){
	var queryObj ={};
	for ( var i in vueBindFormQueryData ){
	    queryObj[i] = '"'+vueBindFormQueryData[i]+'"';
	}
	return queryObj;
}

/**
 * 生成综合查询form
 * @param vueBindFormQueryDataName - form 绑定的data名
 * @param queryFormItemName - form item label 数组
 * @param queryFormItemKey - form item 在data中的属性名
 * @param queryFormItemType - form item 的类型，如果是select 需要写成特定格式 select:option项的data
 * 
 */
function createTableQueryFrom(vueBindFormQueryDataName,queryFormItemName,queryFormItemKey,queryFormItemType){
	var icolSpan = 24/queryFromRowItemNum-1;//24栅格
	var totalRow = parseInt(queryFormItemKey.length/queryFromRowItemNum);
	if(queryFormItemKey.length%queryFromRowItemNum!=0) totalRow ++;
	
	var itemIndex = 0;
	var queryForm = '<i-form ref="'+vueBindFormQueryDataName+'" :model="'+vueBindFormQueryDataName+'"  :show-message="false" label-position="left" :label-width="'+queryFormItemWidth+'" >';
	//行
	for(var rowIndex = 0;rowIndex<totalRow;rowIndex++){
		queryForm+='<Row type="flex" justify="space-between" >';
		//项
		for(var rowItemIndex = 0;rowItemIndex<queryFromRowItemNum;rowItemIndex++){
			if(itemIndex == queryFormItemKey.length) break;
			queryForm+='<i-col span="'+icolSpan+'">';
			
			var itemTypeArray = queryFormItemType[itemIndex].split('#');
			var itemType = itemTypeArray[0];
			
			if(itemType=='string'){
				queryForm+='<Form-item label="'+queryFormItemName[itemIndex]+'：" prop="'+queryFormItemKey[itemIndex]+'">';
				queryForm+='<i-input v-model="'+vueBindFormQueryDataName+'.'+queryFormItemKey[itemIndex]+'" ></i-input>';
				queryForm+='</Form-item>';
			}else if(itemType.indexOf('number')>=0){
				queryForm+='<Form-item label="'+queryFormItemName[itemIndex]+'：" prop="'+queryFormItemKey[itemIndex]+'">';
				var rangeStr = '';
				if(itemType.indexOf('<number')>=0){
					var minIndex = itemType.indexOf('<number');
					rangeStr+=' :min="'+itemType.substring(0,minIndex)+'"';
				}
				if(itemType.indexOf('number<')>=0){
					var maxIndex = itemType.lastIndexOf('<');
					rangeStr+=' :max="'+itemType.substring(maxIndex+1)+'"';
				}
				
				queryForm+='<Input-number '+rangeStr+' v-model="'+vueBindFormQueryDataName+'.'+queryFormItemKey[itemIndex]+'"></Input-number>';
				queryForm+='</Form-item>';
				
			}else if(itemType=='select'){
				queryForm+='<Form-item label="'+queryFormItemName[itemIndex]+'：" prop="'+queryFormItemKey[itemIndex]+'">';
				queryForm+='<i-select v-model="'+vueBindFormQueryDataName+'.'+queryFormItemKey[itemIndex]+'" clearable>';
				queryForm+='<i-option v-for="item in '+itemTypeArray[1]+'" :value="item.value" :key="item">{{ item.label }}</i-option>';
				queryForm+='</i-select>';
				queryForm+='</Form-item>';
			}else if(itemType.startsWith("date")){
				queryForm+='<Form-item label="'+queryFormItemName[itemIndex]+'：" prop="'+queryFormItemKey[itemIndex]+'">';
				queryForm+='<Date-picker placement="bottom" v-model="'+vueBindFormQueryDataName+'.'+queryFormItemKey[itemIndex]+'" type="'+itemType+'"></Date-picker>';
				queryForm+='</Form-item>';
			}
			
			queryForm+='</i-col>';
			itemIndex++;
		}
		queryForm+='</Row>';
	}
	queryForm+='<Row type="flex" justify="center" >';
	queryForm+='<i-col span="'+icolSpan+'">';
	queryForm+='<i-button icon="ios-search"  @click="'+querySubmitButtonName+'()" >查询</i-button>';
	queryForm+='<i-button icon="ios-search"  @click="resetVueFormData(\''+vueBindFormQueryDataName+'\')"  style="margin-left: 8px">重置</i-button>';
	queryForm+='</i-col>';
	queryForm+='</Row>';
	queryForm+='</i-form>';
	return queryForm;
}

/**
 * 设置 生成form 标签代码 到指定的id 上
 * @param vueBindFormQueryDataName query form data name
 * @param queryFormItemName 
 * @param queryFormItemKey
 * @param queryFormItemType
 * @param queryFormDomId
 * @returns
 */
function setVueFormTemplate(vueBindFormQueryDataName,queryFormItemName,queryFormItemKey,queryFormItemType,queryFormDomId){
	document.getElementById(queryFormDomId).innerHTML = createTableQueryFrom(vueBindFormQueryDataName,queryFormItemName,queryFormItemKey,queryFormItemType);
}

/**
 * 设置 query from data
 * @param vueBindFormQueryDataValue
 * @param vueBindFormQueryData
 * @returns
 */
function setVueBindFormQueryData(vueBindFormQueryDataValue,vueBindFormQueryData){
	if(!vueBindFormQueryData) defaultVueBindFormQueryData = vueBindFormQueryDataValue;
	else vueBindFormQueryData = vueBindFormQueryDataValue;
}

/**
 * 生成 query from data
 * @param vueBindFormQueryDataValue
 * @param vueBindFormQueryData
 * @returns
 */
function createVueBindFormQueryData(queryFormItemKey){
	var queryformdata = {};
	for(var i in queryFormItemKey){
		queryformdata[queryFormItemKey[i]] = '';
	}
	return queryformdata;
}

/**
 * 设置 add update form model data 
 * @param vueBindFormModelDataValue 
 * @param vueBindFormModelData vue data object
 * @returns
 */
function setVueBindFormModelData(vueBindFormModelDataValue,vueBindFormModelData){
	if(!vueBindFormModelData) defaultVueBindFormAddData = defaultVueBindFormUpdateData = vueBindFormModelDataValue;
	else vueBindFormModelData = vueBindFormModelDataValue;//可修改为vueBindFormModelData数组赋值
}


/**
 * 设置form 验证规则 data
 * @param vueBindFormRulesDataValue vue data 的value
 * @param vueBindFormRulesData vue data object
 * @returns
 */
function setVueBindFormRulesData(vueBindFormRulesDataValue,vueBindFormRulesData){
	if(!vueBindFormRulesData) {
		defaultVueBindFormRulesAddData = defaultVueBindFormRulesUpdateData = vueBindFormRulesDataValue;
	} else {
		// 此处 可修改为 vueBindFormRulesData数组 循环赋值
		vueBindFormRulesData = vueBindFormRulesDataValue;
	}
}
