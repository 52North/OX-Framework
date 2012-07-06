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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ShowRequestDialog extends ApprovalDialog implements ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JPanel buttonPanel = null;
    private JScrollPane mainScrollPane = null;
    private XMLPane xmlPane = null;
    private JButton sendButton = null;
    private JButton cancelButton = null;

    private String buttonText = "Send Request - show Response Doc";

    private String text;

    /**
     * This is the default constructor
     */
    public ShowRequestDialog(Component owner, String title, String text) {
        this.text = text;

        initialize(owner);

        this.setTitle(title);
    }

    public ShowRequestDialog(Component owner, String title, String text, String buttonCaption) {
        buttonText = buttonCaption;

        this.text = text;

        initialize(owner);

        this.setTitle(title);

    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(Component owner) {
        this.setSize(1000, 600);
        this.setLocation(owner.getLocation());
        this.setModal(true);
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 100.0D;
            gridBagConstraints.weighty = 100.0D;
            gridBagConstraints.gridx = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints1.weightx = 0.0D;
            gridBagConstraints1.weighty = 0.0D;
            gridBagConstraints1.gridy = 1;
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.add(getButtonPanel(), gridBagConstraints1);
            jContentPane.add(getMainScrollPane(), gridBagConstraints);
        }
        return jContentPane;
    }

    /**
     * This method initializes buttonPanel
     * 
     * @return javax.swing.JPanel
     */
    protected JPanel getButtonPanel() {
        if (buttonPanel == null) {

            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 2;
            gridBagConstraints3.gridy = 0;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 0;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.add(getSendButton(), gridBagConstraints2);
            buttonPanel.add(getCancelButton(), gridBagConstraints3);
        }
        return buttonPanel;
    }

    /**
     * This method initializes textSP
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
     * This method initializes xmlPane
     * 
     * @return XMLPane
     */
    private XMLPane getXMLPane() {
        if (xmlPane == null) {
            xmlPane = new XMLPane(text);
            xmlPane.setEditable(false);
        }
        return xmlPane;
    }

    /**
     * This method initializes sendButton
     * 
     * @return javax.swing.JButton
     */
    protected JButton getSendButton() {
        if (sendButton == null) {
            sendButton = new JButton();
            sendButton.setText(buttonText);
            sendButton.addActionListener(this);
        }
        return sendButton;
    }

    /**
     * This method initializes cancelButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText("Cancel");
            cancelButton.setMnemonic(java.awt.event.KeyEvent.VK_ESCAPE);
            cancelButton.addActionListener(this);
        }
        return cancelButton;
    }

    /**
     * 
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(getCancelButton())) {
            setReturnVal(CANCEL_OPTION);
            dispose();
        }
        else if (e.getSource().equals(getSendButton())) {
            setReturnVal(APPROVE_OPTION);
            dispose();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // new ShowRequestDialog(new JDialog(), "Hallo Welt", "<test>Hallo Welt</test>").setVisible(true);
        new ShowRequestDialog(new JDialog(), "Hallo Welt", "<test>Hallo Welt</test>", "test").setVisible(true);
    }

} // @jve:decl-index=0:visual-constraint="10,10"