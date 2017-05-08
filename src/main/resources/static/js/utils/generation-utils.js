var permissionHolder = []; //记录当前用户所有验证通过的权限，以备避免同权限再次查询

var noCheckbox = false; //是否不为数据表格生成第一列复选框

var currentButtonName = null; //当前解析的按钮的名字
var currentButtonPermissive = true; //当前用户是否拥有当前按钮的权限， 默认不加权限控制，也就是拥有权限
var buttonSecurityKey = null; //每条记录后按钮的安全秘钥，一般为业务模块名称，如'user'、'role'或'permission'

var currentHideColumns = []; //当前数据表格被隐藏的列
var currentHideColumnNames = []; //当前数据表格隐藏列对应的列字段名称

var defaultValueSize = 20; //当前行当前列td中显示属性值的默认最大长度，只对字符串有效
var currentValueSize = []; //当前行当前列td中显示属性值的用户自定义最大长度，只对字符串有效

$(document).ready(function() {
	//手动实现并注册全局String.startsWith方法
	if (typeof String.prototype.startsWith != 'function') {
		String.prototype.startsWith = function (prefix){
			return this.slice(0, prefix.length) === prefix;
		};
	}
});

/**
 * 根据输入的自定义按钮详细信息数组查询是否当前用户拥有使用当前自定义按钮的权限
 * @param buttonDetailsArray - 按钮详细信息数组
 * @result boolean value - true: 拥有使用当前按钮权限, false: 没有使用该按钮权限
 */
function checkButtonPermission(buttonDetailsArray) {
	// 如果之前已经判断了当前同类型的按钮的权限，则直接返回是否拥有该按钮权限，不再重新判断
	if (currentButtonName != null && currentButtonName != '' && currentButtonName == buttonDetailsArray[0]) {
		return currentButtonPermissive;
	}
	// 获取当前按钮名称，并默认用户拥有当前按钮的使用权限
	currentButtonName = buttonDetailsArray[0];
	currentButtonPermissive = true;
	// 查询是否有权限
	if (buttonDetailsArray.length > 4) {
		var securityAction = buttonDetailsArray[4];
		// 如果曾经验证过拥有该权限，则直接返回拥有该权限而不去后台查询
		if ($.inArray(securityAction, permissionHolder) > -1) {
			return currentButtonPermissive;
		}
		// 去后台查询权限
		if (securityAction != null && securityAction.trim() != '' && securityAction.indexOf(':') != -1) {
			currentButtonPermissive = checkPermission(securityAction);
			// 当查询到当前用户拥有使用当前按钮的权限时，将权限存起以备下次检查之用
			if (currentButtonPermissive) {
				permissionHolder.push(securityAction);
			}
		}
	}
	return currentButtonPermissive;
}

/**
 * 通过输入的key与业务对象id判断该默认的(update\copy\delete)按钮是否需要进行权限控制
 * @param key - 按钮映射值与名字 - eg. update#修改?
 * @param action - 权限值 - permission:update
 * @returns boolean value - true: 拥有权限, false: 不拥有权限
 */
function checkDefaultButtonPermission(key, action) {
	var required = key.indexOf('?') != -1;
	//判断key中是否有字符'?'，如果存在则说明当前按钮需要进行权限控制
	//反之则说明当前按钮不需要权限控制，当前用户默认拥有对该按钮操作的权限
	if (required) {
		var secButtonDetailsArray = [key,null,null,null,action]; 
		return checkButtonPermission(secButtonDetailsArray);
	}
	return true;
}

/**
 * 根据输入的按钮关键字和数据对象id自动解析关键字对应的按钮并返回
 * @param key - 当前按钮信息，如系统默认按钮'update#修改?', 格式为'默认按钮关键字#按钮显示名称?'，其中'?'标识需要进行权限控制
 *				或自定义按钮信息'设置#am-icon-gear#am-text-secondary#set(event,this,?id)#role:allot'，
 *				自定义按钮格式为'按钮显示名称#图标#样式#单击方法#权限控制对应的action'
 * @param id - 当前业务记录的主键ID
 */
function parseButton(key,id) {
	var style, icon, action, name, method = null;
	var buttonDetailsArray = key.split('#');
	if (key.startsWith('update')) {
		style = 'am-btn am-btn-default am-btn-xs am-text-secondary';
		icon = 'am-icon-pencil-square-o';
		action = buttonSecurityKey + ':update';
		method = 'updateRow';
		name = buttonDetailsArray.length > 1 ? buttonDetailsArray[1].replace('?','') : '编辑';
		return checkDefaultButtonPermission(key,action) ? 
				generateDefaultButton(id,name,method,style,icon) : '';
	} else if (key.startsWith('copy')) {
		style = 'am-btn am-btn-default am-btn-xs am-hide-sm-only';
		icon = 'am-icon-copy';
		action = buttonSecurityKey + ':copy';
		method = 'copyRow';
		name = buttonDetailsArray.length > 1 ? buttonDetailsArray[1].replace('?','') : '复制';
		return checkDefaultButtonPermission(key, action) ? 
				generateDefaultButton(id,name,method,style,icon) : '';
	} else if (key.startsWith('delete')) {
		style = 'am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only';
		icon = 'am-icon-trash-o';
		action = buttonSecurityKey + ':delete';
		method = 'deleteRow';
		name = buttonDetailsArray.length > 1 ? buttonDetailsArray[1].replace('?','') : '删除';
		return checkDefaultButtonPermission(key, action) ? 
				generateDefaultButton(id,name,method,style,icon) : '';
	} else {
		if (buttonDetailsArray.length == 1) {
			buttonDetailsArray[1] = ('am-icon-pencil-square-o');
			buttonDetailsArray[2] = ('am-btn am-btn-default am-btn-xs am-text-secondary');
			buttonDetailsArray[3] = ('');
		} else if (buttonDetailsArray.length == 2) {
			buttonDetailsArray[2] = ('am-btn am-btn-default am-btn-xs am-text-secondary');
			buttonDetailsArray[3] = ('');
		} else if (buttonDetailsArray.length == 3) {
			buttonDetailsArray[3] = ('');
		}
		var buttonContent = '<button onclick="'+buttonDetailsArray[3]+'" class="customButton'+id+' '+buttonDetailsArray[2]+'"><span class="'+buttonDetailsArray[1]+'"></span> '+buttonDetailsArray[0]+'</button>';
		// 如果当前用户没有使用此按钮权限，则不显示该按钮
		if (!checkButtonPermission(buttonDetailsArray)) return '';
		return buttonContent.replace('?id',id);
	}
}

function generateDefaultButton(id,name,method,style,icon) {
	var content = [];
	content.push('<button onclick="');
	content.push(method);
	content.push('(event,');
	content.push(id);
	content.push(')" class="defaultButton');
	content.push(id);
	content.push(' ');
	content.push(style);
	content.push('"><span class="');
	content.push(icon);
	content.push('"></span> ');
	content.push(name);
	content.push('</button>');
	return content.join('');
}

function generateDefualtDataTableTh(column,name) {
	var content = [];
	content.push($.inArray(column, currentHideColumns) == 0 ? '<th hidden="true" name="th_' : '<th name="th_');
	content.push(name);
	content.push('">');
	content.push(column);
	content.push('</th>');
	return content.join('');
}

function generateDefaultDataTableTd(name,value) {
	var content = [];
	if (!noCheckbox) {
		content.push('<td><input type="checkbox" onclick="invertSelectRow(this)" class="idc" id="');
		content.push(value[0]);
		content.push('"></td>');
	}
	var currentValue = currentName = null;
	var isNotOperation = false;
	for(var j in value) {
		currentValue = value[j];
		currentName = name[j];
		isNotOperation = currentName != 'operation';
		//如果当前列被设置为隐藏，则隐藏当前td，否则正常显示
		content.push($.inArray(currentName, currentHideColumnNames) == 0 ? '<td hidden="true" name="' : '<td name="')
		content.push(currentName);
		//如果当前td内容不为按钮，则需要为当前td增加title属性
		if (isNotOperation) {
			content.push('" title="');
			content.push(currentValue);
		}
		content.push('">');
		//如果当前td内容不为按钮，则需要为td的内容进行长度限制
		if (isNotOperation) {
			//如果当前字段值不是字符串则原样输出，如果是字符串则进行长度限制
			content.push((typeof currentValue == 'string') ? currentValue.cut(currentValueSize[j]) : currentValue);
		} else {
			content.push('<div class="am-btn-toolbar"><div class="am-btn-group am-btn-group-xs">');
			for(var k in currentValue) {
				content.push(parseButton(currentValue[k],value[0]));
			}
			content.push('</div></div>');
		}
		content.push('</td>');
	}
	return content.join('');
}

function generateDefaultDataTableTr(name,value) {
	var content = [];
	content.push('<tr onclick="selectRow(this)" id="');
	content.push(value[0]);
	content.push('">');
	content.push(generateDefaultDataTableTd(name,value));
	content.push('</tr>');
	return content.join('');
}

/**
 * 根据输入的表头列、显示属性名和显示属性值自动生成默认风格数据表格
 * @param column - 数据表表头显示的所有列
 * @param name - 表头显示列对应的对象属性名,'operation'为按钮组名称,不可更改
 * @param value - 存储数据表中每一条数据,每条数据中属性值顺序必须与column和name中排列的属性一致
 * @param nock - 是否不生成的数据表格第一列的复选框
 * @param btnSecurityKey - 每条记录后按钮的安全秘钥，一般为业务模块名称，如'user'、'role'或'permission'
 * @returns 以HTML形式存在的数据表
 */
function generateDefaultDataTable(column,name,value,nock,btnSecurityKey) {
	//拼接数据表表头
	var content = [];
	content.push('<table id="dataTable" class="am-table am-table-striped am-table-hover table-main">');
	content.push('<thead><tr>');
	//将按钮的安全秘钥设置为公有变量
	buttonSecurityKey = btnSecurityKey;
	//判断是否在第一列生成复选框列
	noCheckbox = nock;
	if (!noCheckbox) {
		content.push('<th class="table-check"><input type="checkbox" id="all" /></th>')
	}
	for(var i in column) {
		var col = generateSpecificColumn(column[i],name[i]);
		content.push(generateDefualtDataTableTh(col,name[i]));
	}
	content.push('</tr></thead><tbody>');
	//拼接数据表内容
	for(var i in value) {
		content.push(generateDefaultDataTableTr(name,value[i]));
	}
	
	//如无数据,提示无记录
	if(value == []) {
		content.push(('<tr><td colspan="'+(column.length + (noCheckbox ? 0 : 1))+'">未查询到任何记录...</td></tr>'));
	}
	//拼接数据表表尾
	content.push('</body></table>');
	
	return content.join('');
}

function generateSpecificColumn(column,name) {
	//截取该列属性值的长度
	var cutSize = column.match(specificCutReg);
	currentValueSize.push(cutSize != null ? cutSize[0].replace(/[^0-9]/ig,"") : defaultValueSize);
	column = column.replace(specificCutReg,'');
	
	//隐藏
	if (column.indexOf(generalHide) != -1) {
		column = column.replace(generalHide,'');
		currentHideColumns.push(column);
		currentHideColumnNames.push(name)
	} 
	//排序
	else if (column.indexOf(generalSort) != -1) {
		column = '<div style="float:left;">' + column.replace(generalSort,'') + '</div>' + '<div style="text-align: center;height: 10px;width:20px;float:left;">' +
			 '<a href="javascript:initPageableData(0,true,'+globalSize+',\''+name+'\',0)" class="am-icon-sort-asc" style="display:block; height: 50%;width: 12px;"></a><a href="javascript:initPageableData(0,true,'+globalSize+',\''+name+'\',1)" class="am-icon-sort-desc" style="display:block; height: 50%;width: 12px;"></a>' + '</div>';
	}
	return column;
}

var generalSort = "#S#";
var generalHide = '#H#';
var specificCutReg=new RegExp(/(#)(C)(\d+)(#)/g);
//var numericSort = "#NS#";
