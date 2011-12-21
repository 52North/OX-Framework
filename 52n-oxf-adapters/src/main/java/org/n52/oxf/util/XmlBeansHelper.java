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
 
 Created on: 22.07.2007
 *********************************************************************************/

package org.n52.oxf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.opengis.ows.x11.ExceptionType;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlTokenSource;
import org.apache.xmlbeans.XmlValidationError;
import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.OwsExceptionReport;
import org.n52.oxf.owsCommon.OwsExceptionReport.ExceptionCode;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * (this class is not part of the core because it depends on XMLBeans)
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class XmlBeansHelper {

    private static final String XML_OPTIONS_CHARACTER_ENCODING = "UTF-8";

    public static final String OM_1_0_NAMESPACE_URI = "http://www.opengis.net/om/1.0";

    public static final String OM_NAMESPACE_PREFIX = "om";

    public static final String SOS_1_0_NAMESPACE_URI = "http://www.opengis.net/sos/1.0";

    public static final String SOS_NAMESPACE_PREFIX = "sos";

    public static final String OGC_NAMESPACE_URI = "http://www.opengis.net/ogc";

    public static final String OGC_NAMESPACE_PREFIX = "ogc";

    public static final String SWE_1_0_1_NAMESPACE_URI = "http://www.opengis.net/swe/1.0.1";

    public static final String SWE_NAMESPACE_PREFIX = "swe";

    public static final String SA_1_0_NAMESPACE_URI = "http://www.opengis.net/sampling/1.0";

    public static final String SA_NAMESPACE_PREFIX = "sa";

    public static final String GML_NAMESPACE_URI = "http://www.opengis.net/gml";

    public static final String GML_NAMESPACE_PREFIX = "gml";

    public static final String SML_1_0_1_NAMESPACE_URI = "http://www.opengis.net/sensorML/1.0.1";

    public static final String SML_NAMESPACE_PREFIX = "sml";

    public static final String XSI_NAMESPACE_URI = "http://www.w3.org/2001/XMLSchema-instance";

    public static final String XSI_NAMESPACE_PREFIX = "xsi";

    private static Logger LOGGER = LoggingHandler.getLogger(XmlBeansHelper.class);

    /**
     * help-method that formats a XmlTokenString into a String.
     * 
     * @param unformattedRequestDocument
     * @return
     */
    public static String formatStringRequest(XmlTokenSource unformattedRequestDocument) {
        XmlOptions xmlOpts = xmlOptionsForNamespaces();

        return formatStringRequest(unformattedRequestDocument, xmlOpts);
    }

    /**
     * help-method that formats a XmlTokenString into a String.
     * 
     * @param unformattedRequestDocument
     * @param xmlOpts
     * @return
     */
    public static String formatStringRequest(XmlTokenSource unformattedRequestDocument, XmlOptions xmlOpts) {
        xmlOpts.setSavePrettyPrint();
        xmlOpts.setSavePrettyPrintIndent(5);
        xmlOpts.setCharacterEncoding("UTF-8");
        String s = unformattedRequestDocument.xmlText(xmlOpts);

        // this character replacement must be carried out for a nice format of
        // the XML-String
        return s.replace("\n     ", "");
    }

    /**
     * Adds the default Namespaces to the target XML Object
     * 
     * Use of xmlOptions recommended (see xmlOptionsForNamespaces())!
     * 
     * @param target
     *        XML Object the Namespaces should be added to
     */
    @Deprecated
    public static void addDefaultNS(XmlTokenSource target) {
        // TODO: Make this dynamic to use this function from every builder
        XmlCursor obsCursor = target.newCursor();
        obsCursor.toFirstContentToken();
        obsCursor.insertNamespace(OM_NAMESPACE_PREFIX, OM_1_0_NAMESPACE_URI);
        obsCursor.insertNamespace(SA_NAMESPACE_PREFIX, SA_1_0_NAMESPACE_URI);
        obsCursor.insertNamespace(SWE_NAMESPACE_PREFIX, SWE_1_0_1_NAMESPACE_URI);
        obsCursor.insertNamespace(SOS_NAMESPACE_PREFIX, SOS_1_0_NAMESPACE_URI);
        obsCursor.insertNamespace(GML_NAMESPACE_PREFIX, GML_NAMESPACE_URI);
        obsCursor.insertNamespace(SML_NAMESPACE_PREFIX, SML_1_0_1_NAMESPACE_URI);
    }

    /**
     * gives {@link XmlOptions} to set the default namespaces via {@link XmlObject}.xmlText(setOptions()).
     * 
     * @return
     */
    public static XmlOptions xmlOptionsForNamespaces() {
        XmlOptions options = new XmlOptions();
        options.setCharacterEncoding(XML_OPTIONS_CHARACTER_ENCODING);

        options.setUseDefaultNamespace();
        options.setSaveNamespacesFirst();
        options.setSavePrettyPrint();
        options.setSaveAggressiveNamespaces();

        HashMap<String, String> suggestedPrefixes = new HashMap<String, String>();
        suggestedPrefixes.put(SOS_1_0_NAMESPACE_URI, SOS_NAMESPACE_PREFIX);
        suggestedPrefixes.put(SWE_1_0_1_NAMESPACE_URI, SWE_NAMESPACE_PREFIX);
        suggestedPrefixes.put(OM_1_0_NAMESPACE_URI, OM_NAMESPACE_PREFIX);
        suggestedPrefixes.put(SA_1_0_NAMESPACE_URI, SA_NAMESPACE_PREFIX);
        suggestedPrefixes.put(GML_NAMESPACE_URI, GML_NAMESPACE_PREFIX);
        suggestedPrefixes.put(SML_1_0_1_NAMESPACE_URI, SML_NAMESPACE_PREFIX);
        suggestedPrefixes.put(XSI_NAMESPACE_URI, XSI_NAMESPACE_PREFIX);
        options.setSaveSuggestedPrefixes(suggestedPrefixes);

        return options;
    }

    /**
     * checks whether the getObservation XMLDocument is valid and adds the {@link ExceptionCode} that matches
     * the reported {@link XmlValidationError} to the returned {@link OwsExceptionReport}.
     * 
     * @param xb_doc
     *        the document which should be checked
     * 
     * @throws OwsExceptionReport
     *         if the Document is not valid
     */
    public static void validateDocument(XmlObject xb_doc) throws OwsExceptionReport {

        // Create an XmlOptions instance and set the error listener.
        ArrayList<XmlError> validationErrors = new ArrayList<XmlError>();
        XmlOptions validationOptions = new XmlOptions();
        validationOptions.setErrorListener(validationErrors);

        // Validate the GetCapabilitiesRequest XML document
        boolean isValid = xb_doc.validate(validationOptions);

        // Create Exception with error message if the xml document is invalid
        if ( !isValid) {

            String message = null;
            String parameterName = null;

            // getValidation error and throw service exception for the first
            // error
            Iterator<XmlError> iter = validationErrors.iterator();
            while (iter.hasNext()) {

                // ExceptionCode for Exception
                ExceptionCode exCode = null;

                // get name of the missing or invalid parameter
                XmlError error = iter.next();
                message = error.getMessage();

                // TODO implement with error.getErrorCode(); - see:
                // http://xmlbeans.apache.org/docs/2.4.0/reference/index.html
                // (maybe instead of message?)

                if (message != null) {

                    // check, if parameter is missing or value of parameter is
                    // invalid to ensure, that correct
                    // exceptioncode in exception response is used

                    // invalid parameter value
                    if (message.startsWith("The value")) {
                        exCode = ExceptionCode.InvalidParameterValue;

                        // split message string to get attribute name
                        String[] messAndAttribute = message.split("attribute '");
                        if (messAndAttribute.length == 2) {
                            parameterName = messAndAttribute[1].replace("'", "");
                        }
                    }

                    // invalid enumeration value --> InvalidParameterValue
                    else if (message.contains("not a valid enumeration value")) {
                        exCode = ExceptionCode.InvalidParameterValue;

                        // get attribute name
                        String[] messAndAttribute = message.split(" ");
                        parameterName = messAndAttribute[10];
                    }

                    // mandatory attribute is missing --> missingParameterValue
                    else if (message.startsWith("Expected attribute")) {
                        exCode = ExceptionCode.MissingParameterValue;

                        // get attribute name
                        String[] messAndAttribute = message.split("attribute: ");
                        if (messAndAttribute.length == 2) {
                            String[] attrAndRest = messAndAttribute[1].split(" in");
                            if (attrAndRest.length == 2) {
                                parameterName = attrAndRest[0];
                            }
                        }
                    }

                    // mandatory element is missing --> missingParameterValue
                    else if (message.startsWith("Expected element")) {
                        exCode = ExceptionCode.MissingParameterValue;

                        // get element name
                        String[] messAndElements = message.split(" '");
                        if (messAndElements.length >= 2) {
                            String elements = messAndElements[1];
                            if (elements.contains("offering")) {
                                parameterName = "offering";
                            }
                            else if (elements.contains("observedProperty")) {
                                parameterName = "observedProperty";
                            }
                            else if (elements.contains("responseFormat")) {
                                parameterName = "responseFormat";
                            }
                            else if (elements.contains("procedure")) {
                                parameterName = "procedure";
                            }
                            else if (elements.contains("featureOfInterest")) {
                                parameterName = "featureOfInterest";
                            }
                            else {
                                // TODO check if other elements are invalid
                            }
                        }
                    }
                    // invalidParameterValue
                    else if (message.startsWith("Element")) {
                        exCode = ExceptionCode.InvalidParameterValue;

                        // get element name
                        String[] messAndElements = message.split(" '");
                        if (messAndElements.length >= 2) {
                            String elements = messAndElements[1];
                            if (elements.contains("offering")) {
                                parameterName = "offering";
                            }
                            else if (elements.contains("observedProperty")) {
                                parameterName = "observedProperty";
                            }
                            else if (elements.contains("responseFormat")) {
                                parameterName = "responseFormat";
                            }
                            else if (elements.contains("procedure")) {
                                parameterName = "procedure";
                            }
                            else if (elements.contains("featureOfInterest")) {
                                parameterName = "featureOfInterest";
                            }
                            else {
                                // TODO check if other elements are invalid
                            }
                        }
                    }
                    else {
                        // create service exception
                        OwsExceptionReport se = new OwsExceptionReport();
                        se.addCodedException(ExceptionCode.InvalidRequest, null, "[XmlBeans validation error:] "
                                + message);
                        String shortName = xb_doc.schemaType().getShortJavaName();
                        LOGGER.error("The request >>>" + shortName + "<<< is invalid!", se);
                        throw se;
                    }

                    // create service exception
                    OwsExceptionReport se = new OwsExceptionReport();
                    se.addCodedException(exCode, parameterName, "[XmlBeans validation error:] " + message);
                    String shortName = xb_doc.schemaType().getShortJavaName();
                    LOGGER.error("The request >>>" + shortName + "<<< is invalid!", se);
                    throw se;
                }

            }

        }
    }

    /**
     * This method uses validateDocument(...) to validate the given XmlObject and prints the returned
     * Exceptions to the console.
     * 
     * @param xmlObj
     */
    public static void validateToSystemOut(XmlObject xmlObj) {
        LOGGER.debug("VALIDATING DOCUMENT:");
        try {
            XmlBeansHelper.validateDocument(xmlObj);
        }
        catch (OwsExceptionReport e) {
            ArrayList<ExceptionType> exceptions = e.getExceptions();
            for (ExceptionType exception : exceptions) {
                System.out.println(exception.xmlText());
            }
        }
    }
    

    public static XmlObject parseXmlObjectFromDomNode(Node node, String name) throws OXFException {
        XmlObject xmlObject = null;
        try {
            boolean found = false;
            if (node.hasChildNodes()) {
                NodeList list = node.getChildNodes();
                for(int j = 0; j < list.getLength(); j++) {
                    Node child = list.item(j);
                    String localName = child.getLocalName();
                    if (child.getNodeType() == Node.ELEMENT_NODE && localName.equals(name)) {
                        xmlObject = XmlObject.Factory.parse(child);
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                throw new OXFException("Could not find '" + name + "' in DOM node.");
            }
        }
        catch (XmlException e) {
            throw new OXFException("Could parse '" + name + "'.", e);
        }
        return xmlObject;
    }

}