/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.csw.adapter;

import java.util.ArrayList;
import java.util.List;

import net.opengis.cat.csw.x202.CapabilitiesDocument;
import net.opengis.ows.DCPDocument;
import net.opengis.ows.DomainType;
import net.opengis.ows.OperationDocument;
import net.opengis.ows.RequestMethodType;

import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Contents;
import org.n52.oxf.ows.capabilities.DCP;
import org.n52.oxf.ows.capabilities.GetRequestMethod;
import org.n52.oxf.ows.capabilities.OnlineResource;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.ows.capabilities.PostRequestMethod;
import org.n52.oxf.ows.capabilities.RequestMethod;
import org.n52.oxf.ows.capabilities.ServiceIdentification;
import org.n52.oxf.ows.capabilities.ServiceProvider;
import org.n52.oxf.valueDomains.StringValueDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class CSWCapabilitiesMapper_202 {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSWCapabilitiesMapper_202.class);

    public ServiceDescriptor mapCapabilities(CapabilitiesDocument xb_capabilitiesDoc) throws OXFException {
        String version = null;
        ServiceIdentification serviceIdentification = null;
        ServiceProvider serviceProvider = null;
        OperationsMetadata operationsMetadata = mapOperationsMetadata(xb_capabilitiesDoc.getCapabilities().getOperationsMetadata());
        Contents contents = null;

        ServiceDescriptor serviceDesc = new ServiceDescriptor(version,
                                                              serviceIdentification,
                                                              serviceProvider,
                                                              operationsMetadata,
                                                              contents);
        return serviceDesc;
    }

    private OperationsMetadata mapOperationsMetadata(net.opengis.ows.OperationsMetadataDocument.OperationsMetadata xb_operationsMetadata)
    {
        //
        // map the operations:
        //

        OperationDocument.Operation[] xb_operations = xb_operationsMetadata.getOperationArray();
        Operation[] oc_operations = new Operation[xb_operations.length];
        for (int i = 0; i < xb_operations.length; i++) {
            OperationDocument.Operation xb_operation = xb_operations[i];

            String oc_operationName = xb_operation.getName();

            //
            // map the operations DCPs:
            //

            DCPDocument.DCP[] xb_dcps = xb_operation.getDCPArray();
            DCP[] oc_dcps = new DCP[xb_dcps.length];
            for (int j = 0; j < xb_dcps.length; j++) {
                DCPDocument.DCP xb_dcp = xb_dcps[j];

                //
                // map the RequestMethods:
                //

                List<RequestMethod> oc_requestMethods = new ArrayList<RequestMethod>();

                RequestMethodType[] xb_getRequestMethods = xb_dcp.getHTTP().getGetArray();
                for (int k = 0; k < xb_getRequestMethods.length; k++) {
                    RequestMethodType xb_getRequestMethod = xb_getRequestMethods[k];
                    OnlineResource oc_onlineRessource = new OnlineResource(xb_getRequestMethod.getHref());
                    RequestMethod oc_requestMethod = new GetRequestMethod(oc_onlineRessource);
                    oc_requestMethods.add(oc_requestMethod);
                }

                RequestMethodType[] xb_postRequestMethods = xb_dcp.getHTTP().getPostArray();
                for (int k = 0; k < xb_postRequestMethods.length; k++) {
                    RequestMethodType xb_postRequestMethod = xb_postRequestMethods[k];
                    OnlineResource oc_onlineRessource = new OnlineResource(xb_postRequestMethod.getHref());
                    RequestMethod oc_requestMethod = new PostRequestMethod(oc_onlineRessource);
                    oc_requestMethods.add(oc_requestMethod);
                }

                oc_dcps[j] = new DCP(oc_requestMethods);
            }

            //
            // map the operations parameters:
            //

            DomainType[] xb_parameters = xb_operation.getParameterArray();

            List<Parameter> oc_parameters = new ArrayList<Parameter>();

//            for (int j = 0; j < xb_parameters.length; j++) {
//
//                DomainType xb_parameter = xb_parameters[j];
//
//                String parameterName = xb_parameter.getName();
//
//                //
//                // map the parameters' values to StringValueDomains
//                //
//
//                AllowedValues xb_allowedValues = xb_parameter.getAllowedValues();
//
//                if (xb_allowedValues != null) {
//                    ValueType[] xb_values = xb_allowedValues.getValueArray();
//
//                    StringValueDomain oc_values = new StringValueDomain();
//                    for (int k = 0; k < xb_values.length; k++) {
//                        ValueType xb_value = xb_values[k];
//
//                        oc_values.addPossibleValue(xb_value.getStringValue());
//                    }
//
//                    Parameter oc_parameter = new Parameter(parameterName, true, oc_values, null);
//                    oc_parameters.add(oc_parameter);
//                }
//            }
            
            Parameter[] parametersArray = new Parameter[oc_parameters.size()];
            oc_parameters.toArray(parametersArray);

            oc_operations[i] = new Operation(oc_operationName, parametersArray, null, oc_dcps);
        }

        return new OperationsMetadata(oc_operations);
    }
}
