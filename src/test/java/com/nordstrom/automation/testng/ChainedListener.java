package com.nordstrom.automation.testng;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.testng.IAnnotationTransformer3;
import org.testng.IClassListener;
import org.testng.IConfigurationListener2;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IFactoryAnnotation;
import org.testng.annotations.IListenersAnnotation;
import org.testng.annotations.ITestAnnotation;
import org.testng.collections.Sets;

public class ChainedListener
                implements IAnnotationTransformer3, ISuiteListener, IConfigurationListener2,
                IInvokedMethodListener, ITestListener, IMethodInterceptor, IClassListener {
    
    static Set<String> configSuccess = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> configFailure = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> configSkipped = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> beforeConfig = Collections.synchronizedSet(Sets.<String>newHashSet());
    
    static Set<String> beforeMethodBefore = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> beforeMethodAfter = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> testMethodBefore = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> testMethodAfter = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> afterMethodBefore = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> afterMethodAfter = Collections.synchronizedSet(Sets.<String>newHashSet());
    
    static Set<String> beforeClass = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> afterClass = Collections.synchronizedSet(Sets.<String>newHashSet());
    
    static Set<String> testStarted = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> testSuccess = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> testFailure = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> testSkipped = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> testCurved = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> testsBegun = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> testsEnded = Collections.synchronizedSet(Sets.<String>newHashSet());
    
    static Set<String> suiteBegun = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> suiteEnded = Collections.synchronizedSet(Sets.<String>newHashSet());
    
    static Set<String> xformTest = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> xformConfig = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> xformProvider = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> xformFactory = Collections.synchronizedSet(Sets.<String>newHashSet());
    static Set<String> xformListeners = Collections.synchronizedSet(Sets.<String>newHashSet());
    
    static Set<String> interceptor = Collections.synchronizedSet(Sets.<String>newHashSet());

    @Override
    public void onConfigurationSuccess(ITestResult itr) {
        configSuccess.add(itr.getName());
    }

    @Override
    public void onConfigurationFailure(ITestResult itr) {
        configFailure.add(itr.getName());
    }

    @Override
    public void onConfigurationSkip(ITestResult itr) {
        configSkipped.add(itr.getName());
    }

    @Override
    public void beforeConfiguration(ITestResult tr) {
        beforeConfig.add(tr.getName());
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.getTestMethod().isBeforeMethodConfiguration()) {
            beforeMethodBefore.add(testResult.getName());
        } else if (method.isTestMethod()) {
            testMethodBefore.add(testResult.getName());
        } else if (method.getTestMethod().isAfterMethodConfiguration()) {
            afterMethodBefore.add(testResult.getName());
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.getTestMethod().isBeforeMethodConfiguration()) {
            beforeMethodAfter.add(testResult.getName());
        } else if (method.isTestMethod()) {
            testMethodAfter.add(testResult.getName());
        } else if (method.getTestMethod().isAfterMethodConfiguration()) {
            afterMethodAfter.add(testResult.getName());
        }
    }

    @Override
    public void onBeforeClass(ITestClass testClass) {
        beforeClass.add(testClass.getRealClass().getSimpleName());
    }

    @Override
    public void onAfterClass(ITestClass testClass) {
        afterClass.add(testClass.getRealClass().getSimpleName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        testStarted.add(result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testSuccess.add(result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testFailure.add(result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testSkipped.add(result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        testCurved.add(result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        testsBegun.add(context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        testsEnded.add(context.getName());
    }

    @Override
    public void onStart(ISuite suite) {
        suiteBegun.add(suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        suiteEnded.add(suite.getName());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testCtor,
                    Method testMethod) {
        
        if (testClass != null) {
            xformTest.add("class: " + testClass.getSimpleName());
        } else if (testCtor != null) {
            xformTest.add("ctor: " + testCtor.getName());
        } else {
            xformTest.add("method: " + testMethod.getName());
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void transform(IConfigurationAnnotation annotation, Class testClass,
                    Constructor testCtor, Method testMethod) {
        
        if (testClass != null) {
            xformConfig.add("class: " + testClass.getSimpleName());
        } else if (testCtor != null) {
            xformConfig.add("ctor: " + testCtor.getName());
        } else {
            xformConfig.add("method: " + testMethod.getName());
        }
    }

    @Override
    public void transform(IDataProviderAnnotation annotation, Method method) {
        xformProvider.add(method.getName());
    }

    @Override
    public void transform(IFactoryAnnotation annotation, Method method) {
        xformFactory.add(method.getName());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void transform(IListenersAnnotation annotation, Class testClass) {
        xformListeners.add(testClass.getSimpleName());
    }
    
    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        interceptor.add(context.getName());
        return methods;
    }

}