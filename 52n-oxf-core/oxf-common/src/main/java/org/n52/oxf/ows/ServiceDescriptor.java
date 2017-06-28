/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.ows;

import org.n52.oxf.ows.capabilities.Contents;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.n52.oxf.ows.capabilities.ServiceIdentification;
import org.n52.oxf.ows.capabilities.ServiceProvider;

/**
 * This class provides access to all the serviceMetadata which is described in the OWS Common spec (04-016r5h)
 * of the OGC. This Descriptor has to be filled by each ServiceAdapter.
 * 
 * The main parts of that model are:
 * <ul>
 * <li>ServiceIdentification</li>
 * <li>ServiceProvider</li>
 * <li>OperationsMetadata</li>
 * <li>Contents</li>
 * </ul>
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster </a>
 * @author <a href="mailto:broering@52north.org">Arne Broering </a>
 */
public class ServiceDescriptor {

    /**
     * required attribute.
     */
    private ServiceIdentification serviceIdentification;

    /**
     * required attribute.
     */
    private ServiceProvider serviceProvider;

    /**
     * required attribute.
     */
    private OperationsMetadata operationsMetadata;

    /**
     * required attribute.
     */
    private Contents contents;

    /**
     * Zero or one(optional) value for updateSequence is possible.
     */
    private String updateSequence;

    /**
     * The version of the GetCapabilities operation response. <br>
     * <br>
     * One (mandatory) value for version is required. <br>
     * <br>
     * Example value for version: "1.0.0"
     */
    private String version;

    public ServiceDescriptor(String version,
                             ServiceIdentification si,
                             ServiceProvider sp,
                             OperationsMetadata om,
                             Contents c) {
        setVersion(version);
        setServiceIdentification(si);
        setServiceProvider(sp);
        setOperationsMetadata(om);
        setContents(c);
    }

    public ServiceDescriptor(String url,
                             String version,
                             ServiceIdentification si,
                             ServiceProvider sp,
                             OperationsMetadata om,
                             Contents c,
                             String updateSequence) {
        setVersion(version);
        setUpdateSequence(updateSequence);
        setServiceIdentification(si);
        setServiceProvider(sp);
        setOperationsMetadata(om);
        setContents(c);
    }

    /**
     * @return a XML representation of this ServiceDescriptor.
     */
    public String toXML() {
        String res = "<ServiceDescriptor version=\"" + version + "\" updateSequence=\"" + updateSequence + "\">";
        res += contents.toXML();
        res += operationsMetadata.toXML();
        res += serviceIdentification.toXML();
        res += serviceProvider.toXML();

        res += "</ServiceDescriptor>";
        return res;
    }

    public String getUpdateSequence() {
        return updateSequence;
    }

    protected void setUpdateSequence(String updateSequence) {
        this.updateSequence = updateSequence;
    }

    public String getVersion() {
        return version;
    }

    /**
     * sets the version of the GetCapabilities operation response.
     * 
     * @param version the version to set
     * @throws IllegalArgumentException
     *         if version.equals("").
     */
    protected void setVersion(String version) throws IllegalArgumentException {
        if (version.equals("")) {
            throw new IllegalArgumentException("The parameter 'version' is illegal");
        }
        this.version = version;
    }

    public OperationsMetadata getOperationsMetadata() {
        return operationsMetadata;
    }

    protected void setOperationsMetadata(OperationsMetadata operationsMetadata) {
        this.operationsMetadata = operationsMetadata;
    }

    public ServiceIdentification getServiceIdentification() {
        return serviceIdentification;
    }

    protected void setServiceIdentification(ServiceIdentification serviceIdentification) {
        this.serviceIdentification = serviceIdentification;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    protected void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    protected void setContents(Contents contents) {
        this.contents = contents;
    }

    public Contents getContents() {
        return contents;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ServiceDescriptor #####################\nversion: ");
        sb.append(this.version);
        sb.append("\n");
        sb.append(this.serviceProvider);
        sb.append("\n");
        sb.append(this.serviceIdentification);
        sb.append("\n");
        sb.append(this.contents);
        sb.append("\n");
        sb.append(this.operationsMetadata);
        sb.append("\n");
        sb.append(this.updateSequence);
        sb.append("\n#######################################\n");
        return sb.toString();
    }
}