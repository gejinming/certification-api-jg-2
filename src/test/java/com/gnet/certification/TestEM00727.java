package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Response;
import jodd.http.HttpResponse;
import org.docx4j.fonts.GlyphCheck;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.toc.TocException;
import org.docx4j.toc.TocGenerator;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author YHL
 * @since 2017/10/27 上午11:47
 */
public class TestEM00727 extends CertificationAddBeforeJUnit {

    @Test
    public void testExecute() throws Exception {

        System.out.println("test EM00727");

        Map<String, Object> data = new HashMap<>();
        data.put("versionId", "300514");
        HttpResponse httpResponse = super.api("EM00727", token, data).send();
        this.isOk(httpResponse);
        Response response = this.parse(httpResponse);
        JSONObject object = (JSONObject) response.getData();
        System.out.println(object);


    }

    @Test
    public void test() {

        String inputFilePath = "/Users/yuhailun/DeskTop/test/test.xml";
        String outputFilePath = "/Users/yuhailun/DeskTop/test/dest.docx";


        WordprocessingMLPackage wordMLPackage = null;
        try {
            wordMLPackage = WordprocessingMLPackage.load(new File(inputFilePath));
        } catch (Docx4JException e) {
            e.printStackTrace();
        }

        TocGenerator tocGenerator = null;
        try {
            tocGenerator = new TocGenerator(wordMLPackage);
        } catch (TocException e) {
            e.printStackTrace();
        }

        try {
            tocGenerator.generateToc( 0,    "TOC \\o \"1-3\" \\h \\z \\u ", true);
        } catch (TocException e) {
            e.printStackTrace();
        }

        try {
            wordMLPackage.save(new File(outputFilePath));
        } catch (Docx4JException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckFont() {
        try {
            PhysicalFonts.discoverPhysicalFonts();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PhysicalFont physicalFont = PhysicalFonts.get("Times New Roman");

        if (physicalFont==null) {
            System.out.println("missing TNR!");
        } else {
            try {
                System.out.println(GlyphCheck.hasChar(physicalFont, 'ě'));
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
