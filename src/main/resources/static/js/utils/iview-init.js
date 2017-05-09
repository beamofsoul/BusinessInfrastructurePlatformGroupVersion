var vueContentBeforeCreate = function(){};
var vueContentCreated = function(){};
var vueContentBeforeMount = function(){};
var vueContentMounted = function(){};
var vueContentBeforeUpdate = function(){};
var vueContentUpdated = function(){};
var vueContentBeforeDestroy = function(){};
var vueContentDestroyed = function(){};
var vueContentMethods = {};
var vueContentData = function() {};




var vueContentElementSelector = '#contentContainer';

var tableColumnDatas;
var parseValuesOnTableEachRow = function() {};

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

var addFormName = 'addForm';
var updateFormName = 'updateForm';
var queryFormName = 'queryForm';

var loadPageableDataUrl;//加载分页业务数据用的URL
var currentAction = null;// 当前用户操作的行为 - add、update、delete 等

//##########################################################//
vueContentData = function() {
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
vueContentMethods = {
		
	loadPage:loadPageFn,
	changePage:changePageFn,
	
	addButton:addButtonFn,
	submitAdd:submitAddFn,
	
	updateButton:updateButtonFn,
	submitUpdate:submitUpdateFn,
	
	deleteButton:deleteButtonFn,
    submitDelete:submitDeleteFn,
	
	querySubmit:querySubmitFn,
	
	tableCheckboxSelectedData:tableCheckboxSelectedDataFn,
    
    rowUpdateButton:rowUpdateButtonFn,
    rowDeleteButton:rowDeleteButtonFn
    
}

function initializeContentOptions() {
	return {el: vueContentElementSelector, data: vueContentData, methods: vueContentMethods, beforeCreate: vueContentBeforeCreate, created: vueContentCreated, beforeMount: vueContentBeforeMount, mounted: vueContentMounted, beforeUpdate: vueContentBeforeUpdate, updated: vueContentUpdated, beforeDestroy: vueContentBeforeDestroy, destroyed: vueContentDestroyed};
}
//^^^^^^^^^^^^^^^^^^^^^^ 格式 顺序 不动 ^^^^^^^^^^^^^^^^^^^^^//