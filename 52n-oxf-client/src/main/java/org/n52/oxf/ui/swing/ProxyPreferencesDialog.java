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
 
 Created on: 01.10.2006
 *********************************************************************************/

package org.n52.oxf.ui.swing;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.n52.oxf.util.LoggingHandler;

public class ProxyPreferencesDialog extends JDialog {

    private Logger LOGGER = LoggingHandler.getLogger(ProxyPreferencesDialog.class);
    
    private JPanel proxyPanel;
    private JRadioButton useProxyRadioButton;
    private JLabel proxyPortLabel;
    private JTextField proxyPortField;
    private JLabel proxyNameLabel;
    private JTextField proxyNameField;
    private JButton useSettingsButton;
    private JLabel nonProxyHostsLabel;
    private JTextField nonProxyHostsText;

    private JLabel outputLabel;
    private JLabel portLabel;

    /**
     * 
     * @param gui
     */
    public ProxyPreferencesDialog(Component owner) {
        super();

        setTitle("Specify Proxy Settings.");

        initComponents(owner);

        // loading Proxy-Properties from System:
        String proxyHost = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");
        String nonProxyHosts = System.getProperty("http.nonProxyHosts");

        setProxySettings(proxyHost, proxyPort, nonProxyHosts);
    }

    /**
     * 
     * 
     */
    private void initComponents(Component owner) {
        proxyPanel = new JPanel();
        useProxyRadioButton = new JRadioButton("Do you want to use a proxy?");
        proxyPortLabel = new JLabel("Proxy Port:");
        proxyPortField = new JTextField("8080");
        proxyNameLabel = new JLabel("Proxy Host:");
        proxyNameField = new JTextField("proxy");
        useSettingsButton = new JButton("Use these settings.");
        nonProxyHostsLabel = new JLabel("nonProxyHosts:  (seperator: '|')");
        nonProxyHostsText = new JTextField();

        portLabel = new JLabel();
        outputLabel = new JLabel();

        MyGridBagLayout proxyPanelLayout = new MyGridBagLayout(proxyPanel);
        proxyPanel.setLayout(proxyPanelLayout);
        proxyPanel.setBorder(new TitledBorder("Proxy:"));

        proxyPanelLayout.addComponent(useProxyRadioButton,
                                      0,
                                      0,
                                      2,
                                      1,
                                      100,
                                      100,
                                      GridBagConstraints.NORTHWEST,
                                      GridBagConstraints.HORIZONTAL,
                                      new Insets(4, 4, 4, 4));
        proxyPanelLayout.addComponent(proxyNameLabel,
                                      0,
                                      1,
                                      1,
                                      1,
                                      100,
                                      100,
                                      GridBagConstraints.NORTHWEST,
                                      GridBagConstraints.HORIZONTAL,
                                      new Insets(4, 4, 4, 4));
        proxyPanelLayout.addComponent(proxyNameField,
                                      1,
                                      1,
                                      1,
                                      1,
                                      100,
                                      100,
                                      GridBagConstraints.NORTHWEST,
                                      GridBagConstraints.HORIZONTAL,
                                      new Insets(4, 4, 4, 4));
        proxyPanelLayout.addComponent(proxyPortLabel,
                                      0,
                                      2,
                                      1,
                                      1,
                                      100,
                                      100,
                                      GridBagConstraints.NORTHWEST,
                                      GridBagConstraints.HORIZONTAL,
                                      new Insets(4, 4, 4, 4));
        proxyPanelLayout.addComponent(proxyPortField,
                                      1,
                                      2,
                                      1,
                                      1,
                                      100,
                                      100,
                                      GridBagConstraints.NORTHWEST,
                                      GridBagConstraints.HORIZONTAL,
                                      new Insets(4, 4, 4, 4));
        proxyPanelLayout.addComponent(nonProxyHostsLabel,
                                      0,
                                      3,
                                      1,
                                      1,
                                      100,
                                      100,
                                      GridBagConstraints.NORTHWEST,
                                      GridBagConstraints.HORIZONTAL,
                                      new Insets(4, 4, 4, 4));
        proxyPanelLayout.addComponent(nonProxyHostsText,
                                      0,
                                      4,
                                      2,
                                      1,
                                      100,
                                      100,
                                      GridBagConstraints.NORTHWEST,
                                      GridBagConstraints.HORIZONTAL,
                                      new Insets(4, 4, 4, 4));

        useProxyRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (useProxyRadioButton.isSelected()) {
                    proxyPortField.setEnabled(true);
                    proxyNameField.setEnabled(true);
                    nonProxyHostsText.setEditable(true);
                }
                else {
                    proxyPortField.setEnabled(false);
                    proxyNameField.setEnabled(false);
                    nonProxyHostsText.setEditable(false);
                }
            }
        });

        outputLabel.setText("output panel:");

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dispose();
            }
        });

        useSettingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveSettings();
                setVisible(false);
            }
        });

        MyGridBagLayout mainLayout = new MyGridBagLayout(this);
        getContentPane().setLayout(mainLayout);

        mainLayout.addComponent(proxyPanel, 0, 1, 1, 1, 100, 100);
        mainLayout.addComponent(useSettingsButton, 0, 2, 1, 1, 100, 100);

        setLocation(owner.getLocation());
        
        pack();
    }

    /**
     * 
     * @param proxyHost
     * @param proxyPort
     * @param nonProxyHosts
     */
    private void setProxySettings(String proxyHost, String proxyPort, String nonProxyHosts) {
        if (proxyHost != null && proxyPort != null) {
            if ( !proxyHost.equals("") && !proxyPort.equals("")) {
                proxyPortField.setText(proxyPort);
                proxyNameField.setText(proxyHost);

                proxyPortField.setEnabled(true);
                proxyNameField.setEnabled(true);

                useProxyRadioButton.setSelected(true);
            }
        }
        else {
            proxyPortField.setEnabled(false);
            proxyNameField.setEnabled(false);

            useProxyRadioButton.setSelected(false);
        }
        
        if (nonProxyHosts != null) {
            nonProxyHostsText.setText(nonProxyHosts);
        }
        else {
            nonProxyHostsText.setText("");
        }
    }

    public void saveSettings() {

        if (useProxyRadioButton.isSelected()) {
            System.setProperty("http.proxyHost", proxyNameField.getText());
            System.setProperty("http.proxyPort", proxyPortField.getText());
            System.setProperty("http.nonProxyHosts", nonProxyHostsText.getText());
            
            LOGGER.info("setting http.proxyHost: " + proxyNameField.getText());
            LOGGER.info("setting http.proxyPort: " + proxyPortField.getText());
            LOGGER.info("setting http.nonProxyHosts: " + nonProxyHostsText.getText());
        }
        else {
            System.setProperty("http.proxyHost", "");
            System.setProperty("http.proxyPort", "");
            System.setProperty("http.nonProxyHosts", "");
            
            LOGGER.info("setting http.proxyHost: -");
            LOGGER.info("setting http.proxyPort: -");
            LOGGER.info("setting http.nonProxyHosts: -");
        }

        JOptionPane.showMessageDialog(this,
                                      "Settings Saved",
                                      "Information",
                                      JOptionPane.INFORMATION_MESSAGE);

    }

    public static void main(String[] args) {
        new ProxyPreferencesDialog(new JFrame()).setVisible(true);
    }

}