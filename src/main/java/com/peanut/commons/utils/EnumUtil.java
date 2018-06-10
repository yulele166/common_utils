package com.peanut.commons.utils;

import java.util.List;

/**
 * 枚举工具类
 */
public class EnumUtil {
    /**
     * 根据枚举index返回枚举元素，index从0开始
     * 
     * @param <T>
     *            枚举类型
     * @param values
     *            枚举元素输注
     * @param index
     *            从0开始的index
     * @return 枚举元素
     */
    public static <T extends Enum<T>> T valueOf(List<T> values, int index) {
        if(values == null || index < 0 || index >= values.size()){
            return null;
        }
        return values.get(index);       
    }
    
    /** 
     * @description: 根据枚举index返回枚举元素，如果元素为null，则取默认值
     * @author: yong_li 
     * @param <T>
     * @param values
     * @param index
     * @param defaultValue
     * @return
     */ 
    public static <T extends Enum<T>> T valueOf(List<T> values, int index, T defaultValue) {
        T value = valueOf(values, index);
        if(value != null){
            return value;
        }
        return defaultValue;
    }
}
