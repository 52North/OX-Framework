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
package org.n52.oxf.ows.capabilities;

/**
 * This class is used by each Operation and is utilized to describe one possible parameter of the operation.<br>
 * If you need to associate the Parameter with a specific dataset (or resource) please use the sub-class <a
 * href=DatasetParameter.html>DatasetParameter</a>.
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class Parameter {

    /**
     * type of associated specifiedValue: org.n52.oxf.valueDomains.BoundingBox
     */
    public static final String COMMON_NAME_BBOX = "BBOX";

    /**
     * type of associated specifiedValue: java.lang.Integer
     */
    public static final String COMMON_NAME_WIDTH = "WIDTH";

    /**
     * type of associated specifiedValue: java.lang.Integer
     */
    public static final String COMMON_NAME_HEIGHT = "HEIGHT";

    /**
     * type of associated specifiedValue: java.lang.String
     */
    public static final String COMMON_NAME_SRS = "SRS";

    /**
     * type of associated specifiedValue: java.lang.String
     */
    public static final String COMMON_NAME_FORMAT = "FORMAT";

    public static final String COMMON_NAME_STYLE = "STYLE";

    public static final String COMMON_NAME_VERSION = "VERSION";

    /**
     * type of associated specifiedValue: org.n52.oxf.valueDomains.time.ITime
     */
    public static final String COMMON_NAME_TIME = "TIME";

    /**
     * type of associated specifiedValue: java.lang.String <br>
     * the name or id of the dataset (&rarr; dataset means layer, coverage or something like that).
     */
    public static final String COMMON_NAME_RESOURCE_ID = "RESOURCE_ID";

    /**
     * The service-sided-name or identifier of this parameter. This name is specified by the OWS spec of the
     * service.<br>
     * <br>
     * One (mandatory)
     */
    protected String serviceSidedName;

    /**
     * This "commonName" is used by the OX-framework to address this parameter. All possible "common names"
     * are specified as static attributes in this class. Please look up the possible values and mark this
     * parameter with the corresponding "commonName". If this parameter doesn't correspond to any of the
     * listed commonNames, let the commonName-attribute stay <code>null</code>. <br>
     * One optional.
     */
    private String commonName = null;

    /**
     * indicates if this ParameterContainer is required for the operation.<br>
     * <br>
     * One (mandatory)
     */
    protected boolean required;

    /**
     * Valid values for this parameter.<br>
     * Holds all possible values. In OWScommon this is named "value".<br>
     * <br>
     * One (mandatory)
     */
    protected IValueDomain valueDomain;

    /**
     * this constructor has all attributes as its parameters. Constructs a Parameter with a given
     * serviceSidedName and the indication, if the Parameter is required.
     * 
     * @param name
     *        the name of the parameter
     * @param required
     *        indicates if a ParameterContainer is required.
     * @param valueDomain
     *        holds all possible values.
     * @param commonName
	 *            the "commonName" is used internally by the OX-framework to
	 *            address this parameter.<br>
	 *            ATTENTION: if the parameter has no corresponding commonName
	 *            please set it on <code>null</code>.
     */
    public Parameter(String name, boolean required, IValueDomain valueDomain, String commonName) {
        setServiceSidedName(name);
        setRequired(required);
        setValueDomain(valueDomain);
        setCommonName(commonName);
    }

    @Override
    public String toString() {
        String res = "[Parameter: '" + serviceSidedName + "'/'" + commonName + "'";
        res += " ValueDomain: ";
        if (valueDomain != null) {
            res += valueDomain.toXML();
        }
        res += "]";

        return res;
    }

    /**
     * @return a XML representation of this Parameter.
     */
    public String toXML() {
        String res = "<Parameter serviceSidedName=\"" + serviceSidedName + "\" required=\""
                + required + "\" commonName=\"" + commonName + "\">";

        res += "<ValueDomain>";
        if (valueDomain != null) {
            res += valueDomain.toXML();
        }
        res += "</ValueDomain>";

        res += "</Parameter>";

        return res;
    }

    public String getServiceSidedName() {
        return serviceSidedName;
    }

    protected void setServiceSidedName(String name) throws IllegalArgumentException {
        if ( !name.equals("")) {
            this.serviceSidedName = name;
        }
        else {
            throw new IllegalArgumentException("The parameter 'serviceSidedName' is illegal.");
        }
    }

    /**
     * @return the "commonName" of this parameter which is used by the OX-framework to address this parameter.
     *         All possible "common names" are specified as static attributes in this class. If this parameter
     *         doesn't correspond to any of the listed commonNames, this method will return <code>null</code>.
     */
    public String getCommonName() {
        return commonName;
    }

    /**
     * @param commonName
     *        The "commonName" is used by the OX-framework to address this parameter. All possible "common
     *        names" are specified as static attributes in this class. Please look up the possible values and
     *        mark this parameter with the corresponding "commonName". If this parameter doesn't correspond to
     *        any of the listed commonNames, let the commonName-attribute stay <code>null</code>.
     */
    protected void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    /**
     * indicates if a parameter has to be part of a certain operation.
     * 
     * @return <code>true</code>, if this parameter has to be part of a certain operation.
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * @param required
     *        has to be set, if the parameter is required in the Operation. <code>false</code> is standard!.
     */
    protected void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * IValueDomain describes the allowed values of a parameter.
     * 
     * @return Returns the valueDomain.
     */
    public IValueDomain getValueDomain() {
        return valueDomain;
    }

    /**
     * IValueDomain describes the allowed values of a parameter.
     * 
     * @param valueDomain
     *        The valueDomain to set.
     */
    protected void setValueDomain(IValueDomain<?> valueDomain) {
        this.valueDomain = valueDomain;
    }
}