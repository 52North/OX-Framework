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
package org.n52.oxf.ows.capabilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Could also be implemented by using Generics. This class is a container for the dataIdentificationList
 * elements. dataIdentifications are something like FeatureTypes (WFS) or Layers (WMS) or Coverages (WCS).
 * This container can be empty or null, if the service has no data (i.e. WebNotificationService,
 * WebAlertService, SensorPlaningService).
 *
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 * @author <a href="mailto:broering@52north.org">Arne Broering </a>
 */
public class Contents {

    private final List<Dataset> dataIdentificationList;

    /**
     * initializes a new (empty) ArrayList for dataIdentifications.
     */
    public Contents() {
        dataIdentificationList = new ArrayList<Dataset>();
    }

    public Contents(List<Dataset> dataIdentificationList) {
        this.dataIdentificationList = dataIdentificationList;
    }

    public void addDataIdentication(Dataset di) {
        dataIdentificationList.add(di);
    }

    /**
     * @return a XML representation of this Contents-section.
     */
    public String toXML() {
        String res = "<Contents>";

        res += "<DataIdentifications>";
        if (dataIdentificationList != null) {
            StringBuilder sb = new StringBuilder(res);
            for (Dataset dataID : dataIdentificationList) {
                sb.append(dataID.toXML());
            }
            res = sb.toString();
        }
        res += "</DataIdentifications>";

        res += "</Contents>";
        return res;
    }

    public Dataset getDataIdentification(int i) {
        return (Dataset) dataIdentificationList.get(i);
    }

    /**
     *
     * @param identifier a Dataset identifier
     * @return the dataset with the specified identifier/name or <code>null</code> if there is no dataset
     *         with the specified identifier/name.
     */
    public Dataset getDataIdentification(String identifier) {
        for (Dataset data : dataIdentificationList) {
            if (data.getIdentifier().equals(identifier)) {
                return data;
            }
        }

        return null;
    }

    public int getDataIdentificationCount() {
        return dataIdentificationList.size();
    }

    public String[] getDataIdentificationIDArray() {
        String[] idArray = new String[dataIdentificationList.size()];

        for (int i=0; i<dataIdentificationList.size(); i++)
            idArray[i] = dataIdentificationList.get(i).getIdentifier();

        return idArray;
    }

    public void removeDataIdentification(int i) {
        dataIdentificationList.remove(i);
    }
}
