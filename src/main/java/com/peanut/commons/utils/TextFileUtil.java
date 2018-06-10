package com.peanut.commons.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.math.Fraction;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 文本文件的读写
 * @author ming.yang
 * @since 2015年12月15日 下午2:07:07
 */
public class TextFileUtil {
	// 读取指定路径文本文件
	public static String read(String filePath) {
		StringBuilder str = new StringBuilder();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filePath));
			String s;
			try {
				while ((s = in.readLine()) != null){
					str.append(s + '\n');
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str.toString();
	}
	/**
	 * 解密
	 * 
	 * @param raw
	 * @param content
	 * @return
	 */
	public static byte[] decrypt(String aesKey, byte[] data) {
		Cipher cipher = null;
		byte[] decrypted = null;
		try {
			/* 解密 */
			SecretKeySpec skeySpec = new SecretKeySpec(aesKey.getBytes(), "AES");
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			decrypted = cipher.doFinal(data);
			// logger.info("解密后："
			// + ConvertUtil.bytesToHexString(decryptded).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decrypted;
	}
	// 写入指定的文本文件，append为true表示追加，false表示重头开始写，
	// text是要写入的文本字符串，text为null时直接返回
	public static void write(String filePath, boolean append, String text) {
		if (text == null)
			return;
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath, append));
			try {
				out.write(text);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
	
				//		System.out.println(JSONObject.toJSON(sss));
	}
}
