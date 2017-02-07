package com.nordstrom.automation.testng;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.ITestNGListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlSuite.FailurePolicy;

import com.nordstrom.automation.testng.ExecutionFlowController;

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
	
	@Test
	public void testMethodListenerExtension() {
		
		ExecutionFlowController efc = new ExecutionFlowController();
		TestListenerAdapter tla = new TestListenerAdapter();
		
		TestNG testNG = new TestNG();
		testNG.setTestClasses(new Class[]{MethodListenerExtension.class});
		testNG.addListener((ITestNGListener) efc);
		testNG.addListener((ITestNGListener) tla);
		testNG.run();
		
		assertEquals(tla.getFailedTests().size(), 0, "Unexpected test method failure");
		assertEquals(tla.getConfigurationFailures().size(), 0, "Unexpected configuration method failure");
		
		assertEquals(tla.getPassedTests().size(), 1, "Incorrect passed test count");
		assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
		assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
		
		assertTrue(MethodListenerExtension.beforeMethodBefore, "Incorrect [beforeMethod] 'before' value");
		assertTrue(MethodListenerExtension.testMethodBefore, "Incorrect [testMethod] 'before' value");
		assertTrue(MethodListenerExtension.afterMethodBefore, "Incorrect [afterMethod] 'before' value");
		assertTrue(MethodListenerExtension.beforeMethodAfter, "Incorrect [beforeMethod] 'after' value");
		assertTrue(MethodListenerExtension.testMethodAfter, "Incorrect [testMethod] 'after' value");
		assertTrue(MethodListenerExtension.afterMethodAfter, "Incorrect [afterMethod] 'after' value");
		
	}

}
