<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../include_admin_taglib.html"%> 

<!DOCTYPE HTML>
<html>
	<head>
		<title></title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/iview/iview.css">
		<style lang="less">
		    my-ivu-form-item{margin-bottom: 10px;}
		</style>
	</head>
	<body>
		<div id="contentContainer" width="100%" height="100%" style="margin: 15px;">
		
			<!-- 综合查询 form -->
			<div id="queryFormDiv" ></div>
	    	<br>
		    <hr>
			
			<!-- 按钮 -->
		 	<div style="margin-bottom: 10px;margin-top: 20px;">
		 		<Button-group>
			        <i-button type="ghost"  @click="addButton()"  ><Icon type="ios-download-outline"></Icon> 新增</i-button>
			    	<i-button type="ghost"  @click="updateButton()"><Icon type="ios-download-outline"></Icon> 修改</i-button>
			    	<i-button type="ghost"  @click="deleteButton()"><Icon type="ios-download-outline"></Icon> 删除</i-button>
			    </Button-group>
			</div>
			
			<!-- 数据表格 -->
			<i-table border :context="self" :columns="tableColumns" :data="tableData" @on-selection-change="tableCheckboxSelectedData" ></i-table>
			
			<!-- 分页标签 -->
			<div style="margin: 10px;overflow: hidden">
		        <div style="float: right;">
		            <Page show-total :page-size="pageSize" :total="pageTotal" :current="pageCurrent" @on-change="changePage($event)"></Page>
		        </div>
		    </div>
			
			<%-- <%@ include file="admin_user_add_iview.html"%> --%>
			<!-- 新增用户 -->
	    	<Modal
		    	width="600"
		        v-model="modalAdd"
		      	:styles="{top: '80px'}"
		        title="增加用户">
	       	
		       	<i-form ref="dataForm" :model="dataForm" :rules="dataFormValidate" :label-width="80">
			        <Form-item label="姓名" prop="username">
			            <i-input v-model="dataForm.username" placeholder="请输入姓名"></i-input>
			        </Form-item>
			        <Form-item label="密码" prop="password">
			            <i-input v-model="dataForm.password" type="password" placeholder="请输入密码"></i-input>
			        </Form-item>
			        <Form-item label="确认密码" prop="repassword">
			            <i-input v-model="dataForm.repassword" type="password" placeholder="请输入确认密码"></i-input>
			        </Form-item>
			        <Form-item label="昵称" prop="nickname">
			            <i-input v-model="dataForm.nickname" placeholder="请输入确认密码"></i-input>
			        </Form-item>
			        <Form-item label="手机号码" prop="phone">
			        	<i-input v-model="dataForm.phone" placeholder="请输入确认密码"></i-input>
			        </Form-item>
			        <Form-item label="电子邮箱" prop="email">
			        	<i-input v-model="dataForm.email" placeholder="请输入确认密码"></i-input>
			        </Form-item>
			        <Form-item label="可用状态" prop="status">
			            <Radio-group v-model="dataForm.status" type="button">
					        <Radio label="1"><span>可用</span></Radio>
					        <Radio label="0"><span>冻结</span></Radio>
					    </Radio-group>
			        </Form-item>
			    </i-form>
		    	<!-- 自定义 modal 底部按钮 -->
			    <div slot="footer">
			     	<i-button type="primary"  @click="submitAdd()">提交</i-button>
		            <i-button type="primary"  @click="formReset('dataForm')"  style="margin-left: 8px">重置</i-button>
		            <i-button type="primary"  @click="modalAdd = false"  style="margin-left: 8px">取消</i-button>
		        </div>
	    	</Modal>
	    	
	    	<!-- 删除 -->
		    <Modal v-model="modalDel" width="360">
		        <p slot="header" style="color:#f60;text-align:center">
		            <Icon type="information-circled"></Icon>
		            <span>删除确认</span>
		        </p>
		        <div style="text-align:center">
		            <p>{{modalDelMessage}} </p>
		        </div>
		        <div slot="footer">
		        	<i-button type="error"  :loading="modalDelSubmitLoading" @click="submitDelete">删除</i-button>
		        	<i-button type="primary"  @click="modalDel = false">取消</i-button>
		        </div>
		    </Modal>
		    
		    <!-- 修改 -->
	    	<Modal
		    	width="600"
		        v-model="modalUpdate"
		      	:styles="{top: '50px'}"
		        title="修改用户">
	       	
			    <i-form ref="dataForm" :model="dataForm" :rules="dataFormValidate" :label-width="80">
			        <Form-item label="姓名" prop="username">
			            <i-input v-model="dataForm.username" disabled  placeholder="请输入姓名"></i-input>
			        </Form-item>
			        <Form-item label="密码" prop="password">
			            <i-input v-model="dataForm.password"  type="password" placeholder="请输入密码"></i-input>
			        </Form-item>
			        <Form-item label="确认密码" prop="repassword">
			            <i-input v-model="dataForm.repassword" type="password" placeholder="请输入确认密码"></i-input>
			        </Form-item>
			        <Form-item label="昵称" disabled  prop="nickname">
			            <i-input v-model="dataForm.nickname" placeholder="请输入确认密码"></i-input>
			        </Form-item>
			        <Form-item label="手机号码" prop="phone">
			        	<i-input v-model="dataForm.phone" placeholder="请输入确认密码"></i-input>
			        </Form-item>
			        <Form-item label="电子邮箱" prop="email">
			        	<i-input v-model="dataForm.email" placeholder="请输入确认密码"></i-input>
			        </Form-item>
			        <Form-item label="可用状态" prop="status">
			            <Radio-group v-model="dataForm.status" type="button">
					        <Radio label="1"><span>可用</span></Radio>
					        <Radio label="0"><span>冻结</span></Radio>
					    </Radio-group>
			        </Form-item>
			    </i-form>
		    	<!-- 自定义 modal 底部按钮 -->
			    <div slot="footer">
			     	<i-button type="primary"  @click="submitUpdate()">提交</i-button>
		            <i-button type="primary"  @click="modalUpdate = false"  style="margin-left: 8px">取消</i-button>
		        </div>
	    	</Modal>
	    	
		</div>

	<%@ include file="../include_list_required_iview.html"%>  
	<script src="${pageContext.request.contextPath }/static/js/business/user-biz-iview.js"></script>

</body>
</html>