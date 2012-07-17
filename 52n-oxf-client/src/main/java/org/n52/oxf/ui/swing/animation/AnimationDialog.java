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

package org.n52.oxf.ui.swing.animation;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.n52.oxf.render.OverlayEngine;
import org.n52.oxf.ui.swing.AnimatedMapCanvas;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class AnimationDialog extends JDialog {

    private JPanel jContentPane = null;
    private AnimationPanel animationPanel = null;
    
    public AnimationDialog(JFrame owner, AnimatedMapCanvas map) {
        super(owner, "Animation Controller");
        
        animationPanel = new AnimationPanel(map);
        
        setBounds(910, 470, 430, 130);
        setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(animationPanel, java.awt.BorderLayout.CENTER);
        }
        return jContentPane;
    }
    
    public static void main(String[] args) {
        AnimationDialog ad = new AnimationDialog(null, new AnimatedMapCanvas(new OverlayEngine()));
        
        ad.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        ad.setVisible(true);
    }
}