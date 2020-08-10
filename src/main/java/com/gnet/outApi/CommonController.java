package com.gnet.outApi;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.render.RenderException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gnet.Constant;
import com.gnet.FileInfo;
import com.gnet.FileInfo.FileType;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.IApi;
import com.gnet.api.sign.SignCreator;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.plugin.route.ControllerBind;
import com.gnet.service.ApiPermissionService;
import com.gnet.utils.DateUtil;
import com.gnet.utils.DateUtils;
import com.gnet.utils.FreemarkerUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.upload.UploadFile;

import freemarker.template.TemplateException;

@ControllerBind(controllerKey = "/outer")
public class CommonController extends Controller {

	public static final Logger logger = Logger.getLogger(CommonController.class);
	
	@SuppressWarnings("unchecked")
	public void api() {
		Date date = new Date();
		
		String method = getRequest().getMethod().toUpperCase();
		
		Response response = new Response();
		ResponseHeader header = new ResponseHeader();

		Request r = new Request();
		header.setTrdate(DateUtils.formatDateTime(new Date()));
		
		// 过滤File文件
		FileInfo fileInfo = null;
		String contentType = getRequest().getContentType();
		if (StrKit.notBlank(contentType) && contentType.toLowerCase().indexOf("multipart") != -1) {
			// 需要解析文件
			try {
				fileInfo = getFileInfo();
			} catch (IOException | TemplateException e) {
				header.setErrorcode("1006");
				header.setSuccflag(Response.FAIL);
				response.setHeader(header);
				renderJson(response);
				return;
			}
		}
		
		String content;
		
		if (!ConfigUtils.getBoolean("global", "isDev")) {
			JSONObject all;
			try {
				all = JSON.parseObject(getPara("content"));
			} catch (Exception e) {
				header.setErrorcode("991");
				header.setSuccflag(Response.FAIL);
				response.setHeader(header);
				renderJson(response);
				return;
			}
			
			if (!all.containsKey("content") || !all.containsKey("sign")) {
				header.setErrorcode("992");
				header.setSuccflag(Response.FAIL);
				response.setHeader(header);
				renderJson(response);
				return;
			}
			
			String sign = all.getString("sign");
			content = all.getString("content");
			
			// 签名验证
			if (!SignCreator.verify(sign)) {
				header.setErrorcode("1000");
				header.setSuccflag(Response.FAIL);
				response.setHeader(header);
				renderJson(response);
				return;
			}
		} else {
			content = getPara("content");
		}
		
		// 请求为空
		if (StringUtils.isBlank(content)) {
			header.setErrorcode("1001");
			header.setSuccflag(Response.FAIL);
			response.setHeader(header);
			renderJson(response);
			return;
		}
		
		// 检查结果
		try {
			r = JSON.parseObject(content, Request.class);
		} catch (Exception e) {
			// 请求格式错误
			header.setTrcode("EMXXXXXXX");
			header.setErrorcode("1002");
			header.setSuccflag(Response.FAIL);
			response.setHeader(header);
			renderJson(response);
			return;
		}
		header.setTrcode(r.getHeader().getTrcode());
		header.setAppseq(r.getHeader().getAppseq());
		
//		// Token校验
//		String noToken[] = new String[]{"EM00001", "EM00137"};
//		String token = null;
//		if (!ArrayUtils.contains(noToken, r.getHeader().getTrcode())) {
//			// 在必须校验的范围内
//			token = r.getHeader().getToken();
//			if (StrKit.isBlank(token)) {
//				// 请求格式错误
//				header.setErrorcode("1010");
//				header.setSuccflag(Response.FAIL);
//				response.setHeader(header);
//				renderJson(response);
//				return;
//			}
//
//			if (!UserCacheKit.isLogin(token)) {
//				try {
//					getResponse().sendError(401, "无权访问该API");
//				} catch (IOException e) {
//					logger.error(e.getMessage(), e);
//					// ignore
//				}
//				renderNull();
//				return;
//			}
//		}

		if (!r.getHeader().getTrcode().startsWith("OUTER")) {
			try {
				getResponse().sendError(404, "未找到该路径");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				// ignore
			}
			renderNull();
			return;
		}
		
		// 将FileInfo存储在data数据中，传递到service层
		if (fileInfo != null) {
			r.getData().put("fileInfo", fileInfo);
		}
		
		// 未找到服务号
		IApi service = null;
		try {
			service = SpringContextHolder.getBean(r.getHeader().getTrcode());
		} catch (Exception e) {
			header.setErrorcode("1003");
			header.setSuccflag(Response.FAIL);
			response.setHeader(header);
			renderJson(response);
			return;
		}
		
//		//权限控制
//		if (!ArrayUtils.contains(noToken, r.getHeader().getTrcode()) && !Constant.USER_ADMIN.equals(UserCacheKit.getUser(token).getStr("loginName"))) {
//			ApiPermissionService apiPermissionService = SpringContextHolder.getBean(ApiPermissionService.class);
//			if (!apiPermissionService.hasApiPermission(r.getHeader().getTrcode(), r.getHeader().getToken())) {
//				//无访问权限
//				try {
//					getResponse().sendError(403, "无访问该API的权限");
//				} catch (IOException e) {
//					logger.error(e.getMessage(), e);
//				}
//				renderNull();
//				return;
//			}
//		}
		
		String requestIp = getIpAddr(this.getRequest());
		r.setRequestIp(requestIp);
		response = service.excute(r, response, header, method);
		
		// 如果是文件类的Response，renderFile返回
		if(Response.WORD_FILE.equals(response.getHeader().getSuccflag()) && response.getData() != null && response.getData() instanceof File){
			File file = (File) response.getData();
			HttpServletResponse httpServletResponse = getResponse();
			String fileName = "Word";
			try {
				fileName = new String(response.getFileName().getBytes("UTF-8"), "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			httpServletResponse.setContentType("application/msword");
			httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + fileName + ".doc");
			httpServletResponse.setContentLength((int)file.length());
			InputStream inputStream = null;
			OutputStream outputStream = null;
			try {
				inputStream = new BufferedInputStream(new FileInputStream(file));
				outputStream = httpServletResponse.getOutputStream();
				byte[] buffer = new byte[1024];
				for (int n = -1; (n = inputStream.read(buffer)) != -1;) {
					outputStream.write(buffer, 0, n);
				}
				outputStream.flush();
			}
			catch (Exception e) {
				throw new RenderException(e);
			}
			finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(file.exists()){
					file.delete();
				}
			}
		} else if (Response.FILE.equals(response.getHeader().getSuccflag()) && response.getData() != null && response.getData() instanceof File) {
			renderFile((File) response.getData());
		} else {
			renderJson(response);
		}
		
		
		Date over = new Date();
		long execute_time = over.getTime() - date.getTime();
		
		if (logger.isDebugEnabled()) {
			logger.debug(r.getHeader().getTrcode() + " execute time is :" + execute_time + " ms");
		}
		
		return;
	}
	
	/**
	 * 获得上传文件信息
	 *
	 * @return 文件信息
	 * @throws TemplateException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private FileInfo getFileInfo() throws IOException, TemplateException {
		Date date = new Date();
		Properties properties = ConfigUtils.getProps("upload");
		String encode = getPara("encode");
		FileType fileType = FileType.valueOf(getPara("fileType"));
		String defaultFieldName = "upfile";
		String defaultTempDirectory = System.getProperty("java.io.tmpdir") + "/upload_certification/" + UUID.randomUUID();

		Map<String, String> models = Maps.newConcurrentMap();
		models.put("ioTemp", System.getProperty("java.io.tmpdir"));
		models.put("yyyyMM", DateUtil.dateToString(date, "yyyyMM"));
		models.put("time", DateUtil.dateToString(date, "HHmmss"));
		models.put("rand", String.valueOf((int) Math.random() * 10000));
		models.put("uuid", UUID.randomUUID().toString());

		Integer maxSize = null;
		String fieldName = null;
		String tempDirectory = null;
		String targetDirectory = null;
		List<String> uploadExtensions = null;
		if (fileType == FileType.flash) {
			maxSize = Integer.valueOf(properties.getProperty("flashMaxSize"));
			fieldName = properties.getProperty("flashFieldName", defaultFieldName);
			tempDirectory = FreemarkerUtils.process(properties.getProperty("flashCachePath", defaultTempDirectory), models);
			targetDirectory = FreemarkerUtils.process(properties.getProperty("flashPathFormat"), models);
			uploadExtensions = (List<String>) properties.get("flashAllowFiles");
		} else if (fileType == FileType.media) {
			maxSize = Integer.valueOf(properties.getProperty("videoMaxSize"));
			fieldName = properties.getProperty("videoFieldName", defaultFieldName);
			tempDirectory = FreemarkerUtils.process(properties.getProperty("videoCachePath", defaultTempDirectory), models);
			targetDirectory = FreemarkerUtils.process(properties.getProperty("videoPathFormat"), models);
			uploadExtensions = (List<String>) properties.get("videoAllowFiles");
		} else if (fileType == FileType.file) {
			maxSize = Integer.valueOf(properties.getProperty("fileMaxSize"));
			fieldName = properties.getProperty("fileFieldName", defaultFieldName);
			tempDirectory = FreemarkerUtils.process(properties.getProperty("fileCachePath", defaultTempDirectory), models);
			targetDirectory = FreemarkerUtils.process(properties.getProperty("filePathFormat"), models);
			uploadExtensions = (List<String>) properties.get("fileAllowFiles");
		} else {
			maxSize = Integer.valueOf(properties.getProperty("imageMaxSize"));
			fieldName = properties.getProperty("imageFieldName", defaultFieldName);
			tempDirectory = FreemarkerUtils.process(properties.getProperty("imageCachePath", defaultTempDirectory), models);
			targetDirectory = FreemarkerUtils.process(properties.getProperty("imagePathFormat"), models);
			uploadExtensions = (List<String>) properties.get("imageAllowFiles");
		}

		UploadFile up = getFile(fieldName, tempDirectory, maxSize, encode);
		if (getParaToInt("size") != null && getParaToInt("size") > maxSize) {
			return null;
		}
		if (FilenameUtils.isExtension(up.getFileName(), uploadExtensions)) {
			return null;
		}
		return FileInfo.getFileInfo(up, targetDirectory, fileType, DateUtil.stringtoDate(getPara("lastModifiedDate"), "EEE MMM dd yyyy hh:mm:ss z"), getParaToLong("size"));
	}
	
	/**
	 * 从request中获取IP地址
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) { 
	    String ip = request.getHeader("x-forwarded-for"); 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){ 
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr(); 
	    }
	    return ip; 
	}

}
