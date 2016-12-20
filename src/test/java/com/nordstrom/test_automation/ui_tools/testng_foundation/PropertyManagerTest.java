package com.nordstrom.test_automation.ui_tools.testng_foundation;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.google.common.collect.Maps;

public class PropertyManagerTest {
	
	private static final String BOOLEAN_KEY = "BOOLEAN";
	private static final Boolean BOOLEAN_VAL = Boolean.TRUE;
	private static final String STRING_KEY = "STRING";
	private static final String STRING_VAL = "This is a string.";
	private static final String OBJECT_KEY = "OBJECT";
	private static final BigDecimal OBJECT_VAL = new BigDecimal("3.14159265359");
	
	private static final String[] SET_VALUES = new String[]{ BOOLEAN_KEY, STRING_KEY, OBJECT_KEY };
	
	private static final Set<String> expectNames;
	private static final Map<String, Object> attributeMap;
	
	static {
		Map<String, Object> map = Maps.newHashMap();
		map.put(BOOLEAN_KEY, BOOLEAN_VAL);
		map.put(STRING_KEY, STRING_VAL );
		map.put(OBJECT_KEY, OBJECT_VAL);
		attributeMap = Collections.unmodifiableMap(map);
		expectNames = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(SET_VALUES)));
	}
	
	@Test
	public void testAttributeExtraction() {
		
		ITestResult testResult = Reporter.getCurrentTestResult();
		
		testResult.setAttribute(BOOLEAN_KEY, BOOLEAN_VAL);
		testResult.setAttribute(STRING_KEY, STRING_VAL);
		testResult.setAttribute(OBJECT_KEY, OBJECT_VAL);
		Map<String, Object> extractedMap = PropertyManager.extractAttributes(testResult);
		
		assertEquals(extractedMap, attributeMap, "Extracted map differs from expected map");
		
	}

	@Test
	public void testAttributeInjection() {
		
		ITestResult testResult = Reporter.getCurrentTestResult();
		
		PropertyManager.injectAttributes(attributeMap, testResult);
		Set<String> actualNames = testResult.getAttributeNames();
		
		assertEquals(actualNames, expectNames, "Actual names differ from expected names");
		assertEquals(testResult.getAttribute(BOOLEAN_KEY), BOOLEAN_VAL, "Boolean value is incorrect");
		assertEquals(testResult.getAttribute(STRING_KEY), STRING_VAL, "String value is incorrect");
		assertEquals(testResult.getAttribute(OBJECT_KEY), OBJECT_VAL, "Object value is incorrect");
		
	}

}
