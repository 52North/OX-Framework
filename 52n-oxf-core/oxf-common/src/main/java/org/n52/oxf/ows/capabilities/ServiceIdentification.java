/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
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
     * @throws IllegalArgumentException
     *         if parameters are not correct.
     */
    public ServiceIdentification(String title, String serviceType, String[] serviceTypeVersion) {
        super(title);
        setServiceType(serviceType);
        setServiceTypeVersion(serviceTypeVersion);
        setFees("NONE");
        setAccessConstraints(new String[] {"NONE"});
    }

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

    public String[] getAccessConstraints() {
        return accessConstraints;
    }

    public String getFees() {
        return fees;
    }

    public String[] getServiceTypeVersion() {
        return serviceTypeVersion;
    }

    /**
     * sets the version for this service
     * 
     * @param version the version to set
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

    public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType
     *        The serviceType to set.
     * @throws IllegalArgumentException
     *         if serviceType == null or empty.
     */
    protected void setServiceType(String serviceType) throws IllegalArgumentException {
        if (serviceType == null || serviceType.isEmpty()) {
            throw new IllegalArgumentException("The parameter 'serviceType' is null or empty.");
        }
        else {
            this.serviceType = serviceType;
        }

    }
}