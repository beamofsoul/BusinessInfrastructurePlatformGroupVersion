package com.beamofsoul.bip.management.util;

import static com.beamofsoul.bip.management.util.ConfigurationReader.asString;
import static com.beamofsoul.bip.management.util.ConfigurationReader.getValue;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.beamofsoul.bip.management.mail.Mail;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName MailUtils
 * @Description 系统邮件操作工具类
 * @author MingshuJian
 * @Date 2017年3月23日 下午4:01:40
 * @version 1.0.0
 */
@Slf4j
public class MailUtils {
	
	private static JavaMailSender sender = SpringUtils.getBean(JavaMailSender.class);
    private static String from = asString(getValue(ConfigurationReader.SPRING_MAIL_USERNAME));

	public static boolean send(Mail mail) {
		try {
			final MimeMessage message = sender.createMimeMessage();
			final MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(mail.getTo());
			helper.setSubject(mail.getSubject());
			helper.setText(mail.getText(), true);
			sender.send(message);
			log.debug(mail.getLog4Succeed());
			return true;
		} catch (Exception e) {
			log.error(mail.getLog4Fail(), e);
			return false;
		}
	}
}
