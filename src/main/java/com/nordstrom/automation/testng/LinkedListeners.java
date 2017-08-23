package com.nordstrom.automation.testng;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.testng.ITestNGListener;

@Retention(RUNTIME)
@Target({TYPE})
@Inherited
public @interface LinkedListeners {
    Class<? extends ITestNGListener>[] value() default {};
}
