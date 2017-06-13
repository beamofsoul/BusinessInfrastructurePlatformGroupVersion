package com.beamofsoul.bip.management.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beamofsoul.bip.entity.SensitiveWord;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class SensitiveWordsMapping {
	
	public final static Map<String, SensitiveWord> SENSITIVEWORD_REPLACEMENT_MAP = new HashMap<String, SensitiveWord>();
	
	public static void fill(List<SensitiveWord> sws) {
		log.debug("开始加载敏感词映射信息...");
		sws.stream().forEach(e -> SENSITIVEWORD_REPLACEMENT_MAP.put(e.getWord(), e));
		log.debug("敏感词映射信息加载完毕...");
	}
	
	public static void refill(List<SensitiveWord> sws) {
		SENSITIVEWORD_REPLACEMENT_MAP.clear();
		fill(sws);
	}
	
	public static String filter(String words) {
		return filter(words, null);
	}
	
	public static String filter(String words, final String replacement) {
		boolean clear = replacement != null;
		SensitiveWord sw = null;
		for (Map.Entry<String, SensitiveWord> entry : SENSITIVEWORD_REPLACEMENT_MAP.entrySet()) {
			sw = entry.getValue();
			if (sw.getAvailable())
				words = sw.getRegular() ? 
						words.replaceAll(entry.getKey(), clear ? replacement : sw.getReplacement()) : 
							words.replace(entry.getKey(), clear ? replacement : sw.getReplacement());
		}
		return words;
	}
	
	public static String clear(String words) {
		return filter(words, "");
	}
}
