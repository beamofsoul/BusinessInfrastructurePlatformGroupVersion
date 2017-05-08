package com.beamofsoul.bip.management.mail;

import java.util.Date;

/**
 * @ClassName UnreplyableMail
 * @Description 系统不可回复邮件抽象父类
 * @author MingshuJian
 * @Date 2017年3月24日 上午9:39:22
 * @version 1.0.0
 */
public abstract class UnreplyableMail extends Mail {

	public UnreplyableMail(String from, String to, String subject, String text, Date date) {
		super(from, to, subject, text, date);
	}
	
	final static String UNREPLYABLE_NOTICE = "此电子邮件地址无法接受回复。如需更多信息，请访问CloudClass官网</a>。";
}
