package com.test.utils;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

public class CommonUtils {

	private static Logger logger = Logger.getLogger(CommonUtils.class.getName());

	/**
	 * Evaluate the time taken for execution of a test case
	 * 
	 */
	public static long timeElapsed(long start, long end) {
		return (end - start) / 1000l;
	}

	/**
	 * Clearing the Data Folders before starting the test cases
	 * 
	 */
	public static void removeFiles(String path) {
		try {
			File file = new File(path);
			File[] files = file.listFiles();
			if (null != files) {
				for (File f : files) {
					if (f.isFile() && f.exists()) {
						f.delete();
						logger.info("successfully deleted");
					} else {
						logger.info("cant delete a file due to open or error");
					}
				}
			}
		} catch (Exception e) {
			logger.warning("Exception Occured While Removing files : " + e.getMessage());
		}
	}

	/**
	 * For converting Temprature Unit from Kelvin to Celcius
	 * 
	 */
	public static Float kelvinToDegrees(String temp) {
		return (Float.parseFloat(temp) - 273.15f);
	}

	/**
	 * For converting Humidity from Sting to Float having %
	 * 
	 */
	public static Float parseHumidityValue(String humidity) {
		if (humidity.contains("%")) {
			String humid = humidity.replace("%", "");
			return Float.parseFloat(humid);
		} else
			return Float.parseFloat(humidity);
	}
	
	/**
	 * Getting Wind Speed
	 * 
	 */
	public static Float ndtvWindSpeedValue(String windSpeed) {
		String[] wind=windSpeed.split(" Gusting");
		System.out.println(Arrays.toString(wind));
		String speed=wind[0].replace(" KPH", "");
		return Float.parseFloat(speed);
	}
	
	/**
	 * For converting wind Speed Unit from m/sec to KPH
	 * 
	 */
	public static Float apiSpeedConversion(String windSpeed) {
		return (Float.parseFloat(windSpeed)*3.6f);
	}
}
