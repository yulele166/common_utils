package com.peanut.commons.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * 集合相关的工具类
 */
public class CollectionUtil {
	/**
	 * 构建泛型类型的HashMap,该Map的初始容量是0
	 * 
	 * @param <K>
	 * @param <V>
	 * @return
	 */

	public static <K, V> Map<K, V> hashMap() {

		return new HashMap<K, V>(0);
	}

	public static <K, V> ConcurrentHashMap<K, V> concurrentMap() {
		return new ConcurrentHashMap<K, V>();
	}

	public static <E> List<E> arrayList() {
		return new ArrayList<E>();
	}

	public static <E> List<E> arrayList(E obj) {
		List<E> _list = arrayList();
		_list.add(obj);
		return _list;
	}

	public static <E> LinkedList<E> linkedList() {
		return new LinkedList<E>();
	}

	public static <E> Set<E> hashSet() {
		return new HashSet<E>();
	}

	public static <E> List<E> arrayList(Collection<E> c) {
		return new ArrayList<E>(c);
	}

	public static <E> Set<E> hashSet(Collection<E> c) {
		return new HashSet<E>(c);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Map<?, ?> map, Object obj, Class<T> clazz) {
		return (T) map.get(obj);
	}

	public static <E> boolean isEmpty(Collection<E> c) {
		return c == null || c.isEmpty();
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

}
