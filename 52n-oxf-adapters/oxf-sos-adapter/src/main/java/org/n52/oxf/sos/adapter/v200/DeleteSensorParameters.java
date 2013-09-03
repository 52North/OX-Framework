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
package org.n52.oxf.sos.adapter.v200;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.sos.request.v200.Sos200RequestParameters;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class DeleteSensorParameters extends Sos200RequestParameters {

	static final String SENSOR_ID = "procedure";

	/**
	 * @param sensorId
	 * 		the identifier of the sensor to delete
	 */
	public DeleteSensorParameters(final String sensorId) {
		addNonEmpty(SENSOR_ID, sensorId);
	}

	/**
	 * @return a ParameterContainer used to create the according request
	 * 
	 * @throws OXFException See {@link ParameterContainer#addParameterShell(String, String...)}
	 */
	public ParameterContainer getParameterContainer() throws OXFException
	{
		final ParameterContainer pc = new ParameterContainer();
		pc.addParameterShell(ISOSRequestBuilder.SERVICE, getSingleValue(SERVICE_TYPE));
		pc.addParameterShell(ISOSRequestBuilder.VERSION, getSingleValue(SERVICE_VERSION));
		pc.addParameterShell(ISOSRequestBuilder.DELETE_SENSOR_PROCEDURE, getSingleValue(SENSOR_ID));
		return pc;
	}

}
