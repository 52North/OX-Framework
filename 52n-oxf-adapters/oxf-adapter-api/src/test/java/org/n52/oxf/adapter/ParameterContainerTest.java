/**
 * Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
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
