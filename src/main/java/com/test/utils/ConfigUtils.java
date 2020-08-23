package com.test.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtils {
	private static Properties prop;
	static {
		try {
			prop = readPropertyFile();
		} catch (Exception e) {
			System.out.println("Could not Read the Properties file");
		}
	}

	private static Properties readPropertyFile() throws IOException {
		Properties prp = new Properties();
		String propFilePath = "src/main/resources/config.properties";
		String[] tmpArray = propFilePath.split("/");
		String propFileName = tmpArray[tmpArray.length - 1];
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = loader.getResourceAsStream(propFileName);

		try {
			if (inputStream != null) {
				prp.load(inputStream);
			}
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return prp;
	}

	public static String getProperty(String name) {
		if (prop != null)
			return prop.getProperty(name);
		return null;
	}
}
