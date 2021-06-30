package com.nordstrom.automation.testng;

import java.nio.file.Path;
import org.testng.ITestResult;

import com.google.common.base.Optional;

public class UnitTestCapture extends ArtifactCollector<UnitTestArtifact> {
    
    private static final String ARTIFACT_PATH = "ArtifactPath";
    
    public UnitTestCapture() {
        super(new UnitTestArtifact());
    }
    
    /**
     * Capture artifact from the current test result context.
     * <p>
     * <b>NOTE</b>: This override is here solely to record the artifact path for the benefit of the unit tests,
     * as verification meta-data. It makes no contribution to the actual process of artifact capture
     * 
     * @param testResult TestNG test result object
     * @return path at which the captured artifact was stored
     */
    @Override
    public Optional<Path> captureArtifact(ITestResult testResult) {
        Optional<Path> artifactPath = super.captureArtifact(testResult);
        testResult.setAttribute(ARTIFACT_PATH, artifactPath);
        return artifactPath;
    }
    
    /**
     * Get the path at which the captured artifact was stored.
     * 
     * @param testResult TestNG test result object
     * @return path at which the captured artifact was stored (may be 'null')
     */
    @SuppressWarnings("unchecked")
    public static Optional<Path> getArtifactPath(ITestResult testResult) {
        return (Optional<Path>) testResult.getAttribute(ARTIFACT_PATH);
    }
    
}
