package com.nordstrom.automation.testng;

import java.util.List;

import org.testng.IMethodInstance;
import org.testng.ITestContext;

public class ChainedListener extends AbstractChainedListener {

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        interceptor.add(context.getName());
        return methods;
    }

}
