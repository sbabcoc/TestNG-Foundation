package com.nordstrom.automation.testng;

import org.testng.ISuite;

/**
 * This interface provides the connection between {@link ListenerChain} and test classes that require the activation of
 * specific listeners, especially where the listeners must be invoked in a specific order. Test classes that implement
 * {@link ListenerChainable} get the opportunity to attach listeners to the chain before the SuiteRunner starts.
 * 
 * @see ListenerChain
 */
public interface ListenerChainable {
    
	/**
	 * This method is invoked by {@link ListenerChain#onStart(ISuite)} to provide the opportunity to attach listeners
	 * to the specified chain via that chain's {@link ListenerChain#around(Class) around(Class)} method. The order in
	 * which listener methods are invoked is determined by the order in which listener objects are added to the chain.
	 * 
	 * Listener <i>before</i> methods are invoked in <b>last-added-first-called</b> order.
	 * Listener <i>after</i> methods are invoked in <b>first-added-first-called</b> order.
	 * Only one instance of any given listener class will be included in the chain.
	 *  
	 * @param listenerChain chain to which listeners will be attached
	 */
    void attachListeners(ListenerChain listenerChain);
    
}
