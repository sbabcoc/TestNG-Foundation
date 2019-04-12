package com.nordstrom.automation.testng;

import java.nio.file.Path;
import org.slf4j.Logger;
import org.testng.ITestResult;

/**
 * This interface defines the contract fulfilled by artifact capture providers. Instances of this interface supply the
 * scenario-specific implementation for artifact capture through the {@link ArtifactCollector} listener.
 * <p>
 * <b>IMPLEMENTING ARTIFACTTYPE</b>
 * <pre><code>
 * package com.nordstrom.example;
 * 
 * import java.nio.file.Path;
 * 
 * import org.slf4j.Logger;
 * import org.slf4j.LoggerFactory;
 * import org.testng.ITestResult;
 * 
 * import com.nordstrom.automation.testng.ArtifactType;
 * 
 * public class MyArtifactType implements ArtifactType {
 *     
 *     private static final String ARTIFACT_PATH = "artifacts";
 *     private static final String EXTENSION = "txt";
 *     private static final String ARTIFACT = "This text artifact was captured for '%s'";
 *     private static final Logger LOGGER = LoggerFactory.getLogger(MyArtifactType.class);
 * 
 *     &#64;Override
 *     public boolean canGetArtifact(ITestResult result) {
 *         return true;
 *     }
 * 
 *     &#64;Override
 *     public byte[] getArtifact(ITestResult result) {
 *         return String.format(ARTIFACT, result.getName()).getBytes().clone();
 *     }
 *     
 *     &#64;Override
 *     public Page getArtifactPath(ITestResult result) {
 *         return ArtifactType.super.getArtifactPath(result).resolve(ARTIFACT_PATH);
 *     }
 * 
 *     &#64;Override
 *     public String getArtifactExtension() {
 *         return EXTENSION;
 *     }
 *     
 *     &#64;Override
 *     public Logger getLogger() {
 *         return LOGGER;
 *     }
 * }
 * </code></pre>
 * <b>CREATING A TYPE-SPECIFIC ARTIFACT COLLECTOR</b>
 * <pre><code>
 * package com.nordstrom.example;
 * 
 * import com.nordstrom.automation.testng.ArtifactCollector;
 * 
 * public class MyArtifactCapture extends ArtifactCollector&lt;MyArtifactType&gt; {
 *     
 *     public MyArtifactCapture() {
 *         super(new MyArtifactType());
 *     }
 * }
 * </code></pre>
 */
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
     * @param result TestNG test result object
     * @return artifact storage path; {@code null} to accept default path
     */
    Path getArtifactPath(ITestResult result);
    
    /**
     * Get the extension for artifact files of this type.
     * <p>
     * <b>NOTE</b>: The returned path can be either relative or absolute.
     * 
     * @return artifact file extension
     */
    String getArtifactExtension();
}
