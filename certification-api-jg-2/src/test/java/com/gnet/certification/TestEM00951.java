package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import java.util.HashMap;
import java.util.Map;
import jodd.http.HttpResponse;
import org.junit.Test;

/**
 * 文件管理-创建文件夹测试
 *
 * @author yuhailun
 * @date 2018/01/29
 * @description
 **/
public class TestEM00951 extends CertificationAddBeforeJUnit {

    @Test
    public void testExecute() {

        Map<String, Object> data = new HashMap<>();
        data.put("majorId", 471384);
        data.put("parentId", 478327);
        data.put("directoryName", "测试2");
        HttpResponse httpResponse = super.api("EM00951", token, data).send();
        this.isOk(httpResponse);
        Response response = this.parse(httpResponse);
        JSONObject object = (JSONObject) response.getData();
        Assert.assertNotNull(object);
        System.out.println(object);

    }

}
