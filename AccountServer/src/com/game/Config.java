package com.game;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	private static Properties properties = null;
	private static String mainPath = null;

	public static boolean initConfig(String path) {
		if (path.startsWith("/")) {
			mainPath = path.substring(0, path.indexOf("/", 2));
		}
		
		if (mainPath != null && mainPath.trim().length() > 0) {
			if (!mainPath.startsWith("/")) {
				return false;
			}
		}

		if (properties == null) {
			return loadProperties(path);
		}
		return true;
	}

	private static boolean loadProperties(String path) {
		try {
			properties = new Properties();
			InputStream inputStream = new BufferedInputStream(new FileInputStream(path));
			properties.load(inputStream);
			inputStream.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据key得到对应的配置
	 */
	public static String getPath(String key) {
		String path = properties.getProperty(key);
		if (mainPath != null && mainPath.trim().length() > 0) {
			if (path.startsWith("/") || mainPath.endsWith("/")) {
				path = mainPath + path;
			} else {
				path = mainPath + "/" + path;
			}
		}
		return path;
	}

	/**
	 * 根据Key读取对应的值
	 */
	public static String getValue(String key) {
		return properties.getProperty(key);
	}

	/**
	 * 根据Key获取对应的值
	 */
	public static int getInt(String key) {
		return Integer.parseInt(properties.getProperty(key));
	}
}
