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

package org.n52.oxf.ui.swing.generic;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import org.n52.oxf.*;
import org.n52.oxf.adapter.*;
import org.n52.oxf.ows.capabilities.*;
import org.n52.oxf.valueDomains.filter.*;
import org.n52.oxf.valueDomains.spatial.*;
import org.n52.oxf.valueDomains.time.*;

/**
 * This class can be used to generically produce a dialog where the user may setup the parameter configuration
 * to connect to a service. Please use the methods <code>showForm</code> and afterwards
 * <code>getFilledParameterContainer</code> to generate a ParameterContainer object that can be sent to the
 * service. <br>
 * <br>
 * The following code pops up a dialog where the user may setup the parameter configuration and afterwards
 * prints the resulting ParameterConatiner to the console.
 * 
 * <pre>
 *   // we assume that we have the following objects instantiated: (otherwise adjust this example).
 *   ServiceDescriptor descriptor = ...;
 *   IServiceAdapter adapter = ...;
 *   IServiceRenderer renderer = ...;
 *   
 *   // the Parameter-array params[] must be caught from the ServiceDescriptor
 *   Parameter[] params = descriptor.getOperationsMetadata().getOperationByName(&quot;...operationToExecute...&quot;).getParameters();
 *   
 *   GenericParameterConfigurator gpc = new GenericParameterConfigurator();
 *   int returnVal = gpc.showForm(params, new JFrame());
 *   
 *   ParameterContainer paramContainer = null;
 *   if (returnVal == GenericParameterConfigurator.APPROVE_OPTION) {
 *       paramContainer = gpc.getFilledParameterContainer();
 *       System.out.println(paramContainer);
 *   }
 *   
 *   // now you can create a RasterServiceLayer:
 *   RasterServiceLayer layer = new RasterServiceLayer(adapter, renderer, descriptor, paramContainer, data.getIdentifier());
 * </pre>
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class GenericParameterConfigurator {

    /**
     * Return value if cancel is chosen.
     */
    public static final int CANCEL_OPTION = 1;

    /**
     * Return value if approve (yes, ok) is chosen.
     */
    public static final int APPROVE_OPTION = 0;

    /**
     * Return value if an error occured.
     */
    public static final int ERROR_OPTION = -1;

    private int returnValue = ERROR_OPTION;

    private GenericParameterConfigDialog dialog;

    private Parameter resourceIDParameter = null;

    /**
     * Pops up a GenericParameterConfigurator.
     * 
     * @param params
     * @param parent
     * 
     * @return - the return state of the GenericParameterConfigurator on popdown:
     *         <li>GenericParameterConfigurator.CANCEL_OPTION
     *         <li>GenericParameterConfigurator.APPROVE_OPTION
     *         <li>GenericParameterConfigurator.ERROR_OPTION if an error occurs or the dialog is dismissed
     * 
     */
    public int showDialog(Parameter[] params) {
        dialog = new GenericParameterConfigDialog(params);
        dialog.setVisible(true);

        return returnValue;
    }

    /**
     * 
     * @return
     * @throws OXFException
     */
    public ParameterContainer getFilledParameterContainer() throws OXFException {
        ParameterContainer paramContainer = null;

        if (returnValue == APPROVE_OPTION) {
            paramContainer = new ParameterContainer();

            for (ParameterPanel p : dialog.getParameterPanelList()) {

                if (p instanceof DatasetParameterPanel) {
                    DatasetParameterPanel datasetParamPanel = (DatasetParameterPanel) p;

                    if (datasetParamPanel.isSelected()) {
                        // set selected resourceIDParameterShell:
                        ParameterShell resourceIDParameterShell = new ParameterShell(resourceIDParameter,
                                                                                     datasetParamPanel.getDatasetIdentifier());
                        paramContainer.addParameterShell(resourceIDParameterShell);

                        for (ServiceParameterPanel spp : datasetParamPanel.getParameterPanelList()) {
                            InputElement inputElement = spp.getInputElement();
                            if (inputElement != null) {
                                paramContainer.addParameterShell(inputElement.getParameterShell());
                            }
                        }
                    }
                }
                else if (p instanceof ServiceParameterPanel) {
                    InputElement inputElement = ((ServiceParameterPanel) p).getInputElement();
                    if (inputElement != null) {
                        paramContainer.addParameterShell(inputElement.getParameterShell());
                    }
                }
            }
        }
        return paramContainer;
    }

    /**
     * 
     * 
     * 
     * @author <a href="mailto:broering@52north.org">Arne Broering</a>
     * 
     */
    private class GenericParameterConfigDialog extends JDialog implements ActionListener {

        private Parameter[] params;

        private JScrollPane scrollPane;
        private JPanel buttonPanel;
        private JButton okButton;

        private JPanel subPanel;

        private List<ParameterPanel> parameterPanelList;

        private ButtonGroup buttonGroup;

        /**
         * @param String
         *        identifier of the dataset
         * @param ServiceParameterPanel
         *        the associated ServiceParameterPanel
         */
        private Map<String, DatasetParameterPanel> datasetParamPanelMap;

        /**
         * 
         * @param params
         *        the Parameters used to construct the ParameterPanels.
         * @param parent
         */
        public GenericParameterConfigDialog(Parameter[] params) {
            super();
            setTitle("Please specify values for the parameters.");
            setModal(true);

            this.params = params;

            parameterPanelList = new ArrayList<ParameterPanel>();
            datasetParamPanelMap = new HashMap<String, DatasetParameterPanel>();

            this.setLayout(new BorderLayout());

            setSize(600, 400);
            setLocation(200, 200);

            scrollPane = new JScrollPane();
            subPanel = new JPanel();
            subPanel.setLayout(new GridBagLayout());
            scrollPane.add(subPanel);
            scrollPane.setViewportView(subPanel);
            this.add(BorderLayout.CENTER, scrollPane);

            buttonPanel = new JPanel();
            okButton = new JButton("ok");
            buttonPanel.add(okButton);
            this.add(BorderLayout.SOUTH, buttonPanel);

            okButton.addActionListener(this);

            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    returnValue = CANCEL_OPTION;
                }
            });

            buttonGroup = new ButtonGroup();

            addParameterPanels();
        }

        /**
         * 
         * @return
         */
        public List<ParameterPanel> getParameterPanelList() {
            return parameterPanelList;
        }

        /**
         * 
         * @return
         */
        public List<ServiceParameterPanel> getServiceParameterPanelList() {
            List<ServiceParameterPanel> list = new ArrayList<ServiceParameterPanel>();

            for (ParameterPanel panel : parameterPanelList) {
                if (panel instanceof ServiceParameterPanel) {
                    list.add((ServiceParameterPanel) panel);
                }
            }
            return list;
        }

        /**
         * 
         * @return
         */
        public List<DatasetParameterPanel> getDatasetParameterPanelList() {
            List<DatasetParameterPanel> list = new ArrayList<DatasetParameterPanel>();

            for (ParameterPanel panel : parameterPanelList) {
                if (panel instanceof DatasetParameterPanel && !list.contains(panel)) {
                    list.add((DatasetParameterPanel) panel);
                }
            }
            return list;
        }

        /**
         * adds the ParameterPanels to the Dialog.
         */
        private void addParameterPanels() {
            for (int i = 0; i < params.length; i++) {
                Parameter parameter = params[i];

                // if the parameter has the CommonName 'COMMON_NAME_RESOURCE_ID' do not make a ParameterPanel
                // for it but save the Parameter in the attribute resourceIDParameter:
                if (parameter.getCommonName() != null
                        && parameter.getCommonName().equals(Parameter.COMMON_NAME_RESOURCE_ID)) {
                    resourceIDParameter = parameter;
                }
                else {
                    ServiceParameterPanel parameterPanel = new ServiceParameterPanel();
    
                    // add UIelements to the "parameterPanel":
                    UIElement parameterName = new UIElement();
                    parameterName.addComponent(new JLabel(parameter.getServiceSidedName()),
                                               0,
                                               0,
                                               1,
                                               1,
                                               100,
                                               100);
                    parameterPanel.addUIElement(parameterName);
    
                    IValueDomain vd = parameter.getValueDomain();
                    if (vd instanceof BoundingBox) {
                        parameterPanel.addInputElement(new BoundingBoxInputElement(parameter));
                    }
    
                    else if (vd instanceof FilterValueDomain) {
                        parameterPanel.addInputElement(new FilterValueDomainInputElement(parameter));
                    }
    
                    else if (vd instanceof TemporalValueDomain) {
                        parameterPanel.addInputElement(new TemporalValueDomainInputElement(parameter));
                    }
    
                    else if (vd instanceof IDiscreteValueDomain) {
                        parameterPanel.addInputElement(new DiscreteValueDomainInputElement(parameter));
                    }
    
                    else if (vd instanceof IRangeValueDomain) {
                        parameterPanel.addInputElement(new RangeValueDomainInputElement(parameter));
                    }
    
                    // add the "parameterPanel":
                    if (parameter instanceof DatasetParameter) {
                        DatasetParameter datasetParameter = (DatasetParameter) parameter;
    
                        String datasetID = datasetParameter.getAssociatedDataset().getIdentifier();
    
                        if (datasetParamPanelMap.containsKey(datasetID)) {
                            DatasetParameterPanel datasetParameterPanel = datasetParamPanelMap.get(datasetID);
                            datasetParameterPanel.addParameterPanel(parameterPanel);
                        }
                        else {
                            DatasetParameterPanel datasetParameterPanel = new DatasetParameterPanel(datasetID,
                                                                                                    buttonGroup);
                            datasetParameterPanel.addParameterPanel(parameterPanel);
    
                            datasetParamPanelMap.put(datasetID, datasetParameterPanel);
    
                            parameterPanelList.add(datasetParameterPanel);
                        }
                    }
                    else {
                        // layout the "parameterPanel":
                        GridBagConstraints gbcParameterPanel = new GridBagConstraints(0,
                                                                                      i,
                                                                                      1,
                                                                                      1,
                                                                                      100,
                                                                                      10,
                                                                                      GridBagConstraints.PAGE_START,
                                                                                      GridBagConstraints.HORIZONTAL,
                                                                                      new Insets(1,
                                                                                                 1,
                                                                                                 1,
                                                                                                 1),
                                                                                      0,
                                                                                      0);
    
                        // last element: weighty = 100:
                        if (i == params.length - 1) {
                            gbcParameterPanel.weighty = 100;
                        }
    
                        ((GridBagLayout) subPanel.getLayout()).setConstraints(parameterPanel,
                                                                              gbcParameterPanel);
    
                        subPanel.add(parameterPanel);
    
                        parameterPanelList.add(parameterPanel);
                    }
                }

            }// end-for

            // add "datasetParamPanel"s:
            Iterator<String> keySetIterator = datasetParamPanelMap.keySet().iterator();
            int j = 0;
            while (keySetIterator.hasNext()) {
                String key = keySetIterator.next();
                DatasetParameterPanel datasetParamPanel = (DatasetParameterPanel) datasetParamPanelMap.get(key);

                // layout the "datasetParamPanel":
                GridBagConstraints gbcDatasetParamPanel = new GridBagConstraints(0,
                                                                                 j,
                                                                                 1,
                                                                                 1,
                                                                                 100,
                                                                                 10,
                                                                                 GridBagConstraints.PAGE_START,
                                                                                 GridBagConstraints.HORIZONTAL,
                                                                                 new Insets(1,
                                                                                            1,
                                                                                            1,
                                                                                            1),
                                                                                 0,
                                                                                 0);

                // last element: weighty = 100:
                if (j == datasetParamPanelMap.keySet().size() - 1) {
                    gbcDatasetParamPanel.weighty = 100;
                }

                ((GridBagLayout) subPanel.getLayout()).setConstraints(datasetParamPanel,
                                                                      gbcDatasetParamPanel);

                subPanel.add(datasetParamPanel);
                j++;
            }
        }

        /**
         * 
         */
        public void actionPerformed(ActionEvent event) {
            if (event.getSource().equals(okButton)) {
                returnValue = APPROVE_OPTION;
                this.dispose();
            }
        }
    }

    private abstract class ParameterPanel extends JPanel {

        public ParameterPanel() {
            this.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED, null, null));
            this.setLayout(new BoxLayout(this, javax.swing.BoxLayout.X_AXIS));
        }
    }

    private class ServiceParameterPanel extends ParameterPanel {

        private InputElement inputElement;

        public ServiceParameterPanel() {
            super();
        }

        public void addUIElement(UIElement e) {
            this.add(e);
        }

        public void addInputElement(InputElement e) {
            this.add(e);

            inputElement = e;
        }

        public InputElement getInputElement() {
            return inputElement;
        }
    }

    private class DatasetParameterPanel extends ParameterPanel {

        private JRadioButton radioButton;

        private String datasetIdentifier;

        private JPanel yPanel;

        private List<ServiceParameterPanel> paramPanelList;

        public DatasetParameterPanel(String datasetIdentifier, ButtonGroup buttonGroup) {
            super();

            this.datasetIdentifier = datasetIdentifier;

            paramPanelList = new ArrayList<ServiceParameterPanel>();

            radioButton = new JRadioButton(datasetIdentifier);
            buttonGroup.add(radioButton);

            yPanel = new JPanel();
            yPanel.setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED, null, null));
            yPanel.setLayout(new BoxLayout(yPanel, javax.swing.BoxLayout.Y_AXIS));

            this.add(radioButton, BorderLayout.WEST);
            this.add(yPanel, BorderLayout.CENTER);
        }

        public String getDatasetIdentifier() {
            return datasetIdentifier;
        }

        public boolean isSelected() {
            return radioButton.isSelected();
        }

        public void addParameterPanel(ServiceParameterPanel paramPanel) {
            yPanel.add(paramPanel);
            paramPanelList.add(paramPanel);
        }

        public List<ServiceParameterPanel> getParameterPanelList() {
            return paramPanelList;
        }
    }

    private class UIElement extends JPanel {

        public UIElement() {
            this.setLayout(new GridBagLayout());
        }

        public void addComponent(Component c,
                                 int gridx,
                                 int gridy,
                                 int gridwidth,
                                 int gridheight,
                                 double weightx,
                                 double weighty) {

            GridBagConstraints componentGBC = new GridBagConstraints(gridx,
                                                                     gridy,
                                                                     gridwidth,
                                                                     gridheight,
                                                                     weightx,
                                                                     weighty,
                                                                     GridBagConstraints.PAGE_START,
                                                                     GridBagConstraints.HORIZONTAL,
                                                                     new Insets(1, 1, 1, 1),
                                                                     0,
                                                                     0);

            ((GridBagLayout) getLayout()).setConstraints(c, componentGBC);
            add(c);
        }
    }

    private abstract class InputElement extends UIElement {

        public abstract ParameterShell getParameterShell() throws OXFException;
    }

    private class BoundingBoxInputElement extends InputElement {

        private Parameter bBoxParameter;

        private JTextField maxXTextField;
        private JTextField minXTextField;
        private JTextField maxYTextField;
        private JTextField minYTextField;

        public BoundingBoxInputElement(Parameter param) {
            bBoxParameter = param;

            BoundingBox bBox = (BoundingBox) bBoxParameter.getValueDomain();

            minXTextField = new JTextField("" + bBox.getMinValue()[0]);
            minYTextField = new JTextField("" + bBox.getMinValue()[1]);

            maxXTextField = new JTextField("" + bBox.getMaxValue()[0]);
            maxYTextField = new JTextField("" + bBox.getMaxValue()[1]);

            // minX : minXTextField - minY : minYTextField
            // maxX : maxXTextField - maxY : maxYTextField
            //
            
            addComponent(new JLabel("minX: "), 0, 0, 1, 1, 100, 100);
            addComponent(minXTextField, 1, 0, 1, 1, 100, 100);

            addComponent(new JLabel("minY: "), 2, 0, 1, 1, 100, 100);
            addComponent(minYTextField, 3, 0, 1, 1, 100, 100);

            addComponent(new JLabel("maxX: "), 0, 1, 1, 1, 100, 100);
            addComponent(maxXTextField, 1, 1, 1, 1, 100, 100);

            addComponent(new JLabel("maxY: "), 2, 1, 1, 1, 100, 100);
            addComponent(maxYTextField, 3, 1, 1, 1, 100, 100);
        }

        public ParameterShell getParameterShell() throws OXFException {
            double[] lowerLeftCorner = new double[] {Double.parseDouble(minXTextField.getText()),
                                                     Double.parseDouble(minYTextField.getText())};
            double[] upperRightCorner = new double[] {Double.parseDouble(maxXTextField.getText()),
                                                      Double.parseDouble(maxYTextField.getText())};

            BoundingBox bBoxValue = new BoundingBox("", lowerLeftCorner, upperRightCorner);

            ParameterShell paramShell = new ParameterShell(bBoxParameter, bBoxValue);

            return paramShell;
        }
    }

    private class TemporalValueDomainInputElement extends InputElement {

        private Parameter parameter;

        private JComboBox dateComboBox;

        public TemporalValueDomainInputElement(Parameter param) {
            this.parameter = param;

            TemporalValueDomain temporalDomain = (TemporalValueDomain) param.getValueDomain();

            dateComboBox = new JComboBox();

            for (ITime time : temporalDomain.getPossibleValues()) {
                dateComboBox.addItem(time);
            }

            // addComponent(new JLabel("possibleValues: "), 0, 0, 1, 1, 100, 100);
            addComponent(dateComboBox, 0, 0, 1, 1, 100, 100);
        }

        public ParameterShell getParameterShell() throws OXFException {
            ITime time = (ITime) dateComboBox.getSelectedItem();

            ParameterShell paramShell = new ParameterShell(parameter, time);

            return paramShell;
        }
    }

    private class RangeValueDomainInputElement extends InputElement {

        private Parameter parameter;

        private JTextField maxTextField;
        private JTextField minTextField;

        public RangeValueDomainInputElement(Parameter param) {
            this.parameter = param;

            IRangeValueDomain valueDomain = (IRangeValueDomain) param.getValueDomain();

            maxTextField = new JTextField("" + valueDomain.getMaxValue());
            minTextField = new JTextField("" + valueDomain.getMinValue());

            addComponent(new JLabel("min: "), 0, 0, 1, 1, 100, 100);
            addComponent(minTextField, 1, 0, 1, 1, 100, 100);

            addComponent(new JLabel("max: "), 2, 0, 1, 1, 100, 100);
            addComponent(maxTextField, 3, 0, 1, 1, 100, 100);
        }

        public ParameterShell getParameterShell() throws OXFException {
            Object[] values = new Object[] {parameter.getValueDomain().produceValue(minTextField.getText()),
                                            parameter.getValueDomain().produceValue(maxTextField.getText())};

            ParameterShell paramShell = new ParameterShell(parameter, values);

            return paramShell;
        }
    }

    private class DiscreteValueDomainInputElement extends InputElement {

        private Parameter parameter;

        private JComboBox comboBox;

        /**
         * 
         * @param param
         */
        public DiscreteValueDomainInputElement(Parameter param) {
            parameter = param;

            IDiscreteValueDomain dvd = (IDiscreteValueDomain) param.getValueDomain();

            List possibleValues = dvd.getPossibleValues();
            comboBox = new JComboBox();

            for (Object o : possibleValues) {
                comboBox.addItem(o);
            }

            // addComponent(new JLabel("possibleValues: "), 0, 0, 1, 1, 100, 100);
            addComponent(comboBox, 0, 0, 1, 1, 100, 100);
        }

        public ParameterShell getParameterShell() throws OXFException {
            ParameterShell paramShell = new ParameterShell(parameter, comboBox.getSelectedItem());

            return paramShell;
        }
    }

    /**
     * 
     * 
     * 
     * @author <a href="mailto:staschc@52north.org">Christoph Stasch</a>
     * 
     */
    private class FilterValueDomainInputElement extends InputElement {

        private Parameter parameter;

        private JComboBox comboBox;

        /**
         * 
         * @param param
         */
        public FilterValueDomainInputElement(Parameter param) {
            parameter = param;

            FilterValueDomain fvd = (FilterValueDomain) param.getValueDomain();

            ArrayList<IFilter> possibleValues = (ArrayList<IFilter>) fvd.getPossibleValues();
            comboBox = new JComboBox();

            for (IFilter f : possibleValues) {
                String type = new String(f.getFilterType());
                comboBox.addItem(type);
            }

            addComponent(new JLabel("possibleValues: "), 0, 0, 1, 1, 100, 100);
            addComponent(comboBox, 1, 0, 1, 1, 100, 100);
        }

        /**
         * 
         */
        public ParameterShell getParameterShell() throws OXFException {
            ParameterShell paramShell = new ParameterShell(parameter, comboBox.getSelectedItem());

            return paramShell;
        }
    }
}