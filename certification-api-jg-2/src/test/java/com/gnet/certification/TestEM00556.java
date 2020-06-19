package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

/**
 * 课程达成度报表生成状态查询测试用例
 * 
 * @author wct
 * @date 2016年7月12日
 */
public class TestEM00556 extends CertificationAddBeforeJUnit {
	
	/**
	 * 检查能否正常增加成绩组成元素明细和指标点的关联
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("grade", 2013);
		data.put("versionId", 10001L);
		
		HttpResponse httpResponse = super.api("EM00556", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
		if(object != null) {
			System.out.println(object);
		} else {
			System.out.println("返回错误码：" + header.getErrorcode());
		}
	}
}
