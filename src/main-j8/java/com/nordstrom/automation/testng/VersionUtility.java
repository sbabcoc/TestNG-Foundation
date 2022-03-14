package com.nordstrom.automation.testng;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.TestResult;
import org.testng.internal.annotations.DisabledRetryAnalyzer;

public class VersionUtility {
    
    public static Class<? extends IRetryAnalyzer> getRetryAnalyzerClass(ITestAnnotation annotation) {
        Class<? extends IRetryAnalyzer> retryAnalyzerClass = annotation.getRetryAnalyzerClass();
        return (retryAnalyzerClass != DisabledRetryAnalyzer.class) ? retryAnalyzerClass : null;
    }
    
    public static ITestResult newEmptyTestResult() {
        return TestResult.newEmptyTestResult();
    }

}
