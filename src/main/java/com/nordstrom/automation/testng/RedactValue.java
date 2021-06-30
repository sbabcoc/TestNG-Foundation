package com.nordstrom.automation.testng;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Use this annotation to mark test method arguments whose values should be redacted in log output:
 * 
 * <blockquote><pre>
 * &#64;Test
 * &#64;Parameters({"username", "password"})
 * public void testLogin(String username, &#64;RedactValue String password) {
 *     // test implementation goes here
 * }</pre></blockquote>
 */
@Retention(RUNTIME)
@Target(PARAMETER)
@Inherited
public @interface RedactValue { }
