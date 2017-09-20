package com.nordstrom.automation.testng;

import org.slf4j.Logger;
import org.testng.ITestResult;

public interface ArtifactType {
    
    /**
     * 
     * @return
     */
    Logger getLogger();
    
    /**
     * 
     * @return
     */
    boolean canGetArtifact(ITestResult result);
    
    /**
     * 
     * @return
     */
    byte[] getArtifact(ITestResult result);
    
    /**
     * 
     * @return
     */
    default String getArtifactPath() {
        return "";
    }
    
    /**
     * 
     * @return
     */
    String getArtifactExtension();
    
}
