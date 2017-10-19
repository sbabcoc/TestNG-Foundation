package com.nordstrom.automation.testng;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.Optional;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * This test class is driven by {@link ListenerChainTest}. Attempts to run the tests in this class without specifying
 * a group will typically fail, because this results in execution of tests that are intended to fail "unexpectedly".
 */
@Test
@Listeners
@LinkedListeners({ChainedListener.class, ExecutionFlowController.class})
class ListenerChainTestCases {
    
    private int invokeCount;
    
    @Test
    public ListenerChainTestCases() {
    }
    
    @DataProvider(name = "data")
    public Object[][] dataProvider() {
        return new Object[][] {{"data"}};
    }
    
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
    
    @Test(groups = {"happyPath"}, dataProvider = "data")
    public void happyPath(String data) {
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
    
    @Test(groups = {"happyPath"})
    public void testAttachedListener() {
        ITestResult result = Reporter.getCurrentTestResult();
        Optional<ChainedListener> optional = ListenerChain.getAttachedListener(result, ChainedListener.class);
        assertTrue(optional.isPresent());
    }
    
}