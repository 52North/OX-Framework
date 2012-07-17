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

package org.n52.oxf.ui.wms;

import org.n52.oxf.adapter.IServiceAdapter;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.render.IRenderer;
import org.n52.oxf.valueDomains.spatial.BoundingBox;

public class WMSLayer {

    private String name;
    private String title;
    private String abstractDescription;
    private BoundingBox latLonBoundingBox;
    private BoundingBox[] boundingBoxArray;
    private String[] srsArray;
    private int opaque;
    
    private IServiceAdapter serviceAdapter;
    private IRenderer renderer;
    private IFeatureStore featureStore;
    
    private String serviceURL;
    private ParameterContainer paramCon;
    
    public WMSLayer(String name,
                    String title,
                    BoundingBox latLonBoundingBox,
                    String[] srsArray,
                    IServiceAdapter serviceAdapter,
                    IRenderer renderer,
                    IFeatureStore featureStore,
                    ParameterContainer paramCon,
                    String serviceURL) {
        super();
        this.name = name;
        this.title = title;
        this.latLonBoundingBox = latLonBoundingBox;
        this.srsArray = srsArray;
        this.serviceAdapter = serviceAdapter;
        this.renderer = renderer;
        this.featureStore = featureStore;
        this.paramCon = paramCon;
        this.serviceURL = serviceURL;
    }

    public WMSLayer(String name,
                    String title,
                    String abstractDescription,
                    BoundingBox latLonBoundingBox,
                    BoundingBox[] boundingBoxArray,
                    String[] srsArray,
                    int opaque,
                    IServiceAdapter serviceAdapter,
                    IRenderer renderer,
                    IFeatureStore featureStore,
                    ParameterContainer paramCon,
                    String serviceURL) {
        this.name = name;
        this.title = title;
        this.abstractDescription = abstractDescription;
        this.latLonBoundingBox = latLonBoundingBox;
        this.boundingBoxArray = boundingBoxArray;
        this.srsArray = srsArray;
        this.opaque = opaque;       
        this.serviceAdapter = serviceAdapter;
        this.renderer = renderer;
        this.featureStore = featureStore;
        this.paramCon = paramCon;
        this.serviceURL = serviceURL;
    }
    
    public ParameterContainer getParameterContainer() {
        return paramCon;
    }

    public IRenderer getRenderer() {
        return renderer;
    }

    public String getServiceURL() {
        return serviceURL;
    }

    public String getName() {
        return name;
    }

    public IFeatureStore getFeatureStore() {
        return featureStore;
    }

    public IServiceAdapter getServiceAdapter() {
        return serviceAdapter;
    }

    public String getAbstractDescription() {
        return abstractDescription;
    }

    public BoundingBox[] getBoundingBoxArray() {
        return boundingBoxArray;
    }

    public BoundingBox getLatLonBoundingBox() {
        return latLonBoundingBox;
    }

    public ParameterContainer getParamCon() {
        return paramCon;
    }

    public String getTitle() {
        return title;
    }

    public String[] getSrsArray() {
        return srsArray;
    }

    public int getOpaque() {
        return opaque;
    }
    
    
}