package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import jodd.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YHL
 * @since 2017/10/24 上午9:39
 */
public class TestEM00765 extends CertificationAddBeforeJUnit {


    @Test
    public void testExecute() {

        Map<String, Object> data = new HashMap<>();

        data.put("originFilePath", "/Users/yuhailun/GitRepo/certification-api/target/certification-api/word/export/1e49417f-67b1-4eef-b8cb-b0d3a78d4958.xml");
        data.put("exportFileName", "课程目标评价表-汽车服务系统规划与数学求解");
        HttpResponse httpResponse = super.api("EM00765", token, data).send();


    }

}
