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

package org.n52.oxf.ui.swing.sos;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ConnectException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.sos.SOSAdapter;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ui.swing.ShowRequestDialog;
import org.n52.oxf.ui.swing.ShowXMLDocDialog;
import org.n52.oxf.util.IOHelper;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ShowGetObservationRequestDialog extends ShowRequestDialog implements ActionListener {

    /**
     * Return value if visualize is chosen.
     */
    public static final int VISUALIZE_OPTION = 2;

    private JButton visualizeObsButton = null;
    private Operation getObsOp;
    private SOSAdapter adapter;
    private ParameterContainer paramCon;

    private OperationResult opResult = null;

    /**
     * @param title
     * @param text
     * @param adapter
     * @param paramCon
     * @param getObsOp
     */
    public ShowGetObservationRequestDialog(JDialog owner,
                                           String title,
                                           String text,
                                           SOSAdapter adapter,
                                           Operation getObsOp,
                                           ParameterContainer paramCon) {
        super(owner, title, text);

        this.adapter = adapter;
        this.getObsOp = getObsOp;
        this.paramCon = paramCon;

        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 1;

        getButtonPanel().add(getVisualizeObsButton(), gridBagConstraints4);
    }

    /**
     * This method initializes visualizeObsButton
     * 
     * @return javax.swing.JButton
     */
    public JButton getVisualizeObsButton() {
        if (visualizeObsButton == null) {
            visualizeObsButton = new JButton();
            visualizeObsButton.setText("Send Request - visualize Observations");

            visualizeObsButton.addActionListener(this);
        }
        return visualizeObsButton;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource().equals(getVisualizeObsButton())) {
                opResult = adapter.doOperation(getObsOp, paramCon);

                setReturnVal(VISUALIZE_OPTION);
            }
            else if (e.getSource().equals(getSendButton())) {
                opResult = adapter.doOperation(getObsOp, paramCon);

                String getObsDoc = IOHelper.readText(opResult.getIncomingResultAsStream());

                setReturnVal(APPROVE_OPTION);

                ShowXMLDocDialog showXMLDialog = new ShowXMLDocDialog(getOwner().getLocation(),
                                                                      "GetObservation Response",
                                                                      getObsDoc);
                showXMLDialog.setVisible(true);
            }
        }
        catch (OXFException ex) {
            if (ex.getCause() instanceof ConnectException) {
                JOptionPane.showMessageDialog(this,
                                              "Could not connect to service!",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
            }
            setReturnVal(ERROR_OPTION);
            ex.printStackTrace();
        }
        catch (ExceptionReport ex) {
            setReturnVal(ERROR_OPTION);
            ex.printStackTrace();
        }
        catch (IOException ex) {
            setReturnVal(ERROR_OPTION);
            ex.printStackTrace();
        }

        dispose();
    }

    /**
     * 
     * @return
     */
    public OperationResult getOperationResult() {
        return opResult;
    }

}