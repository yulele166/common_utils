package com.peanut.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class FileUtil {

	// 日志
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	// 加解密密钥
	public static final String AES_KEY = "9618433740985479";

	/**
	 * @param args
	 * @throws UnsupportedEncodingException  cn.cheering.createuid.util.FileUtil
	 */
//	public static void main(String[] args) throws UnsupportedEncodingException {
//		
//		/* 开始时间和结束时间 */
//		String calDate = ConfigUtil.getString("CalDate");
//		String calStart = ConfigUtil.getString("CalStart");
//		String calEnd = ConfigUtil.getString("CalEnd");
//		logger.info(" CalDate: " + calDate  + " CalStart:"+ calStart + " CalEnd:"+calEnd );
//		String start = null;
//		String end = null;
//		if (calDate.contains(",")) {
//			String[] dates = calDate.split(",");
//			start = dates[0];
//			if (dates.length > 1) {
//				end = dates[1];
//			}
//		} else {
//			start = calDate;
//			end = calDate;
//		}
//		start += " " + calStart;
//		end += " " + calEnd;
//		long s = DateUtil.getMSELByFormat(start, "yyyy-MM-dd HH");
//		long e = DateUtil.getMSELByFormat(end, "yyyy-MM-dd HH");
//		/* 写文件 */
//		Map<String, String> domainMap = ConfigUtil.getMap("DataDomain", ",");
//		String srcPath = ConfigUtil.getString("DataSource");
//		srcPath = srcPath.replace("\\", File.separator);
//		String logTime = null;
//		while (s <= e) {
//			logTime = DateUtil.formatDate(s);
//			logTime = logTime.substring(0, 10);
//			logger.error(" logTime: " + logTime + " srcPath: " + srcPath);
//			getFileList(srcPath, domainMap, logTime);
//			s = s + 60 * 60 * 1000;
//		}
//	}


	/**
	 * 获取数据文件列表
	 */
	public static void getFileList(String srcPath, Map<String, String> domainMap,
			String logTime) {
		/* 取服务器时间 */
		Date date = null;
		String upDate = "";
		
		/* 多个域名 */
		int datasource = 1;
		for(String domain : domainMap.keySet()){
			if(domain.equalsIgnoreCase("pl.5bdzc.com")){
				datasource = 3;//超级优化
			}else{
				datasource = 1;
			}
			/* 压缩文件 */
			String analyName1 = domain + "_" + logTime + ".log.gz";
			String analyPath1 = srcPath + File.separator + analyName1;
			File file = new File(analyPath1);
			/* 未压缩文件 */
			String analyName2 = domain + "_" + logTime + ".log";
			String analyPath2 = srcPath + File.separator + analyName2;
			File file2 = new File(analyPath2);
//			logger.error(" analyPath1: "+ analyPath1 + file.exists() +" analyPath2:"+analyPath2+file2.exists());
			/* 是否压缩 */
			boolean isZip = false;
			String analyPath = null;
			if (file.exists() && file.isFile()) {
				isZip = true;
				analyPath = analyPath1;
			} else if (file2.exists() && file2.isFile()) {
				isZip = false;
				analyPath = analyPath2;
			} else {
				continue;
			}
//			logger.error(" analyPath: " + analyPath + " isZip: " + isZip);
			/* 读取文件 */
			try {
				FileInputStream fis = new FileInputStream(analyPath);
				readZipFile(fis, upDate, isZip, datasource);
				fis.close();
			} catch (FileNotFoundException e) {
				logger.error(e.getLocalizedMessage(), e);
			} catch (IOException e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		}
	}

	/**
	 * 读取gzip文件，去掉文件头
	 */
	public static void readZipFile(InputStream fis, String upDate, boolean isZip,int datasource) {
		/* 文件流 */
		InputStreamReader is = null;
		try {
			/* 解压 */
			if (isZip) {
				GZIPInputStream gzip = new GZIPInputStream(fis);
				is = new InputStreamReader(gzip);
			} else {
				is = new InputStreamReader(fis);
			}

			BufferedReader br = new BufferedReader(is,10*1024*1024);//10M缓存
			while (br.ready()) {
				/* 去掉文件头： 时间,ip */
				String tempLine = br.readLine();
				String[] str = tempLine.split("\"");
				if (str.length > 2)
					tempLine = str[2];
				
				// logger.info(tempLine);
				if (tempLine.equalsIgnoreCase("-")) {
					continue;
				}
				/* 十六进制转换 */
				byte[] data = GetLogBuffer(tempLine);
				/* 解压 */
				data = gzipDecompress(data);
				if (data == null)
					continue;
				/* 解密 */
				byte[] out = decrypt(AES_KEY, data);
				if (out == null)
					continue;
				/* 读取文件内容 */
				readLogFile(out, upDate, datasource);
			}
			
			br.close();
			is.close();
			

		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage(), ex);
		}
	}

	/**
	 * 十六进制转换
	 * 
	 * @param line
	 * @return byte[] data
	 */
	public static byte[] GetLogBuffer(String line) {
		/* 输出 */
		byte[] data = null;
		try {
			List<Byte> bufferList = new ArrayList<Byte>();
			int index = 0;
			int linelength = line.length();
			while (index < linelength) {
				byte[] temp = null;
				if (index <= linelength - 2
						&& line.substring(index, index + 2).equalsIgnoreCase(
								"\\x")) {
					int endIndex = index + 4;
					if (index + 4 > linelength)
						endIndex = linelength;
					String srcStr = line.substring(index + 2, endIndex);
					/* 16进制String转byte[] */
					temp = ConvertUtil.hexStringToBytes(srcStr);
					// logger.info(srcStr+" "+ConvertUtil.bytesToHexString(temp));
					index = index + 4;
				} else {
					char c = line.charAt(index);
					/* 16进制String转byte[] */
					temp = ConvertUtil.getBytes(c);
					// logger.info(c+" "+ConvertUtil.bytesToHexString(temp));
					index = index + 1;
				}

				for (byte t : temp)
					bufferList.add(t);
			}

			/* 输出字节数组 */
			data = new byte[bufferList.size()];
			int i = 0;
			for (Byte b : bufferList) {
				data[i] = b;
				i++;
			}
//			logger.info("16进制转换："
//					+ ConvertUtil.bytesToHexString(data).toString());
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}

		return data;
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
			logger.info(ex.getLocalizedMessage(), ex);
		}
		return b;
	}

	/**
	 * 解密
	 * 
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
			// + ConvertUtil.bytesToHexString(decrypted).toString());
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		return decrypted;
	}

	
	/**
	 * 解析日志文件内容
	 */
	public static void readLogFile(byte[] data, String upDate, int datasource) {
		/* 去除空字符、换行等 */
		byte[] content = new byte[data.length];
		int i = 0;
		for (byte b : data) {
			if (b == 10 || b == 13 || b == 32 || b == 9) {
				continue;
			}
			content[i] = b;
			i++;
		}

		try {
			/* 读取字节流 */
			ByteArrayInputStream bis = new ByteArrayInputStream(content);
			BufferedInputStream in = new BufferedInputStream(bis);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = -1;
			while ((len = in.read(buffer, 0, buf_size)) != -1) {
				bos.write(buffer, 0, len);
			}
			/* 文件内容 ，解决乱码问题 */
			byte[] out = bos.toByteArray();
			String line = new String(out, "utf-8");
			/* 关闭输入流 */
			bos.flush();
			bos.close();
			in.close();
			bis.close();

			/* 解析采集数据： 手机信息=headStr，行为数据= actionStr */
			String[] dataStr = line.split("\\|");
			String headStr = "";
			String actionStr = "";
			/* 防止行为数据为空 */
			if (dataStr.length == 1) {
				headStr = dataStr[0];
			} else if (dataStr.length > 1) {
				headStr = dataStr[0];
				actionStr = dataStr[1];
			}

			
		} catch (FileNotFoundException e) {
			logger.error(e.getLocalizedMessage(), e);
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
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
		
		if (subIndex > 0 && subIndex<imeicode.length()) {
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
			logger.error(e.getLocalizedMessage(), e);
			ret = null;
		}

		return ret;
	}
	public static void main(String[] args) {
		System.out.println(getIMEI("1458424790877","2097877870692784320488353491"));
	}

}
