package com.gnet.certification;

import com.gnet.FileInfo;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class EM01198Test extends CertificationAddBeforeJUnit {
    @Test
    public void testExecute() {
        //Map<String, Object> param = request.getData();
        String docxPath = "E:/毕设.docx";
        Object fileInfoObject = docxPath;
        FileInfo fileInfo = (FileInfo) fileInfoObject;
        String url = fileInfo.getTempUrl();
        File file = new File(url);

        try {
            InputStream inputStream = new FileInputStream(file);


            try {
                XWPFDocument doc = new XWPFDocument(inputStream);
                List<XWPFParagraph> paragraphs = doc.getParagraphs();
                for (int i=0;i<paragraphs.size();i++){
                    XWPFParagraph paragraph = paragraphs.get(i);
                    String text = paragraph.getText();
                    List<XWPFRun> runs = paragraph.getRuns();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}