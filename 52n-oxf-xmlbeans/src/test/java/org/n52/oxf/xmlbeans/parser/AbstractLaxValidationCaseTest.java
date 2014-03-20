/**
 * Copyright (C) 2014
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
package org.n52.oxf.xmlbeans.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlValidationError;
import org.junit.Test;


public class AbstractLaxValidationCaseTest {

	@Test
	public void shouldReturnFalseIfReceivingNullValue() {
		final XmlError ve = null;

		assertThat(new LaxValidationCaseSeam().shouldPass(ve), is(false));
	}

	@Test
	public void shouldReturnFalseIfReceivingUnknownType() {
		final XmlError validationError = new MyType();

		assertThat(new LaxValidationCaseSeam().shouldPass(validationError), is(false));
	}

	private class LaxValidationCaseSeam extends AbstractLaxValidationCase {

		@Override
		public boolean shouldPass(final XmlValidationError validationError) {
			return true;
		}

	}

	private class MyType extends XmlError {

		private static final long serialVersionUID = 1L;

		public MyType() {
			super(mock(XmlError.class));
		}
	}

}
