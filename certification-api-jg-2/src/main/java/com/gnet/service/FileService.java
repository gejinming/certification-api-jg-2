package com.gnet.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import jodd.typeconverter.Convert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.stereotype.Component;

import com.gnet.FileInfo;
import com.gnet.FileInfo.FileType;
import com.gnet.FileInfo.OrderType;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.utils.DateUtil;
import com.gnet.utils.FreemarkerUtils;
import com.google.common.collect.Maps;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;

import freemarker.template.TemplateException;

/**
 * 文件Service层
 * 
 * @type service
 * @description 提供文件的上传、复制、查询功能
 * @author xuq
 * @date 2015年10月23日15:54:01
 * @version 1.0
 */
@Component(value = "fileService")
public class FileService {

	private static final Logger logger = Logger.getLogger(FileService.class);

	/**
	 * 上传文件
	 * 
	 * @description 根据文件信息上传文件，async参数若为true，则不上传0。
	 * 
	 * @call {@linkplain FileInfo fileInfo} {@linkplain FileInfo#getFile()
	 *       getFile}
	 * 
	 * @param fileInfo
	 *            文件信息
	 * @param async
	 *            是否异步上传
	 */
	public boolean upload(FileInfo fileInfo, boolean async) {
		Properties properties = ConfigUtils.getProps("storage");
		if (fileInfo == null) {
			return false;
		}
		File tempFile = fileInfo.getFile();
		if (tempFile == null) {
			return false;
		}
		try {
			String destPath = "";
			boolean storageEnable = Convert.toBooleanValue(properties.getProperty("storageEnable", "false"));
			if (storageEnable) {
				String storageIp = properties.getProperty("storageIp", "127.0.0.1");
				String storagePort = properties.getProperty("storagePort", "21");
				String storagePath = properties.getProperty("storagePath", "");
				destPath = storageIp + ":" + storagePort + "/" + storagePath + destPath;
			} else {
				destPath = fileInfo.getTargetUrl();
			}

			if (!tempFile.getParentFile().exists()) {
				tempFile.getParentFile().mkdirs();
			}
			if (async) {
				// TODO 添加异步上传
				// addTask(storagePlugin, destPath, tempFile,
				// multipartFile.getContentType());
			} else {
				try {
					upload(destPath, tempFile);
				} finally {
					FileUtils.deleteQuietly(tempFile);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 上传文件
	 * 
	 * @description 根据文件信息默认上传文件
	 * 
	 * @call {@linkplain FileService this}
	 *       {@linkplain FileService#upload(FileInfo, boolean) upload}
	 * 
	 * @param fileInfo
	 * @param file
	 *            没用的参数
	 * @return
	 */
	public boolean upload(FileInfo fileInfo, File file) {
		return upload(fileInfo, false);
	}

	/**
	 * 不建议使用
	 * 
	 * @deprecated
	 * @param fileInfo
	 * @return
	 */
	@Deprecated
	public String uploadLocal(FileInfo fileInfo) {
		File file = fileInfo.getFile();
		if (file == null) {
			return null;
		}
		try {
			String destPath = fileInfo.getTargetUrl();
			String dirPath = PathKit.getWebRootPath();
			destPath = dirPath + destPath.replace("/", "\\");
			File destFile = new File(destPath);
			if (!destFile.getParentFile().exists()) {
				destFile.getParentFile().mkdirs();
			}
			fileChannelCopy(file, destFile);
			return destPath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 文件浏览
	 * 
	 * @description 根据文件路径、文件类型、排序方式读取文件列表
	 * 
	 * @param path
	 *            文件路径
	 * @param fileType
	 *            文件类型
	 * @param orderType
	 *            排序方式
	 * @return 文件信息列表
	 * @throws TemplateException
	 * @throws IOException
	 */
	public List<FileInfo> browser(String path, FileType fileType, OrderType orderType) throws IOException, TemplateException {
		Date date = new Date();
		Properties properties = ConfigUtils.getProps("ueditor");
		if (path != null) {
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			if (!path.endsWith("/")) {
				path += "/";
			}
		} else {
			path = "/";
		}

		String dirPath = PathKit.getWebRootPath();

		String virtualPath = "";

		Map<String, String> models = Maps.newConcurrentMap();
		models.put("ioTemp", System.getProperty("java.io.tmpdir"));
		models.put("yyyyMM", DateUtil.dateToString(date, "YYYYMM"));
		models.put("time", DateUtil.dateToString(date, "HHmmss"));
		models.put("rand", String.valueOf((int) Math.random() * 10000));
		models.put("uuid", UUID.randomUUID().toString());

		if (fileType == FileType.flash) {
			virtualPath = FreemarkerUtils.process(properties.getProperty("flashPathFormat"), models);
		} else if (fileType == FileType.media) {
			virtualPath = FreemarkerUtils.process(properties.getProperty("videoPathFormat"), models);
		} else if (fileType == FileType.file) {
			virtualPath = FreemarkerUtils.process(properties.getProperty("filePathFormat"), models);
		} else {
			virtualPath = FreemarkerUtils.process(properties.getProperty("imagePathFormat"), models);
		}

		virtualPath = StringUtils.substringBeforeLast(virtualPath, "/");
		String browsePath = dirPath + (virtualPath + path).replace("/", "\\");
		List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		if (browsePath.indexOf("..") >= 0) {
			return fileInfos;
		}
		File directory = new File(browsePath);
		if (directory.exists() && directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				FileInfo fileInfo = new FileInfo();
				fileInfo.setName(file.getName());
				fileInfo.setTargetUrl(virtualPath + path + file.getName());
				fileInfo.setIsDirectory(file.isDirectory());
				fileInfo.setSize(file.length());
				fileInfo.setLastModified(new Date(file.lastModified()));
				fileInfos.add(fileInfo);
			}
		}
		if (orderType == OrderType.size) {
			Collections.sort(fileInfos, new SizeComparator());
		} else if (orderType == OrderType.type) {
			Collections.sort(fileInfos, new TypeComparator());
		} else {
			Collections.sort(fileInfos, new NameComparator());
		}
		return fileInfos;
	}

	private class NameComparator implements Comparator<FileInfo> {
		public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
			return new CompareToBuilder().append(!fileInfos1.getIsDirectory(), !fileInfos2.getIsDirectory()).append(fileInfos1.getName(), fileInfos2.getName()).toComparison();
		}
	}

	private class SizeComparator implements Comparator<FileInfo> {
		public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
			return new CompareToBuilder().append(!fileInfos1.getIsDirectory(), !fileInfos2.getIsDirectory()).append(fileInfos1.getSize(), fileInfos2.getSize()).toComparison();
		}
	}

	private class TypeComparator implements Comparator<FileInfo> {
		public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
			return new CompareToBuilder().append(!fileInfos1.getIsDirectory(), !fileInfos2.getIsDirectory()).append(FilenameUtils.getExtension(fileInfos1.getName()), FilenameUtils.getExtension(fileInfos2.getName())).toComparison();
		}
	}

	/**
	 * 使用文件通道的方式复制文件
	 * 
	 * @description 将文件s的内容拷贝到文件t中，这会覆盖文件t的内容
	 * 
	 * @param s
	 *            源文件
	 * @param t
	 *            新文件
	 */
	public void fileChannelCopy(File s, File t) {
		FileInputStream fi = null;
		FileOutputStream fo = null;

		FileChannel in = null;
		FileChannel out = null;

		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);

			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道

			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void upload(String path, File file) {
		String dirPath = PathKit.getWebRootPath();
		path = dirPath + path;
		File destFile = new File(path);

		if (logger.isDebugEnabled()) {
			logger.debug("UPLOAD DEST PATH:" + destFile);
		}

		try {
			FileUtils.moveFile(file, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
