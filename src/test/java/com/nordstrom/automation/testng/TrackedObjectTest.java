package com.nordstrom.automation.testng;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.internal.TestResult;

public class TrackedObjectTest {
    
    private static final String KEY = "key";
    
    @Test
    public void basicBehavior() {
        Object obj = new Object();
        ITestResult result = Reporter.getCurrentTestResult();
        TrackedObject<Object> tracked = TrackedObject.create(result, KEY, obj);
        assertTrue(result.getAttributeNames().contains(KEY));
        Object attr = result.getAttribute(KEY);
        assertTrue(attr instanceof TrackedObject);
        Object val = tracked.getValue();
        assertTrue(val == obj);
        assertTrue(tracked.release(result));
        assertFalse(result.getAttributeNames().contains(KEY));
        assertFalse(tracked.release(result));
    }

    @Test
    public void repeatedResultIgnored() {
        Object obj = new Object();
        ITestResult result = Reporter.getCurrentTestResult();
        TrackedObject<Object> tracked = TrackedObject.create(result, KEY, obj);
        tracked.addRef(result);
        assertTrue(tracked.release(result));
        assertFalse(tracked.release(result));
    }
    
    @Test
    public void referenceTracked() {
        Object obj = new Object();
        ITestResult result = Reporter.getCurrentTestResult();
        TrackedObject<Object> tracked = TrackedObject.create(result, KEY, obj);
        
        ITestResult result2 = new TestResult();
        ITestResult result3 = new TestResult();
        
        tracked.addRef(result2);
        assertTrue(result2.getAttributeNames().contains(KEY));
        tracked.addRef(result3);
        assertTrue(result3.getAttributeNames().contains(KEY));
        
        assertTrue(tracked.release(result));
        assertFalse(tracked.release(result));
        assertTrue(tracked.release(result2));
        assertFalse(tracked.release(result2));
        assertTrue(tracked.release(result3));
        assertFalse(tracked.release(result3));
    }
    
    @Test
    public void bulkRelease() {
        Object obj = new Object();
        ITestResult result = Reporter.getCurrentTestResult();
        TrackedObject<Object> tracked = TrackedObject.create(result, KEY, obj);
        
        ITestResult result2 = new TestResult();
        ITestResult result3 = new TestResult();
        
        tracked.addRef(result2);
        tracked.addRef(result3);
        
        assertTrue(result.getAttributeNames().contains(KEY));
        assertTrue(result2.getAttributeNames().contains(KEY));
        assertTrue(result3.getAttributeNames().contains(KEY));
        
        assertTrue(tracked.release() == obj);
        
        assertFalse(result.getAttributeNames().contains(KEY));
        assertFalse(result2.getAttributeNames().contains(KEY));
        assertFalse(result3.getAttributeNames().contains(KEY));
        
        assertNull(tracked.release());
    }

}
