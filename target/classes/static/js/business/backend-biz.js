$(document).ready(function () {
  $('a#changePasswordLink').click(changePassworkLinkClick);
  $('button#submitChange').click(submitChangeClick);
});
/*****************************************************************************************
 ************************************* CHANGE PASSWORD ***********************************
 *****************************************************************************************/
function changePassworkLinkClick() {
  hasButtonBehavior($('a#changePasswordLink'), 'change', true);
}

function submitChangeClick() {
  //验证
  var currentPasswordValue = $('input#change_current_password').val();
  var latestPasswordValue = $('input#change_latest_password').val();
  var confirmPasswordValue = $('input#change_confirm_password').val();

  if (!currentPasswordValue) {
    warn('请输入当前密码!');
    return false;
  }
  if (!latestPasswordValue) {
    warn('请输入最新密码!');
    return false;
  }
  if (!confirmPasswordValue) {
    warn('请输入确认密码!');
    return false;
  }
  if (currentPasswordValue === latestPasswordValue) {
    warn('当前密码不可与最新密码一致!');
    return false;
  }
  if (latestPasswordValue !== confirmPasswordValue) {
    warn('最新密码与验证密码不匹配!');
    return false;
  }
  if (latestPasswordValue.length < 6 || latestPasswordValue.length > 16) {
    warn('最新密码长度需在6位至16位之间!');
    return false;
  }

  //提交
  $.ajax({
    headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
    type: "POST",
    url: projectPath + '/admin/user/changePassword',
    data: JSON.stringify({'currentPassword': currentPasswordValue, 'latestPassword': latestPasswordValue}),
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      console.log(XMLHttpRequest.responseText);
      console.log(errorThrown);
    },
    success: function (data) {
      if (data.changed) {
        $('input#change_current_password').val('');
        $('input#change_latest_password').val('');
        $('input#change_confirm_password').val('');
        $('button#cancelChange').click();
        warn('密码修改成功!');
      } else {
        warn('密码修改失败，当前密码错误!');
      }
    }
  });
}

/**
 * 调入iframe框内的内容
 * @param {type} url
 * @returns 
 */
function loadIframeContent(url) {
  $('#iframeBody').prop('src', url);
}
function keepFocusOnIframe() {
  iframeBody.focus();
}
/**
 * 焦点在iframe框外,按F5时刷新iframe框内
 */
$("body").bind("keydown", function () {
  if (event.keyCode === 116) {
    frames[0].window.location.reload();
    return false;
  }
});
function myProgress() {
  $.AMUI.progress.done(true);
}