package com.nordstrom.automation.testng;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

@LinkedListeners({ChainedListener.class, ExecutionFlowController.class})
public class ConstructorFactory {

    public ConstructorFactory() {
    }

    @Factory(dataProvider = "ints")
    public ConstructorFactory(final int i) {
        // not important
    }

    @DataProvider
    public Object[] ints() {
        return new Object[]{1, 2, 3};
    }

    @Test
    public void fakeTest() {
        // not important
    }
}
