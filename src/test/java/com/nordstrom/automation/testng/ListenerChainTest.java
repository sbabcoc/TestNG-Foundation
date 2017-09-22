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
        
        assertEquals(tla.getPassedTests().size(), 2, "Incorrect passed test count");
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
        
        assertTrue(ChainedListener.beforeClass.contains("ListenerChainTestCases"));
        assertTrue(ChainedListener.afterClass.contains("ListenerChainTestCases"));
        
        assertTrue(ChainedListener.testsBegun.contains("Command line test"));
        assertTrue(ChainedListener.testsEnded.contains("Command line test"));
        
        assertTrue(ChainedListener.suiteBegun.contains("Command line suite"));
        assertTrue(ChainedListener.suiteEnded.contains("Command line suite"));
        
        Set<String> expectTests = new HashSet<>(Arrays.asList("method: testSkipped",
                        "method: happyPath", "method: beforeSuccess", "method: beforeSkipped",
                        "method: skipBeforeFailed", "method: skipBeforeSkipped",
                        "method: testAfterSkipped", "method: productTest", "method: failAndPass",
                        "method: afterSuccess", "method: afterFailure",
                        "class: ListenerChainTestCases", "method: testAfterFailed",
                        "method: beforeFailure", "method: afterSkipped", "method: testFailed",
                        "method: testAttachedListener"));
        Set<String> expectConfigs = new HashSet<>(Arrays.asList("method: afterSuccess",
                        "method: afterFailure", "method: beforeSuccess", "method: beforeFailure",
                        "method: beforeSkipped", "method: afterSkipped"));
        
        assertEquals(ChainedListener.xformTest, expectTests);
        assertEquals(ChainedListener.xformConfig, expectConfigs);
        assertTrue(ChainedListener.xformProvider.contains("dataProvider"));
        assertTrue(ChainedListener.xformFactory.contains("createInstances"));
        assertTrue(ChainedListener.xformListeners.contains("ListenerChainTestCases"));
        
        assertTrue(ChainedListener.interceptor.contains("Command line test"));
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
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ListenerChainTestCases.class});
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
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ListenerChainTestCases.class});
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
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ListenerChainTestCases.class});
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
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ListenerChainTestCases.class});
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
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ListenerChainTestCases.class});
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
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ListenerChainTestCases.class});
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
    
}
