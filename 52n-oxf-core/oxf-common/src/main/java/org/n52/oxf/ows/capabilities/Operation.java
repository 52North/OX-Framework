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
package org.n52.oxf.ows.capabilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * This class holds the values to invoke a service operation. One Operation will be generated for each
 * operation of a OWS (i.e. GetMap)
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class Operation {

    /**
     * Name of this operation (request) (for example, GetCapabilities).<br>
     * <br>
     * One (mandatory)
     */
    private String name;

    /**
     * a Parameter supplies a valid domain that applies to this operation which the server implements.<br>
     * <br>
     * Zero or more (optional)
     */
    private List<Parameter> parameters;

    /**
     * here only String representation, cause it is not focus of this framework.<br>
     * <br>
     * Zero or more (optional)
     */
    private String[] constraints;

    /**
     * Information for a Distributed Computing Platform (DCP) supported for this operation.<br>
     * <br>
     * One or more (mandatory)
     */
    private DCP[] dcps;

    public Operation(final String name, final Parameter[] parameters, final String[] constraints, final DCP[] dcps) {
        setName(name);
        setParameters(parameters);
        setConstraints(constraints);
        setDcps(dcps);
    }

    public Operation(final String name, final DCP[] dcps) {
        setName(name);
        setDcps(dcps);
    }

    /**
     * convenience constructor. Creates an Operation with the specified name and sets one DCP with the
     * specified httpGetHref and httpPostHref. The constraints and parameters attributes will stay
     * <code>null</code>.
     * 
     * @param name
     * @param dcps
     */
    public Operation(final String name, final String httpGetHref, final String httpPostHref) {
        setName(name);

        final DCP[] dcp = new DCP[1];
        dcp[0] = new DCP(new GetRequestMethod(new OnlineResource(httpGetHref)),
                         new PostRequestMethod(new OnlineResource(httpPostHref)));

        setDcps(dcp);
    }

    /**
     * @return a XML representation of this OperationsMetadata-section.
     */
    public String toXML() {
        String res = "<Operation name=\"" + name + "\">";

        res += "<Parameters>";
        if (parameters != null) {
            for (final Parameter parameter : parameters) {
                res += parameter.toXML();
            }
        }
        res += "</Parameters>";

        res += "<Constraints>";
        if (constraints != null) {
            for (final String c : constraints) {
                res += "<Constraint>";
                res += c;
                res += "<Constraint>";
            }
        }
        res += "</Constraints>";

        res += "<DCPs>";
        if (dcps != null) {
            for (final DCP dcp : dcps) {
                res += dcp.toXML();
            }
        }
        res += "</DCPs>";

        res += "</Operation>";

        return res;
    }

    @Override
	public String toString() {
    	String params = null;
    	if (parameters != null) {
    		params = "[";
    		for (final Parameter param : parameters) {
                params += "ServiceSidedName: " 	+ param.getServiceSidedName() 
                		+ "  CommonName: "		+ param.getCommonName()
                		+ "  ValueDomain-class: "
                					+ param.getValueDomain().getClass() + "\n";
            }
    		params += "]";
    	}
		return String.format(
				"Operation [name=%s, parameters=%s, constraints=%s, dcps=%s]",
				name,
				(parameters!=null?params:parameters),
				Arrays.toString(constraints),
				Arrays.toString(dcps));
	}

    /**
     * @return Returns the constraints.
     */
    public String[] getConstraints() {
        return constraints;
    }

    /**
     * @param constraints
     *        The constraints to set.
     */
    protected void setConstraints(final String[] constraints) {
        this.constraints = constraints;
    }

    /**
     * @return Returns the dcps.
     */
    public DCP[] getDcps() {
        return dcps;
    }

    /**
     * @param dcps
     *        The dcps to set.
     */
    protected void setDcps(final DCP[] dcps) {
        this.dcps = dcps;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *        The name to set.
     * @exception IllegalArgumentException
     *            if name is empty.
     */
    protected void setName(final String name) throws IllegalArgumentException {
        if ( !name.equals("")) {
            this.name = name;
        }
        else {
            throw new IllegalArgumentException("The parameter 'name' is illegal.");
        }
    }

    /**
     * @return Returns the parameters.
     */
    public List<Parameter> getParameters() {
        return parameters;
    }

    /**
     * @param parameters
     *        The parameters to set.
     */
    protected void setParameters(final Parameter[] parameters) {
        this.parameters = new ArrayList<Parameter>();

        for (final Parameter parameter : parameters) {
            this.parameters.add(parameter);
        }
    }

    /**
     * This returns a Map, which contains the parameterNames (= serviceSidedNames) as Keys and the
     * Parameters itself as values.
     * 
     * @return
     */
    public Map<String, Parameter> getParametersAsMap() {
        if (parameters == null) {
            throw new NullPointerException();
        }
        final Map<String, Parameter> parameterMap = new HashMap<String, Parameter>();
        for (final Parameter param : parameters) {
            parameterMap.put(param.getServiceSidedName(), param);
        }
        return parameterMap;
    }

    /**
     * @return the parameter with the specified serviceSidedName (ignore case!).
     */
    public Parameter getParameter(final String serviceSidedName) {
        for (final Parameter param : parameters) {
            if (param.getServiceSidedName().equalsIgnoreCase(serviceSidedName)) {
                return param;
            }
        }
        return null;
    }

    /**
     * 
     * @param serviceSidedName
     * @param datasetID
     * @return the DatasetParameter with the specified serviceSidedName (ignore case!) and the specified
     *         datasetID.
     */
    public DatasetParameter getParameter(final String serviceSidedName, final String datasetID) {
        for (final Parameter param : parameters) {
            if (param instanceof DatasetParameter) {
                final DatasetParameter datasetParam = (DatasetParameter) param;

                if (datasetParam.getAssociatedDataset().getIdentifier().equalsIgnoreCase(datasetID)
                        && param.getServiceSidedName().equalsIgnoreCase(serviceSidedName)) {
                    return datasetParam;
                }
            }
        }
        return null;
    }

    /**
     * 
     * @param param
     */
    public void addParameter(final Parameter param) {
        parameters.add(param);
    }
}