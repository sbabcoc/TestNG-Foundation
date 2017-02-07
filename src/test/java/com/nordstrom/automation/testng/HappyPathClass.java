package com.nordstrom.automation.testng;

import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

class HappyPathClass {
	
	protected static final String ATTRIBUTE = "ATTRIBUTE";
	protected static final String VALUE = "VALUE";
	
	protected static String fromBefore;
	protected static ITestResult beforeResult;
	protected static String fromMethod;
	protected static ITestResult methodResult;
	protected static String fromAfter;
	protected static ITestResult afterResult;
	
	@BeforeMethod
	public void beforeMethod(Method method) {
		if ("testMethod".equals(method.getName())) {
			fromBefore = VALUE;
			beforeResult = Reporter.getCurrentTestResult();
			beforeResult.setAttribute(ATTRIBUTE, fromBefore);
		}
	}
	
	@Test
	public void testMethod() {
		methodResult = Reporter.getCurrentTestResult();
		fromMethod = (String) methodResult.getAttribute(ATTRIBUTE);
		assertTrue(true);
	}
	
	@Test
	public void secondTest() {
		assertTrue(true);
	}
	
	@AfterMethod
	public void afterMethod(Method method) {
		if ("testMethod".equals(method.getName())) {
			afterResult = Reporter.getCurrentTestResult();
			fromAfter = (String) afterResult.getAttribute(ATTRIBUTE);
		}
	}
}