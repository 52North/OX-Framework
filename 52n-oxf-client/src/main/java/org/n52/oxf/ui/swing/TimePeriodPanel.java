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
 
 Created on: 09.01.2006
 *********************************************************************************/

package org.n52.oxf.ui.swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.n52.oxf.valueDomains.time.ITimePeriod;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.n52.oxf.valueDomains.time.TimePeriod;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class TimePeriodPanel extends TimePanel {

    private JLabel endLabel = null;
    private JTextField beginTimeZoneTF = null;
    private JTextField beginSecondTF = null;
    private JTextField beginMinuteTF = null;
    private JTextField beginHourTF = null;
    private JTextField beginDayTF = null;
    private JTextField beginMonthTF = null;
    private JTextField beginYearTF = null;
    private JLabel timeZoneLabel = null;
    private JLabel secondLabel = null;
    private JLabel minute = null;
    private JLabel hour = null;
    private JLabel day = null;
    private JLabel month = null;
    private JLabel year = null;
    private JLabel d1Label = null;
    private JLabel d2Label = null;
    private JLabel d3Label = null;
    private JTextField endYearTF = null;
    private JTextField endMonthTF = null;
    private JTextField endDayTF = null;
    private JLabel jLabel = null;
    private JTextField endHourTF = null;
    private JTextField endMinuteTF = null;
    private JTextField endSecondTF = null;
    private JTextField endTimeZoneTF = null;
    private JLabel jLabel1 = null;
    private JLabel jLabel2 = null;
    private JLabel beginLabel = null;
    private JLabel beginLabel1 = null;
    /**
     * This method initializes timeZoneTF
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getTimeZoneTF() {
        if (beginTimeZoneTF == null) {
            beginTimeZoneTF = new JTextField();
            beginTimeZoneTF.setPreferredSize(new java.awt.Dimension(40, 20));
        }
        return beginTimeZoneTF;
    }
    /**
     * This method initializes secondTF
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getSecondTF() {
    	if (beginSecondTF == null) {
    		beginSecondTF = new JTextField();
    		beginSecondTF.setPreferredSize(new java.awt.Dimension(40, 20));
    	}
    	return beginSecondTF;
    }

    /**
     * This method initializes minuteTF
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getMinuteTF() {
        if (beginMinuteTF == null) {
            beginMinuteTF = new JTextField();
            beginMinuteTF.setPreferredSize(new java.awt.Dimension(40, 20));
        }
        return beginMinuteTF;
    }

    /**
     * This method initializes hourTF
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getHourTF() {
        if (beginHourTF == null) {
            beginHourTF = new JTextField();
            beginHourTF.setPreferredSize(new java.awt.Dimension(40, 20));
        }
        return beginHourTF;
    }

    /**
     * This method initializes dayTF
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getDayTF() {
        if (beginDayTF == null) {
            beginDayTF = new JTextField();
            beginDayTF.setPreferredSize(new java.awt.Dimension(40, 20));
        }
        return beginDayTF;
    }

    /**
     * This method initializes monthTF
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getMonthTF() {
        if (beginMonthTF == null) {
            beginMonthTF = new JTextField();
            beginMonthTF.setPreferredSize(new java.awt.Dimension(40, 20));
        }
        return beginMonthTF;
    }

    /**
     * This method initializes yearTF
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getYearTF() {
        if (beginYearTF == null) {
            beginYearTF = new JTextField();
            beginYearTF.setPreferredSize(new java.awt.Dimension(40, 20));
        }
        return beginYearTF;
    }

    /**
     * This method initializes jTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getJTextField() {
        if (endYearTF == null) {
            endYearTF = new JTextField();
            endYearTF.setPreferredSize(new java.awt.Dimension(40, 20));
        }
        return endYearTF;
    }

    /**
     * This method initializes jTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getJTextField2() {
        if (endMonthTF == null) {
            endMonthTF = new JTextField();
            endMonthTF.setPreferredSize(new Dimension(40, 20));
        }
        return endMonthTF;
    }

    /**
     * This method initializes jTextField1
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getJTextField1() {
        if (endDayTF == null) {
            endDayTF = new JTextField();
            endDayTF.setPreferredSize(new Dimension(40, 20));
        }
        return endDayTF;
    }

    /**
     * This method initializes jTextField2
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getJTextField22() {
        if (endHourTF == null) {
            endHourTF = new JTextField();
            endHourTF.setPreferredSize(new Dimension(40, 20));
        }
        return endHourTF;
    }

    /**
     * This method initializes jTextField3
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getJTextField3() {
        if (endMinuteTF == null) {
            endMinuteTF = new JTextField();
            endMinuteTF.setPreferredSize(new Dimension(40, 20));
        }
        return endMinuteTF;
    }

    /**
     * This method initializes jTextField4
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getJTextField4() {
        if (endSecondTF == null) {
            endSecondTF = new JTextField();
            endSecondTF.setPreferredSize(new Dimension(40, 20));
        }
        return endSecondTF;
    }
    
    /**
     * This method initializes jTextField4
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getEndTimeZoneTF() {
    	if (endTimeZoneTF == null) {
    		endTimeZoneTF = new JTextField();
    		endTimeZoneTF.setPreferredSize(new Dimension(40, 20));
    	}
    	return endTimeZoneTF;
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
    public TimePeriodPanel(ITimePeriod timePeriod) {
        super(timePeriod);
        initialize();
        
        initTimeValues(timePeriod);
    }
    
    private void initTimeValues(ITimePeriod timePeriod) {
        
        ITimePosition beginPos = timePeriod.getStart();
        ITimePosition endPos = timePeriod.getEnd();
        
        setBeginPosition(beginPos);
        setEndPosition(endPos);
    }
    
    public void setBeginPosition(ITimePosition beginPos) {
        beginYearTF.setText("" + beginPos.getYear());
        beginMonthTF.setText("" + beginPos.getMonth());
        beginDayTF.setText("" + beginPos.getDay());
        beginHourTF.setText("" + beginPos.getHour());
        beginMinuteTF.setText("" + beginPos.getMinute());
        beginSecondTF.setText("" + beginPos.getSecond());
        beginTimeZoneTF.setText(beginPos.getTimezone());
    }
    
    public void setEndPosition(ITimePosition endPos) {
        endYearTF.setText("" + endPos.getYear());
        endMonthTF.setText("" + endPos.getMonth());
        endDayTF.setText("" + endPos.getDay());
        endHourTF.setText("" + endPos.getHour());
        endMinuteTF.setText("" + endPos.getMinute());
        endSecondTF.setText("" + endPos.getSecond());
        endTimeZoneTF.setText(endPos.getTimezone());
    }
    
  /**
     * This method initializes this
     * 
     * @return voi			beginLabel1.setText("JLabel");
			beginLabel.setText("Begin Position:");
			gridBagConstraints.insets = new java.awt.Insets(5,5,5,5);
			this.add(beginLabel, gridBagConstraints);
d
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
        gridBagConstraints16.gridx = 6;
        gridBagConstraints16.gridy = 1;
        GridBagConstraints gridBagConstraints91 = new GridBagConstraints();
        gridBagConstraints91.gridx = 10;
        gridBagConstraints91.gridy = 2;
        jLabel2 = new JLabel();
        jLabel2.setText(":");
        GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
        gridBagConstraints81.gridx = 8;
        gridBagConstraints81.gridy = 2;
        jLabel1 = new JLabel();
        jLabel1.setText(":");
        GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
        gridBagConstraints71.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints71.gridy = 2;
        gridBagConstraints71.weightx = 1.0;
        gridBagConstraints71.gridx = 12;
        GridBagConstraints gridBagConstraintsTZ2 = new GridBagConstraints();
        gridBagConstraintsTZ2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraintsTZ2.gridy = 2;
        gridBagConstraintsTZ2.weightx = 1.0;
        gridBagConstraintsTZ2.gridx = 13;
        GridBagConstraints gridBagConstraintsTZ1 = new GridBagConstraints();
        gridBagConstraintsTZ1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraintsTZ1.gridy = 1;
        gridBagConstraintsTZ1.weightx = 1.0;
        gridBagConstraintsTZ1.gridx = 13;
        GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
        gridBagConstraints61.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints61.gridy = 2;
        gridBagConstraints61.weightx = 1.0;
        gridBagConstraints61.gridx = 9;
        GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
        gridBagConstraints51.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints51.gridy = 2;
        gridBagConstraints51.weightx = 1.0;
        gridBagConstraints51.gridx = 7;
        GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
        gridBagConstraints41.gridx = 6;
        gridBagConstraints41.gridy = 2;
        jLabel = new JLabel();
        jLabel.setText("-");
        GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
        gridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints31.gridy = 2;
        gridBagConstraints31.weightx = 1.0;
        gridBagConstraints31.gridx = 1;
        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints21.gridy = 2;
        gridBagConstraints21.weightx = 1.0;
        gridBagConstraints21.gridx = 2;
        GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
        gridBagConstraints17.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints17.gridy = 2;
        gridBagConstraints17.weightx = 1.0;
        gridBagConstraints17.gridx = 3;
        d3Label = new JLabel();
        d3Label.setText("-");
        GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
        gridBagConstraints15.gridx = 8;
        gridBagConstraints15.gridy = 1;
        d2Label = new JLabel();
        d2Label.setText(":");
        GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
        gridBagConstraints14.gridx = 10;
        gridBagConstraints14.gridy = 1;
        d1Label = new JLabel();
        d1Label.setText(":");
        GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
        gridBagConstraints13.gridx = 3;
        gridBagConstraints13.weightx = 100.0D;
        gridBagConstraints13.gridy = 0;
        year = new JLabel();
        year.setText("year");
        GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
        gridBagConstraints12.gridx = 2;
        gridBagConstraints12.weightx = 100.0D;
        gridBagConstraints12.gridy = 0;
        month = new JLabel();
        month.setText("month");
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 1;
        gridBagConstraints11.weightx = 100.0D;
        gridBagConstraints11.gridy = 0;
        day = new JLabel();
        day.setText("day");
        GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
        gridBagConstraints10.gridx = 7;
        gridBagConstraints10.weightx = 100.0D;
        gridBagConstraints10.gridy = 0;
        hour = new JLabel();
        hour.setText("hour");
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        gridBagConstraints9.gridx = 9;
        gridBagConstraints9.weightx = 100.0D;
        gridBagConstraints9.gridy = 0;
        minute = new JLabel();
        minute.setText("minute");
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.gridx = 11;
        gridBagConstraints8.weightx = 100.0D;
        gridBagConstraints8.gridy = 0;
        secondLabel = new JLabel();
        secondLabel.setText("second");
        GridBagConstraints gridBagConstraintsTZLbl = new GridBagConstraints();
        gridBagConstraints8.gridx = 12;
        gridBagConstraints8.weightx = 100.0D;
        gridBagConstraints8.gridy = 0;
        timeZoneLabel = new JLabel();
        timeZoneLabel.setText("Timezone");
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints7.gridy = 1;
        gridBagConstraints7.gridx = 3;
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
        gridBagConstraints4.gridx = 7;
        gridBagConstraints4.weightx = 100.0D;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.gridx = 9;
        gridBagConstraints3.weightx = 100.0D;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.weightx = 100.0D;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints2.gridx = 12;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints1.weightx = 10.0D;
        gridBagConstraints1.gridy = 2;
        endLabel = new JLabel();
        endLabel.setText("End Position:");
        GridBagConstraints gridBagConstraintsBeginLabel = new GridBagConstraints();
        gridBagConstraintsBeginLabel.gridx = 0;
        gridBagConstraintsBeginLabel.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraintsBeginLabel.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraintsBeginLabel.weightx = 10.0D;
        gridBagConstraintsBeginLabel.gridy = 1;
        beginLabel = new JLabel();
        beginLabel.setText("Begin Position:");
        this.setLayout(new GridBagLayout());
        this.setSize(400, 70);
        this.setPreferredSize(new java.awt.Dimension(400,70));
        this.add(endLabel, gridBagConstraints1);
        this.add(beginLabel, gridBagConstraintsBeginLabel);
        this.add(getJTextField(), gridBagConstraints17);
        this.add(getSecondTF(), gridBagConstraints2);
        this.add(getJTextField2(), gridBagConstraints21);
        this.add(getJTextField1(), gridBagConstraints31);
        this.add(getMinuteTF(), gridBagConstraints3);
        this.add(jLabel, gridBagConstraints41);
        this.add(getJTextField22(), gridBagConstraints51);
        this.add(getJTextField3(), gridBagConstraints61);
        this.add(getJTextField4(), gridBagConstraints71);
        this.add(secondLabel, gridBagConstraints8);
        this.add(timeZoneLabel, gridBagConstraintsTZLbl);
        this.add(minute, gridBagConstraints9);
        this.add(d1Label, gridBagConstraints14);
        this.add(jLabel1, gridBagConstraints81);
        this.add(jLabel2, gridBagConstraints91);
        this.add(getEndTimeZoneTF(), gridBagConstraintsTZ2);
        this.add(getTimeZoneTF(), gridBagConstraintsTZ1);
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
    public TimePeriod getChosenTime() {
        
        String beginSecond = (beginSecondTF.getText().equals("")) ? "00" : beginSecondTF.getText();
        String beginMinute = (beginMinuteTF.getText().equals("")) ? "00" : beginMinuteTF.getText();
        String beginHour = (beginHourTF.getText().equals("")) ? "00" : beginHourTF.getText();
        String beginDay = beginDayTF.getText();
        String beginMonth = beginMonthTF.getText();
        String beginYear = beginYearTF.getText();
        String beginTZ = beginTimeZoneTF.getText();

        String endSecond = (endSecondTF.getText().equals("")) ? "00" : endSecondTF.getText();
        String endMinute = (endMinuteTF.getText().equals("")) ? "00" : endMinuteTF.getText();
        String endHour = (endHourTF.getText().equals("")) ? "00" : endHourTF.getText();
        String endDay = endDayTF.getText();
        String endMonth = endMonthTF.getText();
        String endYear = endYearTF.getText();
        String endTZ = endTimeZoneTF.getText();

        
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
        
        // TODO hard-coded: Sommerzeit für DEMO-Zwecke rein gehackt!!!!
        timeZoneDiffToUTC = timeZoneDiffToUTC + 1;
        
        systemTimeZone += Math.abs(timeZoneDiffToUTC);
        String beginString = "";
        String endString = "";
        
        //Wenn die Zeitzone vorgegeben dann diese nehmen sonst die vom System nehmen
        if(beginTZ.equals("")){
        	beginString = beginYear + "-" + beginMonth + "-" + beginDay + "T" + beginHour + ":" + beginMinute + ":" + beginSecond + systemTimeZone;
        }else{
        	beginString = beginYear + "-" + beginMonth + "-" + beginDay + "T" + beginHour + ":" + beginMinute + ":" + beginSecond + beginTZ;
        }
        if(endTZ.equals("")){
        	endString = endYear + "-" + endMonth + "-" + endDay + "T" + endHour + ":" + endMinute + ":" + endSecond + systemTimeZone;
        }else{
        	endString = endYear + "-" + endMonth + "-" + endDay + "T" + endHour + ":" + endMinute + ":" + endSecond + endTZ;
        }
        
        return new TimePeriod(beginString, endString);
    }

    @Override
    public void clear() {
        beginSecondTF.setText("");
        beginMinuteTF.setText("");
        beginHourTF.setText("");
        beginDayTF.setText("");
        beginMonthTF.setText("");
        beginYearTF.setText("");
        endSecondTF.setText("");
        endMinuteTF.setText("");
        endHourTF.setText("");
        endDayTF.setText("");
        endMonthTF.setText("");
        endYearTF.setText("");
    }

} // @jve:decl-index=0:visual-constraint="-41,11"