package com.peanut.commons.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class MD5Util {
	
	public static String encrypt(String... strInput) {
		if(strInput==null || strInput.length==0){
			return "";
		}
		
		StringBuilder strSrc = new StringBuilder();
		for(String str : strInput){
			strSrc.append(str);
		}
		
		
		MessageDigest md;
		//====CHANGE END===============
		// 加密后的字符串
		String strDes = null;
		// 要加密的字符串字节型数组
		byte[] bt = strSrc.toString().getBytes();
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(bt);
			// 通过执行诸如填充之类的最终操作完成哈希计算
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return strDes;
	}

	// 将字节数组转换成16进制的字符串
	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
}
