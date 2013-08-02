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

import org.n52.oxf.adapter.*;
import org.n52.oxf.ows.*;
import org.n52.oxf.ows.capabilities.*;
import org.n52.oxf.render.*;
import org.n52.oxf.serialization.ContextWriter;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class RasterServiceLayer extends AbstractServiceLayer {

    protected RasterServiceLayerProcessor processor;

    /**
     * @param adapter
     * @param rasterRenderer
     * @param descriptor
     * @param parameterContainer
     * @param layerName
     * @param layerTitle
     */
    public RasterServiceLayer(IServiceAdapter adapter,
                              IRasterDataRenderer rasterRenderer,
                              ServiceDescriptor descriptor,
                              ParameterContainer parameterContainer,
                              String layerName,
                              String layerTitle,
                              String resourceOperationName) {
        super(adapter, rasterRenderer, descriptor, parameterContainer, layerName, layerTitle, resourceOperationName);

        this.processor = new RasterServiceLayerProcessor(this);
    }

    /**
     * 
     */
    @Override
    public IRasterDataRenderer getRenderer() {
        return (IRasterDataRenderer) renderer;
    }

    /**
     * 
     * @return a RasterServiceLayerProcessor which can be used to execute the "download - and - render -
     *         data"-process in a separate <code>Thread</code>.
     */
    public RasterServiceLayerProcessor getProcessor() {
        return processor;
    }
}