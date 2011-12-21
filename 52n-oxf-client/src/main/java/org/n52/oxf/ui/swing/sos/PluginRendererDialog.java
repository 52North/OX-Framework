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
 
 Created on: 01.01.2007
 *********************************************************************************/

package org.n52.oxf.ui.swing.sos;

import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.JButton;

public class PluginRendererDialog extends JDialog {

    private String className;
    
    private JPanel jContentPane = null;
    private JLabel rendererClassNameLabel = null;
    private JTextField rendererClassNameTextField = null;
    private JButton okButton = null;

    /**
     * This method initializes rendererClassNameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getRendererClassNameTextField() {
        if (rendererClassNameTextField == null) {
            rendererClassNameTextField = new JTextField();
        }
        return rendererClassNameTextField;
    }

    /**
     * This method initializes okButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setText("OK");
            okButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    className = rendererClassNameTextField.getText();
                    
                    setVisible(false);
                }
            });
        }
        return okButton;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    /**
     * This is the default constructor
     */
    public PluginRendererDialog(JDialog owner) {
        super(owner);
        initialize(owner);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(JDialog owner) {
        this.setSize(427, 125);
        this.setLocation(owner.getLocation());
        this.setTitle("Plugin Renderer");
        this.setContentPane(getJContentPane());
        
        getRendererClassNameTextField().setText("org.n52.oxf.render.sos.IDWRenderer");
        
        this.setModal(true);
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridwidth = 2;
            gridBagConstraints2.gridy = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.gridx = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            rendererClassNameLabel = new JLabel();
            rendererClassNameLabel.setText("Renderer class-name:");
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.add(rendererClassNameLabel, gridBagConstraints);
            jContentPane.add(getRendererClassNameTextField(), gridBagConstraints1);
            jContentPane.add(getOkButton(), gridBagConstraints2);
        }
        return jContentPane;
    }
    
    public String getClassName() {
        return className;
    }

    
}  //  @jve:decl-index=0:visual-constraint="10,10"