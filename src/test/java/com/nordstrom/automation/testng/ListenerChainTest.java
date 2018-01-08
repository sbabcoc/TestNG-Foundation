package com.nordstrom.automation.testng;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.testng.ITestNGListener;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Test;

public class ListenerChainTest {

    @Test
    public void verifyHappyPath() {
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ListenerChainTestCases.class, ListenerChainTestFactory.class});
        testNG.addListener((ITestNGListener) lc);
        testNG.addListener((ITestNGListener) tla);
        testNG.setGroups("happyPath");
        testNG.run();
        
        ChainedListener chainedListener = lc.getAttachedListener(ChainedListener.class).get();
        
        assertEquals(tla.getPassedTests().size(), 2, "Incorrect passed test count");
        assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
        assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
        assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
        assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
        assertEquals(tla.getConfigurationSkips().size(), 0, "Incorrect configuration method skip count");
        
        assertTrue(chainedListener.configSuccess.contains("beforeSuccess"));
        assertTrue(chainedListener.configSuccess.contains("afterSuccess"));
        assertTrue(chainedListener.beforeConfig.contains("beforeSuccess"));
        assertTrue(chainedListener.beforeConfig.contains("afterSuccess"));
        
        assertTrue(chainedListener.beforeMethodBefore.contains("beforeSuccess"));
        assertTrue(chainedListener.beforeMethodAfter.contains("beforeSuccess"));
        assertTrue(chainedListener.testMethodBefore.contains("happyPath"));
        assertTrue(chainedListener.testMethodAfter.contains("happyPath"));
        assertTrue(chainedListener.afterMethodBefore.contains("afterSuccess"));
        assertTrue(chainedListener.afterMethodAfter.contains("afterSuccess"));
        
        assertTrue(chainedListener.testStarted.contains("happyPath"));
        assertTrue(chainedListener.testSuccess.contains("happyPath"));
        
        assertTrue(chainedListener.beforeClass.contains("ListenerChainTestCases"));
        assertTrue(chainedListener.afterClass.contains("ListenerChainTestCases"));
        
        assertTrue(chainedListener.testsBegun.contains("Command line test"));
        assertTrue(chainedListener.testsEnded.contains("Command line test"));
        
        assertTrue(chainedListener.suiteBegun.contains("Command line suite"));
        assertTrue(chainedListener.suiteEnded.contains("Command line suite"));
        
        Set<String> expectTests = new HashSet<>(Arrays.asList("method: testSkipped",
                        "method: happyPath", "method: beforeSuccess", "method: beforeSkipped",
                        "method: skipBeforeFailed", "method: skipBeforeSkipped",
                        "method: testAfterSkipped", "method: productTest", "method: failAndPass",
                        "method: afterSuccess", "method: afterFailure",
                        "class: ListenerChainTestCases", "method: testAfterFailed",
                        "method: beforeFailure", "method: afterSkipped", "method: testFailed",
                        "method: testAttachedListeners"));
        Set<String> expectConfigs = new HashSet<>(Arrays.asList("method: afterSuccess",
                        "method: afterFailure", "method: beforeSuccess", "method: beforeFailure",
                        "method: beforeSkipped", "method: afterSkipped"));
        
        assertEquals(chainedListener.xformTest, expectTests);
        assertEquals(chainedListener.xformConfig, expectConfigs);
        assertTrue(chainedListener.xformProvider.contains("method: dataProvider"));
        assertTrue(chainedListener.xformFactory.contains("method: createInstances"));
        assertTrue(chainedListener.xformListeners.contains("class: ListenerChainTestCases"));
        
        assertTrue(chainedListener.interceptor.contains("Command line test"));
    }
    
    @Test
    public void verifyTestFailed() {
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ListenerChainTestCases.class});
        testNG.addListener((ITestNGListener) lc);
        testNG.addListener((ITestNGListener) tla);
        testNG.setGroups("testFailed");
        testNG.run();
        
        ChainedListener chainedListener = lc.getAttachedListener(ChainedListener.class).get();
        
        assertEquals(tla.getPassedTests().size(), 0, "Incorrect passed test count");
        assertEquals(tla.getFailedTests().size(), 1, "Incorrect failed test count");
        assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
        assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
        assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
        assertEquals(tla.getConfigurationSkips().size(), 0, "Incorrect configuration method skip count");
        
        assertTrue(chainedListener.configSuccess.contains("beforeSuccess"));
        assertTrue(chainedListener.configSuccess.contains("afterSuccess"));
        assertTrue(chainedListener.beforeConfig.contains("beforeSuccess"));
        assertTrue(chainedListener.beforeConfig.contains("afterSuccess"));
        
        assertTrue(chainedListener.beforeMethodBefore.contains("beforeSuccess"));
        assertTrue(chainedListener.beforeMethodAfter.contains("beforeSuccess"));
        assertTrue(chainedListener.testMethodBefore.contains("testFailed"));
        assertTrue(chainedListener.testMethodAfter.contains("testFailed"));
        assertTrue(chainedListener.afterMethodBefore.contains("afterSuccess"));
        assertTrue(chainedListener.afterMethodAfter.contains("afterSuccess"));
        
        assertTrue(chainedListener.testStarted.contains("testFailed"));
        assertTrue(chainedListener.testFailure.contains("testFailed"));
        
    }
    
    @Test
    public void verifyTestSkipped() {
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ListenerChainTestCases.class});
        testNG.addListener((ITestNGListener) lc);
        testNG.addListener((ITestNGListener) tla);
        testNG.setGroups("testSkipped");
        testNG.run();
        
        ChainedListener chainedListener = lc.getAttachedListener(ChainedListener.class).get();
        
        assertEquals(tla.getPassedTests().size(), 0, "Incorrect passed test count");
        assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
        assertEquals(tla.getSkippedTests().size(), 1, "Incorrect skipped test count");
        assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
        assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
        assertEquals(tla.getConfigurationSkips().size(), 0, "Incorrect configuration method skip count");
        
        assertTrue(chainedListener.configSuccess.contains("beforeSuccess"));
        assertTrue(chainedListener.configSuccess.contains("afterSuccess"));
        assertTrue(chainedListener.beforeConfig.contains("beforeSuccess"));
        assertTrue(chainedListener.beforeConfig.contains("afterSuccess"));
        
        assertTrue(chainedListener.beforeMethodBefore.contains("beforeSuccess"));
        assertTrue(chainedListener.beforeMethodAfter.contains("beforeSuccess"));
        assertTrue(chainedListener.testMethodBefore.contains("testSkipped"));
        assertTrue(chainedListener.testMethodAfter.contains("testSkipped"));
        assertTrue(chainedListener.afterMethodBefore.contains("afterSuccess"));
        assertTrue(chainedListener.afterMethodAfter.contains("afterSuccess"));
        
        assertTrue(chainedListener.testStarted.contains("testSkipped"));
        assertTrue(chainedListener.testSkipped.contains("testSkipped"));
        
    }
    
    @Test
    public void verifyFailAndPass() {
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ListenerChainTestCases.class});
        testNG.addListener((ITestNGListener) lc);
        testNG.addListener((ITestNGListener) tla);
        testNG.setGroups("failAndPass");
        testNG.run();
        
        ChainedListener chainedListener = lc.getAttachedListener(ChainedListener.class).get();
        
        assertEquals(tla.getPassedTests().size(), 3, "Incorrect passed test count");
        assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
        assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
        assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 1, "Incorrect curve-graded success count");
        assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
        assertEquals(tla.getConfigurationSkips().size(), 0, "Incorrect configuration method skip count");
        
        assertTrue(chainedListener.configSuccess.contains("beforeSuccess"));
        assertTrue(chainedListener.configSuccess.contains("afterSuccess"));
        assertTrue(chainedListener.beforeConfig.contains("beforeSuccess"));
        assertTrue(chainedListener.beforeConfig.contains("afterSuccess"));
        
        assertTrue(chainedListener.beforeMethodBefore.contains("beforeSuccess"));
        assertTrue(chainedListener.beforeMethodAfter.contains("beforeSuccess"));
        assertTrue(chainedListener.testMethodBefore.contains("failAndPass"));
        assertTrue(chainedListener.testMethodAfter.contains("failAndPass"));
        assertTrue(chainedListener.afterMethodBefore.contains("afterSuccess"));
        assertTrue(chainedListener.afterMethodAfter.contains("afterSuccess"));
        
        assertTrue(chainedListener.testStarted.contains("failAndPass"));
        assertTrue(chainedListener.testCurved.contains("failAndPass"));
        
    }
    
    @Test
    public void verifyBeforeFailed() {
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ListenerChainTestCases.class});
        testNG.addListener((ITestNGListener) lc);
        testNG.addListener((ITestNGListener) tla);
        testNG.setGroups("beforeFailed");
        testNG.run();
        
        ChainedListener chainedListener = lc.getAttachedListener(ChainedListener.class).get();
        
        assertEquals(tla.getPassedTests().size(), 0, "Incorrect passed test count");
        assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
        assertEquals(tla.getSkippedTests().size(), 1, "Incorrect skipped test count");
        assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
        assertEquals(tla.getConfigurationFailures().size(), 1, "Incorrect configuration method failure count");
        assertEquals(tla.getConfigurationSkips().size(), 1, "Incorrect configuration method skip count");
        
        assertTrue(chainedListener.configFailure.contains("beforeFailure"));
        assertTrue(chainedListener.configSkipped.contains("afterSuccess"));
        assertTrue(chainedListener.beforeConfig.contains("beforeFailure"));
        
        assertTrue(chainedListener.beforeMethodBefore.contains("beforeFailure"));
        assertTrue(chainedListener.beforeMethodAfter.contains("beforeFailure"));
        assertTrue(chainedListener.testMethodBefore.contains("skipBeforeFailed"));
        assertTrue(chainedListener.testMethodAfter.contains("skipBeforeFailed"));
        
        assertTrue(chainedListener.testStarted.contains("skipBeforeFailed"));
        assertTrue(chainedListener.testSkipped.contains("skipBeforeFailed"));
        
    }
    
    @Test
    public void verifyBeforeSkipped() {
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ListenerChainTestCases.class});
        testNG.addListener((ITestNGListener) lc);
        testNG.addListener((ITestNGListener) tla);
        testNG.setGroups("beforeSkipped");
        testNG.run();
        
        ChainedListener chainedListener = lc.getAttachedListener(ChainedListener.class).get();
        
        assertEquals(tla.getPassedTests().size(), 0, "Incorrect passed test count");
        assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
        assertEquals(tla.getSkippedTests().size(), 1, "Incorrect skipped test count");
        assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
        assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
        assertEquals(tla.getConfigurationSkips().size(), 2, "Incorrect configuration method skip count");
        
        assertTrue(chainedListener.configSkipped.contains("beforeSkipped"));
        assertTrue(chainedListener.configSkipped.contains("afterSuccess"));
        assertTrue(chainedListener.beforeConfig.contains("beforeSkipped"));
        
        assertTrue(chainedListener.beforeMethodBefore.contains("beforeSkipped"));
        assertTrue(chainedListener.beforeMethodAfter.contains("beforeSkipped"));
        assertTrue(chainedListener.testMethodBefore.contains("skipBeforeSkipped"));
        assertTrue(chainedListener.testMethodAfter.contains("skipBeforeSkipped"));
        
        assertTrue(chainedListener.testStarted.contains("skipBeforeSkipped"));
        assertTrue(chainedListener.testSkipped.contains("skipBeforeSkipped"));
        
    }
    
    @Test
    public void verifyAfterFailed() {
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ListenerChainTestCases.class});
        testNG.addListener((ITestNGListener) lc);
        testNG.addListener((ITestNGListener) tla);
        testNG.setGroups("afterFailed");
        testNG.run();
        
        ChainedListener chainedListener = lc.getAttachedListener(ChainedListener.class).get();
        
        assertEquals(tla.getPassedTests().size(), 1, "Incorrect passed test count");
        assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
        assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
        assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
        assertEquals(tla.getConfigurationFailures().size(), 1, "Incorrect configuration method failure count");
        assertEquals(tla.getConfigurationSkips().size(), 0, "Incorrect configuration method skip count");
        
        assertTrue(chainedListener.configSuccess.contains("beforeSuccess"));
        assertTrue(chainedListener.configFailure.contains("afterFailure"));
        assertTrue(chainedListener.beforeConfig.contains("beforeSuccess"));
        assertTrue(chainedListener.beforeConfig.contains("afterFailure"));
        
        assertTrue(chainedListener.beforeMethodBefore.contains("beforeSuccess"));
        assertTrue(chainedListener.beforeMethodAfter.contains("beforeSuccess"));
        assertTrue(chainedListener.testMethodBefore.contains("testAfterFailed"));
        assertTrue(chainedListener.testMethodAfter.contains("testAfterFailed"));
        assertTrue(chainedListener.afterMethodBefore.contains("afterFailure"));
        assertTrue(chainedListener.afterMethodAfter.contains("afterFailure"));
        
        assertTrue(chainedListener.testStarted.contains("testAfterFailed"));
        assertTrue(chainedListener.testSuccess.contains("testAfterFailed"));
        
    }
    
    @Test
    public void verifyAfterSkipped() {
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ListenerChainTestCases.class});
        testNG.addListener((ITestNGListener) lc);
        testNG.addListener((ITestNGListener) tla);
        testNG.setGroups("afterSkipped");
        testNG.run();
        
        ChainedListener chainedListener = lc.getAttachedListener(ChainedListener.class).get();
        
        assertEquals(tla.getPassedTests().size(), 1, "Incorrect passed test count");
        assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
        assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
        assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
        assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
        assertEquals(tla.getConfigurationSkips().size(), 1, "Incorrect configuration method skip count");
        
        assertTrue(chainedListener.configSuccess.contains("beforeSuccess"));
        assertTrue(chainedListener.configSkipped.contains("afterSkipped"));
        assertTrue(chainedListener.beforeConfig.contains("beforeSuccess"));
        assertTrue(chainedListener.beforeConfig.contains("afterSkipped"));
        
        assertTrue(chainedListener.beforeMethodBefore.contains("beforeSuccess"));
        assertTrue(chainedListener.beforeMethodAfter.contains("beforeSuccess"));
        assertTrue(chainedListener.testMethodBefore.contains("testAfterSkipped"));
        assertTrue(chainedListener.testMethodAfter.contains("testAfterSkipped"));
        assertTrue(chainedListener.afterMethodBefore.contains("afterSkipped"));
        assertTrue(chainedListener.afterMethodAfter.contains("afterSkipped"));
        
        assertTrue(chainedListener.testStarted.contains("testAfterSkipped"));
        assertTrue(chainedListener.testSuccess.contains("testAfterSkipped"));
        
    }

    @Test
    public void verifyConstructorFactory(){
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();

        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ConstructorFactory.class});
        testNG.addListener((ITestNGListener) lc);
        testNG.addListener((ITestNGListener) tla);
        testNG.run();

        ChainedListener chainedListener = lc.getAttachedListener(ChainedListener.class).get();
        
        assertEquals(tla.getPassedTests().size(), 3, "Incorrect passed test count");
        assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
        assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
        assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
        assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
        assertEquals(tla.getConfigurationSkips().size(), 0, "Incorrect configuration method skip count");
        
        assertTrue(chainedListener.testMethodBefore.contains("fakeTest"));
        assertTrue(chainedListener.testMethodAfter.contains("fakeTest"));
        
        assertTrue(chainedListener.testStarted.contains("fakeTest"));
        assertTrue(chainedListener.testSuccess.contains("fakeTest"));
        
        assertTrue(chainedListener.beforeClass.contains("ConstructorFactory"));
        assertTrue(chainedListener.afterClass.contains("ConstructorFactory"));
        
        assertTrue(chainedListener.testsBegun.contains("Command line test"));
        assertTrue(chainedListener.testsEnded.contains("Command line test"));
        
        assertTrue(chainedListener.suiteBegun.contains("Command line suite"));
        assertTrue(chainedListener.suiteEnded.contains("Command line suite"));
        
        assertTrue(chainedListener.xformTest.contains("method: fakeTest"));
        assertTrue(chainedListener.xformProvider.contains("method: ints"));
        assertTrue(chainedListener.xformFactory.contains("ctor: (unknown)"));
        
        assertTrue(chainedListener.interceptor.contains("Command line test"));
    }
    
}
