package com.beamofsoul.bip.management.util;

import java.util.UUID;

/**
 * @ClassName CodeGenerator
 * @Description 邀请码生成器
 * @author MingshuJian
 * @Date 2017年1月20日 上午10:54:15
 * @version 1.0.0
 */
public class CodeGenerator {
	
	private static final String INVITATION_CODE_SIZE = "%05d";
	
	private static final int INVITATION_CODE_ENCRY_KEY = 1;

	public static String generateInvitationCode() throws Exception {
		return RC4.encry2String(String.format(INVITATION_CODE_SIZE, INVITATION_CODE_ENCRY_KEY), 
				UUID.randomUUID().toString()).toUpperCase();
	}
	
	public static String generateSixDigitCode() {
		return String.valueOf(getRandomNumber(100000,999999));
	}
	
	public static int getRandomNumber(int min, int max) {
	    return min + (int)(Math.random() * ((max - min) + 1));
	}
}
