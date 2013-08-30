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
package org.n52.oxf.sos.request.v200;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.n52.oxf.sos.observation.DefaultObservationParametersFactory;
import org.n52.oxf.xml.XMLConstants;

// TODO add more tests regarding offeringIds and observations
public class InsertObservationParametersTest {

	@Rule public ExpectedException thrown = ExpectedException.none();
	
	@Test public void
	shouldThrowIllegalArgumentExceptionIfMissingAllParameters() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter 'offeringIds' is required and may not be null or empty!");
		new InsertObservationParameters(null, (String[])null);
	}
	
	@Test public void
	shouldThrowIllegalArgumentExceptionIfMissingProcedureId() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter 'offeringIds' is required and may not be null or empty!");
		new InsertObservationParameters(new DefaultObservationParametersFactory()
		.createObservationParametersFor(XMLConstants.QNAME_OM_1_0_MEASUREMENT_OBSERVATION),(String[])null);
	}
	
	@Test public void
	shouldThrowIllegalArgumentExceptionIfMissingObservationParameter() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter 'ObservationParameters' with may not be null or empty!");
		new InsertObservationParameters(null,"sdf");
	}
	
	@Test public void
	shouldThrowIllegalArgumentExceptionIfReceivingInvalidObservationParameter() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter 'ObservationParameters' with may not be null or empty!");
		new InsertObservationParameters( new DefaultObservationParametersFactory()
		.createObservationParametersFor(XMLConstants.QNAME_OM_1_0_MEASUREMENT_OBSERVATION),"sdf");
	}
	
}
