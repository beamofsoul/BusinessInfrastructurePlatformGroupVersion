var tableColumnDatas;

var addFormContent;
var updateFormContent;
var queryFormContent;
var addFormValidateContent;
var updateFormValidateContent;

var pageTotal = 0;
var pageCurrent = 1;
var pageSize = 10;

var modalAdd = false;
var modalUpdate = false;
var modalDelSubmitLoading = false;
var modalDel = false;

var vueContentElementSelector = '#contentContainer';
var vueContentData = function() {
	return {
    	pageTotal: pageTotal,
		pageCurrent: pageCurrent,
		pageSize: pageSize,
	    	
		modalAdd: modalAdd,
		modalUpdate: modalUpdate,
		modalDel: modalDel,
		
		modalDelSubmitLoading: modalDelSubmitLoading,
		modalDelMessage: '',
		modalDelRowIds: '',
	        	
	    tableColumns: tableColumnDatas,
	    tableData: [],
	    tableCheckedData: [],
            
        addForm: addFormContent,
        addFormValidate: addFormValidateContent,
        updateForm: updateFormContent,
        updateFormValidate: updateFormValidateContent,
        queryForm: queryFormContent,
        
    	self: this
    }
};

var vueContentBeforeCreate = function(){};
var vueContentCreated = function(){};
var vueContentBeforeMount = function(){};
var vueContentMounted = function(){};
var vueContentBeforeUpdate = function(){};
var vueContentUpdated = function(){};
var vueContentBeforeDestroy = function(){};
var vueContentDestroyed = function(){};
var vueContentMethods = {};

function initializeContentOptions() {
	return {el: vueContentElementSelector, data: vueContentData, methods: vueContentMethods, beforeCreate: vueContentBeforeCreate, created: vueContentCreated, beforeMount: vueContentBeforeMount, mounted: vueContentMounted, beforeUpdate: vueContentBeforeUpdate, updated: vueContentUpdated, beforeDestroy: vueContentBeforeDestroy, destroyed: vueContentDestroyed};
}

//取出table选中checkbox的所有记录id
function getTableCheckedDataIds(tableCheckedData) {
	var ids = '';
	for (var i in tableCheckedData){
		ids+=tableCheckedData[i].id+",";
	}
	return (ids == '' ? ids : ids.substring(0, ids.length - 1));
}
//清空当页选中的table中checkbox
function clearTableCheckedData(){
	vueContentObject.tableCheckedData = [];
}

//格式化服务端返回的table数据
function formatTableData(data){
	var value = [];
	for(var i=0;i<data.pageableData.numberOfElements;i++) {
		value[i] = parseValuesOnEachRow(data.pageableData.content[i]);
	}
	return value;
}


