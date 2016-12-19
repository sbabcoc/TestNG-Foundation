package com.nordstrom.test_automation.ui_tools.testng_foundation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;

import org.testng.ITestNGListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.xml.XmlSuite.FailurePolicy;

public class ExecutionFlowControllerTest {
	
	@Test
	public void testAttributeHandOff() {
		
		ExecutionFlowController efc = new ExecutionFlowController();
		TestListenerAdapter tla = new TestListenerAdapter();
		
		TestNG testNG = new TestNG();
		testNG.setTestClasses(new Class[]{HappyPathClass.class});
		testNG.addListener((ITestNGListener) efc);
		testNG.addListener((ITestNGListener) tla);
		testNG.run();
		
		assertEquals(tla.getFailedTests().size(), 0, "Unexpected test method failure");
		assertEquals(tla.getConfigurationFailures().size(), 0, "Unexpected configuration method failure");
		
		assertEquals(tla.getPassedTests().size(), 2, "Incorrect passed test count");
		assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
		assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
		
		assertEquals(HappyPathClass.fromBefore, HappyPathClass.VALUE, "Incorrect [before] value");
		assertEquals(HappyPathClass.fromMethod, HappyPathClass.VALUE, "Incorrect [method] value");
		assertEquals(HappyPathClass.fromAfter, HappyPathClass.VALUE, "Incorrect [after] value");
		
	}
	
	@Test
	public void testSkipFromBefore() {
		
		ExecutionFlowController efc = new ExecutionFlowController();
		TestListenerAdapter tla = new TestListenerAdapter();
		
		TestNG testNG = new TestNG();
		testNG.setTestClasses(new Class[]{SkipFromBefore.class});
		testNG.addListener((ITestNGListener) efc);
		testNG.addListener((ITestNGListener) tla);
		testNG.setConfigFailurePolicy(FailurePolicy.CONTINUE);
		testNG.run();
		
		assertEquals(tla.getFailedTests().size(), 0, "Unexpected test method failure");
		assertEquals(tla.getConfigurationFailures().size(), 0, "Unexpected configuration method failure");
		
		assertEquals(tla.getPassedTests().size(), 1, "Incorrect passed test count");
		assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
		assertEquals(tla.getConfigurationSkips().size(), 1, "Incorrect configuration skip count");
		assertEquals(tla.getSkippedTests().size(), 1, "Incorrect skipped test count");
		ITestResult methodResult = tla.getSkippedTests().get(0);
		assertEquals(methodResult.getName(), "testMethod", "Incorrect skipped test name");
		
		assertEquals(SkipFromBefore.fromBefore, SkipFromBefore.VALUE, "Incorrect [before] value");
		assertEquals(methodResult.getAttribute(SkipFromBefore.ATTRIBUTE), SkipFromBefore.VALUE, "Incorrect [method] value");
		assertEquals(SkipFromBefore.fromAfter, SkipFromBefore.VALUE, "Incorrect [after] value");
		
	}
	
	@Test
	public void testSkipFromMethod() {
		
		ExecutionFlowController efc = new ExecutionFlowController();
		TestListenerAdapter tla = new TestListenerAdapter();
		
		TestNG testNG = new TestNG();
		testNG.setTestClasses(new Class[]{SkipFromMethod.class});
		testNG.addListener((ITestNGListener) efc);
		testNG.addListener((ITestNGListener) tla);
		testNG.run();
		
		assertEquals(tla.getFailedTests().size(), 0, "Unexpected test method failure");
		assertEquals(tla.getConfigurationFailures().size(), 0, "Unexpected configuration method failure");
		
		assertEquals(tla.getPassedTests().size(), 1, "Incorrect passed test count");
		assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
		assertEquals(tla.getSkippedTests().size(), 1, "Incorrect skipped test count");
		assertEquals(tla.getSkippedTests().get(0).getName(), "testMethod", "Incorrect skipped test name");
		
		assertEquals(SkipFromMethod.fromBefore, SkipFromMethod.VALUE, "Incorrect [before] value");
		assertEquals(SkipFromMethod.fromMethod, SkipFromMethod.VALUE, "Incorrect [method] value");
		assertEquals(SkipFromMethod.fromAfter, SkipFromMethod.VALUE, "Incorrect [after] value");
		
	}
	
	private static class HappyPathClass {
		
		protected static final String ATTRIBUTE = "ATTRIBUTE";
		protected static final String VALUE = "VALUE";
		
		protected static String fromBefore;
		protected static ITestResult beforeResult;
		protected static String fromMethod;
		protected static ITestResult methodResult;
		protected static String fromAfter;
		protected static ITestResult afterResult;
		
		@BeforeMethod
		public void beforeMethod(Method method) {
			if ("testMethod".equals(method.getName())) {
				fromBefore = VALUE;
				beforeResult = Reporter.getCurrentTestResult();
				beforeResult.setAttribute(ATTRIBUTE, fromBefore);
			}
		}
		
		@Test
		public void testMethod() {
			methodResult = Reporter.getCurrentTestResult();
			fromMethod = (String) methodResult.getAttribute(ATTRIBUTE);
			assertTrue(true);
		}
		
		@Test
		public void secondTest() {
			assertTrue(true);
		}
		
		@AfterMethod
		public void afterMethod(Method method) {
			if ("testMethod".equals(method.getName())) {
				afterResult = Reporter.getCurrentTestResult();
				fromAfter = (String) afterResult.getAttribute(ATTRIBUTE);
			}
		}
	}
	
	private static class SkipFromBefore {
		
		protected static final String ATTRIBUTE = "ATTRIBUTE";
		protected static final String VALUE = "VALUE";
		
		protected static String fromBefore;
		protected static ITestResult beforeResult;
		protected static String fromMethod;
		protected static ITestResult methodResult;
		protected static String fromAfter;
		protected static ITestResult afterResult;
		
		@BeforeMethod
		public void beforeMethod(Method method) {
			if ("testMethod".equals(method.getName())) {
				fromBefore = VALUE;
				beforeResult = Reporter.getCurrentTestResult();
				beforeResult.setAttribute(ATTRIBUTE, fromBefore);
				throw new SkipException("Skip from [before]");
			}
		}
		
		@Test
		public void testMethod() {
			methodResult = Reporter.getCurrentTestResult();
			fromMethod = (String) methodResult.getAttribute(ATTRIBUTE);
			assertTrue(true);
		}
		
		@Test
		public void secondTest() {
			assertTrue(true);
		}
		
		@AfterMethod
		public void afterMethod(Method method) {
			if ("testMethod".equals(method.getName())) {
				afterResult = Reporter.getCurrentTestResult();
				fromAfter = (String) afterResult.getAttribute(ATTRIBUTE);
			}
		}
	}

	private static class SkipFromMethod {
		
		protected static final String ATTRIBUTE = "ATTRIBUTE";
		protected static final String VALUE = "VALUE";
		
		protected static String fromBefore;
		protected static ITestResult beforeResult;
		protected static String fromMethod;
		protected static ITestResult methodResult;
		protected static String fromAfter;
		protected static ITestResult afterResult;
		
		@BeforeMethod
		public void beforeMethod(Method method) {
			if ("testMethod".equals(method.getName())) {
				fromBefore = VALUE;
				beforeResult = Reporter.getCurrentTestResult();
				beforeResult.setAttribute(ATTRIBUTE, fromBefore);
			}
		}
		
		@Test
		public void testMethod() {
			methodResult = Reporter.getCurrentTestResult();
			fromMethod = (String) methodResult.getAttribute(ATTRIBUTE);
			throw new SkipException("Skip from [method]");
		}
		
		@Test
		public void secondTest() {
			assertTrue(true);
		}
		
		@AfterMethod
		public void afterMethod(Method method) {
			if ("testMethod".equals(method.getName())) {
				afterResult = Reporter.getCurrentTestResult();
				fromAfter = (String) afterResult.getAttribute(ATTRIBUTE);
			}
		}
	}

}
