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
package org.n52.oxf.ows;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an Exception occuring while service side execution.
 * <br/>
 * use either one of the provided exception codes or define your own ones.
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class OWSException extends Exception {
	
    public static final String OPERATION_NOT_SUPPORTED = "OperationNotSupported";
    public static final String MISSING_PARAMETER_VALUE = "MissingParameterValue";
    public static final String INVALID_PARAMTER_VALUE = "InvalidParameterValue";
    public static final String VERSION_NEGOTIATION_FOUND = "VersionNegotiationFound";
    public static final String INVALID_UPDATE_SEQUENCE = "InvalidUpdateSequence";
    public static final String NO_APPLICABLE_CODE = "NoApplicableCode";
    
    private String[] exceptionTexts;
    private String sentRequest;
    private String exceptionCode;
    private String locator;

    /**
     * 
     * @param exceptionTexts
     * @param exceptionCode
     * @param sentRequest
     */
	public OWSException(String[] exceptionTexts, String exceptionCode, String sentRequest) {
        super(exceptionCode);
		this.exceptionCode = exceptionCode;
		this.exceptionTexts = exceptionTexts;
        this.sentRequest = sentRequest;
	}
    
    /**
     * 
     * @param exceptionTexts
     * @param exceptionCode
     * @param sentRequest
     * @param locator
     */
    public OWSException(String[] exceptionTexts, String exceptionCode, String sentRequest, String locator) {
        super(exceptionCode);
        this.exceptionCode = exceptionCode;
        this.exceptionTexts = exceptionTexts;
        this.sentRequest = sentRequest;
        this.locator = locator;
    }
    
    /**
     * 
     */
    @Override
    public void printStackTrace(PrintStream s) {
        s.println("");
        s.println("Exception: " + exceptionCode);
        s.println("Locator: " + locator);
        
        for(String excTxt : exceptionTexts) {
            s.println("Exception text: " + excTxt);
        }
        
        s.println("Sent request was:");
        s.println(sentRequest);
        
        super.printStackTrace(s);
    }
    
    /**
     * @return a HTML representation of this OWSException
     */
    public String toHtmlString() {
        String res = "";
        res += "<b>Exception:</b> " + exceptionCode + "<br>";
        res += "<b>Locator:</b> " + locator + "<br>";
        
        for(String excTxt : exceptionTexts) {
            res += "<b>Exception text:</b> " + excTxt + "<br>";
        }
        
        res += "<b>Sendt request was:</b>" + "<br>";
        res += "<code>" + replaceTagBrackets(sentRequest) + "</code>";
        
        return res;
    }
    
    /**
     * replaces the '<' and '>' characters in a String through '&lt;' and '&gt;'.
     */
    private String replaceTagBrackets (String xmlString) {
        String res = xmlString.replaceAll("<", "&lt;");
        res = res.replaceAll(">", "&gt;");
        
        return res;
    }
    
	/**
	 * @return Returns the exceptionCode.
	 */
	public String getExceptionCode() {
		return exceptionCode;
	}

	/**
	 * @return Returns the exceptionTexts.
	 */
	public String[] getExceptionTexts() {
		return exceptionTexts;
	}

	public String getSentRequest() {
        return sentRequest;
    }

    /**
	 * indicates in which part the exception occured. This is optional!
	 * @return Returns the locator.
	 */
	public String getLocator() {
		return locator;
	}

	/**
	 * indicates in which part the exception occured. This is optional!
	 * @param locator The locator to set.
	 */
	public void setLocator(String locator) {
		this.locator = locator;
	}
}