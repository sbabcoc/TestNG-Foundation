package com.nordstrom.automation.testng;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.nordstrom.automation.testng.TestNGConfig.TestNGSettings;
import com.nordstrom.common.base.ExceptionUnwrapper;

public class RetryAnalyzer implements IRetryAnalyzer {
    
    private final TestNGConfig config;
    private final Map<InvocationRecord, Integer> invocations;
    private final ServiceLoader<RetryAnalyzer> retryAnalyzerLoader;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    public RetryAnalyzer() {
        config = TestNGConfig.getConfig();
        invocations = new ConcurrentHashMap<>();
        retryAnalyzerLoader = ServiceLoader.load(RetryAnalyzer.class);
    }

    @Override
    public boolean retry(ITestResult result) {
        boolean doRetry = false;
        result.setThrowable(ExceptionUnwrapper.unwrap(result.getThrowable()));
        
        InvocationRecord invocation = new InvocationRecord(result);
        Integer count = invocations.get(invocation);
        
        if (count == null) {
            count = config.getInt(TestNGSettings.MAX_RETRY.key());
        }
        
        if (count > 0) {
            invocations.put(invocation, --count);
            doRetry = isRetriable(result);
            
            if (doRetry) {
                logger.warn("### RETRY ### [{}/{}] {}", invocation.suiteName, invocation.testName, invocation);
            }
        }
        
        return doRetry;
    }

    /**
     * Determine if the specified failed test should be retried
     *  
     * @param result failed result to be evaluated
     * @return {@code true} if test should be retried; otherwise {@code false}
     */
    protected boolean isRetriable(ITestResult result) {
        Iterator<RetryAnalyzer> iterator = retryAnalyzerLoader.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().retry(result)) {
                return true;
            }
        }
        return false;
    }
}
