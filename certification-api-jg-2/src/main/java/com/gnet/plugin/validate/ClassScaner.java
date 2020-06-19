package com.gnet.plugin.validate;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassScaner {
	/**
	 * 获取某包下所有类
	 * @param packageName 包名
	 * @param isRecursion 是否遍历子包
	 * @return 类的完整名称
	 */
	public static Set<String> getClassnameFromPackage(String packageName, boolean isRecursion) {
		Set<String> names = null;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		URL url = loader.getResource(packageName.replace(".", "/"));
		if (url != null) {
			String protocol = url.getProtocol();
			if (protocol.equals("file")) {
				names = getFromDir(url.getPath(), packageName, isRecursion);
			} else if (protocol.equals("jar")) {
				JarFile jarFile = null;
				try{
	                jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
				} catch(Exception e){
					e.printStackTrace();
				}
				
				if(jarFile != null){
					getFromJar(jarFile.entries(), packageName, isRecursion);
				}
			}
		} else {
			/*从所有的jar包中查找包名*/
			names = getFromJars(((URLClassLoader)loader).getURLs(), packageName, isRecursion);
		}
		
		return names;
	}

	/**
	 * 从项目文件获取某包下所有类
	 * @param path 文件路径
	 * @param className 类名集合
	 * @param isRecursion 是否遍历子包
	 * @return 类的完整名称
	 */
	private static Set<String> getFromDir(String path, String pkg, boolean isRecursion) {
		Set<String> className = new HashSet<String>();
		File file = new File(path);
		File[] files = file.listFiles();
		for (File childFile : files) {
			if (childFile.isDirectory()) {
				if (isRecursion) {
					className.addAll(getFromDir(childFile.getPath(), pkg+"."+childFile.getName(), isRecursion));
				}
			} else {
				String fileName = childFile.getName();
				if (fileName.endsWith(".class") && !fileName.contains("$")) {
					className.add(pkg+ "." + fileName.replace(".class", ""));
				}
			}
		}

		return className;
	}

	
	/**
	 * @param jarEntries
	 * @param pkg
	 * @param isRecursion
	 * @return
	 */
	private static Set<String> getFromJar(Enumeration<JarEntry> jarEntries, String pkg, boolean isRecursion){
		Set<String> names = new HashSet<String>();
		
		while (jarEntries.hasMoreElements()) {
			JarEntry jarEntry = jarEntries.nextElement();
			if(!jarEntry.isDirectory()){
				/*
	             * 这里是为了方便，先把"/" 转成 "." 再判断 ".class" 的做法可能会有bug
	             * (FIXME: 先把"/" 转成 "." 再判断 ".class" 的做法可能会有bug)
	             */
				String entryName = jarEntry.getName().replace("/", ".");
				if (entryName.endsWith(".class") && !entryName.contains("$") && entryName.startsWith(pkg)) {
					entryName = entryName.replace(".class", "");
					if(isRecursion){
						names.add(entryName);
					} else if(!entryName.replace(pkg+".", "").contains(".")){
						names.add(entryName);
					}
				}
			}
		}
		
		return names;
	}
	
	/**
	 * 从所有jar中搜索该包，并获取该包下所有类
	 * @param urls URL集合
	 * @param pkg 包路径
	 * @param isRecursion 是否遍历子包
	 * @return 类的完整名称
	 */
	private static Set<String> getFromJars(URL[] urls, String pkg, boolean isRecursion) {
		Set<String> names = new HashSet<String>();
		
		for (int i = 0; i < urls.length; i++) {
			String classPath = urls[i].getPath();
			
			//不必搜索classes文件夹
			if (classPath.endsWith("classes/")) {continue;}

			JarFile jarFile = null;
			try {
				jarFile = new JarFile(classPath.substring(classPath.indexOf("/")));
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (jarFile != null) {
				names.addAll(getFromJar(jarFile.entries(), pkg, isRecursion));
			}
		}
		
		return names;
	}
}