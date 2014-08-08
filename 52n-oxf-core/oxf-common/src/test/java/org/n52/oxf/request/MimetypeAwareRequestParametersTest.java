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
