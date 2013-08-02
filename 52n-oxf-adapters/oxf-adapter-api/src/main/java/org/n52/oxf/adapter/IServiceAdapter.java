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

package org.n52.oxf.adapter;

import org.n52.oxf.*;
import org.n52.oxf.ows.*;
import org.n52.oxf.ows.capabilities.*;

/**
 * Specifies the methods which should be implemented by every extended OGC Web
 * Service Adapter. The methods are so generic that they could be used by
 * various applications to access the specific OGC Web Service.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering </a>
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster </a>
 */
public interface IServiceAdapter {
	
	/**
	 * initializes the ServiceDescriptor by requesting the capabilities document
	 * of the OWS.
	 * 
	 * @param serviceURL
	 *            the base URL of the Service
	 *            
	 * @return the ServiceDescriptor based on the retrieved capabilities
	 *         document
	 * @throws ExceptionReport
	 *             if service side Exception occurs.
	 * @throws OXFException
	 *             if internal Exception occurs.
	 */
	public ServiceDescriptor initService(String serviceURL) throws ExceptionReport, OXFException;

	/**
	 * executes the OWS operation with the specified operationName of the OWS
	 * which is identified by the given serviceURL.
	 * 
	 * @param operation
	 *            the operation which the adapter has to execute on the service.
	 *            this operation includes also the parameter values.
	 *            
	 * @param parameterContainer
	 *            contains all ParameterShells for the Operation.
	 * @throws ExceptionReport
	 *             if service side exception occures.
	 * @throws OXFException
	 *             if internal exception occures.
	 * @return the result of the executed OWS operation.
	 */
    public OperationResult doOperation(Operation operation, ParameterContainer parameterContainer) throws ExceptionReport, OXFException;

	/**
	 * @return a description of the implemented OGC Web Service Adapter.
	 */
    public String getDescription();

	/**
	 * @return the type of the service which is connectable by this
	 *         ServiceAdapter.
	 */
    public String getServiceType();

	/**
	 * @return the versions of the services which are connectable by this
	 *         ServiceAdapter. Should look like e.g. { "1.1.0","1.2.0" }.
	 */
    public String[] getSupportedVersions();

	/**
	 * @return the name of the service operation which returns the data to be
	 *         added to a map view as a layer.<br>
	 *         (e.g.: WCS - GetCoverage; WMS - GetMap).<br>
	 *         If the service does not support such an operation this method
	 *         returns <code>null</code>.
	 */
    public String getResourceOperationName();
}