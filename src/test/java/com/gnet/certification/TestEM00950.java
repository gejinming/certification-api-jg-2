package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import java.util.HashMap;
import java.util.Map;
import jodd.http.HttpResponse;
import org.junit.Test;

/**
 * 文件管理-查询文件列表
 *
 * @author yuhailun
 * @date 2018/01/29
 * @description
 **/
public class TestEM00950 extends CertificationAddBeforeJUnit {

    @Test
    public void testExecute() {

        Map<String, Object> data = new HashMap<>();
        data.put("majorId", 471384);
        HttpResponse httpResponse = super.api("EM00950", token, data).send();
        this.isOk(httpResponse);
        Response response = this.parse(httpResponse);
        JSONObject object = (JSONObject) response.getData();
        Assert.assertNotNull(object);
        System.out.println(object);

    }

}
