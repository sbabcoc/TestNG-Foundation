package com.nordstrom.automation.testng;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.testng.IClassListener;
import org.testng.IConfigurationListener;
import org.testng.IConfigurationListener2;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.collections.Lists;
import org.testng.collections.Sets;

/**
 * This TestNG listener enables the addition of other listeners at runtime and guarantees the order in which they're
 * invoked. This is similar in behavior to a JUnit rule chain.
 */
public class ListenerChain
		implements ISuiteListener, ITestListener, IClassListener, IInvokedMethodListener, IConfigurationListener2 {
	
	private Set<Class<? extends ITestNGListener>> allListeners = 
			Collections.synchronizedSet(Sets.<Class<? extends ITestNGListener>>newHashSet());
	
	private List<ISuiteListener> suiteListeners = Collections.synchronizedList(Lists.<ISuiteListener>newArrayList());
	private List<ITestListener> testListeners = Collections.synchronizedList(Lists.<ITestListener>newArrayList());
	private List<IClassListener> classListeners = Collections.synchronizedList(Lists.<IClassListener>newArrayList());
	private List<IInvokedMethodListener> methodListeners = Collections.synchronizedList(Lists.<IInvokedMethodListener>newArrayList());
	private List<IConfigurationListener2> configListeners = Collections.synchronizedList(Lists.<IConfigurationListener2>newArrayList());

	/** 
	 * [ISuiteListener]
	 * This method is invoked before the SuiteRunner starts.
	 * 
	 * @param suite current test suite
	 */
	@Override
	public void onStart(ISuite suite) {
		Set<ListenerChainable> chainables = Sets.newHashSet();
		for (ITestNGMethod method : suite.getAllMethods()) {
			Object instance = method.getInstance();
			if (instance instanceof ListenerChainable) {
				chainables.add((ListenerChainable) instance);
			}
		}
		
		for (ListenerChainable chainable : chainables) {
			chainable.attachListeners(this);
		}
		
		for (int i = suiteListeners.size() - 1; i > -1; i--) {
			suiteListeners.get(i).onStart(suite);
		}
	}

	/**
	 * [ISuiteListener]
	 * This method is invoked after the SuiteRunner has run all
	 * the test suites.
	 * 
	 * @param suite current test suite
	 */
	@Override
	public void onFinish(ISuite suite) {
		for (ISuiteListener suiteListener : suiteListeners) {
			suiteListener.onFinish(suite);
		}
	}

	/**
	 * [ITestListener]
	 * Invoked each time before a test will be invoked.
	 * The {@code ITestResult} is only partially filled with the references to
	 * class, method, start millis and status.
	 *
	 * @param result the partially filled {@code ITestResult}
	 * @see ITestResult#STARTED
	 */
	@Override
	public void onTestStart(ITestResult result) {
		for (int i = testListeners.size() - 1; i > -1; i--) {
			testListeners.get(i).onTestStart(result);
		}
	}

	/**
	 * [ITestListener]
	 * Invoked each time a test succeeds.
	 *
	 * @param result {@code ITestResult} containing information about the run test
	 * @see ITestResult#SUCCESS
	 */
	@Override
	public void onTestSuccess(ITestResult result) {
		for (ITestListener testListener : testListeners) {
			testListener.onTestSuccess(result);
		}
	}

	/**
	 * [ITestListener]
	 * Invoked each time a test fails.
	 *
	 * @param result {@code ITestResult} containing information about the run test
	 * @see ITestResult#FAILURE
	 */
	@Override
	public void onTestFailure(ITestResult result) {
		for (ITestListener testListener : testListeners) {
			testListener.onTestFailure(result);
		}
	}

	/**
	 * [ITestListener]
	 * Invoked each time a test is skipped.
	 *
	 * @param result {@code ITestResult} containing information about the run test
	 * @see ITestResult#SKIP
	 */
	@Override
	public void onTestSkipped(ITestResult result) {
		for (ITestListener testListener : testListeners) {
			testListener.onTestSkipped(result);
		}
	}

	/**
	 * [ITestListener]
	 * Invoked each time a method fails but has been annotated with
	 * successPercentage and this failure still keeps it within the
	 * success percentage requested.
	 *
	 * @param result {@code ITestResult} containing information about the run test
	 * @see ITestResult#SUCCESS_PERCENTAGE_FAILURE
	 */
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		for (ITestListener testListener : testListeners) {
			testListener.onTestFailedButWithinSuccessPercentage(result);
		}
	}

	/**
	 * [ITestListener]
	 * Invoked after the test class is instantiated and before
	 * any configuration method is called.
	 * 
	 * @param context context for the test run
	 */
	@Override
	public void onStart(ITestContext context) {
		for (int i = testListeners.size() - 1; i > -1; i--) {
			testListeners.get(i).onStart(context);
		}
	}

	/**
	 * [ITestListener]
	 * Invoked after all the tests have run and all their
	 * Configuration methods have been called.
	 * 
	 * @param context context for the test run
	 */
	@Override
	public void onFinish(ITestContext context) {
		for (ITestListener testListener : testListeners) {
			testListener.onFinish(context);
		}
	}

	/**
	 * [IClassListener]
	 * Invoked after the test class is instantiated and before
	 * {@link org.testng.annotations.BeforeClass &#64;BeforeClass} 
	 * configuration methods are called.
	 * 
	 * @param testClass TestNG representation for the current test class
	 */
	@Override
	public void onBeforeClass(ITestClass testClass) {
		for (int i = classListeners.size() - 1; i > -1; i--) {
			classListeners.get(i).onBeforeClass(testClass);
		}
	}

	/**
	 * [IClassListener]
	 * Invoked after all of the test methods of the test class have been invoked
	 * and before {@link org.testng.annotations.AfterClass &#64;AfterClass}
	 * configuration methods are called.
	 * 
	 * @param testClass TestNG representation for the current test class
	 */
	@Override
	public void onAfterClass(ITestClass testClass) {
		for (IClassListener classListener : classListeners) {
			classListener.onAfterClass(testClass);
		}
	}

	/**
	 * [IInvokedMethodListener]
	 * Invoked before each test or configuration method is invoked by TestNG
	 * 
	 * @param method TestNG representation of the method that's about to be invoked
	 * @param testResult test result object for the method that's about to be invoked
	 */
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		for (int i = methodListeners.size() - 1; i > -1; i--) {
			methodListeners.get(i).beforeInvocation(method, testResult);
		}
	}

	/**
	 * [IInvokedMethodListener]
	 * Invoked after each test or configuration method is invoked by TestNG
	 * 
	 * @param method TestNG representation of the method that's just been invoked
	 * @param testResult test result object for the method that's just been invoked
	 */
	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		for (IInvokedMethodListener methodListener : methodListeners) {
			methodListener.afterInvocation(method, testResult);
		}
	}
	
	/**
	 * [IConfigurationListener]
	 * Invoked whenever a configuration method succeeded.
	 * 
	 * @param itr test result object for the associated configuration method
	 */
	@Override
	public void onConfigurationSuccess(ITestResult itr) {
		for (IConfigurationListener configListener : configListeners) {
			configListener.onConfigurationSuccess(itr);
		}
	}

	/**
	 * [IConfigurationListener]
	 * Invoked whenever a configuration method failed.
	 * 
	 * @param itr test result object for the associated configuration method
	 */
	@Override
	public void onConfigurationFailure(ITestResult itr) {
		for (IConfigurationListener configListener : configListeners) {
			configListener.onConfigurationFailure(itr);
		}
	}

	/**
	 * [IConfigurationListener]
	 * Invoked whenever a configuration method was skipped.
	 * 
	 * @param itr test result object for the associated configuration method
	 */
	@Override
	public void onConfigurationSkip(ITestResult itr) {
		for (IConfigurationListener configListener : configListeners) {
			configListener.onConfigurationSkip(itr);
		}
	}

	/**
	 * [IConfigurationListener2]
	 * Invoked before a configuration method is invoked.
	 * 
	 * @param tr test result object for the associated configuration method
	 */
	@Override
	public void beforeConfiguration(ITestResult tr) {
		for (int i = configListeners.size() - 1; i > -1; i--) {
			configListeners.get(i).beforeConfiguration(tr);
		}
	}

	/**
	 * Wrap the current listener chain with an instance of the specified listener class.<br>
	 * <b>NOTE</b>: The order in which listener methods are invoked is determined by the
	 * order in which listener objects are added to the chain. Listener <i>before</i> methods
	 * are invoked in last-added-first-called order. Listener <i>after</i> methods are invoked
	 * in first-added-first-called order.<br>
	 * <b>NOTE</b>: Only one instance of any given listener class will be included in the chain.
	 * 
	 * @param listenerTyp listener class to add to the chain
	 * @return {@code this} (the target instance of {@link ListenerChain})
	 */
	public ListenerChain around(Class<? extends ITestNGListener> listenerTyp) {
		if (!allListeners.contains(listenerTyp)) {
			allListeners.add(listenerTyp);
			try {
				ITestNGListener listenerObj = listenerTyp.newInstance();
				if (listenerObj instanceof ISuiteListener)
					suiteListeners.add((ISuiteListener) listenerObj);
				if (listenerObj instanceof ITestListener)
					testListeners.add((ITestListener) listenerObj);
				if (listenerObj instanceof IClassListener)
					classListeners.add((IClassListener) listenerObj);
				if (listenerObj instanceof IInvokedMethodListener)
					methodListeners.add((IInvokedMethodListener) listenerObj);
				if (listenerObj instanceof IConfigurationListener2)
					configListeners.add((IConfigurationListener2) listenerObj);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException("Unable to instantiate listener: " + listenerTyp.getName(), e);
			}
		}
		return this;
	}

}
