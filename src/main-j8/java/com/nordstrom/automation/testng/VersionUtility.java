package com.nordstrom.automation.testng;

import org.testng.IRetryAnalyzer;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
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

    public static Class<? extends IRetryAnalyzer> getRetryAnalyzer() {
        ITestResult testResult = Reporter.getCurrentTestResult();
        ITestNGMethod method = testResult.getMethod();
        Class<? extends IRetryAnalyzer> clazz = method.getRetryAnalyzerClass();
        return (clazz != DisabledRetryAnalyzer.class) ? clazz : null;
    }
    
}
