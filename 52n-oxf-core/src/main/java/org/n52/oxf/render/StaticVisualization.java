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
 
 Created on: 07.06.2006
 *********************************************************************************/


package org.n52.oxf.render;

import java.awt.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class StaticVisualization implements IVisualization {

    protected Image image;
    
    protected Image legend = null;
    
    /**
     * @param timeStamp
     */
    public StaticVisualization(Image image) {
        this.image = image;
    }
    
    /**
     * @param timeStamp
     * @param legend
     */
    public StaticVisualization(Image image, Image legend) {
        this.image = image;
        this.legend = legend;
    }

    public Image getRendering() {
        return image;
    }
    
    public Image getLegend() {
        return legend;
    }
}