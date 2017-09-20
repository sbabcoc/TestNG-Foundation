package com.nordstrom.automation.testng;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.nordstrom.common.file.PathUtils;

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
    
    public void captureArtifact(ITestResult testResult) {
        if (! provider.canGetArtifact(testResult)) {
            return;
        }
        
        byte[] artifact = provider.getArtifact(testResult);
        if (artifact.length == 0) {
            return;
        }
        
        Path collectionPath = getCollectionPath(testResult);
        if (!collectionPath.toFile().exists()) {
            try {
                Files.createDirectory(collectionPath);
            } catch (IOException e) {
                String messageTemplate = "Unable to create collection directory ({}); no artifact was captured";
                provider.getLogger().info(messageTemplate, collectionPath, e);
                return;
            }
        }
        
        Path artifactPath;
        try {
            artifactPath = PathUtils.getNextPath(
                            collectionPath, 
                            getArtifactBaseName(testResult), 
                            provider.getArtifactExtension());
        } catch (IOException e) {
            provider.getLogger().info("Unable to get output path; no artifact was captured", e);
            return;
        }
        
        try {
            provider.getLogger().info("Saving captured artifact to ({}).", artifactPath);
            Files.write(artifactPath, artifact);
        } catch (IOException e) {
            provider.getLogger().info("I/O error saving to ({}); no artifact was captured", artifactPath, e);
            return;
        }
    }
    
    private Path getCollectionPath(ITestResult testResult) {
        ITestContext testContext = testResult.getTestContext();
        String outputDirectory = testContext.getOutputDirectory();
        Path collectionPath = Paths.get(outputDirectory);
        return collectionPath.resolve(provider.getArtifactPath());
    }
    
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
