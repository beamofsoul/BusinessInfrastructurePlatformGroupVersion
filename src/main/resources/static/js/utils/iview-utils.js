function getVueObject() {
	return vueContentObject;
}

//获取指定vue ref name 对象 
function getVueRefObject(refName) {
	return getVueObject().$refs[refName];
}

//
function setVueContentBeforeCreateFunction (beforeCreateFunction){
	vueContentBeforeCreate = beforeCreateFunction;
}

//
function setVueContentMountedFunction (mountedFunction){
	vueContentMounted = mountedFunction;
}


function toastError(content, duration, onClose){
	toast(content, 'error', duration, onClose);
}

function toastSuccess(content, duration, onClose){
	toast(content, 'success', duration, onClose);
}

function toastInfo(content, duration, onClose){
	toast(content, 'info', duration, onClose);
}

function toastLoading(content, duration, onClose){
	return toast(content, 'loading', duration, onClose);
}

function toastWarning(content, duration, onClose){
	toast(content, 'warning', duration, onClose);
}

function toast(content, type, duration, onClose){
	if('success' === type){
		getVueObject().$Message.success(content, duration, onClose)
	}else if('warning' === type){
		getVueObject().$Message.warning(content, duration, onClose)
	}else if('error' === type){
		getVueObject().$Message.error(content, duration, onClose)
	}else if('loading' === type){
		return getVueObject().$Message.loading(content, duration, onClose)
	}else if('info' === type){
		getVueObject().$Message.info(content, duration, onClose)
	}else{
		getVueObject().$Message.info(content, duration, onClose);
	}
}
//##########################################################//

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
	        	
	    tableColumns: tableColumnData,
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