package com.test.cases;

import static com.test.utils.CommonUtils.timeElapsed;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.test.driver.DriverManager;
import com.test.pages.TestPage;
import com.test.utils.CommonUtils;
import com.test.utils.ConfigUtils;

public class TestWeatherReport {

	public static WebDriver driver;
	public static WebDriverWait wait;
	TestPage page;
	Logger logger = Logger.getLogger(TestWeatherReport.class.getName());
	Long startTime, endTime;

	/**
	 * Browser Initialization
	 * 
	 * @throws Exception
	 */
	@BeforeSuite
	@Parameters("browser")
	public void initialize(@Optional("Chrome") String browser) throws Exception {
		logger.info("Initializing Driver");
		DriverManager driverManager = new DriverManager(browser);
		driver = driverManager.getDriver();
		page = new TestPage(driver);
	}

	/**
	 * Removes existing screenshots and csv data
	 */
	@BeforeTest
	public void clear() {
		logger.info("clear() : method start");
		CommonUtils.removeFiles(ConfigUtils
				.getProperty("SCREENSHOT_PATH")); /* clearing screenshots */
		CommonUtils.removeFiles(ConfigUtils
				.getProperty("REPORTS_PATH")); /* clearing Emailable reports */
		logger.info("clear() : method end");
	}

	/**
	 * Screenshot on Failure
	 * 
	 * @throws IOException
	 */
	@AfterMethod
	public void takeScreenShotOnFailure(ITestResult testResult) throws IOException {
		try {
			if (testResult.getStatus() == ITestResult.FAILURE) {
				logger.info(testResult.getStatus() + " Screenshot Status [2: FAILURE]");
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile,
						new File(ConfigUtils.getProperty("SCREENSHOT_PATH") + testResult.getName() + ".jpg"));
			} else
				logger.info(testResult.getStatus() + " Screenshot Status [1: SUCCESS]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closing the browser
	 * 
	 * @throws Exception
	 */
	@AfterSuite
	public void tearDown() throws Exception {
		driver.quit();
	}

	/**
	 * Navigating to Weather Page
	 * 
	 */
	@Test
	public void testWeatherMenu() {
		logger.info("testWeatherMenu validation Started:");
		startTime = System.currentTimeMillis();
		Assert.assertEquals(page.weatherMenu(), "Current weather conditions in your city.");
		endTime = System.currentTimeMillis();
		logger.info("Time elapsed for testWeatherMenu: " + timeElapsed(startTime, endTime) + " seconds");
	}

	/**
	 * City which you want to Pin on your map
	 * 
	 * @throws Exception
	 * 
	 */
	@Test(dependsOnMethods = "testWeatherMenu")
	public void testPinCity() throws Exception {
		logger.info("testPinCity validation Started:");
		startTime = System.currentTimeMillis();
		Assert.assertTrue(page.pinCity());
		endTime = System.currentTimeMillis();
		logger.info("Time elapsed for testPinCity: " + timeElapsed(startTime, endTime) + " seconds");
	}

	/**
	 * Detailed Weather Report
	 * 
	 */
	@Test(dependsOnMethods = "testPinCity")
	public void testDetailNDTVReport() {
		logger.info("testDetailNDTVReport validation Started:");
		startTime = System.currentTimeMillis();
		Assert.assertNotNull(page.getNDTVWeatherReport());
		endTime = System.currentTimeMillis();
		logger.info("Time elapsed for testDetailNDTVReport: " + timeElapsed(startTime, endTime) + " seconds");
	}

	/**
	 * API call for Weather Report
	 * 
	 */
	@Test(dependsOnMethods = "testDetailNDTVReport")
	public void testAPIWeatherDetails() {
		logger.info("testAPIWeatherDetails validation Started:");
		startTime = System.currentTimeMillis();
		Assert.assertEquals(page.apiWeatherDetails(), 200);
		endTime = System.currentTimeMillis();
		logger.info("Time elapsed for testAPIWeatherDetails: " + timeElapsed(startTime, endTime) + " seconds");
	}

	/**
	 * Test Variance using Comparator for Temprature
	 * 
	 * @throws Exception
	 * 
	 */
	@Test(dependsOnMethods = "testAPIWeatherDetails")
	public void testTempVariance() throws Exception {
		logger.info("testTempVariance validation Started:");
		startTime = System.currentTimeMillis();
		Assert.assertTrue(page.isValidTemp());
		endTime = System.currentTimeMillis();
		logger.info("Time elapsed for testTempVariance: " + timeElapsed(startTime, endTime) + " seconds");
	}

	/**
	 * Test Variance using Comparator for Humidity
	 * 
	 * @throws Exception
	 * 
	 */
	@Test(dependsOnMethods = "testAPIWeatherDetails")
	public void testHumidityVariance() throws Exception {
		logger.info("testHumidityVariance validation Started:");
		startTime = System.currentTimeMillis();
		Assert.assertTrue(page.isValidHumidity());
		endTime = System.currentTimeMillis();
		logger.info("Time elapsed for testHumidityVariance: " + timeElapsed(startTime, endTime) + " seconds");
	}
	
	/**
	 * Test Variance using Comparator for Wind Speed
	 * 
	 * @throws Exception
	 * 
	 */
	@Test(dependsOnMethods = "testAPIWeatherDetails")
	public void testWindSpeedVariance() throws Exception {
		logger.info("testWindSpeedVariance validation Started:");
		startTime = System.currentTimeMillis();
		Assert.assertTrue(page.isValidWindSpeed());
		endTime = System.currentTimeMillis();
		logger.info("Time elapsed for testWindSpeedVariance: " + timeElapsed(startTime, endTime) + " seconds");
	}
}
