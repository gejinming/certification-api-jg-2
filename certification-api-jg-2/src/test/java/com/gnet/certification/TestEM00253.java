package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import jodd.http.HttpResponse;

public class TestEM00253 extends CertificationAddBeforeJUnit {

	/**
	 * 检查正常的数据是否成功输出
	 */
	@Test
	public void testExecute() {
		Map<String, Object> data = new HashMap<>();
		data.put("majorId", 165879);
		data.put("id", 165878);
		data.put("code", "gwdx-xxxy-jsj1112");
		data.put("name", "高网计算机111班22");
		data.put("description", "高网大学信息学院计算机专业111班行政班班级描述内容2016年10月24日20:55:12");
		data.put("classLeader", "王老师");
		data.put("remark", "高网大学信息学院计算机专业111班行政班备注内容2016年10月24日20:55:33");
		data.put("grade", 2011);
		HttpResponse httpResponse = super.api("EM00253", token, data).send();
		this.isOk(httpResponse);
		
		Response response = this.parse(httpResponse);
		JSONObject object = (JSONObject) response.getData();
		ResponseHeader header = response.getHeader();
		if(object != null) {
			Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
			System.out.println(object);
		} else {
			System.out.println("返回错误码：" + header.getErrorcode());
		}
	}
	
}
