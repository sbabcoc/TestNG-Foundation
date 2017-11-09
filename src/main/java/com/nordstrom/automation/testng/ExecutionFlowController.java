package com.nordstrom.automation.testng;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.IAnnotationTransformer;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import com.nordstrom.automation.testng.TestNGConfig.TestNGSettings;

/**
 * This TestNG listener performs several basic functions related to test method execution: <br>
 * <ul>
 *     <li>Propagate attributes: [<i>before</i> method] &rarr; [test method] &rarr; [<i>after</i> method]</li>
 *     <li>For test classes that implement the {@link IInvokedMethodListenerEx} interface,
 *     <b>ExecutionFlowController</b> forwards calls received by its own invoked method listener implementation to
 *     the corresponding methods in the test class. In-bound attribute propagation is performed before forwarding the
 *     {@link #beforeInvocation(IInvokedMethod, ITestResult)} call, and out-bound attribute propagation is performed
 *     after forwarding the {@link #afterInvocation(IInvokedMethod, ITestResult)} call.</li>
 * </ul> 
 */

/*
 * TODO - This block comment describes features that have yet to be implemented.
 * 
 * The following items describe features that have yet to be implemented: target platform, automatic retry, and method
 * timeout management. All of these will be implemented in the method interceptor.
 * 
 *     <li>For test classes that request target platform support: 
 *         <ul>
 *             <li>For runs targeting a specific platform, filter out non-target methods.</li>
 *             <li>Attach the specified (or default) target platform to each test method.</li>
 *             <li>Activate target platform prior to invoking each test method.</li>
 *         </ul>
 *     </li>
 *     <li>If automatic retry of failed tests is enabled: 
 *         <ul>
 *             <li>Attach the specified (or default) retry analyzer to each test method with no prior declaration.</li>
 *             <li><b>NOTE</b>: TestNG sets the status of retried tests to {@code SKIP}. The 'throwable' of these
 *                 retried tests distinguishes them from actual skipped tests, for which the 'throwable' is {@link 
 *                 org.testng.SkipException}.</li> 
 *         </ul>
 *     </li>
 *     <li>For methods that don't specify a timeout interval, set the configured (or default) standard interval.</li>
 */
public class ExecutionFlowController implements IInvokedMethodListener, IMethodInterceptor, IAnnotationTransformer {
    
    protected static final ThreadLocal<Map<String, Object>> fromBefore = new InheritableThreadLocal<Map<String, Object>>() {
        @Override
        public Map<String, Object> initialValue() {
            return new HashMap<>();
        }
    };
    
    protected static final ThreadLocal<Map<String, Object>> fromMethod = new InheritableThreadLocal<>();

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (testResult.getInstance() instanceof IInvokedMethodListenerEx) {
            ((IInvokedMethodListenerEx) testResult.getInstance()).afterInvocation(method, testResult);
        }
        
        if (method.getTestMethod().isBeforeMethodConfiguration()) {
            // merge with attributes from prior methods
            Map<String, Object> attributes = fromBefore.get();
            attributes.putAll(PropertyManager.extractAttributes(testResult));
            fromBefore.set(attributes);
        } else if (method.isTestMethod()) {
            fromMethod.set(PropertyManager.extractAttributes(testResult));
        } else if (method.getTestMethod().isAfterMethodConfiguration()) {
            // nothing to do here
        }
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.getTestMethod().isBeforeMethodConfiguration()) {
            // nothing to do here
        } else if (method.isTestMethod()) {
            PropertyManager.injectAttributes(fromBefore.get(), testResult);
            fromBefore.remove();
        } else if (method.getTestMethod().isAfterMethodConfiguration()) {
            PropertyManager.injectAttributes(fromMethod.get(), testResult);
            fromMethod.remove();
        }
        
        if (testResult.getInstance() instanceof IInvokedMethodListenerEx) {
            ((IInvokedMethodListenerEx) testResult.getInstance()).beforeInvocation(method, testResult);
        }
    }

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        // target platform support: filter out non-targeted methods
        // target platform support: set method target descriptions
        
        return methods;
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
        }
    }
}
