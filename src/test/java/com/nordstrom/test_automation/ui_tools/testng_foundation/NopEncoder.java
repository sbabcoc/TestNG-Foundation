/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2011, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package com.nordstrom.test_automation.ui_tools.testng_foundation;

import java.io.IOException;
import java.io.OutputStream;

import ch.qos.logback.core.encoder.EncoderBase;

public class NopEncoder<E> extends EncoderBase<E> {

	public void close() throws IOException {
	}

	public void doEncode(E event) throws IOException {
	}

	public void init(OutputStream os) throws IOException {
	}
}
