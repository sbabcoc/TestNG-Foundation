package com.nordstrom.test_automation.ui_tools.testng_foundation;

import java.util.List;
import java.util.Map;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.ITestResult;

/**
 * This TestNG listener performs several basic functions related to test method execution: <br>
 * <ul>
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
 *     <li>Manage Selenium driver lifetime.
 *         <ul>
 *             <li>Driver capabilities will be provided by implementations of a standard interface.</li>
 *             <li>A few standard implementations will be provided (Chrome/Firefox/Edge).</li>
 *             <li>The standard implementations will be cataloged in an enumeration that maps target browsers to
 *                 capabilities records and fully qualified implementation class names.</li>
 *             <li>The driver catalog will be extensible through the Java properties mechanism.</li>
 *             <li><b>NOTE</b>: Method timeout intervals are adjusted to account for driver acquisition delay.</li>
 *         </ul>
 *     </li>
 *     <li>Propagate attributes: [<i>before</i> method] &rarr; [test method] &rarr; [<i>after</i> method]</li>
 * </ul> 
 */
public class ExecutionFlowController implements IInvokedMethodListener, IMethodInterceptor {
	
	protected static final ThreadLocal<Map<String, Object>> fromBefore = new InheritableThreadLocal<>();
	protected static final ThreadLocal<Map<String, Object>> fromMethod = new InheritableThreadLocal<>();

	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		if (testResult.getInstance() instanceof IInvokedMethodListenerEx) {
			((IInvokedMethodListenerEx) testResult.getInstance()).afterInvocation(method, testResult);
		}
		
	    if (method.getTestMethod().isBeforeMethodConfiguration()) {
		    fromBefore.set(PropertyManager.extractAttributes(testResult));
	    } else if (method.isTestMethod()) {
			fromMethod.set(PropertyManager.extractAttributes(testResult));
		} else if (method.getTestMethod().isAfterMethodConfiguration()) {
			
		}
	}

	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		if (testResult.getInstance() instanceof IInvokedMethodListenerEx) {
			((IInvokedMethodListenerEx) testResult.getInstance()).beforeInvocation(method, testResult);
		}
		
	    if (method.getTestMethod().isBeforeMethodConfiguration()) {
			
	    } else if (method.isTestMethod()) {
			PropertyManager.injectAttributes(fromBefore.get(), testResult);
			fromBefore.remove();
		} else if (method.getTestMethod().isAfterMethodConfiguration()) {
			PropertyManager.injectAttributes(fromMethod.get(), testResult);
			fromMethod.remove();
		}
	}

	@Override
	public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
		// target platform support: filter out non-targeted methods
		// target platform support: set method target descriptions
		
		return methods;
	}

}
