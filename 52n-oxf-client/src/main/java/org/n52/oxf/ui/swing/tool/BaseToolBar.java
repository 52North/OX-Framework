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

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.n52.oxf.layer.*;
import org.n52.oxf.ui.swing.*;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class BaseToolBar extends JToolBar implements ActionListener {
    
    /**
     * the associated MapCanvas.
     */
    protected MapCanvas map;
    
    protected List<IContextLayer> selectedLayerList;

    protected JFrame owner;
    
    /**
     * 
     */
    public BaseToolBar(JFrame owner, int orientation, MapCanvas map) {
        super(orientation);
        
        this.map = map;
        this.owner = owner; 
    }

    public void addTool(MapTool tool){
        add(tool);
        tool.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent evt) {
        MapTool selectedTool = (MapTool)evt.getSource();
        map.setActiveTool(selectedTool);
    }

    
    
    public MapCanvas getMap() {
        return map;
    }

    public void setMap(MapCanvas map) {
        this.map = map;
    }

    public List<IContextLayer> getSelectedLayerList() {
        return selectedLayerList;
    }

    public void setSelectedLayerList(List<IContextLayer> selectedLayerList) {
        this.selectedLayerList = selectedLayerList;
    }
    
}