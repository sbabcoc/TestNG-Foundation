package com.nordstrom.automation.testng;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.TestResult;

public class VersionUtility {
    
    public static Class<? extends IRetryAnalyzer> getRetryAnalyzerClass(ITestAnnotation annotation) {
        IRetryAnalyzer retryAnalyzer = annotation.getRetryAnalyzer();
        return (retryAnalyzer != null) ? retryAnalyzer.getClass() : null;
    }

    public static ITestResult newEmptyTestResult() {
        return new TestResult();
    }

}
