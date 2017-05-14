<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../include_admin_taglib.html"%>
<!DOCTYPE HTML>
<html class="no-js">
  <head>
    <title>Backend Role User Management Page</title>
    <meta name="description" content="这是后台管理角色权限管理页面">
    <meta name="keywords" content="roleUserManagement">
    <%@ include file="../include_admin_head.html"%>

    <link rel="stylesheet" href="${pageContext.request.contextPath }/static/AMAZEUI-TREE/css/amazeui.tree.min.css">
  </head>
  <body>
    <tags:subtitle primary="角色管理" secondary="用户角色设置" hr="true" />
    <div class="am-g am-margin-top">
      <div class="am-u-sm-2 am-u-md-2 am-text-left">
        <tags:tree />
      </div>
      <div class="am-u-sm-10 am-u-md-10">
        <div id="tableContainer"></div>
        <tags:pagination sizes="5,10,20"/>
      </div>
    </div>

    <%@ include file="../include_list_required.html"%>
    <%@ include file="admin_role_user_update.html"%>

    <script src="${pageContext.request.contextPath }/static/AMAZEUI-TREE/js/amazeui.tree.min.js"></script>
    <script src="${pageContext.request.contextPath }/static/js/business/role-user-biz.js"></script>
  </body>
</html>