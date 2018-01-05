package com.nordstrom.automation.testng;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class ConstructorFactory {

    public ConstructorFactory(){}

    @DataProvider
    public Object[] ints(){return new Object[]{1, 2, 3};}

    @Factory(dataProvider = "ints", dataProviderClass = ConstructorFactory.class)
    public ConstructorFactory(int i){ }

    @Test
    public void test(){}
}
