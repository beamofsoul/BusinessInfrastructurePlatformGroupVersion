<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../include_admin_taglib.html"%>
<!DOCTYPE HTML>
<html class="no-js">
  <head>
    <title>Backend Organization Management Page</title>
    <meta name="description" content="这是后台管理组织结构管理页面">
    <meta name="keywords" content="organizationManagement">
    <%@ include file="../include_admin_head.html"%>
  </head>
  <body>
    <tags:subtitle primary="组织结构管理" secondary="列表" hr="true" />
    <tags:buttons security="organization" />
    <div class="am-g">
      <div class="am-u-sm-12">
        <div id="tableContainer"></div>
        <tags:pagination sizes="10,20"/>
      </div>
    </div>

    <%@ include file="../include_list_required.html"%>

    <%@ include file="admin_organization_add.html"%>
    <%@ include file="admin_organization_update.html"%>
    <%@ include file="admin_organization_copy.html"%>
    
    <script src="${pageContext.request.contextPath }/static/js/business/organization-biz.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/static/ckeditor/ckeditor.js"></script>
  </body>
</html>
