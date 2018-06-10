package com.peanut.commons.utils;

/**
 * 
 *
 * 断言工具类，用于对方法的传入参数进行校验，如果未通过则
 * 抛出<code>IllegalArgumentException</code>异常
 */
public abstract class Assert {
    
    /**
     * 断言对象不为空
     * 
     * @param obj
     */
    public static void notNull(Object obj) {
        notNull(obj, null);     
    }

    /**
     * 断言对象不为空
     * 
     * @param obj
     */
    public static void notNull(Object obj, String msg) {
        if (obj == null) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 断言表达式为真
     * 
     * @param expression
     */
    public static void isTrue(boolean expression) {
        isTrue(expression, null);
    }

    /**
     * 断言表达式为真
     * 
     * @param expression
     */
    public static void isTrue(boolean expression, String msg) {
        if (!expression) {
            throw new IllegalArgumentException(msg);
        }
    }
    
    /** 
     * @description: 断言数组没有越界
     * @author: yong_li 
     * @param index
     * @param arraySize
     */ 
    public static void isValidIndex(int index, int arraySize) {
        isValidIndex(index, arraySize, null);               
    }
    
    /** 
     * @description: 断言数组没有越界
     * @author: yong_li 
     * @param index
     * @param arraySize
     * @param msg
     */ 
    public static void isValidIndex(int index, int arraySize, String msg) {
        if(index < 0 || index >= arraySize){
            throw new ArrayIndexOutOfBoundsException(msg);
        }       
    }
}
