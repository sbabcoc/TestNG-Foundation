package com.nordstrom.test_automation.ui_tools.testng_foundation;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.testng.Reporter;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;

public class ReporterAppender extends OutputStreamAppender<ILoggingEvent> {
	
	protected boolean logToStdOut = false;
	
    @Override
    public void start() {
    	encoder = new PatternLayoutEncoder();
    	setOutputStream(new ByteArrayOutputStream());
        super.start();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
    	if (isStarted()) {
	    	super.append(eventObject);
	    	Reporter.log(getOutput(), logToStdOut);
    	}
    }
    
    private String getOutput() {
    	Charset charset = ((PatternLayoutEncoder) encoder).getCharset();
    	ByteArrayOutputStream outputStream = (ByteArrayOutputStream) getOutputStream();
    	if (charset == null) {
    		return outputStream.toString();
    	} else {
	    	try {
				return outputStream.toString(charset.name());
			} catch (UnsupportedEncodingException e) {
				throw new IllegalStateException("An existing charset cannot possibly be unsupported.");
			}
    	}
    }
    
	/**
	 * Specify if output should be sent to <i>STDOUT</i> in addition to the TestNG HTML report.
	 * @param logToStdOut {@code false} to send output only to the TestNG HTML report; 
	 *         {@code true} to fork output to <i>STDOUT</i> and the TestNG HTML report.
	 */
	public void setLogToStdOut(boolean logToStdOut) {
		this.logToStdOut = logToStdOut;
	}
	
	/**
	 * Determine if output is being forked to <i>STDOUT</i> and the TestNG HTML report.
	 * @return {@code false} if output is being sent solely to the TestNG HTML report; 
	 *         {@code true} if output is being forked to <i>STDOUT</i> and the TestNG HTML report.
	 */
	public boolean doLogToStdOut() {
		return logToStdOut;
	}

}
