//销毁上一个content页面遗留vueContentObject对象实例
if (vueContentObject)
    getVueObject().$destroy();
// 当前用户能够操作的所有行为
var actions = {
    'del': {'key': 'del', 'url': 'delete'},
    'add': {'key': 'add', 'url': 'singleAdd'},
    'update': {'key': 'update', 'url': 'singleUpdate'},
    'copy': {'key': 'copy', 'url': 'singleAdd'}
};
//table column 显示名
tableColumnsName = ['', 'ID', '名称', '优先级', '创建日期', '修改日期', '操作'];
//table column 对应data中的属性名   全选 加 'selection' 项 , 操作 加 'operation' 项。
tableColumnsKey = ['selection', 'id', 'name', 'priority', 'createDate', 'modifyDate', 'operation'];
//table 每行需要的按钮
tableButtonsOnEachRow = [
    'rowUpdateButton#修改',
    'rowCopyButton#复制',
    'rowDeleteButton#删除'];
//分页取数据url
loadPageableDataUrl = 'rolesByPage';
//格式化table行数据格式
parseValuesOnTableEachRow = function (obj) {
    //alert(JSON.stringify(obj));
    return {id: obj.id,
        name: obj.name,
        priority: obj.priority,
        createDate: formatDate(obj.createDate, true),
        modifyDate: formatDate(obj.modifyDate, true)};
};
//设置add update vue form data obj
setFormDataObject({id: -1, name: '', priority: ''});
//综合查询 form
var queryFormItemName = ['ID', '名称', '优先级', '创建日期'];
var queryFormItemKey = ['id', 'name', 'priority', 'createDate'];
var queryFormItemType = ['string', 'string', 'string', 'date'];
//form 验证信息
//var generalValidataionContent = {};
//setValidataionContent(generalValidataionContent);

//排序方法
vueContentMethods.vueBindTableSortMethod = function (a, b, c, d, e) {
    this.vueTableLoadPageMethod();
    console.log(a);
    console.log(b);
    console.log(e);
    console.log(d);
    console.log(e);
};
// 自定义 Data
vueContentBeforeCreate = function () {
    customVueContentData = {
        vueAllotPermissionForm: {items: []}, //() => {items:[]},
        vueAllotModalVisible: false
    };
};
// 自定义 Module
vueContentMethods.doAllotButton = doAllotButton;
vueContentMethods.loadAllotPermissionAll = loadAllotPermissionAll;
vueContentMethods.generateItemObj = generateItemObj;
vueContentMethods.loadAllotRolePermission = loadAllotRolePermission;
vueContentMethods.resetAllotForm = resetAllotForm;
vueContentMethods.clickAllotCheckAll = clickAllotCheckAll;
vueContentMethods.changeAllotCheckAll = changeAllotCheckAll;
vueContentMethods.submitAllotForm = submitAllotForm;
vueContentMethods.cancelAllotForm = cancelAllotForm;
//点击'分配权限'按钮，弹出AllotForm Modal
function doAllotButton() {
    var checkedRows = this[currentCheckedTableRowName];
    if (checkedRows.length === 0) {
        toastInfo('至少选中1条记录!');
        return;
    }
    this.loadAllotPermissionAll();
    this.vueAllotModalVisible = true;
}
//调入所有权限信息并初始化
function loadAllotPermissionAll() {
    $.get(projectRootPath + '/admin/permission/allAvailable', function (data) {
        var array = data.all;
        var arrayLength = array.length;
        if (arrayLength !== 0) {
            var curGroup = array[0].group, curIds = [], curNames = [], curOriginals = [], curValues = [];
            for (var i = 0; i < arrayLength; i++) {
                if (curGroup !== array[i].group) {
                    vueContentObject.vueAllotPermissionForm.items.push(
                        generateItemObj(false, false, curGroup, curIds, curNames, curOriginals, curValues));
                    curGroup = array[i].group;
                    curIds = [];
                    curNames = [];
                    curOriginals = [];
                    curValues = [];
                }
                curIds.push(array[i].id);
                curNames.push(array[i].name);
                curOriginals.push(false);
                curValues.push(false);
            }
            vueContentObject.vueAllotPermissionForm.items.push(
                generateItemObj(false, false, curGroup, curIds, curNames, curOriginals, curValues));
            vueContentObject.loadAllotRolePermission(vueContentObject.vueCheckedTableRow[0].id);
        }
    });
}
function generateItemObj(indeterminate, checkAll, group, ids, names, originals, values) {
    var obj = {};
    obj.indeterminate = indeterminate;
    obj.checkAll = checkAll;
    obj.group = group;
    obj.ids = ids;
    obj.names = names;
    obj.originals = originals;
    obj.values = values;
    return obj;
}
//调入角色相应的权限并更新
function loadAllotRolePermission(roleId) {
    $.iposty("single", {id: roleId}, function (data) {
        var curRoleId = data.obj.id;
        var curRolePermission = [];
        for (var i = 0, l = data.obj.permissions.length; i < l; i++) {
            curRolePermission.push(data.obj.permissions[i].id);
        }
        var array = vueContentObject.vueAllotPermissionForm.items;
        for (var i in array) {
            for (var j in array[i].ids) {
                if (curRolePermission.indexOf(array[i].ids[j], 0) !== -1) {
                    array[i].originals[j] = true;
                }
            }
        }
        vueContentObject.resetAllotForm();
    });
}
//分配权限Modal ReSet
function resetAllotForm() {
//this.$refs[name].resetFields(); 此功能不稳定!
    var arrayObj = this.vueAllotPermissionForm.items;
    for (var i in arrayObj) {
        arrayObj[i].values = arrayObj[i].originals.slice(0);
        this.changeAllotCheckAll(i);
    }
}
//分配权限Modal 手动全选/全释放
function clickAllotCheckAll(index) {
    var arrayItem = this.vueAllotPermissionForm.items[index];
    if (arrayItem.indeterminate) {
        arrayItem.indeterminate = false;
        arrayItem.checkAll = true;
    } else {
        arrayItem.checkAll = !arrayItem.checkAll;
    }
    for (var v in arrayItem.values) {
        arrayItem.values[v] = arrayItem.checkAll;
    }
}
//分配权限Modal 当每单选时,是否判断被全选/全释放
function changeAllotCheckAll(index) {
    var arrayItem = this.vueAllotPermissionForm.items[index];
    var trueCount = 0, falseCount = 0;
    var curLength = arrayItem.values.length;
    for (var i = 0; i < curLength; i++) {
        if (arrayItem.values[i]) {
            trueCount++;
        } else {
            falseCount++;
        }
    }
    arrayItem.indeterminate = (trueCount === curLength || falseCount === curLength) ? false : true;
    arrayItem.checkAll = trueCount === curLength ? true : false;
}
//Modal 提交按钮
function submitAllotForm() {
    var roleId = this.vueCheckedTableRow[0].id;
    var permissionIds = [];
    var array = vueContentObject.vueAllotPermissionForm.items;
    for (var i in array) {
        for (var j in array[i].originals) {
            if (array[i].values[j]) {
                permissionIds.push(array[i].ids[j]);
            }
        }
    }
    $.iposty("allotPermissionsToRole",
        {roleId: roleId, permissionIds: permissionIds},
        function (data) {
            if (data.allotted) {
                toastInfo('分配权限成功!');
            }
        });
    this.cancelAllotForm();
}
//Modal 取消按钮 / X按钮
function cancelAllotForm() {
    this.vueAllotPermissionForm = {items: []};
    this.vueTableData = this.vueTableData.slice(0);
    this.vueCheckedTableRow = [];
    this.vueAllotModalVisible = false;
}

var vueContentObject = new Vue(initializeContentOptions());
