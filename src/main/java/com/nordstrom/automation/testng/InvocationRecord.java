package com.nordstrom.automation.testng;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import org.testng.ITestResult;

/**
 * 
 */
class InvocationRecord {
    
    final Method method;
    final String suiteName;
    final String testName;
    final String className;
    final String methodName;
    final Object[] parameters;
    
    /**
     * 
     * @param result
     */
    InvocationRecord(ITestResult result) {
        method = result.getMethod().getConstructorOrMethod().getMethod();
        suiteName = result.getTestContext().getSuite().getName();
        testName = result.getTestContext().getName();
        className = result.getMethod().getRealClass().getName();
        methodName = result.getMethod().getMethodName();
        parameters = result.getParameters();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((suiteName == null) ? 0 : suiteName.hashCode());
        result = prime * result
                + ((testName == null) ? 0 : testName.hashCode());
        result = prime * result
                + ((className == null) ? 0 : className.hashCode());
        result = prime * result
                + ((methodName == null) ? 0 : methodName.hashCode());
        result = prime * result + Arrays.hashCode(parameters);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof InvocationRecord)) {
            return false;
        }
        InvocationRecord other = (InvocationRecord) obj;
        if (suiteName == null) {
            if (other.suiteName != null) {
                return false;
            }
        } else if (!suiteName.equals(other.suiteName)) {
            return false;
        }
        if (testName == null) {
            if (other.testName != null) {
                return false;
            }
        } else if (!testName.equals(other.testName)) {
            return false;
        }
        if (className == null) {
            if (other.className != null) {
                return false;
            }
        } else if (!className.equals(other.className)) {
            return false;
        }
        if (methodName == null) {
            if (other.methodName != null) {
                return false;
            }
        } else if (!methodName.equals(other.methodName)) {
            return false;
        }
        if (!Arrays.equals(parameters, other.parameters)) {
            return false;
        }
        return true;
    }
    
    /**
     * Get signature string for the specified method object.
     * 
     * @param method method object
     * @return signature string for the specified method object
     */
    public static String getQualifiedName(Method method) {
        String methodSig = method.toString();
        int endIndex = methodSig.lastIndexOf('(');
        int midIndex = methodSig.lastIndexOf('.', endIndex - 1);
        int beginIndex = methodSig.lastIndexOf('.', midIndex - 1) + 1;
        return methodSig.substring(beginIndex, endIndex);
    }
    
    @Override
    public String toString() {
        int len = parameters.length;
        StringBuilder builder = new StringBuilder(getQualifiedName(method)).append('(');
        
        if (len > 0) {
            int i = 0;
            Parameter[] methodParms = method.getParameters();
            
            do {
                Parameter parm = methodParms[i];
                if (null == parm.getAnnotation(RedactValue.class)) {
                    builder.append(parameters[i]);
                } else {
                    builder.append("|:").append(parm.getName()).append(":|");
                }
                
                if (++i == len) break;
                
                builder.append(", ");
            } while (true);
        }
        
        return builder.append(')').toString();
    }
    
}