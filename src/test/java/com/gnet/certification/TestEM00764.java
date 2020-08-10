package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YHL
 * @since 2017/10/22 下午7:15
 */
public class TestEM00764 extends CertificationAddBeforeJUnit {

    @Test
    public void testExecute() {

        Map<String, Object> data = new HashMap<>();

        data.put("id", 452904);
        HttpResponse httpResponse = super.api("EM00764", token, data).send();
        this.isOk(httpResponse);
        Response response = this.parse(httpResponse);
        JSONObject object = (JSONObject) response.getData();
        System.out.println(object);


    }

}
