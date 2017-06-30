/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.oxf.ows;

import java.io.PrintStream;

/**
 * Represents an Exception occuring while service side execution.
 * <br>
 * use either one of the provided exception codes or define your own ones.
 *
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class OWSException extends Exception {

	private static final long serialVersionUID = 1L;
	public static final String OPERATION_NOT_SUPPORTED = "OperationNotSupported";
    public static final String MISSING_PARAMETER_VALUE = "MissingParameterValue";
    public static final String INVALID_PARAMTER_VALUE = "InvalidParameterValue";
    public static final String VERSION_NEGOTIATION_FOUND = "VersionNegotiationFound";
    public static final String INVALID_UPDATE_SEQUENCE = "InvalidUpdateSequence";
    public static final String NO_APPLICABLE_CODE = "NoApplicableCode";

    private final String[] exceptionTexts;
    private final String sentRequest;
    private final String exceptionCode;
    private String locator;

	public OWSException(String[] exceptionTexts, String exceptionCode, String sentRequest) {
        super(exceptionCode);
		this.exceptionCode = exceptionCode;
        if (exceptionTexts != null) {
            this.exceptionTexts = exceptionTexts.clone();
        } else {
            this.exceptionTexts = new String[0];
        }
        this.sentRequest = sentRequest;
	}

    public OWSException(String[] exceptionTexts, String exceptionCode, String sentRequest, String locator) {
        super(exceptionCode);
        this.exceptionCode = exceptionCode;
        if (exceptionTexts != null) {
            this.exceptionTexts = exceptionTexts.clone();
        } else {
            this.exceptionTexts = new String[0];
        }
        this.sentRequest = sentRequest;
        this.locator = locator;
    }

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

        StringBuilder sb = new StringBuilder(res);
        for(String excTxt : exceptionTexts) {
            sb.append("<b>Exception text:</b> ").append(excTxt).append("<br>");
        }

        res = sb.toString();
        res += "<b>Sendt request was:</b>" + "<br>";
        res += "<code>" + replaceTagBrackets(sentRequest) + "</code>";

        return res;
    }

    /**
     * Replaces the '&lt;' and '&gt;' characters in a String through '&amp;lt;' and '&amp;gt;'.
     */
    private String replaceTagBrackets (String xmlString) {
        String res = xmlString.replaceAll("<", "&lt;");
        res = res.replaceAll(">", "&gt;");

        return res;
    }

	public String getExceptionCode() {
		return exceptionCode;
	}

	public String[] getExceptionTexts() {
		return exceptionTexts == null? null : exceptionTexts.clone();
	}

	public String getSentRequest() {
        return sentRequest;
    }

    /**
	 * Indicates in which part the exception occurred. This is optional!
	 *
	 * @return Returns the locator.
	 */
	public String getLocator() {
		return locator;
	}

	/**
	 * Indicates in which part the exception occurred. This is optional!
	 *
	 * @param locator The locator to set.
	 */
	public void setLocator(String locator) {
		this.locator = locator;
	}
}
