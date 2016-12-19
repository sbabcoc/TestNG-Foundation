package com.nordstrom.test_automation.ui_tools.testng_foundation;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.collections.Maps;

public class PropertyManager {
	
	private static final String DRIVER = "DRIVER";
	
	public static WebDriver getDriver() {
		return getDriver(Reporter.getCurrentTestResult());
	}
	
	public static void setDriver(WebDriver driver) {
		setDriver(driver, Reporter.getCurrentTestResult());
	}
	
	public static WebDriver getDriver(ITestResult testResult) {
		return (WebDriver) testResult.getAttribute(DRIVER);
	}
	
	public static void setDriver(WebDriver driver, ITestResult testResult) {
		testResult.setAttribute(DRIVER, driver);
	}

	public static Map<String, Object> extractAttributes(ITestResult testResult) {
		Map<String, Object> result = Maps.newHashMap();
		for (String thisName : testResult.getAttributeNames()) {
			result.put(thisName, testResult.getAttribute(thisName));
		}
		return result;
	}

	public static void injectAttributes(Map<String, Object> attributes, ITestResult testResult) {
		if (attributes != null) {
			for (String thisName : attributes.keySet()) {
				testResult.setAttribute(thisName, attributes.get(thisName));
			}
		}
	}

}
