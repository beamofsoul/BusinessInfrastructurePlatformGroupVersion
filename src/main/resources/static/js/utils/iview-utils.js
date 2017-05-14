function getVueObject() {
	return vueContentObject;
}
 
/**
 * 获取指定vue ref name 对象
 * @param refName
 * @returns
 */
function getVueRefObject(refName) {
	return getVueObject().$refs[refName];
}

/**
 * vue 生命周期 new Vue后 第一个方法
 * @param beforeCreateFunction
 * @returns
 */
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
//new Vue() 之前 方法
var beforeNewVueFunction = function(){};
// new Vue() 之后 生命周期方法
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

var vueContentDataObject = {
	    	
		defaultVueBindModalAddData: defaultVueBindModalAddData,
		defaultVueBindModalUpdateData: defaultVueBindModalUpdateData,
		defaultVueBindModalDelData: defaultVueBindModalDelData,
		
		defaultVueBindModalDelLoadingData: defaultVueBindModalDelLoadingData,
		defaultVueBindModalDelMessageData: defaultVueBindModalDelMessageData,
		defaultVueTableDelRowIdsData:'',
		
		defaultVueBindTableColumnsData : defaultVueBindTableColumnsData,
		defaultVueBindTableDataData :[],
		defaultVueTableCheckedData: [],
		defaultVueBindPageTotalData: defaultVueBindPageTotalData,
		defaultVueBindPageCurrentData: defaultVueBindPageCurrentData,
		defaultVueBindPageSizeData: defaultVueBindPageSizeData,
        
		defaultVueBindFormAddData :defaultVueBindFormAddData,
		defaultVueBindFormUpdateData: defaultVueBindFormUpdateData,
		defaultVueBindFormQueryData: defaultVueBindFormQueryData,
		defaultVueBindCollapseQueryFormData: defaultVueBindCollapseQueryFormData,
		
        defaultVueBindFormRulesAddData:defaultVueBindFormRulesAddData,
        defaultVueBindFormRulesUpdateData:defaultVueBindFormRulesUpdateData
        
//    	self: this
    };


//var vueContentDataObject;
vueContentData = function() {
//  console.log(‘此处定义 biz 文件中 用vueContentDataObject 为 undefined’)	
//	vueContentDataObject = {
//	    	
//			defaultVueBindModalAddData: defaultVueBindModalAddData,
//			defaultVueBindModalUpdateData: defaultVueBindModalUpdateData,
//			defaultVueBindModalDelData: defaultVueBindModalDelData,
//			
//			defaultVueBindModalDelLoadingData: defaultVueBindModalDelLoadingData,
//			defaultVueBindModalDelMessageData: defaultVueBindModalDelMessageData,
//			defaultVueTableDelRowIdsData:'',
//			
//			defaultVueBindTableColumnsData : defaultVueBindTableColumnsData,
//			defaultVueBindTableDataData :[],
//			defaultVueTableCheckedData: [],
//			defaultVueBindPageTotalData: defaultVueBindPageTotalData,
//			defaultVueBindPageCurrentData: defaultVueBindPageCurrentData,
//			defaultVueBindPageSizeData: defaultVueBindPageSizeData,
//	        
//			defaultVueBindFormAddData :defaultVueBindFormAddData,
//			defaultVueBindFormUpdateData: defaultVueBindFormUpdateData,
//			defaultVueBindFormQueryData: defaultVueBindFormQueryData,
//			defaultVueBindCollapseQueryFormData: defaultVueBindCollapseQueryFormData,
//			
//	        defaultVueBindFormRulesAddData:defaultVueBindFormRulesAddData,
//	        defaultVueBindFormRulesUpdateData:defaultVueBindFormRulesUpdateData,
//	        aa:console.log(this),
//	    	self: this
//	    };
	vueContentDataObject.self = this;
	return vueContentDataObject;
	
}
vueContentMethods = {
		
	vueBindTableCheckedDataMethod:vueBindTableCheckedDataMethod,
	vueTableLoadPageMethod:vueTableLoadPageMethod,
	vueBindPageOnChangeMethod:vueBindPageOnChangeMethod,
	vueBindButtonClickQueryMethod:vueBindButtonClickQueryMethod,
	
	vueBindButtonUpdateMethod:vueBindButtonUpdateMethod,
	vueBindButtonDeleteMethod:vueBindButtonDeleteMethod,
	
	vueBindButtonHeadAddMethod:vueBindButtonHeadAddMethod,
	vueBindButtonHeadAddSubmitMethod:vueBindButtonHeadAddSubmitMethod,
	
	vueBindButtonHeadUpdateMethod:vueBindButtonHeadUpdateMethod,
	vueBindButtonHeadUpdateSubmitMethod:vueBindButtonHeadUpdateSubmitMethod,
	
	vueBindButtonHeadDeleteMethod:vueBindButtonHeadDeleteMethod,
	vueBindButtonHeadDeleteSubmitMethod:vueBindButtonHeadDeleteSubmitMethod

}

beforeNewVueFunction = function (){
	
	if(tableColumnsName&&tableColumnsKey) {
		//根据用户定义的数据 设置table columns data
		setVueTableColumnsData(tableColumnsName,tableColumnsKey,tableButtonsOnEachRow);
		
		//设置 vue 生命周期 Mounted 时 调用table读取页数据
//		setVueContentMountedFunction(function () {this.vueTableLoadPageMethod()});
		console.log('此处先注释 在biz 文件中调用')
//		setVueContentMountedFunction(function () {this.vueTableLoadPageMethod();this.uploadList = this.$refs.upload.fileList;});
		
	}
	
	if(defaultVueBindFormQueryDataName&&queryFormItemName&&queryFormItemKey&&queryFormItemType&&defaultQueryFormDomId){
		//根据用户定义的数据 生成query form 放到指定 dom id
		setVueFormTemplate(defaultVueBindFormQueryDataName,queryFormItemName,queryFormItemKey,queryFormItemType,defaultQueryFormDomId);
		//设置 query
		setVueBindFormQueryData(createVueBindFormQueryData(queryFormItemKey));
	}
	
}

// new Vue() 构造参数
function initializeContentOptions() {
	if(beforeNewVueFunction) beforeNewVueFunction();
	return {el: vueContentElementSelector, data: vueContentData, methods: vueContentMethods, beforeCreate: vueContentBeforeCreate, created: vueContentCreated, beforeMount: vueContentBeforeMount, mounted: vueContentMounted, beforeUpdate: vueContentBeforeUpdate, updated: vueContentUpdated, beforeDestroy: vueContentBeforeDestroy, destroyed: vueContentDestroyed};
}
