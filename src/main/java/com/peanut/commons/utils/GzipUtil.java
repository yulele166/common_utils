package com.peanut.commons.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.http.util.TextUtils;

/**
 * @author ming.yang
 * @since 2015年12月4日 下午2:18:32
 */
public class GzipUtil {
//	byte[] encryptArray = AesUtil.encrypt(Const.AES_KEY.getBytes(), data.getBytes());
//	byte[] dataArray = Util.gzipCompress(encryptArray);
//	int dataLength = dataArray.length;
//	InputStreamEntity sEntity = new InputStreamEntity(new ByteArrayInputStream(dataArray, 10, dataLength - 10),
//			dataLength - 10);

	/**
	 * gzip压缩
	 */
	public static byte[] gzipCompress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return new byte[0];
		}
		return gzipCompress(str.getBytes());
	}

	/**
	 * gzip压缩
	 */
	public static byte[] gzipCompress(byte[] source) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(source);
		gzip.close();
		return out.toByteArray();
	}

	/**
	 * gzip解压
	 */
	public static byte[] gzipDecompress(String str) throws IOException {
		if (TextUtils.isEmpty(str)) {
			return new byte[0];
		}
		return gzipDecompress(str.getBytes());
	}

	/**
	 * gzip解压
	 */
	public static byte[] gzipDecompress(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			GZIPInputStream gzip = new GZIPInputStream(bis);
			byte[] buf = new byte[1024];
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	/**
	 * gzip解压
	 */
	public static byte[] gzipDecompress(InputStream is) {
		byte[] b = null;
		try {
			GZIPInputStream gzip = new GZIPInputStream(is);
			byte[] buf = new byte[1024];
			int num = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((num = gzip.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			b = baos.toByteArray();
			baos.flush();
			baos.close();
			gzip.close();
			is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}
}
