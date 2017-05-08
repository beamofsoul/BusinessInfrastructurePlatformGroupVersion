//初始化完毕后记录表单所有属性值，用于后期提交修改时判断表单属性值是否被修改
var initializedFormData = {};
//tableContainer中表格显示列的中文名称
var columnNames = ['ID','名称','上级节点','页面访问秘钥','后台执行秘钥','分组','针对资源类型','排序','可用状态','创建日期','最后修改日期','操作'];
//tableContainer中表格每列对应的业务模型实体类的属性名
var attributeNames = ['id','name','parentId','url','action','group','resourceType','sort','available','createDate','modifyDate','operation'];
//tableContainer中表格每列需要的按钮
var buttonsOnEachRow = ['update?','copy?','delete?'];
//提交add、update或copy表单时的表单验证规则
var parentIdReg = /^[0-9]*$/;
var urlReg = /^[a-z]+\/{1}[a-z]+$/;
var actionReg = /^[a-z]+\:{1}[a-z]+$/;
var sortReg = /^[0-9]*$/;
var validateRules = [
	{name: 'name', required: true, unique:'checkPermissionNameUnique', minLength: 3, maxLength: 10},
	{name: 'parentId', required: true, regex: [{rule: parentIdReg, warn: '上级节点必须为正整数!'}]},
	{name: 'url', required: true, regex: [{rule: urlReg, warn: '页面访问秘钥必须以一个左斜杠[/]分割的两个小写英文字符串组成!'}]},
	{name: 'action', required: true, regex: [{rule: actionReg, warn: '后台访问秘钥必须以一个冒号[:]分割的两个小写英文字符串组成!'}]},
	{name: 'sort', required: true, regex: [{rule: sortReg, warn: '排序必须为正整数!'}]}
];
/*****************************************************************************************
 *************************************** LIST PAGE ***************************************
 *****************************************************************************************/
loadPageableDataUrl = 'permissionsByPage'; //加载分页数据对应的后台请求映射URL
loadPageableDataCallback = function(data) { //pagination-utils.js 获取完分页数据后自动生成数据表格
	data = data.pageableData;
	var value = [];
	for(var i=0;i<data.numberOfElements;i++) {
		value[i] = parseValuesOnEachRow(data.content[i]);
	}
	return generateDefaultDataTable(columnNames,attributeNames,value,false,'permission');
};
/*****************************************************************************************
 ************************************** OTHER PAGES **************************************
 *****************************************************************************************/
documentReady = function() { //pagination-utils.js 绑定所有表单单击(目的为提交表单)事件
	$('#submitAdd').click(function() {
		submitForm(validateForm('add',validateRules),'singleAdd','addForm',function(){$('#cancelAdd').click()});
	});	
	$('#submitUpdate').click(function() {
		submitForm(validateForm('update',validateRules),'singleUpdate','updateForm',function(){$('#closeUpdate').click()},true);
	});
	$('#submitCopy').click(function() {
		submitForm(validateForm('copy',validateRules),'singleAdd','copyForm',function(){$('#closeCopy').click()});
	});
	$('#closeUpdate').click(function() {doCheckAll(false)});
	$('#closeCopy').click(function() {doCheckAll(false)});
};

/**
 * 初始化update表单和copy表单数据
 */
initUpdateForm = function(obj) {initForm('update',obj)} //basic-button-utils.js
initCopyForm = function(obj) {initForm('copy',obj)} //basic-button-utils.js
deleteBefore = function(obj) {checkUsedPermissions(obj)} //basic-button-utils.js

/**
 * 提交表单
 * @param validateFormFunction - 保存数据之前需要验证表单的方法
 * @param url - 提交至后台的URL
 * @param formId - 提交的表单Id
 * @param callback - 保存成功后的回调方法
 * @param isByUpdate - 是否提交的是update表单
 */
function submitForm(validateFormFunction,url,formId,callback,isByUpdate) {
	if(validateFormFunction) { //最后要清空ADD与COPY FORM
		saveData(url,formId,function(data){
			fresh4NewData(data,function(){callback();},isByUpdate);
		});
	}
}

/**
 * 保存add、update或copy表单提交的数据
 * @param url - 提交至后台的URL
 * @param formId - 提交的表单Id
 * @param sucFunc - 保存成功后的回调方法
 */
function saveData(url,formId,sucFunc) {
	//在更新记录时判断表单数据是否更改
	var savingFormData = $('#'+formId).serializeObject();
	if (formId.indexOf('update')<0 || JSON.stringify(savingFormData) !== JSON.stringify(initializedFormData)) {
		$.ajax({
		    headers: {
		        'Accept': 'application/json',
		        'Content-Type': 'application/json'
		    },
	        type: "POST",
	        url: url,
	        data: formatNonstandardData2JSON(savingFormData),
	        error: function(XMLHttpRequest, textStatus, errorThrown) {
		        console.log(XMLHttpRequest.responseText);
		    },
	        success: function(data) {
	        	sucFunc(data);
	        }
	    });
	} else {
		sucFunc(null);
	}
}

/**
 * 当用户操作完增加、修改或复制记录后对tableContainer中的数据进行更新
 * @param data - 更改后的数据
 * @param callback - 回调方法
 * @param isByUpdate - 是否是update后进入此方法
 */
function fresh4NewData(data,callback,isByUpdate) {
	if (data != null) {
		if (isByUpdate) {
			$('table#dataTable').find('tr#'+data.updated.id)
			.html(generateDefaultDataTableTd(
					attributeNames,parseValuesOnEachRow(data.updated)));
		} else {
			var last = getLastRecord(); 
			var total = getTotalRecords();
			var size = getHowManyRecords();
			
			//新纪录是否能够直接在最后一页显示,而不需要增加一页
			if (total % size > 0 || total == 0) {
				//当前页是否为最后一页
				if (last == total) {
		    		$('table#dataTable tbody')
		    			.append(generateDefaultDataTableTr(
		    					attributeNames,parseValuesOnEachRow(data.created)));
					last++;
				} 
				total++;
				if (total == 1) setFirstRecord(last);
				setLastRecord(last);
				setTotalRecords(total)
				gotoPageNumber(getLastPageNumber());
			} else {
				//无论当前页在不在最后一页,最后一页也没有任何空间可以显示新纪录
				//涉及到增加页码按钮,需要重新生成页码按钮,则需要重新刷新页面
				initPageableData(getLastPageNumber(),true,size);
			}
		}
	}
	callback();
}

/**
 * 初始化update与copy表单，当用户打开此模态窗口时
 * @param formKey - update或copy
 * @param obj - 初始化表单所用的数据对象
 */
function initForm(formKey,obj) {
	$('#'+formKey+'_id').val(obj.id);
	$('#'+formKey+'_name').val(obj.name);
	$('#'+formKey+'_parentId').val(obj.parentId);
	$('#'+formKey+'_url').val(obj.url);
	$('#'+formKey+'_action').val(obj.action);
	$('#'+formKey+'_group').val(obj.group);
	$('#'+formKey+'_sort').val(obj.sort);
	$('#'+formKey+'Form').find('input[name=resourceType][value='+(obj.resourceType)+']').uCheck('check');
	$('#'+formKey+'Form').find('input[name=available][value='+(obj.available)+']').uCheck('check');
	
	//初始化完毕后记录表单所有属性值，用于后期提交修改时判断表单属性值是否被修改
    if (formKey === "update")
      initializedFormData = $('#'+formKey + 'Form').serializeObject();
}

/**
 * 如何将输入参数obj的值赋值给tableContainer中每条记录的属性
 * @param obj - tableContainer中每条记录针对的业务模型实体类对象实例
 * @returns 格式化后记载着tableContainer中一条记录中每列属性的数组
 */
function parseValuesOnEachRow(obj) {
	return [obj.id,
		obj.name,
		obj.parentId,
		obj.url,
		obj.action,
		obj.group,
		obj.resourceType == 'button' ? '按钮' : '菜单',
		obj.sort,
		obj.available ? '启用' : '弃用',
		formatDate(obj.createDate,true),
		formatDate(obj.modifyDate,true),
		buttonsOnEachRow]
}

/**
 * 判断是否选中的权限已经被使用，如果被使用则取消执行所有后续JS 
 * @param obj - 一个或多个角色id字符串，以逗号分隔
 */
function checkUsedPermissions(obj) {
	//判断是否当前角色已经被使用	
	var isUsed;
	$.ajax({
	    headers: {
	        'Accept': 'application/json',
	        'Content-Type': 'application/json'
	    },
	    cache: false,
        async: false,
	    type: 'POST',
	    url: 'isUsedPermissions',
	    data: obj,
	    dataType: 'json',
	    success: function(data) {
	    	isUsed = data.isUsed;
	    }
	});
	if (isUsed) {
		warn('所选权限已经被使用!');
		abort();
	}
}

/**
 * 实现列表页面搜索按钮的单击事件
 * @param obj - 搜索按钮对象
 */
function onSearch(obj) {
	var value = $('#searchInList').val();
	initPageableDataByCondition(0,true,globalSize, value ? {'name': '"'+value+'"'} : null);
}