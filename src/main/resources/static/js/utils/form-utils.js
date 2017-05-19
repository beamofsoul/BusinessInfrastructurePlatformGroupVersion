// 当前用户能够操作的所有行为
var actions = {
  'del': {'key': 'del', 'url': 'delete'},
  'add': {'key': 'add', 'url': 'singleAdd'},
  'update': {'key': 'update', 'url': 'singleUpdate'},
  'copy': {'key': 'copy', 'url': 'singleAdd'}
};

var vueAddForm;
var vueUpdateForm;
var vueQueryForm;
var vueCopyForm;

var vueAddFormRules;
var vueUpdateFormRules;
var vueCopyFormRules;

var vueAddModalVisible = false; //添加form 对话框 显示状态
var vueUpdateModalVisible = false;  //修改form 对话框 显示状态
var vueDeleteModalVisible = false;  //删除 对话框 显示状态
var vueDeleteProgressVisible = false; //删除form 对话框 按钮状态
var vueDeleteMessage = '';
var vueCopyModalVisible = false;    //复制form 对话框 显示状态

var currentAction = null; // 当前用户操作的行为 - add、update、delete 等

var queryFromRowItemNum = 4;  //综合查询 每行放控件的数量
var queryFormItemWidth = 80;  //综合查询 控件的宽度 像素
var querySubmitButtonName = 'submitQueryForm';  //综合查询 提交按钮触发函数名
var vueQueryFormVisible = '-1';
var defaultQueryFormDomId = 'queryFormDomId'; //默认的 query form 页面 dom id

var initAddForm;      //初始化添加页面表单业务数据的方法
var initUpdateForm; //初始化更新页面表单业务数据的方法
var initCopyForm;   //初始化复制页面表单业务数据的方法

var beforeAdd;      //执行进入添加按钮单击事件方法首先需要执行的方法
var beforeUpdate;   //执行进入修改按钮单击事件方法首先需要执行的方法
var beforeDelete;   //执行进入删除按钮单击事件方法首先需要执行的方法
var beforeCopy;     //执行进入复制按钮单击事件方法首先需要执行的方法

var addBefore;		//执行修改后台方法之前需要执行的方法
var updateBefore;   //执行修改后台方法之前需要执行的方法
var deleteBefore;   //执行删除后台方法之前需要执行的方法
var copyBefore;     //执行复制后台方法之前需要执行的方法

var submitAddAfter;      //提交后执行的自定义方法
var submitUpdateAfter;   //提交后执行的自定义方法
var submitDeleteAfter;   //提交后执行的自定义方法
var submitCopyAfter;     //提交后执行的自定义方法

var submitAddBefore;      //提交前执行的自定义方法
var submitUpdateBefore;   //提交前执行的自定义方法
var submitDeleteBefore;   //提交前执行的自定义方法
var submitCopyBefore;     //提交前执行的自定义方法


/********************  添加按钮  *********************/
/**
 * 添加
 */
function doAddButton() {
  if (beforeAdd)
    beforeAdd();

  resetForm('vueAddForm');
  currentAction = actions.add;
  this.vueAddModalVisible = true;

  if (initAddForm)
    initAddForm();
  
  if(addBefore)
	  addBefore();
}
/**
 * 添加提交
 */
function submitAddForm() {
  var _self = this;
  if(submitAddBefore) submitAddBefore(_self.vueAddForm);
  submitFormValidate(currentAction, function (data) {
    toastSuccess('提交成功!');
    _self.vueAddModalVisible = false;
    resetForm('vueAddForm');
    if(submitAddAfter)submitAddAfter();
  });
}
/********************  修改按钮  *********************/

// 默认的初始化updateform逻辑
initUpdateForm = function (obj) {
  var updateForm = getVueObject().vueUpdateForm;
  //mapping 值映射
  copyPropertiesValue(updateForm, obj);
  //格式化返回json属性类型
  formatObject2String(updateForm);

  getVueObject().vueUpdateModalVisible = true;
  currentAction = actions.update;
};
/**
 * table 上部 修改按钮
 */
function doUpdateButton() {
  if (beforeUpdate)
    beforeUpdate();
  var _self = this;
  var checkdata = _self[currentCheckedTableRowName];
  if (checkdata.length !== 1) {
    toastInfo('请选择1条记录!');
    return;
  }
  currentAction = actions.update;
  resetForm('vueUpdateForm');
  getSingleData(getCheckedTableRowIds(checkdata), updateBefore, function (data) {
    initUpdateForm(data);
  });
}
/**
 * table row 修改按钮
 * @param index 索引
 * @param tableDataName index所在data的名称
 */
function rowUpdateButton(index, tableDataName) {
  if (beforeUpdate)
    beforeUpdate();
  var _self = this;
  getSingleData(_self[tableDataName][index].id, updateBefore, function (data) {
    initUpdateForm(data);
  });
}

/**
 * 根据数据表格中复选框选中的记录Id，获取所对应的业务对象实例
 */
var getSingleData = function (idData, successCallbackBefore, successCallback, errorCallback) {
  var result = false;
  $.iposty('single', {'id': idData},
      function (data) {
        var obj = data.obj;
        if ((successCallbackBefore && successCallbackBefore(obj)) || !successCallbackBefore) {
          if (successCallback)
            successCallback(obj);
          result = true;
        }
      },
      errorCallback
      );
  return result;
};
/**
 * 修改form提交
 * @returns
 */
function submitUpdateForm() {
  var _self = this;
  if(submitUpdateBefore) submitUpdateBefore(_self.vueUpdateForm);
  submitFormValidate(currentAction, function (data) {
    toastSuccess('更新成功!');
    _self.vueUpdateModalVisible = false;
    resetForm('vueUpdateForm');
    if(submitUpdateAfter) submitUpdateAfter();
  });
}

/**
 * 删除
 * @param vueTableCheckedDataName
 * @param actionsType
 * @param vueBindModalDataName
 * @returns
 */
function doDeleteButton() {
  if (beforeDelete)
    beforeDelete();
  var _self = this;
  var checkData = _self[currentCheckedTableRowName];

  if (checkData.length === 0) {
    toastInfo('至少选中1条记录!');
    return;
  }
  if (deleteBefore)
    deleteBefore(getCheckedTableRowIds(checkData));
  this.vueDeleteMessage = "即将删除" + checkData.length + "条记录,是否继续删除?";
  this.vueCheckedTableRowIds = getCheckedTableRowIds(checkData);//将要删除的id 赋值给data
  currentAction = actions.del;

  this.vueDeleteModalVisible = true;
}

/**
 * table row 删除按钮
 * @param index 索引
 * @param vueBindTableDataDataName index所在data的名称
 * @returns
 */
function rowDeleteButton(index, tableDataName) {
  var id = String(getVueObject()[tableDataName][index].id);
  if (beforeDelete)
    beforeDelete();
  if (deleteBefore)
    deleteBefore(id);

  getVueObject().vueDeleteMessage = "是否继续删除此条记录?";
  getVueObject()[currentCheckedTableRowIdsName] = id;
  getVueObject().vueDeleteModalVisible = true;// 显示删除界面
  currentAction = actions.del;
}

/**
 * 删除提交
 */
function submitDeleteForm() {
  var _self = this;
  if(submitDeleteBefore) submitDeleteBefore(_self[currentCheckedTableRowIdsName]);
  _self.vueDeleteProgressVisible = true;
  submitForm(currentAction, _self[currentCheckedTableRowIdsName],
      function (data) {
        toastSuccess('删除成功');
        _self.vueDeleteProgressVisible = false;
        _self.vueDeleteModalVisible = false;
        if(submitDeleteAfter)submitDeleteAfter();
      },
      function (errorMessage) {
        toastError(errorMessage);
        _self.vueDeleteProgressVisible = false;
      }
  );
}

/********************  复制按钮  *********************/
// 默认的初始化copyform逻辑
initCopyForm = function (obj) {
  var copyForm = getVueObject().vueCopyForm;
  copyPropertiesValue(copyForm, obj); //mapping 值映射
  formatObject2String(copyForm);  //格式化返回json属性类型
  getVueObject().vueCopyModalVisible = true;
  currentAction = actions.copy;
};
/**
 * table row 复制按钮
 * @param index 索引
 * @param tableDataName index所在data的名称
 */
function rowCopyButton(index, tableDataName) {
  if (beforeCopy)
    beforeCopy();
  var _self = this;
  getSingleData(_self[tableDataName][index].id, copyBefore, function (data) {
    initCopyForm(data);
  });
}
//复制.提交
function submitCopyForm() {
  var _self = this;
  _self.vueCopyForm.id = -1;
  submitFormValidate(currentAction, function (data) {
    toastSuccess('提交成功!');
    _self.vueCopyModalVisible = false;
    resetForm('vueCopyForm');
    if(submitCopyAfter)submitCopyAfter();
  });
}


/**
 * 综合 查询 按钮
 */
function submitQueryForm() {
  //此处可设置 table 相关值 ，然后在加载分页
  this.doLoadPage();
}

/**
 * 获得当前vue form data
 * @returns
 */
function getCurrentForm() {
  return (!currentAction || !vueContentObject) ? null : currentAction.key === actions.add.key ? getVueObject().vueAddForm : getVueObject().vueUpdateForm;
}

/**
 * 获得当前vue form data name
 * @returns
 */
function getCurrentFormName() {
  return (!currentAction || !vueContentObject) ? null : currentAction.key === actions.add.key ? 'vueAddForm' : 'vueUpdateForm';
}

/**
 * 重置 form data
 * @param formDataName
 * @returns
 */
function resetForm(formName) {
//	console.log('测试 resetForm   '+formName);
//	console.log(JSON.stringify(vueAddForm));
//	vueAddForm.id=0;
//	vueAddForm.idasdasdf=0;
//	console.log(JSON.stringify(vueAddForm));
//	vueAddForm.idasdasdf=2;
//	console.log(JSON.stringify(vueAddForm));

  getVueRefObject(formName).resetFields();
//  console.log(JSON.stringify(vueAddForm));

}



/**
 * form 验证 callback
 * @param callback
 * @param isSuccess
 * @param errorMessage
 * @returns
 */
function formValidateCallback(callback, isSuccess, errorMessage) {
  if (!isSuccess)
    callback(new Error(errorMessage));
  else
    callback();
}

/**
 * form 通用校验方法
 * @param rule 验证规则
 * @param value 值
 * @param callback 验证结果 
 */
function validateFormRules(rule, value, callback) {
  var form = getCurrentForm();

  if (rule.equal) {
    var equalValue = form[rule.equal];
    if (value === '') {
      formValidateCallback(callback, false);
    } else if (value !== equalValue) {
      formValidateCallback(callback, false);
    } else {
      formValidateCallback(callback, true);
    }
  } else if (rule.unique) {
    var url = rule.unique;
    $.iposty(url, {'data': value, 'id': form.id}, function (data) {
      formValidateCallback(callback, data.isUnique);
    });
  } else if (rule.otherValidate) {
    var filedName = rule.otherValidate;
    if (form[filedName] !== '')
      getVueRefObject(getCurrentFormName()).validateField(filedName);
    formValidateCallback(callback, true);
  }
}

/**
 * 提交表单
 * @param currentAction
 * @param data
 * @param callback
 * @param errorCallback
 */
function submitForm(currentAction, data, callback, errorCallback) {
  // 包装请求后 回调函数.data 请求成功后 后台返回的值
  var successCallback = function (data) {
    fresh4NewData(data, function () {
      callback();
    });
  };
  // 复制一份data，将属性的值为对象的 ，对象中 属性值全部为空的 。做附空值
  var copyData = clearNullProperties(data);
  
  if (currentAction.key === actions.del.key)
    $.idel(currentAction.url, copyData, successCallback, errorCallback);
  else
    $.iposty(currentAction.url, copyData, successCallback, errorCallback);
}

/**
 * form验证并提交
 * @param currentAction
 * @param successCallback
 * @param errorCallback
 * @returns
 */
function submitFormValidate(currentAction, successCallback, errorCallback) {
  getVueRefObject(getCurrentFormName()).validate((valid) => {
    if (valid)
      submitForm(currentAction, getCurrentForm(), successCallback, errorCallback);
    else
      toastError('表单验证失败!');
  });
}
;

/**
 * 格式化查询form的数据 加“\“ \”” 后台需要
 * @param vueBindFormQueryData query form data
 * @returns
 */
function formatQueryFormData(vueBindFormQueryData) {
  var queryObj = {};
  for (var i in vueBindFormQueryData) {
    queryObj[i] = '"' + vueBindFormQueryData[i] + '"';
  }
  return queryObj;
}

/**
 * 生成综合查询form
 * @param queryFormName - form 绑定的data名
 * @param queryFormItemName - form item label 数组
 * @param queryFormItemKey - form item 在data中的属性名
 * @param queryFormItemType - form item 的类型，如果是select 需要写成特定格式 select:option项的data
 * 
 */
function createTableQueryFrom(queryFormName, queryFormItemName, queryFormItemKey, queryFormItemType) {
  var icolSpan = 24 / queryFromRowItemNum - 1;//24栅格
  var totalRow = parseInt(queryFormItemKey.length / queryFromRowItemNum);
  
  if (queryFormItemKey.length % queryFromRowItemNum !== 0) totalRow++;
//  console.log('24格 每个控件占几格 '+icolSpan)
//  console.log('总共多少行 '+totalRow)
//  console.log('总共多少个控件 '+queryFormItemKey.length)
  var itemIndex = 0;
  var queryForm = '<i-form ref="' + queryFormName + '" :model="' + queryFormName + '"  :show-message="false" label-position="left" :label-width="' + queryFormItemWidth + '" >';
  //行
  for (var rowIndex = 0; rowIndex < totalRow; rowIndex++) {
//	  console.log('生成第几行 '+rowIndex)
    queryForm += '<Row type="flex" justify="space-between" >';
    //项
    for (var rowItemIndex = 0; rowItemIndex < queryFromRowItemNum; rowItemIndex++) {
//    	console.log('生成这行第几个控件 '+rowItemIndex)
//    	console.log('---------生成总共控件的第几个 '+(itemIndex+1))
//    	console.log('queryFormItemKey.length '+queryFormItemKey.length);
    	
      if (itemIndex >= queryFormItemKey.length){
//    	  console.log('生成总共控件的第几个aaaaaa '+itemIndex)
    	  queryForm += '<i-col span="' + icolSpan + '"></i-col>';
    	  itemIndex++;
    	  continue;
      }
      
      
      queryForm += '<i-col span="' + icolSpan + '">';

      var itemTypeArray = queryFormItemType[itemIndex].split('#');
      var itemType = itemTypeArray[0];

      if (itemType === 'string') {
        queryForm += '<Form-item label="' + queryFormItemName[itemIndex] + '：" prop="' + queryFormItemKey[itemIndex] + '">';
        queryForm += '<i-input v-model="' + queryFormName + '.' + queryFormItemKey[itemIndex] + '" ></i-input>';
        queryForm += '</Form-item>';
      } else if (itemType.indexOf('number') >= 0) {
        queryForm += '<Form-item label="' + queryFormItemName[itemIndex] + '：" prop="' + queryFormItemKey[itemIndex] + '">';
        var rangeStr = '';
        if (itemType.indexOf('<number') >= 0) {
          var minIndex = itemType.indexOf('<number');
          rangeStr += ' :min="' + itemType.substring(0, minIndex) + '"';
        }
        if (itemType.indexOf('number<') >= 0) {
          var maxIndex = itemType.lastIndexOf('<');
          rangeStr += ' :max="' + itemType.substring(maxIndex + 1) + '"';
        }

        queryForm += '<Input-number ' + rangeStr + ' v-model="' + queryFormName + '.' + queryFormItemKey[itemIndex] + '"></Input-number>';
        queryForm += '</Form-item>';

      } else if (itemType === 'select') {
        queryForm += '<Form-item label="' + queryFormItemName[itemIndex] + '：" prop="' + queryFormItemKey[itemIndex] + '">';
        queryForm += '<i-select v-model="' + queryFormName + '.' + queryFormItemKey[itemIndex] + '" clearable>';
        queryForm += '<i-option v-for="item in ' + itemTypeArray[1] + '" :value="item.value" :key="item">{{ item.label }}</i-option>';
        queryForm += '</i-select>';
        queryForm += '</Form-item>';
      } else if (itemType.startsWith("date")) {
        queryForm += '<Form-item label="' + queryFormItemName[itemIndex] + '：" prop="' + queryFormItemKey[itemIndex] + '">';
        queryForm += '<Date-picker placement="bottom" v-model="' + queryFormName + '.' + queryFormItemKey[itemIndex] + '" type="' + itemType + '"></Date-picker>';
        queryForm += '</Form-item>';
      }

      queryForm += '</i-col>';
      itemIndex++;
    }
    queryForm += '</Row>';
  }
  queryForm += '<Row type="flex" justify="center" >';
  queryForm += '<i-col span="' + icolSpan + '">';
  queryForm += '<i-button icon="ios-search"  @click="' + querySubmitButtonName + '()" >查询</i-button>';
  queryForm += '<i-button icon="ios-search"  @click="resetForm(\'' + queryFormName + '\')"  style="margin-left: 8px">重置</i-button>';
  queryForm += '</i-col>';
  queryForm += '</Row>';
  queryForm += '</i-form>';
  return queryForm;
}

/**
 * 设置 生成form 标签代码 到指定的id 上
 * @param queryFormName query form data name
 * @param queryFormItemName 
 * @param queryFormItemKey
 * @param queryFormItemType
 * @param queryFormDomId
 * @returns
 */
function createFormTemplate(queryFormName, queryFormItemName, queryFormItemKey, queryFormItemType, queryFormDomId) {
  document.getElementById(queryFormDomId).innerHTML = createTableQueryFrom(queryFormName, queryFormItemName, queryFormItemKey, queryFormItemType);
}

/**
 * 设置 query from data
 * @param vueBindFormQueryDataValue
 * @param vueBindFormQueryData
 * @returns
 */
function createQueryForm(queryForm, queryFormData) {
  if (!queryFormData)
    vueQueryForm = queryForm;
  else
    queryFormData = queryForm;
}

/**
 * 生成 query from data
 * @param vueBindFormQueryDataValue
 * @param vueBindFormQueryData
 * @returns
 */
function createQueryFormData(queryFormItemKey) {
  var obj = {};
  for (var i in queryFormItemKey) {
    obj[queryFormItemKey[i]] = '';
  }
  return obj;
}

/**
 * 设置 add update form model data 
 * @param vueBindFormModelDataValue 
 * @param vueBindFormModelData vue data object
 * @returns
 */
function setFormDataObject(formDataValue, formData) {
  if (!formData)
    vueAddForm = vueUpdateForm = vueCopyForm = formDataValue;
  else
    formData = formDataValue;
}

/**
 * 设置form 验证规则 data
 * @param vueBindFormRulesDataValue vue data 的value
 * @param vueBindFormRulesData vue data object
 * @returns
 */
function setFormRulesObject(vueBindFormRulesDataValue) {
  vueAddFormRules = vueUpdateFormRules = vueBindFormRulesDataValue;
}
