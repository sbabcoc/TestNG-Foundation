package com.nordstrom.test_automation.ui_tools.testng_foundation;

import static org.testng.Assert.assertEquals;

import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

class MethodListenerExtension implements IInvokedMethodListenerEx {
	
	private static final String FROM_BEFORE = "FromBefore";
	private static final String FROM_METHOD = "FromMethod";
	
	static boolean beforeMethodBefore;
	static boolean beforeMethodAfter;
	static boolean testMethodBefore;
	static boolean testMethodAfter;
	static boolean afterMethodBefore;
	static boolean afterMethodAfter;
	
	@BeforeMethod
	public void beforeMethod() {
		Reporter.getCurrentTestResult().setAttribute(FROM_BEFORE, FROM_BEFORE);
	}
	
	@Test
	public void testMethod() {
		Reporter.getCurrentTestResult().setAttribute(FROM_METHOD, FROM_METHOD);
		String fromBefore = (String) Reporter.getCurrentTestResult().getAttribute(FROM_BEFORE);
		assertEquals(fromBefore, FROM_BEFORE, "Incorrect [fromBefore] value");
	}
	
	@AfterMethod
	public void afterMethod() {
		String fromBefore = (String) Reporter.getCurrentTestResult().getAttribute(FROM_BEFORE);
		String fromMethod = (String) Reporter.getCurrentTestResult().getAttribute(FROM_METHOD);
		assertEquals(fromBefore, FROM_BEFORE, "Incorrect [fromBefore] value");
		assertEquals(fromMethod, FROM_METHOD, "Incorrect [fromMethod] value");
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
	    if (method.getTestMethod().isBeforeMethodConfiguration()) {
			beforeMethodBefore = true;
	    } else if (method.isTestMethod()) {
	    	testMethodBefore = true;
		} else if (method.getTestMethod().isAfterMethodConfiguration()) {
			afterMethodBefore = true;
		}
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
	    if (method.getTestMethod().isBeforeMethodConfiguration()) {
			beforeMethodAfter = true;
	    } else if (method.isTestMethod()) {
	    	testMethodAfter = true;
		} else if (method.getTestMethod().isAfterMethodConfiguration()) {
			afterMethodAfter = true;
		}
	}
}