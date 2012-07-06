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
package org.n52.oxf.ui.swing.ses;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.tree.ContentTree;
import java.awt.Point;

public class SesGUI extends JDialog {
	
	private SesGUIController controller;
    private MapCanvas map = null;
    private ContentTree tree = null;
    private Component owner = null;
    private String consumer = "http://localhost:8082/SESMuseConsumer/services/consumer";
    
    private int hight = 550;
    private int width = 600;

	private JDialog jDialog = null;  //  @jve:decl-index=0:visual-constraint="56,47"
	private JPanel jContentPane = null;
	private JPanel jPanelService = null;
	private JPanel jPanelSubscription = null;
	private JLabel jLabelService = null;
	private JComboBox jComboBoxService = null;
	private JButton jButtonGetCapabilities = null;
	private JLabel jLabel1Topic = null;
	private JTextField jTextFieldTopic = null;
	private JLabel jLabelFilterType = null;
	private JComboBox jComboBoxFilterType = null;
	private JLabel jLabelFilter = null;
	private JTextArea jTextAreaFilter = null;
	private JButton jButtonSubscribe = null;
	private JButton jButtonUnsubscribe = null;
	private JLabel jLabelConsumerRef = null;
	private JTextField jTextFieldConsumerRef = null;
	private JPanel jPanelUnsubscribe = null;
	private JLabel jLabelResource = null;
	private JTextField jTextFieldResource = null;
	public SesGUI(JFrame owner, MapCanvas map, ContentTree tree) {
        this.owner = owner;
        this.map = map;
        this.tree = tree;
        
        getJDialog();
        controller = new SesGUIController(this, map, tree, owner);
	}

	/**
	 * This method initializes jDialog	
	 * 	
	 * @return javax.swing.JDialog	
	 */
	private JDialog getJDialog() {
		if (jDialog == null) {
			owner.getX();
			jDialog = new JDialog();
			jDialog.setSize(new Dimension(600, 610));
			jDialog.setTitle("SES");
			jDialog.setResizable(false);
			
			//Center dialog
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension size = jDialog.getSize();
			screenSize.height = screenSize.height/2;
			screenSize.width = screenSize.width/2;
			size.height = size.height/2;
			size.width = size.width/2;
			int y = screenSize.height - size.height;
			int x = screenSize.width - size.width;
			
			jDialog.setLocation(new Point(x, y));
			jDialog.setContentPane(getJContentPane());
			jDialog.setVisible(true);
		}
		return jDialog;
	}

	/**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJPanelService(), null);
			jContentPane.add(getJPanelSubscription(), null);
			jContentPane.add(getJPanelUnsubscribe(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanelService	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelService() {
		if (jPanelService == null) {
			jLabelConsumerRef = new JLabel();
			jLabelConsumerRef.setBounds(new Rectangle(10, 44, 125, 17));
			jLabelConsumerRef.setText("Consumer Reference:");
			jLabelService = new JLabel();
			jLabelService.setBounds(new Rectangle(10, 20, 126, 20));
			jLabelService.setText("Service to connect:");
			jPanelService = new JPanel();
			jPanelService.setLayout(null);
			jPanelService.setBounds(new Rectangle(10, 13, 580, 106));
			jPanelService.setBorder(BorderFactory.createTitledBorder(null, "Service", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanelService.add(jLabelService, null);
			jPanelService.add(getJComboBoxService(), null);
			jPanelService.add(getJButtonGetCapabilities(), null);
			jPanelService.add(jLabelConsumerRef, null);
			jPanelService.add(getJTextFieldConsumerRef(), null);
		}
		return jPanelService;
	}

	/**
	 * This method initializes jPanelSubscription	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSubscription() {
		if (jPanelSubscription == null) {
			jLabelFilter = new JLabel();
			jLabelFilter.setBounds(new Rectangle(32, 216, 39, 20));
			jLabelFilter.setText("Filter:");
			jLabelFilterType = new JLabel();
			jLabelFilterType.setBounds(new Rectangle(14, 50, 100, 20));
			jLabelFilterType.setText("Filter Type:");
			jLabel1Topic = new JLabel();
			jLabel1Topic.setBounds(new Rectangle(13, 24, 86, 20));
			jLabel1Topic.setText("Topic:");
			jPanelSubscription = new JPanel();
			jPanelSubscription.setLayout(null);
			jPanelSubscription.setBounds(new Rectangle(10, 125, 580, 394));
			jPanelSubscription.setBorder(BorderFactory.createTitledBorder(null, "Subscribe", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanelSubscription.add(jLabel1Topic, null);
			jPanelSubscription.add(getJTextFieldTopic(), null);
			jPanelSubscription.add(jLabelFilterType, null);
			jPanelSubscription.add(getJComboBoxFilterType(), null);
			jPanelSubscription.add(jLabelFilter, null);
			jPanelSubscription.add(getJTextAreaFilter(), null);
			jPanelSubscription.add(getJButtonSubscribe(), null);
		}
		return jPanelSubscription;
	}

	/**
	 * This method initializes jComboBoxService	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getJComboBoxService() {
		if (jComboBoxService == null) {
			jComboBoxService = new JComboBox();
			jComboBoxService.setBounds(new Rectangle(140, 20, 428, 20));
			jComboBoxService.setEditable(true);
		}
		return jComboBoxService;
	}

	/**
	 * This method initializes jButtonGetCapabilities	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonGetCapabilities() {
		if (jButtonGetCapabilities == null) {
			jButtonGetCapabilities = new JButton();
			jButtonGetCapabilities.setBounds(new Rectangle(210, 70, 222, 25));
			jButtonGetCapabilities.setText("GetCapabilities");
			jButtonGetCapabilities.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_GetCapabilitiesButton();
                }
            });
		}
		return jButtonGetCapabilities;
	}

	/**
	 * This method initializes jTextFieldTopic	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getJTextFieldTopic() {
		if (jTextFieldTopic == null) {
			jTextFieldTopic = new JTextField();
			jTextFieldTopic.setBounds(new Rectangle(140, 24, 200, 20));
		}
		return jTextFieldTopic;
	}

	/**
	 * This method initializes jComboBoxFilterType	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getJComboBoxFilterType() {
		if (jComboBoxFilterType == null) {
			String[] filters = new String[]{"Filter Level 1", "Filter Level 2", "Filter Level 3"};
			jComboBoxFilterType = new JComboBox(filters);
			jComboBoxFilterType.setBounds(new Rectangle(140, 53, 200, 20));
		}
		return jComboBoxFilterType;
	}

	/**
	 * This method initializes jTextAreaFilter	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	public JTextArea getJTextAreaFilter() {
		if (jTextAreaFilter == null) {
			jTextAreaFilter = new JTextArea();
			jTextAreaFilter.setBounds(new Rectangle(140, 85, 429, 264));
			jTextAreaFilter.setText("*");
		}
		return jTextAreaFilter;
	}

	/**
	 * This method initializes jButtonSubscribe	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSubscribe() {
		if (jButtonSubscribe == null) {
			jButtonSubscribe = new JButton();
			jButtonSubscribe.setBounds(new Rectangle(418, 362, 150, 25));
			jButtonSubscribe.setText("Subscribe");
			jButtonSubscribe.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_SubscribeButton();
                }
            });
		}
		return jButtonSubscribe;
	}

	/**
	 * This method initializes jButtonUnsubscribe	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonUnsubscribe() {
		if (jButtonUnsubscribe == null) {
			jButtonUnsubscribe = new JButton();
			jButtonUnsubscribe.setText("UnSubscribe");
			jButtonUnsubscribe.setBounds(new Rectangle(418, 18, 150, 25));
			jButtonUnsubscribe.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_UnsubscribeButton();
                }
			});
		}
		return jButtonUnsubscribe;
	}

	/**
	 * This method initializes jTextFieldConsumerRef	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getJTextFieldConsumerRef() {
		if (jTextFieldConsumerRef == null) {
			jTextFieldConsumerRef = new JTextField();
			jTextFieldConsumerRef.setBounds(new Rectangle(140, 44, 428, 20));
			jTextFieldConsumerRef.setText(consumer);
		}
		return jTextFieldConsumerRef;
	}

	/**
	 * This method initializes jPanelUnsubscribe	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelUnsubscribe() {
		if (jPanelUnsubscribe == null) {
			jLabelResource = new JLabel();
			jLabelResource.setBounds(new Rectangle(14, 22, 118, 20));
			jLabelResource.setText("Muse-Resource");
			jPanelUnsubscribe = new JPanel();
			jPanelUnsubscribe.setLayout(null);
			jPanelUnsubscribe.setBounds(new Rectangle(10, 525, 580, 53));
			jPanelUnsubscribe.setBorder(BorderFactory.createTitledBorder(null, "UnSubscribe", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanelUnsubscribe.add(getJButtonUnsubscribe(), null);
			jPanelUnsubscribe.add(jLabelResource, null);
			jPanelUnsubscribe.add(getJTextFieldResource(), null);
		}
		return jPanelUnsubscribe;
	}

	/**
	 * This method initializes jTextFieldResource	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getJTextFieldResource() {
		if (jTextFieldResource == null) {
			jTextFieldResource = new JTextField();
			jTextFieldResource.setBounds(new Rectangle(140, 22, 95, 20));
			jTextFieldResource.setText("2");
		}
		return jTextFieldResource;
	}
}
