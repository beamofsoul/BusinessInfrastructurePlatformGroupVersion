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