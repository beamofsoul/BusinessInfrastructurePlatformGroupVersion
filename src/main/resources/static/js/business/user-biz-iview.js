/** 在未销毁vueContentObject之前 可自定义操作  **/
//##########################################################//
//销毁上一个content页面遗留vueContentObject对象实例
if (vueContentObject) getVueObject().$destroy();
//^^^^^^^^^^^^^^^^^^^^^^ 格式 顺序 不动 ^^^^^^^^^^^^^^^^^^^^^//

function validateUserName (rule,value,callback) {
	var form = getCurrentForm();
	$.iposty('user/checkUsernameUnique',{'data':value,'id':form.id}, function(data) {
		formValidateCallback(callback,data.isUnique,'用户名已被占用');
    });
}
function validatePass(rule,value,callback) {
	if (value === '') {
		formValidateCallback(callback,false,'请输入密码');
    } else {
        if (getCurrentForm().repassword !== '')
        	getVueRefObject(getCurrentFormName()).validateField('repassword');
        formValidateCallback(callback,true);
    }
}
function validatePassCheck(rule, value, callback) {
	if (value === '') {
		formValidateCallback(callback,false,'请输入确认密码');
	} else if (value !== getCurrentForm().password) {
		formValidateCallback(callback,false,'两次输入密码不一致!');
	} else {
		formValidateCallback(callback,true);
    }
}

vueContentBeforeCreate = function() {
	this.statusList = [{value: '1',label: '启用'},{value: '0',label: '禁用'}];
}

vueContentMounted = function () {this.loadPage()}


// 当前用户能够操作的所有行为
var actions = {'del': {'key':'del','url':'delete'},'add': {'key':'add','url':'user/singleAdd'},'update':{'key':'update','url':'user/singleUpdate'},'copy':{'key':'copy','url':'singleAdd'}};
//tableContainer中表格显示列的中文名称
var columnNames = ['','ID','昵称','用户名','密码','邮箱地址','电话号码','状态','注册日期','最后修改日期','操作'];
//tableContainer中表格每列对应的业务模型实体类的属性名  全选 加 'selection' 项 , 操作 加 'operation' 项。
var attributeNames = ['selection','id','nickname','username','password','email','phone','status','createDate','modifyDate','operation'];
//tableContainer中表格每列需要的按钮 
var buttonsOnEachRow = ['rowUpdateButton#修改','rowDeleteButton#删除'];
//分页取数据url
loadPageableDataUrl = 'user/usersByPage';
//综合查询 form
var queryFormItemName = ['ID','昵称','用户名','密码','邮箱地址','电话号码','状态','注册日期'];
var queryFormItems = ['id','nickname','username','password','email','phone','status','createDate'];
var queryFormItemType = ['string','string','string','string','string','string','select#statusList','date'];

//##########################################################//
//table 创建列头
tableColumnDatas = createTable(columnNames,attributeNames,buttonsOnEachRow);
//^^^^^^^^^^^^^^^^^^^^^^ 格式 顺序 不动 ^^^^^^^^^^^^^^^^^^^^^//

//格式化table行数据格式
parseValuesOnTableEachRow = function (obj) {
	return {id :obj.id,
		nickname :obj.nickname,
		username :obj.username,
		password :obj.password,
		email :obj.email,
		phone :obj.phone,
		status :obj.status == 1 ? '可用' : '冻结',
		createDate:formatDate(obj.createDate,true),
		modifyDate:formatDate(obj.modifyDate,true)};
}

$('#queryFormDiv').html(createTableQueryFrom(queryFormName,queryFormItemName,queryFormItems,queryFormItemType));

//////////////////////////////自定义数据与方法 ////////////////////////////
queryFormContent = {id:'',name:'',status: '',createDate: '',username: ''};//考虑生成 如果生成 可拿走

var generalFormContent = {	id:-1,username: '',password: '',repassword: '',nickname: '',phone: '',email: '',status: '1'};//考虑生成 form是否和table结构相同
addFormContent = updateFormContent = generalFormContent;

var generalValidataionContent = {
	'username': [{ required: true,  min: 6, message: '用户名要大于6个字符', trigger: 'blur' },{ validator: this.validateUserName, trigger: 'blur'}],
	'password': [{ required: true,validator: this.validatePass, trigger: 'blur' }],
    'repassword': [{ required: true,validator: this.validatePassCheck, trigger: 'blur' }]
}
addFormValidateContent = updateFormValidateContent = generalValidataionContent;

//##########################################################//
var vueContentObject = new Vue(initializeContentOptions());
//^^^^^^^^^^^^^^^^^^^^^^ 格式 顺序 不动 ^^^^^^^^^^^^^^^^^^^^^//
/** 初始化vueContentObject之后 可自定义操作  **/


