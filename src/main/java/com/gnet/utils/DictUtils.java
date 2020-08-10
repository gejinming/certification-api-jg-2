package com.gnet.utils;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;

/**
 * dict search.
 * 
 * @type utils
 * @description 数据字典的工具类 数据字典的内容保存在xml文件中 工具类初始化时，传入xml文件路径，解析xml文件获得json格式的数据
 *              提供根据数据种类获得数据、根据数据种类和键值获得标记信息、根据数据类型获得所有标记信息、
 *              根据数据种类和键值获得所有标记信息的操作
 * @author xuq
 * @date 2014年10月16日
 * @version 1.0
 */
public class DictUtils {

	public static final Logger logger = Logger.getLogger(DictUtils.class);

	private static JSONArray dics = new JSONArray();
	private static JSONArray groups = new JSONArray();
	private static List<JSONObject> indexs = Lists.newArrayList();

	/**
	 * init dict data.
	 * 
	 * @param xmlPath
	 */
	public DictUtils(String xmlPath) {
		try {
			SAXReader saxReader = new SAXReader();
			String path = PathKit.getRootClassPath() + File.separator + xmlPath;
			logger.info("xml path loaded:" + path);
			Document document = saxReader.read(path);

			dics = parseDicts(document.selectNodes("//certification/dicts/dic"));
			groups = parseGroups(document.selectNodes("//certification/groups/group"));
		} catch (Exception e) {
			logger.info("parser error:" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * init dicts.
	 * 
	 * @description 初始化数据字典工具类 传入xml文件路径，解析xml文件获得json格式的数据
	 * @version 1.0
	 * @param xmlPath
	 */
	public static void init(String xmlPath) {
		new DictUtils(xmlPath);
	}
	
	/**
	 * get all dicts
	 * @return
	 */
	public static JSONArray findDicts() {
		return dics;
	}

	/**
	 * search type.
	 * 
	 * @description 传入需要的数据种类 遍历索引json数组判断数据字典是否经过分组，在数据字典中找到对应的标签组的json数组并返回
	 *              若未找到返回null
	 * @version 1.0
	 * @param type
	 * @return
	 */
	public static JSONObject findDicByType(String type) {
		Long time = new Date().getTime();
		for (JSONObject index : indexs) {
			if (type.equals(index.getString("type"))) {
				//
				boolean isGroup = index.getBooleanValue("isGroup");
				if (isGroup) {
					// search in groups and return list.
					Iterator<Object> iter = groups.iterator();
					while (iter.hasNext()) {
						JSONObject group = (JSONObject) iter.next();
						if (group.getString("type").equals(type)) {
							//logger.info("dict utils —— get this type needs：" + (new Date().getTime() - time) + "ms");
							//logger.info("dict utils —— result:" + group);
							return group;
						}
					}
				} else {
					// search in dics and return object.
					Iterator<Object> iter = dics.iterator();
					while (iter.hasNext()) {
						JSONObject dic = (JSONObject) iter.next();
						if (dic.getString("type").equals(type)) {
							//logger.info("dict utils —— get this type needs：" + (new Date().getTime() - time) + "ms");
							//logger.info("dict utils —— result:" + dic);
							return dic;
						}
					}
				}
			}
		}
		logger.info("dict utils —— get this type needs：" + (new Date().getTime() - time) + "ms");
		return null;
	}

	/**
	 * get label by type and key.
	 * 
	 * @description 传入需要的数据种类和键值 获得对应数据种类的标签组的json数组，获得数组中的所有键值，进行遍历。
	 *              当匹配到目标键值，返回键值对应的标记 未匹配到返回null
	 * @call {@linkplain DictUtils this}
	 *       {@linkplain DictUtils#findLabelByTypeAndKey(String, Integer)
	 *       findLabelByTypeAndKey}
	 * @version 1.0
	 * @param type
	 * @param key
	 * @return
	 */
	public static String findLabelByTypeAndKey(String type, Integer keyInt) {
		Long time = new Date().getTime();
		String key = String.valueOf(keyInt);
		JSONObject jsonObject = findDicByType(type);
		if (jsonObject.containsKey("keys")) {
			JSONArray keys = jsonObject.getJSONArray("keys");
			for (int i = 0; i < keys.size(); i++) {
				JSONObject keyObj = keys.getJSONObject(i);
				if (key.equals(keyObj.getString("key"))) {
					//logger.info("dict utils —— get this type needs：" + (new Date().getTime() - time) + "ms");
					//logger.info("dict utils —— result:" + keyObj);
					return keyObj.getString("label");
				}
			}
		} else {
			JSONArray dics = jsonObject.getJSONArray("dics");
			for (int i = 0; i < dics.size(); i++) {
				JSONObject dic = dics.getJSONObject(i);
				String prefix = dic.getString("value");
				JSONArray keys = dic.getJSONArray("keys");
				for (int j = 0; j < keys.size(); j++) {
					JSONObject keyObj = keys.getJSONObject(j);
					if (key.equals(prefix + keyObj.getString("key"))) {
						//logger.info("dict utils —— get this type needs：" + (new Date().getTime() - time) + "ms");
						//logger.info("dict utils —— result:" + keyObj);
						return keyObj.getString("label");
					}
				}
			}
		}
		logger.info("dict utils —— get this type needs：" + (new Date().getTime() - time) + "ms");
		logger.info("dict utils —— result: IS NULL");
		return null;
	}

	/**
	 * get label by type and key.
	 * 
	 * @param type
	 * @param label
	 * @return
	 */
	@Deprecated
	public static Integer findKeyByTypeAndLabel(String type, String label) {
		Long time = new Date().getTime();
		JSONObject jsonObject = findDicByType(type);
		if (jsonObject.containsKey("keys")) {
			JSONArray keys = jsonObject.getJSONArray("keys");
			for (int i = 0; i < keys.size(); i++) {
				JSONObject keyObj = keys.getJSONObject(i);
				if (label.equals(keyObj.getString("label"))) {
					//logger.info("dict utils —— get this type needs：" + (new Date().getTime() - time) + "ms");
					//logger.info("dict utils —— result:" + keyObj);
					return keyObj.getInteger("key");
				}
			}
		} else {
			JSONArray dics = jsonObject.getJSONArray("dics");
			for (int i = 0; i < dics.size(); i++) {
				JSONObject dic = dics.getJSONObject(i);
				JSONArray keys = dic.getJSONArray("keys");
				for (int j = 0; j < keys.size(); j++) {
					JSONObject keyObj = keys.getJSONObject(j);
					if (label.equals(keyObj.getString("label"))) {
						//logger.info("dict utils —— get this type needs：" + (new Date().getTime() - time) + "ms");
						//logger.info("dict utils —— result:" + keyObj);
						return keyObj.getInteger("key");
					}
				}
			}
		}
		logger.info("dict utils —— get this type needs：" + (new Date().getTime() - time) + "ms");
		logger.info("dict utils —— result: IS NULL");
		return null;
	}

	/**
	 * get all list labels by type
	 * 
	 * @description 传入数据种类，获得所有标记内容的列表
	 * @call {@linkplain DictUtils this}
	 *       {@linkplain DictUtils#findLabelsByTypeAndKey(String, String)
	 *       findLabelsByTypeAndKey}
	 * @version 1.0
	 */
	public static List<String> findLabelsByType(String type) {
		return findLabelsByTypeAndKey(type, null);
	}

	/**
	 * get list labels by type and keys array.
	 * 
	 * @description 传入数据种类与一组键值，获得对应标记内容的列表并返回 若传入键值为空，则返回所有的标记内容
	 * @call {@linkplain DictUtils this}
	 *       {@linkplain DictUtils#findDicByType(String) findDicByType}
	 * @version 1.0
	 * @param type
	 * @param keyArray
	 * @return
	 */
	public static List<String> findLabelsByTypeAndKey(String type, String keyArray) {
		Long time = new Date().getTime();
		List<String> labels = Lists.newArrayList();
		JSONObject jsonObject = findDicByType(type);
		if (jsonObject.containsKey("keys")) {
			JSONArray keys = jsonObject.getJSONArray("keys");
			for (int i = 0; i < keys.size(); i++) {
				JSONObject keyObj = keys.getJSONObject(i);
				if (keyArray == null) {
					labels.add(keyObj.getString("label"));
				}
				if (keyArray != null && keyArray.contains(keyObj.getString("key"))) {
					//logger.info("dict utils —— get this type needs：" + (new Date().getTime() - time) + "ms");
					//logger.info("dict utils —— result:" + keyObj);
					labels.add(keyObj.getString("label"));
				}
			}
		} else {
			JSONArray dics = jsonObject.getJSONArray("dics");
			for (int i = 0; i < dics.size(); i++) {
				JSONObject dic = dics.getJSONObject(i);
				String prefix = dic.getString("value");
				JSONArray keys = dic.getJSONArray("keys");
				for (int j = 0; j < keys.size(); j++) {
					JSONObject keyObj = keys.getJSONObject(j);
					if (keyArray == null) {
						labels.add(keyObj.getString("label"));
					}
					if (keyArray != null && keyArray.contains(prefix + keyObj.getString("key"))) {
						//logger.info("dict utils —— get this type needs：" + (new Date().getTime() - time) + "ms");
						//logger.info("dict utils —— result:" + keyObj);
						labels.add(keyObj.getString("label"));
					}
				}
			}
		}
		logger.info("dict utils —— get this type needs：" + (new Date().getTime() - time) + "ms");
		logger.info("dict utils —— result: IS NULL");
		return labels;
	}

	/**
	 * get all list labels by type
	 */
	@Deprecated
	public static <T> List<T> findKeysByType(String type) {
		return findKeysByTypeAndLabel(type, null);
	}

	/**
	 * get keys by type and label array.
	 * 
	 * @param type
	 * @param keyArray
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public static <T> List<T> findKeysByTypeAndLabel(String type, String labelArray) {
		Long time = new Date().getTime();
		List<T> labels = Lists.newArrayList();
		JSONObject jsonObject = findDicByType(type);
		if (jsonObject.containsKey("keys")) {
			JSONArray keys = jsonObject.getJSONArray("keys");
			for (int i = 0; i < keys.size(); i++) {
				JSONObject keyObj = keys.getJSONObject(i);
				if (labelArray == null) {
					labels.add((T) keyObj.get("key"));
				}
				if (labelArray != null && labelArray.contains(keyObj.getString("label"))) {
					//logger.info("dict utils —— get this type needs：" + (new Date().getTime() - time) + "ms");
					//logger.info("dict utils —— result:" + keyObj);
					labels.add((T) keyObj.get("key"));
				}
			}
		} else {
			JSONArray dics = jsonObject.getJSONArray("dics");
			for (int i = 0; i < dics.size(); i++) {
				JSONObject dic = dics.getJSONObject(i);
				JSONArray keys = dic.getJSONArray("keys");
				for (int j = 0; j < keys.size(); j++) {
					JSONObject keyObj = keys.getJSONObject(j);
					if (labelArray == null) {
						labels.add((T) keyObj.get("key"));
					}
					if (labelArray != null && labelArray.contains(keyObj.getString("label"))) {
						//logger.info("dict utils —— get this type needs：" + (new Date().getTime() - time) + "ms");
						//logger.info("dict utils —— result:" + keyObj);
						labels.add((T) keyObj.get("key"));
					}
				}
			}
		}
		logger.info("dict utils —— get this type needs：" + (new Date().getTime() - time) + "ms");
		logger.info("dict utils —— result: IS NULL");
		return labels;
	}

	/**
	 * test main function.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new DictUtils("exam.xml");
		List<String> keys = DictUtils.findKeysByType("question.type");

		System.out.println(keys);
	}

	/**
	 * parse element's keys.
	 * 
	 * @description 从元素中解析出键值，并以json数组格式返回
	 * @version 1.0
	 * @param ele
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private JSONArray parseKey(Element ele) {
		JSONArray keys = new JSONArray();

		Iterator iter = ele.elementIterator("key");
		while (iter.hasNext()) {
			JSONObject key = new JSONObject();

			Element keyElement = (Element) iter.next();
			key.put("key", keyElement.attributeValue("value"));
			key.put("label", keyElement.attributeValue("label"));

			keys.add(key);
		}

		return keys;
	}

	/**
	 * parse dic element.
	 * 
	 * @description 从元素中解析出种类、名字和键值组，将数据类型与分组标志位添加到索引json数组后，以json数组格式返回
	 * @call {@linkplain DictUtils this}{@linkplain DictUtils#parseKey(Element)
	 *       parseKey}
	 * @version 1.0
	 * @param dicElement
	 * @return
	 */
	private JSONObject parseDic(Element dicElement) {
		JSONObject dic = new JSONObject();

		dic.put("type", dicElement.attributeValue("type"));
		dic.put("name", dicElement.attributeValue("name"));
		dic.put("keys", parseKey(dicElement));

		JSONObject index_item = new JSONObject();
		index_item.put("type", dicElement.attributeValue("type"));
		index_item.put("isGroup", false);
		indexs.add(index_item);

		return dic;
	}

	/**
	 * parse dic element.
	 * 
	 * @description 从元素中解析出种类、名字和键值组，并以json数组格式返回
	 * @call {@linkplain DictUtils this}{@linkplain DictUtils#parseKey(Element)
	 *       parseKey}
	 * @version 1.0
	 * @param dicElement
	 * @return
	 */
	private JSONObject parseGroupDic(Element dicElement) {
		JSONObject dic = new JSONObject();

		dic.put("value", dicElement.attributeValue("value"));
		dic.put("name", dicElement.attributeValue("name"));
		dic.put("keys", parseKey(dicElement));

		return dic;
	}

	/**
	 * parse dics elements.
	 * 
	 * @description 将列表格式的数据字典转化为json数组格式并返回
	 * @call {@linkplain DictUtils this}{@linkplain DictUtils#parseDic(Element)
	 *       parseDic}
	 * @version 1.0
	 * @param dicsElement
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private JSONArray parseDicts(List dicsElement) {
		JSONArray dics = new JSONArray();

		Iterator iter = dicsElement.iterator();

		while (iter.hasNext()) {
			JSONObject dic = new JSONObject();

			Element dicElement = (Element) iter.next();
			dic = parseDic(dicElement);

			dics.add(dic);
		}

		return dics;

	}

	/**
	 * parse group element.
	 * 
	 * @description 从元素中解析出一组种类、名字和键值组的信息，将数据类型与分组标志位添加到索引json数组后，以json数组格式返回
	 * @call {@linkplain DictUtils this}
	 *       {@linkplain DictUtils#parseGroupDic(Element) parseGroupDic}
	 * @version 1.0
	 * @param groupElement
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private JSONObject parseGroup(Element groupElement) {
		JSONObject group = new JSONObject();
		JSONArray dics = new JSONArray();

		Iterator iter = groupElement.elementIterator();

		while (iter.hasNext()) {
			JSONObject dic = new JSONObject();

			Element dicElement = (Element) iter.next();
			dic = parseGroupDic(dicElement);

			dics.add(dic);
		}

		group.put("type", groupElement.attributeValue("type"));
		group.put("name", groupElement.attributeValue("name"));
		group.put("dics", dics);

		JSONObject index_item = new JSONObject();
		index_item.put("type", groupElement.attributeValue("type"));
		index_item.put("isGroup", true);
		indexs.add(index_item);

		return group;
	}

	/**
	 * parse groups element.
	 * 
	 * @description 从列表中解析出多组种类、名字和键值组的信息，并以json数组格式返回
	 * @call {@linkplain DictUtils this}
	 *       {@linkplain DictUtils#parseGroup(Element) parseGroup}
	 * @version 1.0
	 * @param groupsElement
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private JSONArray parseGroups(List groupsElement) {
		JSONArray groups = new JSONArray();

		Iterator iter = groupsElement.iterator();

		while (iter.hasNext()) {
			JSONObject group = new JSONObject();

			Element groupElement = (Element) iter.next();
			group = parseGroup(groupElement);

			groups.add(group);
		}

		return groups;
	}

}
