package com.nordstrom.automation.testng;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.testng.IAnnotationTransformer;
import org.testng.IAnnotationTransformer2;
import org.testng.IAnnotationTransformer3;
import org.testng.IConfigurationListener;
import org.testng.IConfigurationListener2;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IInvokedMethodListener2;
import org.testng.ITestContext;
import org.testng.ITestNGListener;
import org.testng.ITestResult;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IFactoryAnnotation;
import org.testng.annotations.IListenersAnnotation;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.InvokedMethod;

import com.google.common.collect.Lists;

/**
 * This TestNG listener enables the addition of other listeners at runtime and guarantees the order in which they're
 * invoked. This is similar in behavior to a JUnit rule chain.
 */
public class ListenerChain extends AbstractListenerChain
                implements IAnnotationTransformer3, IConfigurationListener2, IInvokedMethodListener2 {
    
    private List<IAnnotationTransformer2> annotationXformers2;
    private List<IAnnotationTransformer3> annotationXformers3;
    private List<IConfigurationListener2> configListeners2;
    private List<IInvokedMethodListener2> methodListeners2;
    
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
        
        super.transform(annotation, testClass, testCtor, testMethod);
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
     * [IConfigurationListener]
     * Invoked whenever a configuration method succeeded.
     * 
     * @param itr test result object for the associated configuration method
     */
    @Override
    public void onConfigurationSuccess(ITestResult itr) {
        super.onConfigurationSuccess(itr);
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
        super.onConfigurationFailure(itr);
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
        super.onConfigurationSkip(itr);
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
     * [ITestListener]
     * Invoked each time a test is skipped.
     *
     * @param result {@code ITestResult} containing information about the run test
     * @see ITestResult#SKIP
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        
        // >>>>> ENTER workaround for TestNG bug
        // https://github.com/cbeust/testng/issues/1602
        ITestContext context = result.getTestContext();
        IInvokedMethod method = new InvokedMethod(
                        result.getTestClass(), result.getMethod(), System.currentTimeMillis(), result);
        
        beforeInvocation(method, result, context);
        // <<<<< LEAVE workaround for TestNG bug
        
        super.onTestSkipped(result);
    }

    @Override
    protected void initialize() {
        super.initialize();
        annotationXformers2 = new ArrayList<>();
        annotationXformers3 = new ArrayList<>();
        configListeners2 = new ArrayList<>();
        methodListeners2 = new ArrayList<>();
    }
    
    @Override
    protected void bucketizeListener(ITestNGListener object) {
        super.bucketizeListener(object);
        
        if (object instanceof IAnnotationTransformer3) {
            synchronized(annotationXformers3) {
                annotationXformers3.add((IAnnotationTransformer3) object);
            }
        } else if (object instanceof IAnnotationTransformer2) {
            synchronized(annotationXformers2) {
                annotationXformers2.add((IAnnotationTransformer2) object);
            }
        } else if (object instanceof IAnnotationTransformer) {
            synchronized(annotationXformers) {
                annotationXformers.add((IAnnotationTransformer) object);
            }
        }
        
        if (object instanceof IConfigurationListener2) {
            synchronized(configListeners2) {
                configListeners2.add((IConfigurationListener2) object);
            }
        } else if (object instanceof IConfigurationListener) {
            synchronized(configListeners) {
                configListeners.add((IConfigurationListener) object);
            }
        }
        
        if (object instanceof IInvokedMethodListener2) {
            synchronized(methodListeners2) {
                methodListeners2.add((IInvokedMethodListener2) object);
            }
        } else if (object instanceof IInvokedMethodListener) {
            synchronized(methodListeners) {
                methodListeners.add((IInvokedMethodListener) object);
            }
        }
    }
}
