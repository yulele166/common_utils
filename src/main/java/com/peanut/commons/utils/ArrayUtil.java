package com.peanut.commons.utils;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
public class ArrayUtil {
    
    public static boolean isIllegalIndex (int index, int arraySize) {
        return index < 0 || index >= arraySize;     
    }

    public static int[] intList2Array(List<Integer> list) {
        if (list != null) {
            int[] ary = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                ary[i] = list.get(i);
            }
            return ary;
        }
        return null;
    }

    public static int[] intSet2Array(Set<Integer> set) {
        if (set != null) {
            int i = 0;
            int[] ary = new int[set.size()];
            for (Integer integer : set) {
                ary[i] = integer;
                i++;
            }
            return ary;
        }
        return null;
    }
    
    /**
     * 创建一个二维数组
     * 
     * @param <T>
     *            数组单元的类型
     * @param rows
     *            数组的第一维长度
     * @param cols
     *            数组的第二位长度
     * @param clazz
     *            数组单元的类型
     * @return 如果类型实例化失败,则数组里的单元为null
     */
    @SuppressWarnings("unchecked")
    public static <T> T[][] createTwoDimensionArray(int rows, int cols,
            Class<T> clazz) {
        T[][] arr = (T[][]) Array.newInstance(clazz, rows, cols);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                try {
                    arr[y][x] = clazz.newInstance();
                } catch (Exception e) {
                    arr[y][x] = null;
                }
            }
        }
        return arr;
    }

    /**
     * 进行数组比较,每次比较会将结果回调给不同的函数.如果数组中存在null的元素, 则跳过此元素进行下一个的比较,且不会调用
     * <code>compCallback</code>
     * 
     * @param <T>
     *            数组的类型,必须实现<code>Comparable</code>接口
     * @param arr1
     *            第一个数组
     * @param arr2
     *            第二个数组
     * @param compCallback
     *            比较后的回调函数,第一个参数是比较值comp,见<code>Compareble.compareTo</code>方法.
     *            第二个参数根据comp的值不同,当comp小于0时为较小的值,当comp等于0时为相同的值,当comp大于0时为较大的值
     */
    public static <T extends Comparable<T>> void compare(T[] arr1, T[] arr2,Functions.Function2<Integer, T> compCallback) {
        int index1 = 0;
        int index2 = 0;
        while (index1 < arr1.length && index2 < arr2.length) {
            T obj1 = arr1[index1];
            T obj2 = arr2[index2];
            if (obj1 == null) {
                index1++;
                continue;
            }
            if (obj2 == null) {
                index2++;
                continue;
            }
            int comp = obj1.compareTo(obj2);
            if (comp < 0) {
                compCallback.apply(comp, obj1);
                index1++;
            } else if (comp == 0) {
                compCallback.apply(comp, obj1);
                index1++;
                index2++;
            } else {
                compCallback.apply(comp, obj2);
                index2++;
            }
        }
        // 修正从单元格0（第一行第一列）移动到单元格9（第二行第一列）时比较结果不准确的问题
        // modified by zhangwh 2010/3/29
        while (index1 < arr1.length) {
            if (arr1[index1] != null)
                compCallback.apply(-1, arr1[index1]);
            index1++;
        }
        while (index2 < arr2.length) {
            if (arr2[index2] != null)
                compCallback.apply(1, arr2[index2]);
            index2++;
        }
    }
    
    public static int[] mergeArray(int[] source,int... updates){
        if(updates == null || updates.length == 0){
            return source;
        }
        if(source == null || source.length == 0){
            return updates;
        }
        int[] arr = Arrays.copyOf(source, source.length + updates.length);
        System.arraycopy(updates, 0, arr, source.length, updates.length);
        return arr;
    }
    
    /**
     * 简单的Merge... XXX 有需要再加原生类型的merge吧...
     * @description: 
     * @param <T>
     * @param source
     * @param updates
     * @return
     */
    public static <T> T[] mergeArray(T[] source,T[] updates){
        if(updates == null || updates.length == 0){
            return source;
        }
        if(source == null || source.length == 0){
            return updates;
        }
        T[] arr = Arrays.copyOf(source, source.length + updates.length);
        System.arraycopy(updates, 0, arr, source.length, updates.length);
        return arr;
    }
    
    public static <T> T[] mergeArray(T[] source,T update){
        if(update == null){
            return source;
        }
        T[] arr = Arrays.copyOf(source, source.length + 1);
        arr[source.length] = update;
        return arr;
    }
    
    /**
     * 大批量的merge~。~保证不为空
     * @description: 
     * @param <T>
     * @param arrays
     * @return
     */
    public static <T> T[] mergeArray(T[]...  arrays){
        T[] arr = null;
        for(int i=0;i<arrays.length;i++){
            arr = mergeArray(arr,arrays[i]);
        }
        return arr;
    }
    
    public static <T> T[] getNotNullArrayData (T[] src) {
        int nullCount = 0;
        for(int i=0;i<src.length;i++){
            if(src[i] == null){
                nullCount ++;
            }else{
                if(nullCount > 0){
                    src[i - nullCount] = src[i];
                    src[i] = null;
                }
            }
        }
        return Arrays.copyOf(src, src.length - nullCount);
    }
    
    public static<E> ArrayList<E> asList (E... param) {
        List<E> _list = Arrays.asList(param);
        return new ArrayList<E>(_list);
    }
    public static void main(String[] args) {
		System.out.println(1);
	}
}
