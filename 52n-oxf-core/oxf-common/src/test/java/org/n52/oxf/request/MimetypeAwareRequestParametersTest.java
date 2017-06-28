/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.oxf.request;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.nio.charset.Charset;

import org.junit.Test;

public class MimetypeAwareRequestParametersTest {

	@Test public void
	shouldBeInvalidAfterCreate(){
		assertThat(new MimetypeAwareRequestParametersSeam().isValid(), is(false));
	}

	@Test public void
	isSetMimetypeShouldReturnTrueIfSet() {
		final MimetypeAwareRequestParametersSeam parameters = new MimetypeAwareRequestParametersSeam();
		parameters.setCharset(Charset.forName("UTF-8"));
		parameters.setType("text/xml");
		assertThat(parameters.isSetMimetype(), is(true));
	}

	@Test public void
	isSetMimetypeShouldReturnFalseIfTypeOrCharsetIsMissing() {
		MimetypeAwareRequestParametersSeam parameters = new MimetypeAwareRequestParametersSeam();
		parameters.setCharset(Charset.forName("UTF-8"));
		assertThat(parameters.isSetMimetype(), is(false));
		parameters = new MimetypeAwareRequestParametersSeam();
		parameters.setType("text/xml");
		assertThat(parameters.isSetMimetype(), is(false));
	}

	private class MimetypeAwareRequestParametersSeam extends MimetypeAwareRequestParameters {}
}
