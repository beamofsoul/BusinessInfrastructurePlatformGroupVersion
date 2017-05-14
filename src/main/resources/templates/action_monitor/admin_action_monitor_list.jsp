<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../include_admin_taglib.html"%>
<!DOCTYPE HTML>
<html class="no-js">
  <head>
    <title>Backend ActionMonitor Management Page</title>
    <meta name="description" content="这是后台管理行为监控页面">
    <meta name="keywords" content="actionMonitorManagement">
    <%@ include file="../include_admin_head.html"%>
  </head>
  <body>
    <tags:subtitle primary="行为监控" secondary="列表" hr="true" />
    <div class="am-g">
      <div class="am-u-sm-12">
        <div id="tableContainer"></div>
        <tags:pagination sizes="10,20"/>
      </div>
    </div>

    <%@ include file="../include_list_required.html"%>

    <script src="${pageContext.request.contextPath }/static/js/business/action-monitor-biz.js"></script>
  </body>
</html>
