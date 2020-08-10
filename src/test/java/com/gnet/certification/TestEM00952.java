package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import jodd.http.HttpResponse;
import org.junit.Test;

/**
 * 文件上传测试
 *
 * @author yuhailun
 * @date 2018/01/30
 * @description
 **/
public class TestEM00952 extends CertificationAddBeforeJUnit {

    @Test
    public void testExecute() {

        Map<String, Object> data = new HashMap<>();
        data.put("fileType", "file");
        File file = new File("/Users/yuhailun/desktop/test.txt");
        HttpResponse httpResponse = super.api("EM00952", token, data).form("upfile", file).send();
        this.isOk(httpResponse);
        Response response = this.parse(httpResponse);
        JSONObject object = (JSONObject) response.getData();
        Assert.assertNotNull(object);
        System.out.println(object);

    }

}
