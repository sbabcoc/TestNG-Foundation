package com.nordstrom.automation.testng;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

/**
 * This interface extends {@link IInvokedMethodListener} without adding any new method signatures. While any class that
 * implements {@link IInvokedMethodListenerEx} can be activated as an invoked method listener, the intended target for
 * this interface is test classes whose execution will be orchestrated by {@link ExecutionFlowController}. During test
 * execution, the execution flow controller forwards calls received by its own {@link #beforeInvocation(IInvokedMethod,
 * ITestResult)} and {@link #afterInvocation(IInvokedMethod, ITestResult)} implementations to those of the test class.
 * 
 * @see ExecutionFlowController
 * @see IInvokedMethodListener
 */
public interface IInvokedMethodListenerEx extends IInvokedMethodListener {

}
