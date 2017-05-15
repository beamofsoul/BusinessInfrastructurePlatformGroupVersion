var defaultVueBindFormAddData;
var defaultVueBindFormUpdateData;
var defaultVueBindFormQueryData;

var defaultVueBindFormRulesAddData;
var defaultVueBindFormRulesUpdateData;

var defaultVueBindFormAddDataName = 'defaultVueBindFormAddData';
var defaultVueBindFormUpdateDataName = 'defaultVueBindFormUpdateData';
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
/**
 * 添加
 * @param vueBindFormAddDataName
 * @param actionsType
 * @param vueBindModalDataName
 * @returns
 */
function vueBindButtonHeadAddMethod (vueBindFormAddDataName,actionsType,vueBindModalDataName){
	if(!vueBindFormAddDataName) vueBindFormAddDataName = defaultVueBindFormAddDataName;
	resetVueFormData(vueBindFormAddDataName);
	if(!actionsType) currentAction = actions.add;
	else currentAction = actions[actionsType];
	if(!vueBindModalDataName) this.defaultVueBindModalAddData = true;
	else this[vueBindModalDataName] = true;
}
function vueBindButtonHeadAddSubmitMethod (vueBindFormAddDataName,vueBindModalDataName) {
	var _self = this;
	console.log(123123)
	submitFormValidate(currentAction,function(data){
		toastSuccess('提交成功!');
		if(!vueBindModalDataName) _self.defaultVueBindModalAddData = false;
		else _self[vueBindModalDataName] = false;
		
		if(!vueBindFormAddDataName) vueBindFormAddDataName = defaultVueBindFormAddDataName;
		resetVueFormData(vueBindFormAddDataName);
	});
}

/**
 * 修改
 * @param vueTableCheckedDataName
 * @param vueBindFormUpdateDataName
 * @param actionsType
 * @param vueBindModalDataName
 * @returns
 */
function vueBindButtonHeadUpdateMethod(vueTableCheckedDataName,vueBindFormUpdateDataName,actionsType,vueBindModalDataName){
	var _self = this;
	var vueTableCheckedDataLength = 0;
	if(!vueTableCheckedDataName) vueTableCheckedDataName = 'defaultVueTableCheckedData';
	vueTableCheckedDataLength = _self[vueTableCheckedDataName].length;
	if(vueTableCheckedDataLength!=1){
		toastInfo('请选择1条记录!');
		return;
	}
	if(!actionsType) currentAction = actions.update;
	else currentAction = actions[actionsType];
	if(!vueBindModalDataName) this.defaultVueBindModalUpdateData = true;
	else this[vueBindModalDataName] = true;
	if(!vueBindFormUpdateDataName) vueBindFormUpdateDataName = defaultVueBindFormUpdateDataName;
	resetVueFormData(vueBindFormUpdateDataName);
	
		
	$.iposty('single', {'id':getVueTableCheckedDataIds(_self[vueTableCheckedDataName])}, function(data){
		_self[vueBindFormUpdateDataName] = data.obj;
		});
}

function vueBindButtonHeadUpdateSubmitMethod(vueTableCheckedDataName,vueBindFormUpdateDataName,vueBindModalDataName){
	var _self = this;
	submitFormValidate(currentAction,function(data){
		toastSuccess('更新成功!');
		if(!vueBindModalDataName) _self.defaultVueBindModalUpdateData = false;
		else _self[vueBindModalDataName] = false;
		if(!vueBindFormUpdateDataName) vueBindFormUpdateDataName = defaultVueBindFormUpdateDataName;
		resetVueFormData(vueBindFormUpdateDataName);
	});
}

/**
 * 删除
 * @param vueTableCheckedDataName
 * @param actionsType
 * @param vueBindModalDataName
 * @returns
 */
function vueBindButtonHeadDeleteMethod(vueTableCheckedDataName,actionsType,vueBindModalDataName){
	var _self = this;
	var vueTableCheckedDataLength = 0;
	if(!vueTableCheckedDataName) vueTableCheckedDataName = 'defaultVueTableCheckedData';
	vueTableCheckedDataLength = _self[vueTableCheckedDataName].length;
	if(vueTableCheckedDataLength!=1){
		toastInfo('至少选中1条记录!');
		return;
	}
	this.defaultVueBindModalDelMessageData = "即将删除"+vueTableCheckedDataLength+"条记录,是否继续删除?";
	this.defaultVueTableDelRowIdsData = getVueTableCheckedDataIds(_self[vueTableCheckedDataName]);//将要删除的id 赋值给data
	if(!actionsType) currentAction = actions.del;
	else currentAction = actions[actionsType];	
	if(!vueBindModalDataName) this.defaultVueBindModalDelData = true;
	else this[vueBindModalDataName] = true;
}
function vueBindButtonHeadDeleteSubmitMethod (vueTableCheckedDataName,vueBindModalDataName){
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
	return (!currentAction || !vueContentObject) ? null : currentAction.key == actions.add.key ? getVueObject()['defaultVueBindFormAddData'] : getVueObject()['defaultVueBindFormUpdateData'];
}

/**
 * 获得当前vue form data name
 * @returns
 */ 
function getCurrentVueFormDataName() {
	return (!currentAction || !vueContentObject) ? null : currentAction.key == actions.add.key ? defaultVueBindFormAddDataName : defaultVueBindFormUpdateDataName;
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
	console.log(form);
	
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
	
	console.log(vueBindFormQueryDataName)
	console.log(queryFormItemName)
	console.log(queryFormItemKey)
	console.log(queryFormItemType)
	
	var icolSpan = 24/queryFromRowItemNum-1;//24栅格
	var totalRow = parseInt(queryFormItemKey.length/queryFromRowItemNum);
	if(queryFormItemKey.length%queryFromRowItemNum!=0) totalRow ++;
	
	var itemIndex = 0;
	var queryForm = '<i-form ref="'+vueBindFormQueryDataName+'" :model="'+vueBindFormQueryDataName+'"  :show-message="false" label-position="left" :label-width="'+queryFormItemWidth+'" >';
	//行
	for(var rowIndex = 0;rowIndex<totalRow;rowIndex++){
		console.log('row index '+rowIndex);
		console.log('row index totalRow '+totalRow);
		queryForm+='<Row type="flex" justify="space-between" >';
		//项
		for(var rowItemIndex = 0;rowItemIndex<queryFromRowItemNum;rowItemIndex++){
			console.log(itemIndex);
		
			if(itemIndex == queryFormItemKey.length) break;
			
			queryForm+='<i-col span="'+icolSpan+'">';
			
			var itemTypeArray = queryFormItemType[itemIndex].split('#');
			var itemType = itemTypeArray[0];
			
			if(itemType=='string'){
				queryForm+='<Form-item label="'+queryFormItemName[itemIndex]+'：" prop="'+queryFormItemKey[itemIndex]+'">';
				queryForm+='<i-input v-model="'+vueBindFormQueryDataName+'.'+queryFormItemKey[itemIndex]+'" ></i-input>';
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
//	console.log(queryformdata);
//	console.log(JSON.stringify(queryformdata))
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
