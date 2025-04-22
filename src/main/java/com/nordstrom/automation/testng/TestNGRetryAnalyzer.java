package com.nordstrom.automation.testng;

import org.testng.ITestResult;

/**
 * <b>TestNG Foundation</b> retry analyzers implement this interface. 
 */
public interface TestNGRetryAnalyzer {

    /**
     * Determine if the specified failed test should be retried.
     *  
     * @param result failed result to be evaluated
     * @return {@code true} if test should be retried; otherwise {@code false}
     */
    boolean retry(final ITestResult result);
    
}
