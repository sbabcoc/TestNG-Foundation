package com.nordstrom.automation.testng;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;

import org.testng.IAnnotationTransformer;
import org.testng.IClassListener;
import org.testng.IConfigurationListener;
import org.testng.IExecutionListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

/**
 * This TestNG listener enables the addition of other listeners at runtime and guarantees the order in which they're
 * invoked. This is similar in behavior to a JUnit rule chain.
 */
public abstract class AbstractListenerChain implements IAnnotationTransformer, IExecutionListener, ISuiteListener,
		IConfigurationListener, IInvokedMethodListener, ITestListener, IMethodInterceptor, IClassListener {
    
    private Set<Class<?>> markedClasses = Collections.synchronizedSet(new HashSet<Class<?>>());
    private Set<Class<? extends ITestNGListener>> listenerSet = 
            Collections.synchronizedSet(new HashSet<Class<? extends ITestNGListener>>());
    
    protected List<ITestNGListener> listeners;
    protected List<IAnnotationTransformer> annotationXformers;
    protected List<IExecutionListener> executionListeners;
    protected List<ISuiteListener> suiteListeners;
    protected List<IConfigurationListener> configListeners;
    protected List<IInvokedMethodListener> methodListeners;
    protected List<ITestListener> testListeners;
    protected List<IMethodInterceptor> methodInterceptors;
    protected List<IClassListener> classListeners;
    
    private static final String LISTENER_CHAIN = "ListenerChain";
    
    public AbstractListenerChain() {
        initialize();
        for (LinkedListener listener : ServiceLoader.load(LinkedListener.class)) {
            attachListener(null, listener);
        }
    }

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
    }

    /**
     * [IExecutionListener]
     * Invoked before the TestNG run starts.
     */
    @Override
    public void onExecutionStart() {
        synchronized(executionListeners) {
            for (IExecutionListener executionListener : executionListeners) {
                executionListener.onExecutionStart();
            }
        }
    }

    /**
     * [IExecutionListener]
     * Invoked once all the suites have been run.
     */
    @Override
    public void onExecutionFinish() {
        synchronized(executionListeners) {
            for (IExecutionListener executionListener : executionListeners) {
                executionListener.onExecutionFinish();
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
     * [IInvokedMethodListener(2)]
     * Invoked before each test or configuration method is invoked by TestNG
     * 
     * @param method TestNG representation of the method that's about to be invoked
     * @param testResult test result object for the method that's about to be invoked
     * @param context test context
     */
    // @Override omitted to avoid interface conflict
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
        synchronized(methodListeners) {
            for (IInvokedMethodListener methodListener : Lists.reverse(methodListeners)) {
                methodListener.beforeInvocation(method, testResult);
            }
        }
    }

    /**
     * [IInvokedMethodListener(2)]
     * Invoked after each test or configuration method is invoked by TestNG
     * 
     * @param method TestNG representation of the method that's just been invoked
     * @param testResult test result object for the method that's just been invoked
     * @param context text context
     */
    // @Override omitted to avoid interface conflict
    public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
        synchronized(methodListeners) {
            for (IInvokedMethodListener methodListener : methodListeners) {
                methodListener.afterInvocation(method, testResult);
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
    public static <T extends ITestNGListener> Optional<T>
            getAttachedListener(ISuite suite, Class<T> listenerType) {
        
        Objects.requireNonNull(suite, "[suite] must be non-null");
        Objects.requireNonNull(listenerType, "[listenerType] must be non-null");
        AbstractListenerChain chain = (AbstractListenerChain) suite.getAttribute(LISTENER_CHAIN);
        Objects.requireNonNull(chain, "Specified suite has no ListenerChain");
        return chain.getAttachedListener(listenerType);
    }

    /**
     * Get reference to an instance of the specified listener type.
     * 
     * @param <T> listener type
     * @param listenerType listener type
     * @return optional listener instance
     */
    @SuppressWarnings("unchecked")
    public <T extends ITestNGListener> Optional<T> getAttachedListener(Class<T> listenerType) {
        for (ITestNGListener listener : listeners) {
            if (listener.getClass() == listenerType) {
                return Optional.of((T) listener);
            }
        }
        return Optional.absent();
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
    protected void attachListeners(Class<?> testClass, Constructor<?> testCtor, Method testMethod) {
        if (testClass != null) {
            processLinkedListeners(testClass);
        } else if (testCtor != null) {
            processLinkedListeners(testCtor.getDeclaringClass());
        } else if (testMethod != null) {
            processLinkedListeners(testMethod.getDeclaringClass());
        }
    }
    
    /**
     * Attach linked listeners that are active on the test class that contains the specified test method.
     * 
     * @param testMethod test method
     */
    protected void attachListeners(Method testMethod) {
        if (testMethod != null) {
            processLinkedListeners(testMethod.getDeclaringClass());
        }
    }
    
    /**
     * Attach linked listeners that are active on the specified test class.
     * 
     * @param testClass test class
     */
    protected void attachListeners(Class<?> testClass) {
        if (testClass != null) {
            processLinkedListeners(testClass);
        }
    }
    
    /**
     * Process the {@link LinkedListeners} annotation of the specified test class.
     * 
     * @param testClass test class
     */
    protected void processLinkedListeners(Class<?> testClass) {
        Objects.requireNonNull(testClass, "[testClass] must be non-null");
        
        LinkedListeners annotation = testClass.getAnnotation(LinkedListeners.class);
        if (null != annotation) {
            Class<?> markedClass = getMarkedClass(testClass);
            if ( ! markedClasses.contains(markedClass)) {
                markedClasses.add(markedClass);
                for (Class<? extends ITestNGListener> listener : annotation.value()) {
                    attachListener(listener, null);
                }
            }
        }
    }
    
    protected void initialize() {
        listeners = new ArrayList<>();
        annotationXformers = new ArrayList<>();
        executionListeners = new ArrayList<>();
        suiteListeners = new ArrayList<>();
        configListeners = new ArrayList<>();
        methodListeners = new ArrayList<>();
        testListeners = new ArrayList<>();
        methodInterceptors = new ArrayList<>();
        classListeners = new ArrayList<>();
    }
    
    /**
     * Wrap the current listener chain with an instance of the specified listener class.
     * <p>
     * <b>NOTE</b>: The order in which listener methods are invoked is determined by the
     * order in which listener objects are added to the chain. Listener <i>before</i> methods
     * are invoked in last-added-first-called order. Listener <i>after</i> methods are invoked
     * in first-added-first-called order.<br>
     * <b>NOTE</b>: Only one instance of any given listener class will be included in the chain.
     * 
     * @param listenerTyp listener class to add to the chain (may be 'null')
     * @param listenerObj listener object to add to the chain (may be 'null')
     */
    public void attachListener(Class<? extends ITestNGListener> listenerTyp, ITestNGListener listenerObj) { //NOSONAR
        Class<? extends ITestNGListener> type;
        ITestNGListener object;
        
        if ((listenerTyp == null) && (listenerObj == null)) {
            throw new IllegalArgumentException("Neither [listenerTyp] nor [listenerObj] was specified");
        } else if (listenerObj != null) {
            object = listenerObj;
            type = listenerObj.getClass();
        } else {
            object = null;
            type = listenerTyp;
        }
        
        if ( ! listenerSet.contains(type)) { //NOSONAR
            listenerSet.add(type);
            
            if (object == null) {
                try {
                    object = type.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    throw new RuntimeException("Unable to instantiate listener: " + type.getName(), e);
                }
            }
            
            synchronized(listeners) {
                listeners.add(object);
            }
            
            bucketizeListener(object);
        }
    }

    protected void bucketizeListener(ITestNGListener object) {
        if (object instanceof IExecutionListener) {
            synchronized(executionListeners) {
                executionListeners.add((IExecutionListener) object);
            }
        }
        
        if (object instanceof ISuiteListener) {
            synchronized(suiteListeners) {
                suiteListeners.add((ISuiteListener) object);
            }
        }
        
        if (object instanceof ITestListener) {
            synchronized(testListeners) {
                testListeners.add((ITestListener) object);
            }
        }
        
        if (object instanceof IMethodInterceptor) {
            synchronized(methodInterceptors) {
                methodInterceptors.add((IMethodInterceptor) object);
            }
        }
        
        if (object instanceof IClassListener) {
            synchronized(classListeners) {
                classListeners.add((IClassListener) object);
            }
        }
    }
    
    /**
     * Get the class in the hierarchy of the specified test class that declares the {@link LinkedListeners} annotation.
     * 
     * @param testClass test class to be evaluated
     * @return class that declares the {@link LinkedListeners} annotation; {@code null} if annotation isn't found
     */
    private static Class<?> getMarkedClass(Class<?> testClass) {
        for (Class<?> thisClass = testClass; thisClass != null; thisClass = thisClass.getSuperclass()) {
            for (Annotation annotation : thisClass.getDeclaredAnnotations()) {
                if (annotation.annotationType().isAssignableFrom(LinkedListeners.class)) {
                    return thisClass;
                }
            }
        }
        return null;
    }
}
