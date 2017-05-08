package com.beamofsoul.bip.management.util;

/**
 * @ClassName RC4
 * @Description RC4算法实现对数据的加密与解密
 * @author MingshuJian
 * @Date 2017年1月20日 上午11:35:47
 * @version 1.0.0
 */
public class RC4 {
	
	public static String decry(byte[] data, String key) {  
        if (data == null || key == null) {  
            return null;  
        }  
        return asString(base(data, key));  
    }  
  
    public static String decry(String data, String key) {  
        if (data == null || key == null) {  
            return null;  
        }  
        return new String(base(hexString2Bytes(data), key));  
    }  
  
    public static byte[] encry2Byte(String data, String key) throws Exception {  
        if (data == null || key == null) {  
            return null;  
        }  
        byte b_data[] = data.getBytes("UTF-8");  
        return base(b_data, key);  
    }  
  
    public static String encry2String(String data, String key) throws Exception {  
        if (data == null || key == null) {  
            return null;  
        }  
        return toHexString(asString(encry2Byte(data, key)));  
    }  
  
    private static String asString(byte[] buf) {  
        StringBuffer strbuf = new StringBuffer(buf.length);  
        for (int i = 0; i < buf.length; i++) {  
            strbuf.append((char) buf[i]);  
        }  
        return strbuf.toString();  
    }  
  
    private static byte[] initKey(String aKey) {  
        byte[] b_key = aKey.getBytes();  
        byte state[] = new byte[256];  
  
        for (int i = 0; i < 256; i++) {  
            state[i] = (byte) i;  
        }  
        int index1 = 0;  
        int index2 = 0;  
        if (b_key == null || b_key.length == 0) {  
            return null;  
        }  
        for (int i = 0; i < 256; i++) {  
            index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;  
            byte tmp = state[i];  
            state[i] = state[index2];  
            state[index2] = tmp;  
            index1 = (index1 + 1) % b_key.length;  
        }  
        return state;  
    }  
  
    private static String toHexString(String s) {  
        String str = "";  
        for (int i = 0; i < s.length(); i++) {  
            int ch = (int) s.charAt(i);  
            String s4 = Integer.toHexString(ch & 0xFF);  
            if (s4.length() == 1) {  
                s4 = '0' + s4;  
            }  
            str = str + s4;  
        }  
        return str;// 0x表示十六进制  
    }  
  
    private static byte[] hexString2Bytes(String src) {  
        int size = src.length();  
        byte[] ret = new byte[size / 2];  
        byte[] tmp = src.getBytes();  
        for (int i = 0; i < size / 2; i++) {  
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);  
        }  
        return ret;  
    }  
  
    private static byte uniteBytes(byte src0, byte src1) {  
        char _b0 = (char) Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();  
        _b0 = (char) (_b0 << 4);  
        char _b1 = (char) Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();  
        byte ret = (byte) (_b0 ^ _b1);  
        return ret;  
    }  
  
    private static byte[] base(byte[] input, String mKkey) {  
        int x = 0;  
        int y = 0;  
        byte key[] = initKey(mKkey);  
        int xorIndex;  
        byte[] result = new byte[input.length];  
  
        for (int i = 0; i < input.length; i++) {  
            x = (x + 1) & 0xff;  
            y = ((key[x] & 0xff) + y) & 0xff;  
            byte tmp = key[x];  
            key[x] = key[y];  
            key[y] = tmp;  
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;  
            result[i] = (byte) (input[i] ^ key[xorIndex]);  
        }  
        return result;  
    }  
}
