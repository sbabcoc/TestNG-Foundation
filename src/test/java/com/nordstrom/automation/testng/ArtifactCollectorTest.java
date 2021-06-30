package com.nordstrom.automation.testng;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.testng.ITestNGListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Test;

import com.nordstrom.automation.testng.UnitTestArtifact.CaptureState;

public class ArtifactCollectorTest {

    @Test
    public void verifyHappyPath() {
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ArtifactCollectorTestCases.class});
        testNG.addListener((ITestNGListener) lc);
        testNG.addListener((ITestNGListener) tla);
        testNG.setGroups("testPassed");
        testNG.run();
        
        assertEquals(tla.getPassedTests().size(), 1, "Incorrect passed test count");
        assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
        assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
        assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
        assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
        assertEquals(tla.getConfigurationSkips().size(), 0, "Incorrect configuration method skip count");
        
        ITestResult result = tla.getPassedTests().get(0);
        assertNull(UnitTestArtifact.getCaptureState(result), "Artifact provider capture state should be 'null'");
        assertNull(UnitTestCapture.getArtifactPath(result), "Artifact capture should not have been requested");
    }
    
    @Test
    public void verifyCaptureOnFailure() {
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ArtifactCollectorTestCases.class});
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
        
        ITestResult result = tla.getFailedTests().get(0);
        assertEquals(UnitTestArtifact.getCaptureState(result), CaptureState.CAPTURE_SUCCESS, "Incorrect artifact provider capture state");
        assertTrue(UnitTestCapture.getArtifactPath(result).isPresent(), "Artifact capture output path is not present");
    }
    
    @Test
    public void verifyCanNotCapture() {
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ArtifactCollectorTestCases.class});
        testNG.addListener((ITestNGListener) lc);
        testNG.addListener((ITestNGListener) tla);
        testNG.setGroups("canNotCapture");
        testNG.run();
        
        assertEquals(tla.getPassedTests().size(), 0, "Incorrect passed test count");
        assertEquals(tla.getFailedTests().size(), 1, "Incorrect failed test count");
        assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
        assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
        assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
        assertEquals(tla.getConfigurationSkips().size(), 0, "Incorrect configuration method skip count");
        
        ITestResult result = tla.getFailedTests().get(0);
        assertEquals(UnitTestArtifact.getCaptureState(result), CaptureState.CAN_NOT_CAPTURE, "Incorrect artifact provider capture state");
        assertFalse(UnitTestCapture.getArtifactPath(result).isPresent(), "Artifact capture output path should not be present");
    }
    
    @Test
    public void verifyWillNotCapture() {
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ArtifactCollectorTestCases.class});
        testNG.addListener((ITestNGListener) lc);
        testNG.addListener((ITestNGListener) tla);
        testNG.setGroups("willNotCapture");
        testNG.run();
        
        assertEquals(tla.getPassedTests().size(), 0, "Incorrect passed test count");
        assertEquals(tla.getFailedTests().size(), 1, "Incorrect failed test count");
        assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
        assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
        assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
        assertEquals(tla.getConfigurationSkips().size(), 0, "Incorrect configuration method skip count");
        
        ITestResult result = tla.getFailedTests().get(0);
        assertEquals(UnitTestArtifact.getCaptureState(result), CaptureState.CAPTURE_FAILED, "Incorrect artifact provider capture state");
        assertFalse(UnitTestCapture.getArtifactPath(result).isPresent(), "Artifact capture output path should not be present");
    }
    
    @Test
    public void verifyOnDemandCapture() {
        
        ListenerChain lc = new ListenerChain();
        TestListenerAdapter tla = new TestListenerAdapter();
        
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ArtifactCollectorTestCases.class});
        testNG.addListener((ITestNGListener) lc);
        testNG.addListener((ITestNGListener) tla);
        testNG.setGroups("onDemandCapture");
        testNG.run();
        
        assertEquals(tla.getPassedTests().size(), 1, "Incorrect passed test count");
        assertEquals(tla.getFailedTests().size(), 0, "Incorrect failed test count");
        assertEquals(tla.getSkippedTests().size(), 0, "Incorrect skipped test count");
        assertEquals(tla.getFailedButWithinSuccessPercentageTests().size(), 0, "Incorrect curve-graded success count");
        assertEquals(tla.getConfigurationFailures().size(), 0, "Incorrect configuration method failure count");
        assertEquals(tla.getConfigurationSkips().size(), 0, "Incorrect configuration method skip count");
        
        ITestResult result = tla.getPassedTests().get(0);
        assertEquals(UnitTestArtifact.getCaptureState(result), CaptureState.CAPTURE_SUCCESS, "Incorrect artifact provider capture state");
        assertTrue(UnitTestCapture.getArtifactPath(result).isPresent(), "Artifact capture output path is not present");
    }
}
