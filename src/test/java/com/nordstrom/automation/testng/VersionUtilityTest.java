package com.nordstrom.automation.testng;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertEquals;

import org.testng.ITestNGListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.nordstrom.automation.testng.TestNGConfig.TestNGSettings;

public class VersionUtilityTest {
    
    @BeforeClass
    public void beforeClass() {
        System.setProperty(TestNGSettings.MAX_RETRY.key(), "1");
    }
    
    @Test
    public void withoutFlowController() {
        
        TestNG testNG = new TestNG();
        
        TestListenerAdapter tla = new TestListenerAdapter();

        testNG.setTestClasses(new Class[]{RetryAnalyzerCheck.class});
        testNG.addListener((ITestNGListener) tla);
        testNG.run();
        
        assertEquals(tla.getPassedTests().size(), 3, "Incorrect passed test count");
        assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
        assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
        
        assertNull(RetryAnalyzerCheck.unspecified,
                "Retry analyzer found for test with no specified retry analyzer");
        
    }
    
    @Test
    public void withFlowController() {
        
        TestNG testNG = new TestNG();
        
        ExecutionFlowController efc = new ExecutionFlowController();
        TestListenerAdapter tla = new TestListenerAdapter();

        testNG.setTestClasses(new Class[]{RetryAnalyzerCheck.class});
        testNG.addListener((ITestNGListener) efc);
        testNG.addListener((ITestNGListener) tla);
        testNG.run();
        
        assertEquals(tla.getPassedTests().size(), 3, "Incorrect passed test count");
        assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
        assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
        
        assertEquals(RetryAnalyzerCheck.unspecified, RetryManager.class,
                "Retry manager not injected for test with no specified retry analyzer");
        
    }
    
    @Test
    public void testEmptyTestResult() {
        ITestResult testResult = VersionUtility.newEmptyTestResult();
        assertNull(testResult.getTestContext(), "Test context should be 'null'");
        assertNull(testResult.getInstanceName(), "Instance name should be 'null'");
        assertNull(testResult.getMethod(), "Method should be 'null'");
        assertNull(testResult.getName(), "Name should be 'null'");
    }

    @AfterClass
    public void afterClass() {
        System.clearProperty(TestNGSettings.MAX_RETRY.key());
    }
    
}
