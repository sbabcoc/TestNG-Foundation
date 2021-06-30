package com.nordstrom.automation.testng;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.TestResult;

public class VersionUtility {
	
	public static Class<? extends IRetryAnalyzer> getRetryAnalyzerClass(ITestAnnotation annotation) {
		return annotation.getRetryAnalyzerClass();
	}
	
	public static ITestResult newEmptyTestResult() {
		return TestResult.newEmptyTestResult();
	}

}
