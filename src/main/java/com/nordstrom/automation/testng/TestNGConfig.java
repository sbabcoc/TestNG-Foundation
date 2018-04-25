package com.nordstrom.automation.testng;

import java.io.IOException;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.nordstrom.automation.settings.SettingsCore;
import com.nordstrom.common.base.UncheckedThrow;

/**
 * This class declares the settings and methods related to TestNG configuration.
 * 
 * @see TestNGSettings
 */
public class TestNGConfig extends SettingsCore<TestNGConfig.TestNGSettings> {
    
    private static final String SETTINGS_FILE = "testng.properties";
    private static final String TESTNG_CONFIG = "TESTNG_CONFIG";
    
    /**
     * This enumeration declares the settings that enable you to control the parameters
     * used by <b>TestNG Foundation</b>.
     * <p>
     * Each setting is defined by a constant name and System property key. Many settings
     * also define default values. Note that all of these settings can be overridden via
     * the {@code testng.properties} file and System property declarations.
     */
    public enum TestNGSettings implements SettingsCore.SettingsAPI {
        /** name: <b>testng.timeout.test</b> <br> default: {@code null} */
        TEST_TIMEOUT("testng.timeout.test", null),
        /** name: testng.max.retry <br> default: {@code null} */
        MAX_RETRY("testng.max.retry", null);

        private String propertyName;
        private String defaultValue;
        
        TestNGSettings(String propertyName, String defaultValue) {
            this.propertyName = propertyName;
            this.defaultValue = defaultValue;
        }
        
        @Override
        public String key() {
            return propertyName;
        }

        @Override
        public String val() {
            return defaultValue;
        }
    }
    
    private static final ThreadLocal<TestNGConfig> testNGConfig = new InheritableThreadLocal<TestNGConfig>() {
        @Override
        protected TestNGConfig initialValue() {
            try {
                return new TestNGConfig();
            } catch (ConfigurationException | IOException e) {
                throw UncheckedThrow.throwUnchecked(e);
            }
        }
    };

    /**
     * Instantiate a <b>TestNG Foundation</b> configuration object.
     * 
     * @throws ConfigurationException If a failure is encountered while initializing this configuration object.
     * @throws IOException If a failure is encountered while reading from a configuration input stream.
     */
    public TestNGConfig() throws ConfigurationException, IOException {
        super(TestNGSettings.class);
    }

    /**
     * Get the TestNG configuration object for the current context.
     * 
     * @return TestNG configuration object
     */
    public static TestNGConfig getConfig() {
        return getConfig(Reporter.getCurrentTestResult());
    }
    
    /**
     * Get the TestNG configuration object for the specified context.
     * 
     * @param testResult configuration context (TestNG test result object)
     * @return TestNG configuration object
     */
    public static TestNGConfig getConfig(ITestResult testResult) {
        if (testResult == null) {
            return testNGConfig.get();
        }
        
        TestNGConfig config = (TestNGConfig) testResult.getAttribute(TESTNG_CONFIG);
        
        if (config == null) {
            config = testNGConfig.get();
            testResult.setAttribute(TESTNG_CONFIG, config);
        }
        
        return config;
    }
    
    @Override
    public String getSettingsPath() {
        return SETTINGS_FILE;
    }
}
