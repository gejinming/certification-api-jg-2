package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: yuhailun
 * @date: 2017/11/22
 * @description:
 **/
public class TestEM00810 extends CertificationAddBeforeJUnit {


    @Test
    public void testExecute() {
        Map<String, Object> data = new HashMap<>();
        data.put("courseId", 471395);

        HttpResponse httpResponse = super.api("EM00810", token, data).send();
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
