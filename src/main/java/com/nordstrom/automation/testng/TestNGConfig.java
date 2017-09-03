package com.nordstrom.automation.testng;

import java.io.IOException;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.nordstrom.automation.settings.SettingsCore;

public class TestNGConfig extends SettingsCore<TestNGConfig.TestNGSettings> {
    
    private static final String SETTINGS_FILE = "testng.properties";
    private static final String TESTNG_CONFIG = "TESTNG_CONFIG";
    
    public enum TestNGSettings implements SettingsCore.SettingsAPI {
        /** name: <b>testng.timeout.test</b> <br> default: {@code null} */
        TEST_TIMEOUT("testng.timeout.test", null);

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
    
    private static final ThreadLocal<TestNGConfig> testNGConfig = new ThreadLocal<>();

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
            return getTestNGConfig();
        }
        if (testResult.getAttribute(TESTNG_CONFIG) == null) {
            synchronized (TESTNG_CONFIG) {
                if (testResult.getAttribute(TESTNG_CONFIG) == null) {
                    testResult.setAttribute(TESTNG_CONFIG, getTestNGConfig());
                }
            }
        }
        return (TestNGConfig) testResult.getAttribute(TESTNG_CONFIG);
    }
    
    private static TestNGConfig getTestNGConfig() {
        if (testNGConfig.get() == null) {
            try {
                testNGConfig.set(new TestNGConfig());
            } catch (ConfigurationException | IOException e) {
                throw new RuntimeException("Failed to instantiate settings", e);
            }
        }
        return testNGConfig.get();
    }
    
    @Override
    public String getSettingsPath() {
        return SETTINGS_FILE;
    }
}
