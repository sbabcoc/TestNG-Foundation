package com.nordstrom.automation.testng;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.testng.ITestNGListener;

/**
 * This annotation enables test class implementors to specify an array of TestNG listeners to attach to the
 * {@link AbstractListenerChain}.
 */
@Retention(RUNTIME)
@Target({TYPE})
@Inherited
public @interface LinkedListeners {
    /**
     * Get the array of TestNG listener classes specified in this annotation.
     * 
     * @return array of listener classes (may be empty)
     */
    Class<? extends ITestNGListener>[] value() default {};
}
