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
 
 Created on: 11.06.2006
 *********************************************************************************/


package org.n52.oxf.ui.swing;

import java.awt.*;
import javax.swing.*;

/**
 * 
 * Sample code for how to use the ApprovalDialog:
 * 
 * <pre>
 * ApprovalDialog approvalDialg = new ApprovalDialog();
 * int returnVal = approvalDialg.showDialog();
 * 
 * if (returnVal == ApprovalDialog.APPROVE_OPTION) {
 *     ...
 * }
 * </pre>
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public abstract class ApprovalDialog extends JDialog {

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

    
    private int returnVal = ERROR_OPTION;
    


    public ApprovalDialog() {
        super();
        
        setModal(true);
    }

    public ApprovalDialog(Dialog owner) throws HeadlessException {
        super(owner, true);
    }

    public ApprovalDialog(Dialog owner, String title) throws HeadlessException {
        super(owner, title, true);
    }

    public ApprovalDialog(Frame owner, String title) throws HeadlessException {
        super(owner, title, true);
    }

    public ApprovalDialog(Frame owner) throws HeadlessException {
        super(owner, true);
    }



    public int getReturnVal() {
        return returnVal;
    }

    public void setReturnVal(int returnVal) {
        this.returnVal = returnVal;
    }

    /**
     * Pops up an ApprovalDialog.
     * 
     * @return - the return state of the ApprovalDialog on popdown:
     *         <li>ApprovalDialog.CANCEL_OPTION
     *         <li>ApprovalDialog.APPROVE_OPTION
     *         <li>ApprovalDialog.ERROR_OPTION if an error occurs or the dialog is dismissed
     */
    public int showDialog() {
        setVisible(true);
        dispose();

        return getReturnVal();
    }
}