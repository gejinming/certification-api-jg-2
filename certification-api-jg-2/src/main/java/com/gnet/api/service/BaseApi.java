package com.gnet.api.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.math.NumberUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.DateUtils;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class BaseApi {
	
	/** 默认页码 */
	public static final int DEFAULT_PAGE_NUMBER = 1;

	/** 默认每页记录数 */
	public static final int DEFAULT_PAGE_SIZE = 20;

	public static final String METHOD_BOTH = "BOTH";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";
	
	/**
	 * 参数转为String
	 * @param param
	 * @return
	 */
	public String paramsStringFilter(Object param) {
		String result = null;
		if (param != null) {
			result = param.toString().trim();
		}
		return result;
	}
	
	/**
	 * 参数转为Boolean
	 * @param param
	 * @return
	 */
	public Boolean paramsBooleanFilter(Object param) {
		String result = paramsStringFilter(param);
		if (!"true".equalsIgnoreCase(result) && !"false".equalsIgnoreCase(result)) {
			return null;
		}
		return Boolean.parseBoolean(result);
	}
	
	/**
	 * 参数转化为Double
	 * @param param
	 * @return
	 */
	public Double paramsDoubleFilter(Object param) {
		String result = paramsStringFilter(param);
		if (!NumberUtils.isNumber(result)) {
			return null;
		}
		return Double.parseDouble(result);
	}

	/**
	 * 参数转化为Integer
	 * @param param
	 * @return
	 */
	public Integer paramsIntegerFilter(Object param) {
		String result = paramsStringFilter(param);
		if (!NumberUtils.isDigits(result)) {
			return null;
		}
		return Integer.parseInt(result);
	}
	
	/**
	 * 参数转化为Long
	 * @param param
	 * @return
	 */
	public Long paramsLongFilter(Object param) {
		String result = paramsStringFilter(param);
		if (!NumberUtils.isNumber(result)) {
			return null;
		}
		return Long.parseLong(result);
	}
	
	/**
	 * 参数转化为Date
	 * @param param
	 * @return
	 */
	public Date paramsDateFilter(Object param) {
		return param == null ? null : DateUtils.parseDate(param);
	}
	
	/**
	 * 参数转换成BigDecimal
	 * @param param
	 * @return
	 */
	public BigDecimal paramsBigDecimalFilter(Object param) {
		String result = paramsStringFilter(param);
		if (!NumberUtils.isNumber(result)) {
			return null;
		}
		return new BigDecimal(result);
	}
	
	
	/**
	 * 参数转化为Map
	 * @param param
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map paramsMapFilter(Object param) {
		Map map = new HashMap();
		if(param == null) {
			return new HashMap();
		}
		if(param.toString().length() <= 2) {
			return new HashMap();
		}
		String mspStr = param.toString().substring(1, param.toString().length() - 1);
		StringTokenizer items = null;
		for(StringTokenizer entrys = new StringTokenizer(mspStr, ",");entrys.hasMoreTokens();   
		map.put(items.nextToken().trim(), items.hasMoreTokens() ? ((Object) (items.nextToken())) : null))  
			items = new StringTokenizer(entrys.nextToken(), ":");  
		return map;

	}
	
	/**
	 * 参数转为JSONObject
	 * @param param
	 * @return
	 */
	public JSONObject paramsJSONObjectFilter(Object param) {
		if(param == null) {
			return new JSONObject();
		}
		JSONObject object = new JSONObject();
		try{
			object = JSONObject.parseObject(param.toString());
		}catch (Exception e) {
			return new JSONObject();
		}
		return object;
	}
	
	/**
	 * 参数转为JSONArray
	 * @param param
	 * @return
	 */
	public JSONArray paramsJSONArrayFilter(Object param) {
		if(param == null) {
			return new JSONArray();
		}
		JSONArray array = new JSONArray();
		try{
			array = JSONArray.parseArray(param.toString());
		}catch (Exception e) {
			return new JSONArray();
		}
		return array;
	}
	
	/**
	 * 参数转为List<T>
	 * @param param
	 * @param clazz
	 * @return
	 */
	public <T> List<T> paramsJSONArrayFilter(Object param, Class<T> clazz) {
		if(param == null) {
			return new ArrayList<T>();
		}
		List<T> array = new ArrayList<T>();
		try{
			array = JSONArray.parseArray(param.toString(), clazz);
		}catch (Exception e) {
			return new ArrayList<T>();
		}
		return array;
	}
	
	/**
	 * 获取数量
	 * 注意：sql中必须as num
	 * @param sql
	 * @param paras
	 * @return
	 */
	public int getCount(String sql, Object ...paras) {
		Record record = Db.findFirst(sql, paras);
		return paramsIntegerFilter(record.getColumns().get("num"));
	}
	
	/**
	 * 通过注册的service Name获取容器中的类
	 * @param service name
	 * @return
	 */
	public Object getBean(String name) {
		return SpringContextHolder.getBean(name);
	}
	
	
	/**
	 * 获取下一个数据库的序列
	 * @return
	 */
	public Long getNextValue() {
		IdGenerate idGenerate = (IdGenerate) getBean("idGenerate");
		return idGenerate.getNextValue();
	}
	
	
	/**
	 * 限制请求方式
	 * @param response
	 * @param header
	 * @param method
	 * @param type
	 * @return
	 */
	public Response methodPostFilter(Response response, ResponseHeader header, String method, String type) {
		if(method.equals(type)){
			response.setHeader(header);
			return response;
		}
		return renderFAIL("1001", response, header);
	}
	
	/**
	 * 返回对的Response
	 * @param data
	 * @param response
	 * @param header
	 * @return
	 */
	public Response renderSUC(Object data, Response response, ResponseHeader header) {
		header.setSuccflag(Response.SUCCESS);
		response.setHeader(header);
		response.setData(data);
		return response;
	}
	
	/**
	 * 返回错误的Response
	 * @param errorCode
	 * @param response
	 * @param header
	 * @return
	 */
	public Response renderFAIL(String errorCode, Response response, ResponseHeader header) {
		return renderFAIL(errorCode, response, header, null);
	}
	
	
	/**
	 * 返回错误的Response附带错误的详细信息
	 * @param errorCode
	 * @param response
	 * @param header
	 * @param errorMessage
	 * @return
	 */
	public Response renderFAIL(String errorCode, Response response, ResponseHeader header, Object errorMessage){
		header.setSuccflag(Response.FAIL);
		header.setErrorcode(errorCode);
		header.setErrormessage(errorMessage);
		response.setHeader(header);
		return response;
	}
	
	
	/**
	 * 返回文件Response
	 * @param file
	 * @param response
	 * @param header
	 * @return
	 */
	public Response renderFILE(File file, Response response, ResponseHeader header) {
		header.setSuccflag(Response.FILE);
		response.setHeader(header);
		response.setData(file);
		return response;
	}

	/**
	 * 返回文件Response
	 * @param file
	 * @param response
	 * @param header
	 * @return
	 */
	public Response renderWordFile(File file, Response response, ResponseHeader header) {
		header.setSuccflag(Response.WORD_FILE);
		response.setHeader(header);
		response.setData(file);
		return response;
	}
	
}
