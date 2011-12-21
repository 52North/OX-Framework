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
 
 Created on: 07.01.2006
 *********************************************************************************/

package org.n52.oxf.ui.swing.wms;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import org.n52.oxf.ui.swing.*;
import org.n52.oxf.ui.swing.tree.*;
import org.n52.oxf.ui.swing.wms.ConnectWMSDialogController.capabilitiesState;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ConnectWMSDialog extends JDialog {

    private ConnectWMSDialogController controller;

    private JPanel jContentPane = null;
    private JPanel buttonPanel = null;
    private JPanel mainPanel = null;
    private JPanel statusPanel = null;
    private JButton getCapabilitiesButton = null;
    private JButton getMapButton = null;
    private JLabel statusLabel = null;
    // the actual status of the request
    private JLabel status = null;
    private JLabel serviceURLLabel = null;
    private JComboBox serviceURLCB = null;
    private JProgressBar progressBar = null;

    private final int progressLength = 400;

    private JLabel versionLabel = null;

    private JList versionList = null;

    private DefaultListModel defaultListModel = null; // @jve:decl-index=0:visual-constraint=""

    /**
     * This method initializes buttonPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 2;
            gridBagConstraints5.weightx = 10.0D;
            gridBagConstraints5.weighty = 10.0D;
            gridBagConstraints5.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints5.gridy = 0;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 0);
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.weightx = 10.0D;
            gridBagConstraints4.weighty = 10.0D;
            gridBagConstraints4.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints4.gridx = 0;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.add(getGetCapabilitiesButton(), gridBagConstraints4);
            buttonPanel.add(getGetMapButton(), gridBagConstraints5);
        }
        return buttonPanel;
    }

    /**
     * This method initializes mainPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints7.gridy = 1;
            gridBagConstraints7.weightx = 1.0;
            gridBagConstraints7.weighty = 1.0;
            gridBagConstraints7.gridx = 1;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints6.insets = new java.awt.Insets(10, 10, 10, 10);
            gridBagConstraints6.gridy = 1;
            versionLabel = new JLabel();
            versionLabel.setText("WMS Version:");
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridx = 1;
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.weightx = 100.0D;
            gridBagConstraints3.weighty = 0.0D;
            gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 0);
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.insets = new java.awt.Insets(10, 10, 10, 10);
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.weightx = 0.0D;
            gridBagConstraints2.weighty = 0.0D;
            gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints2.gridx = 0;
            serviceURLLabel = new JLabel();
            serviceURLLabel.setText("Service-URL:");
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.add(serviceURLLabel, gridBagConstraints2);
            mainPanel.add(getServiceURLCB(), gridBagConstraints3);
            mainPanel.add(versionLabel, gridBagConstraints6);
            mainPanel.add(getVersionList(), gridBagConstraints7);
        }
        return mainPanel;
    }

    /**
     * 
     * @return the status panel for the getCapabilities status
     */
    private JPanel getStatusPanel() {
        if (statusPanel == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints1.anchor = GridBagConstraints.WEST;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.insets = new java.awt.Insets(5, 10, 5, 10);
            gridBagConstraints1.weightx = 0.0D;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints2.anchor = GridBagConstraints.WEST;
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.insets = new java.awt.Insets(5, 10, 5, 10);
            gridBagConstraints2.weightx = 0.0D;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 2;
            gridBagConstraints4.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints4.anchor = GridBagConstraints.EAST;
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.insets = new java.awt.Insets(5, 10, 5, 10);
            gridBagConstraints4.weightx = 100.0D;

            statusPanel = new JPanel();

            statusPanel.setLayout(new GridBagLayout());
            statusLabel = new JLabel();
            statusLabel.setText("Status: ");
            statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
            statusPanel.add(statusLabel, gridBagConstraints1);
            status = new JLabel();
            status.setText(capabilitiesState.NO_ACTIVE_REQUEST.toString());
            statusPanel.add(status, gridBagConstraints2);

            progressBar = new JProgressBar(0, progressLength);
            progressBar.setValue(0);
            statusPanel.add(progressBar, gridBagConstraints4);
        }

        return statusPanel;
    }

    /**
     * This method initializes getCapabilitiesButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getGetCapabilitiesButton() {
        if (getCapabilitiesButton == null) {
            getCapabilitiesButton = new JButton();
            getCapabilitiesButton.setText("GetCapabilities");
            getCapabilitiesButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_getCapabilitiesButton(e);
                }
            });
        }
        return getCapabilitiesButton;
    }

    /**
     * This method initializes getObservationButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getGetMapButton() {
        if (getMapButton == null) {
            getMapButton = new JButton();
            getMapButton.setText("GetMap");
            getMapButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_getMapButton(e);
                }
            });
        }
        return getMapButton;
    }

    /**
     * This method initializes serviceURLCB
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getServiceURLCB() {
        if (serviceURLCB == null) {
            serviceURLCB = new JComboBox();
            serviceURLCB.setEditable(true);
        }
        return serviceURLCB;
    }

    /**
     * This method initializes versionList
     * 
     * @return javax.swing.JList
     */
    public JList getVersionList() {
        if (versionList == null) {
            versionList = new JList();
            versionList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            versionList.setModel(getDefaultListModel());

            ((DefaultListModel) versionList.getModel()).addElement("1.0.0");
            ((DefaultListModel) versionList.getModel()).addElement("1.1.0");
            ((DefaultListModel) versionList.getModel()).addElement("1.1.1");

            versionList.setSelectedIndex(2);
        }
        return versionList;
    }

    /**
     * This method initializes defaultListModel
     * 
     * @return javax.swing.DefaultListModel
     */
    private DefaultListModel getDefaultListModel() {
        if (defaultListModel == null) {
            defaultListModel = new DefaultListModel();
        }
        return defaultListModel;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new ConnectWMSDialog(null, null, null).setVisible(true);

    }

    public ConnectWMSDialog(Component owner, MapCanvas map, ContentTree tree, String[] wmsURLS) {
        initialize(owner);

        controller = new ConnectWMSDialogController(this, map, tree);
        controller.loadURLs(wmsURLS);

    }

    /**
     * This is the default constructor
     */
    public ConnectWMSDialog(Component owner, MapCanvas map, ContentTree tree) {
        initialize(owner);

        controller = new ConnectWMSDialogController(this, map, tree);
        controller.loadURLs();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    protected void initialize(Component owner) {
        this.setSize(520, 178);
        this.setLocation(owner.getLocation());
        this.setModal(false);
        this.setTitle("Connect WMS");
        this.setContentPane(getJContentPane());
        
        addWindowAdapter();
    }
    
    private void addWindowAdapter() {
        // we want to use our own window listener when closing the window.
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        // listen for window closing and window gained focus events
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // warn if any requests are still pending
                if (controller.isGetCapabilitiesTaskBusy() || controller.isGetMapTaskBusy()) {
                    Object[] options = {"OK", "CANCEL"};
                    String warning_message = "A request task is still busy.\n"
                            + "Clicking 'OK' will attempt to cancel all busy tasks and \nclose all dependent dialogs. \n"
                            + "Do you wish to continue?";
                    int chosenOption = JOptionPane.showOptionDialog(null,
                                                                    warning_message,
                                                                    "Warning",
                                                                    JOptionPane.DEFAULT_OPTION,
                                                                    JOptionPane.WARNING_MESSAGE,
                                                                    null,
                                                                    options,
                                                                    options[0]);
                    if (chosenOption == JOptionPane.OK_OPTION) {
                        super.windowClosing(e);
                        controller.dialogWindowClosed();
                        (e.getWindow()).dispose();
                    }
                }
                else {
                    super.windowClosing(e);
                    controller.dialogWindowClosed();
                    (e.getWindow()).dispose();
                }
            }
        });
        addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                super.windowGainedFocus(e);
                setButtonStates();
            }
        });
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
            gridBagConstraints2.gridy = 2;
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.weightx = 100.0D;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weighty = 100.0D;
            gridBagConstraints1.weightx = 100.0D;
            gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.weightx = 100.0D;
            gridBagConstraints.weighty = 100.0D;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;

            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.add(getButtonPanel(), gridBagConstraints);
            jContentPane.add(getMainPanel(), gridBagConstraints1);
            jContentPane.add(getStatusPanel(), gridBagConstraints2);
        }
        return jContentPane;
    }

    /**
     * Sets the enabled states of the getMap, and getCapabilities buttons and updates the status label for the
     * getCapabilities request. This is usually called from one of the background threads in the controller
     * for this dialog.
     * 
     */
    public void setButtonStates() {
        // GUI updates should only be performed in the EventDispatch thread
        if (SwingUtilities.isEventDispatchThread()) {
            // check the state of the getCapabilities request
            ConnectWMSDialogController.capabilitiesState state = controller.getState();
            switch (state) {
            case NO_ACTIVE_REQUEST:
                // double check
                if (controller.isGetCapabilitiesTaskBusy()) {
                    // inconsistent state
                    throw new RuntimeException("GetCapabilities request is in an inconsistent state.");
                }
                else {
                    status.setText(capabilitiesState.NO_ACTIVE_REQUEST.toString());
                }
                getCapabilitiesButton.setEnabled(true);
                progressBar.setIndeterminate(false);
                progressBar.setValue(0);
                break;
            case INITIALIZING_REQUEST:
                status.setText(capabilitiesState.INITIALIZING_REQUEST.toString());
                getCapabilitiesButton.setEnabled(false);
                progressBar.setIndeterminate(true);
                break;
            case WAITING_FOR_SERVICE:
                status.setText(capabilitiesState.WAITING_FOR_SERVICE.toString());
                getCapabilitiesButton.setEnabled(false);
                progressBar.setIndeterminate(true);
                break;
            case INITIALIZING_RESPONSE_DISPLAY:
                status.setText(capabilitiesState.INITIALIZING_RESPONSE_DISPLAY.toString());
                getCapabilitiesButton.setEnabled(false);
                progressBar.setIndeterminate(true);
                break;
            case DONE:
                status.setText(capabilitiesState.DONE.toString());
                getCapabilitiesButton.setEnabled(false);
                progressBar.setIndeterminate(false);
                progressBar.setValue(progressLength);
                break;
            }

            if (controller.isGetMapTaskBusy()) {
                getMapButton.setEnabled(false);
            }
            else {
                getMapButton.setEnabled(true);
            }
        }
        else {
            // if this is not the event dispatch thread, put a request on the
            // queue for that thread.
            Runnable changeButtons = new Runnable() {
                public void run() {
                    setButtonStates();
                }
            };
            SwingUtilities.invokeLater(changeButtons);
        }
    }

} // @jve:decl-index=0:visual-constraint="10,10"