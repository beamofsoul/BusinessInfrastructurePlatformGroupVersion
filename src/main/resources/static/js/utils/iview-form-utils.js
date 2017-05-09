// 获得当前form
function getCurrentForm() {
	return (!currentAction || !vueContentObject) ? null : currentAction.key == actions.add.key ? vueContentObject.addForm : vueContentObject.updateForm;
}

// 获得当前form name
function getCurrentFormName() {
	return (!currentAction || !vueContentObject) ? null : currentAction.key == actions.add.key ? addFormName : updateFormName;
}

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

// 提交表单
function submitForm(currentAction,data, callback,errorCallback) {
	// 包装请求后 回调函数.data 请求成功后 后台返回的值
	var successCallback = function(data){
		fresh4NewData(data,function(){callback();});
	};
    if(currentAction.key == actions.del.key){
    	$.idel(currentAction.url,data,successCallback,errorCallback);
    } else {
    	$.iposty(currentAction.url, data, successCallback,errorCallback);
    }
}

// 格式化查询form的数据 加“\“ \”” 后台需要
function formatQueryFormData(vueObj){
	var queryObj ={};
	for ( var i in vueObj.queryForm ){
		queryObj[i] = vueObj.queryForm[i];
	    queryObj[i] = '"'+queryObj[i]+'"';
	}
	return queryObj;
}

/**
 * 生成综合查询form
 * @param iformName - form 绑定的data名称
 * @param queryFormItemName - form item label 数组
 * @param queryFormItems - form item 在data中的属性名
 * @param queryFormItemType - form item 的类型，如果是select 需要写成特定格式 select:option项的data
 * 
 */
function createTableQueryFrom(iformName,queryFormItemName,queryFormItems,queryFormItemType){
	var rowItemNum = 4;//每行放的个数
	var icolSpan = 24/rowItemNum-1;//24栅格
	var totalRow = queryFormItems.length/rowItemNum;
	if(queryFormItems.length%rowItemNum!=0){
		totalRow ++;
	}
	
	var itemIndex = 0;
	var queryForm = '<i-form ref="'+iformName+'" :model="'+iformName+'"  :show-message="false" label-position="left" :label-width="80" >';
	//行
	for(var rowIndex = 0;rowIndex<totalRow;rowIndex++){
		queryForm+='<Row type="flex" justify="space-between" >';
		//项
		for(var rowItemIndex = 0;rowItemIndex<rowItemNum;rowItemIndex++){
			queryForm+='<i-col span="'+icolSpan+'">';
			if(queryFormItemType[itemIndex]=='input'){
				queryForm+='<Form-item label="'+queryFormItemName[itemIndex]+'：" prop="'+queryFormItems[itemIndex]+'">';
				queryForm+='<i-input v-model="'+iformName+'.'+queryFormItems[itemIndex]+'" ></i-input>';
				queryForm+='</Form-item>';
			}else if(queryFormItemType[itemIndex].indexOf(':')!=-1){
				queryForm+='<Form-item label="'+queryFormItemName[itemIndex]+'：" prop="'+queryFormItems[itemIndex]+'">';
				queryForm+='<i-select v-model="'+iformName+'.'+queryFormItems[itemIndex]+'" clearable>';
				queryForm+='<i-option v-for="item in '+queryFormItemType[itemIndex].slice(queryFormItemType[itemIndex].indexOf(':')+1)+'" :value="item.value" :key="item">{{ item.label }}</i-option>';
				queryForm+='</i-select>';
				queryForm+='</Form-item>';
			}else if(queryFormItemType[itemIndex]=='date'){
				queryForm+='<Form-item label="'+queryFormItemName[itemIndex]+'：" prop="'+queryFormItems[itemIndex]+'">';
				queryForm+='<Date-picker placement="bottom" v-model="'+iformName+'.'+queryFormItems[itemIndex]+'" type="date"  ></Date-picker>';
				queryForm+='</Form-item>';
			}
			queryForm+='</i-col>';
			itemIndex++;
		}
		queryForm+='</Row>';
	}
	
	queryForm+='<Row type="flex" justify="center" >';
	queryForm+='<i-col span="'+icolSpan+'">';
	queryForm+='<i-button icon="ios-search"  @click="querySubmit()" >查询</i-button>';
	queryForm+='<i-button icon="ios-search"  @click="formReset(\''+iformName+'\')"  style="margin-left: 8px">重置</i-button>';
	queryForm+='</i-col>';
	queryForm+='</Row>';
	queryForm+='</i-form>';
	return queryForm;
}
