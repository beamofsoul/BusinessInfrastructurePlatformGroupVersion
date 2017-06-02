//销毁上一个content页面遗留vueContentObject对象实例
if (vueContentObject)
    getVueObject().$destroy();
// 当前用户能够操作的所有行为
var actions = {'update': {'key': 'update', 'url': 'singleUpdate'}};
//table column 显示名
tableColumnsName = ['ID', 'UserID', '昵称', '用户名', '角色', '操作'];
//table column 对应data中的属性名
tableColumnsKey = ['id', 'userId', 'nickname', 'username', 'roleName', 'operation'];
//table 每行需要的按钮
tableButtonsOnEachRow = ['clickRowSetRoleButton#设置'];
//分页取数据url
loadPageableDataUrl = 'userRolesByPage';
//格式化table行数据格式
parseValuesOnTableEachRow = function (obj) {
    return {
        id: obj.id,
        userId: obj.userId,
        nickname: obj.nickname,
        username: obj.username,
        roleName: obj.roleName === '' ? '暂无角色' : obj.roleName};
};
//设置add update vue form data obj
//setFormDataObject({id: -1, name: '', priority: ''});

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
        vueSetRoleTreeData: loadSetRoleAllRole2TreeData(),
        vueSetRoleFormData: {userId: 0, nickName: '', userName: '', allRoles: [], userRoles: []},
        vueSetRoleCondition: null,
        vueSetRoleModalVisible: false
    };
};
hasQueryForm = false; //是否有queryForm

// 自定义 Module
vueContentMethods.loadSetRoleAllRole2TreeData = loadSetRoleAllRole2TreeData;
vueContentMethods.onSelectChangeTree = onSelectChangeTree;
vueContentMethods.clickRowSetRoleButton = clickRowSetRoleButton;
vueContentMethods.loadSetRoleUserRole = loadSetRoleUserRole;
vueContentMethods.submitSetRoleForm = submitSetRoleForm;
vueContentMethods.cancelSetRoleForm = cancelSetRoleForm;
vueContentMethods.loadUserRolesBypage = loadUserRolesBypage;
//调入所有权限信息并初始化
function loadSetRoleAllRole2TreeData() {
    let curSetRoleTreeData = [{title: '角色列表', expand: true, selected: true, children: []}];
    $.iposty('all', {}, function (data) {
        for (let role of data.all) {
            curSetRoleTreeData[0].children.push({title: role.name, id: role.id});
        }
    });
    return curSetRoleTreeData;
}
//
function onSelectChangeTree() {
    let obj = this.vueSetRoleTreeData;
    if (obj[0].selected) {
        this.vueSetRoleCondition = null;
        //loadUserRolesBypage(vueCurrentPage - 1, this[currentPageSizeName], null);
        this.doLoadPage();
    } else {
        this.vueSetRoleCondition = {};
        for (let children of obj[0].children) {
            if (children.selected) {
                this.vueSetRoleCondition.ids = children.id;
                break;
            }
        }
        loadUserRolesBypage(0, this[currentPageSizeName], this.vueSetRoleCondition);
    }
}
//
function loadUserRolesBypage(page, size, condition) {
    $.iposty("userRolesByPage", {page: page, size: size, condition: condition}, function (data) {
        vueContentObject.vueTableData = [];
        for (let rowContent of data.pageableData.content) {
            let row = {};
            for (let key of tableColumnsKey) {
                row[key] = rowContent[key];
            }
            vueContentObject.vueTableData.push(row);
        }
        vueContentObject.vueRecordTotal = data.pageableData.totalElements;
        setTimeout(toastLoading('正在加载中...', 0), 30);
    });
}
//
function clickRowSetRoleButton(index, tableDataName) {
    this.vueSetRoleFormData.allRoles = [];
    let formdata = this.vueSetRoleFormData;
    formdata.userId = this.vueTableData[index].userId;
    formdata.nickName = this.vueTableData[index].nickname;
    formdata.userName = this.vueTableData[index].username;
    for (let children of this.vueSetRoleTreeData[0].children) {
        formdata.allRoles.push({id: children.id, name: children.title});
    }
    this.loadSetRoleUserRole(formdata.userId);
    this.vueSetRoleModalVisible = true;
}
//调入角色相应的权限并更新
function loadSetRoleUserRole(userId) {
    $.iposty("singleUserRoleCombineRole", {id: userId}, function (data) {
        vueContentObject.vueSetRoleFormData.userRoles = data.obj.roleName.split(",").slice(0);
    });
}
//Modal 提交按钮
function submitSetRoleForm() {
    let formdata = this.vueSetRoleFormData;
    if (formdata.userRoles[0] === '') {
        formdata.userRoles.splice(0, 1);
    }
    let roleId = "";
    for (let userRole of formdata.userRoles) {
        for (let allRole of formdata.allRoles) {
            if (userRole === allRole.name) {
                roleId += ((roleId === "") ? "" : ",") + allRole.id;
                break;
            }
        }
    }
    $.iposty("setUserRoles", {userId: this.vueSetRoleFormData.userId, roleId: roleId}, function (data) {
        vueContentObject.loadUserRolesBypage(vueCurrentPage - 1, vueContentObject[currentPageSizeName], vueContentObject.vueSetRoleCondition);

    });
    this.cancelSetRoleForm();
}
//Modal 取消按钮 / X按钮
function cancelSetRoleForm() {
    this.vueSetRoleFormData = {userId: 0, nickName: '', userName: '', allRoles: [], userRoles: []};
    this.vueSetRoleModalVisible = false;
}

var vueContentObject = new Vue(initializeContentOptions());
