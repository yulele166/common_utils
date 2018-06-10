package com.peanut.commons.utils;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @author ming.yang
 * @since 2016年4月18日 下午8:59:56
 */
public class StringUtil {
	/**
	 * 改自 jdk中String 类中的方法， 为了处理日志中参数取不到值 出现连续分隔符的情况
	 * 	例如 ：  1_2_3____ 原始分割中  一单遇到 空字符串在末尾 无法添加至数组中，统一改为unknow替换
	 * @param str
	 * @param regex
	 * @param limit
	 * @return
	 */
	public static String[] split(String str, String regex, int limit) {

		char ch = 0;
		if (((regex.length() == 1 && ".$|()[{^?*+\\".indexOf(ch = regex.charAt(0)) == -1)
				|| (regex.length() == 2 && regex.charAt(0) == '\\' && (((ch = regex.charAt(1)) - '0') | ('9' - ch)) < 0
						&& ((ch - 'a') | ('z' - ch)) < 0 && ((ch - 'A') | ('Z' - ch)) < 0))
				&& (ch < Character.MIN_HIGH_SURROGATE || ch > Character.MAX_LOW_SURROGATE)) {
			int off = 0;
			int next = 0;
			boolean limited = limit > 0;
			ArrayList<String> list = new ArrayList<>();
			while ((next = str.indexOf(ch, off)) != -1) {
				if (!limited || list.size() < limit - 1) {
					if (("").equals(str.substring(off, next))) {
						list.add("unknow");
					} else {
						list.add(str.substring(off, next));
					}
					off = next + 1;
				} else { // last one
					// assert (list.size() == limit - 1);
					if ("".equals(str.substring(off, str.length()))) {
						list.add("unknow");
					} else {
						list.add(str.substring(off, str.length()));
					}
					off = str.length();
					break;
				}
			}
			// If no match was found, return this
			if (off == 0)
				return new String[] { str };

			// Add remaining segment
			if (!limited || list.size() < limit)
				if ("".equals(str.substring(off, str.length()))) {
					list.add("unknow");
				} else {
					list.add(str.substring(off, str.length()));
				}

			// Construct result
			int resultSize = list.size();
			if (limit == 0) {
				while (resultSize > 0 && list.get(resultSize - 1).length() == 0) {
					resultSize--;
				}
			}
			String[] result = new String[resultSize];
			return list.subList(0, resultSize).toArray(result);
		}
		return Pattern.compile(regex).split(str, limit);

	}

	public static String[] split(String str, String regex) {
		return split(str, regex, 0);
	}

	public static void main(String[] args) {
		String[] ss = split("1_2_3__4___", "_");
		String s = "";
		System.out.println(ss.length);
		for (String string : ss) {
			System.out.println(string);
		}
	}
}
