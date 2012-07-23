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

    private List<Dataset> dataIdentificationList;

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
            for (Dataset dataID : dataIdentificationList) {
                res += dataID.toXML();
            }
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
     * @param identifier
     * @return the dataset with the specified identifier/name or <code>null</code> if there is no dataset
     *         with teh specified identifier/name.
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