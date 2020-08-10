package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YHL
 * @since 2017/10/31 上午9:50
 */
public class TestEM00729 extends CertificationAddBeforeJUnit {


    @Test
    public void testExecute() throws Exception {

        System.out.println("test EM00729");

        Map<String, Object> data = new HashMap<>();
        data.put("versionId", "300296");
        HttpResponse httpResponse = super.api("EM00729", token, data).send();
        this.isOk(httpResponse);
        Response response = this.parse(httpResponse);
        JSONObject object = (JSONObject) response.getData();
        System.out.println(object);

    }

}
