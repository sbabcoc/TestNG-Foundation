package com.nordstrom.automation.testng;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.IAnnotationTransformer;
import org.testng.IClassListener;
import org.testng.IConfigurationListener;
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

public class ChainedListener implements IAnnotationTransformer, ISuiteListener, IConfigurationListener,
		IInvokedMethodListener, ITestListener, IMethodInterceptor, IClassListener {
    
    Set<String> configSuccess = Collections.synchronizedSet(new HashSet<String>());
    Set<String> configFailure = Collections.synchronizedSet(new HashSet<String>());
    Set<String> configSkipped = Collections.synchronizedSet(new HashSet<String>());
    Set<String> beforeConfig = Collections.synchronizedSet(new HashSet<String>());
    
    Set<String> beforeMethodBefore = Collections.synchronizedSet(new HashSet<String>());
    Set<String> beforeMethodAfter = Collections.synchronizedSet(new HashSet<String>());
    Set<String> testMethodBefore = Collections.synchronizedSet(new HashSet<String>());
    Set<String> testMethodAfter = Collections.synchronizedSet(new HashSet<String>());
    Set<String> afterMethodBefore = Collections.synchronizedSet(new HashSet<String>());
    Set<String> afterMethodAfter = Collections.synchronizedSet(new HashSet<String>());
    
    Set<String> beforeClass = Collections.synchronizedSet(new HashSet<String>());
    Set<String> afterClass = Collections.synchronizedSet(new HashSet<String>());
    
    Set<String> testStarted = Collections.synchronizedSet(new HashSet<String>());
    Set<String> testSuccess = Collections.synchronizedSet(new HashSet<String>());
    Set<String> testFailure = Collections.synchronizedSet(new HashSet<String>());
    Set<String> testSkipped = Collections.synchronizedSet(new HashSet<String>());
    Set<String> testCurved = Collections.synchronizedSet(new HashSet<String>());
    Set<String> testsBegun = Collections.synchronizedSet(new HashSet<String>());
    Set<String> testsEnded = Collections.synchronizedSet(new HashSet<String>());
    
    Set<String> suiteBegun = Collections.synchronizedSet(new HashSet<String>());
    Set<String> suiteEnded = Collections.synchronizedSet(new HashSet<String>());
    
    Set<String> xformTest = Collections.synchronizedSet(new HashSet<String>());
    Set<String> xformConfig = Collections.synchronizedSet(new HashSet<String>());
    Set<String> xformProvider = Collections.synchronizedSet(new HashSet<String>());
    Set<String> xformFactory = Collections.synchronizedSet(new HashSet<String>());
    Set<String> xformListeners = Collections.synchronizedSet(new HashSet<String>());
    
    Set<String> interceptor = Collections.synchronizedSet(new HashSet<String>());

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
        xformProvider.add("method: " + method.getName());
    }

    @Override
    public void transform(IFactoryAnnotation annotation, Method method) {
        if (method != null) {
            xformFactory.add("method: " + method.getName());
        } else {
            xformFactory.add("ctor: (unknown)");
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void transform(IListenersAnnotation annotation, Class testClass) {
        xformListeners.add("class: " + testClass.getSimpleName());
    }
    
    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        interceptor.add(context.getName());
        return methods;
    }

}