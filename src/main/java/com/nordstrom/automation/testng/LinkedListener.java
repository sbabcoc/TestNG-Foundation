package com.nordstrom.automation.testng;

import org.testng.ITestNGListener;

/**
 * This is a marker interface for listeners that can be linked to the {@link ListenerChain} via its service loader.
 */
public interface LinkedListener extends ITestNGListener {

}
