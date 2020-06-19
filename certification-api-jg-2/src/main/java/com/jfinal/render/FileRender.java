/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.render;

import static com.jfinal.core.Const.DEFAULT_FILE_CONTENT_TYPE;

import com.jfinal.kit.PathKit;
import java.io.*;
import java.net.URLEncoder;
import javax.servlet.ServletContext;

/**
 * FileRender.
 */
public class FileRender extends Render {
	
	private static final long serialVersionUID = 4293616220202691369L;
	private File file;
	private String fileName;
	private static String fileDownloadPath;
	private static ServletContext servletContext;
	private static String webRootPath;
	
	public FileRender(File file) {
		this.file = file;
	}
	
	public FileRender(String fileName) {
		this.fileName = fileName;
	}
	
	static void init(String fileDownloadPath, ServletContext servletContext) {
		FileRender.fileDownloadPath = fileDownloadPath;
		FileRender.servletContext = servletContext;
		webRootPath = PathKit.getWebRootPath();
	}
	
	public void render() {
		if (fileName != null) {
			if (fileName.startsWith("/"))
				file = new File(webRootPath + fileName);
			else
				file = new File(fileDownloadPath + fileName);
		}
		
		if (file == null || !file.isFile() || file.length() > Integer.MAX_VALUE) {
            // response.sendError(HttpServletResponse.SC_NOT_FOUND);
            // return;
			
			// throw new RenderException("File not found!");
			RenderFactory.me().getErrorRender(404).setContext(request, response).render();
			return ;
        }
		
//		try {
//			response.addHeader("Content-disposition", "attachment; filename=" + new String(file.getName().getBytes("GBK"), "ISO8859-1"));
//		} catch (UnsupportedEncodingException e) {
//			response.addHeader("Content-disposition", "attachment; filename=" + file.getName());
//		}
		try {
			String encoded = URLEncoder.encode(file.getName(), "UTF-8").replace("+", "%20").replace("*", "%2A")
				.replace("%7E", "~");
			response.addHeader("Content-disposition", String.format("attachment; filename=%s; filename*=utf-8''%s", encoded, encoded));
		} catch (UnsupportedEncodingException e) {
			// ignore
		}

		String contentType = servletContext.getMimeType(file.getName());
        if (contentType == null) {
        	contentType = DEFAULT_FILE_CONTENT_TYPE;		// "application/octet-stream";
        }
        
        response.setContentType(contentType);
        response.setContentLength((int)file.length());
		response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            outputStream = response.getOutputStream();
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
            // 文件下载以后，删除它
//            if(file.exists()) {
//            	boolean result = file.delete();
//            	System.out.println("!!!!!!!!!!!!!!!!"+fileName+"文件删除!!!!!!!!!!!!!!!!!! = "+result);
//            }
        }
	}
}


