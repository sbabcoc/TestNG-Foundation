package com.nordstrom.automation.testng;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.testng.ITestResult;

/**
 * This class is a reference-tracking wrapper used by {@link PropertyManager} to record the test result objects to
 * which an object reference is propagated. This enables the client to release all references when the value is no
 * longer needed.
 * 
 * @param <T> tracked object type 
 */
public class TrackedObject<T extends Object> {
    
    private String key;
    private T value;
    private Set<ITestResult> references = new HashSet<>();
    
    /**
     * Constructor: Initialize key and value for reference-tracked object
     * 
     * @param result test result object
     * @param key key used to store the reference-tracked object in test result attribute collections
     * @param value object reference for which attribute propagation is to be tracked
     */
    public TrackedObject(ITestResult result, String key, T value) {
        this.key = key;
        this.value = value;
        addRef(result);
    }
    
    /**
     * Get the value store in this reference-tracking wrapper.
     * 
     * @return tracked object reference
     */
    public T getValue() {
        return value;
    }
    
    /**
     * Propagate a reference to the tracked object into the specified test result.
     * 
     * @param result test result to receive a reference to the tracked object
     */
    public void addRef(ITestResult result) {
        references.add(result);
        result.setAttribute(key, this);
    }
    
    /**
     * Release reference to the tracked object from the specified test result.
     * 
     * @param result test result for which tracked object reference should be released
     * @return 'true' if test result had reference to tracked object; otherwise 'false' 
     */
    public boolean release(ITestResult result) {
        if (references.contains(result)) {
            result.setAttribute(key, null);
            references.remove(result);
            if (references.isEmpty()) {
                value = null;
            }
            return true;
        }
        return false;
    }
    
    /**
     * Release all references to the tracked object from the test result into which they were propagated.
     * 
     * @return the tracked object reference itself
     */
    public T release() {
        T local = value;
        Iterator<ITestResult> iterator = references.iterator();
        while (iterator.hasNext()) {
            ITestResult result = iterator.next();
            result.setAttribute(key, null);
            iterator.remove();
        }
        value = null;
        return local;
    }
}
