package com.nordstrom.automation.testng;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.testng.IAnnotationTransformer;
import org.testng.IAnnotationTransformer2;
import org.testng.IAnnotationTransformer3;
import org.testng.IClassListener;
import org.testng.IConfigurationListener;
import org.testng.IConfigurationListener2;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IInvokedMethodListener2;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.ITestResult;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IFactoryAnnotation;
import org.testng.annotations.IListenersAnnotation;
import org.testng.annotations.ITestAnnotation;

import com.google.common.collect.Lists;

/**
 * This TestNG listener enables the addition of other listeners at runtime and guarantees the order in which they're
 * invoked. This is similar in behavior to a JUnit rule chain.
 */
public class ListenerChain
                implements IAnnotationTransformer3, ISuiteListener, IConfigurationListener2,
                IInvokedMethodListener2, ITestListener, IMethodInterceptor, IClassListener {
    
    private Set<Class<?>> markedClasses = Collections.synchronizedSet(new HashSet<>());
    private Set<Class<? extends ITestNGListener>> listenerSet = 
            Collections.synchronizedSet(new HashSet<>());
    
    private List<ITestNGListener> listeners = new ArrayList<>();
    private List<IAnnotationTransformer> annotationXformers = new ArrayList<>();
    private List<IAnnotationTransformer2> annotationXformers2 = new ArrayList<>();
    private List<IAnnotationTransformer3> annotationXformers3 = new ArrayList<>();
    private List<ISuiteListener> suiteListeners = new ArrayList<>();
    private List<IConfigurationListener> configListeners = new ArrayList<>();
    private List<IConfigurationListener2> configListeners2 = new ArrayList<>();
    private List<IInvokedMethodListener> methodListeners = new ArrayList<>();
    private List<IInvokedMethodListener2> methodListeners2 = new ArrayList<>();
    private List<ITestListener> testListeners = new ArrayList<>();
    private List<IMethodInterceptor> methodInterceptors = new ArrayList<>();
    private List<IClassListener> classListeners = new ArrayList<>();
    
    private static final String LISTENER_CHAIN = "ListenerChain";

    /**
     * [IAnnotationTransformer]
     * This method will be invoked by TestNG to give you a chance to modify a TestNG annotation read from your test
     * classes. You can change the values you need by calling any of the setters on the ITest interface. Note that
     * only one of the three parameters testClass, testCtor and testMethod will be non-null.
     * 
     * @param annotation The annotation that was read from your test class.
     * @param testClass If the annotation was found on a class, this parameter represents this class (null otherwise).
     * @param testCtor If the annotation was found on a constructor, this parameter represents this constructor (null
     *        otherwise).
     * @param testMethod If the annotation was found on a method, this parameter represents this method (null
     *        otherwise).
     */
    @Override
    @SuppressWarnings("rawtypes")
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testCtor, Method testMethod) {
        attachListeners(testClass, testCtor, testMethod);
        
        synchronized(annotationXformers) {
            for (IAnnotationTransformer annotationXformer : annotationXformers) {
                annotationXformer.transform(annotation, testClass, testCtor, testMethod);
            }
        }
        synchronized(annotationXformers2) {
            for (IAnnotationTransformer2 annotationXformer : annotationXformers2) {
                annotationXformer.transform(annotation, testClass, testCtor, testMethod);
            }
        }
        synchronized(annotationXformers3) {
            for (IAnnotationTransformer3 annotationXformer : annotationXformers3) {
                annotationXformer.transform(annotation, testClass, testCtor, testMethod);
            }
        }
    }

    /**
     * [IAnnotationTransformer2]
     * Transform an IConfiguration annotation. Note that only one of the three parameters testClass, testCtor and
     * testMethod will be non-null.
     * 
     * @param annotation The annotation that was read from your test class.
     * @param testClass If the annotation was found on a class, this parameter represents this class (null otherwise).
     * @param testCtor If the annotation was found on a constructor, this parameter represents this constructor (null
     *        otherwise).
     * @param testMethod If the annotation was found on a method, this parameter represents this method (null
     *        otherwise).
     */
    @Override
    @SuppressWarnings("rawtypes")
    public void transform(IConfigurationAnnotation annotation, Class testClass,
                    Constructor testCtor, Method testMethod) {
        
        attachListeners(testClass, testCtor, testMethod);

        synchronized(annotationXformers2) {
            for (IAnnotationTransformer2 annotationXformer : annotationXformers2) {
                annotationXformer.transform(annotation, testClass, testCtor, testMethod);
            }
        }
        synchronized(annotationXformers3) {
            for (IAnnotationTransformer3 annotationXformer : annotationXformers3) {
                annotationXformer.transform(annotation, testClass, testCtor, testMethod);
            }
        }
    }

    /**
     * [IAnnotationTransformer2]
     * Transform an IDataProvider annotation.
     * 
     * @param annotation The data provider annotation.
     * @param method The method annotated with the IDataProvider annotation.
     */
    @Override
    public void transform(IDataProviderAnnotation annotation, Method method) {
        attachListeners(method);
        
        synchronized(annotationXformers2) {
            for (IAnnotationTransformer2 annotationXformer : annotationXformers2) {
                annotationXformer.transform(annotation, method);
            }
        }
        synchronized(annotationXformers3) {
            for (IAnnotationTransformer3 annotationXformer : annotationXformers3) {
                annotationXformer.transform(annotation, method);
            }
        }
    }

    /**
     * [IAnnotationTransformer2]
     * Transform an IFactory annotation.
     * 
     * @param annotation The factory annotation.
     * @param method The method annotated with the IFactory annotation.
     */
    @Override
    public void transform(IFactoryAnnotation annotation, Method method) {
        attachListeners(method);
        
        synchronized(annotationXformers2) {
            for (IAnnotationTransformer2 annotationXformer : annotationXformers2) {
                annotationXformer.transform(annotation, method);
            }
        }
        synchronized(annotationXformers3) {
            for (IAnnotationTransformer3 annotationXformer : annotationXformers3) {
                annotationXformer.transform(annotation, method);
            }
        }
    }

    /**
     * [IAnnotationTransformer3]
     * Transform a Listeners annotation.
     * 
     * @param annotation The listeners annotation.
     * @param testClass The test class annotated with the Listeners annotation.
     */
    @Override
    @SuppressWarnings("rawtypes")
    public void transform(IListenersAnnotation annotation, Class testClass) {
        attachListeners(testClass);
        
        synchronized(annotationXformers3) {
            for (IAnnotationTransformer3 annotationXformer : annotationXformers3) {
                annotationXformer.transform(annotation, testClass);
            }
        }
    }
    
    /**
     * [ISuiteListener]
     * This method is invoked before the SuiteRunner starts.
     * 
     * @param suite current test suite
     */
    @Override
    public void onStart(ISuite suite) {
        suite.setAttribute(LISTENER_CHAIN, this);
        
        synchronized(suiteListeners) {
            for (ISuiteListener suiteListener : Lists.reverse(suiteListeners)) {
                suiteListener.onStart(suite);
            }
        }
    }

    /**
     * [ISuiteListener]
     * This method is invoked after the SuiteRunner has run all
     * the test suites.
     * 
     * @param suite current test suite
     */
    @Override
    public void onFinish(ISuite suite) {
        synchronized(suiteListeners) {
            for (ISuiteListener suiteListener : suiteListeners) {
                suiteListener.onFinish(suite);
            }
        }
    }

    /**
     * [IConfigurationListener]
     * Invoked whenever a configuration method succeeded.
     * 
     * @param itr test result object for the associated configuration method
     */
    @Override
    public void onConfigurationSuccess(ITestResult itr) {
        synchronized(configListeners) {
            for (IConfigurationListener configListener : configListeners) {
                configListener.onConfigurationSuccess(itr);
            }
        }
        synchronized(configListeners2) {
            for (IConfigurationListener2 configListener : configListeners2) {
                configListener.onConfigurationSuccess(itr);
            }
        }
    }

    /**
     * [IConfigurationListener]
     * Invoked whenever a configuration method failed.
     * 
     * @param itr test result object for the associated configuration method
     */
    @Override
    public void onConfigurationFailure(ITestResult itr) {
        synchronized(configListeners) {
            for (IConfigurationListener configListener : configListeners) {
                configListener.onConfigurationFailure(itr);
            }
        }
        synchronized(configListeners2) {
            for (IConfigurationListener2 configListener : configListeners2) {
                configListener.onConfigurationFailure(itr);
            }
        }
    }

    /**
     * [IConfigurationListener]
     * Invoked whenever a configuration method was skipped.
     * 
     * @param itr test result object for the associated configuration method
     */
    @Override
    public void onConfigurationSkip(ITestResult itr) {
        synchronized(configListeners) {
            for (IConfigurationListener configListener : configListeners) {
                configListener.onConfigurationSkip(itr);
            }
        }
        synchronized(configListeners2) {
            for (IConfigurationListener2 configListener : configListeners2) {
                configListener.onConfigurationSkip(itr);
            }
        }
    }

    /**
     * [IConfigurationListener2]
     * Invoked before a configuration method is invoked.
     * 
     * @param tr test result object for the associated configuration method
     */
    @Override
    public void beforeConfiguration(ITestResult tr) {
        synchronized(configListeners2) {
            for (IConfigurationListener2 configListener : Lists.reverse(configListeners2)) {
                configListener.beforeConfiguration(tr);
            }
        }
    }

    /**
     * [IInvokedMethodListener]
     * Invoked before each test or configuration method is invoked by TestNG
     * 
     * @param method TestNG representation of the method that's about to be invoked
     * @param testResult test result object for the method that's about to be invoked
     */
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        // NOTE: This method will never be called
    }

    /**
     * [IInvokedMethodListener]
     * Invoked after each test or configuration method is invoked by TestNG
     * 
     * @param method TestNG representation of the method that's just been invoked
     * @param testResult test result object for the method that's just been invoked
     */
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        // NOTE: This method will never be called
    }
    
    /**
     * [IInvokedMethodListener2]
     * Invoked before each test or configuration method is invoked by TestNG
     * 
     * @param method TestNG representation of the method that's about to be invoked
     * @param testResult test result object for the method that's about to be invoked
     * @param context test context
     */
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
        synchronized(methodListeners) {
            for (IInvokedMethodListener methodListener : Lists.reverse(methodListeners)) {
                methodListener.beforeInvocation(method, testResult);
            }
        }
        synchronized(methodListeners2) {
            for (IInvokedMethodListener2 methodListener : Lists.reverse(methodListeners2)) {
                methodListener.beforeInvocation(method, testResult, context);
            }
        }
    }

    /**
     * [IInvokedMethodListener2]
     * Invoked after each test or configuration method is invoked by TestNG
     * 
     * @param method TestNG representation of the method that's just been invoked
     * @param testResult test result object for the method that's just been invoked
     * @param context text context
     */
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
        synchronized(methodListeners) {
            for (IInvokedMethodListener methodListener : methodListeners) {
                methodListener.afterInvocation(method, testResult);
            }
        }
        synchronized(methodListeners2) {
            for (IInvokedMethodListener2 methodListener : methodListeners2) {
                methodListener.afterInvocation(method, testResult, context);
            }
        }
    }

    /**
     * [ITestListener]
     * Invoked each time before a test will be invoked.
     * The {@code ITestResult} is only partially filled with the references to
     * class, method, start millis and status.
     *
     * @param result the partially filled {@code ITestResult}
     * @see ITestResult#STARTED
     */
    @Override
    public void onTestStart(ITestResult result) {
        synchronized(testListeners) {
            for (ITestListener testListener : Lists.reverse(testListeners)) {
                testListener.onTestStart(result);
            }
        }
    }

    /**
     * [ITestListener]
     * Invoked each time a test succeeds.
     *
     * @param result {@code ITestResult} containing information about the run test
     * @see ITestResult#SUCCESS
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        synchronized (testListeners) {
            for (ITestListener testListener : testListeners) {
                testListener.onTestSuccess(result);
            }
        }
    }

    /**
     * [ITestListener]
     * Invoked each time a test fails.
     *
     * @param result {@code ITestResult} containing information about the run test
     * @see ITestResult#FAILURE
     */
    @Override
    public void onTestFailure(ITestResult result) {
        synchronized (testListeners) {
            for (ITestListener testListener : testListeners) {
                testListener.onTestFailure(result);
            }
        }
    }

    /**
     * [ITestListener]
     * Invoked each time a test is skipped.
     *
     * @param result {@code ITestResult} containing information about the run test
     * @see ITestResult#SKIP
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        synchronized (testListeners) {
            for (ITestListener testListener : testListeners) {
                testListener.onTestSkipped(result);
            }
        }
    }

    /**
     * [ITestListener]
     * Invoked each time a method fails but has been annotated with
     * successPercentage and this failure still keeps it within the
     * success percentage requested.
     *
     * @param result {@code ITestResult} containing information about the run test
     * @see ITestResult#SUCCESS_PERCENTAGE_FAILURE
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        synchronized (testListeners) {
            for (ITestListener testListener : testListeners) {
                testListener.onTestFailedButWithinSuccessPercentage(result);
            }
        }
    }

    /**
     * [ITestListener]
     * Invoked after the test class is instantiated and before
     * any configuration method is called.
     * 
     * @param context context for the test run
     */
    @Override
    public void onStart(ITestContext context) {
        synchronized (testListeners) {
            for (ITestListener testListener : Lists.reverse(testListeners)) {
                testListener.onStart(context);
            }
        }
    }

    /**
     * [ITestListener]
     * Invoked after all the tests have run and all their
     * Configuration methods have been called.
     * 
     * @param context context for the test run
     */
    @Override
    public void onFinish(ITestContext context) {
        synchronized (testListeners) {
            for (ITestListener testListener : testListeners) {
                testListener.onFinish(context);
            }
        }
    }

    /**
     * [IMethodInterceptor]
     * Invoked to enable alteration of the list of test methods that TestNG is about to run.
     * 
     * @param methods list of test methods.
     * @param context test context.
     * @return the list of test methods to run.
     */
    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        synchronized (methodInterceptors) {
            for (IMethodInterceptor interceptor : methodInterceptors) {
                methods = interceptor.intercept(methods, context);
            }
        }
        return methods;
    }

    /**
     * [IClassListener]
     * Invoked after the test class is instantiated and before
     * {@link org.testng.annotations.BeforeClass &#64;BeforeClass} 
     * configuration methods are called.
     * 
     * @param testClass TestNG representation for the current test class
     */
    @Override
    public void onBeforeClass(ITestClass testClass) {
        synchronized (classListeners) {
            for (IClassListener classListener : Lists.reverse(classListeners)) {
                classListener.onBeforeClass(testClass);
            }
        }
    }

    /**
     * [IClassListener]
     * Invoked after all of the test methods of the test class have been invoked
     * and before {@link org.testng.annotations.AfterClass &#64;AfterClass}
     * configuration methods are called.
     * 
     * @param testClass TestNG representation for the current test class
     */
    @Override
    public void onAfterClass(ITestClass testClass) {
        synchronized (classListeners) {
            for (IClassListener classListener : classListeners) {
                classListener.onAfterClass(testClass);
            }
        }
    }
    
    /**
     * Get reference to an instance of the specified listener type.
     * 
     * @param <T> listener type
     * @param result TestNG test result object
     * @param listenerType listener type
     * @return optional listener instance
     */
    public static <T extends ITestNGListener> Optional<T>
            getAttachedListener(ITestResult result, Class<T> listenerType) {
        
        Objects.requireNonNull(result, "[result] must be non-null");
        return getAttachedListener(result.getTestContext(), listenerType);
    }
    
    /**
     * Get reference to an instance of the specified listener type.
     * 
     * @param <T> listener type
     * @param context TestNG test context object
     * @param listenerType listener type
     * @return optional listener instance
     */
    public static <T extends ITestNGListener> Optional<T>
            getAttachedListener(ITestContext context, Class<T> listenerType) {
        
        Objects.requireNonNull(context, "[context] must be non-null");
        return getAttachedListener(context.getSuite(), listenerType);
    }
    
    /**
     * Get reference to an instance of the specified listener type.
     * 
     * @param <T> listener type
     * @param suite TestNG suite object
     * @param listenerType listener type
     * @return optional listener instance
     */
    @SuppressWarnings("unchecked")
    public static <T extends ITestNGListener> Optional<T>
            getAttachedListener(ISuite suite, Class<T> listenerType) {
        
        Objects.requireNonNull(suite, "[suite] must be non-null");
        Objects.requireNonNull(listenerType, "[listenerType] must be non-null");
        ListenerChain chain = (ListenerChain) suite.getAttribute(LISTENER_CHAIN);
        for (ITestNGListener listener : chain.listeners) {
            if (listener.getClass() == listenerType) {
                return Optional.of((T) listener);
            }
        }
        return Optional.empty();
    }

    /**
     * Attach linked listeners that are active on the test class that contains the specified test method.
     * 
     * @param testMethod test method
     */
    private void attachListeners(Method testMethod) {
        attachListeners(testMethod.getDeclaringClass());
    }
    
    /**
     * Attach linked listeners that are active on the test class defined by the specified test context. Note that only
     * one of the three parameters testClass, testCtor and testMethod will be non-null.
     * 
     * @param testClass If the annotation was found on a class, this parameter represents this class (null otherwise).
     * @param testCtor If the annotation was found on a constructor, this parameter represents this constructor (null
     *        otherwise).
     * @param testMethod If the annotation was found on a method, this parameter represents this method (null
     *        otherwise).
     */
    private void attachListeners(Class<?> testClass, Constructor<?> testCtor, Method testMethod) {
        if (testClass != null) {
            attachListeners(testClass);
        } else if (testCtor != null) {
            attachListeners(testCtor.getDeclaringClass());
        } else {
            attachListeners(testMethod.getDeclaringClass());
        }
    }
    
    /**
     * Attach linked listeners that are active on the specified test class.
     * 
     * @param testClass test class
     */
    private void attachListeners(Class<?> testClass) {
        LinkedListeners annotation = testClass.getAnnotation(LinkedListeners.class);
        if (null != annotation) {
            Class<?> markedClass = testClass;
            while (null == markedClass.getDeclaredAnnotation(LinkedListeners.class)) {
                markedClass = markedClass.getSuperclass();
            }
            if ( ! markedClasses.contains(markedClass)) {
                markedClasses.add(markedClass);
                for (Class<? extends ITestNGListener> listener : annotation.value()) {
                    attachListener(listener);
                }
            }
        }
    }
    
    /**
     * Wrap the current listener chain with an instance of the specified listener class.<br>
     * <b>NOTE</b>: The order in which listener methods are invoked is determined by the
     * order in which listener objects are added to the chain. Listener <i>before</i> methods
     * are invoked in last-added-first-called order. Listener <i>after</i> methods are invoked
     * in first-added-first-called order.<br>
     * <b>NOTE</b>: Only one instance of any given listener class will be included in the chain.
     * 
     * @param listener listener class to add to the chain
     */
    private void attachListener(Class<? extends ITestNGListener> listener) {
        if ( ! listenerSet.contains(listener)) {
            listenerSet.add(listener);
            try {
                ITestNGListener listenerObj = listener.newInstance();
                
                synchronized(listeners) {
                    listeners.add(listenerObj);
                }
                
                if (listenerObj instanceof IAnnotationTransformer3) {
                    synchronized(annotationXformers3) {
                        annotationXformers3.add((IAnnotationTransformer3) listenerObj);
                    }
                } else if (listenerObj instanceof IAnnotationTransformer2) {
                    synchronized(annotationXformers2) {
                        annotationXformers2.add((IAnnotationTransformer2) listenerObj);
                    }
                } else if (listenerObj instanceof IAnnotationTransformer) {
                    synchronized(annotationXformers) {
                        annotationXformers.add((IAnnotationTransformer) listenerObj);
                    }
                }
                
                if (listenerObj instanceof ISuiteListener) {
                    synchronized(suiteListeners) {
                        suiteListeners.add((ISuiteListener) listenerObj);
                    }
                }
                
                if (listenerObj instanceof IConfigurationListener2) {
                    synchronized(configListeners2) {
                        configListeners2.add((IConfigurationListener2) listenerObj);
                    }
                } else if (listenerObj instanceof IConfigurationListener) {
                    synchronized(configListeners) {
                        configListeners.add((IConfigurationListener) listenerObj);
                    }
                }
                
                if (listenerObj instanceof IInvokedMethodListener2) {
                    synchronized(methodListeners2) {
                        methodListeners2.add((IInvokedMethodListener2) listenerObj);
                    }
                } else if (listenerObj instanceof IInvokedMethodListener) {
                    synchronized(methodListeners) {
                        methodListeners.add((IInvokedMethodListener) listenerObj);
                    }
                }
                
                if (listenerObj instanceof ITestListener) {
                    synchronized(testListeners) {
                        testListeners.add((ITestListener) listenerObj);
                    }
                }
                
                if (listenerObj instanceof IMethodInterceptor) {
                    synchronized(methodInterceptors) {
                        methodInterceptors.add((IMethodInterceptor) listenerObj);
                    }
                }
                
                if (listenerObj instanceof IClassListener) {
                    synchronized(classListeners) {
                        classListeners.add((IClassListener) listenerObj);
                    }
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Unable to instantiate listener: " + listener.getName(), e);
            }
        }
    }
}
