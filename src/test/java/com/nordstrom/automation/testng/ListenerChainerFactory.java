package com.nordstrom.automation.testng;

import org.testng.annotations.Factory;

public class ListenerChainerFactory {
    public int invokeCount;

    @Factory
    public Object[] createInstances() {
        return new Object[] { new FactoryProduct() }; 
    }
}