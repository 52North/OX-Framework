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

package org.n52.oxf.ui.swing.csw;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.namespace.QName;

import net.opengis.cat.csw.x202.GetRecordsResponseDocument;
import net.opengis.cat.csw.x202.GetRecordsResponseType;
import net.opengis.cat.csw.x202.SearchResultsType;

import org.apache.xmlbeans.XmlCursor;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.csw.CSWAdapter;
import org.n52.oxf.adapter.csw.CSWRequestBuilder;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.MyGridBagLayout;
import org.n52.oxf.ui.swing.ShowRequestDialog;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSWChooser extends JDialog implements ActionListener {
    
    private static final long serialVersionUID = -5906010770526713489L;

    private static final Logger LOGGER = LoggerFactory.getLogger(CSWChooser.class);

    private JButton btnChoose;
    private JButton btnCancel;

    private JLabel lblCSWURL;
    private JLabel lblType;
    private JLabel lblServiceType;
    private JLabel lblMaxRecords;
    private JLabel lblStartPosition;
    private JLabel lblPropertyName;
    private JLabel lblLiteral;

    private JComboBox cboTextfieldCSWURL;
    private JComboBox cboType;
    private JComboBox cboServiceType;
    private JComboBox cboMaxRecords;
    private JComboBox cboStartPostion;
    private JComboBox cboPropertyName;
    private JComboBox cboLiteral;

    private Component owner;
    private MapCanvas map;
    private ContentTree tree;
    
    public CSWChooser(Component owner, MapCanvas map, ContentTree tree) {
        super();
        
        this.owner = owner;
        this.map = map;
        this.tree = tree;
        
        setSize(700, 300);
        setLocation(owner.getLocation());
        setTitle("Choose CSW");

        btnChoose = new JButton();
        btnChoose.setText("Produce GetRecords Request");
        btnChoose.addActionListener(this);

        btnCancel = new JButton();
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(this);

        lblCSWURL = new JLabel("CSW URL:");
        cboTextfieldCSWURL = new JComboBox();
        cboTextfieldCSWURL.setEditable(true);
        cboTextfieldCSWURL.addItem("http://geossregistries.info:1090/GEOSSCSW202/discovery");

        lblType = new JLabel("Search for:");
        cboType = new JComboBox();
        cboType.addItem("Service");

        lblServiceType = new JLabel("Service type:");
        cboServiceType = new JComboBox();
        cboServiceType.setEditable(true);
        cboServiceType.addItem("WebCoverageService");
        cboServiceType.addItem("WebMapService");
        cboServiceType.addItem("SensorObservationService");
        
        lblMaxRecords = new JLabel("Max records:");
        cboMaxRecords = new JComboBox();
        cboMaxRecords.setEditable(true);
        for (int i = 1; i < 21; i++) {
            cboMaxRecords.addItem(i);
        }
        cboMaxRecords.setSelectedItem("10");

        lblStartPosition = new JLabel("Start position:");
        cboStartPostion = new JComboBox();
        cboStartPostion.setEditable(true);
        for (int i = 1; i < 21; i++) {
            cboStartPostion.addItem(i);
        }

        lblPropertyName = new JLabel("Property to search for (XPath definition):");
        cboPropertyName = new JComboBox();
        cboPropertyName.setEditable(true);
        cboPropertyName.setToolTipText("Put your XPath query here.");
        cboPropertyName.addItem("/rim:Service/rim:Name/rim:LocalizedString/@value");

        lblLiteral = new JLabel("Literal to search for:");
        cboLiteral = new JComboBox();
        cboLiteral.setEditable(true);
        cboLiteral.addItem("*PCI*");
        cboLiteral.addItem("*SPOT*");

        JPanel panelMain = new JPanel();

        MyGridBagLayout layout = new MyGridBagLayout(panelMain);
        panelMain.setLayout(layout);

        layout.addComponent(lblCSWURL, 0, 0, 1, 1, 100, 100);
        layout.addComponent(cboTextfieldCSWURL, 1, 0, 1, 1, 100, 100);
        layout.addComponent(lblType, 0, 1, 1, 1, 100, 100);
        layout.addComponent(cboType, 1, 1, 1, 1, 100, 100);
        layout.addComponent(lblServiceType, 0, 2, 1, 1, 100, 100);
        layout.addComponent(cboServiceType, 1, 2, 1, 1, 100, 100);
        layout.addComponent(lblMaxRecords, 0, 3, 1, 1, 100, 100);
        layout.addComponent(cboMaxRecords, 1, 3, 1, 1, 100, 100);
        layout.addComponent(lblStartPosition, 0, 4, 1, 1, 100, 100);
        layout.addComponent(cboStartPostion, 1, 4, 1, 1, 100, 100);
        layout.addComponent(lblPropertyName, 0, 5, 1, 1, 100, 100);
        layout.addComponent(cboPropertyName, 1, 5, 1, 1, 100, 100);
        layout.addComponent(lblLiteral, 0, 6, 1, 1, 100, 100);
        layout.addComponent(cboLiteral, 1, 6, 1, 1, 100, 100);

        JPanel buttonPanel = new JPanel();
        MyGridBagLayout buttonPanelLayout = new MyGridBagLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);

        buttonPanelLayout.addComponent(btnChoose, 0, 0, 1, 1, 100, 100);
        buttonPanelLayout.addComponent(btnCancel, 1, 0, 1, 1, 100, 100);

        layout.addComponent(buttonPanel, 0, 7, 2, 1, 100, 100);

        getContentPane().add(panelMain);

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnCancel) {
            this.dispose();
            return;
        }

        // CALL DESCRIBE RECORD

        CSWAdapter adapter = new CSWAdapter();
        CSWRequestBuilder builder = new CSWRequestBuilder();
        String request;
        ParameterContainer paramCon = new ParameterContainer();
        ShowRequestDialog dialog;
        Vector<ServiceEntry> services = new Vector<ServiceEntry>();

        OperationResult result;
        try {

            paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_VERSION_PARAMETER,
                                       CSWAdapter.SUPPORTED_VERSIONS[0]);
            paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_SERVICE_PARAMETER,
                                       cboServiceType.getSelectedItem().toString());
            paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_OUTPUT_FORMAT_PARAMETER, "application/xml");
            paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_MAX_RECORDS,
                                       cboMaxRecords.getSelectedItem().toString());
            paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_START_POSITION,
                                       cboStartPostion.getSelectedItem().toString());
            paramCon.addParameterShell(CSWRequestBuilder.GET_RECORDS_OUTPUT_SCHEMA_FORMAT,
                                       "http://www.opengis.net/cat/csw");
            String typeNames[] = {"Service", "Organization"};
            paramCon.addParameterShell(CSWRequestBuilder.GET_RECORDS_TYPE_NAME_PARAMETER, typeNames);
            paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_SCHEMA_LANGUAGE_PARAMETER, "XMLSCHEMA");
            paramCon.addParameterShell(CSWRequestBuilder.GET_RECORDS_LITERAL, cboLiteral.getSelectedItem().toString());
            paramCon.addParameterShell(CSWRequestBuilder.GET_RECORDS_PROPERTY_NAME,
                                       cboPropertyName.getSelectedItem().toString());
            paramCon.addParameterShell(CSWRequestBuilder.GET_RECORDS_SERVICE_TYPE_2SEARCH4_PARAMETER,
                                       cboServiceType.getSelectedItem().toString());
            request = builder.buildGetRecordsRequest(paramCon);

            dialog = new ShowRequestDialog(this, "CSW Request", request, "Send Request");
            if (dialog.showDialog() == ShowRequestDialog.APPROVE_OPTION) {
                OperationResult opResult = adapter.doOperation(new Operation("GetRecords",
                                                                             cboTextfieldCSWURL.getSelectedItem().toString()
                                                                                     + "?",
                                                                             cboTextfieldCSWURL.getSelectedItem().toString()),
                                                               paramCon);
                ByteArrayInputStream inputStream = opResult.getIncomingResultAsStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String xml = "";
                while (reader.ready()) {
                    xml += reader.readLine() + "\n";
                }
                LOGGER.info(xml);

                xml = xml.replaceAll("http://www.opengis.net/cat/csw", "http://www.opengis.net/cat/csw/2.0.2");
                GetRecordsResponseDocument doc = GetRecordsResponseDocument.Factory.parse(xml);
                GetRecordsResponseType type = doc.getGetRecordsResponse();
                SearchResultsType searchResults = type.getSearchResults();

                XmlCursor cursor = searchResults.newCursor();
                cursor.toFirstChild();
                QName service = new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "Service");
                cursor.toChild(service);
                do {
                    // name
                    cursor.toChild(new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "Name"));
                    cursor.toChild(new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "LocalizedString"));
                    String name = cursor.getAttributeText(new QName("value"));

                    cursor.toParent();
                    cursor.toParent();

                    // Description
                    cursor.toChild(new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "Description"));
                    cursor.toChild(new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "LocalizedString"));
                    String description = cursor.getAttributeText(new QName("value"));

                    cursor.toParent();
                    cursor.toParent();

                    // URL
                    QName slot = new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "Slot");
                    cursor.toChild(slot);
                    String url = null;
                    do {
                        if ("serviceInfo".equals(cursor.getAttributeText(new QName("name")))) {
                            url = cursor.getTextValue();
                            break;
                        }
                    } while (cursor.toNextSibling(slot));

                    if (name != null && description != null && url != null) {
                        services.add(new ServiceEntry(name, description, url));
                    }
                    
                    cursor.toParent();

                } while (cursor.toNextSibling(service));

                CSWSearchResultDialog resultDialog = new CSWSearchResultDialog(owner, map, tree, services, cboServiceType.getSelectedItem().toString());

                this.setVisible(false);
            }
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }

}