/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
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
/**********************************************************************************
 Copyright (C) 2007 by 52 North Initiative for Geospatial Open Source Software GmbH

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

 Created on: 22.06.2005
 *********************************************************************************/

package org.n52.oxf.adapter;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


import org.apache.commons.io.input.AutoCloseInputStream;

/**
 * Encapsulates the result of an Operation of a ServiceAdapter. It contains the inputStream and the request
 * the adapter had sent.
 *
 * @author <a href="mailto:foerster@52north.org">Theodor F&ouml;rster</a>
 */
public class OperationResult {

    protected static final int DEFAULT_BUFFER_SIZE = 128000;

    protected byte[] incomingResult;

    protected String sendedRequest;

    protected ParameterContainer usedParameters;

    /**
     * @param incomingResult
     *        InputStream coming as the result from the service.
     * @param usedParameters
     *        ParameterContainer containing the Parameters from which the request has been build.
     * @param sendedRequest
     *        the request-string sended to the service.
     * @throws IOException
     */
    public OperationResult(InputStream incomingStream, ParameterContainer usedParameters, String sendedRequest) throws IOException {
        this.sendedRequest = sendedRequest;
        this.usedParameters = usedParameters;

        setIncomingResult(incomingStream, DEFAULT_BUFFER_SIZE);
    }

    /**
     * reads the incomingStream and stores it in the {@link #incomingResult} byte array.
     *
     * @param incomingStream
     * @param bufferSize
     * @throws IOException
     */
    protected void setIncomingResult(InputStream incomingStream, int bufferSize) throws IOException {
        // DataInputStream in = new DataInputStream(incomingStream);
		ByteArrayOutputStream bufferOutputStream = new ByteArrayOutputStream();
		int read;
		byte[] data = new byte[16384];

		while ((read = incomingStream.read(data, 0, data.length)) != -1) {
			bufferOutputStream.write(data, 0, read);
		}

		bufferOutputStream.flush();
		bufferOutputStream.close();
		incomingResult = bufferOutputStream.toByteArray();
    }

    public byte[] getIncomingResult() {
        return incomingResult;
    }

    /**
     *
     * @return
     * @deprecated use OperationResult#getIncomingResultAsAutoCloseStream() instead
     */
    @Deprecated
    public ByteArrayInputStream getIncomingResultAsStream() {
        return new ByteArrayInputStream(incomingResult);
    }

    public InputStream getIncomingResultAsAutoCloseStream() {
        return new AutoCloseInputStream(new ByteArrayInputStream(incomingResult));
    }

    public String getSendedRequest() {
        return sendedRequest;
    }

    public ParameterContainer getUsedParameters() {
        return usedParameters;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OperationResult:[\nDEFAULT_BUFFER_SIZE: ");
        sb.append(DEFAULT_BUFFER_SIZE);
        sb.append(",\nincomingResult: ");
        String incoming = new String(getIncomingResult());
        sb.append(incoming);
        sb.append(",\nsendedRequest: ");
        sb.append(sendedRequest);
        sb.append(",\nusedParameters: ");
        sb.append(usedParameters);
        sb.append("]");

        return sb.toString();
    }
}