/**
 * $Id: CommonCst.java 716657 2014-05-07 08:02:07Z bjwangwei $
 * Copyright(C) 2014-2020 netease - easegame, All Rights Reserved.
 */
package com.peanut.commons.utils.constants;
/**
 * 公共包常量
 * @author <a href="mailto:bjwangwei@corp.netease.com">Wang Wei</a>
 * @version 1.0 2014年5月7日 下午3:07:23
 */
public class CommonCst {
    /** 默认编码 */
    public static final String DEFAULT_ENCODING = "UTF-8".intern();
    
    /** 字符串默认值 */
    public static final String DEFAULT_STRING_VAL = "".intern();
    /** 整数默认值 */
    public static final int DEFAULT_INT_VAL = 0;
    /** 短整数默认值 */
    public static final short DEFAULT_SHORT_VAL = 0;
    /** 字符默认值 */
    public static final char DEFAULT_CHAR_VAL = 0;
    /** 字节默认值 */
    public static final byte DEFAULT_BYTE_VAL = 0;
    /** 长整默认值 */
    public static final long DEFAULT_LONG_VAL = 0L;
    /** 布尔默认值 */
    public static final boolean DEFAULT_BOOL_VAL = false;
    /** 单精度浮点默认值 */
    public static final float DEFAULT_FLOAT_VAL = 0.0F;
    /** 双精度浮点默认值 */
    public static final double DEFAULT_DOUBLE_VAL = 0.0D;
    
    /** 时间相关参数 */
    public static final long HOURS_PER_DAY = 24;
    public static final long MINUTES_PER_HOUR = 60;
    public static final long SECONDS_PER_MINUTE = 60;
    public static final long MILLIONSECONDS_PER_SECOND = 1000;
    public static final long MILLIONSECONDS_PER_MINUTE = MILLIONSECONDS_PER_SECOND * SECONDS_PER_MINUTE;
    public static final long MILLIONSECONDS_PER_HOUR = MILLIONSECONDS_PER_SECOND * SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    public static final long MILLION_SECOND_PER_DAY = HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE * MILLIONSECONDS_PER_SECOND;
    
    
    /** 默认日期格式 */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd".intern();
    /** 默认时间格式 */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss".intern();
    /** 默认日期时间格式(精确到秒) */
    public static final String DEFAULT_DATETIME_FORMAT = (DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT).intern();
    /** yyyyMMddHHmmss */
    public static final String FULL_DATETIME = "yyyyMMddHHmmss";
    
    public static final String DAY_HOUR_DATETIME="yyyyMMddHH";
    /** DB表名用的特殊格式 */
    public static final String DBTABLE_DATE_FORMAT = "yyyyMMdd".intern();
    
}
