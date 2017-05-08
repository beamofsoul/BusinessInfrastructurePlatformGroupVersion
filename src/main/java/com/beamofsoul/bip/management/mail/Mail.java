package com.beamofsoul.bip.management.mail;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @ClassName Mail
 * @Description 系统邮件抽象父类
 * @author MingshuJian
 * @Date 2017年3月24日 上午9:02:44
 * @version 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Mail {
	
	String from;
	String to;
	String subject;
	String text;
	Date date;
	
	public abstract String getLog4Succeed();
	public abstract String getLog4Fail();
}
