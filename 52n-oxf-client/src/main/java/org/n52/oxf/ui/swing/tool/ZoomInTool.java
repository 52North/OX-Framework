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

package org.n52.oxf.ui.swing.tool;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.icons.IconAnchor;
import org.n52.oxf.util.OXFEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class ZoomInTool extends MapTool {
    
    private static final long serialVersionUID = -6783581435965294071L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ZoomInTool.class);
    
    private double zoomFactor = 1.25;
    
    public ZoomInTool(JFrame owner, MapCanvas map) {
        super(owner, map);
        

        setIcon(new ImageIcon(IconAnchor.class.getResource("zoom_in.gif")));
        setToolTipText("Zoom in");
    }

    public void activate(){
        super.activate();
        
        try {
            zoomIn();
        }
        catch (OXFEventException e) {
            LOGGER.error("Could not zoom in.", e);
        }
    }

    private void zoomIn() throws OXFEventException {
        System.out.println("zoom in");
        map.getLayerContext().getContextBoundingBox().zoom(null, zoomFactor);
    }
}