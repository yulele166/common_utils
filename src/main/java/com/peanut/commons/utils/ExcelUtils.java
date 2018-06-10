package com.peanut.commons.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * excel工具，用户读取excel
 */
public class ExcelUtils {
    @SuppressWarnings("unchecked")
    public static <T> List<T> parseExcel(Class<T> cls, String f) {
        File file = new File(f);
        if (!file.isFile()) throw new IllegalArgumentException("f is not a file: " + f);
        if (!file.getName().endsWith(".xls") && !file.getName().endsWith(".xlsx")) throw new IllegalArgumentException("f is not excel file: " + f);
        
        List<T> ret = new ArrayList<T>();
        int row = 0;
        String headerName = null;
        try {
            Workbook wb = WorkbookFactory.create(file);
            Sheet sheet = wb.getSheetAt(0);
            String[] header = parseHeader(sheet.getRow(1));
            String[] type = parseHeader(sheet.getRow(2));
            int len = Math.min(header.length, type.length);
            a:for (Row r : sheet) {
                if (r.getRowNum() > 2) {
                    row = r.getRowNum();
                    Object o = cls.newInstance();
                    int index = 0;
                    for (int i = 0; i < len; i++, index++) {
                        if (index > type.length - 1 || StringUtils.isEmpty(header[index]) || StringUtils.isEmpty(type[index])) continue;
                        
                        Cell c = r.getCell(i, Row.CREATE_NULL_AS_BLANK);
                        c.setCellType(Cell.CELL_TYPE_STRING);
                        
                        if (i == 0 && StringUtils.isEmpty(c.getStringCellValue())) continue a;
                        
                        headerName = header[index];
                        setProperty(o, header[index].trim(), type[index].trim(), StringUtils.trim(c.getStringCellValue()));
                    }
                    ret.add((T) o);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("[parse excel error]: " + f + " [row]: " + row + " [cell]: " + headerName, e);
        }
        
        return Collections.unmodifiableList(ret);
    }
    
    private static void setProperty(Object o, String name, String type, String stringCellValue) throws NoSuchFieldException, SecurityException, NumberFormatException, IllegalArgumentException, IllegalAccessException, ParseException {
        Field field = o.getClass().getDeclaredField(name);
        field.setAccessible(true);
        switch (type) {
            case "int": field.setInt(o, NumberUtils.toInt(stringCellValue)); break;
            case "long": field.setLong(o, NumberUtils.toLong(stringCellValue)); break;
            case "double": field.setDouble(o, NumberUtils.toDouble(stringCellValue)); break;
            case "string": field.set(o, stringCellValue); break;
            case "list<int>": field.set(o, parseListObj("int", stringCellValue)); break;
            case "list<double>": field.set(o, parseListObj("double", stringCellValue)); break;
            case "list<string>": field.set(o, parseListObj("string", stringCellValue)); break;
            case "map": field.set(o, parseMapObj(stringCellValue)); break;
            case "date": field.set(o, parseDate(stringCellValue));break;
        }
    }

    /**
     * @param stringCellValue
     * @return
     * @throws ParseException 
     */
    private static Date parseDate(String str) throws ParseException {
        if (StringUtils.isEmpty(str)) return null;
        return DateUtils.parseDate(str, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * @param stringCellValue
     * @return
     */
    private static Object parseMapObj(String s) {
        if (StringUtils.isEmpty(s)) return Collections.unmodifiableMap(Collections.emptyMap());
        Map<Integer, Double> ret = new HashMap<>();
        for (String e : s.split(";")) {
            String[] kv = e.split(",");
            ret.put(Integer.valueOf(kv[0].trim()), Double.valueOf(kv[1].trim()));
        }
        return Collections.unmodifiableMap(ret);
    }

    /**
     * @param type
     * @param stringCellValue
     * @return
     */
    private static Object parseListObj(String type, String s) {
        switch (type) {
            case "int": return toList(s, new ParseCallback<Integer>() {
    
                @Override
                public Integer parse(String t) {
                    return Integer.valueOf(t);
                }
                
            });
            case "double": return toList(s, new ParseCallback<Double>() {
                
                @Override
                public Double parse(String t) {
                    return Double.valueOf(t);
                }
                
            });
            case "string": return toList(s, new ParseCallback<String>() {
                
                @Override
                public String parse(String t) {
                    return t;
                }
                
            });
        }
        return null;
    }

    private interface ParseCallback<T> {
        T parse(String t);
    }
    
    /**
     * @param stringCellValue
     * @return
     */
    private static <T> List<T> toList(String s, ParseCallback<T> t) {
        if (s == null || s.trim().length() == 0) {
            List<T> ret = Collections.emptyList();
            return Collections.unmodifiableList(ret);
        }
        
        String[] ss = s.split(",");
        List<T> ret = new ArrayList<>();
        for (String e : ss) {
            ret.add(t.parse(e.trim()));
        }
        return Collections.unmodifiableList(ret);
    }
    
    /**
     * @param row
     * @return
     */
    private static String[] parseHeader(Row row) {
        String[] ret = new String[row.getPhysicalNumberOfCells()];
        int i = 0;
        for (Cell c : row) {
            c.setCellType(Cell.CELL_TYPE_STRING);
            ret[i] = toLowerCamelCase(c.getStringCellValue().split("-")[0]);
            i++;
        }
        return ret;
    }

    public static String toLowerCamelCase(String source) {
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        for (char c : source.toLowerCase().toCharArray()) {
            if (c != '_' && c >= 97 && c <= 122) {
                sb.append(flag ? (char)(c - 32) : c);
                flag = false;
            }
            else if (c != '_') sb.append(c);
            else flag = true;
        }
        return sb.toString();
    }
    
    public static String toUpperCamelCase(String source) {
        String a = toLowerCamelCase(source);
        return a.substring(0, 1).toUpperCase() + a.substring(1, a.length());
    }
    
    public static void main(String[] args) throws ParseException {
        System.out.println(DateUtils.parseDate("2013-10-10  10:00:00", "yyyy-MM-dd HH:mm:ss"));
    }
}
