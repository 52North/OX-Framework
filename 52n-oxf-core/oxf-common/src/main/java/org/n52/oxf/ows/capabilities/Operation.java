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

    public Operation(String name, Parameter[] parameters, String[] constraints, DCP[] dcps) {
        setName(name);
        setParameters(parameters);
        setConstraints(constraints);
        setDcps(dcps);
    }

    public Operation(String name, DCP[] dcps) {
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
    public Operation(String name, String httpGetHref, String httpPostHref) {
        setName(name);

        DCP[] dcp = new DCP[1];
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
            for (Parameter parameter : parameters) {
                res += parameter.toXML();
            }
        }
        res += "</Parameters>";

        res += "<Constraints>";
        if (constraints != null) {
            for (String c : constraints) {
                res += "<Constraint>";
                res += c;
                res += "<Constraint>";
            }
        }
        res += "</Constraints>";

        res += "<DCPs>";
        if (dcps != null) {
            for (DCP dcp : dcps) {
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
    		for (Parameter param : parameters) {
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
    protected void setConstraints(String[] constraints) {
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
    protected void setDcps(DCP[] dcps) {
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
    protected void setName(String name) throws IllegalArgumentException {
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
    protected void setParameters(Parameter[] parameters) {
        this.parameters = new ArrayList<Parameter>();

        for (int i = 0; i < parameters.length; i++) {
            this.parameters.add(parameters[i]);
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
        Map<String, Parameter> parameterMap = new HashMap<String, Parameter>();
        for (Parameter param : parameters) {
            parameterMap.put(param.getServiceSidedName(), param);
        }
        return parameterMap;
    }

    /**
     * @return the parameter with the specified serviceSidedName (ignore case!).
     */
    public Parameter getParameter(String serviceSidedName) {
        for (Parameter param : parameters) {
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
    public DatasetParameter getParameter(String serviceSidedName, String datasetID) {
        for (Parameter param : parameters) {
            if (param instanceof DatasetParameter) {
                DatasetParameter datasetParam = (DatasetParameter) param;

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
    public void addParameter(Parameter param) {
        this.parameters.add(param);
    }
}