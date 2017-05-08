var initAddForm;    //初始化添加页面表单业务数据的方法
var initUpdateForm; //初始化更新页面表单业务数据的方法
var initCopyForm;   //初始化复制页面表单业务数据的方法

var beforeAdd;      //执行进入添加按钮单击事件方法首先需要执行的方法
var beforeUpdate;   //执行进入修改按钮单击事件方法首先需要执行的方法
var beforeDelete;   //执行进入删除按钮单击事件方法首先需要执行的方法
var beforeCopy;     //执行进入复制按钮单击事件方法首先需要执行的方法

var updateBefore;   //执行修改后台方法之前需要执行的方法
var copyBefore;     //执行复制后台方法之前需要执行的方法
var deleteBefore;   //执行删除后台方法之前需要执行的方法

var addModalWidth = addModalHeight = null;        //初始化添加页面高度与宽度
var updateModalWidth = updateModalHeight = null;  //初始化更新页面高度与宽度
var copyModalWidth = copyModalHeight = null;      //初始化复制页面高度与宽度

$('button#invertButton').click(function(){
	if($('#all').is(':checked')) {
		$('#all').click();
	} else {
		$(':checkbox.idc').each(function() {
       		$(this).prop('checked',!$(this).is(':checked'));
       	});
		checkAll();
	}
});
$('button#addButton').click(function(){
	if (beforeAdd) beforeAdd();
	if (initAddForm) initAddForm();
	hasButtonBehavior(this,'add',true,addModalWidth,addModalHeight);
});
$('button#updateButton').click(function(){
	if (beforeUpdate) beforeUpdate();
	hasButtonBehavior(this,'update',false,null,null);
	var count = countCheckedbox();
	if (count == 0) {
		warn('请选中至少一条记录!');
	} else if (count == 1) {
		if (doFunctionWithSingleData(initUpdateForm,updateBefore)) 
			hasButtonBehavior(this,'update',true,updateModalWidth,updateModalHeight);
	} else {
		ask('您确定要同时修改选中的' + count + '条记录吗？',function(){
			warn('系统暂不支持批量修改功能！');
		});
	}
});

var hasButtonBehavior = function(selector,target,hasBehavior,width,height) {
	var content = [];
	if (hasBehavior && target != null) {
		content.push('{target: "#doc-modal-');
		content.push(target);
		content.push('", closeViaDimmer: 0');
		if (width) {
			content.push(', width: ');
			content.push(width);
		}
		if (height) {
			content.push(', height: ');
			content.push(height);
		}
		content.push('}');
	}
	$(selector).attr('data-am-modal',content.join(''));
}

$('button#deleteButton').click(function() {
	if (beforeDelete) beforeDelete();
	var count = countCheckedbox();
	if (count == 0) {
		warn('请选中至少一条记录!');
		return;
	}
	
	var ids = getCheckedIds();
	//删除前，执行需要在删除执行前执行的方法
	if (deleteBefore) deleteBefore(ids);
	ask('您确定要删除选中的' + count + '条记录吗？','是','否',function(){
		//执行删除操作
		$.del('delete',ids,function(){
			warn('删除成功!');
			reloadPageableDataAfterDelte();
		});
	});
});

var updateRow = function(e,id) {
	onlySelectCurrentRow(e,id);
	$('button#updateButton').click();
}

var copyRow = function(e,id) {
	if (beforeCopy) beforeCopy(id);
	onlySelectCurrentRow(e,id);
	if (doFunctionWithSingleData(initCopyForm,copyBefore)) {
		var options = {closeViaDimmer: 0};
		if (copyModalWidth) options.width = copyModalWidth;
		if (copyModalHeight) options.height = copyModalHeight;
		$('#doc-modal-copy').modal(options);
	}
}

var deleteRow = function(e,id) {
	onlySelectCurrentRow(e,id);
	$('button#deleteButton').click();
}

/**
 * 根据数据表格中复选框选中的记录Id，获取所对应的业务对象实例
 * @param fn1 - 获取到业务对象实例后，所需执行的回调方法1
 * @param fn2 - 获取到业务对象实例后，所需执行的回调方法2
 */
var doFunctionWithSingleData = function(initFormFn,actionBeforeFn) {
	var result = false;
	$.ajax({
		headers: {'Accept': 'application/json','Content-Type': 'application/json'},
	    cache: false,
        async: false,
	    type: 'POST',
	    url: 'single',
	    data: JSON.stringify({'id':getCheckedIds()}),
	    dataType: 'json',
	    success: function(data) {
	    	var obj = data.obj;
	    	if((actionBeforeFn && actionBeforeFn(obj)) || !actionBeforeFn) { 
				if(initFormFn) initFormFn(obj);
				result = true;
			}
	    }
	});
	return result;
}

/**
 * 向toolbar中注册自定义的内联按钮，使自定义按钮样式与默认按钮一致
 */
function registerCustomInlineButtons() {
	var customInlineButtons = $('div#customInlineButtons').html();
	$('div#customInlineButtons').remove();
	$('div#toolbar').find('div.am-btn-group').append(customInlineButtons);
}