package com.nordstrom.test_automation.ui_tools.testng_foundation;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.ContextBase;
import ch.qos.logback.core.util.StatusPrinter;

public class ReporterAppenderTest {

    Context context = new ContextBase();

    public Appender<ILoggingEvent> getAppender() {
        return new ReporterAppender();
    }

    protected Appender<ILoggingEvent> getConfiguredAppender() {
        ReporterAppender ca = new ReporterAppender();
        ca.setEncoder(new NopEncoder<ILoggingEvent>());
        ca.start();
        return ca;
    }

    @Test
    public void testNewAppender() {
        // new appenders should be inactive
        Appender<ILoggingEvent> appender = getAppender();
        assertFalse(appender.isStarted());
    }

    @Test
    public void testConfiguredAppender() {
        Appender<ILoggingEvent> appender = getConfiguredAppender();
        appender.start();
        assertTrue(appender.isStarted());

        appender.stop();
        assertFalse(appender.isStarted());

    }

    @Test
    public void testNoStart() {
        Appender<ILoggingEvent> appender = getAppender();
        appender.setContext(context);
        appender.setName("doh");
        // is null OK?
        appender.doAppend(null);
        StatusChecker checker = new StatusChecker(context.getStatusManager());
        StatusPrinter.print(context);
        checker.assertContainsMatch("Attempted to append to non started appender \\[doh\\].");
    }
}
