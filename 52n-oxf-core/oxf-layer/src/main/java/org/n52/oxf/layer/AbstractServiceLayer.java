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

package org.n52.oxf.layer;

import org.n52.oxf.adapter.IServiceAdapter;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.render.IRenderer;
import org.n52.oxf.valueDomains.spatial.BoundingBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public abstract class AbstractServiceLayer extends AbstractLayer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServiceLayer.class);

    protected IServiceAdapter serviceAdapter;

    protected ServiceDescriptor serviceDescriptor;

    /**
     * the name of the service Operation providing the data.
     */
    protected String resourceOperationName;

    /**
     * 
     * @param adapter
     * @param serviceRenderer
     * @param descriptor
     * @param parameterContainer
     *        initial ParameterContainer
     * @param layerName
     */
    public AbstractServiceLayer(IServiceAdapter adapter,
                                IRenderer renderer,
                                ServiceDescriptor descriptor,
                                ParameterContainer parameterContainer,
                                String layerName,
                                String layerTitle,
                                String resourceOperationName) {
        super(layerName, layerTitle, parameterContainer, renderer);

        this.serviceAdapter = adapter;
        this.serviceDescriptor = descriptor;
        this.resourceOperationName = resourceOperationName;
    }

    public IServiceAdapter getServiceAdapter() {
        return serviceAdapter;
    }

    public String getResourceOperationName() {
        return resourceOperationName;
    }

    /**
     * 
     * @return
     */
    public ServiceDescriptor getServiceDescriptor() {
        return serviceDescriptor;
    }

    /**
     * 
     */
    public String getLayerSourceType() {
        return getServiceDescriptor().getServiceIdentification().getServiceType();
    }

    /**
     * 
     */
    public String getLayerSourceTitle() {
        return getServiceDescriptor().getServiceIdentification().getTitle();
    }

    /**
     * 
     */
    public IBoundingBox getBBox() {
        if (getParameterContainer().containsParameterShellWithCommonName(Parameter.COMMON_NAME_BBOX)) {
            BoundingBox paramBBox = (BoundingBox) getParameterContainer().getParameterShellWithCommonName(Parameter.COMMON_NAME_BBOX).getSpecifiedValue();
            BoundingBox bBox = new BoundingBox(getSrs(), paramBBox.getLowerCorner(), paramBBox.getUpperCorner());
            return bBox;
        }
        return null;
    }

    /**
     * 
     */
    public String getSrs() {
        if (getParameterContainer().containsParameterShellWithCommonName(Parameter.COMMON_NAME_SRS)) {
            String srs = (String) getParameterContainer().getParameterShellWithCommonName(Parameter.COMMON_NAME_SRS).getSpecifiedValue();
            return srs;
        }
        return null;
    }
}