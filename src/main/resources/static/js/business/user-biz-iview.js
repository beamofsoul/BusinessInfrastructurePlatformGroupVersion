//销毁上一个content页面遗留vueContentObject对象实例
if (vueContentObject) vueContentObject.$destroy();

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
var queryFormItemType = ['input','input','input','input','input','input','select:statusList','date'];
$('#queryFormDiv').html(createTableQueryFrom('queryForm',queryFormItemName,queryFormItems,queryFormItemType));

//解析每行数据
function parseValuesOnEachRow(obj) {
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
//table 创建列头
var tableColumnDatas = createTable(columnNames,attributeNames,buttonsOnEachRow);

///////////////////////////////////////////////////////////////////////
/////////////////////////初始化Vue所需方法与数据 ///////////////////////////
//////////////////////////////////////////////////////////////////////

vueContentBeforeCreate = function() {this.statusList = [{value: '1',label: '启用'},{value: '0',label: '禁用'}]}
vueContentMounted = function () {this.loadPage()}
vueContentMethods = {
	// 加载页数据	
	loadPage () {
		var _self = this;
		const msg = this.$Message.loading('正在加载中...',0);
		clearTableCheckedData();
		
    	$.iposty(loadPageableDataUrl, {page: (_self.pageCurrent-1) , size: _self.pageSize,condition:formatQueryFormData(_self)}, function(data){
    		_self.tableData = formatTableData(data);// 分页数据
	    	_self.pageTotal = data.pageableData.totalElements;// 总记录数
	    	setTimeout(msg, 100);//销毁加载提示
		});
    },
    // 翻页
 	changePage (pageClick) {
		if (this.pageCurrent != pageClick) {
			this.pageCurrent = pageClick;
		}
 		this.loadPage();
    },
	/********** table头部按钮 ************/
	// 添加
	addButton (){
		formDataReset(addFormName);
		currentAction = actions.add;
		this.modalAdd = true;
	},
	submitAdd () {
		var _self = this;
		submitFormValidate(currentAction,function(data){
			_self.$Message.success('提交成功!');
			_self.modalAdd=false;
			formDataReset(getCurrentFormName());
		});
    },
	// 修改
	updateButton (){
		var _self = this;
		if(_self.tableCheckedData.length!=1){
			_self.$Message.error('请选择1条记录!');
			return;
		}

		currentAction = actions.update;
		this.modalUpdate = true;
    		
    	$.iposty('user/single', {'id':getTableCheckedDataIds(this.tableCheckedData)}, function(data){
    			_self.updateForm = data.obj;
    		});
	},
	submitUpdate(){
		var _self = this;
		submitFormValidate(currentAction,function(data){
			_self.$Message.success('更新成功!');
			_self.modalUpdate = false;
			formDataReset(getCurrentFormName());
		});
	},
	// 删除
	deleteButton (){
		if(this.tableCheckedData.length==0){
			this.$Message.error('至少选中一条记录!');
			return;
		}
		this.modalDelMessage = "即将删除"+this.tableCheckedData.length+"条记录,是否继续删除?";
		this.modalDelRowIds = getTableCheckedDataIds(this.tableCheckedData);//将要删除的id 赋值给data
    		
		currentAction = actions.del;
		this.modalDel = true;
    },
    submitDelete (){
		var _self = this;
		_self.modalDelSubmitLoading = true;
		submitForm(currentAction,_self.modalDelRowIds,
			function(data){_self.$Message.success('删除成功');_self.modalDelSubmitLoading = false;_self.modalDel = false;},
			function(errorMessage){_self.$Message.error(errorMessage);_self.modalDelSubmitLoading = false;}
		);
	},
	// 查询 
	querySubmit(){
    	this.loadPage();
    },
	// iview table binding checkbox 选中事件，selection：当前所有已选中的数据
	tableCheckboxSelectedData(selection){
		this.tableCheckedData = selection;
	},
    // table row 修改按钮
    rowUpdateButton (index) {
    	var _self = this;
    	$.iposty('user/single', {'id':_self.tableData[index].id}, 
			function(data){_self.updateForm = data.obj;_self.modalUpdate = true;},
			function(errorMessage){_self.$Message.error(errorMessage);}
    	);
    },
    // table row 删除按钮
    rowDeleteButton (index) {
		this.modalDelMessage = "是否继续删除此条记录?";
		this.modalDelRowIds = ''+this.tableData[index].id;
		this.modalDel = true;// 显示删除界面
    },
    // form 校验方法，此方法中this 要用vueContentObject 代替
    validateUserName (rule,value,callback) {
    	var form = getCurrentForm();
    	$.iposty('user/checkUsernameUnique',{'data':value,'id':form.id}, function(data) {
    		if (!data.isUnique) callback(new Error('用户名已被占用'));
    		else callback();
        });
    },
    validatePass(rule,value,callback) {
    	var form = getCurrentForm();
    	var formName = getCurrentFormName();
    	if (value === '') {
            callback(new Error('请输入密码'));
        } else {
            if (form.repassword !== '') vueContentObject.$refs[formName].validateField('repassword');
            callback();
        }
    },
    validatePassCheck(rule, value, callback) {
    	var form = getCurrentForm();
    	if (value === '') {callback(new Error('请输入确认密码'));} 
    	else if (value !== form.password) {callback(new Error('两次输入密码不一致!'));} 
        else {callback();}
    }
}

///////////////////////////////////////////////////////////////////////
//////////////////////////////自定义数据与方法 ////////////////////////////
/////////////////////////////////////////////////////////////////////

queryFormContent = {
	id:'',
	name:'',//昵称
	status: '',//状态
	createDate: '',//注册日期
	username: ''//用户名
}
var generalFormContent = {
	id:-1,
	username: '',
	password: '',
	repassword: '',
	nickname: '',
	phone: '',
	email: '',
	status: '1',
}
addFormContent = updateFormContent = generalFormContent;

var generalValidataionContent = {
	'username':  [{ required: true,  min: 6, message: '用户名要大于6个字符', trigger: 'change' },{ validator: vueContentMethods.validateUserName, trigger: 'blur'}],
	'password': [{ required: true,validator: vueContentMethods.validatePass, trigger: 'blur' }],
    'repassword': [{ required: true,validator: vueContentMethods.validatePassCheck, trigger: 'blur' }]
}
addFormValidateContent = updateFormValidateContent = generalValidataionContent;

///////////////////////////////////////////////////////////////////////
//////////////////////////////初始化Vue ////////////////////////////////
//////////////////////////////////////////////////////////////////////
var vueContentObject = new Vue(initializeContentOptions());
