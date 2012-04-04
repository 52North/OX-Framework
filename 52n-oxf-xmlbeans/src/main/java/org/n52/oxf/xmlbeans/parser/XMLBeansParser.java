/**
 * Copyright (C) 2012
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
package org.n52.oxf.xmlbeans.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.n52.oxf.xmlbeans.parser.XMLHandlingException.XMLHandlingExceptionCollection;
import org.n52.oxf.xmlbeans.parser.sosexample.ExceptionCode;
import org.n52.oxf.xmlbeans.parser.sosexample.OwsExceptionReport;
import org.w3c.dom.Node;

/**
 * Use this class when validating XML contents.
 * Several static methods are provided to parse XML
 * from {@link String} or {@link InputStream}.
 * 
 * 
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * @author Matthes Rieke <m.rieke@52north.org>
 * @author Carsten Hollmann <c.hollmann@52north.org>
 * 
 */
public class XMLBeansParser {
	
	/*
	 * use this list to define special validation cases (mostly substitution groups)
	 */
	private static List<LaxValidationCase> laxValidationCases;

	static {
		laxValidationCases = new ArrayList<LaxValidationCase>();
	}
	
	public static void registerLaxValidationCase(LaxValidationCase lvc) {
		laxValidationCases.add(lvc);
	}

	/**
	 * Reads the given source. The source may be only the xml-document or
	 * contain an application/x-www-form-url encoded string. In this case, the
	 * request must have the form <em>request=</em>
	 * 
	 * @param source The xml source.
	 * @return The parsed xbeans XmlObject
	 * @throws XMLHandlingException thrown if the XML is incorrect
	 */
	public static XmlObject parse(String source) throws XMLHandlingException {
		return parse(source, true);
	}

	/**
	 * Reads the given source. The source may be only the xml-document or
	 * contain an application/x-www-form-url encoded string. In this case, the
	 * request must have the form <em>request=</em>
	 * 
	 * @param source The xml source.
	 * @param validate Validate the source?
	 * @return The parsed xbeans XmlObject
	 * @throws XMLHandlingException thrown if the XML is incorrect
	 */
	public static XmlObject parse(String source, boolean validate) throws XMLHandlingException {

		XmlObject doc;
		try {

			doc = XmlObject.Factory.parse(source);
			if (validate) {
				validate(doc);
			}
			return doc;

		} catch (XmlException e) {
			throw new XMLHandlingException("Cannot parse xml: "+e.getMessage(), e);
		}
	}

	
	/**
	 * @param resourceAsStream the xml source as stream
	 * @return The parsed xbeans XmlObject
	 * @throws XMLHandlingException thrown if the XML is incorrect
	 */
	public static XmlObject parse(InputStream resourceAsStream) throws XMLHandlingException {
		return parse(resourceAsStream, true);
	}

	/**
	 * @param resourceAsStream The source as a stream.
	 * @param validate Validate the source?
	 * @return The parsed xbeans XmlObject
	 * @throws XMLHandlingException thrown if the XML is incorrect
	 */
	public static XmlObject parse(InputStream resourceAsStream, boolean validate) throws XMLHandlingException {
		XmlObject doc;
		try {
			doc = XmlObject.Factory.parse(resourceAsStream);

			if (validate) {
				validate(doc);
			}
			return doc;

		} catch (XmlException e) {
			
			/* cannot parse xml string. Maybe a stream problem? try to read as String!
			 * This has been implemented because of XmlBeans stream issues. */
			BufferedReader b = new BufferedReader(new InputStreamReader(resourceAsStream));
			
			StringWriter w = new StringWriter();			
			try {
				while(b.ready()) {
					w.write(b.readLine());
				}
			} catch (IOException e2) {
				throw new XMLHandlingException("Cannot read the document: Transmission interrupted!", e);
			}
			
			try {
				return XmlObject.Factory.parse(w.toString());
			} catch (XmlException e1) {
				throw new XMLHandlingException("The document you supplied was incomplete. Please try again.", e);
			}
			
		} catch (IOException e) {
			throw new XMLHandlingException("Cannot read the document: Transmission interrupted!", e);
		}
	}

	/**
	 * Reads the given source. The source may be only the xml-document or
	 * contain an application/x-www-form-url encoded string. In this case, the
	 * request must have the form <em>request=</em>
	 * 
	 * @param xmlnode The xml source.
	 * @param validate Validate the source?
	 * @return The parsed xbeans XmlObject
	 * @throws XMLHandlingException thrown if the XML is incorrect
	 */
	public static XmlObject parse(Node xmlnode, boolean validate) throws XMLHandlingException {
		XmlObject doc;
		try {
			doc = XmlObject.Factory.parse(xmlnode);

			if (validate) {
				validate(doc);
			}
			return doc;

		} catch (XmlException e) {
			throw new XMLHandlingException("Cannot read the node", e);
		}
	}
	
	/**
	 * Validates an xml doc. If the validation fails, the exception contains a
	 * detailed list of errors.
	 * 
	 * @param doc the document to validate
	 * @throws XMLHandlingException thrown if the XML is incorrect
	 */
	public static void validate(XmlObject doc) throws XMLHandlingException {
		List<XmlError> validationErrors = new ArrayList<XmlError>();
		XmlOptions validationOptions = new XmlOptions();
		validationOptions.setErrorListener(validationErrors);

		boolean isValid = doc.validate(validationOptions);

		// create XmlException with error-message if the XML is invalid.
		if (!isValid) {
			throw new XMLHandlingException("Invalid xml content\n", validationErrors);
		}
	}
	
	/**
	 * Validates an xml doc. If the validation fails, the exception contains a
	 * detailed list of errors.
	 * 
	 * @param doc the document to validate
	 * @throws XMLHandlingException thrown if the XML is incorrect
	 */
	public static void laxValidate(XmlObject doc) throws XMLHandlingException {

		// Create an XmlOptions instance and set the error listener.
		ArrayList<XmlError> validationErrors = new ArrayList<XmlError>();
		XmlOptions validationOptions = new XmlOptions();
		validationOptions.setErrorListener(validationErrors);

		// Validate the GetCapabilitiesRequest XML document
		boolean isValid = doc.validate(validationOptions);

		// Create Exception with error message if the xml document is invalid
		if (!isValid) {

			/*
			 * check if we have special validation cases which could
			 * let the message pass anyhow
			 */
			List<XmlError> errors = new ArrayList<XmlError>();
			for (XmlError error : validationErrors) {
				for (LaxValidationCase lvc : laxValidationCases) {
					if (!lvc.shouldPass((XmlValidationError) error)) {
						errors.add(error);
					}
				}	
			}
			
			if (!errors.isEmpty()) {
				/*
				 * throw the errors for enabling improved exception
				 * handling in calling methods
				 */
				throw new XMLHandlingException.XMLHandlingExceptionCollection(errors);
			}
		}
	}

	public void sosValidateExample(XmlObject xb_doc) throws OwsExceptionReport {
		/*
		 * this is just an example :-)
		 */
		registerLaxValidationCase(new GMLAbstractFeatureCase());

		try {
			laxValidate(xb_doc);
		} catch (XMLHandlingException e) {
			/*
			 * "business" logic ok? I guess so, because it is only
			 * doing exception-related handling. 
			 */
			String message = null;
			String parameterName = null;
			
			List<XMLHandlingException> exs;
			if (e instanceof XMLHandlingExceptionCollection) {
				/*
				 * multiple exceptions
				 */
				exs = ((XMLHandlingExceptionCollection) e).getInnerExceptions();
			} else {
				/*
				 * one exception
				 */
				exs = new ArrayList<XMLHandlingException>(1);
				exs.add(e);
			}
			
			for (XMLHandlingException error : exs) {
				// ExceptionCode for Exception
				ExceptionCode exCode = null;

				// get name of the missing or invalid parameter
				message = error.getMessage();
				if (message != null) {

					// check, if parameter is missing or value of parameter
					// is
					// invalid to ensure, that correct
					// exceptioncode in exception response is used

					// invalid parameter value
					if (message.startsWith("The value")) {
						exCode = ExceptionCode.InvalidParameterValue;

						// split message string to get attribute name
						String[] messAndAttribute = message
								.split("attribute '");
						if (messAndAttribute.length == 2) {
							parameterName = messAndAttribute[1]
									.replace("'", "");
						}
					}

					// invalid enumeration value --> InvalidParameterValue
					else if (message.contains("not a valid enumeration value")) {
						exCode = ExceptionCode.InvalidParameterValue;

						// get attribute name
						String[] messAndAttribute = message.split(" ");
						parameterName = messAndAttribute[10];
					}

					// mandatory attribute is missing -->
					// missingParameterValue
					else if (message.startsWith("Expected attribute")) {
						exCode = ExceptionCode.MissingParameterValue;

						// get attribute name
						String[] messAndAttribute = message
								.split("attribute: ");
						if (messAndAttribute.length == 2) {
							String[] attrAndRest = messAndAttribute[1]
									.split(" in");
							if (attrAndRest.length == 2) {
								parameterName = attrAndRest[0];
							}
						}
					}

					// mandatory element is missing -->
					// missingParameterValue
					else if (message.startsWith("Expected element")) {
						exCode = ExceptionCode.MissingParameterValue;

						// get element name
						String[] messAndElements = message.split(" '");
						if (messAndElements.length >= 2) {
							String elements = messAndElements[1];
							if (elements.contains("offering")) {
								parameterName = "offering";
							} else if (elements.contains("observedProperty")) {
								parameterName = "observedProperty";
							} else if (elements.contains("responseFormat")) {
								parameterName = "responseFormat";
							} else if (elements.contains("procedure")) {
								parameterName = "procedure";
							} else if (elements.contains("featureOfInterest")) {
								parameterName = "featureOfInterest";
							} else {
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
							} else if (elements.contains("observedProperty")) {
								parameterName = "observedProperty";
							} else if (elements.contains("responseFormat")) {
								parameterName = "responseFormat";
							} else if (elements.contains("procedure")) {
								parameterName = "procedure";
							} else if (elements.contains("featureOfInterest")) {
								parameterName = "featureOfInterest";
							} else {
								// TODO check if other elements are invalid
							}
						}
					} else {
						// create service exception
						OwsExceptionReport se = new OwsExceptionReport();
						se.addCodedException(ExceptionCode.InvalidRequest,
								null, "[XmlBeans validation error:] " + message);
//						LOGGER.error("The request is invalid!", se);
						throw se;
					}

					// create service exception
					OwsExceptionReport se = new OwsExceptionReport();
					se.addCodedException(exCode, parameterName,
							"[XmlBeans validation error:] " + message);
//					LOGGER.error("The request is invalid!", se);
					throw se;
				}
			}
		}
	}
	
}
