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
 
 Created on: 11.08.2005
 *********************************************************************************/

package org.n52.oxf.ui.swing.tool;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.n52.oxf.ui.swing.*;

/**
 * 
 * A MapTool can be selected by calling the setActiveTool method of the MapCanvas class. This method will
 * automatically de-select the previously selected MapTool.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class MapTool extends JButton implements MouseInputListener {

    protected String tooltipText = "Tooltip...";
    protected Dimension size     = new Dimension(16, 16);
    protected Cursor cursor      = new Cursor(Cursor.DEFAULT_CURSOR);
    
    protected MapCanvas map;
    protected JFrame owner;
    
    
    /**
     * 
     */
    public MapTool(JFrame owner, MapCanvas map) {
        super();
        
        this.map = map;
        this.owner = owner;
        
        setSize(size);
        setToolTipText(tooltipText);
    }

    /**
     * called by the MapCanvas at the end of the <code>setActiveTool</code> method.
     */
    public void activate() {
        setCursor(cursor);
    }

    public void mouseClicked(MouseEvent evt) {
    }

    public void mousePressed(MouseEvent evt) {
    }

    public void mouseReleased(MouseEvent evt) {
    }

    public void mouseEntered(MouseEvent evt) {
    }

    public void mouseExited(MouseEvent evt) {
    }
    
    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }
    
    public void setMap(MapCanvas map) {
        this.map = map;
    }
    
    public MapCanvas getMapCanvas(){
        return map;
    }
}