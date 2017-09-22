package com.nordstrom.automation.testng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

public class UnitTestArtifact implements ArtifactType {
    
    private static final String CAN_GET = "CanGet";
    private static final String WILL_GET = "WillGet";
    private static final String CAPTURE_STATE = "CaptureState";
    
    private static final String EXTENSION = "txt";
    private static final String ARTIFACT = "This text artifact was captured for '%s'";
    private static final Logger LOGGER = LoggerFactory.getLogger(UnitTestArtifact.class);
    
    public enum CaptureState {
        ABLE_TO_CAPTURE, CAN_NOT_CAPTURE, CAPTURE_SUCCESS, CAPTURE_FAILED
    }
    
    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public boolean canGetArtifact(ITestResult result) {
        if (canGet(result)) {
            setCaptureState(result, CaptureState.ABLE_TO_CAPTURE);
            return true;
        } else {
            setCaptureState(result, CaptureState.CAN_NOT_CAPTURE);
            return false;
        }
    }

    @Override
    public byte[] getArtifact(ITestResult result) {
        if (willGet(result)) {
            setCaptureState(result, CaptureState.CAPTURE_SUCCESS);
            return String.format(ARTIFACT, result.getName()).getBytes().clone();
        } else {
            setCaptureState(result, CaptureState.CAPTURE_FAILED);
            return new byte[0];
        }
    }

    @Override
    public String getArtifactExtension() {
        return EXTENSION;
    }
    
    static void setCanGet(ITestResult result, boolean canGet) {
        result.setAttribute(CAN_GET, Boolean.valueOf(canGet));
    }
    
    static boolean canGet(ITestResult result) {
        Object canGet = result.getAttribute(CAN_GET);
        return (canGet != null) ? ((Boolean) canGet).booleanValue() : true;
    }

    static void setWillGet(ITestResult result, boolean willGet) {
        result.setAttribute(WILL_GET, Boolean.valueOf(willGet));
    }
    
    static boolean willGet(ITestResult result) {
        Object willGet = result.getAttribute(WILL_GET);
        return (willGet != null) ? ((Boolean) willGet).booleanValue() : true;
    }

    static void setCaptureState(ITestResult result, CaptureState state) {
        result.setAttribute(CAPTURE_STATE, state);
    }
    
    static CaptureState getCaptureState(ITestResult result) {
        return (CaptureState) result.getAttribute(CAPTURE_STATE);
    }

}
