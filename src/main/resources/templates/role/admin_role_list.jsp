<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../include_admin_taglib.html"%>
<!DOCTYPE HTML>
<html class="no-js">
  <head>
    <title>Backend Role Management Page</title>
    <meta name="description" content="这是后台管理角色管理页面">
    <meta name="keywords" content="roleManagement">
    <%@ include file="../include_admin_head.html"%>
  </head>
  <body>
    <tags:subtitle primary="角色管理" secondary="列表" hr="true" />
    <tags:buttons security="role" />

    <div id="customInlineButtons" hidden="true">
      <sec:authorize access="hasPermission('role','role:rolepermission')">
        <button type="button" class="am-btn am-btn-default" id="allotPermissionButton">
          <span class="am-icon-cog"></span> 分配权限
        </button>
      </sec:authorize>
      <button type="button" class="am-btn am-btn-default" id="refreshMappingButton">
        <span class="am-icon-refresh"></span> 更新映射
      </button>
    </div>
    <div class="am-g">
      <div class="am-u-sm-12">
        <div id="tableContainer"></div>
        <tags:pagination sizes="10,20"/>
      </div>
    </div>

    <%@ include file="../include_list_required.html"%>

    <%@ include file="admin_role_add.html"%>
    <%@ include file="admin_role_copy.html"%>
    <%@ include file="admin_role_update.html"%>
    <%@ include file="admin_role_permission_allot.html"%>

    <script src="${pageContext.request.contextPath }/static/js/business/role-biz.js"></script>
  </body>
</html>