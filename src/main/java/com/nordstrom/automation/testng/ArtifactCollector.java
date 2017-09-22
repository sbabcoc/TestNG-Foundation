package com.nordstrom.automation.testng;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.nordstrom.common.file.PathUtils;

/**
 * This TestNG {@link ITestListener test listener} that serves as the foundation for artifact-capturing test listeners.
 * This is a generic class, with the artifact-specific implementation provided by implementations of the {@link
 * ArtifactType} interface.
 * 
 * @param <T> scenario-specific artifact capture type
 */
public class ArtifactCollector<T extends ArtifactType> implements ITestListener {
    
    private final T provider;
    
    public ArtifactCollector(T provider) {
        this.provider = provider;
    }

    @Override
    public void onFinish(ITestContext arg0) {
        // nothing to do here
    }

    @Override
    public void onStart(ITestContext arg0) {
        // nothing to do here
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
        // nothing to do here
    }

    @Override
    public void onTestFailure(ITestResult testResult) {
        captureArtifact(testResult);
    }

    @Override
    public void onTestSkipped(ITestResult arg0) {
        // nothing to do here

    }

    @Override
    public void onTestStart(ITestResult arg0) {
        // nothing to do here
    }

    @Override
    public void onTestSuccess(ITestResult arg0) {
        // nothing to do here
    }
    
    /**
     * Capture artifact from the current test result context.
     * 
     * @param testResult TestNG test result object
     * @return path at which the captured artifact was stored
     */
    public Optional<Path> captureArtifact(ITestResult testResult) {
        if (! provider.canGetArtifact(testResult)) {
            return Optional.empty();
        }
        
        byte[] artifact = provider.getArtifact(testResult);
        if (artifact.length == 0) {
            return Optional.empty();
        }
        
        Path collectionPath = getCollectionPath(testResult);
        if (!collectionPath.toFile().exists()) {
            try {
                Files.createDirectories(collectionPath);
            } catch (IOException e) {
                String messageTemplate = "Unable to create collection directory ({}); no artifact was captured";
                provider.getLogger().warn(messageTemplate, collectionPath, e);
                return Optional.empty();
            }
        }
        
        Path artifactPath;
        try {
            artifactPath = PathUtils.getNextPath(
                            collectionPath, 
                            getArtifactBaseName(testResult), 
                            provider.getArtifactExtension());
        } catch (IOException e) {
            provider.getLogger().warn("Unable to get output path; no artifact was captured", e);
            return Optional.empty();
        }
        
        try {
            provider.getLogger().info("Saving captured artifact to ({}).", artifactPath);
            Files.write(artifactPath, artifact);
        } catch (IOException e) {
            provider.getLogger().warn("I/O error saving to ({}); no artifact was captured", artifactPath, e);
            return Optional.empty();
        }
        
        return Optional.of(artifactPath);
    }
    
    /**
     * Get path of directory at which to store artifacts.
     * 
     * @param testResult TestNG test result object
     * @return path of artifact storage directory
     */
    private Path getCollectionPath(ITestResult testResult) {
        ITestContext testContext = testResult.getTestContext();
        String outputDirectory = testContext.getOutputDirectory();
        Path collectionPath = Paths.get(outputDirectory);
        return collectionPath.resolve(provider.getArtifactPath(null));
    }
    
    /**
     * Get base name for artifact files for the specified test result.
     * <br><br>
     * <b>NOTE</b>: The base name is derived from the name of the current test.
     * If the method is parameterized, a hash code is computed from the parameter
     * values and appended to the base name as an 8-digit hexadecimal integer.
     * 
     * @param testResult TestNG test result object
     * @return artifact file base name
     */
    private String getArtifactBaseName(ITestResult testResult) {
        Object[] parameters = testResult.getParameters();
        if (parameters.length == 0) {
            return testResult.getName();
        } else {
            int hashcode = Arrays.deepHashCode(parameters);
            String hashStr = String.format("%08X", hashcode);
            return testResult.getName() + "-" + hashStr;
        }
    }

}
