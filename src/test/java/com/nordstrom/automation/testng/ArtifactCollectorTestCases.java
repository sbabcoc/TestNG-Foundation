package com.nordstrom.automation.testng;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.google.common.base.Optional;

@LinkedListeners({UnitTestCapture.class, ExecutionFlowController.class})
public class ArtifactCollectorTestCases {

    @Test(groups = {"testPassed"})
    public void testPassed() {
        System.out.println("testPassed");
        assertTrue(true);
    }
    
    @Test(groups = {"testFailed"})
    public void testFailed() {
        System.out.println("testFailed");
        fail("testFailed");
    }
    
    @Test(groups = {"canNotCapture"})
    public void testCanNotCapture() {
        System.out.println("canNotCapture");
        ITestResult result = Reporter.getCurrentTestResult();
        UnitTestArtifact.disableCapture(result);
        fail("canNotCapture");
    }
    
    @Test(groups = {"willNotCapture"})
    public void testWillNotCapture() {
        System.out.println("willNotCapture");
        ITestResult result = Reporter.getCurrentTestResult();
        UnitTestArtifact.crippleCapture(result);
        fail("willNotCapture");
    }
    
    @Test(groups = {"onDemandCapture"})
    public void testOnDemandCapture() {
        System.out.println("onDemandCapture");
        getListener().captureArtifact(Reporter.getCurrentTestResult());
    }
    
    private UnitTestCapture getListener() {
        ITestResult result = Reporter.getCurrentTestResult();
        Optional<UnitTestCapture> optional = AbstractListenerChain.getAttachedListener(result, UnitTestCapture.class);
        return optional.get();
    }
    
}
