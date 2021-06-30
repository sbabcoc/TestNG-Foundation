package com.nordstrom.automation.testng;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.TestResult;

public class VersionUtility {
	
	public static IRetryAnalyzer getRetryAnalyzerClass(ITestAnnotation annotation) {
		return annotation.getRetryAnalyzer();
	}

	public static ITestResult newEmptyTestResult() {
		return new TestResult();
	}

}
