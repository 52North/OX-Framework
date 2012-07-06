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

package org.n52.oxf.ui.swing;

import java.awt.*;

import javax.swing.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class ShowXMLDocDialog extends JDialog {

    private JPanel jContentPane = null;
    private JScrollPane mainScrollPane = null;
    private String text = "";
    private XMLPane xmlPane = null;

    /**
     * This method initializes mainScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getMainScrollPane() {
        if (mainScrollPane == null) {
            mainScrollPane = new JScrollPane();
            mainScrollPane.setViewportView(getXMLPane());
        }
        return mainScrollPane;
    }

    /**
     * This method initializes mainEditorPane	
     * 	
     */
    private XMLPane getXMLPane() {
        if (xmlPane == null) {
            xmlPane = new XMLPane(text);
            xmlPane.setEditable(false);
        }
        return xmlPane;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        JDialog d = new JDialog();
        
        new ShowXMLDocDialog(new Point(100,100), "Hallo Welt", "<test>Hallo Welt</test>").setVisible(true);
        
        d.dispose();
    }

    /**
     * This is the default constructor
     * @param text 
     * @param textS 
     */
    public ShowXMLDocDialog(Point p, String title, String text) {
        this.text = text;
        
        initialize(p);
        
        this.setTitle(title);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(Point p) {
        this.setSize(800, 355);
        
        this.setLocation(p);
        
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    protected JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getMainScrollPane(), java.awt.BorderLayout.CENTER);
        }
        return jContentPane;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"