package com.gnet.service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.utils.FileUtils;
import com.google.common.collect.Lists;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;

import java.io.*;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 专业认证文件管理系统
 *
 * @author yuhailun
 * @date 2018/01/29
 * @description
 **/
@Component
public class CcFileService {

    private final static Logger logger = Logger.getLogger(CcFileService.class);

    private final static String blackList = "`\\/?\"<>|*";

    /**
     * 初始化文件系统的根目录
     *
     * @param majorId 专业编号
     * @return 文件根目录路径
     */
    public String initRootPath(Long majorId) {

        if (majorId == null) {
            return null;
        }

        String fileSystemPath = ConfigUtils.getStr("global", "fileSystemPath");

        if (StringUtils.isBlank(fileSystemPath)) {
            return null;
        }

        String rootPathName = PathKit.getWebRootPath() + File.separator + fileSystemPath + File.separator + String.valueOf(majorId);

        File rootPath = new File(rootPathName);
        if (!rootPath.exists() && !rootPath.mkdirs()) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("初始化文件根目录%s失败", rootPath.getPath()));
            }
            return null;
        }

        return rootPathName;
    }

    /**
     * 初始化文件系统的垃圾桶根目录
     *
     * @param majorId 专业编号
     * @return 垃圾桶根目录路径
     */
    public String initTrashPath(Long majorId) {
        if (majorId == null) {
            return null;
        }

        String trashSystemPath = ConfigUtils.getStr("global", "trashSystemPath");

        if (StringUtils.isBlank(trashSystemPath)) {
            return null;
        }

        String trashPathName = PathKit.getWebRootPath() + File.separator + trashSystemPath + File.separator + String.valueOf(majorId);

        File trashPath = new File(trashPathName);
        if (!trashPath.exists() && !trashPath.mkdirs()) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("初始化垃圾桶根目录%s失败", trashPath.getPath()));
            }
            return null;
        }

        return trashPathName;
    }

    /**
     * 创建文件夹
     *
     * @param majorId        专业编号
     * @param parentPathName 父目录名称
     * @param directoryName  文件夹名称
     * @return 文件夹
     */
    public File createDirectory(Long majorId, String parentPathName, String directoryName) {

        if (StringUtils.isBlank(parentPathName)) {
            parentPathName = initRootPath(majorId);
        }

        if (StringUtils.isBlank(parentPathName)) {
            return null;
        }

        StringBuilder filePathName = new StringBuilder(parentPathName);
        if (!parentPathName.endsWith(File.separator)) {
            filePathName.append(File.separator);
        }

        filePathName.append(directoryName);

        File filePath = new File(filePathName.toString());

        if (!filePath.exists() && !filePath.mkdirs()) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("创建文件夹%s失败", filePath.getPath()));
            }
            return null;
        }

        return filePath;
    }

    /**
     * 上传文件
     *
     * @param majorId        专业编号
     * @param parentPathName 父目录
     * @param sourceFile     上传文件
     * @return 文件
     */
    public File uploadFile(Long majorId, String parentPathName, File sourceFile) {
        if (StringUtils.isBlank(parentPathName)) {
            parentPathName = initRootPath(majorId);
        }

        if (StringUtils.isBlank(parentPathName)) {
            return null;
        }

        StringBuilder baseName = new StringBuilder(parentPathName);
        if (!parentPathName.endsWith(File.separator)) {
            baseName.append(File.separator);
        }

        String fileName = baseName.toString() + sourceFile.getName();

        File targetFile = new File(fileName);

        if (targetFile.exists()) {
            if (sourceFile.length() == targetFile.length()) {
                String targetFileMD5 = getFileMD5(targetFile);
                String sourceFileMD5 = getFileMD5(sourceFile);
                if (StringUtils.isNotBlank(targetFileMD5) && StringUtils.isNotBlank(sourceFileMD5) && targetFileMD5.equals(sourceFileMD5)) {
                    return targetFile;
                }
            }
            String secFileName = baseName + FilenameUtils.getBaseName(sourceFile.getName()) + "(1)." + FilenameUtils.getExtension(sourceFile.getName());
            targetFile = new File(secFileName);
        }

        try {
            FileUtils.copyFile(sourceFile, targetFile);
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("上传文件%s失败", sourceFile.getName()), e);
            }
            return null;
        }

        return targetFile;
    }

    /**
     * 文件下载
     *
     * @param path    文件路径
     * @param majorId 专业编号
     * @param isFile  是否是文件
     * @return 文件
     */
    public File downLoad(String path, Long majorId, Boolean isFile, String name) {


            if (StringUtils.isBlank(path) || majorId == null || isFile == null) {
                return null;
            }

            File file = new File(path);
            if (!file.exists()) {
                return null;
            }

            String trashPath = initTrashPath(majorId);
            if (StringUtils.isBlank(trashPath)) {
                return null;
            }


            String  targetFileName = trashPath + File.separator + FilenameUtils.getBaseName(file.getName()) + ".zip";



            File targetFile = new File(targetFileName);

            if (targetFile.exists()) {
                targetFile.delete();
            }

            try {
                if (!targetFile.createNewFile()) {
                    return null;
                }
            } catch (IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("下载失败", e);
                }
                return null;
            }

            FileUtils.zipFiles(path, "*", targetFileName);

            File zipFile = new File(targetFileName);
            if (!zipFile.exists()) {
                return null;
            }

            return zipFile;


    }
    /*public Response downLoadOnes(Response response, ResponseHeader header, String path) {

        //String path = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "templatePath") + "学生名单导入(所有教学班).xls";

        return renderFILE(new File(path), response, header);
    }*/

    /*public File downLoadOne(HttpServletRequest response, String fileUrl, String name) throws UnsupportedEncodingException {
        //String filePath = "c:/upload/";
        File file = new File(fileUrl);
        //原始文件名
        *//*String fileName0 = fileUrl.substring(0, fileUrl.indexOf(".") - 2);
        String fileFormat = fileUrl.substring(fileUrl.indexOf("."));
        String fileName = fileName0 + fileFormat;*//*
        if (file.exists()) { //判断文件是否存在
            // 配置文件下载
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8"));

            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流


            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            logger.info("----------file download---" + fileUrl);
            System.out.println("----------file download---" + fileUrl);
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
        return null;
    }
*/

    /**
     * 文件重命名
     *
     * @param oldFileName 旧文件名
     * @param newFileName 新文件名
     * @return 是否成功
     */
    public File rename(String oldFileName, String newFileName) {

        if (StringUtils.isBlank(oldFileName) || StringUtils.isBlank(newFileName)) {
            return null;
        }

        File oldFile = new File(oldFileName);

        if (!oldFile.exists()) {
            return null;
        }

        String parentPath = oldFile.getParent();

        File newFile = new File(parentPath + File.separator + newFileName);

        if (!oldFile.renameTo(newFile)) {
            return null;
        }

        return newFile;
    }

    /**
     * 删除文件
     *
     * @param path    原文件路径
     * @param majorId 专业编号
     * @return 删除文件路径
     */
    public String delete(String path, Long majorId) {
        if (StringUtils.isBlank(path) || majorId == null) {
            return null;
        }

        File file = new File(path);

        String trashPath = initTrashPath(majorId);

        if (!file.exists()) {
            return null;
        }

        String targetFileName = null;

        if (file.isFile()) {
            targetFileName = trashPath + File.separator + file.getName();
            if (!FileUtils.copyFile(path, targetFileName) || !FileUtils.deleteFile(path)) {
                return null;
            }
            return targetFileName;
        }

        targetFileName = trashPath + File.separator + file.getPath();

        if (!FileUtils.copyDirectory(path, targetFileName) || !FileUtils.deleteDirectory(path)) {
            return null;
        }

        return targetFileName;
    }

    /**
     * 获取所有父级目录
     *
     * @param majorId 专业编号
     * @param path    路径
     * @return 所有父级目录路径
     */
    public List<String> getAllParentPath(Long majorId, String path) {

        String rootPath = initRootPath(majorId);

        String replacePath = path.replace(rootPath, "");

//        String[] dirArray = replacePath.split(File.separator);
        String[] dirArray = StringUtils.split(replacePath, File.separatorChar);
        List<String> list = Lists.newArrayList();

        String temp = rootPath;
        for (String dir : dirArray) {
            temp = temp + File.separator + dir;
            list.add(temp);
        }

        return list;
    }

    /**
     * 获取文件MD5
     *
     * @param file 文件
     * @return 文件MD5
     */
    public String getFileMD5(File file) {
        if (file == null || !file.isFile()) {
            return null;
        }

        MessageDigest digest;
        FileInputStream fileInputStream = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            while ((len = fileInputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }

            return new BigInteger(1, digest.digest()).toString(16);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("Get file MD5 failed", e);
            }
            return null;
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Get file MD5 failed", e);
                }
            }
        }
    }

    /**
     * 文件名是否合法
     *
     * @param name 文件名称
     * @return
     */
    public boolean isValidFileName(String name) {
        char[] chars = blackList.toCharArray();

        for (char c : chars) {
            if (name.indexOf(c) != -1) {
                return false;
            }
        }
        return true;
    }

}
