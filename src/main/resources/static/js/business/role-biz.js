//当前用户操作的行为 - add、update、delete、allot等等
var currentAction = null;
//当前用户能够操作的所有行为
var actions = {'add': {'key':'add','url':'singleAdd'},'update':{'key':'update','url':'singleUpdate'},'copy':{'key':'copy','url':'singleAdd'},'allot':{'key':'allot','url':'allotPermissionsToRole'}};
//初始化完毕后记录表单所有属性值，用于后期提交修改时判断表单属性值是否被修改
var initializedFormData = {};
//保存完整系统权限信息
var allPermissionData = null;
//分配权限按钮弹出的模态窗口的宽度和高度
var allotModalWindoWidth = '800';
var allotModalWindowHeight = '600';
var allotModalWindowPermissionsAreaHeight = '430px';

//tableContainer中表格显示列的中文名称
var columnNames = ['ID','名称','优先级#S#','创建日期','最后修改日期','操作'];
//tableContainer中表格每列对应的业务模型实体类的属性名
var attributeNames = ['id','name','priority','createDate','modifyDate','operation'];
//tableContainer中表格每列需要的按钮
var buttonsOnEachRow = ['update?','copy?','delete?'];
//提交add、update或copy表单时的表单验证规则
var numberReg = /^(\d{1,2})$/;
var validateRules = [
	{name: 'name', required: true, unique:'checkRoleNameUnique', minLength: 3, maxLength: 10},
	{name: 'priority', regex: [{rule: numberReg, warn: '优先级必须为0-99之间的正整数!'}]}
];
/*****************************************************************************************
 *************************************** LIST PAGE ***************************************
 *****************************************************************************************/
loadPageableDataUrl = 'rolesByPage'; //加载分页数据对应的后台请求映射URL
loadPageableDataCallback = function(data) { //pagination-utils.js 获取完分页数据后自动生成数据表格
	data = data.pageableData;
	var value = [];
	for(var i=0;i<data.numberOfElements;i++) {
		value[i] = parseValuesOnEachRow(data.content[i]);
	}
	return generateDefaultDataTable(columnNames,attributeNames,value,false,'role');
}
/*****************************************************************************************
 ************************************** OTHER PAGES **************************************
 *****************************************************************************************/
documentReady = function() { //pagination-utils.js 绑定所有表单单击(目的为提交表单)事件
	$('#submitAdd').click(function() {
		currentAction = actions.add;
		submitForm(validateForm(currentAction.key,validateRules),currentAction.key,function(){$('#cancelAdd').click();});
	});	
	$('#submitUpdate').click(function() {
		currentAction = actions.update;
		submitForm(validateForm(currentAction.key,validateRules),currentAction.key,function(){$('#closeUpdate').click();});
	});
	$('#submitCopy').click(function() {
		currentAction = actions.copy;
		submitForm(validateForm(currentAction.key,validateRules),currentAction.key,function(){$('#closeCopy').click();});
	});
	$('#submitAllot').click(function() {
		currentAction = actions.allot;
		submitForm(true,currentAction.key,function() {$('#closeAllot').click();});
	});
	$('#closeUpdate').click(function() {doCheckAll(false);});
	$('#closeCopy').click(function() {doCheckAll(false);});
	$('#closeAllot').click(function() {doCheckAll(false);});
	
	//注册自定义按钮并绑定其单击事件
	if(allPermissionData === null) getAllPermissionData();
	registerCustomInlineButtons();
	$('button#allotPermissionButton').click(allotPermissionButtonClickFunction);
	$('button#refreshMappingButton').click(refreshMappingButtonClickFunction);
};

/**
 * 获取当前操作的表单对象
 * @returns 当前表单JQuery对象
 */
function getCurrentForm() {
	return $('#' + currentAction.key + 'Form');
}

/**
 * 更新映射按钮鼠标点击事件
 */
function refreshMappingButtonClickFunction() {
	$.call('refreshMapping');
	warn('更新成功!');
}

/**
 * 分配角色权限按钮鼠标单击事件
 */
function allotPermissionButtonClickFunction() {
	//默认清空按钮弹出窗体事件，避免在执行一次以后，下次不需要选择记录仍然能弹出窗体
	hasButtonBehavior($(this), actions.allot.key);
	var count = countCheckedbox();
	if (count == 0) {
		warn('请选中一条记录!');
	} else if (count == 1) {
		doFunctionWithSingleData(initAllotForm);
		hasButtonBehavior($(this), actions.allot.key, true, allotModalWindoWidth, allotModalWindowHeight);
	} else {
		warn('系统暂不支持批量分配权限功能！');
	}
}

/**
 * 初始化update表单和copy表单数据
 */
initUpdateForm = function(obj) {initForm(actions.update.key,obj)} //basic-button-utils.js
initCopyForm = function(obj) {initForm(actions.copy.key,obj)} //basic-button-utils.js
initAllotForm = function(obj) {initForm(actions.allot.key,obj)} //in current JavaScript file
deleteBefore = function(obj) {checkUsedRoles(obj)} //basic-button-utils.js

/**
 * 提交表单
 * @param validateFormFunction - 保存数据之前需要验证表单的方法
 * @param url - 提交至后台的URL
 * @param callback - 保存成功后的回调方法
 */
function submitForm(validateFormFunction, formKey, callback) {
	if(validateFormFunction) { //最后要清空ADD与COPY FORM
		saveData(formKey, function(data){
			fresh4NewData(data,function(){callback();});
		});
	}
}

/**
 * 保存add、update或copy表单提交的数据
 * @param url - 提交至后台的URL
 * @param sucFunc - 保存成功后的回调方法
 */
function saveData(formKey, sucFunc) {
	//在更新记录时判断表单数据是否更改
	var savingFormData = $(getCurrentForm()).serializeObject();
	if (formKey !=='update' ||JSON.stringify(savingFormData) !== JSON.stringify(initializedFormData)) {
		$.ajax({
		    headers: {
		        'Accept': 'application/json',
		        'Content-Type': 'application/json'
		    },
		    cache: false,
	        type: "POST",
	        url: currentAction.url,
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
 */
function fresh4NewData(data,callback) {
	if (data != null) {
		if (currentAction == actions.allot) {
			if (data.allotted == false) warn('分配角色权限失败，请查看系统日志!');
		} else if (currentAction == actions.update) {
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
	$('#'+formKey+'_priority').val(obj.priority);
	//生成权限复选框并初始化选中状态
	if (formKey == 'allot') {
		$('#allot_name_content').html(' - ' + obj.name);
		generatePermissionCheckbox(formKey,obj);
	}
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
		obj.priority,
		formatDate(obj.createDate,true),
		formatDate(obj.modifyDate,true),
		buttonsOnEachRow]
}

/**
 * 判断是否选中的角色已经被使用，如果被使用则取消执行所有后续JS 
 * @param obj - 一个或多个角色id字符串，以逗号分隔
 */
function checkUsedRoles(obj) {
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
	    url: 'isUsedRoles',
	    data: obj,
	    dataType: 'json',
	    success: function(data) {
	    	isUsed = data.isUsed;
	    }
	});
	if (isUsed) {
		warn('所选角色已经被使用!');
		abort();
	}
}

/**
 * 获取所有系统权限信息
 */
function getAllPermissionData() {
	$.get(projectPath + '/admin/permission/allAvailable', function(data){
		allPermissionData = data.all;
	});
}

/**
 * 生成所有权限信息，并根据所选角色所保有的权限情况初始化权限选中状态
 * @param formKey - allot
 * @param obj - 初始化表单所用的数据对象
 */
function generatePermissionCheckbox(formKey, obj) {
	//分配角色权限复选框容器Id的一个组成部分 - 通用字符
	var element = 'permission';
	//将生产好的html代码填充进checkbox_container中
	var container = $('#'+formKey+'_'+element+'_checkbox_container');
	$(container).html(generatePermissionCheckbox0(obj.permissions, element, 'group'));
	//当subgroup中的所有复选框都被选中时，该subgroup最前面所对应的全选复选框，将被置为选中状态
	checkPermissionGroupCheckbox(container);
	//将所有checkbox_container中checkbox空间进行AMAZE UI的CSS渲染
	enableAllCheckboxStyle($(container).find(':checkbox'));
}

/**
 * 生成权限复选框组HTML代码并返回
 * @param permissions - 用户拥有的所有权限
 * @param element - 每个独立权限复选框的id与name
 * @param subGroupId - 生成权限分组复选框的id
 * @returns 分配权限功能下权限复选框组HTML代码
 */
function generatePermissionCheckbox0(permissions, element, subGroupId) {
	//最新权限分组的分组名称
	var lastGroup = null;
	//装载权限复选框组HTML的容器
	var content = [];
	content.push('<div class="am-form-group" style="width: 100%; height: ');
	content.push(allotModalWindowPermissionsAreaHeight);
	content.push('; overflow-y: scroll;">');
	
	for(r in allPermissionData) {
		var permission = allPermissionData[r];
		
		//如果当前权限与上一个权限信息来自不同分组，则建立新的分组
		if (permission.group != lastGroup) {
			lastGroup = permission.group;
			if (r != 0) {
				content.push('</div>');
				content.push('<br>');
			}
			content.push('<label class="am-checkbox-inline">');
			content.push('<input type="checkbox" id="');
			content.push(subGroupId);
			content.push('_');
			content.push(permission.id);
			content.push('_all');
			content.push('" data-am-ucheck class="group_checkbox" onclick="javascript:groupChecker(this)">');
			content.push('<strong>')
			content.push(lastGroup);
			content.push('</strong>')
			content.push('</label>  - ');
			content.push('<div class="');
			content.push(subGroupId);
			content.push('">');
		}
		
		//开始针对当前权限拼接新的复选框控件
		content.push('<label class="am-checkbox-inline">');
		content.push('<input type="checkbox" name="')
		content.push(element)
		content.push('Ids" value="');
		content.push(permission.id);
		content.push('" data-am-ucheck ');
		//选中当前用户拥有的权限
		if (identifyOwnedPermission(permissions, permission)) content.push(' checked');
		content.push(' onclick="javascript: checkPermissionGroupCheckbox()"> ');
		content.push(permission.name);
		content.push('</label>');
	}
	content.push('</div>'); // end of subgroup div
	content.push('</div>');
	
	return content.join('');
}

/**
 * 业务特定方法 - 当选中所有或取消任意一个subgroup中的复选框时，其最前的全选复选框将被置为选中状态或未选中状态
 * @param container
 */
function checkPermissionGroupCheckbox(container) {
	if (!container) container = $('#allot_permission_checkbox_container');
	checkGroup($(container).find('div.group'));
}

/**
 * 在所有权限中识别当前用户拥有的权限，以便对其进行标识(勾选其复选框)
 * @param permissions - 所有权限
 * @param permission - 一个用户拥有的权限
 */
function identifyOwnedPermission(permissions, permission) {
	for(t in permissions)
		if (permissions[t].id == permission.id)  
			return true;
	return false;
}

/**
 * 当一个subgroup中的所有复选框都被选中时，该subgroup最前面所对应的全选复选框，将被置为选中状态
 * @param selector - 所有subgroup的选择器
 */
function checkGroup(selector) {
	if (!selector) {
		selector = $()
	}
	$(selector).each(function() {
		var isGroupSelected = true;
		$(this).find(':checkbox').each(function() {
			if (this.checked == false) {
				isGroupSelected = false;
			}
		});
		$(this).prev().children(0).prop('checked', isGroupSelected);
	});
}

/**
 * 当每个subgrou最前面所对应的全选复选框被选中或取消选中时，该subgroup中所有复选框也将随着全选复选框的选中状态而变化自己的选中状态
 * @param selector - 当前subgroup的全选复选框
 */
function groupChecker(selector) {
	var isGroupChecked = $(selector).is(':checked');
	$(selector).parent().next(0).find(':checkbox').prop('checked', isGroupChecked ? true : false);
}