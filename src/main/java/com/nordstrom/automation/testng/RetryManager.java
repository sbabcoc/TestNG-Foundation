package com.nordstrom.automation.testng;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.nordstrom.automation.testng.TestNGConfig.TestNGSettings;
import com.nordstrom.common.base.ExceptionUnwrapper;

/**
 * This TestNG retry analyzer provides a framework for invoking collections of scenario-specific analyzers that are
 * installed via the {@link ServiceLoader}:
 * <ul>
 *     <li>Managed retry analyzers implement the {@link TestNGRetryAnalyzer} interface.</li>
 *     <li>Analyzer classes are added to the managed collection via entries in a file named
 *     <blockquote>{@code META-INF/services/com.nordstrom.automation.testng.TestNGRetryAnalyzer}</blockquote></li> 
 *     <li>The number of times a failed test will be retried is configured via the
 *     {@link TestNGSettings#MAX_RETRY MAX_RETRY} setting, which defaults to <b>0</b>.</li>
 * </ul>
 * 
 * Prior to retrying a failed test, <b>RetryManager</b> emits a debug-level message in this format:
 * <blockquote>{@code ### RETRY ### [suite-name/test-name] className.methodName(parmValue...)}</blockquote>
 * 
 * The class/method portion of these messages is produced by the {@link InvocationRecord} class. The content of each
 * {@code parmValue} item (if any) represents the actual value passed to the corresponding argument of a failed
 * invocation of a parameterized test. Sensitive values can be redacted by marking their test method arguments with
 * the {@link RedactValue} annotation:
 * 
 * <blockquote><pre>
 * &#64;Test
 * &#64;Parameters({"username", "password"})
 * public void testLogin(String username, &#64;RedactValue String password) {
 *     // test implementation goes here
 * }</pre></blockquote>
 * 
 * The retry message for this method would include the actual user name, but redact the password:
 * <blockquote>{@code ### RETRY ### [MySuite/MyTest] AccountTest.testLogin(john.doe, |:arg1:|)}</blockquote>
 * 
 * <b>AUTOMATIC ATTACHMENT OF RETRYMANAGER</b>
 * <p>
 * Note that <b>RetryManager</b> is attached by {@link ExecutionFlowController} to every test method when automatic
 * retry is enabled. This behavior can be disabled on a per-method or per-class basis via the {@link NoRetry}
 * annotation. You can also specify an alternate retry analyzer via the {@link TestNGSettings#RETRY_ANALYZER
 * RETRY_ANALYZER} setting. See the {@link ExecutionFlowController} documentation for more details.
 * <p>
 * <b>USING RETRYMANAGER IN ANOTHER FRAMEWORK</b>
 * <p>
 * Typically, scenario-specific retry analyzers are installed via the service loader. However, if you plan to use
 * <b>RetryManager</b> in another framework, we recommend that you extend this class and override the
 * {@link #isRetriable(ITestResult)} method instead of registering your retry analyzer via the service loader.
 * This strategy enables clients of your framework to add their own analyzers without disconnecting yours. Just make
 * sure to invoke the overridden method in <b>RetryManager</b> if your analyzer declines to request method retry:
 * 
 * <blockquote><pre>
 * &#64;Override
 * protected boolean isRetriable(ITestResult result) {
 *     if (isRetriableInFramework(result)) {
 *         return true;
 *     }
 *     return super.isRetriable(result);
 * }</pre></blockquote>
 * 
 * Remember to override the value of the {@link TestNGSettings#RETRY_ANALYZER RETRY_ANALYZER} setting with the
 * fully-qualified class name of your framework-specific extension of <b>RetryManager</b> to enable activation
 * of your analyzer by {@link ExecutionFlowController}.
 * <br>&nbsp;
 * @see ExecutionFlowController
 * @see InvocationRecord
 * @see RedactValue
 * @see NoRetry
 */
public class RetryManager implements IRetryAnalyzer {
    
    private final TestNGConfig config;
    private final Map<InvocationRecord, Integer> invocations;
    private final ServiceLoader<TestNGRetryAnalyzer> retryAnalyzerLoader;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * Constructor: Initialize invocation map and service loader.
     */
    public RetryManager() {
        config = TestNGConfig.getConfig();
        invocations = new ConcurrentHashMap<>();
        retryAnalyzerLoader = ServiceLoader.load(TestNGRetryAnalyzer.class);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retry(final ITestResult result) {
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
                logger.warn("### RETRY ### [{}/{}] {}", invocation.suiteName, invocation.testName, invocation,
                        getThrowableToLog(result));
            }
        }
        
        return doRetry;
    }

    /**
     * Determine if the specified failed test should be retried.
     *  
     * @param result failed result to be evaluated
     * @return {@code true} if test should be retried; otherwise {@code false}
     */
    protected boolean isRetriable(final ITestResult result) {
        synchronized(retryAnalyzerLoader) {
            for (TestNGRetryAnalyzer analyzer : retryAnalyzerLoader) {
                if (analyzer.retry(result)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Get the {@link Throwable} to log with the retry notification.
     * 
     * @param result result of test method that's being retried
     * @return if exception logging is indicated, the exception that caused the test to fail; otherwise {@code null}
     */
    private Throwable getThrowableToLog(ITestResult result) {
        if (logger.isDebugEnabled() || config.getBoolean(TestNGSettings.RETRY_MORE_INFO.key())) {
            return result.getThrowable();
        }
        return null;
    }
}
