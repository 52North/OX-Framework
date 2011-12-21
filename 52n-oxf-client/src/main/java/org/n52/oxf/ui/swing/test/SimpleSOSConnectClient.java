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
 
 Created on: 01.11.2007
 *********************************************************************************/
package org.n52.oxf.ui.swing.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.n52.oxf.ui.swing.sos.ConnectSOSDialogReduced;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class SimpleSOSConnectClient extends JFrame implements ActionListener {

    private JPanel mainPanel;
    private JButton startButton;
    
    public SimpleSOSConnectClient() {
        super("Test your SOS' operations.");
        
        this.setSize(350, 85);
        this.setLocation(100, 100);
        
        mainPanel = new JPanel();
        startButton = new JButton("start");
        
        startButton.addActionListener(this);
        
        mainPanel.add(startButton);
        
        this.getContentPane().add(mainPanel);
        

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("shut down application.");
                System.exit(0);
            }
        });
    }
    
    public void actionPerformed(ActionEvent e) {
        new ConnectSOSDialogReduced(this).setVisible(true);
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        new SimpleSOSConnectClient().setVisible(true);
    }
}