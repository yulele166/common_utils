package com.peanut.commons.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 请求数据中的加密解密过程
 *
 */
public class AesUtil {

	public static String key = "9618433740985479";

	public static byte[] encrypt(byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	public static byte[] decrypt(byte[] encrypted) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	public static void main(String[] args) throws Exception {
		String s = "test for aes";
		byte[] sss = encrypt(s.getBytes());
		byte[] gzip  = GzipUtil.gzipCompress(sss);
		String ssss = gzip + "";
		byte[] gzipresult = GzipUtil.gzipDecompress(ssss.getBytes());
//		System.out.println(new String(decrypt(gzipresult)));
		
	}
	
	

}
