package com.peanut.commons.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSONArray;

/**
 * @user ming.yang
 * @date 2016年5月9日
 */
public class FileUtilForLog {
	// 加解密密钥
	public static final String AES_KEY = "9618433740985479";

	public static String read(String filePath) throws Exception {
		// 当前日期前一天的完整路径
		BufferedReader in = null;
		StringBuilder sbf = new StringBuilder();
		String s = "";
		int errorNum = 0;
		in = new BufferedReader(new InputStreamReader(new FileInputStream(
				filePath), "utf-8"));
		while ((s = in.readLine()) != null) {
			try {
				String[] ss = s.split("\"");
				String logStgr = ss[2];
				if (logStgr.equals("-") || logStgr.length() < 10) {
					continue;
				}
				/* 十六进制转换 */
				byte[] data = FileUtil.GetLogBuffer(ss[2]);
				/* 解压 */
				if (data == null)
					continue;
				data = gzipDecompress(data);
				/* 解密 */
				byte[] out = decrypt(data);
				System.out.println(new String(out));
			} catch (Exception e) {
				continue;
			}
		}
		System.out.println(errorNum);
		return sbf.toString();
	}

	/**
	 * gzip解压
	 */
	public static byte[] gzipDecompress(byte[] data) {
		/* 先加上头部十个字节，再解压 */
		byte[] content = new byte[10 + data.length];
		content[0] = 0x1f;
		content[1] = (byte) 0x8b;
		content[2] = 0x08;
		System.arraycopy(data, 0, content, 10, data.length);
		// logger.info("加十字节：" +
		// ConvertUtil.bytesToHexString(content).toString()+" "+content.length);
		/* 输出 */
		byte[] b = null;
		try {
			/* 解压 */
			ByteArrayInputStream bis = new ByteArrayInputStream(content);
			GZIPInputStream gzip = new GZIPInputStream(bis);
			/* 缓冲区 */
			byte[] buf = new byte[2048];
			int num = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((num = gzip.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			b = baos.toByteArray();
			baos.flush();
			baos.close();
			gzip.close();
			bis.close();
			// logger.info("解压后：" + ConvertUtil.bytesToHexString(b).toString());
		} catch (Exception ex) {
		}
		return b;
	}

	/**
	 * 解密
	 * 
	 * @param raw
	 * @param content
	 * @return
	 */
	public static byte[] decrypt(byte[] data) {
		Cipher cipher = null;
		byte[] decrypted = null;
		try {
			/* 解密 */
			SecretKeySpec skeySpec = new SecretKeySpec(AES_KEY.getBytes(),
					"AES");
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

	/**
	 * IMEI解码
	 * 
	 * @param id
	 * @param time
	 * @return
	 */
	public static String getIMEI(String id, String time) {

		String imeicode = id;
		String timecode = time;
		int subIndex = timecode.length() * 2 - imeicode.length();
		int removecount = Math.min(imeicode.length() - timecode.length(),
				timecode.length());
		for (int i = 1; i <= removecount; i++) {
			int removeIndex = imeicode.length() - i;
			String before = imeicode.substring(0, removeIndex);
			String after = "";
			if (i > 1)
				after = imeicode.substring(removeIndex + 1);
			imeicode = before + after;
		}

		if (subIndex > 0 && subIndex < imeicode.length()) {
			imeicode = imeicode.substring(subIndex);
		}
		return getIMEI(imeicode);
	}

	/**
	 * imei解码
	 * 
	 * @param imei
	 * @return
	 */
	public static String getIMEI(String imei) {

		byte[] temp = null;
		String ret = null;
		try {
			temp = imei.getBytes("utf-8");
			int length = temp.length;
			int count = length / 2 / 2 + (length / 2 % 2 == 0 ? 0 : 1);

			for (int i = 0; i < length / 2 - count; i++) {
				int index = length - 2 * i - 1;
				byte tempByte = temp[i + count];
				temp[i + count] = temp[index];
				temp[index] = tempByte;
			}
			for (int i = 0; i < count; i++) {
				int index = 0;

				if (length % 2 == 0) {
					index = length / 2 + 2 * i;
				} else {
					index = length / 2 + 2 * i + 1;
				}

				byte tempByte = temp[i];
				temp[i] = temp[index];
				temp[index] = tempByte;
			}

			ret = new String(temp);
		} catch (Exception e) {
			ret = null;
		}

		return ret;
	}

	public static void main(String[] args) throws Exception {
		// String s =
		// "\x01\xD0\x00/\xFFK3\xCD\xD6\xCC\xB7d\xBA_$\xFD\x0Cz\xEB0I\xC1\x18\xBFG\x81\x8D\xDD
		// DfR\xA7\xDAaI\x1C\xB9\xC7\x09\x14\xED\xA4\x96V\x1E\xC3\x8C\x9E\xB8\x0C\xFBp\x1C2\x9C\x85\x85\xCD;\xBF\xDB\xFE.^\xE7\xD9U\xAD^\x1B\x03\x83\xAF\xB2.\x85\x95\x8BXk{\xC9\xCF\x9E2\xA9\x89\xEA\xBA\x9F#\xA2\xB9c\xD7y\xC3\xEE\x94y\x90\x98\xB5\xC3/2\x96Eo\x9D\xBA\x83m\xFB\xD6\x09/\x9C\x02\x1C%[\x1E\xEF\x03\xFB\xBFs\x94<\x8B\xE7\x11\xDE\xE2(\x7F1\xAFm\xF7\xFE\x84\x12u\x0B\x115s<~4\x98\x12+\xEEoP\x19\x11[AC\x18\xC0\x12\xE5\xFD\xF4C\xF6\x12\x86\xAE\x9A\xB3\x03=\xAE\xEC\xD0\xAFoI\xF1\x15\x0F\x8C\x84\xE7\x9B5\x85\xFB\x0C>\x8D\xE8:N\x1C\x97u\x5C*(BRr=\x89\xE1\x81\x92\xDAc\xD0\x00\x00\x00";
		read("/Users/ming.yang/app/logs/5igam.5bbrz.com_2016051101.log");
		// read("/Users/ming.yang/app/logs/5iwig.5bbrz.com_2016032005.log");

	}
}
