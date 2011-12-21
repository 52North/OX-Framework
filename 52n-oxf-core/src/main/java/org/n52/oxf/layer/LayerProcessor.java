/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 22.05.2006
 *********************************************************************************/

package org.n52.oxf.layer;

import org.apache.log4j.*;
import org.n52.oxf.*;
import org.n52.oxf.owsCommon.*;
import org.n52.oxf.util.*;

/**
 * This class is a specialized Thread which can be used to execute the "request - and - render -
 * data"-process. Perfomance will be increased when this <code>LayerProcessor</code> will be started
 * "parallel" for all <code>Layer</code>s.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public abstract class LayerProcessor implements IEventEmitter {

    private static Logger LOGGER = LoggingHandler.getLogger(LayerProcessor.class);

    /** the associated IContextLayer */
    protected IContextLayer layer;

    protected OXFEventSupport eventSupport;

    /**
     * 
     */
    protected LayerProcessor(IContextLayer layer) {
        super();

        this.eventSupport = new OXFEventSupport(this);
        
        this.layer = layer;
    }

    /**
     * executes the data request and renders the data directly afterwards.
     * 
     */
    public void startProcess() {

        LOGGER.info("Processing layer: '" + layer.getIDName() + "'");

        getProcess().start();
    }

    /**
     * 
     * @return the process which realizes the parallelism of data processing.
     */
    public abstract Thread getProcess();
    

    // ----- OXFEventEmitting:

    /**
     * Add an IEventListener to the OXFEventSupport.
     */
    public void addEventListener(IEventListener listener) {
        eventSupport.addOXFEventListener(listener);
    }

    /**
     * Remove an IEventListener from the OXFEventSupport.
     */
    public void removeEventListener(IEventListener listener) {
        eventSupport.removeOXFEventListener(listener);

    }
    
    /**
     * Returns the layer currently being processed by this layer processor
     * @return org.n52.oxf.layer.IContextLayer
     */
    public IContextLayer getLayer() {
    	return layer;
    }
}