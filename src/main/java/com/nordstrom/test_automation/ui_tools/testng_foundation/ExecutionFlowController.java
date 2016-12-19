package com.nordstrom.test_automation.ui_tools.testng_foundation;

import java.util.Map;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public class ExecutionFlowController implements IInvokedMethodListener {
	
	protected static final ThreadLocal<Map<String, Object>> fromBefore = new InheritableThreadLocal<>();
	protected static final ThreadLocal<Map<String, Object>> fromMethod = new InheritableThreadLocal<>();

	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
	    if (method.getTestMethod().isBeforeMethodConfiguration()) {
		    fromBefore.set(PropertyManager.extractAttributes(testResult));
	    } else if (method.isTestMethod()) {
			fromMethod.set(PropertyManager.extractAttributes(testResult));
		} else if (method.getTestMethod().isAfterMethodConfiguration()) {
			
		}
	}

	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
	    if (method.getTestMethod().isBeforeMethodConfiguration()) {
			
	    } else if (method.isTestMethod()) {
			PropertyManager.injectAttributes(fromBefore.get(), testResult);
			fromBefore.remove();
		} else if (method.getTestMethod().isAfterMethodConfiguration()) {
			PropertyManager.injectAttributes(fromMethod.get(), testResult);
			fromMethod.remove();
		}
	}

}
