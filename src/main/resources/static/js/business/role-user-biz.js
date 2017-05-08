//初始化完毕后记录表单所有属性值，用于后期提交修改时判断表单属性值是否被修改
var initializedFormData = {};
//加载角色树数据需要链接后台方法的URL地址
var loadTreeDataUrl = 'all';
//保存完整角色树中角色数据
var allRoleData = null;

$(document).ready(function() {
	initTreeData();
});

/**
 * 根据角色业务对象主键Id初始化角色树控件
 * @param roleId - 角色业务对象主键Id
 */
function initTreeData(roleId) {
	//链接后台方法的需要的输入参数
	var data = [];
	//此次方法是否获得的是完整得角色树数据
	var isAll = true;
	//根据获取角色业务对象主键Id对所有角色树数据进行筛选
	if (roleId) {
		isAll = false;
		data.push({roleId: roleId});
	}
	$.ajax({
	    type: 'POST',
	    url: loadTreeDataUrl,
	    data: data,
	    dataType: 'json',
	    success: function(data) {
	    	allRoleData = data.all;
	    	var treeData = generateTree('角色列表', data.all);
	    	buildTree(function(parentData, callback){
	    		callback({data: parentData.subs || treeData});
	    	});
	    },
	    error: function(XMLHttpRequest, textStatus, errorThrown) { 
	        console.log(XMLHttpRequest.responseText);
	    }
	});
}

/**
 * 渲染并设置角色树控件
 * @param dataSource - 渲染并填充角色树控件所需的数据源
 * @param options - 角色树控件可选设置
 */
buildTree = function(dataSource, options) {
	if (this.options) {
		var multiSelect = true; //是否可以多选
		if (this.options.multiSelect) {
			multiSelect = this.options.multiSelect;
		}
		var cacheItems = true; //是否缓存节点
		if (this.options.cacheItems) {
			cacheItems = this.options.cacheItems;
		}
		var folderSelect = true; //是否可以选择文件夹(包含子节点的节点)
		if (this.options.folderSelect) {
			folderSelect = this.options.folderSelect;
		}
	}
	
	$('#dataTree').tree({
		dataSource: dataSource,
		multiSelect: multiSelect,
		cacheItems: cacheItems,
		folderSelect: folderSelect
	}).tree('discloseAll');
	
	$('#dataTree').on('selected.tree.amui', function (event, data) {
		treeNodeSelected(event,data);
	});
}

/**
 * 通过输入的标题与子节点的数据生成整个角色树数据对象
 * @param title 生成根节点需要的标题
 * @param data 生成根节点下子节点需要的数据对象
 */
generateTree = function(title, data) {
	return fillTree(generateRoot(title), generateSubs(data));;
}

/**
 * 通过输入的标题生成根节点
 * @param title 生成根节点需要的标题(根节点显示名)
 */
generateRoot = function(title) {
	return [{title: title, type: 'folder', attr: {id: 0}}];
}

/**
 * 通过输入参数解析解析并生成子节点
 * @param data 包含子节点数据的对象
 */
generateSubs = function(data) {
	var subs = [];
	for(r in data) {
		var rd = data[r];
		subs.push({title: rd.name, type: 'item', attr: {id: rd.id}});
	}
	return subs;
}

/**
 * 填充角色树根节点的子节点
 * @param root 根节点
 * @param subs 根节点的子节点
 */
fillTree = function(root, subs) {
	root[0].subs = subs;
	return root;
}

/**
 * 角色树选中事件
 * @param event - 树选中事件对象
 * @param data - 树的数据对象
 */
treeNodeSelected = function(event, data) {
	var itemCode = data.selected[0].attr.id;
	if (itemCode == 0) {
		//root node被选中,获取所有用户角色数据
		initPageableDataByCondition(0,true,globalSize);
	} else {
		//判断被选中的是folder还是item
		if (data.selected[0].type == 'item') {
			//如果是item,重新加载用户角色数据并按照当前角色进行筛选
			initPageableDataByCondition(0,true,globalSize,{'ids': itemCode});
		} else {
			//如果是folder,重新加载用户角色数据并按照当前选中folder下所有角色进行筛选
			var subs = data.selected[0].subs;
			var ids = parseSubs(subs);
			initPageableDataByCondition(0,true,globalSize,{'ids': ids});
		}
	}
}

/**
 * 解析并返回以井号间隔的所选角色记录主键Id的字符串
 * @param subs 当前选中节点包含的子节点
 * @returns String 以井号间隔的所选角色记录主键Id
 */
function parseSubs(subs) {
	var content = [];
	if (subs.length) {
		for(i in subs) {
			var sub = subs[i];
			if (sub.type == 'item') {
				content.push(sub.attr.id);
			} else {
				content.push(parseSubs(sub.subs));
			}
		}
	}
	return content.join('#');
}

//tableContainer中表格显示列的中文名称
var columnNames = ['ID','昵称','用户名','角色','操作'];
//tableContainer中表格每列对应的业务模型实体类的属性名
var attributeNames = ['id','name','username','roleName','operation'];
//tableContainer中表格每列需要的按钮
var buttonsOnEachRow = ['设置#am-icon-gear#am-btn am-btn-default am-btn-xs am-text-secondary#set(event,this,?id)#role:allot'];

/*****************************************************************************************
 *************************************** LIST PAGE ***************************************
 *****************************************************************************************/
loadPageableDataUrl = 'userRolesByPage'; //加载分页数据对应的后台请求映射URL
loadPageableDataCallback = function(data) { //pagination-utils.js 获取完分页数据后自动生成数据表格
	data = data.pageableData;
	var value = [];
	for(var i=0;i<data.numberOfElements;i++) {
		value[i] = parseValuesOnEachRow(data.content[i]);
	}
	var noCheckbox = true;
	return generateDefaultDataTable(columnNames,attributeNames,value, noCheckbox);
}

/**
 * 如何将输入参数obj的值赋值给tableContainer中每条记录的属性
 * @param obj - tableContainer中每条记录针对的业务模型实体类对象实例
 * @returns 格式化后记载着tableContainer中一条记录中每列属性的数组
 */
function parseValuesOnEachRow(obj) {
	return [obj.userId,
		obj.nickname,
		obj.username,
		obj.roleName == '' ? '暂无角色' : obj.roleName,
		buttonsOnEachRow]
}

function set(e,btn,id) {
	$(btn).attr('data-am-modal','{target: "#doc-modal-update", closeViaDimmer: 0}');
	doFunctionWithSingleData(initUpdateForm,id);
}

/*****************************************************************************************
 ************************************** OTHER PAGES **************************************
 *****************************************************************************************/
documentReady = function() { //pagination-utils.js 绑定所有表单单击(目的为提交表单)事件
	$('#submitUpdate').click(function() {
		var roleId = [];
		$('#updateForm').find(':checkbox:checked').each(function() {
			roleId.push($(this).val());
		});
		$('#update_role_id').val(roleId.join(','));
		submitForm(true,'setUserRoles','updateForm',function(){$('#closeUpdate').click();},true);
	});
	$('#closeUpdate').click(function() {doCheckAll(false);});
};

/**
 * 初始化update表单数据
 */
initUpdateForm = function(obj) {initForm('update',obj)} //basic-button-utils.js

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
			$('table#dataTable').find('tr#'+data.updated.userId)
			.html(generateDefaultDataTableTd(
					attributeNames,parseValuesOnEachRow(data.updated)));
		}
	}
	callback();
}

/**
 * 根据输入的业务对象实例主键Id，获取所对应的业务对象实例
 * @param fn - 获取到业务对象实例后，所需执行的回调方法
 * @param id - 业务对象实例对应的主键Id
 */
var doFunctionWithSingleData = function(fn,id) {
	$.ajax({
		headers: {'Accept': 'application/json','Content-Type': 'application/json'},
	    cache: false,
        async: false,
	    type: 'POST',
	    url: 'singleUserRoleCombineRole',
	    data: JSON.stringify({'id':id}),
	    dataType: 'json',
	    success: function(data) {
	    	var obj = data.obj;
			if(fn) fn(obj);
	    }
	});
};

/**
 * 初始化update与copy表单，当用户打开此模态窗口时
 * @param formKey - update
 * @param obj - 初始化表单所用的数据对象
 */
function initForm(formKey,obj) {
	$('#'+formKey+'_id').val(obj.id);
	$('#'+formKey+'_user_id').val(obj.userId);
	$('#'+formKey+'_username').val(obj.username);
	$('#'+formKey+'_nickname').val(obj.nickname);
	
	//生成角色复选框并初始化选中状态
	generateRoleCheckbox(formKey,obj);
	
	//初始化完毕后记录表单所有属性值，用于后期提交修改时判断表单属性值是否被修改
    if (formKey === "update")
      initializedFormData = $('#'+formKey + 'Form').serializeObject();
}

/**
 * 根据角色树内容在生成角色复选框，并初始化选中状态
 * @param formKey - update
 * @param obj - 初始化表单所用的数据对象
 */
function generateRoleCheckbox(formKey, obj) {
	var roleIds = obj.roleId.split(',');
	var element = 'role_id';
	var content = [];
	content.push('<div class="am-form-group">');
	for(r in allRoleData) {
		var role = allRoleData[r]
		content.push('<label class="am-checkbox-inline">');
		content.push('<input type="checkbox" id="');
		content.push(element);
		content.push('" value="');
		content.push(role.id);
		content.push('" data-am-ucheck ');
		if ($.inArray(role.id.toString(), roleIds) > -1) {
			content.push(' checked');
		}
		content.push('> ');
		content.push(role.name);
		content.push('</label>');
	}
	content.push('</div>');
	$('#'+formKey+'_'+element+'_checkbox_container').html(content.join(''));
	
	enableAllCheckboxStyle();
}