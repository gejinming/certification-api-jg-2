package com.gnet.utils;

import com.jfinal.kit.PathKit;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.*;
import java.util.Map;
import java.util.UUID;

/**
 * @author YHL
 * @since 2017/10/18 下午11:59
 */
public class WordTemplateKit {

    private static final Configuration WORD_CONFIGURATION = new Configuration(Configuration.VERSION_2_3_24);

    private static boolean IS_INIT = false;

    /**
     * 文件后缀名
     */
    public static final String FILE_SUFFIX_XML = ".xml";

    /**
     * 文件后缀名
     */
    public static final String FILE_SUFFIX_DOCX = ".docx";

    /**
     * 文件导出路径
     */
    private static final String EXPORT_FILE_PATH = "export";

    /**
     * word模版路径
     */
    private static final String WORD_TEMPLATE_PATH = "word";

    private WordTemplateKit() {
    }

    /**
     * 初始化FreeMarker 参数
     */
    public static void init() {
        if (!IS_INIT) {
            WORD_CONFIGURATION.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
            WORD_CONFIGURATION.setDefaultEncoding("UTF-8");
        }
        IS_INIT = true;
    }

    /**
     * 得到模版路径
     *
     * @return String
     */
    public static String getWordTemplateDirPath() {
        return PathKit.getRootClassPath() + File.separator + WORD_TEMPLATE_PATH;
    }

    /**
     * 设置模版路径
     */
    public static boolean setTemplateDirPath() {
        return setTemplateDirPath(getWordTemplateDirPath());
    }

    /**
     * 设置模版路径
     *
     * @param templatePath 模版路径
     */
    public static boolean setTemplateDirPath(String templatePath) {
        try {
            WORD_CONFIGURATION.setDirectoryForTemplateLoading(new File(templatePath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 得到文件输出路径
     *
     * @return String
     */
    public static String getExportPath() {
        return PathKit.getWebRootPath() + File.separator + WORD_TEMPLATE_PATH + File.separator +  EXPORT_FILE_PATH;
    }

    /**
     * 得到输出文件名称getExportPath
     *
     * @return
     */
    public static String getExportFileName() {
        return getExportFileName(getExportPath());
    }

    /**
     * 得到输出文件名称
     *
     * @param exportPath 导出文件路径
     * @return
     */
    public static String getExportFileName(String exportPath) {
        return getExportFileName(exportPath, UUID.randomUUID().toString(), FILE_SUFFIX_XML);
    }

    /**
     * 得到输出文件名称
     *
     * @return
     */
    public static String getExportFileName(String fileName, String suffix) {
        return getExportFileName(getExportPath(), fileName, suffix);
    }

    /**
     * 得到输出文件名称
     *
     * @param exportPath 导出文件路径
     * @return
     */
    public static String getExportFileName(String exportPath, String fileName, String suffix) {
        return getExportFileName(exportPath, null, fileName, suffix);
    }

    /**
     * 得到输出文件名称
     *
     * @param exportPath 导出文件路径
     * @return
     */
    public static String getExportFileName(String exportPath, String specifiedPath, String fileName, String suffix) {
        StringBuilder stringBuilder = new StringBuilder(exportPath);

        if (StringUtils.isNotBlank(specifiedPath)) {
            stringBuilder.append(File.separator);
            stringBuilder.append(specifiedPath);
        }

        if (StringUtils.isNotBlank(fileName)) {
            stringBuilder.append(File.separator);
            stringBuilder.append(fileName);
            stringBuilder.append(suffix);
        }

        return stringBuilder.toString();
    }

    /**
     * 生成导出文件路径
     *
     * @return
     */
    public static boolean generateExportPath() {
        return generateExportPath(getExportPath());
    }

    /**
     * 生成导出文件路径
     *
     * @param exportPath 导出文件路径
     * @return
     */
    public static boolean generateExportPath(String exportPath) {
        File exportFilePath = new File(exportPath);
        if (!exportFilePath.exists()) {
            if (!exportFilePath.mkdirs()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 生成导出文件
     *
     * @return
     */
    public static File generateExportFile() {
        return generateExportFile(getExportFileName());
    }

    /**
     * 生成导出文件
     *
     * @param exportFileName 导出文件名称
     * @return
     */
    public static File generateExportFile(String exportFileName) {
        File exportFile = new File(exportFileName);

        if (exportFile.exists()) {
            exportFile.delete();
        }

        File parentPathFile = new File(exportFile.getParent());
        if (!parentPathFile.exists()) {
            if (!parentPathFile.mkdirs()) {
                return null;
            }
        }

        try {
            boolean result = exportFile.createNewFile();
            if (!result) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return exportFile;
    }

    /**
     * 预处理 1. 初始化模版参数 2.设置模版路径 3.生成导出文件路径
     *
     * @return
     */
    public static boolean preProcess() {
        init();
        return setTemplateDirPath() && generateExportPath();
    }

    /**
     * @param templateFileName 模版文件名称
     * @param outputFile       输出文件
     * @param dataMap          数据
     */
    public static boolean process(String templateFileName, File outputFile, Map dataMap) {

        Writer outWriter = null;
        try {
            Template template = WORD_CONFIGURATION.getTemplate(templateFileName);
            outWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
            template.process(dataMap, outWriter);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (outWriter != null) {
                    outWriter.flush();
                    outWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     *
     * @param xmlFile
     * @return
     */
    public static File xmlFileToDocxFile(File xmlFile, String docxFileName) {

        File docxFile = generateExportFile(docxFileName);

        if (docxFile == null) {
            return null;
        }

        WordprocessingMLPackage wmlPackage;
        try {
            wmlPackage = Docx4J.load(xmlFile);
            Docx4J.save(wmlPackage, docxFile, Docx4J.FLAG_SAVE_ZIP_FILE);
        } catch (Docx4JException e) {
            e.printStackTrace();
            return null;
        }

        return docxFile;
    }

}
