package com.test.pages;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.test.utils.CommonUtils;
import com.test.utils.ComparatorUtils;
import com.test.utils.ConfigUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class TestPage {
	WebDriver driver;
	WebDriverWait wait;
	JavascriptExecutor js;
	Actions action;
	Weather ndtvWeather;
	Weather apiWeather;
	Response response;
	Logger logger = Logger.getLogger(TestPage.class.getName());

	public TestPage(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Open the Weather Page
	 * 
	 */
	public String weatherMenu() {
		wait = new WebDriverWait(driver, 10);
		WebElement subMenu = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a#h_sub_menu")));
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", subMenu);
		wait = new WebDriverWait(driver, 15);
		WebElement weather = wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("WEATHER")));
		weather.click();
		WebElement clickVerify = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.infoHolder")));
		return clickVerify.getText();
	}

	/**
	 * Pin your city for which you want to enable the Weather Report
	 * 
	 * @throws Exception
	 * 
	 */
	public boolean pinCity() throws Exception {
		String cityName = ConfigUtils.getProperty("CITY");
		WebElement searchCity = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div#more input#searchBox")));
		action = new Actions(driver);
		action.click(searchCity).sendKeys(cityName).build().perform();
		searchCity.sendKeys(Keys.ENTER);
		WebElement city = driver.findElement(By.id(cityName));
		if (city.isSelected())
			logger.info("The City is already Pinned and Available on Map");
		else
			city.click();
		boolean cityOnMap;
		try {
			cityOnMap = driver.findElement(By.cssSelector("div.outerContainer[title='" + cityName + "']"))
					.isDisplayed();
		} catch (NoSuchElementException e) {
			logger.info("City is not displayed on Map");
			Thread.sleep(10000);
			action.doubleClick(city).build().perform();
			cityOnMap = driver.findElement(By.cssSelector("div.outerContainer[title='" + cityName + "']"))
					.isDisplayed();
		}
		if (cityOnMap) {
			logger.info("Your Selected city ia available on Map for Weather Report");
			WebElement cityMap = driver.findElement(By.cssSelector("div.outerContainer[title='" + cityName + "']"));
			String celciusTemp = cityMap.findElement(By.cssSelector("span.tempRedText")).getText();
			String fahrenheitTemp = cityMap.findElement(By.cssSelector("span.tempWhiteText")).getText();
			if (celciusTemp != null && fahrenheitTemp != null)
				return true;
		}
		return false;
	}

	/**
	 * Get detailed weather Report for your pinned City
	 * 
	 */
	public Weather getNDTVWeatherReport() {
		WebElement cityMap = driver
				.findElement(By.cssSelector("div.outerContainer[title='" + ConfigUtils.getProperty("CITY") + "']"));
		WebElement farenhiteTemp = cityMap.findElement(By.cssSelector("span.tempWhiteText"));
		farenhiteTemp.click();
		String[] details = getNDTVWeather();
		if (details != null && details.length == 5) {
			ndtvWeather = new Weather();
			ndtvWeather.setCondition(details[0]);
			ndtvWeather.setWindSpeed(CommonUtils.ndtvWindSpeedValue(details[1]));
			ndtvWeather.setHumidity(CommonUtils.parseHumidityValue(details[2]));
			ndtvWeather.setTempInCelcius(Float.parseFloat(details[3]));
			ndtvWeather.setTempInFahrenheit(Float.parseFloat(details[4]));
			return ndtvWeather;
		}
		return null;
	}

	private String[] getNDTVWeather() {
		String[] eachDetail;
		int i = 0;
		List<WebElement> details = driver.findElements(By.cssSelector("span.heading"));
		String[] actualReport = new String[details.size()];
		for (WebElement detail : details) {
			try {
				eachDetail = detail.getText().split(": ");
			} catch (Exception e) {
				eachDetail = detail.getText().split(" : ");
			}
			logger.info("Weather Details: " + Arrays.toString(eachDetail));
			actualReport[i++] = eachDetail[1];
		}
		return actualReport;
	}

	/**
	 * Get details of Weather Report from API
	 * 
	 */
	public int apiWeatherDetails() {
		RestAssured.baseURI = "http://api.openweathermap.org/";
		if (ConfigUtils.getProperty("CALL_BY").equalsIgnoreCase("City Name"))
			response = cityName();
		else if (ConfigUtils.getProperty("CALL_BY").equalsIgnoreCase("City ID"))
			response = cityId();
		else if (ConfigUtils.getProperty("CALL_BY").equalsIgnoreCase("geographic coordinates"))
			response = geoCord();
		else if (ConfigUtils.getProperty("CALL_BY").equalsIgnoreCase("ZIP code"))
			response = zipCode();
		apiWeather = new Weather();
		String humidity = JsonPath.with(response.body().asString()).get("main.humidity").toString();
		apiWeather.setHumidity(CommonUtils.parseHumidityValue(humidity));
		String temprature = JsonPath.with(response.body().asString()).get("main.temp").toString();
		apiWeather.setTempInCelcius(CommonUtils.kelvinToDegrees(temprature));
		String windSpeed = JsonPath.with(response.body().asString()).get("wind.speed").toString();
		apiWeather.setWindSpeed(CommonUtils.apiSpeedConversion(windSpeed));
		return response.getStatusCode();
	}

	private Response cityName() {
		response = RestAssured.given()
				.queryParams("q", ConfigUtils.getProperty("NAME_PARAM"), "appid", ConfigUtils.getProperty("KEY")).when()
				.get("/data/2.5/weather");
		return response;
	}

	private Response cityId() {
		response = RestAssured.given()
				.queryParams("q", ConfigUtils.getProperty("ID_PARAM"), "appid", ConfigUtils.getProperty("KEY")).when()
				.get("/data/2.5/weather");
		return response;
	}

	private Response geoCord() {
		response = RestAssured
				.given().queryParams("lat", ConfigUtils.getProperty("LATITUDE"), "lon",
						ConfigUtils.getProperty("LONGITUDE"), "appid", ConfigUtils.getProperty("KEY"))
				.when().get("/data/2.5/weather");
		return response;
	}

	private Response zipCode() {
		response = RestAssured.given()
				.queryParams("zip", ConfigUtils.getProperty("ZIP_CODE"), "appid", ConfigUtils.getProperty("KEY")).when()
				.get("/data/2.5/weather");
		return response;
	}

	/**
	 * If Temprature is within variance Range
	 * 
	 */
	public boolean isValidTemp() throws Exception {
		int compare = ComparatorUtils.compareByTemp(ndtvWeather, apiWeather);
		if (compare == 0)
			return true;
		else
			throw new Exception("Temprature is not in Variance Range");
	}

	/**
	 * If Humidity is within variance Range
	 * 
	 */
	public boolean isValidHumidity() throws Exception {
		int compare = ComparatorUtils.compareByHumidity(ndtvWeather, apiWeather);
		if (compare == 0)
			return true;
		else
			throw new Exception("Humidity is not in Variance Range");
	}
	
	/**
	 * If Wind Speed is within variance Range
	 * 
	 */
	public boolean isValidWindSpeed() throws Exception {
		int compare = ComparatorUtils.compareByWindSpeed(ndtvWeather, apiWeather);
		if (compare == 0)
			return true;
		else
			throw new Exception("Wind Speed is not in Variance Range");
	}
}