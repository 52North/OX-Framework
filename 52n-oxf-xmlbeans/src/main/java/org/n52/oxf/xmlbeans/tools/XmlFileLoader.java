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
package org.n52.oxf.xmlbeans.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;

public class XmlFileLoader {

    /**
     * Reads a SOAP XML file and parses its body into an {@link XmlObject}.
     *
     * @param file
     *        file name of the file to parse
     * @param nodeName
     *        name of the node contained by the SOAP body
     * @return an XmlBeans {@link XmlObject} representation of the XML file
     * @throws XmlException
     *         if parsing to XML fails
     * @throws IOException
     *         if file could not be read
     */
    public static XmlObject loadSoapBodyFromXmlFileViaClassloader(String filePath, String nodeName, Class< ? > clazz) throws XmlException, IOException {
        // TODO get rid of Soap XML Binding => use java API and XML Tools
        EnvelopeDocument envelope = (EnvelopeDocument) loadXmlFileViaClassloader(filePath, clazz);
        return SoapUtil.readBodyNodeFrom(envelope, nodeName);
    }

    /**
     * Loads XML files which can be found via the <code>clazz</code>'s {@link ClassLoader}. If not found the
     * {@link FileContentLoader}'s {@link ClassLoader} is asked to load the file. If file could not be found
     * an exception is thrown.
     *
     * @param filePath
     *        the path to the file to be loaded.
     * @param clazz
     *        the class which {@link ClassLoader} to be used.
     * @return an XmlObject of the loaded file.
     * @throws XmlException
     *         if file could not be parsed into XML
     * @throws IOException
     *         if file could not be read.
     * @throws IllegalArgumentException
     *         if file path is <code>null</code> or empty
     * @throws FileNotFoundException
     *         if the resource could not be found be the <code>clazz</code>'s {@link ClassLoader}
     */
    public static XmlObject loadXmlFileViaClassloader(String filePath, Class< ? > clazz) throws XmlException,
            IOException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("Check file path: '" + filePath + "'.");
        }
        InputStream is = clazz.getResourceAsStream(filePath);
        if (is == null) {
            is = XmlFileLoader.class.getResourceAsStream(filePath);
            if (is == null) {
                throw new FileNotFoundException("The resource at '" + filePath + "' cannot be found.");
            }
        }
        return XmlObject.Factory.parse(is);
    }

}
