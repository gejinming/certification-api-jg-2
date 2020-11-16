package com.gnet.certification;

import com.gnet.FileInfo;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.jfinal.log.Logger;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @program: certification-api-jg-2
 * @description: 获取word内容
 * @author: Gjm
 * @create: 2020-07-01 12:02
 **/
@Transactional(readOnly = false)
@Service("EM01198")
public class EM01198 extends BaseApi implements IApi {
    private static final Logger logger = Logger.getLogger(EM01198.class);
    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> param = request.getData();

        Object fileInfoObject = param.get("fileInfo");
        FileInfo fileInfo = (FileInfo) fileInfoObject;
        String url = fileInfo.getTempUrl();
        File file = new File(url);

        try {
            InputStream inputStream = new FileInputStream(file);


            try {
                //读取文档
                XWPFDocument doc = new XWPFDocument(inputStream);
                //获取样式
                XWPFStyles styles = doc.getStyles();
                //获取页眉 页脚
                List<XWPFHeader> headerList = doc.getHeaderList();


                //获取段落
                List<XWPFParagraph> paragraphs = doc.getParagraphs();
                for (int i=0;i<paragraphs.size();i++){
                    String fontNames=null;
                    Integer fontSizes=null;
                    XWPFParagraph paragraph = paragraphs.get(i);

                    //获取每个段落的文本
                    String text = paragraph.getText();
                    logger.info("第"+i+"段内容："+text);
                    //获取大纲级别
                    String titleLvl = getTitleLvl(doc,paragraph);
                    logger.info("内容："+text+"大纲级别为："+titleLvl);
                    //获取格式
                    String styleID = paragraph.getStyleID();
                    logger.info("格式："+paragraph.getStyleID());
                    styles.getStyle(styleID).getCTStyle().getName().getVal();
                    // 获取段落内所有XWPFRun
                    List<XWPFRun> runs = paragraph.getRuns();
                    for (XWPFRun run: runs){

                        //获取字体
                        String fontFamily = run.getFontFamily();
                        logger.info("第"+i+"段内容字体："+fontFamily);
                        //获取字号
                        int fontSize = run.getFontSize();
                        logger.info("第"+i+"段内容字号："+fontSize);
                        String fontName = run.getFontName();
                        logger.info("第"+i+"段内容字体名称："+fontName);
                        if (fontNames==null || fontSizes==null){
                            fontNames=fontName;
                            fontSizes=fontSize;
                        }
                        if (fontName!=null){
                            if (!fontNames.equals(fontName)|| fontSizes!=fontSize){

                                String message="第"+i+"段内容字体格式不一致";
                                logger.error("第"+i+"段内容字体格式不一致");
                                break;
                            }
                        }

                    }
                }
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return renderSUC(null, response, header);
    }

  /**
    * Word中的大纲级别，可以通过getPPr().getOutlineLvl()直接提取，但需要注意，Word中段落级别，通过如下三种方式定义：
    *  1、直接对段落进行定义；
    *  2、对段落的样式进行定义；
    *  3、对段落样式的基础样式进行定义。
    *  因此，在通过“getPPr().getOutlineLvl()”提取时，需要依次在如上三处读取。
    * @param doc
    * @param para
    * @return
    */
  private static String getTitleLvl(XWPFDocument doc, XWPFParagraph para) {
             String titleLvl = "";
             try {
                     //判断该段落是否设置了大纲级别
                     if (para.getCTP().getPPr().getOutlineLvl() != null) {
                             // System.out.println("getCTP()");
             //              System.out.println(para.getParagraphText());
             //              System.out.println(para.getCTP().getPPr().getOutlineLvl().getVal());

                             return String.valueOf(para.getCTP().getPPr().getOutlineLvl().getVal());
                         }
                } catch (Exception e) {

                 }

             try {
                     //判断该段落的样式是否设置了大纲级别
                     if (doc.getStyles().getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl() != null) {

                             // System.out.println("getStyle");
             //              System.out.println(para.getParagraphText());
             //              System.out.println(doc.getStyles().getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl().getVal());

                             return String.valueOf(doc.getStyles().getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl().getVal());
                         }
                 } catch (Exception e) {

                 }

           try {
                    //判断该段落的样式的基础样式是否设置了大纲级别
                     if (doc.getStyles().getStyle(doc.getStyles().getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal())
                             .getCTStyle().getPPr().getOutlineLvl() != null) {
                            // System.out.println("getBasedOn");
            //              System.out.println(para.getParagraphText());
                             String styleName = doc.getStyles().getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal();
             //              System.out.println(doc.getStyles().getStyle(styleName).getCTStyle().getPPr().getOutlineLvl().getVal());

                             return String.valueOf(doc.getStyles().getStyle(styleName).getCTStyle().getPPr().getOutlineLvl().getVal());
                         }
                 } catch (Exception e) {

                }

            try {
                    if(para.getStyleID()!=null){
                            return para.getStyleID();
                         }
                 } catch (Exception e) {

                }

            return titleLvl;
        }


}
