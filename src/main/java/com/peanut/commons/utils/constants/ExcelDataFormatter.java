package com.peanut.commons.utils.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ming.yang
 * @since 2015年12月16日 上午8:28:42
 */
public class ExcelDataFormatter {

	private Map<String, Map<String, String>> formatter = new HashMap<String, Map<String, String>>();

	public void set(String key, Map<String, String> map) {
		formatter.put(key, map);
	}

	public Map<String, String> get(String key) {
		return formatter.get(key);
	}
}
