// 添加
function addButtonFn (){
	formDataReset(addFormName);
	currentAction = actions.add;
	this.modalAdd = true;
}
function submitAddFn () {
	var _self = this;
	submitFormValidate(currentAction,function(data){
		_self.$Message.success('提交成功!');
		_self.modalAdd=false;
		formDataReset(getCurrentFormName());
	});
}

// 修改
function updateButtonFn (){
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
}

function submitUpdateFn(){
	var _self = this;
	submitFormValidate(currentAction,function(data){
		_self.$Message.success('更新成功!');
		_self.modalUpdate = false;
		formDataReset(getCurrentFormName());
	});
}

// 删除
function deleteButtonFn (){
	if(this.tableCheckedData.length==0){
		this.$Message.error('至少选中一条记录!');
		return;
	}
	this.modalDelMessage = "即将删除"+this.tableCheckedData.length+"条记录,是否继续删除?";
	this.modalDelRowIds = getTableCheckedDataIds(this.tableCheckedData);//将要删除的id 赋值给data
		
	currentAction = actions.del;
	this.modalDel = true;
}
function submitDeleteFn (){
	var _self = this;
	_self.modalDelSubmitLoading = true;
	submitForm(currentAction,_self.modalDelRowIds,
		function(data){_self.$Message.success('删除成功');_self.modalDelSubmitLoading = false;_self.modalDel = false;},
		function(errorMessage){_self.$Message.error(errorMessage);_self.modalDelSubmitLoading = false;}
	);
}

//查询 
function querySubmitFn(){
	this.loadPage();
}

// iview table binding checkbox 选中事件，selection：当前所有已选中的数据
function tableCheckboxSelectedDataFn(selection){
	this.tableCheckedData = selection;
}
// table row 修改按钮
function rowUpdateButtonFn (index) {
	var _self = this;
	$.iposty('user/single', {'id':_self.tableData[index].id}, 
		function(data){_self.updateForm = data.obj;_self.modalUpdate = true;},
		function(errorMessage){_self.$Message.error(errorMessage);}
	);
}
//table row 删除按钮
function rowDeleteButtonFn (index) {
	this.modalDelMessage = "是否继续删除此条记录?";
	this.modalDelRowIds = ''+this.tableData[index].id;
	this.modalDel = true;// 显示删除界面
}



