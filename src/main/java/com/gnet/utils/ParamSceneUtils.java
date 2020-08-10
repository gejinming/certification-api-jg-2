package com.gnet.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.object.OrderDirectionType;
import com.gnet.object.OrderType;
import com.gnet.pager.Pageable;
import com.jfinal.log.Logger;

/**
 * 参数情景化处理工具类
 * @author wct
 * @Date 2016年5月12日
 */
public class ParamSceneUtils {
	
	private final static Logger logger = Logger.getLogger(ParamSceneUtils.class);
	
	
	/**
	 * 排序参数解析
	 * 若无排序参数或排序参数正确成功则返回true
	 * 若有排序参数无法获得返回false
	 * @param orderProperty
	 * 			排序参数
	 * @param orderDirection
	 * 			排序方向
	 * @param typeEnum
	 * 			排序类型枚举
	 */
	public static void toOrder(Pageable pageable, String orderProperty, String orderDirection, Class<? extends OrderType> typeEnumClass) {
		toOrder(pageable, orderProperty, orderDirection, typeEnumClass, false);
	}
	
	/**
	 * 排序参数解析
	 * 若无排序参数或排序参数正确成功则返回true
	 * 若有排序参数无法获得返回false
	 * @param orderProperty
	 * 			排序参数
	 * @param orderDirection
	 * 			排序方向
	 * @param typeEnum
	 * 			排序类型枚举
	 * @param isUpperCase
	 * 			是否为大写
	 */
	public static void toOrder(Pageable pageable, String orderProperty, String orderDirection, Class<? extends OrderType> typeEnumClass, Boolean isUpperCase){
		if (StringUtils.isNotBlank(orderProperty)) {
			Map<String, String> map = null;
			try {
				map = getValueByKey(typeEnumClass);
			} catch(Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error(typeEnumClass.getName() + "failed to map", e.getCause());
				}
				pageable.setOrderProperty(null);
				pageable.setOrderDirection(null);
				return;
			}
			if (map.get(orderProperty) == null) {
				throw new NotFoundOrderPropertyException("can't analysis OrderProperty param '" + orderProperty + "'");
			}
			orderProperty = isUpperCase ? map.get(orderProperty).toUpperCase() : map.get(orderProperty);
			if (StringUtils.isBlank(orderDirection) || OrderDirectionType.DESC.getValue().equalsIgnoreCase(orderDirection)) {
				orderDirection = isUpperCase ? OrderDirectionType.DESC.getValue().toUpperCase() : OrderDirectionType.DESC.getValue();
			} else if (OrderDirectionType.ASC.getValue().equalsIgnoreCase(orderDirection)) {
				orderDirection = isUpperCase ? OrderDirectionType.ASC.getValue().toUpperCase() : OrderDirectionType.ASC.getValue();
			} else {
				throw new NotFoundOrderDirectionException("can't analysis OrderDirection param '" + orderDirection + "'");
			}
		} else {
			orderProperty = null;
			orderDirection = null;
		}
		pageable.setOrderProperty(orderProperty);
		pageable.setOrderDirection(orderDirection);
	}
	
	/**
	 * 
	 * @param cls
	 * 		枚举类型的类
	 * @return
	 * 		枚举转化成map
	 * @throws Exception
	 */
	private static Map<String, String> getValueByKey(Class<? extends OrderType> cls) throws Exception {
		Map<String, String> result = new HashMap<>();
		Method method = cls.getMethod("values");
		OrderType[] orderTypes = (OrderType[]) method.invoke(null);
		for (OrderType orderType : orderTypes) {
			result.put(orderType.getKey(), orderType.getValue());
		}
		return result;
	}

}
