<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="include_taglib.html"%>
<!DOCTYPE html>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title>Login Page | Amaze UI Example</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="format-detection" content="telephone=no">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <link rel="alternate icon" type="image/png" href="${pageContext.request.contextPath }/static/AMAZEUI/i/favicon.png">
  <link rel="stylesheet" href="${pageContext.request.contextPath }/static/AMAZEUI/css/amazeui.min.css"/>
  <style>
    .header {
      text-align: center;
    }
    .header h1 {
      font-size: 200%;
      color: #333;
      margin-top: 30px;
    }
    .header p {
      font-size: 14px;
    }
  </style>
</head>
<body>
<div class="header">
  <div class="am-g">
    <h1>登录</h1>
    <p>BIP</p>
  </div>
  <hr />
</div>
<div class="am-g">
  <div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
    <h3>登录</h3>
    <hr>

    <form method="post" class="am-form" action="login">
      <label for="username">用户名:</label>
      <input type="text" name="username" id="username" value="tom">
      <br>
      <label for="password">密码:</label>
      <input type="password" name="password" id="password" value="123456">
      <br>
      <input id="remember-me" name="remember-me" type="checkbox" value="true">自动登录
      <br>
      <br>
      <div class="am-cf">
        <input type="submit" name="" value="登 录" class="am-btn am-btn-primary am-btn-sm am-fl">
        <button type="button" id="forgotPassword" class="am-btn am-btn-default am-btn-sm am-fr">忘记密码 ^_^?</button>
      </div>
      <c:set var="message" value="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}" />
      <c:set var="expired" value="${expired}" />
      <c:choose>
        <c:when test="${not empty message}">
          <div class="am-alert am-alert-warning" data-am-alert>
            <button type="button" class="am-close">&times;</button>
            <c:out value="${ message }" />
          </div>
        </c:when>
        <c:otherwise>
          <c:if test="${not empty expired}">
            <div class="am-alert am-alert-warning" data-am-alert>
              <button type="button" class="am-close">&times;</button>
              <c:out value="${ expired }" />
            </div>
          </c:if>
            <c:out value=" " />
        </c:otherwise>
      </c:choose>
    </form>
    <hr>
    <%@ include file="include_footer.html"%>
  </div>
</div>

<div class="am-modal am-modal-prompt" tabindex="-1" id="forgot-password-prompt">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">忘了密码</div>
    <div class="am-modal-bd">
      请输入用户名，并点击找回按钮
      <input type="text" class="am-modal-prompt-input">
    </div>
    <div class="am-modal-footer">
      <span class="am-modal-btn" id="cancelForgotPassword" data-am-modal-cancel>取消</span>
      <span class="am-modal-btn" data-am-modal-confirm>找回</span>
    </div>
  </div>
</div>

<div class="am-modal am-modal-prompt" tabindex="-1" id="forgot-password-action">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">修改密码</div>
    <div class="am-modal-bd">
      请输入验证码、新密码与验证密码，并点击修改按钮(已发送的验证码有效期为5分钟)
      <input type="text" id="action_code" class="am-modal-prompt-input" placeholder="验证码">
      <input type="text" id="action_latestPassword" class="am-modal-prompt-input" placeholder="新的密码">
      <input type="text" id="action_confirmPassword" class="am-modal-prompt-input" placeholder="确认密码">
    </div>
    <div class="am-modal-footer">
      <span class="am-modal-btn" id="cancelForgotPassword" data-am-modal-cancel>取消</span>
      <span class="am-modal-btn" data-am-modal-confirm>修改</span>
    </div>
  </div>
</div>

<%@ include file="include_script.html"%>

<script type="text/javascript">
var projectPath = window.document.location.pathname.substring(0,window.document.location.pathname.substr(1).indexOf('/')+1);

$(document).ready(function() {
	
/**
 * 用户点击忘记密码按钮
 * 弹出输入框让用户输入账号
 * 向用户邮箱或手机发送一个秘钥
 * 并弹出一个模态窗口让用户输入密码和修改后的新密码
 * 通过此种方法帮助用户找回密码
 */
	$('button#forgotPassword').click(function(){
		//弹出输入框让用户输入账号
		var $parentModal = $('#forgot-password-prompt');
		$parentModal.modal({
			relatedTarget: this,
			closeOnConfirm: false,
			onConfirm: function(e) {
				var username = e.data;
				if (!username) {
					warn('请输入用户名!');
					return false;
				}
				
				handleForgotPasswordModal(username);
				this.close();
			}
		});
	});
});

function handleForgotPasswordModal(username) {
	$.ajax({
	    headers: {'Accept': 'application/json','Content-Type': 'application/json'},
        type: "POST",
        cache: false,
        async: false,
        url: projectPath + '/forgetPassword',
        data: JSON.stringify({'username': username}),
        success: function(data) {
        	result = data.message;
        	if (result == 'ES') {
        		//发送邮件成功
        		resetInputs($parentModal);
        		$parentModal.modal('close');
        		handleChangePasswordModal();
        	} else if (result == 'MS') {
        		//发送短信成功
        		warn('系统暂不支持发送短信功能!');
        	} else {
        		warn(result);
        	}
        }
    });
}

function handleChangePasswordModal() {
	$('#forgot-password-action').modal({
		relatedTarget: this,
		closeOnConfirm: false,
		onConfirm: function(e) {
			var code = e.data[0];
			var latestPassword = e.data[1];
			var comfirmPassword = e.data[2];
			if (!code) {
				warn('请输入验证码!');
				return false;
			}
			if (!latestPassword) {
				warn('请输入新的密码!');
				return false;
			}
			if (!comfirmPassword) {
				warn('请输入确认密码!');
				return false;
			}
			if (latestPassword != comfirmPassword) {
				warn('新密码与确认密码不一致!');
				return false;
			}
			doChangePassword(code,latestPassword);
		}
	});
}

function doChangePassword(code,latestPassword) {
	$.ajax({
	    headers: {'Accept': 'application/json','Content-Type': 'application/json'},
        type: "POST",
        cache: false,
        async: false,
        url: projectPath + '/user/changePasswordWithCode',
        //url: projectPath + '/admin/user/changePasswordWithCode',
        data: JSON.stringify({'code': code, 'password': latestPassword}),
        success: function(data) {
        	var $action = $('#forgot-password-action');
        	resetInputs($action);
        	$action.modal('close');
        	warn(data.changed ? "修改密码成功!" : "修改密码失败，请联系系统管理员!");
        }
    });
}

function resetInputs(selector) {
	$(selector).find('input[type=text]').each(function() {
		$(this).val('');
	});	
}
</script>
</body>
</html>
