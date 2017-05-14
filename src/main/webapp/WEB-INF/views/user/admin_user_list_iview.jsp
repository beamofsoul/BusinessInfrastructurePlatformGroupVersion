<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../include_admin_taglib.html"%> 

<!DOCTYPE HTML>
<html>
	<head>
		<title></title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/iview/iview.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/iview/iview-custom.css">
		
	</head>
	<body>
		<div id="contentContainer" width="100%" height="100%" style="margin: 15px;">
			<!-- 综合查询 form -->
			<Collapse>
		        <Panel>
		           	 综合查询
		            <p slot="content"  id="queryFormDomId"></p>
		        </Panel>
		    </Collapse>
			
			<!-- 按钮 -->
		 	<div style="margin-bottom: 10px;margin-top: 20px;">
		 		<Button-group>
			        <i-button type="ghost"  @click="vueBindButtonHeadAddMethod()"><Icon type="ios-download-outline"></Icon> 新增</i-button>
			    	<i-button type="ghost"  @click="vueBindButtonHeadUpdateMethod()"><Icon type="ios-download-outline"></Icon> 修改</i-button>
			    	<i-button type="ghost"  @click="vueBindButtonHeadDeleteMethod()"><Icon type="ios-download-outline"></Icon> 删除</i-button>
			    </Button-group>
			</div>
			
			<!-- 数据表格 -->
			<i-table border :context="self" :columns="defaultVueBindTableColumnsData" :data="defaultVueBindTableDataData" @on-selection-change="vueBindTableCheckedDataMethod($event,'defaultVueTableCheckedData')" ></i-table>
			
			<!-- 分页标签 -->
			<div style="margin: 10px;overflow: hidden">
		        <div style="float: right;">
		            <Page show-total :page-size="defaultVueBindPageSizeData" :total="defaultVueBindPageTotalData" :current="defaultVueBindPageCurrentData" @on-change="vueBindPageOnChangeMethod($event)"></Page>
		        </div>
		    </div>
			
			<!-- 新增用户 -->
	    	<Modal
		    	width="600"
		        v-model="defaultVueBindModalAddData"
		      	:styles="{top: '80px'}"
		        title="增加用户">
	       	
		       	<i-form ref="defaultVueBindFormAddData" :model="defaultVueBindFormAddData" :rules="defaultVueBindFormRulesAddData" :label-width="80">
			        <Form-item label="姓名" prop="username">
			            <i-input v-model="defaultVueBindFormAddData.username" placeholder="请输入姓名"></i-input>
			        </Form-item>
			        <Form-item label="密码" prop="password">
			            <i-input v-model="defaultVueBindFormAddData.password" type="password" placeholder="请输入密码"></i-input>
			        </Form-item>
			        <Form-item label="确认密码" prop="repassword">
			            <i-input v-model="defaultVueBindFormAddData.repassword" type="password" placeholder="请输入确认密码"></i-input>
			        </Form-item>
			        <Form-item label="昵称" prop="nickname">
			            <i-input v-model="defaultVueBindFormAddData.nickname" placeholder="请输入确认密码"></i-input>
			        </Form-item>
			        <Form-item label="手机号码" prop="phone">
			        	<i-input v-model="defaultVueBindFormAddData.phone" placeholder="请输入确认密码"></i-input>
			        </Form-item>
			        <Form-item label="电子邮箱" prop="email">
			        	<i-input v-model="defaultVueBindFormAddData.email" placeholder="请输入确认密码"></i-input>
			        </Form-item>
			        <Form-item label="可用状态" prop="status">
			            <Radio-group v-model="defaultVueBindFormAddData.status" type="button">
					        <Radio label="1"><span>可用</span></Radio>
					        <Radio label="0"><span>冻结</span></Radio>
					    </Radio-group>
			        </Form-item>
			    </i-form>
		    	<!-- 自定义 modal 底部按钮 -->
			    <div slot="footer">
			     	<i-button type="primary"  @click="vueBindButtonHeadAddSubmitMethod()">提交</i-button>
		            <i-button type="primary"  @click="resetVueFormData('defaultVueBindFormAddData')"  style="margin-left: 8px">重置</i-button>
		            <i-button type="primary"  @click="defaultVueBindModalAddData = false"  style="margin-left: 8px">取消</i-button>
		        </div>
	    	</Modal>
	    	
	    	<!-- 删除 -->
		    <Modal v-model="defaultVueBindModalDelData" width="360">
		        <p slot="header" style="color:#f60;text-align:center">
		            <Icon type="information-circled"></Icon>
		            <span>删除确认</span>
		        </p>
		        <div style="text-align:center">
		            <p>{{defaultVueBindModalDelMessageData}} </p>
		        </div>
		        <div slot="footer">
		        	<i-button type="error"  :loading="defaultVueBindModalDelLoadingData" @click="vueBindButtonHeadDeleteSubmitMethod">删除</i-button>
		        	<i-button type="primary"  @click="defaultVueBindModalDelData = false">取消</i-button>
		        </div>
		    </Modal>
		    
		    <!-- 修改 -->
	    	<Modal
		    	width="600"
		        v-model="defaultVueBindModalUpdateData"
		      	:styles="{top: '50px'}"
		        title="修改用户">
	       	
			    <i-form ref="defaultVueBindFormUpdateData" :model="defaultVueBindFormUpdateData" :rules="defaultVueBindFormRulesUpdateData" :label-width="80">
			        <Form-item label="姓名" prop="username">
			            <i-input v-model="defaultVueBindFormUpdateData.username" disabled  placeholder="请输入姓名"></i-input>
			        </Form-item>
			        <Form-item label="密码" prop="password">
			            <i-input v-model="defaultVueBindFormUpdateData.password"  type="password" placeholder="请输入密码"></i-input>
			        </Form-item>
			        <Form-item label="确认密码" prop="repassword">
			            <i-input v-model="defaultVueBindFormUpdateData.repassword" type="password" placeholder="请输入确认密码"></i-input>
			        </Form-item>
			        <Form-item label="昵称" disabled  prop="nickname">
			            <i-input v-model="defaultVueBindFormUpdateData.nickname" placeholder="请输入确认密码"></i-input>
			        </Form-item>
			        <Form-item label="手机号码" prop="phone">
			        	<i-input v-model="defaultVueBindFormUpdateData.phone" placeholder="请输入确认密码"></i-input>
			        </Form-item>
			        <Form-item label="电子邮箱" prop="email">
			        	<i-input v-model="defaultVueBindFormUpdateData.email" placeholder="请输入确认密码"></i-input>
			        </Form-item>
			        <Form-item label="可用状态" prop="status">
			            <Radio-group v-model="defaultVueBindFormUpdateData.status" type="button">
					        <Radio label="1"><span>可用</span></Radio>
					        <Radio label="0"><span>冻结</span></Radio>
					    </Radio-group>
			        </Form-item>
			    </i-form>
		    	<!-- 自定义 modal 底部按钮 -->
			    <div slot="footer">
			     	<i-button type="primary"  @click="vueBindButtonHeadUpdateSubmitMethod()">提交</i-button>
		            <i-button type="primary"  @click="defaultVueBindModalUpdateData = false"  style="margin-left: 8px">取消</i-button>
		        </div>
	    	</Modal>
	    	
		</div>

	
		<%-- <%@ include file="../include_list_required_iview.html"%>   --%>
		<script src="${pageContext.request.contextPath }/static/HOTKEYS/js/hotkeys.min.js"></script>
		
		<script src="${pageContext.request.contextPath }/static/js/utils/format-utils.js"></script>
		<script src="${pageContext.request.contextPath }/static/js/utils/iview-ajax-utils.js"></script>
	 	<script src="${pageContext.request.contextPath }/static/js/utils/iview-table-utils.js"></script>
		<script src="${pageContext.request.contextPath }/static/js/utils/iview-form-utils.js"></script>
	 	<script src="${pageContext.request.contextPath }/static/js/utils/iview-utils.js"></script>
	 	
	 	<script src="${pageContext.request.contextPath }/static/js/business/user-biz-iview.js"></script>
	

	</body>
</html>