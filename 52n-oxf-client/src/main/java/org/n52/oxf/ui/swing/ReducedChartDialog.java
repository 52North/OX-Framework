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

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import org.jfree.chart.*;
import org.n52.oxf.*;
import org.n52.oxf.feature.*;
import org.n52.oxf.owsCommon.*;
import org.n52.oxf.owsCommon.capabilities.*;
import org.n52.oxf.render.*;
import org.n52.oxf.render.sos.*;
import org.n52.oxf.serviceAdapters.*;
import org.n52.oxf.valueDomains.time.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ReducedChartDialog extends JDialog {

    private IChartRenderer chartRenderer;

    // private TimePeriodPanel timePeriodPanel = null;
    // private JButton applyButton;

    private ParameterContainer paramCon;
    private OXFFeatureCollection observations;
    private IServiceAdapter adapter;
    private ServiceDescriptor descriptor;
    private IFeatureStore featureStore;

    private int dialogWidth = 730;
    private int dialogHeight = 640;

    private int chartWidth = 700;
    private int chartHeight = 400;

    /**
     * This is the default constructor
     */
    public ReducedChartDialog(Component owner,
                              ParameterContainer paramCon,
                              OXFFeatureCollection observations,
                              IServiceAdapter adapter,
                              ServiceDescriptor descriptor,
                              IFeatureStore featureStore,
                              IChartRenderer chartRenderer) {
        setTitle("Chart View");

        this.paramCon = paramCon;
        this.observations = observations;
        this.adapter = adapter;
        this.descriptor = descriptor;
        this.featureStore = featureStore;
        this.chartRenderer = chartRenderer;

        // TODO: this maybe cause a ClassCastException, because time might be of type ITimePosition -->
        // TimePeriod timePeriod = (TimePeriod)
        // paramCon.getParameterShellWithCommonName(Parameter.COMMON_NAME_TIME).getSpecifiedValue();
        //
        // timePeriodPanel = new TimePeriodPanel(timePeriod);

        // applyButton = new JButton("Close");
        // applyButton.addActionListener(this);

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

        // mainLayout.addComponent(0,
        // timePeriodPanel,
        // 0,
        // 1,
        // 1,
        // 1,
        // 100,
        // 10,
        // GridBagConstraints.CENTER,
        // GridBagConstraints.NONE,
        // new Insets(2, 2, 2, 2));

        // mainLayout.addComponent(1,
        // applyButton,
        // 0,
        // 2,
        // 1,
        // 1,
        // 100,
        // 10,
        // GridBagConstraints.CENTER,
        // GridBagConstraints.NONE,
        // new Insets(0, 0, 0, 0));
    }

    /**
     * initializes (or updates) the OXFChartPanel.
     * 
     * @param parameters
     * @param selectedfeatures
     */
    private void initOXFChartPanel(OXFFeatureCollection observations, ParameterContainer paramCon) {

        // String[] foiIdArray = (String[])
        // paramCon.getParameterShellWithServiceSidedName("featureOfInterest").getSpecifiedValueArray();

        OXFChartPanel chartPanel = new OXFChartPanel();

        MyGridBagLayout mainLayout = (MyGridBagLayout) getContentPane().getLayout();

        if (getContentPane().getComponentCount() > 2) {
            getContentPane().remove(2);
        }

        mainLayout.addComponent(0,
                                chartPanel,
                                0,
                                0,
                                1,
                                1,
                                100,
                                100,
                                GridBagConstraints.NORTHWEST,
                                GridBagConstraints.BOTH,
                                new Insets(9, 9, 9, 9));

        // TODO what's about AnimatedVisualization ??
        StaticVisualization visualization = (StaticVisualization) chartRenderer.renderChart(observations,
                                                                                            paramCon,
                                                                                            chartWidth,
                                                                                            chartHeight);

        chartPanel.setChartImage(visualization.getRendering());

        getContentPane().validate();
    }

    /**
     * initializes (or updates) the JFreeChart->ChartPanel.
     * 
     * @param parameters
     * @param selectedfeatures
     */
    private void initChartPanel(OXFFeatureCollection observations, ParameterContainer paramCon) {

        // String[] foiIdArray = (String[])
        // paramCon.getParameterShellWithServiceSidedName("featureOfInterest").getSpecifiedValueArray();

        MyGridBagLayout mainLayout = (MyGridBagLayout) getContentPane().getLayout();

        if (getContentPane().getComponentCount() > 2) {
            getContentPane().remove(2);
        }

        JFreeChart chart = chartRenderer.renderChart(observations, paramCon);
        org.jfree.chart.ChartPanel chartPanel = new org.jfree.chart.ChartPanel(chart);
        chartPanel.setMouseZoomable(true, false);

        mainLayout.addComponent(0,
                                chartPanel,
                                0,
                                0,
                                1,
                                1,
                                100,
                                100,
                                GridBagConstraints.NORTHWEST,
                                GridBagConstraints.BOTH,
                                new Insets(9, 9, 9, 9));

        getContentPane().validate();
    }

    // public TimePeriodPanel getTimePeriodPanel() {
    // return timePeriodPanel;
    // }
    //
    // /**
    // *
    // */
    // public void actionPerformed(ActionEvent e) {
    // try {
    // if (e.getSource().equals(applyButton)) {
    // paramCon.setParameterValue(Parameter.COMMON_NAME_TIME,
    // timePeriodPanel.getChosenTime());
    //
    // OperationResult opRes =
    // adapter.doOperation(descriptor.getOperationsMetadata().getOperationByName(adapter.getResourceOperationName()),
    // paramCon);
    // observations = featureStore.unmarshalFeatures(opRes);
    //
    // initChartPanel(observations, paramCon);
    // }
    // }
    // catch (OXFException exc) {
    // exc.printStackTrace();
    // }
    // catch (ExceptionReport exc) {
    // exc.printStackTrace();
    // }
    // }

}