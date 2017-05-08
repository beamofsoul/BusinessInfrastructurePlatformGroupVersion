<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE HTML>
<html class="no-js fixed-layout">
  <head>
    <title>Backend Management Page</title>
    <meta name="description" content="这是后台管理首页页面">
    <meta name="keywords" content="index">
    <%@ include file="include_admin_head.html"%>
  </head>
  <body data-type="index">
    <%@ include file="include_browser_notice.html"%>
    <%@ include file="include_admin_header.html"%>

    <div class="am-cf admin-main" disabled>
      <%@ include file="include_admin_sidebar.html"%>

      <div id="backDrop" class="modal-backdrop" style="background-color:rgba(0,0,0,0.6);display:none"></div>

      <!-- content start -->
      <div class="admin-content">
        <div class="admin-content-body">
          <iframe id="iframeBody" src="adminIndexContent" frameBorder="0" scrolling="no"  
                  style="position:absolute;width:85%;min-height:700px" onload="keepFocusOnIframe()"></iframe>
        </div>
      </div>
      <!-- content end -->
    </div>

    <%@ include file="include_admin_footer.html"%>
  </body>
</html>
