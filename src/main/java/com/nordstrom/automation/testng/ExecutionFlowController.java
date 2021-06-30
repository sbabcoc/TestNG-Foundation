package com.nordstrom.automation.testng;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

import org.testng.IAnnotationTransformer;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import com.nordstrom.automation.testng.TestNGConfig.TestNGSettings;

import static com.nordstrom.automation.testng.VersionUtility.getRetryAnalyzerClass;

/**
 * This TestNG listener performs several basic functions related to test method execution: 
 * <ul>
 *     <li><b>ExecutionFlowController</b> propagates test context attributes: [<i>before</i> method] &rarr; [test
 *     method] &rarr; [<i>after</i> method]<br>
 *     This feature enables tests to attach context-specific values that are accessible throughout the entire
 *     lifecycle of the test.</li>
 *     <li>For test classes that implement the {@link IInvokedMethodListenerEx} interface,
 *     <b>ExecutionFlowController</b> forwards calls received by its own invoked method listener implementation to
 *     the corresponding methods in the test class. In-bound attribute propagation is performed before forwarding the
 *     {@link #beforeInvocation(IInvokedMethod, ITestResult)} call, and out-bound attribute propagation is performed
 *     after forwarding the {@link #afterInvocation(IInvokedMethod, ITestResult)} call.</li>
 *     <li>For methods that don't specify a timeout interval, <b>ExecutionFlowController</b> sets the configured (or
 *     default) standard interval.</li>
 *     <li>If automatic retry of failed tests is enabled, <b>ExecutionFlowController</b> attaches the specified (or
 *     default) retry analyzer to each test method with no prior declaration.<br>
 *     <b>NOTE</b>: TestNG sets the status of retried tests to {@code SKIP}. The 'throwable' of these retried tests
 *     distinguishes them from actual skipped tests, for which the 'throwable' is {@link org.testng.SkipException}.
 *     </li>
 * </ul> 
 * 
 * <b>CONFIGURING METHOD TIMEOUT</b>
 * <p>
 * Method timeout is controlled by the {@link TestNGSettings#TEST_TIMEOUT TEST_TIMEOUT} setting. By default, this
 * setting is undefined. During initialization phase, TestNG passes each test that's about to be run to the
 * {@link ExecutionFlowController#transform(ITestAnnotation, Class, Constructor, Method) transform} method. If a test
 * timeout interval is specified (via {@link TestNGSettings#TEST_TIMEOUT TEST_TIMEOUT}) and this interval is longer
 * than the timeout that's already assigned to the test, the configured test timeout interval is assigned.
 * <p>
 * <b>CONFIGURING AUTOMATIC RETRY</b>
 * <p>
 * Automatic retry of failed tests is configured via two settings and a service loader provider configuration file:
 * <ul>
 *     <li>{@link TestNGSettings#MAX_RETRY MAX_RETRY} - The number of times a failed test will be retried
 *     (default = <b>0</b>).</li>
 *     <li>{@link TestNGSettings#RETRY_ANALYZER RETRY_ANALYZER} - The fully-qualified name of the retry analyzer class
 *     to attach to each test (default = {@link com.nordstrom.automation.testng.RetryManager}).</li>
 *     <li>{@code META-INF/services/org.testng.IRetryAnalyzer} - Service loader retry analyzer configuration file
 *     (absent by default). To add managed analyzers, create this file and add the fully-qualified names of their
 *     classes, one line per item.</li>
 * </ul>
 * 
 * Note that until you create and populate the provider configuration file, <b>RetryManager</b> will always return
 * {@code false}. Consequently, no failed tests will be retried. The {@link IRetryAnalyzer} implementations in the
 * classes specified by the configuration file determine whether or not any given failed test is retried.
 * <p>
 * <b>DECLINING AUTOMATIC RETRY SUPPORT</b>
 * <p>
 * Once automatic retry is enabled, {@link RetryManager} will be attached to every method that doesn't already specify
 * a retry analyzer. However, there may be test methods or classes that you don't wish to be retried. For example, you
 * may have a long-running test method that would delay completion of the suite, or you have an entire class of tests
 * that rely on externally-managed resources that must be replenished between runs.
 * <p>
 * For these sorts of scenarios, you can mark test methods or classes with the {@link NoRetry} annotation:
 * 
 * <blockquote><pre>
 * &#64;Test
 * &#64;NoRetry
 * public void testLongRunning() {
 *     // test implementation goes here
 * }</pre></blockquote>
 * 
 * <b>REDACTING PARAMETER VALUES IN LOG MESSAGES</b>
 * <p>
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
 * <b>PROPAGATION OF TEST ATTRIBUTES</b>
 * <p>
 * <b>ExecutionFlowController</b> propagates test context attributes from one phase of test execution to the next.
 * This feature enables tests to attach context-specific values that are accessible throughout the entire lifecycle
 * of the test.
 * <p>  
 * The attribute propagation feature of <b>ExecutionFlowController</b> produces many-to-one-to-many behavior:
 * <ul>
 *     <li>The attributes attached to all executed {@code @BeforeMethod} configuration methods are aggregated together
 *     for propagation to the test method.</li>
 *     <li>The attributes attached to the test method (which include those that were propagated from <i>before</i>)
 *     are propagated to all executed {@code @AfterMethod} configuration methods.</li>
 * </ul>
 * 
 * <b>MANAGING OBJECT REFERENCE ATTRIBUTES</b>
 * <p>
 * This attribute propagation feature provides an easy way for tests to maintain context-specific values. For any
 * attribute whose value is an object reference, this behavior can result in the creation of additional references
 * that will prevent the object from being marked for garbage collection until the entire suite of tests completes.
 * For these sorts of attributes, <b>TestNG Foundation</b> provides the {@link TrackedObject} class.
 * <p>
 * <b>TrackedObject</b> is a reference-tracking wrapper used by {@link PropertyManager} to record the test result
 * objects to which an object reference is propagated. This enables the client to release all references when the
 * object is no longer needed:
 * 
 * <blockquote><pre>
 * private static final DRIVER = "Driver";
 * 
 * public void setDriver(WebDriver driver) {
 *     ITestResult result = Reporter.getCurrentTestResult();
 *     if (driver != null) {
 *         new TrackedObject&lt;&gt;(result, DRIVER, driver);
 *     } else {
 *         Object val = result.getAttribute(DRIVER);
 *         if (val instanceof TrackedObject) {
 *             ((TrackedObject&lt;?&gt;) val).release();
 *         } else {
 *             result.setAttribute(DRIVER, null);
 *             result.removeAttribute(DRIVER);
 *         }
 *     }
 * }
 * 
 * public WebDriver getDriver() {
 *     Object obj;
 *     ITestResult result = Reporter.getCurrentTestResult();
 *     Object val = result.getAttribute(DRIVER);
 *     if (val instanceof TrackedObject) {
 *         obj = ((TrackedObject&lt;?&gt;) val).getValue();
 *     } else {
 *         obj = val;
 *     }
 *     return (WebDriver) obj;
 * }</pre></blockquote>
 * 
 * In this example, a <b>Selenium</b> driver attribute is stored as a tracked object. When the driver is no longer
 * needed, specifying a {@code null} value will signal that all propagated references should be released. To retrieve
 * the driver reference from the test attribute, extract it with the {@link TrackedObject#getValue()} method.
 */
public class ExecutionFlowController implements IInvokedMethodListener, IAnnotationTransformer {
    
    protected static final ThreadLocal<ITestResult> fromBefore = new InheritableThreadLocal<>();
    protected static final ThreadLocal<ITestResult> fromMethod = new InheritableThreadLocal<>();
    
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (testResult.getInstance() instanceof IInvokedMethodListenerEx) {
            ((IInvokedMethodListenerEx) testResult.getInstance()).afterInvocation(method, testResult);
        }
        
        Map<String, Object> attributes;
        if (method.getTestMethod().isBeforeMethodConfiguration()) {
            // merge with attributes from prior methods
            ITestResult lastResult = fromBefore.get();
            if (lastResult != null) {
                attributes = PropertyManager.extractAttributes(lastResult);
                PropertyManager.injectAttributes(attributes, testResult);
            }
            fromBefore.set(testResult);
        } else if (method.isTestMethod()) {
            fromMethod.set(testResult);
        } else if (method.getTestMethod().isAfterMethodConfiguration()) {
            // nothing to do here
        }
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        Map<String, Object> attributes;
        if (method.getTestMethod().isBeforeMethodConfiguration()) {
            // nothing to do here
        } else if (method.isTestMethod()) {
            ITestResult lastResult = fromBefore.get();
            if (lastResult != null) {
                attributes = PropertyManager.extractAttributes(lastResult);
                PropertyManager.injectAttributes(attributes, testResult);
                fromBefore.remove();
            }
        } else if (method.getTestMethod().isAfterMethodConfiguration()) {
            attributes = PropertyManager.extractAttributes(fromMethod.get());
            PropertyManager.injectAttributes(attributes, testResult);
        }
        
        if (testResult.getInstance() instanceof IInvokedMethodListenerEx) {
            ((IInvokedMethodListenerEx) testResult.getInstance()).beforeInvocation(method, testResult);
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        // if @Test for test method
        if (testMethod != null) {
            // get TestNG Foundation configuration
            TestNGConfig config = TestNGConfig.getConfig();
            // if default test timeout is defined
            if (config.containsKey(TestNGSettings.TEST_TIMEOUT.key())) {
                // get default test timeout
                long defaultTimeout = config.getLong(TestNGSettings.TEST_TIMEOUT.key());
                // if current timeout is less than default
                if (defaultTimeout > annotation.getTimeOut()) {
                    // set test timeout interval
                    annotation.setTimeOut(defaultTimeout);
                }
            }
            
            // if no retry analyzer is specified
            if (getRetryAnalyzerClass(annotation) == null) {
                // get default retry analyzer
                Class<IRetryAnalyzer> retryAnalyzerClass = config.getRetryAnalyzerClass();
                // if retry enabled
                if (retryAnalyzerClass != null) {
                    // determine if retry is disabled for this method
                    NoRetry noRetryOnMethod = testMethod.getAnnotation(NoRetry.class);
                    // determine if retry is disabled for the class that declares this method
                    NoRetry noRetryOnClass = testMethod.getDeclaringClass().getAnnotation(NoRetry.class);
                    
                    // if retry is not disabled for method or class
                    if ((noRetryOnMethod == null) && (noRetryOnClass == null)) {
                        // set retry analyzer
                        annotation.setRetryAnalyzer(retryAnalyzerClass);
                    }
                }
            }
        }
    }
}
