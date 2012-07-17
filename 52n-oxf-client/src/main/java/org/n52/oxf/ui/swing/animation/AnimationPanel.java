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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.n52.oxf.ui.swing.AnimatedMapCanvas;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class AnimationPanel extends JPanel {

    private static Integer[] framesPerMinuteValues = {10,20,30,40,50,60,70,80,90,100,110,120,130,140,150,160,170,180,190,200,210,220,230,240};
    
    private JButton playButton = null;
    private JButton pauseButton = null;
    private JButton stopButton = null;
    private JButton stepFwdButton = null;
    private JButton stepBwdButton = null;

    private TimeLineSlider timeLineSlider = null;

    private AnimationPanelController controller = null;

    private AnimatedMapCanvas map;
    
    private JComboBox framesPerMinuteComboBox = null;
    
    /**
     * just for the GUI editor.
     */
    public AnimationPanel() {
        super();
        
        initialize();

        controller = new AnimationPanelController(this);
        
        enableButtons(false);
    }

    /**
     * 
     */
    public AnimationPanel(AnimatedMapCanvas map) {
        super();
        this.map = map;
        
        controller = new AnimationPanelController(this);
        
        map.addEventListener(controller);
        
        map.getImageBuilder().addEventListener(controller);
        
        initialize();
        
        enableButtons(false);
        
        initFramesPerMinuteCB(map.getFramesPerMinute());
    }

    private void initFramesPerMinuteCB (int selectedFPM) {
        for (Integer i : framesPerMinuteValues) {
            framesPerMinuteComboBox.addItem(i);
        }
        framesPerMinuteComboBox.setSelectedItem(selectedFPM);
    }
    
    public void enableButtons(boolean enabled) {
        getPlayButton().setEnabled(enabled);
        getPauseButton().setEnabled(enabled);
        getStopButton().setEnabled(enabled);
        getStepFwdButton().setEnabled(enabled);
        getStepBwdButton().setEnabled(enabled);
        getFramesPerMinuteComboBox().setEnabled(enabled);
        getTimeLineSlider().setEnabled(enabled);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
        gridBagConstraints12.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints12.gridy = 0;
        gridBagConstraints12.weightx = 0.0D;
        gridBagConstraints12.weighty = 0.0D;
        gridBagConstraints12.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraints12.gridx = 6;
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 7;
        gridBagConstraints11.gridwidth = 1;
        gridBagConstraints11.weightx = 10.0D;
        gridBagConstraints11.weighty = 0.0D;
        gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints11.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraints11.gridheight = 1;
        gridBagConstraints11.gridy = 0;
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 5;
        gridBagConstraints4.weightx = 0.0D;
        gridBagConstraints4.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraints4.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints4.weighty = 0.0D;
        gridBagConstraints4.gridy = 0;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 4;
        gridBagConstraints3.weightx = 0.0D;
        gridBagConstraints3.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraints3.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints3.weighty = 0.0D;
        gridBagConstraints3.gridy = 0;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 3;
        gridBagConstraints2.weightx = 0.0D;
        gridBagConstraints2.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraints2.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints2.weighty = 0.0D;
        gridBagConstraints2.gridy = 0;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.weightx = 0.0D;
        gridBagConstraints1.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraints1.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints1.weighty = 0.0D;
        gridBagConstraints1.gridy = 0;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 0.0D;
        gridBagConstraints.weightx = 0.0D;
        gridBagConstraints.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(436, 28);
        this.setPreferredSize(new java.awt.Dimension(20,25));
        this.add(getPlayButton(), gridBagConstraints);
        this.add(getPauseButton(), gridBagConstraints1);
        this.add(getStopButton(), gridBagConstraints2);
        this.add(getStepFwdButton(), gridBagConstraints3);
        this.add(getStepBwdButton(), gridBagConstraints4);
        this.add(getTimeLineSlider(), gridBagConstraints11);
        this.add(getFramesPerMinuteComboBox(), gridBagConstraints12);
    }

    /**
     * This method initializes playButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getPlayButton() {
        if (playButton == null) {
            playButton = new JButton();
            playButton.setIcon(new ImageIcon(getClass().getResource("/org/n52/oxf/ui/swing/icons/play_animation.gif")));
            playButton.setPreferredSize(new java.awt.Dimension(20,20));
            //playButton.setText("play");
            playButton.setEnabled(true);
            playButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_playButton();
                }
            });
        }
        return playButton;
    }

    /**
     * This method initializes pauseButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getPauseButton() {
        if (pauseButton == null) {
            pauseButton = new JButton();
            pauseButton.setIcon(new ImageIcon(getClass().getResource("/org/n52/oxf/ui/swing/icons/pause_animation.gif")));
            pauseButton.setPreferredSize(new java.awt.Dimension(20,20));
            //pauseButton.setText("pause");
            pauseButton.setEnabled(true);
            pauseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_pauseButton();
                }
            });
        }
        return pauseButton;
    }

    /**
     * This method initializes stopButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getStopButton() {
        if (stopButton == null) {
            stopButton = new JButton();
            stopButton.setIcon(new ImageIcon(getClass().getResource("/org/n52/oxf/ui/swing/icons/stop_animation.gif")));
            stopButton.setPreferredSize(new java.awt.Dimension(20,20));
            //stopButton.setText("stop");
            stopButton.setEnabled(true);
            stopButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_stopButton();
                }
            });
        }
        return stopButton;
    }

    /**
     * This method initializes stepFwdButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getStepFwdButton() {
        if (stepFwdButton == null) {
            stepFwdButton = new JButton();
            stepFwdButton.setIcon(new ImageIcon(getClass().getResource("/org/n52/oxf/ui/swing/icons/step_forward_animation.gif")));
            stepFwdButton.setPreferredSize(new java.awt.Dimension(20,20));
            stepFwdButton.setEnabled(true);
            stepFwdButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_stepFwdButton();
                }
            });
        }
        return stepFwdButton;
    }

    /**
     * This method initializes stepBwdButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getStepBwdButton() {
        if (stepBwdButton == null) {
            stepBwdButton = new JButton();
            stepBwdButton.setIcon(new ImageIcon(getClass().getResource("/org/n52/oxf/ui/swing/icons/step_backward_animation.gif")));
            stepBwdButton.setPreferredSize(new java.awt.Dimension(20,20));
            stepBwdButton.setEnabled(true);
            stepBwdButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_stepBwdButton();
                }
            });
        }
        return stepBwdButton;
    }

    /**
     * This method initializes timeLineSlider
     * 
     * @return org.n52.oxf.ui.swing.sos.TimeLineCanvas
     */
    private TimeLineSlider getTimeLineSlider() {
        if (timeLineSlider == null) {
            timeLineSlider = new TimeLineSlider(map);
            timeLineSlider.setPreferredSize(new java.awt.Dimension(20,20));
        }
        return timeLineSlider;
    }

    public AnimatedMapCanvas getMap() {
        return map;
    }

    /**
     * This method initializes framesPerMinuteComboBox	
     * 	
     * @return javax.swing.JComboBox	
     */
    protected JComboBox getFramesPerMinuteComboBox() {
        if (framesPerMinuteComboBox == null) {
            framesPerMinuteComboBox = new JComboBox();
            framesPerMinuteComboBox.setToolTipText("Specifiy Frames per Minute");
            framesPerMinuteComboBox.setPreferredSize(new java.awt.Dimension(50,20));
            framesPerMinuteComboBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_fpmComboBox();
                }
            });
        }
        return framesPerMinuteComboBox;
    }

}  //  @jve:decl-index=0:visual-constraint="10,73"