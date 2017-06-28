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
package org.n52.oxf.adapter;

import static java.lang.Boolean.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.n52.oxf.OXFException;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class ParameterContainerTest {
	
	@Test public void
	removeParameterShell_by_parameterName_should_return_true_if_shell_is_contained_and_removed()
			 throws OXFException {
		final ParameterContainer container = new ParameterContainer();
		final String parameterValue = "parameterValue";
		final String parameterName = "parameterName";
		container.addParameterShell(parameterName, parameterValue);
		container.addParameterShell(parameterName.toUpperCase(), parameterValue);
		boolean isShellContained = container.containsParameterShellWithCommonName(parameterName) || 
				container.containsParameterShellWithServiceSidedName(parameterName);
		
		assertThat(isShellContained,is(TRUE));
		final boolean isShellRemoved = container.removeParameterShell(parameterName);
		
		isShellContained = container.containsParameterShellWithCommonName(parameterName) || 
				container.containsParameterShellWithServiceSidedName(parameterName);
		
		assertThat(isShellContained,is(FALSE));
		assertThat(isShellRemoved, is(TRUE));
	}
	
	@Test public void
	removeParameterShell_by_parameterName_should_return_false_if_shell_is_NOT_contained()
			 throws OXFException {
		final ParameterContainer container = new ParameterContainer();
		final boolean isShellRemoved = container.removeParameterShell("any-name");

		assertThat(isShellRemoved, is(FALSE));
	}
}
