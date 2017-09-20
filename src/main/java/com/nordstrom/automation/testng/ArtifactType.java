package com.nordstrom.automation.testng;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.testng.ITestResult;

public interface ArtifactType {
    
    /**
     * Get the SLF4J {@link Logger} for this artifact type.
     * 
     * @return logger for this artifact
     */
    Logger getLogger();
    
    /**
     * Determine if artifact capture is available in the specified context.
     * 
     * @param result TestNG test result object
     * @return 'true' if capture is available; otherwise 'false'
     */
    boolean canGetArtifact(ITestResult result);
    
    /**
     * Capture an artifact from the specified context.
     * 
     * @param result TestNG test result object
     * @return byte array containing the captured artifact; if capture fails, an empty array is returned
     */
    byte[] getArtifact(ITestResult result);
    
    /**
     * Get the path at which to store artifacts.
     * 
     * @return artifact storage path
     */
    default Path getArtifactPath() {
        return Paths.get("");
    }
    
    /**
     * Get the extension for artifact files of this type.
     * <br><br>
     * <b>NOTE</b>: The returned path can be either relative or absolute.
     * 
     * @return artifact file extension
     */
    String getArtifactExtension();
    
}
