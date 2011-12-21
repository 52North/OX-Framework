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
 
 Created on: 30.08.2005
 *********************************************************************************/

package org.n52.oxf.ui.swing;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import org.apache.log4j.*;
import org.n52.oxf.util.*;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class MessageConsole extends JDialog implements ActionListener {

    private static Logger LOGGER = LoggingHandler.getLogger(MessageConsole.class);
    
    private JScrollPane scrollPane;
    private MessagePane messagePane;

    private JMenuBar menuBar;
    
    private JMenu fileMenu;
    private JMenuItem closeMI;
    private JMenuItem saveMI;
    
    private JMenu viewMenu;
    private JMenuItem clearMI;
    
    private JRadioButtonMenuItem allBMI;
    private JRadioButtonMenuItem infoBMI;
    private JRadioButtonMenuItem warnBMI;
    private JRadioButtonMenuItem errorBMI;
    private JRadioButtonMenuItem fatalBMI;
    private ButtonGroup levelGroup;
    
    private JCheckBoxMenuItem divertSysOut;
    private JCheckBoxMenuItem divertSysErr;
    
    /**
     * 
     */
    public MessageConsole(JFrame owner) {
        super(owner, "Message Console");
        
        messagePane = new MessagePane();
        scrollPane  = new JScrollPane(messagePane);
        add(scrollPane, BorderLayout.CENTER);
        
        
        fileMenu    = new JMenu("File");
        closeMI     = new JMenuItem("Close");
        saveMI      = new JMenuItem("Save as...");
        
        fileMenu.add(closeMI);
        fileMenu.add(saveMI);
        
        closeMI.addActionListener(this);
        saveMI.addActionListener(this);
        
        
        viewMenu    = new JMenu("View");
        clearMI     = new JMenuItem("Clear");
        allBMI      = new JRadioButtonMenuItem("ALL", true);
        infoBMI     = new JRadioButtonMenuItem("INFO");
        warnBMI     = new JRadioButtonMenuItem("WARN");
        errorBMI    = new JRadioButtonMenuItem("ERROR");
        fatalBMI    = new JRadioButtonMenuItem("FATAL");
        levelGroup  = new ButtonGroup();
        divertSysOut= new JCheckBoxMenuItem("Divert System.out", false);
        divertSysErr= new JCheckBoxMenuItem("Divert System.err", false);
        
        levelGroup.add(allBMI);
        levelGroup.add(infoBMI);
        levelGroup.add(warnBMI);
        levelGroup.add(errorBMI);
        levelGroup.add(fatalBMI);
        
        viewMenu.add(clearMI);
        viewMenu.addSeparator();
        viewMenu.add(allBMI);
        viewMenu.add(infoBMI);
        viewMenu.add(warnBMI);
        viewMenu.add(errorBMI);
        viewMenu.add(fatalBMI);
        viewMenu.addSeparator();
        viewMenu.add(divertSysOut);
        viewMenu.add(divertSysErr);
        
        clearMI.addActionListener(this);
        
        allBMI.addActionListener(this);
        infoBMI.addActionListener(this);
        warnBMI.addActionListener(this);
        errorBMI.addActionListener(this);
        fatalBMI.addActionListener(this);
        
        divertSysOut.addActionListener(this);
        divertSysErr.addActionListener(this);
        
        menuBar     = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        
        setJMenuBar(menuBar);
        
        setBounds(910, 100, 430, 360);
        
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                dispose();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(closeMI)){
            dispose();
        }
        else if(e.getSource().equals(clearMI)){
            messagePane.setText("");
        }
        else if(e.getSource().equals(saveMI)){
            JFileChooser chooser = new JFileChooser();
            MyFileFilter filter = new MyFileFilter("log", "'log' files");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
               try {
                   IOHelper.saveFile(chooser.getSelectedFile().getAbsolutePath(), messagePane.getText(), true);
                   messagePane.setText("");
                   LOGGER.info("log file saved as: " + chooser.getSelectedFile().getAbsolutePath());
                }
                catch (IOException exc) {
                    LOGGER.error(exc, exc);
                    exc.printStackTrace();
                }
            }
        }
        
        else if(e.getSource().equals(allBMI)){
            messagePane.setText("");
            LoggingHandler.setLevel(Level.ALL);
        }
        else if(e.getSource().equals(infoBMI)){
            messagePane.setText("");
            LoggingHandler.setLevel(Level.INFO);
        }
        else if(e.getSource().equals(warnBMI)){
            messagePane.setText("");
            LoggingHandler.setLevel(Level.WARN);
        }
        else if(e.getSource().equals(errorBMI)){
            messagePane.setText("");
            LoggingHandler.setLevel(Level.ERROR);
        }
        else if(e.getSource().equals(fatalBMI)){
            messagePane.setText("");
            LoggingHandler.setLevel(Level.FATAL);
        }
        
        else if(e.getSource().equals(divertSysOut)){
            if(divertSysOut.isSelected()){
                LoggingHandler.divertSystemOut(true);               
            } else {
                LoggingHandler.divertSystemOut(false); 
            }
        }
        else if(e.getSource().equals(divertSysErr)){
            if(divertSysErr.isSelected()){
                LoggingHandler.divertSystemErr(true);               
            } else {
                LoggingHandler.divertSystemErr(false); 
            }
        }
    }
}


/**
 * 
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
class MessagePane extends JTextArea implements IEventListener {

    /**
     * 
     */
    public MessagePane() {
        super();

        LoggingHandler.getOutputStream().addEventListener(this);
    }

    public void eventCaught(OXFEvent evt) throws OXFEventException {
        if (evt.getName().equals(EventName.LOG_MESSAGE)) {
            append( ((StringBuffer)evt.getValue()).toString() );
        }
    }

}