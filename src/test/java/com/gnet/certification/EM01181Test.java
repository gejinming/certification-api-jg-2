package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class EM01181Test  extends CertificationAddBeforeJUnit{
    @Test
    public void testExecute() {
        Map<String, Object> data = new HashMap<>();
        data.put("typeValue","D");
        data.put("typeName", "张三");
        data.put("planId","855");
        HttpResponse httpResponse = super.api("EM01181", token, data).send();
        this.isOk(httpResponse);

        Response response = this.parse(httpResponse);
        JSONObject object = (JSONObject) response.getData();
        Assert.assertEquals(Boolean.TRUE, object.getBooleanValue("isSuccess"));
        System.out.println(object);

    }

}