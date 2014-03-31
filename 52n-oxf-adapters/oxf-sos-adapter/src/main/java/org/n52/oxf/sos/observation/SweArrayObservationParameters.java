/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
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
package org.n52.oxf.sos.observation;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import org.n52.oxf.xml.XMLConstants;

/**
 * Assembles parameters for a SweArray observation.
 */
public class SweArrayObservationParameters extends ObservationParameters {

	public SweArrayObservationParameters() {
		super(XMLConstants.QNAME_OM_2_0_SWE_ARRAY_OBSERVATION);
		addNonEmpty(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_SWE_ARRAY);
	}

	@Override
	public boolean isValid() {
		return !isEmpty(INSERT_OBSERVATION_VALUE_PARAMETER);
	}

	public void addObservationValue(final String sweArrayXmlString) {
		addNonEmpty(INSERT_OBSERVATION_VALUE_PARAMETER, sweArrayXmlString);
	}

}
