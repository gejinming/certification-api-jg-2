package com.gnet.certification;

import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.model.fields.FieldUpdater;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class TestEM00728 {

    @Test
    public void test() {
        String inputfilepath = "/Users/yuhailun/Desktop/Test/test.docx";

        String outputfilepath = "/Users/yuhailun/Desktop/Test/test.pdf";

        try {
            // First, load Flat OPC Package from file
            OutputStream outputStream = new FileOutputStream(new File(outputfilepath));
//            InputStream inputStream = new FileInputStream(new File(inputfilepath));
            WordprocessingMLPackage wmlPackage = Docx4J.load(new File(inputfilepath));
            Docx4J.toPDF(wmlPackage, outputStream);
//            Docx4J.save(wmlPackage, new File(outputfilepath), Docx4J.FLAG_SAVE_ZIP_FILE);
//            System.out.println("Saved: " + outputfilepath);

//            XWPFDocument document = new XWPFDocument(inputStream);
            //HWPFDocument document = new HWPFDocument(inStream);
//            PdfOptions options = PdfOptions.create();
//            ExtITextFontRegistry fontProvider=ExtITextFontRegistry.getRegistry();
//            options.fontProvider(fontProvider);
//            PdfConverter.getInstance().convert(document, out, options);

        } catch (Exception exc) {
            exc.printStackTrace();
//            throw new RuntimeException(exc);
        }
    }

    @Test
    public void test2() {
//        PDDocument doc = null;
//        try {
//            doc = PDDocument.load(new File("/Users/yuhailun/Desktop/Test/test.pdf"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        int count = doc.getNumberOfPages();
//        out.println("count:" + count);
//        XWPFDocument docx = null;
//        try {
//            docx = new XWPFDocument(POIXMLDocument.openPackage("/Users/yuhailun/Desktop/Test/test.docx"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        int pages = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();//总页数
//        System.out.println ("pages=" + pages);
    }

    @Test
    public void test3() {

        String inputFilePath = "/Users/yuhailun/Desktop/Test/test.docx";

        String outputFilePath = "/Users/yuhailun/Desktop/Test/test.pdf";

        try {

            String regex = ".*(calibri|camb|cour|arial|times|comic|georgia|impact|LSANS|pala|tahoma|trebuc|verdana|symbol|webdings|wingding).*";
            PhysicalFonts.setRegex(regex);
            OutputStream outputStream = new FileOutputStream(new File(outputFilePath));
            WordprocessingMLPackage wordprocessingMLPackage = Docx4J.load(new File(inputFilePath));
            FieldUpdater updater = new FieldUpdater(wordprocessingMLPackage);

            try {
                updater.update(true);
            } catch (Docx4JException e) {
                e.printStackTrace();
            }

            Mapper fontMapper = new IdentityPlusMapper();
            wordprocessingMLPackage.setFontMapper(fontMapper);

            fontMapper.put("宋体", PhysicalFonts.get("SimSun"));
            fontMapper.put("italic", PhysicalFonts.get("italic"));
            fontMapper.put("Wingdings", PhysicalFonts.get("Wingdings"));
            fontMapper.put("Times-Roman", PhysicalFonts.get("Times-Roman"));
            fontMapper.put("Calibri", PhysicalFonts.get("Calibri"));
            fontMapper.put("Times-Bold", PhysicalFonts.get("Times-Bold"));
            fontMapper.put("Symbol", PhysicalFonts.get("Symbol"));
            fontMapper.put("ZapfDingbats", PhysicalFonts.get("ZapfDingbats"));
            fontMapper.put("Times New Roman", PhysicalFonts.get("Times New Roman"));


            FOSettings foSettings = Docx4J.createFOSettings();
            foSettings.setWmlPackage(wordprocessingMLPackage);

            Docx4J.toFO(foSettings, outputStream, Docx4J.FLAG_EXPORT_PREFER_XSL);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
