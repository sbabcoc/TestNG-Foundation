package com.nordstrom.automation.testng;

import java.util.Map;
import java.util.Map.Entry;

import org.testng.ITestResult;
import org.testng.collections.Maps;

/**
 * This utility class provides basic management for properties associated with specific tests.<br>
 * <ul>
 *     <li>{@link #extractAttributes(ITestResult)} extracts the attribute collection from the specified test result
 *         into a {@link Map}.</li>
 *     <li>{@link #injectAttributes(Map, ITestResult)} injects the entries of the specified {@link Map} into the
 *         attribute collection of the specified test result.</li>
 * </ul>
 */
public class PropertyManager {
	
	private PropertyManager() {
		throw new AssertionError("PropertyManager is a static utility class.");
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
			for (Entry<String, Object> thisEntry : attributes.entrySet()) {
				testResult.setAttribute(thisEntry.getKey(), thisEntry.getValue());
			}
		}
	}

}
