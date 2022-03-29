package com.nordstrom.automation.testng;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
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
        unspecified = VersionUtility.getRetryAnalyzer();
        assertNotEquals(unspecified, Placeholder.class);
    }
    
    @Test(retryAnalyzer = TestAnalyzer.class)
    public void retryAnalyzerIsSpecified() {
        assertEquals(VersionUtility.getRetryAnalyzer(), TestAnalyzer.class);
    }
    
    @Test
    @NoRetry
    public void retryIsDisabledForThisTest() {
        assertNull(VersionUtility.getRetryAnalyzer());
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
