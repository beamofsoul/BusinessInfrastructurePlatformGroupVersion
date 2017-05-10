var addFormContent;
var updateFormContent;
var queryFormContent;
var addFormValidateContent;
var updateFormValidateContent;

var addFormName = 'addForm';
var updateFormName = 'updateForm';
var queryFormName = 'queryForm';

var modalAdd = false;//添加form 对话框 显示状态
var modalUpdate = false;//修改form 对话框 显示状态
var modalDelSubmitLoading = false;//删除form 对话框 按钮状态
var modalDel = false;//删除 对话框 显示状态

var currentAction = null;// 当前用户操作的行为 - add、update、delete 等

var queryFromRowItemNum = 4;//综合查询 每行放控件的数量
var queryFormItemWidth = 80;//综合查询 控件的宽度 像素
var querySubmitButtonName = 'querySubmit';//综合查询 提交按钮触发函数名

// 获得当前form
function getCurrentForm() {
	return (!currentAction || !vueContentObject) ? null : currentAction.key == actions.add.key ? getVueObject().addForm : getVueObject().updateForm;
}

// 获得当前form name
function getCurrentFormName() {
	return (!currentAction || !vueContentObject) ? null : currentAction.key == actions.add.key ? addFormName : updateFormName;
}

// 重置 form data
function formDataReset (formDataName) {
	getVueRefObject(formDataName).resetFields();
}

// form验证并提交
function submitFormValidate(currentAction,successCallback,errorCallback){
	getVueRefObject(getCurrentFormName()).validate((valid) => {
		if (valid) submitForm(currentAction,getCurrentForm(),successCallback,errorCallback);
		else toastError('表单验证失败!');
	})
};

//form 验证 callback
function formValidateCallback(callback,isSuccess,errorMessage) {
	if (!isSuccess) callback(new Error(errorMessage));
	else callback();
}

// anys 校验方法
function validateFunction (rule,value,callback) {
	var form = getCurrentForm();
	
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
        	getVueRefObject(getCurrentFormName()).validateField(filedName);
        formValidateCallback(callback,true);
	}
}

// 提交表单
function submitForm(currentAction,data, callback,errorCallback) {
	// 包装请求后 回调函数.data 请求成功后 后台返回的值
	var successCallback = function(data){
		fresh4NewData(data,function(){callback();});
	}
    if(currentAction.key == actions.del.key) $.idel(currentAction.url,data,successCallback,errorCallback);
	else $.iposty(currentAction.url, data, successCallback,errorCallback);
}

//格式化查询form的数据 加“\“ \”” 后台需要
function formatQueryFormData(queryForm){
	var queryObj ={};
	for ( var i in queryForm ){
	    queryObj[i] = '"'+queryForm[i]+'"';
	}
	return queryObj;
}

/**
 * 生成综合查询form
 * @param iformName - form 绑定的data名
 * @param queryFormItemName - form item label 数组
 * @param queryFormItems - form item 在data中的属性名
 * @param queryFormItemType - form item 的类型，如果是select 需要写成特定格式 select:option项的data
 * 
 */
function createTableQueryFrom(queryFormName,queryFormItemName,queryFormItems,queryFormItemType){
	var icolSpan = 24/queryFromRowItemNum-1;//24栅格
	var totalRow = queryFormItems.length/queryFromRowItemNum;
	if(queryFormItems.length%queryFromRowItemNum!=0) totalRow ++;
	
	var itemIndex = 0;
	var queryForm = '<i-form ref="'+queryFormName+'" :model="'+queryFormName+'"  :show-message="false" label-position="left" :label-width="'+queryFormItemWidth+'" >';
	//行
	for(var rowIndex = 0;rowIndex<totalRow;rowIndex++){
		queryForm+='<Row type="flex" justify="space-between" >';
		//项
		for(var rowItemIndex = 0;rowItemIndex<queryFromRowItemNum;rowItemIndex++){
			queryForm+='<i-col span="'+icolSpan+'">';
			var itemTypeArray = queryFormItemType[itemIndex].split('#');
			var itemType = itemTypeArray[0];
			
			if(itemType=='string'){
				queryForm+='<Form-item label="'+queryFormItemName[itemIndex]+'：" prop="'+queryFormItems[itemIndex]+'">';
				queryForm+='<i-input v-model="'+queryFormName+'.'+queryFormItems[itemIndex]+'" ></i-input>';
				queryForm+='</Form-item>';
			}else if(itemType=='select'){
				queryForm+='<Form-item label="'+queryFormItemName[itemIndex]+'：" prop="'+queryFormItems[itemIndex]+'">';
				queryForm+='<i-select v-model="'+queryFormName+'.'+queryFormItems[itemIndex]+'" clearable>';
				queryForm+='<i-option v-for="item in '+itemTypeArray[1]+'" :value="item.value" :key="item">{{ item.label }}</i-option>';
				queryForm+='</i-select>';
				queryForm+='</Form-item>';
			}else if(itemType.startsWith("date")){
				queryForm+='<Form-item label="'+queryFormItemName[itemIndex]+'：" prop="'+queryFormItems[itemIndex]+'">';
				queryForm+='<Date-picker placement="bottom" v-model="'+queryFormName+'.'+queryFormItems[itemIndex]+'" type="'+itemType+'"></Date-picker>';
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
	queryForm+='<i-button icon="ios-search"  @click="formDataReset(\''+queryFormName+'\')"  style="margin-left: 8px">重置</i-button>';
	queryForm+='</i-col>';
	queryForm+='</Row>';
	queryForm+='</i-form>';
	return queryForm;
}
