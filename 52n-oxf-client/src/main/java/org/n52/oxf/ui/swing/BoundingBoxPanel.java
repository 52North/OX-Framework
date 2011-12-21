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
 
 Created on: 26.08.2005
 *********************************************************************************/

package org.n52.oxf.ui.swing;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import org.n52.oxf.context.*;
import org.n52.oxf.util.*;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class BoundingBoxPanel extends JPanel implements IEventListener {
    
    private JTextField urx;
    private JTextField ury;
    private JTextField llx;
    private JTextField lly;
    
    private JLabel srsLabel;
    private JTextField srsTextField;
    
    /**
     * 
     */
    public BoundingBoxPanel(MapCanvas map) {
        super();
        
        ContextBoundingBox cBBox = map.getLayerContext().getContextBoundingBox();
        cBBox.addEventListener(this);
        
        
        this.setBorder(new TitledBorder("BoundingBox:"));
        
        MyGridBagLayout layoutManager = new MyGridBagLayout(this);
        this.setLayout(layoutManager);
        
        urx = new JTextField("" + cBBox.getUpperRight().getX());
        ury = new JTextField("" + cBBox.getUpperRight().getY());
        llx = new JTextField("" + cBBox.getLowerLeft().getX());
        lly = new JTextField("" + cBBox.getLowerLeft().getY());
        
        srsTextField = new JTextField();
        setSrsText(cBBox.getSRS());
        
        layoutManager.addComponent( new JLabel("SRS:"), 0,0,1,1,0,100,GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1));
        layoutManager.addComponent(srsTextField,        1,0,1,1,100,100);
        
        layoutManager.addComponent(new JLabel("ur-x:"), 0,2,1,1,0,0,GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1));
        layoutManager.addComponent(urx,                 1,2,1,1,100,100);
        layoutManager.addComponent(new JLabel("ur-y:"), 0,3,1,1,0,100,GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1));
        layoutManager.addComponent(ury,                 1,3,1,1,100,100);
        
        layoutManager.addComponent(new JLabel("ll-x:"), 0,5,1,1,0,100,GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1));
        layoutManager.addComponent(llx,                 1,5,1,1,100,100);
        layoutManager.addComponent(new JLabel("ll-y:"), 0,6,1,1,0,100,GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(1, 1, 1, 1));
        layoutManager.addComponent(lly,                 1,6,1,1,100,100);
        
        
        urx.setEditable(false);
        ury.setEditable(false);
        llx.setEditable(false);
        lly.setEditable(false);
        srsTextField.setEditable(false);
        srsTextField.setFont(new Font("Courier New", java.awt.Font.BOLD | java.awt.Font.ITALIC, 12));
    }

    public void eventCaught(OXFEvent evt) throws OXFEventException {
        
        if(evt.getName().equals(EventName.EXTENT_CHANGED)){
            ContextBoundingBox cBBox = (ContextBoundingBox)evt.getSource();
            
            urx.setText("" + cBBox.getUpperRight().getX());
            ury.setText("" + cBBox.getUpperRight().getY());
            llx.setText("" + cBBox.getLowerLeft().getX());
            lly.setText("" + cBBox.getLowerLeft().getY());
            
            setSrsText(cBBox.getSRS());
        }
        
        else if(evt.getName().equals(EventName.EXTENT_CHANGED_SILENTLY)){
            ContextBoundingBox cBBox = (ContextBoundingBox)evt.getSource();
            
            urx.setText("" + cBBox.getUpperRight().getX());
            ury.setText("" + cBBox.getUpperRight().getY());
            llx.setText("" + cBBox.getLowerLeft().getX());
            lly.setText("" + cBBox.getLowerLeft().getY());
            
            setSrsText(cBBox.getSRS());
        }
        
        else if(evt.getName().equals(EventName.SRS_CHANGED)){
            setSrsText((String)evt.getValue());
        }
    }

    /**
     * 
     * @param text
     */
    private void setSrsText(String text){
        String srs = "not defined";
        if (text != null && !text.equals("")) {
            srs = text;
        }
        srsTextField.setText(srs);
    }
}