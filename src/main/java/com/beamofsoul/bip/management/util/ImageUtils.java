package com.beamofsoul.bip.management.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;

import javax.imageio.stream.FileImageOutputStream;

import org.apache.commons.lang3.StringUtils;

import lombok.Cleanup;
import lombok.NonNull;

/**
 * @ClassName ImageUtils
 * @Description 处理Image与Base64加密字符串互相转换的工具类
 * @author MingshuJian
 * @Date 2017年3月4日 上午9:07:44
 * @version 1.0.0
 */
public class ImageUtils {
	
	public static final String JPEG = "jpeg";
	public static final String JPG = "jpg";
	public static final String PNG = "png";
	public static final String GIF = "gif";

	/**
	 * @Title: imageToBase64  
	 * @Description: 根据输入图片路径，将图片转换为base64加密字符串  
	 * @param path - 图片的路径
	 * @return String - 图片对应的base64加密字符串
	 */
	public static String imageToBase64(String path) {
		byte[] base64 = null;
		
		try {
			@Cleanup InputStream in = new FileInputStream(path);
			base64 = new byte[in.available()];
			in.read(base64);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//需要后续修改，根据具体图片类型拼接具体base64文件头
		return base64 == null ? null : "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(base64);  
	}
	
	/**
	 * @Title: base64ToImage  
	 * @Description: 根据base64加密字符串生成图片的字节数组
	 * @param base64 - 图片对应的base64加密字符串
	 * @return byte[] -  图片的字节数组 
	 */
	public static byte[] base64ToImage(String base64) {
		byte[] bytes = null;
		if (StringUtils.isBlank(base64)) return bytes;
		
		try {
	        // Base64解码
	        bytes = Base64.getDecoder().decode(base64);
	        for (int i = 0; i < bytes.length; ++i) {
	            if (bytes[i] < 0) {// 调整异常数据
	                bytes[i] += 256;
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return bytes;
	}
	
	/**
	 * @Title: base64ToImage  
	 * @Description: 根据base64加密字符串和图片路径生成图片(.jpeg)至该路径  
	 * @param base64 - 图片对应的base64加密字符串
	 * @param path - 生成图片的路径
	 * @return boolean - true: 转换成功, false: 转换失败  
	 */
	public static boolean base64ToImage(String base64, @NonNull String path, @NonNull String fileName, String suffix) {
		byte[] bytes = base64ToImage(base64);
		if (bytes != null) {
			try {
				//为存储图片创建目录
				File file = new File(path);
				if (!file.exists()) file.mkdirs();
				
				//输出图片文件
				@Cleanup FileImageOutputStream out = new FileImageOutputStream(new File(generateImageFilePath(path, fileName, suffix)));
		        out.write(bytes, 0, bytes.length);
		        out.flush();
		        return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} 
		return false;
	}
	
	public static String generateImageFilePath(@NonNull String path, @NonNull String fullFileName) {
		return path+"//"+fullFileName;
	}
	
	public static String generateImageFilePath(String path, @NonNull String fileName, String suffix) {
		if (suffix == null) suffix = JPEG;
		if (suffix.indexOf(JPG) != -1) suffix = JPG;
		else if (suffix.indexOf(JPEG) != -1) suffix = JPEG;
		else if (suffix.indexOf(PNG) != -1) suffix = PNG;
		else if (suffix.indexOf(GIF) != -1) suffix = GIF;
		else suffix = JPEG;
		return path == null ? fileName+"."+suffix : path+"//"+fileName+"."+suffix;
	}
	
	/**
	 * @Title: getClearPhotoString  
	 * @Description: 截取base64加密信息头(文件格式信息)，并返回剩余信息  
	 * @return String  被截取后的剩余信息
	 */
	public static String getClearPhotoString(String photoString) {
		int delLength = photoString.indexOf(",") + 1;  
        return photoString.substring(delLength, photoString.length());
	}
	
	/**
	 * @Title: getPhotoType  
	 * @Description: 根据完整的base64加密信息头获取图片格式
	 * @return String 图片格式
	 */
	public static String getPhotoType(String photoString) {
		int colonIndex = photoString.indexOf(":");
		int semiIndex = photoString.indexOf(";");
		if (colonIndex > 0 && semiIndex > 0) {
			return photoString.substring(colonIndex + 1, semiIndex);
		}
		return null;
	}
}
