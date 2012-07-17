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

import org.n52.oxf.*;

/**
 * 
 * ServiceIdentification describes the specific service.
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster </a>
 * @author <a href="mailto:broering@52north.org">Arne Broering </a>
 * 
 */
public class ServiceIdentification extends Description {

    /**
     * A service type name from registry of services, normally used for machine-to-machine communication. <br>
     * One (mandatory) value for serviceType is required. <br>
     * <br>
     * Example value for serviceType: "OGC:WMS"
     */
    private String serviceType;

    /**
     * Version of the service type. <br>
     * <br>
     * One or more (mandatory) values for serviceTypeVersion are required. One for each version implemented by
     * server, unordered. <br>
     * <br>
     * Example values for serviceTypeVersion: { "1.2.0", "1.1.0", ...}
     */
    private String[] serviceTypeVersion;

    /**
     * Fees and terms for retrieving data from or otherwise using the server. <br>
     * <br>
     * Reserved value NONE shall be used to mean no fees are imposed. <br>
     * Zero or one (optional) value for fees is possible. <br>
     */
    private String fees;

    /**
     * Access constraints that should be observed to assure the protection of privacy or intellectual
     * property, and any other restrictions on retrieving or using data from the server or otherwise using the
     * server. <br>
     * <br>
     * Reserved value NONE shall be used to mean no constraints are imposed. <br>
     * Zero or more (optional) constraints are possible. <br>
     */
    private String[] accessConstraints;

    /**
     * Constructor which has got the 'required' attributes for the ServiceIdentification-Section as its
     * parameters. <br>
     * And: <br>
     * sets the accessConstraints variable on initialization to "NONE" (as specified by OWS COMMON). <br>
     * sets the fees variable on initialization to "NONE" (as specified by OWS COMMON). <br>
     * <br>
     * 
     * @param title
     *        the title of the service.
     * @param serviceType
     *        service type name normally used for machine-to-machine communication.
     * @param serviceTypeVersion
     *        version(s) of the service type supported by the server. <br>
     * @throws OXFException
     *         if parameters are not correct.
     */
    public ServiceIdentification(String title, String serviceType, String[] serviceTypeVersion) {
        super(title);
        setServiceType(serviceType);
        setServiceTypeVersion(serviceTypeVersion);
        setFees("NONE");
        setAccessConstraints(new String[] {"NONE"});
    }

    /**
     * his constructor has all attributes of the class as its parameters.
     * 
     * @param title
     * @param serviceType
     * @param serviceTypeVersion
     * @param fees
     * @param accessConstraints
     * @param abstractDescription
     * @param keywords
     * @throws OXFException
     */
    public ServiceIdentification(String title,
                                 String serviceType,
                                 String[] serviceTypeVersion,
                                 String fees,
                                 String[] accessConstraints,
                                 String abstractDescription,
                                 String[] keywords) {
        
        super(title, abstractDescription, keywords);
        
        setServiceType(serviceType);
        setServiceTypeVersion(serviceTypeVersion);
        setFees(fees);
        setAccessConstraints(accessConstraints);
    }

    /**
     * @return a XML representation of this ServiceIdentification-section.
     */
    public String toXML() {
        String res = "<ServiceIdentification serviceType=\"" + serviceType + "\">";

        res += "<Fees>" + fees + "</Fees>";

        if (accessConstraints != null) {
            for (String s : accessConstraints) {
                res += "<AccessConstraint>";
                res += s;
                res += "</AccessConstraint>";
            }
        }

        if (serviceTypeVersion != null) {
            for (String s : serviceTypeVersion) {
                res += "<ServiceTypeVersion>";
                res += s;
                res += "</ServiceTypeVersion>";
            }
        }

        res += "</ServiceIdentification>";
        return res;
    }

    /**
     * @return Returns the accessConstraints.
     */
    public String[] getAccessConstraints() {
        return accessConstraints;
    }

    /**
     * @return Returns the fees.
     */
    public String getFees() {
        return fees;
    }

    /**
     * @return Returns the serviceTypeVersion.
     */
    public String[] getServiceTypeVersion() {
        return serviceTypeVersion;
    }

    /**
     * sets the version for this service
     * 
     * @param version
     * @throws IllegalArgumentException
     *         if version.length == 0 or version[0].equals("").
     */
    protected void setServiceTypeVersion(String[] version) throws IllegalArgumentException {
        if (version.length == 0 || version[0].equals("")) {
            throw new IllegalArgumentException("The parameter 'version' is illegal.");
        }
        else {
            serviceTypeVersion = version;
        }
    }

    /**
     * sets the fees for this service.
     * 
     * @param fees
     */
    protected void setFees(String fees) {
        this.fees = fees;
    }

    /**
     * @param constraints
     *        zero or more (optional) accessConstraints are possible
     */
    protected void setAccessConstraints(String[] constraints) {
        accessConstraints = constraints;
    }

    /**
     * @return Returns the serviceType.
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType
     *        The serviceType to set.
     * @throws IllegalArgumentException
     *         if serviceType == null or serviceType.equals("").
     */
    protected void setServiceType(String serviceType) throws IllegalArgumentException {
        if (serviceType == null || serviceType.equals("")) {
            throw new IllegalArgumentException("The parameter 'serviceType' is illegal.");
        }
        else {
            this.serviceType = serviceType;
        }

    }
}