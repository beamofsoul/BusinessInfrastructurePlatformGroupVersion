//tableContainer中表格显示列的中文名称
var columnNames = ['ID','昵称','用户名','密码','邮箱地址','电话号码','状态','注册日期','最后修改日期','操作'];
//tableContainer中表格每列对应的业务模型实体类的属性名
var attributeNames = ['id','nickname','username','password','email','phone','status','createDate','modifyDate','operation'];
//tableContainer中表格每列需要的按钮
var buttonsOnEachRow = ['update?','copy?','delete?'];
//提交add、update或copy表单时的表单验证规则
var usernameReg = /^[a-zA-Z\d]\w{4,11}[a-zA-Z\d]$/;
var nicknameReg = /^[a-zA-Z0-9·\u4e00-\u9fa5]{2,12}$/;
var telePhoneNumReg = /^1[3|4|5|8][0-9]\d{8}$/;
var emailReg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
var validateRules = [
	{name: 'username', required: true, unique:'checkUsernameUnique', regex: [{rule: usernameReg, warn: '用户名称必须为长度6至12位之间以字母、特殊字符(·)或数字字符组成的字符串!'}]},
	{name: 'password', required: true, minLength: 6, maxLength: 16},
	{name: 'repassword', required: true, equal: 'password'},
	{name: 'nickname', required: true, unique:'checkNicknameUnique', regex: [{rule: nicknameReg, warn: '昵称必须为长度2至12位之间以字母、特殊字符(·)、汉字或数组字符组成的字符串!'}]},
	{name: 'email', required: true, regex: [{rule: emailReg, warn: '电子邮箱必须为xxx@xxx.xxx此种格式!'}]},
	{name: 'phone', regex: [{rule: telePhoneNumReg, warn: '手机号码必须为中国境内主要运营商提供的11位号码!'}]}
];
/*****************************************************************************************
 *************************************** LIST PAGE ***************************************
 *****************************************************************************************/
loadPageableDataUrl = 'user/usersByPage'; //加载分页数据对应的后台请求映射URL
loadPageableDataCallback = function(data) {return loadPageableDataCallback0(data,false,'user')};
/*****************************************************************************************
 ************************************** OTHER PAGES **************************************
 *****************************************************************************************/
documentReady = function() { //pagination-utils.js 绑定所有表单单击(目的为提交表单)事件
	$('#submitAdd').click(function() {
		currentAction = actions.add;
		if(isPhotoOversize()) return;
		submitForm(validateForm(currentAction.key,validateRules),currentAction.key,function(){$('#closeAdd').click();resetForm(currentAction.key);});
	});	
	$('#submitUpdate').click(function() {
		currentAction = actions.update;
		//由于更新时不可修改用户名称，所以用户名称在update动作时不需要再次验证
		if(isPhotoOversize()) return;
		var updateValidateRules = jQuery.extend(true, {}, validateRules);
		updateValidateRules[0] = null;
		submitForm(validateForm(currentAction.key,updateValidateRules),currentAction.key,function(){$('#closeUpdate').click();resetForm(currentAction.key);});
	});
	$('#submitCopy').click(function() {
		currentAction = actions.copy;
		if(isPhotoOversize()) return;
		submitForm(validateForm(currentAction.key,validateRules),currentAction.key,function(){$('#closeCopy').click();resetForm(currentAction.key);});
	});
	$('#closeUpdate').click(function() {doCheckAll(false);});
	$('#closeCopy').click(function() {doCheckAll(false);});
	
	//为add、update、copy页面初始化cropper对象
	initCroppers();
};

/**
 * 初始化update表单和copy表单数据
 */
initAddForm = function(obj) {$('#add_crop').prop('disabled', true);} //basic-button-utils.js
initUpdateForm = function(obj) {initForm(actions.update.key,obj)} //basic-button-utils.js
initCopyForm = function(obj) {initForm(actions.copy.key,obj)} //basic-button-utils.js
deleteBefore = function(obj) {checkUsed(obj)} //basic-button-utils.js


/**
 * 保存add、update或copy表单提交的数据
 * @param url - 提交至后台的URL
 * @param sucFunc - 保存成功后的回调方法
 */
function saveData(formKey, sucFunc) { 
	//在更新记录时判断表单数据是否更改
	var savingFormData = getCurrentForm().serializeObject();
	if (formKey !== actions.update.key || JSON.stringify(savingFormData) !== JSON.stringify(initializedFormData)) {
		var formData = formatNonstandardData2JSON(savingFormData);
		formData = JSON.parse(formData);
		formData.photo = $('#'+currentAction.key+'_photo').attr('src');
		formData = formatNonstandardData2JSON(formData);
		$.postify(currentAction.url, formData, function(data) {if(!data.error)sucFunc(data);else warn(data.error)});
	} else {
		sucFunc(null);
	}
}

/**
 * 初始化update与copy表单，当用户打开此模态窗口时
 * @param formKey - update或copy
 * @param obj - 初始化表单所用的数据对象
 */
function initForm(formKey,obj) { console.log(obj)
	//账户信息
	$('#'+formKey+'_id').val(obj.id);
	$('#'+formKey+'_username').val(obj.username);
	$('#'+formKey+'_password').val(obj.password);
	$('#'+formKey+'_repassword').val(obj.password);
	$('#'+formKey+'_nickname').val(obj.nickname);
	$('#'+formKey+'_email').val(obj.email);
	$('#'+formKey+'_phone').val(obj.phone);
	getCurrentForm(formKey).find('input[id$=status][value='+(obj.status)+']').uCheck('check');
	
	//给update或是copy页面的用户头像赋值
	if(obj.photo) {
		var img = new Image();
		img.src = obj.photoString;
		img.id = formKey+'_photo';
		$('#'+formKey+'_photo').parent().html(img);
	} else {
		$('#'+formKey+'_photo').attr('src','');
	}
	//打开表单时，截取按钮应初始化为不可用
	$('#'+formKey+'_crop').prop('disabled', true);
	
	//初始化完毕后记录表单所有属性值，用于后期提交修改时判断表单属性值是否被修改
    if (formKey === actions.update.key)
      initializedFormData = getCurrentForm(formKey).serializeObject();
}

/**
 * 如何将输入参数obj的值赋值给tableContainer中每条记录的属性
 * @param obj - tableContainer中每条记录针对的业务模型实体类对象实例
 * @returns 格式化后记载着tableContainer中一条记录中每列属性的数组
 */
function parseValuesOnEachRow(obj) {
	return [obj.id,
		obj.nickname,
		obj.username,
		obj.password,
		obj.email,
		obj.phone,
		obj.status == 1 ? '可用' : '冻结',
		formatDate(obj.createDate,true),
		formatDate(obj.modifyDate,true),
		buttonsOnEachRow]
}

/**
 * 重置表单输入框、日期控件、图片控件
 * @param formKey - 表单关键字(add)
 */
function resetForm(formKey) {
	if (!formKey) formKey = currentAction.key;
	getCurrentForm(formKey)[0].reset();
	$('#'+formKey+'_photo').attr('src', '');
}

/*****************************************************************************************
 ***************************************** IMAGE *****************************************
 *****************************************************************************************/
/**
 * 为add、update、copy页面初始化cropper对象
 */
function initCroppers() {
	if (window.URL || window.webkitURL) {
		// 初始化
		$('img[id$=_inputImage]').cropper({dragMode: 'none', aspectRatio: '1', cropBoxResizable: false});
		 // 事件代理绑定事件
		$('div[id$=_inputFile]').on('click', '[data-method]', function() {
			var formKey = $(this).attr('id').split('_')[0];
	    	var $image = $('#'+formKey+'_photo');
	        var croppedCanvas = $image.cropper('getCroppedCanvas');
	        var roundedCanvas = getRoundedCanvas(croppedCanvas);
	        var img = canvasToImage(roundedCanvas, formKey+'_photo');
	        $image.parent().html(img);
	        $('#'+formKey+'_crop').prop('disabled', true);
	        initializedFormData = {};
	    })
	} else {
		$('img[id$=_inputImage]').prop('disabled', true).parent().addClass('disabled');
	}
}

/**
 * input[type=file]对象的change事件
 * 重新初始化cropper，以保证新选中的图片能够被预览并且被剪切
 * @param formKey - add、update、copy
 */
function inputImage(formKey) {
	var files = $('#'+formKey+'_inputImage')[0].files;
	if (files && files.length) {
		var file = files[0];
		var blobURL = URL.createObjectURL(file);
		$('#'+formKey+'_photo').cropper('destroy');
    	$('#'+formKey+'_photo').cropper({dragMode: 'none', aspectRatio: '1', cropBoxResizable: false});
    	$('#'+formKey+'_photo').cropper('replace', blobURL);
    	$('#'+formKey+'_inputImage').val('');
    	$('#'+formKey+'_crop').prop('disabled', false);
	}
};

/**
 * 判断用户截取的头像是否超过1MB限制
 */
function isPhotoOversize() {
	if(getImageSize($('#'+currentAction.key+'_photo').attr('src')) > 1) {
		warn('截取的用户头像大小不能超过1MB!');
		return true;
	}
	return false;
}