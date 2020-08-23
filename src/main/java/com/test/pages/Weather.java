package com.test.pages;

public class Weather {
	private String condition;
	private Float windSpeed;
	private Float humidity;
	private Float tempInCelcius;
	private Float tempInFahrenheit;
	
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public Float getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(Float windSpeed) {
		this.windSpeed = windSpeed;
	}
	public Float getHumidity() {
		return humidity;
	}
	public void setHumidity(Float humidity) {
		this.humidity = humidity;
	}
	public Float getTempInCelcius() {
		return tempInCelcius;
	}
	public void setTempInCelcius(Float tempInCelcius) {
		this.tempInCelcius = tempInCelcius;
	}
	public Float getTempInFahrenheit() {
		return tempInFahrenheit;
	}
	public void setTempInFahrenheit(Float tempInFahrenheit) {
		this.tempInFahrenheit = tempInFahrenheit;
	}
}
