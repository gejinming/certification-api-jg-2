package com.gnet.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @type utils
 * @description 集合工具类 提供了分割字符串,字符串数组转化成字符串、图(Map),长整型数组转化为字符串的操作
 *              提供了判断对象是否在某个对象集里、一组数组是否包含在另一组数组里的操作 提供了获取一组数组不在另一组数组内以的部分操作
 *              提供了字符串出现次数的操作 提供了List列表增加长度元素、移除空元素
 * @author xuq
 * @date 2014-06-20
 * @version 1.0
 */
public class CollectionKit {

	public static final String noop = "";

	/**
	 * 将字符串数组转化为根据separator分隔符来分割的字符串
	 * 
	 * @description 传入字符串数组、分隔符，将字符串数组中的字符串通过分隔符拼接成一个字符串。 传入添加单引号标志位(boolean
	 *              string)，若传入true,则在拼接时在子字符串旁加单引号。若传入false则不加 返回拼接后的字符串
	 *              若字符串数组长度为0，则返回空字符串
	 * @version 1.0
	 * @param strs
	 * @param separator
	 * @return
	 */
	public static String convert(String[] strs, String separator, boolean string) {
		/**
		 * fix-bug: 修复数组为空的情况下，会出现错误异常 2015年2月9日 xuq
		 */
		if (strs.length == 0) {
			return noop;
		}
		StringBuffer result = new StringBuffer();
		for (String str : strs) {
			result.append(string ? "'" + str + "'" : str).append(separator);
		}
		result.deleteCharAt(result.length() - 1);
		return result.toString();
	}

	/**
	 * 将Long数组转化为根据separator分隔符来分割的字符串
	 * 
	 * @description 传入长整型数组和分隔符，将长整型数组中的长整型数通过分隔符拼接成一个字符串 若长整型数组长度为0，则返回空字符串
	 * @version 1.0
	 * @param longs
	 * @param separator
	 * @return
	 */
	public static String convert(Long[] longs, String separator) {
		/**
		 * fix-bug: 修复数组为空的情况下，会出现错误异常 2015年2月9日 xuq
		 */
		if (longs.length == 0) {
			return noop;
		}
		StringBuffer result = new StringBuffer();
		for (Long lon : longs) {
			result.append(lon).append(separator);
		}
		result.deleteCharAt(result.length() - 1);
		return result.toString();
	}

	/**
	 * 将Long数组转化为根据separator分隔符来分割的字符串
	 * 
	 * @description 传入长整型数组和分隔符，将长整型数组中的长整型数通过分隔符拼接成一个字符串 若长整型数组长度为0，则返回空字符串
	 * @version 1.0
	 * @param longs
	 * @param separator
	 * @return
	 */
	public static String convert(Integer[] integers, String separator) {
		/**
		 * fix-bug: 修复数组为空的情况下，会出现错误异常 2015年2月9日 xuq
		 */
		if (integers.length == 0) {
			return noop;
		}
		StringBuffer result = new StringBuffer();
		for (Integer integer : integers) {
			result.append(integer).append(separator);
		}
		result.deleteCharAt(result.length() - 1);
		return result.toString();
	}

	/**
	 * 将Long的list转化为根据separator分隔符来分割的字符串
	 * @description 传入长整型数组和分隔符，将长整型数组中的长整型数通过分隔符拼接成一个字符串 若长整型数组长度为0，则返回空字符串
	 * @param List<Long>
	 * @param separator
	 * @return
	 */
	public static String convert(List<Long> integerList, String separator) {
		if (integerList.isEmpty()) {
			return noop;
		}
		StringBuffer result = new StringBuffer();
		for (Long integer : integerList) {
			result.append(integer).append(separator);
		}
		result.deleteCharAt(result.length() - 1);
		return result.toString();
	}
	
	/**
	 * 将字符串进行分割
	 * 
	 * @description 用指定分割符分割指定的字符串，并返回分割后的字符串 若指定的字符串为空，则返回空的字符串数组
	 *              若没有指定的分割符，则返回一个字符串数组，第一个元素的内容为指定字符串
	 * @version 1.0
	 * @param str
	 * @param separator
	 * @return
	 */
	public static String[] convert(String str, String separator) {
		if (StringUtils.isBlank(str)) {
			return new String[0];
		}
		if (!str.contains(separator)) {
			return new String[] { str };
		}
		return str.split(separator);
	}
	
	/**
	 * 将字符串进行分割，转化为Map
	 * 
	 * @description 传入字符串数组和分隔符，将字符串数组中的字符串以分隔符分割
	 *              将分割符的前半部分作为图(Map)的键,将分割符的后半部分作为图(Map)的值 返回得到的图(Map)
	 *              若某个字符串不包含分隔符，则在运行时抛出没有分隔符的异常
	 * @version 1.0
	 * @param str
	 * @param separator
	 * @return
	 */
	public static Map<String, String> convertToMap(String[] strs, String separator) {
		Map<String, String> map = Maps.newConcurrentMap();
		for (String str : strs) {
			if (!str.contains(separator)) {
				throw new RuntimeException("str split error, there is no " + separator + " in this strs[]");
			}
			String[] attrMapping = str.split(separator);
			map.put(attrMapping[0], attrMapping[1]);
		}
		return map;
	}

	/**
	 * 2个集合的交集
	 * @description  传入2个string类型的集合
	 *               循环其中一个集合判断集合中每一个元素是否属于另外一个集合
	 *               如果属于则加入一个新的集合中并且返回
	 * 
	 * @version 1.0
	 * @param strList1
	 * @param strList2
	 * @return
	 */
	public static List<String> intersection(List<String> strList1, List<String> strList2){
		List<String> strList = Lists.newArrayList();
		for(String str : strList1){
			if(strList2.contains(str)){
				strList.add(str);
			}
		}	
		return strList;
	}

	/**
	 * 2个集合的差集
	 * @param big
	 * @param small
	 * @return
	 */
	public static <T> List<T> getDifferenceSetByGuava(List<T> big, List<T> small) {
		Set<T> differenceSet = Sets.difference(Sets.newHashSet(big), Sets.newHashSet(small));
		return Lists.newArrayList(differenceSet);
	}


	/**
	 * 计算字符串出现的次数，未找到返回0
	 * 
	 * @description 传入字符串与需要寻找的字符串，返回查找的字符串出现的次数
	 * @version 1.0
	 * @param str
	 * @param con
	 * @return
	 */
	public static int numberOfStr(String str, String con) {
		str = " " + str;
		if (str.endsWith(con)) {
			return str.split(con).length;
		} else {
			return str.split(con).length - 1;
		}
	}

	/**
	 * 是否存在某个值
	 * 
	 * @description 传入对象数组和需要寻找的对象 若找到返回true，若未找到返回false
	 *              若传入的需要寻找的对象与对象数组中的某个对象类型不符，运行时抛出异常类型不符
	 * @version 1.0
	 * @param str
	 * @param con
	 * @return
	 */
	public static boolean isExisted(Object[] objs, Object con) {
		for (Object object : objs) {
			if (!con.getClass().equals(object.getClass())) {
				throw new RuntimeException("type is not equal");
			}
			if (object.equals(con)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 删除空元素
	 * 
	 * @description 新建一个空的列表，对传入的列表进行遍历，将不为空的元素加入到新建列表中，返回新的列表 若传入列表为空，返回null
	 * @version 1.0
	 * @param <T>
	 * @param list
	 *            list列表
	 * @return 结果
	 */
	public static <T> List<T> removeEmptyItem(List<T> list) {
		List<T> result = new ArrayList<T>();

		if (list == null || list.size() <= 0)
			return null;
		// 循环第一层
		for (T t : list) {
			if (t != null)
				result.add(t);
		}

		return result;
	}

	/**
	 * list增加item 对于超出，则自动增加list的长度
	 * 
	 * @description 传入需要插入的项、需要插入项的列表以及插入的位置 若插入的项的位置大于列表最大长度，则列表增长到插入位的长度
	 *              插入需要插入的项
	 * @version 1.0
	 * @param list
	 *            数组
	 * @param index
	 *            插入的索引
	 * @param item
	 *            插入值
	 */
	public static void addItem(List<Object> list, Integer index, Object item) {
		Integer size = list.size();
		if (index + 1 > size)
			list.addAll(Lists.newArrayList(new Object[index + 1 - size]));
		list.set(index, item);
	}

	/**
	 * 判断compareArray是否被包含在array中 检验规则： type 1: 全部校验则通过 type 2: 满足一个检验通过
	 * 
	 * @description 传入目标数组、查找的数组以及检验规则 遍历目标数组
	 *              若校验规则为满足一个检验通过就通过，则匹配到内容后返回true，匹配不到返回false
	 *              若校验规则为全部校验则通过，则比较每一项是否相同，都相同返回true，有不相同返回false
	 *              若目标的数组或是查找的数组为空，则返回false 若原数组元素的类型与查找数组的类型不符，则返回false
	 * @version 1.0
	 * @param array
	 *            目标数组
	 * @param compareArray
	 *            查找的数组
	 * @param type
	 *            类型
	 * @return 是否包含
	 */
	public static boolean contains(Object[] array, Object[] compareArray, Integer type) {
		boolean result = true;
		if (array == null || array.length == 0 || compareArray == null || compareArray.length == 0) {
			return false;
		}
		if (!((Object) array).getClass().getComponentType().equals(((Object) compareArray).getClass().getComponentType())) {
			return false;
		}
		for (Object item : compareArray) {
			if (!ArrayUtils.contains(array, item)) {
				result = false;
			} else if (type.equals(2)) {
				return true;
			}
		}
		return result;
	}

	/**
	 * 获取包含compareArray的array以外部分
	 * 
	 * @description 传入目标数组和被包含的数组，新建一个空的列表 遍历目标数组，若有元素不存在被包含的数组中，则加入新建的列表
	 *              返回新建的列表，即所有在被包含的数组外的元素 若目标数组与被包含数组为空，则返回空字符串
	 *              若目标数组的类型与被包含数组的类型不同，则返回空的字符串
	 * @version 1.0
	 * @param array
	 *            目标数组
	 * @param containsArray
	 *            被包含数组
	 * @param type
	 *            类型
	 * @return 是否包含
	 */
	public static Object[] except(Object[] array, Object[] containsArray) {
		List<Object> result = Lists.newArrayList();
		Object[] noop = new Object[] {};
		if (array == null || array.length == 0 || containsArray == null || containsArray.length == 0) {
			return noop;
		}
		if (!((Object) array).getClass().getComponentType().equals(((Object) containsArray).getClass().getComponentType())) {
			return noop;
		}
		for (Object item : array) {
			if (!ArrayUtils.contains(containsArray, item)) {
				result.add(item);
			}
		}
		return result.toArray();
	}

	public static void main(String[] args) {
		String[] ids = convert("23413,23143,", ",");
		System.out.println(ids.length);
		for (String id : ids) {

			System.out.println(id);
		}

	}

}
