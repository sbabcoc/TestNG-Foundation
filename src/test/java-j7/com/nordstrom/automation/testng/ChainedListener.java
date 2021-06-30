package com.nordstrom.automation.testng;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer3;
import org.testng.IConfigurationListener2;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IFactoryAnnotation;
import org.testng.annotations.IListenersAnnotation;

public class ChainedListener extends AbstractChainedListener
        implements IAnnotationTransformer3, IConfigurationListener2 {
    
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
    
}