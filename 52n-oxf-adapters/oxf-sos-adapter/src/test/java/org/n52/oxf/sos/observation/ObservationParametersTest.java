/**
 * ﻿Copyright (C) 2012
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
package org.n52.oxf.sos.observation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;

public class ObservationParametersTest {
	
	@Test
	public void shouldAddResultTime()
	{
		final ObservationParameterSeam obsParams = new ObservationParameterSeam(null);
		final String resultTime = "2013-01-02T03:04:05+06:00";
		obsParams.addResultTime(resultTime);
		assertThat(obsParams.getSingleValue(ISOSRequestBuilder.INSERT_OBSERVATION_RESULT_TIME),is(resultTime));		
	}
	
	@Test
	public void shouldAddPhenomenonTime()
	{
		final ObservationParameterSeam obsParams = new ObservationParameterSeam(null);
		final String phenTime = "2013-01-02T03:04:05+06:00";
		obsParams.addPhenomenonTime(phenTime);
		assertThat(obsParams.getSingleValue(ISOSRequestBuilder.INSERT_OBSERVATION_PHENOMENON_TIME),is(phenTime));		
	}
	
	private class ObservationParameterSeam extends ObservationParameters {

		protected ObservationParameterSeam(final QName type) {
			super(type);
		}

		@Override
		public boolean isValid()
		{
			return false;
		}
	}
	
}
