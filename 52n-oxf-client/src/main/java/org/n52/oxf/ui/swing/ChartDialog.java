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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JDialog;

import org.jfree.chart.JFreeChart;
import org.n52.oxf.adapter.IServiceAdapter;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.render.IChartRenderer;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.n52.oxf.valueDomains.time.TimePeriod;
import org.n52.oxf.valueDomains.time.TimePosition;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ChartDialog extends JDialog implements ActionListener {

    private IChartRenderer chartRenderer;

    private TimePeriodPanel timePeriodPanel = null;
    private JButton applyButton;
    private JButton nowButton;

    private ParameterContainer paramCon;
    private OXFFeatureCollection observations;
    private IServiceAdapter adapter;
    private ServiceDescriptor descriptor;
    private IFeatureStore featureStore;

    private int dialogWidth = 730;
    private int dialogHeight = 640;

    /**
     * This is the default constructor
     */
    public ChartDialog(Component owner,
                       ParameterContainer paramCon,
                       OXFFeatureCollection observations,
                       IServiceAdapter adapter,
                       ServiceDescriptor descriptor,
                       IFeatureStore featureStore,
                       IChartRenderer chartRenderer) {
        setTitle("Chart View");
        setLocation(owner.getLocation());

        this.paramCon = paramCon;
        this.observations = observations;
        this.adapter = adapter;
        this.descriptor = descriptor;
        this.featureStore = featureStore;
        this.chartRenderer = chartRenderer;

        // TODO: this maybe cause a ClassCastException, because time might be of type ITimePosition -->
        TimePeriod timePeriod = (TimePeriod) paramCon.getParameterShellWithCommonName(Parameter.COMMON_NAME_TIME).getSpecifiedValue();

        timePeriodPanel = new TimePeriodPanel(timePeriod);

        applyButton = new JButton("Apply");
        applyButton.addActionListener(this);

        nowButton = new JButton("Now");
        nowButton.addActionListener(this);
        
        initialize();
        initChartPanel(observations, paramCon);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(dialogWidth, dialogHeight);

        MyGridBagLayout mainLayout = new MyGridBagLayout(this.getContentPane());

        this.getContentPane().setLayout(mainLayout);

        mainLayout.addComponent(0,
                                timePeriodPanel,
                                0,
                                1,
                                1,
                                1,
                                10,
                                10,
                                GridBagConstraints.EAST,
                                GridBagConstraints.NONE,
                                new Insets(2, 2, 2, 2));

        mainLayout.addComponent(1,
                                applyButton,
                                0,
                                2,
                                2,
                                1,
                                100,
                                10,
                                GridBagConstraints.CENTER,
                                GridBagConstraints.NONE,
                                new Insets(0, 0, 0, 0));
        
        mainLayout.addComponent(2,
                                nowButton,
                                1,
                                1,
                                1,
                                1,
                                10,
                                10,
                                GridBagConstraints.WEST,
                                GridBagConstraints.NONE,
                                new Insets(32, 0, 0, 0));
    }

    /**
     * initializes (or updates) the JFreeChart->ChartPanel.
     * 
     * @param parameters
     * @param selectedfeatures
     */
    private void initChartPanel(OXFFeatureCollection observations, ParameterContainer paramCon) {

        MyGridBagLayout mainLayout = (MyGridBagLayout) getContentPane().getLayout();

        int chartPanelPosition = 3;
        
        if (getContentPane().getComponentCount() > chartPanelPosition) {
            getContentPane().remove(chartPanelPosition);
        }

        JFreeChart chart = chartRenderer.renderChart(observations, paramCon);
        org.jfree.chart.ChartPanel chartPanel = new org.jfree.chart.ChartPanel(chart);
        chartPanel.setMouseZoomable(true, true);
        
        mainLayout.addComponent(chartPanelPosition,
                                chartPanel,
                                0,
                                0,
                                2,
                                1,
                                100,
                                100,
                                GridBagConstraints.NORTHWEST,
                                GridBagConstraints.BOTH,
                                new Insets(9, 9, 9, 9));

        getContentPane().validate();
    }

    public TimePeriodPanel getTimePeriodPanel() {
        return timePeriodPanel;
    }

    /**
     * 
     */
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource().equals(applyButton)) {
                paramCon.setParameterValue(Parameter.COMMON_NAME_TIME,
                                           timePeriodPanel.getChosenTime());

                OperationResult opRes = adapter.doOperation(descriptor.getOperationsMetadata().getOperationByName(adapter.getResourceOperationName()),
                                                            paramCon);
                observations = featureStore.unmarshalFeatures(opRes);

                initChartPanel(observations, paramCon);
            }
            
            else if (e.getSource().equals(nowButton)) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(System.currentTimeMillis());
                Date date = calendar.getTime();
                
                SimpleDateFormat ISO8601FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                String result = ISO8601FORMAT.format(date);
                //convert into YYYYMMDDTHH:mm:ss+HH:00
                //- note the added colon for the Timezone
                result = result.substring(0, result.length()-2) + ":" + result.substring(result.length()-2);
                
                ITimePosition endPos = new TimePosition(result);
                timePeriodPanel.setEndPosition(endPos);
            }
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }

}