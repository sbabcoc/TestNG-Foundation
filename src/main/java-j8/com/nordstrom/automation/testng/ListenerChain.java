package com.nordstrom.automation.testng;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.IConfigurationListener;
import org.testng.IInvokedMethodListener;
import org.testng.ITestNGListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IFactoryAnnotation;
import org.testng.annotations.IListenersAnnotation;

public class ListenerChain extends AbstractListenerChain {

    /**
     * [IConfigurationListener]
     * Invoked before a configuration method is invoked.
     *
     * @param tr The test result
     */
    @Override
    public void beforeConfiguration(ITestResult tr) {
        synchronized(configListeners) {
            for (IConfigurationListener configListener : configListeners) {
                configListener.beforeConfiguration(tr);
            }
        }
    }

    /**
     * [IConfigurationListener]
     * Invoked before a configuration method is invoked.
     *
     * @param tr The test result
     * @param tm The test method
     */
    @Override
    public void beforeConfiguration(ITestResult tr, ITestNGMethod tm) {
        synchronized(configListeners) {
            for (IConfigurationListener configListener : configListeners) {
                configListener.beforeConfiguration(tr, tm);
            }
        }
    }
    
    /**
     * [IAnnotationTransformer]
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

        synchronized(annotationXformers) {
            for (IAnnotationTransformer annotationXformer : annotationXformers) {
                annotationXformer.transform(annotation, testClass, testCtor, testMethod);
            }
        }
    }

    /**
     * [IAnnotationTransformer]
     * Transform an IDataProvider annotation.
     * 
     * @param annotation The data provider annotation.
     * @param method The method annotated with the IDataProvider annotation.
     */
    @Override
    public void transform(IDataProviderAnnotation annotation, Method method) {
        attachListeners(method);
        
        synchronized(annotationXformers) {
            for (IAnnotationTransformer annotationXformer : annotationXformers) {
                annotationXformer.transform(annotation, method);
            }
        }
    }

    /**
     * [IAnnotationTransformer]
     * Transform an IFactory annotation.
     * 
     * @param annotation The factory annotation.
     * @param method The method annotated with the IFactory annotation.
     */
    @Override
    public void transform(IFactoryAnnotation annotation, Method method) {
        attachListeners(method);
        
        synchronized(annotationXformers) {
            for (IAnnotationTransformer annotationXformer : annotationXformers) {
                annotationXformer.transform(annotation, method);
            }
        }
    }

    /**
     * [IAnnotationTransformer]
     * Transform a Listeners annotation.
     * 
     * @param annotation The listeners annotation.
     * @param testClass The test class annotated with the Listeners annotation.
     */
    @Override
    @SuppressWarnings("rawtypes")
    public void transform(IListenersAnnotation annotation, Class testClass) {
        attachListeners(testClass);
        
        synchronized(annotationXformers) {
            for (IAnnotationTransformer annotationXformer : annotationXformers) {
                annotationXformer.transform(annotation, testClass);
            }
        }
    }

    @Override
    protected void bucketizeListener(ITestNGListener object) {
        super.bucketizeListener(object);
        
        if (object instanceof IAnnotationTransformer) {
            synchronized(annotationXformers) {
                annotationXformers.add((IAnnotationTransformer) object);
            }
        }
        
        if (object instanceof IConfigurationListener) {
            synchronized(configListeners) {
                configListeners.add((IConfigurationListener) object);
            }
        }
        
        if (object instanceof IInvokedMethodListener) {
            synchronized(methodListeners) {
                methodListeners.add((IInvokedMethodListener) object);
            }
        }
    }
    
}
