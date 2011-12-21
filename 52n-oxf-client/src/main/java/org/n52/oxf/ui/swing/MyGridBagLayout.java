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
 
 Created on: 15.08.2005
 *********************************************************************************/

package org.n52.oxf.ui.swing;

import java.awt.*;

/**
 * This class is an easy to use GridBagLayout-Manager for your Container (target). 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class MyGridBagLayout extends GridBagLayout {

    private Container target;
    
    /**
     * 
     */
    public MyGridBagLayout(Container target) {
        super();
        
        this.target = target;
    }


    public void addComponent(Component c, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty) {
        this.addComponent(-1, c, gridx, gridy, gridwidth, gridheight, weightx, weighty);
    }
    
    public void addComponent(int index, Component c, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty) {

        GridBagConstraints componentGBC = new GridBagConstraints(gridx,
                                                                 gridy,
                                                                 gridwidth,
                                                                 gridheight,
                                                                 weightx,
                                                                 weighty,
                                                                 GridBagConstraints.PAGE_START,
                                                                 GridBagConstraints.HORIZONTAL,
                                                                 new Insets(1, 1, 1, 1),
                                                                 0,
                                                                 0);

        setConstraints(c, componentGBC);
        target.add(c, index);
    }
    
    public void addComponent(Component c, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets) {
        this.addComponent(-1, c, gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets);
    }
    
    public void addComponent(int index, Component c, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets) {

        GridBagConstraints componentGBC = new GridBagConstraints(gridx,
                                                                 gridy,
                                                                 gridwidth,
                                                                 gridheight,
                                                                 weightx,
                                                                 weighty,
                                                                 anchor,
                                                                 fill,
                                                                 insets,
                                                                 0,
                                                                 0);

        setConstraints(c, componentGBC);
        target.add(c, index);
    }
}