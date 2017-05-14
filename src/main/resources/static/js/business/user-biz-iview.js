//销毁上一个content页面遗留vueContentObject对象实例
if (vueContentObject) getVueObject().$destroy();

// 当前用户能够操作的所有行为
var actions = {'del': {'key':'del','url':'delete'},'add': {'key':'add','url':'singleAdd'},'update':{'key':'update','url':'singleUpdate'},'copy':{'key':'copy','url':'singleAdd'}};

//分页取数据url
loadPageableDataUrl = 'usersByPage';
//table column 显示名
var tableColumnsName = ['','ID','昵称','用户名','密码','邮箱地址','电话号码','状态','注册日期','最后修改日期','操作'];
//table column 对应data中的属性名   全选 加 'selection' 项 , 操作 加 'operation' 项。
var tableColumnsKey = ['selection','id','nickname','username','password','email','phone','status','createDate','modifyDate','operation'];
//table 每行需要的按钮 
var tableButtonsOnEachRow = ['vueBindButtonUpdateMethod#修改','vueBindButtonDeleteMethod#删除'];
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
setVueBindFormModelData({id:-1,username: '',password: '',repassword: '',nickname: '',phone: '',email: '',status: '1'});

//综合查询 form
var queryFormItemName = ['ID','昵称','用户名','密码','邮箱地址','电话号码','状态','注册日期'];
var queryFormItemKey = ['id','nickname','username','password','email','phone','status','createDate'];
var queryFormItemType = ['string','string','string','string','string','string','select#statusDataSelect','date'];
//form 验证信息 
setVueBindFormRulesData({
	'username': [{trigger: 'blur',type: 'string', required: true, pattern: /^[a-zA-Z\d]\w{4,11}[a-zA-Z\d]$/, message: '用户名称必须为长度6至12位之间以字母、特殊字符(·)或数字字符组成的字符串!'},{validator: this.vueFormRulesCommonValidate, trigger: 'blur',unique:'checkUsernameUnique',message: '用户名已被占用'}],
	'password': [{trigger: 'blur',type: 'string', required: true, min:6,max :16,message: '密码为长度6至12位之间字符串!'},{validator: this.vueFormRulesCommonValidate, trigger: 'blur',otherValidate:'repassword',message: '用户名已被占用'}],
    'repassword': [{trigger: 'blur',type: 'string', required: true,message:'请输入确认密码'},{trigger: 'blur',type: 'string', validator: this.vueFormRulesCommonValidate,equal:'password',message: '两次输入密码不一致!'}],
    'nickname': [{trigger: 'blur',type: 'string', required: true, pattern: /^[a-zA-Z0-9·\u4e00-\u9fa5]{2,12}$/, message: '昵称必须为长度2至12位之间以字母、特殊字符(·)、汉字或数组字符组成的字符串!'},{validator: this.vueFormRulesCommonValidate, trigger: 'blur',unique:'checkNicknameUnique',message: '昵称已被占用'}]
});

//new Vue 生命周期 ,此处可定义 vue data obj
setVueContentBeforeCreateFunction(function() {
	
//	console.log('2222222222222222');
//	console.log(vueContentDataObject);
//	vueContentDataObject.statusDataSelect = [{value: '1',label: '启用'},{value: '0',label: '禁用'}];
//	console.log(vueContentDataObject)
//	vueContentDataObject.defaultList= [
//	    {
//	        'name': 'a42bdcc1178e62b4694c830f028db5c0',
//	        'url': 'https://o5wwk8baw.qnssl.com/a42bdcc1178e62b4694c830f028db5c0/avatar'
//	    },
//	    {
//	        'name': 'bc7521e033abdd1e92222d733590f104',
//	        'url': 'https://o5wwk8baw.qnssl.com/bc7521e033abdd1e92222d733590f104/avatar'
//	    }
//	];
//	vueContentDataObject.imgName = '';
//	vueContentDataObject.visible = false;
//	vueContentDataObject.uploadList = [];
	
	
	console.log('2222222222222222  用this 方法中 获取不到 imgName visible 值，console能看到值');
	this.statusDataSelect = [{value: '1',label: '启用'},{value: '0',label: '禁用'}];
	this.defaultList= [
	    {
	        'name': 'a42bdcc1178e62b4694c830f028db5c0',
	        'url': 'https://o5wwk8baw.qnssl.com/a42bdcc1178e62b4694c830f028db5c0/avatar'
	    },
	    {
	        'name': 'bc7521e033abdd1e92222d733590f104',
	        'url': 'https://o5wwk8baw.qnssl.com/bc7521e033abdd1e92222d733590f104/avatar'
	    }
	];
	this.imgName = '';
	this.visible = false;
	this.uploadList = [];
});
//console.log('66666666666666666666666  如果 vueContentDataObject 有值写到外面');
//vueContentDataObject.statusDataSelect = [{value: '1',label: '启用'},{value: '0',label: '禁用'}];
//vueContentDataObject.defaultList= [
//    {
//        'name': 'a42bdcc1178e62b4694c830f028db5c0',
//        'url': 'https://o5wwk8baw.qnssl.com/a42bdcc1178e62b4694c830f028db5c0/avatar'
//    },
//    {
//        'name': 'bc7521e033abdd1e92222d733590f104',
//        'url': 'https://o5wwk8baw.qnssl.com/bc7521e033abdd1e92222d733590f104/avatar'
//    }
//];
//vueContentDataObject.imgName = '';
//vueContentDataObject.visible = false;
//vueContentDataObject.uploadList = [];


///////////////////////////
setVueContentMountedFunction(function () {this.vueTableLoadPageMethod();
//console.log('00000000000000000000000');
//console.log(this.uploadList);
this.uploadList = this.$refs.upload.fileList;});

//////////////////////////////自定义 vue data methods
vueContentMethods.handleView = function(name) {
	console.log('data未能起作用---------------------');
//	console.log(this);
	console.log(this.imgName);

    this.imgName = name;
    this.visible = true;
}
vueContentMethods.handleRemove = function(file) {
    // 从 upload 实例删除数据
    const fileList = this.$refs.upload.fileList;
    this.$refs.upload.fileList.splice(fileList.indexOf(file), 1);
},
vueContentMethods.handleSuccess = function(res, file) {
    // 因为上传过程为实例，这里模拟添加 url
    file.url = 'https://o5wwk8baw.qnssl.com/7eb99afb9d5f317c912f08b5212fd69a/avatar';
    file.name = '7eb99afb9d5f317c912f08b5212fd69a';
},
vueContentMethods.handleFormatError = function(file) {
    this.$Notice.warning({
        title: '文件格式不正确',
        desc: '文件 ' + file.name + ' 格式不正确，请上传 jpg 或 png 格式的图片。'
    });
},
vueContentMethods.handleMaxSize = function(file) {
    this.$Notice.warning({
        title: '超出文件大小限制',
        desc: '文件 ' + file.name + ' 太大，不能超过 2M。'
    });
},
vueContentMethods.handleBeforeUpload = function() {
    const check = this.uploadList.length < 5;
    if (!check) {
        this.$Notice.warning({
            title: '最多只能上传 5 张图片。'
        });
    }
    return check;
}

var vueContentObject = new Vue(initializeContentOptions());

/** 初始化vueContentObject之后 可自定义操作  **/
$(document).ready(function(){
	//伸出与收缩queryForm
	hotkey('space+q', function() {getVueRefObject('defaultVueBindCollapseQueryFormData').value = String(parseInt(getVueRefObject('defaultVueBindCollapseQueryFormData').value) * -1)});
	//弹出与关闭addForm
	hotkey('space+a', function() {if(!vueContentObject.defaultVueBindModalAddData) vueContentObject.vueBindButtonHeadAddMethod(); else vueContentObject.defaultVueBindModalAddData = false});
	//回归页面顶部
	hotkey('space+b', function() {$('.ivu-back-top').click()});
	//数据表格上一页
	hotkey('space+n', function() {$('.ivu-page-prev').click()});
	//数据表格下一页
	hotkey('space+m', function() {$('.ivu-page-next').click()});
	//数据表格第一页
	hotkey('space+,', function() {vueContentObject.defaultVueBindPageCurrentData = 1;vueContentObject.vueBindPageOnChangeMethod(1)});
	//数据表格最后一页
	hotkey('space+.', function() {var pageFinal = Math.ceil(vueContentObject.defaultVueBindPageTotalData / vueContentObject.defaultVueBindPageSizeData);vueContentObject.defaultVueBindPageCurrentData = pageFinal;vueContentObject.vueBindPageOnChangeMethod(pageFinal)});
});
