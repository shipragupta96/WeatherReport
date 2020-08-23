package com.test.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.test.utils.ConfigUtils;

public class DriverManager {
	private WebDriver driver;

	public DriverManager(String driverName) throws Exception {
		if (driverName.equalsIgnoreCase("Chrome"))
			initializeChromeBrowser();
		else if (driverName.equalsIgnoreCase("Firefox"))
			initializeFirefoxBrowser();
		else if (driverName.equalsIgnoreCase("IE"))
			initializeIEBrowser();
		else
			throw new Exception("Invalid Driver Name");
	}

	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * Intialize chrome browser and launch NDTV
	 * 
	 */
	private void initializeChromeBrowser() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.navigate().to(ConfigUtils.getProperty("URL"));
	}

	/**
	 * Intialize Firefox browser and launch NDTV
	 * 
	 */
	private void initializeFirefoxBrowser() {
		System.setProperty("webdriver.gecko.driver", "drivers/geckodriver.exe");
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.get(ConfigUtils.getProperty("URL"));
	}

	/**
	 * Intialize IE browser and launch NDTV
	 * 
	 */
	private void initializeIEBrowser() {
		System.setProperty("webdriver.ie.driver", "drivers/IEDriverServer.exe");
		driver = new InternetExplorerDriver();
		driver.manage().window().maximize();
		driver.get(ConfigUtils.getProperty("URL"));
	}
}
