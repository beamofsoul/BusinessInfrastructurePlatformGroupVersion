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

var vueAddFormName = 'vueAddForm';
var vueUpdateFormName = 'vueUpdateForm';
var vueCopyFormName = 'vueCopyForm';

var vueAddFormRules={};
var vueUpdateFormRules={};
var vueCopyFormRules={};

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

var showAddFormBefore;      //显示增加页面之前执行的自定义方法
var showUpdateFormBefore;   //显示修改页面之前执行的自定义方法
var showCopyFOrmBefore;     //显示复制页面之前执行的自定义方法

var beforeAdd;      //执行进入添加按钮单击事件方法首先需要执行的方法
var beforeUpdate;   //执行进入修改按钮单击事件方法首先需要执行的方法
var beforeDelete;   //执行进入删除按钮单击事件方法首先需要执行的方法
var beforeCopy;     //执行进入复制按钮单击事件方法首先需要执行的方法

//var addBefore;		//执行修改后台方法之前需要执行的方法
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

var hasQueryFrom = true; //是否有queryForm

/********************  添加按钮  *********************/

/**
 * 点击添加按钮，默认弹出AddForm页面
 */
function doAddButton() {
	if (beforeAdd) beforeAdd();
	currentAction = actions.add;
	resetForm();
	if (initAddForm) initAddForm();
	this.vueAddModalVisible = true;
	if(showAddFormBefore) showAddFormBefore();
}

/**
 * 点击AddForm页面提交按钮，默认调用后台方法进行业务记录数据插入，并重置AddForm表单
 */
function submitAddForm() {
	var cachedAddForm = Object.assign({}, this.vueAddForm);
	if(submitAddBefore) submitAddBefore(getVueObject().vueAddForm);
	submitFormValidate(currentAction, function (data) {
		toastSuccess('提交成功!');
		getVueObject().vueAddModalVisible = false;
		resetForm();
		if(submitAddAfter) submitAddAfter(cachedAddForm);
	});
}

/********************  修改按钮  *********************/

/**
 * 点击批量按钮栏处修改按钮，默认初始化并弹出UpdateForm页面之前，对表格记录选中数量进行判断
 */
function doUpdateButton() {
	if (beforeUpdate) beforeUpdate();
	var checkedRows = this[currentCheckedTableRowName];
	if (checkedRows.length !== 1) {
		toastInfo('请选择1条记录!');
		return;
	}
	getSingleData(getCheckedTableRowIds(checkedRows), updateBefore, function(data) {
		initUpdateForm(data);
	});
}

/**
 * 点击数据表格每行中的修改按钮，获取改行所对应的业务对象标识，并获取其数据，以备后续初始化
 * @param index 索引
 * @param tableDataName index所在data的名称
 */
function rowUpdateButton(index, tableDataName) {
	if (beforeUpdate) beforeUpdate();
	getSingleData(this[tableDataName][index].id, updateBefore, function (data) {
		initUpdateForm(data);
	});
}

/**
 * 初始化并弹出UpdateForm页面
 */
initUpdateForm = function (obj) {
	currentAction = actions.update;
	resetForm();
	copyProperties(obj, getVueObject().vueUpdateForm);
	if (showUpdateFormBefore) showUpdateFormBefore(getVueObject().vueUpdateForm);
	getVueObject().vueUpdateModalVisible = true;
};

/**
 * 提交UpdateForm数据至后台，如修改成功进行提示并重置修改表单数据
 */
function submitUpdateForm() {
	if(submitUpdateBefore) submitUpdateBefore(this.vueUpdateForm);
	submitFormValidate(currentAction, function (data) {
		toastSuccess('更新成功!');
		getVueObject().vueUpdateModalVisible = false;
		resetForm();
		if(submitUpdateAfter) submitUpdateAfter(data.updated);
	});
}

/********************  删除按钮  *********************/

/**
 * 点击批量按钮栏中删除按钮时，判断是否用户至少选中了一条数据表格中的记录，并在删除数据前进行确认
 */
function doDeleteButton() {
	if (beforeDelete) beforeDelete();
	var checkedRows = this[currentCheckedTableRowName];
	if (checkedRows.length === 0) {
		toastInfo('至少选中1条记录!');
		return;
	}
	currentAction = actions.del;
	if (deleteBefore) deleteBefore(getCheckedTableRowIds(checkedRows));
	this.vueDeleteMessage = "即将删除" + checkedRows.length + "条记录,是否继续删除?";
	this.vueCheckedTableRowIds = getCheckedTableRowIds(checkedRows); //将要删除的id 赋值给data
	this.vueDeleteModalVisible = true;
}

/**
 * 点击数据表格每行数据上的删除按钮
 * @param index 索引
 * @param vueBindTableDataDataName index所在data的名称
 * @returns
 */
function rowDeleteButton(index, tableDataName) {
	if (beforeDelete) beforeDelete();
	var id = String(getVueObject()[tableDataName][index].id);
	if (deleteBefore) deleteBefore(id);
	currentAction = actions.del;
	this.vueDeleteMessage = "是否继续删除此条记录?";
	this[currentCheckedTableRowIdsName] = id;
	this.vueDeleteModalVisible = true;
}

/**
 * 提交删除表单
 */
function submitDeleteForm() {
	var ids = this[currentCheckedTableRowIdsName];
	var	cachedDeleteIds = ids; 
	if(submitDeleteBefore) submitDeleteBefore(ids);
	this.vueDeleteProgressVisible = true;
	submitForm(currentAction, ids, function (data) {
		if (data.count > 0) {
			toastSuccess('删除成功');
			getVueObject().vueDeleteProgressVisible = false;
		} else {
			toastWarning('记录正被使用，禁止删除');
			getVueObject().vueDeleteProgressVisible = false;
		}
		getVueObject().vueDeleteModalVisible = false;
		if(submitDeleteAfter) submitDeleteAfter(ids);
	}, function (errorMessage) {
		toastError(errorMessage);
		getVueObject().vueDeleteProgressVisible = false;
	});
}

/********************  复制按钮  *********************/

/**
 * 点击数据表格每行复制按钮，将从后台获取最新选中行数据
 * @param index 索引
 * @param tableDataName index所在data的名称
 */
function rowCopyButton(index, tableDataName) {
	if (beforeCopy) beforeCopy();
	getSingleData(this[tableDataName][index].id, copyBefore, function(data) {
		initCopyForm(data);
	});
}

/**
 * 初始化复制表单模型属性值
 */
initCopyForm = function (obj) {
	currentAction = actions.copy;
	var copyForm = getVueObject().vueCopyForm;
	copyPropertiesValue(copyForm, obj); //mapping 值映射
	formatObject2String(copyForm);  //格式化返回json属性类型
	if (showCopyFOrmBefore) showCopyFormBefore(getVueObject().vueCopyForm);
	getVueObject().vueCopyModalVisible = true;
};

/**
 * 提交复制表单至后台，具体执行添加操作
 */
function submitCopyForm() {
	this.vueCopyForm.id = -1;
	submitFormValidate(currentAction, function(data) {
		toastSuccess('提交成功!');
		getVueObject().vueCopyModalVisible = false;
		resetForm();
		if(submitCopyAfter)submitCopyAfter();
	});
}

/********************  查询按钮  *********************/

/**
 * 提交复合查询表单
 */
function submitQueryForm() {
	//此处可设置 table 相关值 ，然后在加载分页
	this.doLoadPage();
}

/**
 * 根据数据表格中复选框选中的记录Id，获取所对应的业务对象实例
 */
var getSingleData = function(idData, successCallbackBefore, successCallback, errorCallback) {
	var result = false;
	$.iposty('single', {'id': idData}, function(data) {
		var obj = data.obj;
        if ((successCallbackBefore && successCallbackBefore(obj)) || !successCallbackBefore) {
        	if (successCallback) successCallback(obj);
        	result = true;
        }
	}, errorCallback);
	return result;
};

/**
 * 获得当前VUE表单对象
 * @returns
 */
function getCurrentForm() {
	return (!currentAction || !vueContentObject) ? null : currentAction.key === actions.add.key ? getVueObject().vueAddForm : currentAction.key === actions.update.key ? getVueObject().vueUpdateForm : getVueObject().vueCopyForm;
}

/**
 * 获得当前VUE表单名称
 * @returns null值或相应的表单名称
 */
function getCurrentFormName() {
	return (!currentAction || !vueContentObject) ? null : currentAction.key === actions.add.key ? vueAddFormName : currentAction.key === actions.update.key ? vueUpdateFormName : vueCopyFormName;
}

/**
 * 清空表单中所有chlearable为true的select标签的选中状态
 * @param formName 表单名
 */
function resetFormSelections(formName) {
	try {
		if (!formName) {
			var currentFormName = getCurrentFormName();
			if (currentFormName) formName = currentFormName;
			if (!formName) throw new Error('无效的表单名称');
		}
		var children = getVueRefObject(formName).$children;
		for(var r in children) {
			var child = children[r].$children[0];
			if (child.clearable) child.clearSingleSelect();
		}
	} catch (e) {
		console.log(e);
	}
}

/**
 * 根据输入的表单名称重置其对应的数据模型
 * @param 表单名称
 */
function resetForm(formName) {
	try {
		if (!formName) {
			var currentFormName = getCurrentFormName();
			if (currentFormName) formName = currentFormName;
			if (!formName) throw new Error('无效的表单名称');
		}
		getVueRefObject(formName).resetFields();
	} catch (e) {
		console.log(e);
	}
}

/**
 * 表单验证回掉函数
 * @param callback 回掉函数
 * @param isSuccess 是否成功
 * @param errorMessage 错误信息
 */
function formValidateCallback(callback, isSuccess, errorMessage) {
	if (!isSuccess) callback(new Error(errorMessage));
	else callback();
}

/**
 * 表单通用校验方法
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
 * @param currentAction 当前行为
 * @param data 要提交的数据
 * @param callback 成功刷新页面后的回调函数
 * @param errorCallback 失败的回调函数
 */
function submitForm(currentAction, data, callback, errorCallback) {
	// 包装请求后 回调函数.data 请求成功后 后台返回的值
	var successCallback = function (data) {
		fresh4NewData(data, function() {callback(data)});
	};

	if (currentAction.key === actions.del.key)
		$.idel(currentAction.url, data, successCallback, errorCallback);
	else {
		//Iview解决resetFields不能清空Select选中项问题之前
		//https://github.com/iview/iview/issues/970
		//暂且用Javascript中数字类型最小值在clearNullStructureObject4JSON方法中表示null值进行处理
		var clearedData = clearNullStructureObject4JSON(formatNonstandardData2JSON(data));
		$.iposty(currentAction.url, clearedData, successCallback, errorCallback);
	}
}

/**
 * 表单验证并提交
 * @param currentAction 当前行为
 * @param successCallback 提交表单成功回调函数
 * @param errorCallback 提交表单失败回调函数
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
 * @param vueBindFormQueryData 综合查询表单数据
 * @returns 格式化后的数据
 */
function formatQueryFormData(vueBindFormQueryData) {
	var queryObj = {};
	for (var i in vueBindFormQueryData) {
		queryObj[i] = '"' + vueBindFormQueryData[i] + '"';
	}
	return queryObj;
}

/**
 * 生成综合查询表单内容
 * @param queryFormName - 综合查询表单绑定的VUE对象Ref名
 * @param queryFormItemName - 综合查询表单数据列名数组
 * @param queryFormItemKey - 综合查询表单数据列名对应的在表单绑定对象对应的属性名
 * @param queryFormItemType - 综合查询表单数据列的类型，如果是select 需要写成特定格式[select#xxxx(xxxx为对应的数据对象名称)]
 */
function createTableQueryForm(queryFormName, queryFormItemName, queryFormItemKey, queryFormItemType) {
	var icolSpan = 24 / queryFromRowItemNum - 1;//24栅格
	var totalRow = parseInt(queryFormItemKey.length / queryFromRowItemNum);

	if (queryFormItemKey.length % queryFromRowItemNum !== 0) totalRow++;
//	console.log('24格 每个控件占几格 '+icolSpan)
//	console.log('总共多少行 '+totalRow)
//	console.log('总共多少个控件 '+queryFormItemKey.length)
	var itemIndex = 0;
	var queryForm = '<i-form ref="' + queryFormName + '" :model="' + queryFormName + '"  :show-message="false" label-position="left" :label-width="' + queryFormItemWidth + '" >';
	//行
	for (var rowIndex = 0; rowIndex < totalRow; rowIndex++) {
//		console.log('生成第几行 '+rowIndex)
		queryForm += '<Row type="flex" justify="space-between" >';
		//项
		for (var rowItemIndex = 0; rowItemIndex < queryFromRowItemNum; rowItemIndex++) {
//			console.log('生成这行第几个控件 '+rowItemIndex)
//			console.log('---------生成总共控件的第几个 '+(itemIndex+1))
//			console.log('queryFormItemKey.length '+queryFormItemKey.length);
	    	
			if (itemIndex >= queryFormItemKey.length){
//				console.log('生成总共控件的第几个aaaaaa '+itemIndex)
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
 * 将生成的综合查询表单放置到页面中显示
 * @param queryFormName 综合查询表单VUE对象Ref名
 * @param queryFormItemName 综合查询表单数据列名数组
 * @param queryFormItemKey 综合查询表单数据列名对应的在表单绑定对象对应的属性名
 * @param queryFormItemType 综合查询表单数据列的类型，如果是select 需要写成特定格式[select#xxxx(xxxx为对应的数据对象名称)]
 * @param queryFormDomId 页面中装载综合查询表单的元素DomId
 */
function createFormTemplate(queryFormName, queryFormItemName, queryFormItemKey, queryFormItemType, queryFormDomId) {
	document.getElementById(queryFormDomId).innerHTML = createTableQueryForm(queryFormName, queryFormItemName, queryFormItemKey, queryFormItemType);
}

/**
 * 为综合查询表单指定数据模型对象
 * @param queryForm 表单对象对应的数据模型对象
 * @param queryFormData 指定的表单对象
 */
function createQueryForm(queryForm, queryFormData) {
	if (!queryFormData)
		vueQueryForm = queryForm;
	else
		queryFormData = queryForm;
}

/**
 * 生成综合查询表单数据模型对象
 * @param queryFormItemKey 综合查询表单涉及到的查询属性
 * @returns 生成好的综合查询表单数据模型对象
 */
function createQueryFormData(queryFormItemKey) {
	var obj = {};
	for (var i in queryFormItemKey) {
		obj[queryFormItemKey[i]] = '';
	}
	return obj;
}

/**
 * 为特定表单指定数据模型对象
 * @param formDataValue 表单对象对应的数据模型对象 
 * @param formData 指定的表单对象
 */
function setFormDataObject(formDataValue, formData) {
	if (!formData)
		vueAddForm = vueUpdateForm = vueCopyForm = formDataValue;
	else
		formData = formDataValue;
}

/**
 * 为特定表单增加验证验证规则
 * @param vueBindFormRulesDataValue 表单验证规则
 */
function setFormRulesObject(vueBindFormRulesDataValue) {
	vueAddFormRules = vueUpdateFormRules = vueCopyFormRules = vueBindFormRulesDataValue;
}