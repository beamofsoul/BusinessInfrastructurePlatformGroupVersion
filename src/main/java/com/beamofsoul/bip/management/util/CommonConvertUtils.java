package com.beamofsoul.bip.management.util;

public class CommonConvertUtils {

	public static Long[] convertToLongArray(String formattedStr, String delimiter) {
		String[] strArray = formattedStr.split(delimiter);
		Long[] longArray = new Long[strArray.length];
		for (int i = 0; i < strArray.length; i++) {
			longArray[i] = Long.valueOf(strArray[i]);
		}
		return longArray;
	}
	
	public static Long[] convertToLongArray(String formattedStr) {
		return convertToLongArray(formattedStr,",");
	}
}
