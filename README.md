# INTRODUCTION

**TestNG Foundation** is a lightweight collection of TestNG listeners, interfaces, and static utility classes that supplement and augment the functionality provided by the TestNG API. The facilities provided by **TestNG Foundation** include two types of runtime listener hooks and automatic test context attribute propagation.

Future releases of **TestNG Foundation** will add automatic retry of failed tests, test execution timeout management, and target platform support. See [ExecutionFlowController](https://git.nordstrom.net/projects/MFATT/repos/testng-foundation/browse/src/main/java/com/nordstrom/automation/testng/ExecutionFlowController.java) for more information.

## TestNG Listeners

* [ExecutionFlowController](https://git.nordstrom.net/projects/MFATT/repos/testng-foundation/browse/src/main/java/com/nordstrom/automation/testng/ExecutionFlowController.java):  
**ExecutionFlowController** is a TestNG listener that propagates test context attributes:  
 [_before_ method] → [test method] → [_after_ method]  
 For test classes that implement the **IInvokedMethodListenerEx** interface, **ExecutionFlowController** forwards calls from its own invoked method listener implementation to the corresponding methods in the test class. Inbound attribute propagation is performed prior to `beforeInvocation()`, and outbound attribute propagation is performed following `afterInvocation()`.
* [ListenerChain](https://git.nordstrom.net/projects/MFATT/repos/testng-foundation/browse/src/main/java/com/nordstrom/automation/testng/ListenerChain.java):  
**ListenerChain** is a TestNG listener that enables you to add other listeners at runtime and guarantees the order in which they're invoked. This is similar in behavior to a JUnit rule chain.

## Interfaces

* [IInvokedMethodListenerEx](https://git.nordstrom.net/projects/MFATT/repos/testng-foundation/browse/src/main/java/com/nordstrom/automation/testng/IInvokedMethodListenerEx.java):  
Test classes that implement the **IInvokedMethodListenerEx** interface are hooked in by the invoked method listener implementation of **ExecutionFlowController**. See the **TestNG Listeners** section above for more details.
* [ListenerChainable](https://git.nordstrom.net/projects/MFATT/repos/testng-foundation/browse/src/main/java/com/nordstrom/automation/testng/ListenerChainable.java):  
 Test classes that implement the **ListenerChainable** interface get the opportunity to attach listeners to the chain before the **SuiteRunner** starts.

## Static Utility Classes

* [PropertyManager](https://git.nordstrom.net/projects/MFATT/repos/testng-foundation/browse/src/main/java/com/nordstrom/automation/testng/PropertyManager.java):  
**PropertyManager** contains two static methods used to propagate attributes from one test context to another:
  * `extractAttributes()` - Extracts all of the attributes of the specified test context into a map.
  * `injectAttributes()` - Injects all of the entries of the specified map into the specified test context as attributes.

## **ExecutionFlowController**, **ListenerChain**, and the **ServiceLoader**

If **ExecutionFlowController** is the only listener you need, or if the order in which your listeners are invoked is inconsequential, the TestNG **@Listeners** annotation is a perfectly fine method to activate your listeners. However, if you need to activate multiple listeners that must be invoked in a specific order, use **ListenerChain** and activate it via the **ServiceLoader** as described in the [**TestNG** documentation](http://testng.org/doc/documentation-main.html#listeners-service-loader):

###### org.testng.ITestNGListener
```
com.nordstrom.automation.testng.ListenerChain
```

In a Maven project, the preceding file is stored in the <span style="color:blue">src/main/resources</span> folder:

![com.testng.ITestNGListener](docs/images/META-INF.png)

Once this file is added to your project, <span style="color:blue">ListenerChain</span> will be loaded automatically whenever you run your tests. To request dynamic listener chaining, your test class implements the <span style="color:blue">ListenerChainable</span> interface:

###### Implementing ListenerChainable
```java
package com.nordstrom.example;
 
import com.nordstrom.automation.selenium.listeners.DriverManager;
import com.nordstrom.automation.testng.ExecutionFlowController;
import com.nordstrom.automation.testng.ListenerChain;
import com.nordstrom.automation.testng.ListenerChainable;
 
public class ExampleTest implements ListenerChainable {
     
    ...
  
    @Override
    public void attachListeners(ListenerChain listenerChain) {
        listenerChain.around(DriverManager.class).around(ExecutionFlowController.class);
    }
}
```

As shown above, we use the **`attachListeners()`** callback to attach <span style="color:blue">DriverManager</span> and <span style="color:blue">ExecutionFlowController</span>. The order in which listener methods are invoked is determined by the order in which listener objects are added to the chain. Listener _before_ methods are invoked in <span style="color:yellowgreen">last-added-first-called</span> order. Listener _after_ methods are invoked in <span style="color:yellowgreen">first-added-first-called</span> order. Only one instance of any given listener class will be included in the chain.
