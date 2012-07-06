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

import java.awt.*;
import java.util.*;

import javax.swing.*;
import org.n52.oxf.valueDomains.time.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class TimePositionPanel extends TimePanel {

    private JLabel beginLabel = null;
    private JTextField secondTF = null;
    private JTextField minuteTF = null;
    private JTextField hourTF = null;
    private JTextField dayTF = null;
    private JTextField monthTF = null;
    private JTextField yearTF = null;
    private JLabel secondLabel = null;
    private JLabel minute = null;
    private JLabel hour = null;
    private JLabel day = null;
    private JLabel month = null;
    private JLabel year = null;
    private JLabel d1Label = null;
    private JLabel d2Label = null;
    private JLabel d3Label = null;
    /**
     * This method initializes secondTF	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getSecondTF() {
        if (secondTF == null) {
            secondTF = new JTextField();
            secondTF.setPreferredSize(new java.awt.Dimension(40,20));
        }
        return secondTF;
    }

    /**
     * This method initializes minuteTF	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getMinuteTF() {
        if (minuteTF == null) {
            minuteTF = new JTextField();
            minuteTF.setPreferredSize(new java.awt.Dimension(40,20));
        }
        return minuteTF;
    }

    /**
     * This method initializes hourTF	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getHourTF() {
        if (hourTF == null) {
            hourTF = new JTextField();
            hourTF.setPreferredSize(new java.awt.Dimension(40,20));
        }
        return hourTF;
    }

    /**
     * This method initializes dayTF	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getDayTF() {
        if (dayTF == null) {
            dayTF = new JTextField();
            dayTF.setPreferredSize(new java.awt.Dimension(40,20));
        }
        return dayTF;
    }

    /**
     * This method initializes monthTF	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getMonthTF() {
        if (monthTF == null) {
            monthTF = new JTextField();
            monthTF.setPreferredSize(new java.awt.Dimension(40,20));
        }
        return monthTF;
    }

    /**
     * This method initializes yearTF	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getYearTF() {
        if (yearTF == null) {
            yearTF = new JTextField();
            yearTF.setPreferredSize(new java.awt.Dimension(40,20));
        }
        return yearTF;
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
    public TimePositionPanel(ITimePosition timePosition) {
        super(timePosition);
        initialize();
        
        initTimeValues(timePosition);
    }
    
    private void initTimeValues(ITimePosition timePosition) {
        
        yearTF.setText("" + timePosition.getYear());
        monthTF.setText("" + timePosition.getMonth());
        dayTF.setText("" + timePosition.getDay());
        
        hourTF.setText("" + timePosition.getHour());
        minuteTF.setText("" + timePosition.getMinute());
        secondTF.setText("" + timePosition.getSecond());
    }
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
        gridBagConstraints16.gridx = 8;
        gridBagConstraints16.gridy = 1;
        d3Label = new JLabel();
        d3Label.setText("-");
        GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
        gridBagConstraints15.gridx = 10;
        gridBagConstraints15.gridy = 1;
        d2Label = new JLabel();
        d2Label.setText(":");
        GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
        gridBagConstraints14.gridx = 13;
        gridBagConstraints14.gridy = 1;
        d1Label = new JLabel();
        d1Label.setText(":");
        GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
        gridBagConstraints13.gridx = 5;
        gridBagConstraints13.gridy = 0;
        year = new JLabel();
        year.setText("year");
        GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
        gridBagConstraints12.gridx = 2;
        gridBagConstraints12.gridy = 0;
        month = new JLabel();
        month.setText("month");
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 1;
        gridBagConstraints11.gridy = 0;
        day = new JLabel();
        day.setText("day");
        GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
        gridBagConstraints10.gridx = 9;
        gridBagConstraints10.gridy = 0;
        hour = new JLabel();
        hour.setText("hour");
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        gridBagConstraints9.gridx = 12;
        gridBagConstraints9.gridy = 0;
        minute = new JLabel();
        minute.setText("minute");
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.gridx = 15;
        gridBagConstraints8.gridy = 0;
        secondLabel = new JLabel();
        secondLabel.setText("second");
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints7.gridy = 1;
        gridBagConstraints7.gridx = 5;
        gridBagConstraints7.weightx = 100.0D;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints6.gridy = 1;
        gridBagConstraints6.gridx = 2;
        gridBagConstraints6.weightx = 100.0D;
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints5.gridy = 1;
        gridBagConstraints5.gridx = 1;
        gridBagConstraints5.weightx = 100.0D;
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints4.gridy = 1;
        gridBagConstraints4.gridx = 9;
        gridBagConstraints4.weightx = 100.0D;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.gridx = 12;
        gridBagConstraints3.weightx = 100.0D;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.weightx = 100.0D;
        gridBagConstraints2.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraints2.gridx = 15;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        this.add(getSecondTF(), gridBagConstraints2);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weightx = 10.0D;
        gridBagConstraints.insets = new java.awt.Insets(5,5,5,5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.gridy = 1;
        beginLabel = new JLabel();
        beginLabel.setText("Time Position:");
        this.setLayout(new GridBagLayout());
        this.setSize(350, 42);
        this.add(beginLabel, gridBagConstraints);
        this.add(getMinuteTF(), gridBagConstraints3);
        this.add(secondLabel, gridBagConstraints8);
        this.add(minute, gridBagConstraints9);
        this.add(d1Label, gridBagConstraints14);
        this.add(getHourTF(), gridBagConstraints4);
        this.add(getDayTF(), gridBagConstraints5);
        this.add(getMonthTF(), gridBagConstraints6);
        this.add(getYearTF(), gridBagConstraints7);
        this.add(hour, gridBagConstraints10);
        this.add(day, gridBagConstraints11);
        this.add(month, gridBagConstraints12);
        this.add(year, gridBagConstraints13);
        this.add(d2Label, gridBagConstraints15);
        this.add(d3Label, gridBagConstraints16);
    }

    /**
     * Valid example time strings:
     * <li>2005-11-01</li>
     * <li>2005-11-01T12:30</li>
     * <li>2005-11-01T12:30:20Z</li>
     */
    public TimePosition getChosenTime() {

        // the difference of the local timezone to UTC in hours: 
        int timeZoneDiffToUTC = TimeZone.getDefault().getRawOffset() / 3600000;
        String systemTimeZone = "";
        if (timeZoneDiffToUTC >= 0) {
            systemTimeZone += "+";
        }
        else {
            systemTimeZone += "-";
        }
        if (Math.abs(timeZoneDiffToUTC) < 10) {
            systemTimeZone += "0";
        }
        systemTimeZone += Math.abs(timeZoneDiffToUTC);
        
        String second = (secondTF.getText().equals("")) ? "00" : secondTF.getText();
        String minute = (minuteTF.getText().equals("")) ? "00" : minuteTF.getText();
        String hour = (hourTF.getText().equals("")) ? "00" : hourTF.getText();
        String day = dayTF.getText();
        String month = monthTF.getText();
        String year = yearTF.getText();

        String timePos = year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":" + second + systemTimeZone;
        
        return new TimePosition(timePos);
    }

    @Override
    public void clear() {
        secondTF.setText("");
        minuteTF.setText("");
        hourTF.setText("");
        dayTF.setText("");
        monthTF.setText("");
        yearTF.setText("");
    }
    
}  //  @jve:decl-index=0:visual-constraint="-41,11"