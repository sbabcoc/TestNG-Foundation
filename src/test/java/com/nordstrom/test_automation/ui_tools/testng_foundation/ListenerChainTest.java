package com.nordstrom.test_automation.ui_tools.testng_foundation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import org.testng.IClassListener;
import org.testng.IConfigurationListener2;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.collections.Sets;

public class ListenerChainTest {

	@Test
	public void verifyHappyPath() {
		
		ExecutionFlowController efc = new ExecutionFlowController();
		ListenerChain lc = new ListenerChain();
		TestListenerAdapter tla = new TestListenerAdapter();
		
		TestNG testNG = new TestNG();
		testNG.setTestClasses(new Class[]{ListenerChainerClass.class});
		testNG.addListener((ITestNGListener) efc);
		testNG.addListener((ITestNGListener) lc);
		testNG.addListener((ITestNGListener) tla);
		testNG.setGroups("happyPath");
		testNG.run();
		
		assertEquals(tla.getPassedTests().size(), 1, "Incorrect passed test count");
		assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
		assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
		assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
		assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
		assertEquals(tla.getConfigurationSkips().size(), 0, "Incorrect configuration method skip count");
		
		assertTrue(ChainedListener.configSuccess.contains("beforeSuccess"));
		assertTrue(ChainedListener.configSuccess.contains("afterSuccess"));
		assertTrue(ChainedListener.beforeConfig.contains("beforeSuccess"));
		assertTrue(ChainedListener.beforeConfig.contains("afterSuccess"));
		
		assertTrue(ChainedListener.beforeMethodBefore.contains("beforeSuccess"));
		assertTrue(ChainedListener.beforeMethodAfter.contains("beforeSuccess"));
		assertTrue(ChainedListener.testMethodBefore.contains("happyPath"));
		assertTrue(ChainedListener.testMethodAfter.contains("happyPath"));
		assertTrue(ChainedListener.afterMethodBefore.contains("afterSuccess"));
		assertTrue(ChainedListener.afterMethodAfter.contains("afterSuccess"));
		
		assertTrue(ChainedListener.testStarted.contains("happyPath"));
		assertTrue(ChainedListener.testSuccess.contains("happyPath"));
		
		assertTrue(ChainedListener.beforeClass.contains("ListenerChainerClass"));
		assertTrue(ChainedListener.afterClass.contains("ListenerChainerClass"));
		
		assertTrue(ChainedListener.testsBegun.contains("Command line test"));
		assertTrue(ChainedListener.testsEnded.contains("Command line test"));
		
		assertTrue(ChainedListener.suiteBegun.contains("Command line suite"));
		assertTrue(ChainedListener.suiteEnded.contains("Command line suite"));
		
	}
	
	@Test
	public void verifytestFailed() {
		
		ExecutionFlowController efc = new ExecutionFlowController();
		ListenerChain lc = new ListenerChain();
		TestListenerAdapter tla = new TestListenerAdapter();
		
		TestNG testNG = new TestNG();
		testNG.setTestClasses(new Class[]{ListenerChainerClass.class});
		testNG.addListener((ITestNGListener) efc);
		testNG.addListener((ITestNGListener) lc);
		testNG.addListener((ITestNGListener) tla);
		testNG.setGroups("testFailed");
		testNG.run();
		
		assertEquals(tla.getPassedTests().size(), 0, "Incorrect passed test count");
		assertEquals(tla.getFailedTests().size(), 1, "Incorrect failed test count");
		assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
		assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
		assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
		assertEquals(tla.getConfigurationSkips().size(), 0, "Incorrect configuration method skip count");
		
		assertTrue(ChainedListener.configSuccess.contains("beforeSuccess"));
		assertTrue(ChainedListener.configSuccess.contains("afterSuccess"));
		assertTrue(ChainedListener.beforeConfig.contains("beforeSuccess"));
		assertTrue(ChainedListener.beforeConfig.contains("afterSuccess"));
		
		assertTrue(ChainedListener.beforeMethodBefore.contains("beforeSuccess"));
		assertTrue(ChainedListener.beforeMethodAfter.contains("beforeSuccess"));
		assertTrue(ChainedListener.testMethodBefore.contains("testFailed"));
		assertTrue(ChainedListener.testMethodAfter.contains("testFailed"));
		assertTrue(ChainedListener.afterMethodBefore.contains("afterSuccess"));
		assertTrue(ChainedListener.afterMethodAfter.contains("afterSuccess"));
		
		assertTrue(ChainedListener.testStarted.contains("testFailed"));
		assertTrue(ChainedListener.testFailure.contains("testFailed"));
		
	}
	
	@Test
	public void verifyTestSkipped() {
		
		ExecutionFlowController efc = new ExecutionFlowController();
		ListenerChain lc = new ListenerChain();
		TestListenerAdapter tla = new TestListenerAdapter();
		
		TestNG testNG = new TestNG();
		testNG.setTestClasses(new Class[]{ListenerChainerClass.class});
		testNG.addListener((ITestNGListener) efc);
		testNG.addListener((ITestNGListener) lc);
		testNG.addListener((ITestNGListener) tla);
		testNG.setGroups("testSkipped");
		testNG.run();
		
		assertEquals(tla.getPassedTests().size(), 0, "Incorrect passed test count");
		assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
		assertEquals(tla.getSkippedTests().size(), 1, "Incorrect skipped test count");
		assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
		assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
		assertEquals(tla.getConfigurationSkips().size(), 0, "Incorrect configuration method skip count");
		
		assertTrue(ChainedListener.configSuccess.contains("beforeSuccess"));
		assertTrue(ChainedListener.configSuccess.contains("afterSuccess"));
		assertTrue(ChainedListener.beforeConfig.contains("beforeSuccess"));
		assertTrue(ChainedListener.beforeConfig.contains("afterSuccess"));
		
		assertTrue(ChainedListener.beforeMethodBefore.contains("beforeSuccess"));
		assertTrue(ChainedListener.beforeMethodAfter.contains("beforeSuccess"));
		assertTrue(ChainedListener.testMethodBefore.contains("testSkipped"));
		assertTrue(ChainedListener.testMethodAfter.contains("testSkipped"));
		assertTrue(ChainedListener.afterMethodBefore.contains("afterSuccess"));
		assertTrue(ChainedListener.afterMethodAfter.contains("afterSuccess"));
		
		assertTrue(ChainedListener.testStarted.contains("testSkipped"));
		assertTrue(ChainedListener.testSkipped.contains("testSkipped"));
		
	}
	
	@Test
	public void verifyFailAndPass() {
		
		ExecutionFlowController efc = new ExecutionFlowController();
		ListenerChain lc = new ListenerChain();
		TestListenerAdapter tla = new TestListenerAdapter();
		
		TestNG testNG = new TestNG();
		testNG.setTestClasses(new Class[]{ListenerChainerClass.class});
		testNG.addListener((ITestNGListener) efc);
		testNG.addListener((ITestNGListener) lc);
		testNG.addListener((ITestNGListener) tla);
		testNG.setGroups("failAndPass");
		testNG.run();
		
		assertEquals(tla.getPassedTests().size(), 3, "Incorrect passed test count");
		assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
		assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
		assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 1, "Incorrect curve-graded success count");
		assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
		assertEquals(tla.getConfigurationSkips().size(), 0, "Incorrect configuration method skip count");
		
		assertTrue(ChainedListener.configSuccess.contains("beforeSuccess"));
		assertTrue(ChainedListener.configSuccess.contains("afterSuccess"));
		assertTrue(ChainedListener.beforeConfig.contains("beforeSuccess"));
		assertTrue(ChainedListener.beforeConfig.contains("afterSuccess"));
		
		assertTrue(ChainedListener.beforeMethodBefore.contains("beforeSuccess"));
		assertTrue(ChainedListener.beforeMethodAfter.contains("beforeSuccess"));
		assertTrue(ChainedListener.testMethodBefore.contains("failAndPass"));
		assertTrue(ChainedListener.testMethodAfter.contains("failAndPass"));
		assertTrue(ChainedListener.afterMethodBefore.contains("afterSuccess"));
		assertTrue(ChainedListener.afterMethodAfter.contains("afterSuccess"));
		
		assertTrue(ChainedListener.testStarted.contains("failAndPass"));
		assertTrue(ChainedListener.testCurved.contains("failAndPass"));
		
	}
	
	@Test
	public void verifyBeforeFailed() {
		
		ExecutionFlowController efc = new ExecutionFlowController();
		ListenerChain lc = new ListenerChain();
		TestListenerAdapter tla = new TestListenerAdapter();
		
		TestNG testNG = new TestNG();
		testNG.setTestClasses(new Class[]{ListenerChainerClass.class});
		testNG.addListener((ITestNGListener) efc);
		testNG.addListener((ITestNGListener) lc);
		testNG.addListener((ITestNGListener) tla);
		testNG.setGroups("beforeFailed");
		testNG.run();
		
		assertEquals(tla.getPassedTests().size(), 0, "Incorrect passed test count");
		assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
		assertEquals(tla.getSkippedTests().size(), 1, "Incorrect skipped test count");
		assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
		assertEquals(tla.getConfigurationFailures().size(), 1, "Incorrect configuration method failure count");
		assertEquals(tla.getConfigurationSkips().size(), 1, "Incorrect configuration method skip count");
		
		assertTrue(ChainedListener.configFailure.contains("beforeFailure"));
		assertTrue(ChainedListener.configSkipped.contains("afterSuccess"));
		assertTrue(ChainedListener.beforeConfig.contains("beforeFailure"));
		
		assertTrue(ChainedListener.beforeMethodBefore.contains("beforeFailure"));
		assertTrue(ChainedListener.beforeMethodAfter.contains("beforeFailure"));
		assertTrue(ChainedListener.testMethodBefore.contains("skipBeforeFailed"));
		assertTrue(ChainedListener.testMethodAfter.contains("skipBeforeFailed"));
		
		assertTrue(ChainedListener.testStarted.contains("skipBeforeFailed"));
		assertTrue(ChainedListener.testSkipped.contains("skipBeforeFailed"));
		
	}
	
	@Test
	public void verifyBeforeSkipped() {
		
		ExecutionFlowController efc = new ExecutionFlowController();
		ListenerChain lc = new ListenerChain();
		TestListenerAdapter tla = new TestListenerAdapter();
		
		TestNG testNG = new TestNG();
		testNG.setTestClasses(new Class[]{ListenerChainerClass.class});
		testNG.addListener((ITestNGListener) efc);
		testNG.addListener((ITestNGListener) lc);
		testNG.addListener((ITestNGListener) tla);
		testNG.setGroups("beforeSkipped");
		testNG.run();
		
		assertEquals(tla.getPassedTests().size(), 0, "Incorrect passed test count");
		assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
		assertEquals(tla.getSkippedTests().size(), 1, "Incorrect skipped test count");
		assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
		assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
		assertEquals(tla.getConfigurationSkips().size(), 2, "Incorrect configuration method skip count");
		
		assertTrue(ChainedListener.configSkipped.contains("beforeSkipped"));
		assertTrue(ChainedListener.configSkipped.contains("afterSuccess"));
		assertTrue(ChainedListener.beforeConfig.contains("beforeSkipped"));
		
		assertTrue(ChainedListener.beforeMethodBefore.contains("beforeSkipped"));
		assertTrue(ChainedListener.beforeMethodAfter.contains("beforeSkipped"));
		assertTrue(ChainedListener.testMethodBefore.contains("skipBeforeSkipped"));
		assertTrue(ChainedListener.testMethodAfter.contains("skipBeforeSkipped"));
		
		assertTrue(ChainedListener.testStarted.contains("skipBeforeSkipped"));
		assertTrue(ChainedListener.testSkipped.contains("skipBeforeSkipped"));
		
	}
	
	@Test
	public void verifyAfterFailed() {
		
		ExecutionFlowController efc = new ExecutionFlowController();
		ListenerChain lc = new ListenerChain();
		TestListenerAdapter tla = new TestListenerAdapter();
		
		TestNG testNG = new TestNG();
		testNG.setTestClasses(new Class[]{ListenerChainerClass.class});
		testNG.addListener((ITestNGListener) efc);
		testNG.addListener((ITestNGListener) lc);
		testNG.addListener((ITestNGListener) tla);
		testNG.setGroups("afterFailed");
		testNG.run();
		
		assertEquals(tla.getPassedTests().size(), 1, "Incorrect passed test count");
		assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
		assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
		assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
		assertEquals(tla.getConfigurationFailures().size(), 1, "Incorrect configuration method failure count");
		assertEquals(tla.getConfigurationSkips().size(), 0, "Incorrect configuration method skip count");
		
		assertTrue(ChainedListener.configSuccess.contains("beforeSuccess"));
		assertTrue(ChainedListener.configFailure.contains("afterFailure"));
		assertTrue(ChainedListener.beforeConfig.contains("beforeSuccess"));
		assertTrue(ChainedListener.beforeConfig.contains("afterFailure"));
		
		assertTrue(ChainedListener.beforeMethodBefore.contains("beforeSuccess"));
		assertTrue(ChainedListener.beforeMethodAfter.contains("beforeSuccess"));
		assertTrue(ChainedListener.testMethodBefore.contains("testAfterFailed"));
		assertTrue(ChainedListener.testMethodAfter.contains("testAfterFailed"));
		assertTrue(ChainedListener.afterMethodBefore.contains("afterFailure"));
		assertTrue(ChainedListener.afterMethodAfter.contains("afterFailure"));
		
		assertTrue(ChainedListener.testStarted.contains("testAfterFailed"));
		assertTrue(ChainedListener.testSuccess.contains("testAfterFailed"));
		
	}
	
	@Test
	public void verifyAfterSkipped() {
		
		ExecutionFlowController efc = new ExecutionFlowController();
		ListenerChain lc = new ListenerChain();
		TestListenerAdapter tla = new TestListenerAdapter();
		
		TestNG testNG = new TestNG();
		testNG.setTestClasses(new Class[]{ListenerChainerClass.class});
		testNG.addListener((ITestNGListener) efc);
		testNG.addListener((ITestNGListener) lc);
		testNG.addListener((ITestNGListener) tla);
		testNG.setGroups("afterSkipped");
		testNG.run();
		
		assertEquals(tla.getPassedTests().size(), 1, "Incorrect passed test count");
		assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
		assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
		assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
		assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
		assertEquals(tla.getConfigurationSkips().size(), 1, "Incorrect configuration method skip count");
		
		assertTrue(ChainedListener.configSuccess.contains("beforeSuccess"));
		assertTrue(ChainedListener.configSkipped.contains("afterSkipped"));
		assertTrue(ChainedListener.beforeConfig.contains("beforeSuccess"));
		assertTrue(ChainedListener.beforeConfig.contains("afterSkipped"));
		
		assertTrue(ChainedListener.beforeMethodBefore.contains("beforeSuccess"));
		assertTrue(ChainedListener.beforeMethodAfter.contains("beforeSuccess"));
		assertTrue(ChainedListener.testMethodBefore.contains("testAfterSkipped"));
		assertTrue(ChainedListener.testMethodAfter.contains("testAfterSkipped"));
		assertTrue(ChainedListener.afterMethodBefore.contains("afterSkipped"));
		assertTrue(ChainedListener.afterMethodAfter.contains("afterSkipped"));
		
		assertTrue(ChainedListener.testStarted.contains("testAfterSkipped"));
		assertTrue(ChainedListener.testSuccess.contains("testAfterSkipped"));
		
	}
	
	private static class ListenerChainerClass implements ListenerChainable {
		
		private int invokeCount;
		
		@BeforeMethod(groups = {"happyPath", "testFailed", "testSkipped", "failAndPass", "afterFailed", "afterSkipped"})
		public void beforeSuccess(Method method) {
			System.out.println("beforeSuccess");
		}
		
		@BeforeMethod(groups = {"beforeFailed"})
		public void beforeFailure(Method method) {
			System.out.println("beforeFailure");
			fail("beforeFailure");
		}
		
		@BeforeMethod(groups = {"beforeSkipped"})
		public void beforeSkipped(Method method) {
			System.out.println("beforeSkipped");
			throw new SkipException("beforeSkipped");
		}
		
		@Test(groups = {"happyPath"})
		public void happyPath() {
			System.out.println("happyPath");
			assertTrue(true);
		}
		
		@Test(groups = {"testFailed"})
		public void testFailed() {
			System.out.println("testFailed");
			fail("testFailed");
		}
		
		@Test(groups = {"testSkipped"})
		public void testSkipped() {
			System.out.println("testSkipped");
			throw new SkipException("testSkipped");
		}
		
		@Test(invocationCount = 4, successPercentage = 75, groups = {"failAndPass"})
		public void failAndPass() {
			System.out.println("failAndPass");
			assertTrue(invokeCount++ > 0);
		}
		
		@Test(groups = {"beforeFailed"})
		public void skipBeforeFailed() {
			System.out.println("skipBeforeFailed");
			assertTrue(true);
		}
		
		@Test(groups = {"beforeSkipped"})
		public void skipBeforeSkipped() {
			System.out.println("skipBeforeSkipped");
			assertTrue(true);
		}
		
		@Test(groups = {"afterFailed"})
		public void testAfterFailed() {
			System.out.println("testAfterFailed");
			assertTrue(true);
		}
		
		@Test(groups = {"afterSkipped"})
		public void testAfterSkipped() {
			System.out.println("testAfterSkipped");
			assertTrue(true);
		}
		
		@AfterMethod(groups = {"happyPath", "testFailed", "testSkipped", "failAndPass", "beforeFailed", "beforeSkipped"})
		public void afterSuccess(Method method) {
			System.out.println("afterSuccess");
		}
		
		@AfterMethod(groups = {"afterFailed"})
		public void afterFailure(Method method) {
			System.out.println("afterFailure");
			fail("afterFailure");
		}
		
		@AfterMethod(groups = {"afterSkipped"})
		public void afterSkipped(Method method) {
			System.out.println("afterSkipped");
			throw new SkipException("afterSkipped");
		}
		
		@Override
		public void attachListeners(ListenerChain listenerChain) {
			listenerChain.around(ChainedListener.class);
		}
		
	}
	
	public static class ChainedListener
			implements ISuiteListener, ITestListener, IClassListener, IInvokedMethodListener, IConfigurationListener2 {
		
		private static Set<String> configSuccess = Collections.synchronizedSet(Sets.<String>newHashSet());
		private static Set<String> configFailure = Collections.synchronizedSet(Sets.<String>newHashSet());
		private static Set<String> configSkipped = Collections.synchronizedSet(Sets.<String>newHashSet());
		private static Set<String> beforeConfig = Collections.synchronizedSet(Sets.<String>newHashSet());
		
		private static Set<String> beforeMethodBefore = Collections.synchronizedSet(Sets.<String>newHashSet());
		private static Set<String> beforeMethodAfter = Collections.synchronizedSet(Sets.<String>newHashSet());
		private static Set<String> testMethodBefore = Collections.synchronizedSet(Sets.<String>newHashSet());
		private static Set<String> testMethodAfter = Collections.synchronizedSet(Sets.<String>newHashSet());
		private static Set<String> afterMethodBefore = Collections.synchronizedSet(Sets.<String>newHashSet());
		private static Set<String> afterMethodAfter = Collections.synchronizedSet(Sets.<String>newHashSet());
		
		private static Set<String> beforeClass = Collections.synchronizedSet(Sets.<String>newHashSet());
		private static Set<String> afterClass = Collections.synchronizedSet(Sets.<String>newHashSet());
		
		private static Set<String> testStarted = Collections.synchronizedSet(Sets.<String>newHashSet());
		private static Set<String> testSuccess = Collections.synchronizedSet(Sets.<String>newHashSet());
		private static Set<String> testFailure = Collections.synchronizedSet(Sets.<String>newHashSet());
		private static Set<String> testSkipped = Collections.synchronizedSet(Sets.<String>newHashSet());
		private static Set<String> testCurved = Collections.synchronizedSet(Sets.<String>newHashSet());
		private static Set<String> testsBegun = Collections.synchronizedSet(Sets.<String>newHashSet());
		private static Set<String> testsEnded = Collections.synchronizedSet(Sets.<String>newHashSet());
		
		private static Set<String> suiteBegun = Collections.synchronizedSet(Sets.<String>newHashSet());
		private static Set<String> suiteEnded = Collections.synchronizedSet(Sets.<String>newHashSet());

		@Override
		public void onConfigurationSuccess(ITestResult itr) {
			configSuccess.add(itr.getName());
		}

		@Override
		public void onConfigurationFailure(ITestResult itr) {
			configFailure.add(itr.getName());
		}

		@Override
		public void onConfigurationSkip(ITestResult itr) {
			configSkipped.add(itr.getName());
		}

		@Override
		public void beforeConfiguration(ITestResult tr) {
			beforeConfig.add(tr.getName());
		}

		@Override
		public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		    if (method.getTestMethod().isBeforeMethodConfiguration()) {
				beforeMethodBefore.add(testResult.getName());
		    } else if (method.isTestMethod()) {
		    	testMethodBefore.add(testResult.getName());
			} else if (method.getTestMethod().isAfterMethodConfiguration()) {
				afterMethodBefore.add(testResult.getName());
			}
		}

		@Override
		public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		    if (method.getTestMethod().isBeforeMethodConfiguration()) {
				beforeMethodAfter.add(testResult.getName());
		    } else if (method.isTestMethod()) {
		    	testMethodAfter.add(testResult.getName());
			} else if (method.getTestMethod().isAfterMethodConfiguration()) {
				afterMethodAfter.add(testResult.getName());
			}
		}

		@Override
		public void onBeforeClass(ITestClass testClass) {
			beforeClass.add(testClass.getRealClass().getSimpleName());
		}

		@Override
		public void onAfterClass(ITestClass testClass) {
			afterClass.add(testClass.getRealClass().getSimpleName());
		}

		@Override
		public void onTestStart(ITestResult result) {
			testStarted.add(result.getName());
		}

		@Override
		public void onTestSuccess(ITestResult result) {
			testSuccess.add(result.getName());
		}

		@Override
		public void onTestFailure(ITestResult result) {
			testFailure.add(result.getName());
		}

		@Override
		public void onTestSkipped(ITestResult result) {
			testSkipped.add(result.getName());
		}

		@Override
		public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
			testCurved.add(result.getName());
		}

		@Override
		public void onStart(ITestContext context) {
			testsBegun.add(context.getName());
		}

		@Override
		public void onFinish(ITestContext context) {
			testsEnded.add(context.getName());
		}

		@Override
		public void onStart(ISuite suite) {
			suiteBegun.add(suite.getName());
		}

		@Override
		public void onFinish(ISuite suite) {
			suiteEnded.add(suite.getName());
		}
		
	}
	
}
