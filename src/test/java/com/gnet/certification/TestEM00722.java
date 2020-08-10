package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestEM00722 extends CertificationAddBeforeJUnit {

    @Test
    public void testExecute() {

        Map<String, Object> data = new HashMap<>();
        data = new HashMap<>();
        data.put("courseOutlineId", 287491);
        System.out.println("test EM00722");
        HttpResponse httpResponse = super.api("EM00722", token, data).send();

    }

}
