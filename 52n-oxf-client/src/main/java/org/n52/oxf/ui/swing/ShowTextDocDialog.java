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

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class ShowTextDocDialog extends JDialog {

    private JPanel jContentPane = null;
    private JScrollPane mainScrollPane = null;
    private String text = "";
    private JTextArea textArea = null;

    /**
     * This method initializes mainScrollPane   
     *  
     * @return javax.swing.JScrollPane  
     */
    private JScrollPane getMainScrollPane() {
        if (mainScrollPane == null) {
            mainScrollPane = new JScrollPane();
            mainScrollPane.setViewportView(getTextPane());
        }
        return mainScrollPane;
    }

    /**
     * This method initializes mainEditorPane   
     *  
     */
    private JTextArea getTextPane() {
        if (textArea == null) {
            textArea = new JTextArea(text);
            textArea.setEditable(false);
        }
        return textArea;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String text = "1. Line.";
        
        text += "\n2. Line";
        
        new ShowTextDocDialog(new JDialog(), "Hallo Welt", text).setVisible(true);
    }

    
    
    /**
     * This is the default constructor
     * @param text 
     * @param textS 
     */
    public ShowTextDocDialog(JDialog owner, String title, String text) {
        super(owner);
        
        this.text = text;
        
        initialize(owner);
        
        this.setTitle(title);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(JDialog owner) {
        this.setSize(400, 200);
        this.setLocation(owner.getLocation());
        this.setModal(true);
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