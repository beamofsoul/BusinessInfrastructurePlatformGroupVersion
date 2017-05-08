var addFormName = 'addForm';
var updateFormName = 'updateForm';
var queryFormName = 'queryForm';

// 重置form
function formDataReset (formDataName) {
  vueContentObject.$refs[formDataName].resetFields();
}

// form验证并提交
function submitFormValidate(currentAction,successCallback,errorCallback){
	var formName = getCurrentFormName();
	var form = getCurrentForm();
	
	vueContentObject.$refs[formName].validate((valid) => {
		if (valid) submitForm(currentAction,form,successCallback,errorCallback);
		else vueContentObject.$Message.error('表单验证失败!');
	})
};


//提交表单
function submitForm(currentAction,data, callback,errorCallback) {
	// 包装请求后 回调函数.data 请求成功后 后台返回的值
	var successCallback = function(data){
		fresh4NewData(data,function(){callback();});
	};
    if(currentAction.key == actions.del.key){
    	$.del(currentAction.url,data,successCallback);
    } else {
    	$.iposty(currentAction.url, data, successCallback,errorCallback);
    }
}

//当用户操作完增加、修改或复制记录后对tableContainer中的数据进行更新
function fresh4NewData(data,callback) {
	// 暂时先请求后台 来重新加载数据
	vueContentObject.loadPage();
	callback();
}

//格式化查询form的数据 加“\“ \”” 后台需要
function formatQueryFormData(vueObj){
	var queryObj ={};
	for ( var i in vueObj.queryForm ){
		queryObj[i] = vueObj.queryForm[i];
	    queryObj[i] = '"'+queryObj[i]+'"';
	}
	return queryObj;
}


function getCurrentForm() {
	return (!currentAction || !vueContentObject) ? null : currentAction.key == actions.add.key ? vueContentObject.addForm : vueContentObject.updateForm;
}
function getCurrentFormName() {
	return (!currentAction || !vueContentObject) ? null : currentAction.key == actions.add.key ? addFormName : updateFormName;
}