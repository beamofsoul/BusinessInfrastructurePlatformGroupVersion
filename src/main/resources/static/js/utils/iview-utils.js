var beforeVueContentCreate = function () {};// new Vue() 之前提供默认执行的方法
var beforeVueContentCreateCustom = function () {};// new Vue() 之前可追加方法
// new Vue()生命周期  beforeCreate（创建前）,created（创建后）,beforeMount(载入前),mounted（载入后）,beforeUpdate（更新前）,updated（更新后）,beforeDestroy（销毁前）,destroyed（销毁后）
var vueContentBeforeCreate = function () {};
var vueContentCreated = function () {};
var vueContentBeforeMount = function () {};
var vueContentMounted = function () {};
var vueContentBeforeUpdate = function () {};
var vueContentUpdated = function () {};
var vueContentBeforeDestroy = function () {};
var vueContentDestroyed = function () {};

var vueContentMethods = {};
var vueContentData = function () {};

var vueContentElementSelector = '#contentContainer';

var customVueContentData = {};//自定义 vue data

vueContentData = function () {
  var defaultVueContentData = {
    vueTableColumns: vueTableColumns,
    vueTableData: vueTableData,
    vueCheckedTableRowIds: vueCheckedTableRowIds,
    vueCheckedTableRow: vueCheckedTableRow,
    vueRecordTotal: vueRecordTotal,
    vueCurrentPage: vueCurrentPage,
    vuePageSize: vuePageSize,

    vueQueryFormVisible: vueQueryFormVisible,
    vueAddModalVisible: vueAddModalVisible,
    vueUpdateModalVisible: vueUpdateModalVisible,
    vueDeleteModalVisible: vueDeleteModalVisible,
    vueDeleteProgressVisible: vueDeleteProgressVisible,
    vueDeleteMessage: vueDeleteMessage,
    vueCopyModalVisible: vueCopyModalVisible,

    vueAddForm: vueAddForm,
    vueUpdateForm: vueUpdateForm,
    vueQueryForm: vueQueryForm,
    vueCopyForm: vueCopyForm,

    vueAddFormRules: vueAddFormRules,
    vueUpdateFormRules: vueUpdateFormRules,
    vueCopyFormRules: vueCopyFormRules,

    self: this
  };
  return Object.assign({}, customVueContentData, defaultVueContentData);
};

vueContentMethods = {
  getCheckedTableRow: getCheckedTableRow,
  doLoadPage: doLoadPage,
  doPageTurning: doPageTurning,

  rowUpdateButton: rowUpdateButton,
  rowDeleteButton: rowDeleteButton,
  rowCopyButton: rowCopyButton,

  doAddButton: doAddButton,
  doUpdateButton: doUpdateButton,
  doDeleteButton: doDeleteButton,

  submitAddForm: submitAddForm,
  submitUpdateForm: submitUpdateForm,
  submitDeleteForm: submitDeleteForm,
  submitQueryForm: submitQueryForm,
  submitCopyForm: submitCopyForm
};

beforeVueContentCreate = function () {
  if(hasTable){
	  if (tableColumnsName && tableColumnsKey) {
	    createTableColumns(tableColumnsName, tableColumnsKey, tableButtonsOnEachRow);//根据用户定义的数据 设置table columns data
	    vueContentMounted = function () {
	      this.doLoadPage()
	    };//设置 vue 生命周期 Mounted 时 调用table读取页数据
	  }
  }
  if(hasQueryFrom){
	  if (queryFormItemName && queryFormItemKey && queryFormItemType && defaultQueryFormDomId) {
	    //根据用户定义的数据 生成query form 放到指定 dom id
	    createFormTemplate('vueQueryForm', queryFormItemName, queryFormItemKey, queryFormItemType, defaultQueryFormDomId);
	    //设置 query
	    createQueryForm(createQueryFormData(queryFormItemKey));
	  }
  }
};

function initializeContentOptions() {
  if (beforeVueContentCreate)
    beforeVueContentCreate();
  if (beforeVueContentCreateCustom)
    beforeVueContentCreateCustom();
  return {
    el: vueContentElementSelector,
    data: vueContentData,
    methods: vueContentMethods,
    beforeCreate: vueContentBeforeCreate,
    created: vueContentCreated,
    beforeMount: vueContentBeforeMount,
    mounted: vueContentMounted,
    beforeUpdate: vueContentBeforeUpdate,
    updated: vueContentUpdated,
    beforeDestroy: vueContentBeforeDestroy,
    destroyed: vueContentDestroyed};
}
/////////////////////////////////////////////////////////////////////////////////////
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
 * 全局显示吐司消息
 * @param content 内容
 * @param duration 指定时间后关闭
 * @param onClose callback
 * @returns
 */
function toastError(content, duration, onClose) {
  toast(content, 'error', duration, onClose);
}

function toastSuccess(content, duration, onClose) {
  toast(content, 'success', duration, onClose);
}

function toastInfo(content, duration, onClose) {
  toast(content, 'info', duration, onClose);
}

function toastLoading(content, duration, onClose) {
  return toast(content, 'loading', duration, onClose);
}

function toastWarning(content, duration, onClose) {
  toast(content, 'warning', duration, onClose);
}

function toast(content, type, duration, onClose) {
  if ('success' === type) {
    getVueObject().$Message.success(content, duration, onClose);
  } else if ('warning' === type) {
    getVueObject().$Message.warning(content, duration, onClose);
  } else if ('error' === type) {
    getVueObject().$Message.error(content, duration, onClose);
  } else if ('loading' === type) {
    return getVueObject().$Message.loading(content, duration, onClose);
  } else if ('info' === type) {
    getVueObject().$Message.info(content, duration, onClose);
  } else {
    getVueObject().$Message.info(content, duration, onClose);
  }
}

