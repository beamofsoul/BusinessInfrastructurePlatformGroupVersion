/**
 * 获取 vue object
 * @param refName
 * @returns
 */
function getVueObject() {
	return vueContentObject;
}
 
/**
 * 获取指定 vue ref name 对象
 * @param refName
 * @returns
 */
function getVueRefObject(refName) {
	return getVueObject().$refs[refName];
}

/**
 * vue 生命周期 
 * @param beforeCreateFunction
 * @returns
 */
function setVueContentBeforeCreateFunction (beforeCreateFunction){
	vueContentBeforeCreate = beforeCreateFunction;
}

function setVueContentCreatedFunction (createdFunction){
	vueContentCreated = createdFunction;
}
function setVueContentMountedFunction (mountedFunction){
	vueContentMounted = mountedFunction;
}

/**
 * 全局显示吐司消息
 * @param content 内容
 * @param duration 指定时间后关闭
 * @param onClose callback
 * @returns
 */
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

//////////////////////////////////////////////////////////////////
// new Vue() 之前  默认方法 加载默认的控件用
var beforeNewVueDefaultFunction = function(){};
// new Vue() 之前  自定义方法 可覆盖默认方法中的方法
var beforeNewVueFunction = function(){};
// vue 生命周期 beforeCreate（创建前）,created（创建后）,beforeMount(载入前),mounted（载入后）,beforeUpdate（更新前）,updated（更新后）,beforeDestroy（销毁前）,destroyed（销毁后）
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
        defaultVueBindFormRulesUpdateData:defaultVueBindFormRulesUpdateData,
        
        customVueData:{},
        
    	self: this
    }
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

beforeNewVueDefaultFunction = function (){
	
	if(tableColumnsName&&tableColumnsKey) {
		//根据用户定义的数据 设置table columns data
		setVueTableColumnsData(tableColumnsName,tableColumnsKey,tableButtonsOnEachRow);
		//设置 vue 生命周期 Mounted 时 调用table读取页数据
//		setVueContentMountedFunction(function () {this.vueTableLoadPageMethod()});
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
	if(beforeNewVueDefaultFunction) beforeNewVueDefaultFunction();//new vue 前 默认的方法
	if(beforeNewVueFunction) beforeNewVueFunction();//new vue 前 自定义方法 可在此中 覆盖上面的beforeNewVueDefaultFunction中方法
	return {el: vueContentElementSelector, data: vueContentData, methods: vueContentMethods, beforeCreate: vueContentBeforeCreate, created: vueContentCreated, beforeMount: vueContentBeforeMount, mounted: vueContentMounted, beforeUpdate: vueContentBeforeUpdate, updated: vueContentUpdated, beforeDestroy: vueContentBeforeDestroy, destroyed: vueContentDestroyed};
}
