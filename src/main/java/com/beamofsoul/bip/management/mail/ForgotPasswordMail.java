package com.beamofsoul.bip.management.mail;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName ForgotPasswordMail
 * @Description 系统忘记密码邮件实例类
 * @author MingshuJian
 * @Date 2017年3月24日 上午9:07:47
 * @version 1.0.0
 */
@Getter
@Setter
public class ForgotPasswordMail extends UnreplyableMail {

	private String code;
	
	public ForgotPasswordMail(String to, String code) {
		super(null, to, generateSubject(), generateText(code), new Date());
		this.code = code;
	}
	
	private static String generateSubject() {
		return "密码找回验证码";
	}
	
	private static String generateText(String code) {
		StringBuffer sb = new StringBuffer();
		sb.append("尊敬的CloudClass用户:<br>您好！此电子邮件地址正在用于找回某个CloudClass帐号密码。");
		sb.append("如果是您本人启动了该密码找回流程，请输入下方显示的数字验证码，验证码的有效期为5分钟。<br>");
		sb.append("如果您并未启动密码找回流程，并且有CloudClass帐号与此电子邮件地址相关联，则可能是其他人在尝试访问您的帐号。");
		sb.append("<i>请勿将此验证码转发给或提供给任何人。</i>");
		sb.append("请访问您账号的登录与安全设置，确保您的账号安全无虞。<h1>");
		sb.append(code);
		sb.append("</h1><br>");
		sb.append("此致<br>CloudClass团队敬上");
		sb.append("<br><br><br><br>");
		sb.append(UNREPLYABLE_NOTICE);
		return sb.toString();
	}

	@Override
	public String getLog4Succeed() {
		return "email has been sent to " + to + " with change password code " + code;
	}

	@Override
	public String getLog4Fail() {
		return "failed to send email to " + to + " for forgot password business";
	}
}
