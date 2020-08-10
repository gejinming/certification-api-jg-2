package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import jodd.http.HttpResponse;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Yuhailun 1258110550@qq.com
 * @Desciption
 * @Date Created in 2017/9/12 0012 20:56:57
 */
public class TestEM00723 extends CertificationAddBeforeJUnit {

    @Test
    public void testExecute() throws Exception {

        System.out.println("test EM00723");

        Map<String, Object> data = new HashMap<>();
        data.put("courseOutlineId", "471730");
        HttpResponse httpResponse = super.api("EM00723", token, data).send();
        this.isOk(httpResponse);
        Response response = this.parse(httpResponse);
        JSONObject object = (JSONObject) response.getData();
        System.out.println(object);

    }

    @Test
    public void test() {

        String html = "<p style=\"text-align: left; text-indent: 29px; line-height: 150%;\">\n" +
                "    <span style=\"font-size: 14px;line-height: 150%;font-family: 宋体\">机械控制工程是</span>\n" +
                "    <span style=\"font-size:14px;line-height:150%;font-family: 宋体;color:#333333;background:white\">以机械运动作为主要受控对象，研究工程中广为应用的经典控制理论中控制系统分析与综合的基本方法，将其用在有机械系统控制</span>\n" +
                "    <span style=\"font-size: 14px;line-height: 150%;font-family: 宋体\">要求的各行业中</span>\n" +
                "    <span style=\"font-size:14px;line-height:150%;font-family: 宋体;color:#333333;background:white\">。</span>\n" +
                "    <span style=\"font-size:14px;line-height:150%;font-family:宋体;color:#333333\">本课程是</span>\n" +
                "    <span style=\"font-size: 14px;line-height: 150%;font-family: 宋体\">为机械设计制造及其自动化专业大三学生开设的专业选修课，为机械工程中广义系统的动力学问题分析，机电传动与测试技术等课程奠定基础；为毕业后从事机电一体化行业的工作的学生提供机电系统设计和机电系统控制的专业知识。本课程主要介绍以拉氏变换为基础的经典控制理论中的主要内容，包括传递函数，方块图，自动控制系统的时域和频域响应，重点掌握控制系统的稳定性、准确性和快速性的概念和性能指标。通过本课程教学，学生应达到下列教学目标：</span>\n" +
                "    <span style=\"font-size: 14px;line-height: 150%\">①</span>\n" +
                "    <span style=\"font-size: 14px;line-height: 150%;font-family: 宋体\">熟悉常用典型信号和典型环节的传递函数；</span>\n" +
                "    <span style=\"font-size: 14px;line-height: 150%\"> ②</span>\n" +
                "    <span style=\"font-size: 14px;line-height: 150%;font-family: 宋体\">掌握控制系统的时域和频域响应；</span>\n" +
                "    <span style=\"font-size: 14px;line-height: 150%\">③</span>\n" +
                "    <span style=\"font-size: 14px;line-height: 150%;font-family: 宋体\">掌握控制系统的稳定性、准确性和快速性的分析方法和性能指标；</span>\n" +
                "    <span style=\"font-size: 14px;line-height: 150%\">④</span>\n" +
                "    <span style=\"font-size: 14px;line-height: 150%;font-family: 宋体\">具有应用机械控制工程这门课的基本方法对后续的课程的学习；</span>\n" +
                "    <span style=\"font-size: 14px;line-height: 150%\">⑤</span>\n" +
                "    <span style=\"font-size: 14px;line-height: 150%;font-family: 宋体\">具有对简单机械控制系统的分析与设计的基本能力；</span>\n" +
                "    <span style=\"font-size: 14px;line-height: 150%\">⑥</span>\n" +
                "    <span style=\"font-size: 14px;line-height: 150%;font-family: 宋体\">具有设计、改造、革新一般生产机械控制系统的初步能力。</span>\n" +
                "</p>";

        WordprocessingMLPackage wordMLPackage = null;
        try {
            wordMLPackage = WordprocessingMLPackage.createPackage();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        final XHTMLImporterImpl xhtmlImporter = new XHTMLImporterImpl(wordMLPackage);
        MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
        List<Object> objectList = null;
        try {
            objectList = xhtmlImporter.convert(html, null);
        } catch (Docx4JException e) {
            e.printStackTrace();
        }
        mainDocumentPart.getContent().addAll(objectList);

        String xml = mainDocumentPart.getXML();
        System.out.println(xml);

    }

}
