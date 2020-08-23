package com.test.utils;

import java.util.Comparator;

import com.test.pages.Weather;

public class ComparatorUtils {

	private static Comparator<Weather> tempComparator = new Comparator<Weather>() {
		public int compare(Weather o1, Weather o2) {
			Float diff = Math.abs(o1.getTempInCelcius() - o2.getTempInCelcius());
			Float variance = Float.parseFloat(ConfigUtils.getProperty("TEMP_VARIANCE"));
			if (diff <= variance)
				return 0;
			return -1;
		}
	};

	private static Comparator<Weather> humidityComparator = new Comparator<Weather>() {
		public int compare(Weather o1, Weather o2) {
			Float diff = Math.abs(o1.getHumidity() - o2.getHumidity());
			Float variance = Float.parseFloat(ConfigUtils.getProperty("HUMIDITY_VARIANCE"));
			if (diff <= variance)
				return 0;
			return -1;
		}
	};
	
	private static Comparator<Weather> windSpeedComparator = new Comparator<Weather>() {
		public int compare(Weather o1, Weather o2) {
			Float diff = Math.abs(o1.getWindSpeed() - o2.getWindSpeed());
			Float variance = Float.parseFloat(ConfigUtils.getProperty("WIND_SPEED_VARIANCE"));
			if (diff <= variance)
				return 0;
			return -1;
		}
	};

	/**
	 * Comparing the Temprature Difference based on Variance
	 * 
	 */
	public static int compareByTemp(Weather o1, Weather o2) {
		return tempComparator.compare(o1, o2);
	}

	/**
	 * Comparing the Humidity Difference based on Variance
	 * 
	 */
	public static int compareByHumidity(Weather o1, Weather o2) {
		return humidityComparator.compare(o1, o2);
	}
	
	/**
	 * Comparing the Wind Speed Difference based on Variance
	 * 
	 */
	public static int compareByWindSpeed(Weather o1, Weather o2) {
		return windSpeedComparator.compare(o1, o2);
	}
}
