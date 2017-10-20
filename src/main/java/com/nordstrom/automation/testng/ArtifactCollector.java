package com.nordstrom.automation.testng;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    
    private static final String ARTIFACT_PATHS = "ArtifactPaths";
    
    private final T provider;
    
    /**
     * Compose this artifact collector with a type-specific artifact implementation.
     * 
     * @param provider artifact implementation for this collector
     */
    public ArtifactCollector(T provider) {
        this.provider = provider;
    }

    @Override
    public void onFinish(ITestContext context) {
        // nothing to do here
    }

    @Override
    public void onStart(ITestContext context) {
        // nothing to do here
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        captureArtifact(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        captureArtifact(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // nothing to do here

    }

    @Override
    public void onTestStart(ITestResult result) {
        // nothing to do here
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // nothing to do here
    }
    
    /**
     * Capture artifact from the current test result context.
     * 
     * @param result TestNG test result object
     * @return (optional) path at which the captured artifact was stored
     */
    public Optional<Path> captureArtifact(ITestResult result) {
        if (! provider.canGetArtifact(result)) {
            return Optional.empty();
        }
        
        byte[] artifact = provider.getArtifact(result);
        if ((artifact == null) || (artifact.length == 0)) {
            return Optional.empty();
        }
        
        Path collectionPath = getCollectionPath(result);
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
                            getArtifactBaseName(result), 
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
        
        recordArtifactPath(artifactPath, result);
        return Optional.of(artifactPath);
    }
    
    /**
     * Get path of directory at which to store artifacts.
     * 
     * @param result TestNG test result object
     * @return path of artifact storage directory
     */
    private Path getCollectionPath(ITestResult result) {
        ITestContext testContext = result.getTestContext();
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
     * @param result TestNG test result object
     * @return artifact file base name
     */
    private static String getArtifactBaseName(ITestResult result) {
        Object[] parameters = result.getParameters();
        if (parameters.length == 0) {
            return result.getName();
        } else {
            int hashcode = Arrays.deepHashCode(parameters);
            String hashStr = String.format("%08X", hashcode);
            return result.getName() + "-" + hashStr;
        }
    }
    
    /**
     * Record the path at which the specified artifact was store in the indicated test result.
     * @param artifactPath path at which the captured artifact was stored 
     * @param result TestNG test result object
     */
    private static void recordArtifactPath(Path artifactPath, ITestResult result) {
        @SuppressWarnings("unchecked")
        List<Path> artifactPaths = (List<Path>) result.getAttribute(ARTIFACT_PATHS);
        if (artifactPaths == null) {
            artifactPaths = new ArrayList<>();
            result.setAttribute(ARTIFACT_PATHS, artifactPaths);
        }
        artifactPaths.add(artifactPath);
    }
    
    /**
     * Retrieve the paths of artifacts that were stored in the indicated test result.
     * 
     * @param result TestNG test result object
     * @return (optional) list of artifact paths
     */
    public static Optional<List<Path>> retrieveArtifactPaths(ITestResult result) {
        @SuppressWarnings("unchecked")
        List<Path> artifactPaths = (List<Path>) result.getAttribute(ARTIFACT_PATHS);
        if (artifactPaths != null) {
            return Optional.of(artifactPaths);
        } else {
            return Optional.empty();
        }
    }

}
