package com.nordstrom.automation.testng;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import org.testng.IRetryAnalyzer;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RetryAnalyzerCheck {
    
    static Class<? extends IRetryAnalyzer> unspecified;
    
    @BeforeClass
    public void beforeClass() {
        unspecified = Placeholder.class;
    }
    
    @Test
    public void retryAnalyzerUnspecified() {
        unspecified = getRetryAnalyzer();
        assertNotEquals(unspecified, Placeholder.class);
    }
    
    @Test(retryAnalyzer = TestAnalyzer.class)
    public void retryAnalyzerIsSpecified() {
        assertEquals(getRetryAnalyzer(), TestAnalyzer.class);
    }
    
    @Test
    @NoRetry
    public void retryIsDisabledForThisTest() {
        assertNull(getRetryAnalyzer());
    }
    
    @SuppressWarnings("deprecation")
    private static Class<? extends IRetryAnalyzer> getRetryAnalyzer() {
        ITestResult testResult = Reporter.getCurrentTestResult();
        ITestNGMethod method = testResult.getMethod();
        IRetryAnalyzer retryAnalyzer = method.getRetryAnalyzer();
        // if retry analyzer defined
        if (retryAnalyzer != null) {
            // get class of retry analyzer
            Class<? extends IRetryAnalyzer> clazz = retryAnalyzer.getClass();
            // if not the default retry analyzer injected by TestNG 7+
            if ( ! "org.testng.internal.annotations.DisabledRetryAnalyzer".equals(clazz.getName())) {
                return clazz;
            }
        }
        return null;
    }
    
    public static class TestAnalyzer implements IRetryAnalyzer {
        @Override
        public boolean retry(ITestResult result) {
            return false;
        }
    }
    
    public static class Placeholder implements IRetryAnalyzer {
        @Override
        public boolean retry(ITestResult result) {
            return false;
        }
    }
    
}
