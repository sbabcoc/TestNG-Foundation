[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.nordstrom.tools/testng-foundation/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.nordstrom.tools/testng-foundation)

# INTRODUCTION

**TestNG Foundation** is a lightweight collection of TestNG listeners, interfaces, and static utility classes that supplement and augment the functionality provided by the TestNG API. The facilities provided by **TestNG Foundation** include two types of runtime listener hooks, test artifact capture, and automatic test context attribute propagation.

Future releases of **TestNG Foundation** will add automatic retry of failed tests, test execution timeout management, and target platform support. See [ExecutionFlowController](https://github.com/Nordstrom/TestNG-Foundation/blob/master/src/main/java/com/nordstrom/automation/testng/ExecutionFlowController.java) for more information.

## TestNG Listeners

* [ExecutionFlowController](https://github.com/Nordstrom/TestNG-Foundation/blob/master/src/main/java/com/nordstrom/automation/testng/ExecutionFlowController.java):  
**ExecutionFlowController** is a TestNG listener that propagates test context attributes:  
 [_before_ method] → [test method] → [_after_ method]  
 For test classes that implement the **IInvokedMethodListenerEx** interface, **ExecutionFlowController** forwards calls from its own invoked method listener implementation to the corresponding methods in the test class. In-bound attribute propagation is performed before forwarding the `beforeInvocation(IInvokedMethod, ITestResult)` call, and out-bound attribute propagation is performed after forwarding the `afterInvocation(IInvokedMethod, ITestResult)` call.
* [ListenerChain](https://github.com/Nordstrom/TestNG-Foundation/blob/master/src/main/java/com/nordstrom/automation/testng/ListenerChain.java):  
**ListenerChain** is a TestNG listener that enables you to add other listeners at runtime and guarantees the order in which they're invoked. This is similar in behavior to a JUnit rule chain. **ListenerChain** also provides static methods that enable you to acquire references to listeners that are linked into the chain.
* [ArtifactCollector](https://github.com/Nordstrom/TestNG-Foundation/blob/master/src/main/java/com/nordstrom/automation/testng/ArtifactCollector.java):  
**ArtifactCollector** is a TestNG [test listener](http://javadox.com/org.testng/testng/6.8/org/testng/ITestListener.html) that serves as the foundation for artifact-capturing test listeners. This is a generic class, with the artifact-specific implementation provided by instances of the **ArtifactType** interface. See the **Interfaces** section below for more details.

## Interfaces

* [LinkedListener](https://github.com/Nordstrom/TestNG-Foundation/blob/master/src/main/java/com/nordstrom/automation/testng/LinkedListener.java):  
This is a marker interface for listeners that can be linked to the **ListenerChain** via its service loader.
* [IInvokedMethodListenerEx](https://github.com/Nordstrom/TestNG-Foundation/blob/master/src/main/java/com/nordstrom/automation/testng/IInvokedMethodListenerEx.java):  
Test classes that implement the **IInvokedMethodListenerEx** interface are hooked in by the invoked method listener implementation of **ExecutionFlowController**. See the **TestNG Listeners** section above for more details.
* [ArtifactType](https://github.com/Nordstrom/TestNG-Foundation/blob/master/src/main/java/com/nordstrom/automation/testng/ArtifactType.java):  
Classes that implement the **ArtifactType** interface provide the artifact-specific methods used by the **ArtifactCollector** listener to capture and store test-related artifacts. The unit tests for this project include a reference implementation (**UnitTestArtifact**) provides a basic outline for a scenario-specific artifact provider. This artifact provider is specified as the superclass type parameter in the **UnitTestCapture** listener, which is a lightweight extension of **ArtifactCollector**. The most basic example is shown below:

###### Implementing ArtifactType
```java
package com.example;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

import com.nordstrom.automation.testng.ArtifactType;

public class MyArtifactType implements ArtifactType {
    
    private static final String ARTIFACT_PATH = "artifacts";
    private static final String EXTENSION = "txt";
    private static final String ARTIFACT = "This text artifact was captured for '%s'";
    private static final Logger LOGGER = LoggerFactory.getLogger(MyArtifactType.class);

    @Override
    public boolean canGetArtifact(ITestResult result) {
        return true;
    }

    @Override
    public byte[] getArtifact(ITestResult result) {
        return String.format(ARTIFACT, result.getName()).getBytes().clone();
    }

    @Override
    public Path getArtifactPath(ITestResult result) {
        return ArtifactType.super.getArtifactPath(result).resolve(ARTIFACT_PATH);
    }
    
    @Override
    public String getArtifactExtension() {
        return EXTENSION;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }
}
```

###### Creating a type-specific artifact collector
```java
package com.example;

import com.nordstrom.automation.testng.ArtifactCollector;

public class MyArtifactCapture extends ArtifactCollector<MyArtifactType> {
    
    public MyArtifactCapture() {
        super(new MyArtifactType());
    }
    
}
```

The preceding code is an example of how the artifact type definition is assigned as the type parameter in a subclass of **ArtifactCollector**. Because TestNG listeners are specified solely by their class, type-specific artifact collectors must be declared this way.

## Annotations

* [LinkedListeners](https://github.com/Nordstrom/TestNG-Foundation/blob/master/src/main/java/com/nordstrom/automation/testng/LinkedListeners.java):  
To attach listeners to an active **ListenerChain**, mark your test class with the **`@LinkedListeners`** annotation.

## Static Utility Classes

* [PropertyManager](https://github.com/Nordstrom/TestNG-Foundation/blob/master/src/main/java/com/nordstrom/automation/testng/PropertyManager.java):  
**PropertyManager** contains two static methods used to propagate attributes from one test context to another:
  * `extractAttributes()` - Extracts all of the attributes of the specified test context into a map.
  * `injectAttributes()` - Injects all of the entries of the specified map into the specified test context as attributes.

## **ExecutionFlowController**, **ListenerChain**, and the **ServiceLoader**

If **ExecutionFlowController** is the only listener you need, or if the order in which your listeners are invoked is inconsequential, the TestNG **`@Listeners`** annotation is a perfectly fine method to activate your listeners. However, if you need to activate multiple listeners that must be invoked in a specific order, use **ListenerChain** and activate it via the **ServiceLoader** as described in the [**TestNG** documentation](http://testng.org/doc/documentation-main.html#listeners-service-loader):

###### org.testng.ITestNGListener
```
com.nordstrom.automation.testng.ListenerChain
```

In a Maven project, the preceding file is stored in the **_src/main/resources_** folder:

![com.testng.ITestNGListener](docs/images/META-INF.png)

Once this file is added to your project, **ListenerChain** will be loaded automatically whenever you run your tests. To link listeners into the chain, you have two options:

1. Specify listeners to attach via the **ListenerChain** service loader.
2. Mark your test class with the **`@LinkedListeners`** annotation.

These options are not mutually exclusive; you can freely apply both within the same project.

### Specifying listeners to attach via the **ListenerChain** service loader

Listeners that you wish to attach via the **ListenerChain** service loader must implement the **LinkedListener** interface: 

###### Service-loaded listener example
```java
package com.example

import org.testng.IClassListener;

import com.nordstrom.automation.testng.LinkedListener;

public class ServiceLoadedListener implements IClassListener, LinkedListener {

    @Override
    public void onBeforeClass(ITestClass testClass) {
        ...
    }

    @Override
    public void onAfterClass(ITestClass testClass) {
        ...
    }
}

```

To specify the listener(s) you wish to attach via the **ListenerChain** service loader, create a file at location  
**_META-INF/services/com.nordstrom.automation.testng.LinkedListener_** and indicate the listener(s) you want to be linked in:

###### com.nordstrom.automation.testng.LinkedListener
```
com.example.ServiceLoadedListener
```

In a Maven project, the preceding file is stored in the **_src/main/resources_** folder:

![com.testng.ITestNGListener](docs/images/META-INF-2.png)

In this example, we've specified a single listener (**ServiceLoadedListener**) that should be attached via the **ListenerChain** service loader. If additional listeners had been specified, each of them would be attached in the order they're specified.

### Marking your test class with the **`@LinkedListeners`** annotation

With the **`@LinkedListeners`** annotation, you specify one or more listener types to attach to the **ListenerChain**:

###### LinkedListeners annotation
```java
package com.example;
 
import com.nordstrom.automation.selenium.listeners.DriverListener;
import com.nordstrom.automation.testng.ExecutionFlowController;
import com.nordstrom.automation.testng.LinkedListeners;
import com.nordstrom.automation.testng.ListenerChain;
 
@LinkedListeners({DriverListener.class, ExecutionFlowController.class})
public class ExampleTest {
    
    ...
    
}
```

As shown above, we use the **`@LinkedListeners`** annotation to attach **DriverListener** and **ExecutionFlowController**. The order in which listener methods are invoked is determined by the order in which listener objects are added to the chain. Listener _before_ methods are invoked in **_last-added-first-called_** order. Listener _after_ methods are invoked in **_first-added-first-called_** order. Only one instance of any given listener class will be included in the chain.

### **ExecutionFlowController** managed features: Method timeout and retry analyzer
The annotation transformer of **ExecutionFlowController** applies the configuration for two managed features to their corresponding attributes in the **`@Test`** annotation:

* The **TEST_TIMEOUT** setting specifies the global test timeout interval (in milliseconds). 
