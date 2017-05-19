//销毁上一个content页面遗留vueContentObject对象实例
if (vueContentObject) getVueObject().$destroy();

//分页取数据url
loadPageableDataUrl = 'usersByPage';
//table column 显示名
tableColumnsName = ['','ID','昵称','用户名','密码','邮箱地址','电话号码','状态','注册日期','最后修改日期','操作'];
//table column 对应data中的属性名   全选 加 'selection' 项 , 操作 加 'operation' 项。
tableColumnsKey = ['selection','id#sortable','nickname','username#sortable','password','email','phone','status','createDate','modifyDate','operation'];
//table 每行需要的按钮 
tableButtonsOnEachRow = ['rowUpdateButton#修改','rowDeleteButton#删除'];
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

//设置add update vue form data obj
setFormDataObject({id:-1,username: '',password: '',repassword: '',nickname: '',phone: '',email: '',status: '1',photo: '',photoString: ''});

//综合查询 form
var queryFormItemName = ['ID','昵称','用户名','密码','邮箱地址','电话号码','状态','注册日期','数字'];
var queryFormItemKey = ['id','nickname','username','password','email','phone','status','createDate','number'];
var queryFormItemType = ['string','string','string','string','string','string','select#statusDataSelect','date','10<number<20'];

//form 验证信息 
setFormRulesObject({
	'username': [{trigger: 'blur',type: 'string', required: true, pattern: /^[a-zA-Z\d]\w{4,11}[a-zA-Z\d]$/, message: '用户名称必须为长度6至12位之间以字母、特殊字符(·)或数字字符组成的字符串!'},{validator: this.validateFormRules, trigger: 'blur',unique:'checkUsernameUnique',message: '用户名已被占用'}],
	'password': [{trigger: 'blur',type: 'string', required: true, min:6,max :16,message: '密码为长度6至12位之间字符串!'},{validator: this.validateFormRules, trigger: 'blur',otherValidate:'repassword',message: '用户名已被占用'}],
    'repassword': [{trigger: 'blur',type: 'string', required: true,message:'请输入确认密码'},{trigger: 'blur',type: 'string', validator: this.validateFormRules,equal:'password',message: '两次输入密码不一致!'}],
    'nickname': [{trigger: 'blur',type: 'string', required: true, pattern: /^[a-zA-Z0-9·\u4e00-\u9fa5]{2,12}$/, message: '昵称必须为长度2至12位之间以字母、特殊字符(·)、汉字或数组字符组成的字符串!'},{validator: this.validateFormRules, trigger: 'blur',unique:'checkNicknameUnique',message: '昵称已被占用'}]
});

////////////////////////////// 在vue生命周期 BeforeCreate 自定义 data ////////////////////////////////
vueContentBeforeCreate = function(){
	customVueContentData = {
		statusDataSelect : [{value: '1',label: '启用'},{value: '0',label: '禁用'}],
		imgName : '',
		imgvisible : false,
		uploadList : []
	}
};
////////////////////////////// 自定义 vue  methods ////////////////////////////////
//查询头像图片
vueContentMethods.handleView = function(name) {
	this.imgName = '';
	for(var i in getVueObject().uploadList){
		if(getVueObject().uploadList[i].name == name)
			this.imgName = getVueObject().uploadList[i].url;
	}
	this.imgvisible = true;
}
//删除头像图片
vueContentMethods.handleRemove = function(name) {
	for(var i in getVueObject().uploadList){
		if(getVueObject().uploadList[i].name == name){
			getVueObject().uploadList.splice(getVueObject().uploadList[i],1);
			getVueObject().vueAddForm.photo = null;
		}
	}
}
function handleFormatError(file) {
	toastError('图片格式不正确');
}
function handleMaxSize(file) {
	toastError('超出文件大小限制,不能超过 2M');
}
vueContentMethods.handleBeforeUpload = function(file) {
	const check = this.uploadList.length < 1;
	if (!check) {
		toastError('头像只能上传 1 张图片。');
	    return false;
	}
	
	
	// check format
	var uploadComponent = this.$refs.upload;
    if (uploadComponent.format.length) {
        const _file_format = file.name.split('.').pop().toLocaleLowerCase();
       
        const checked = uploadComponent.format.some(item => item.toLocaleLowerCase() === _file_format);
        if (!checked) {
        	handleFormatError(file);
            return false;
        }
    }

    // check maxSize
    if (uploadComponent.maxSize) {
        if (file.size > uploadComponent.maxSize * 1024) {
        	handleMaxSize(file, uploadComponent.fileList);
            return false;
        }
    }
    // 读取选择图片数据
	var fr = new FileReader();
	fr.onload = function(e) {
		getVueObject().uploadList.push({
	        'name': file.name,
	        'url': e.target.result
		});
		//设置addFrom data
		getVueObject().vueAddForm.photo = e.target.result;
	};
	fr.readAsDataURL(file);
	return false;
}

//////////////////////////////new vue 前自定义方法 ////////////////////////////////
beforeVueContentCreateCustom = function(){
	vueContentMounted = function () {this.doLoadPage();this.uploadList = this.$refs.upload.fileList;};
}

////////////////////////////// 覆盖 流程方法 实现个性化/////////////////////////////////
beforeAdd = function(){
	getVueObject().uploadList = [];
}
updateBefore = function(obj){
	getVueObject().uploadList = [];
	
	if(obj.photo&&obj.photoString){
		getVueObject().uploadList.push({
			'name': obj.photo,
			'url': obj.photoString
		});
		getVueObject().vueAddForm.photo = obj.photoString;
	}
	else{
		getVueObject().vueAddForm.photo = null;
	}
	return true;
}
//submitUpdateBefore  = function(obj){
//	
//	if(obj.photo&&obj.photoString){
//		  getVueObject().uploadList.push({
//		      'name': obj.photo,
//		      'url': obj.photoString
//			});
//		}
//}
var vueContentObject = new Vue(initializeContentOptions());

