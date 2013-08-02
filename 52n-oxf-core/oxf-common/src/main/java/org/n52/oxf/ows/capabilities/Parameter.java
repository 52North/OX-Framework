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

    /**
     * 
     */
    public static final String COMMON_NAME_STYLE = "STYLE";

    /**
     * 
     */
    public static final String COMMON_NAME_VERSION = "VERSION";

    /**
     * type of associated specifiedValue: org.n52.oxf.valueDomains.time.ITime
     */
    public static final String COMMON_NAME_TIME = "TIME";

    /**
     * type of associated specifiedValue: java.lang.String <br>
     * the name or id of the dataset (--> dataset means layer, coverage or something like that).
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
     * @param serviceSidedName
     *        the serviceSidedName of the parameter
     * @param required
     *        indicates if a ParameterContainer is required.
     * @param valueDomain
     *        holds all possible values.
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

    /**
     * @return Returns the serviceSidedName of the parameter.
     */
    public String getServiceSidedName() {
        return serviceSidedName;
    }

    /**
     * @param serviceSidedName
     *        The serviceSidedName to set.
     */
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
     * @return Returns the required.
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * @param required
     *        has to be set, if the parameter is required in the Operation. false is standard!.
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
    protected void setValueDomain(IValueDomain valueDomain) {
        this.valueDomain = valueDomain;
    }
}