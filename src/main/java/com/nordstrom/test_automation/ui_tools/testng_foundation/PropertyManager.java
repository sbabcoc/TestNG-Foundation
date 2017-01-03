package com.nordstrom.test_automation.ui_tools.testng_foundation;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.collections.Maps;

/**
 * This utility class provides basic management for properties associated with specific tests.<br>
 * <ul>
 *     <li>{@link #extractAttributes(ITestResult)} extracts the attribute collection from the specified test result
 *         into a {@link Map}.</li>
 *     <li>{@link #injectAttributes(Map, ITestResult)} injects the entries of the specified {@link Map} into the
 *         attribute collection of the specified test result.</li>
 *     <li>There are also getters/setters for drivers attached to specific tests: 
 *         <ul>
 *             <li>{@link #getDriver()} gets the driver for the current test.</li>
 *             <li>{@link #setDriver(WebDriver)} sets the driver for the current test.</li>
 *             <li>{@link #getDriver(ITestResult)} gets the driver for the specified test result.</li>
 *             <li>{@link #getDriver(WebDriver, ITestResult)} sets the driver for the specified test result.</li>
 *         </ul>
 *     </li>
 * <ul>
 */
public class PropertyManager {
	
	private static final String DRIVER = "DRIVER";
	
	private PropertyManager() {
		throw new AssertionError("PropertyManager is a static utility class.");
	}
	
	/**
	 * Get the driver for the current test
	 * 
	 * @return driver for the current test
	 */
	public static WebDriver getDriver() {
		return getDriver(Reporter.getCurrentTestResult());
	}
	
	/**
	 * Set the driver for the current test
	 * 
	 * @param driver driver for the current test
	 */
	public static void setDriver(WebDriver driver) {
		setDriver(driver, Reporter.getCurrentTestResult());
	}
	
	/**
	 * Get the driver for the specified test result
	 * 
	 * @param testResult test result object
	 * @return driver from the specified test result
	 */
	public static WebDriver getDriver(ITestResult testResult) {
		return (WebDriver) testResult.getAttribute(DRIVER);
	}
	
	/**
	 * Set the driver for the specified test result
	 * 
	 * @param driver test result object
	 * @param testResult driver for the specified test result
	 */
	public static void setDriver(WebDriver driver, ITestResult testResult) {
		testResult.setAttribute(DRIVER, driver);
	}

	/**
	 * Extract the attribute collection from the specified test result into a {@link Map}.
	 * 
	 * @param testResult test result object
	 * @return map of test result attributes
	 */
	public static Map<String, Object> extractAttributes(ITestResult testResult) {
		Map<String, Object> result = Maps.newHashMap();
		for (String thisName : testResult.getAttributeNames()) {
			result.put(thisName, testResult.getAttribute(thisName));
		}
		return result;
	}

	/**
	 * Inject the entries of the specified {@link Map} into the attribute collection of the specified test result.
	 * 
	 * @param attributes map of test result attributes
	 * @param testResult test result object
	 */
	public static void injectAttributes(Map<String, Object> attributes, ITestResult testResult) {
		if (attributes != null) {
			for (String thisName : attributes.keySet()) {
				testResult.setAttribute(thisName, attributes.get(thisName));
			}
		}
	}

}
