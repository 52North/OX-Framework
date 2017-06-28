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
package org.n52.oxf.xmlbeans.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
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
	private static boolean validationGlobally = true;

	static {
		laxValidationCases = new ArrayList<LaxValidationCase>();
	}

	/**
	 * Use this method to set the state of the validation flag.
	 */
	public static void setValidationGloballyEnabled(final boolean b) {
		validationGlobally = b;
	}

	/**
	 * Register a new {@link LaxValidationCase} which should
	 * let pass corresponding "invalid" documents.
	 *
	 * @param lvc a new lax case
	 */
	public static void registerLaxValidationCase(final LaxValidationCase lvc) {
		laxValidationCases.add(lvc);
	}

	/**
	 * Returns the list of currently registered {@link LaxValidationCase}s
	 * @return a List<LaxValidationCase> containing the currently registered {@link LaxValidationCase}s
	 */
	public static List<LaxValidationCase> getRegisteredLaxValidationCases() {
		return laxValidationCases;
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
	public static XmlObject parse(final String source) throws XMLHandlingException {
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
	public static XmlObject parse(final String source, final boolean validate) throws XMLHandlingException {
		XmlObject doc;
		try {
			doc = XmlObject.Factory.parse(source);
		} catch (final XmlException e) {
			throw new XMLHandlingException("Cannot parse xml: "+e.getMessage(), e);
		}

		if (validate) {
			validateOnParse(doc);
		}
		return doc;
	}


	/**
	 * @param resourceAsStream the xml source as stream
	 * @return The parsed xbeans XmlObject
	 * @throws XMLHandlingException thrown if the XML is incorrect
	 */
	public static XmlObject parse(final InputStream resourceAsStream) throws XMLHandlingException {
		return parse(resourceAsStream, true);
	}


	/**
	 * @param resourceAsStream The source as a stream.
	 * @param validate Validate the source?
	 * @return The parsed xbeans XmlObject
	 * @throws XMLHandlingException thrown if the XML is incorrect
	 */
	public static XmlObject parse(final InputStream resourceAsStream, final boolean validate) throws XMLHandlingException {
		XmlObject doc;
		try {
			doc = XmlObject.Factory.parse(resourceAsStream);
		} catch (final XmlException e) {
			/* cannot parse xml string. Maybe a stream problem? try to read as String!
			 * This has been implemented because of XmlBeans stream issues. */
			doc = parseAsStringDueToXmlBeansStreamIssues(resourceAsStream, e);
		} catch (final IOException e) {
			throw new XMLHandlingException("Cannot read the document: Transmission interrupted!", e);
		}

		if (validate) {
			validateOnParse(doc);
		}
		return doc;
	}

	private static XmlObject parseAsStringDueToXmlBeansStreamIssues(
			final InputStream resourceAsStream, final XmlException e)
			throws XMLHandlingException {
		final BufferedReader b = new BufferedReader(new InputStreamReader(resourceAsStream));

		final StringWriter w = new StringWriter();
		try {
			while(b.ready()) {
				w.write(b.readLine());
			}
		} catch (final IOException e2) {
			throw new XMLHandlingException("Cannot read the document: Transmission interrupted!", e);
		}

		try {
			return XmlObject.Factory.parse(w.toString());
		} catch (final XmlException e1) {
			throw new XMLHandlingException("The document you supplied was incomplete. Please try again.", e);
		}
	}

	/**
	 * Reads the given source. The source may be only the xml-document or
	 * contain an application/x-www-form-url encoded string. In this case, the
	 * request must have the form <em>request=</em>
	 *
	 * @param xmlnode The xml source.
	 * @return The parsed xbeans XmlObject
	 * @throws XMLHandlingException thrown if the XML is incorrect
	 */
	public static XmlObject parse(final Node xmlnode) throws XMLHandlingException {
		return parse(xmlnode, true);
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
	public static XmlObject parse(final Node xmlnode, final boolean validate) throws XMLHandlingException {
		XmlObject doc;
		try {
			doc = XmlObject.Factory.parse(xmlnode);
		} catch (final XmlException e) {
			throw new XMLHandlingException("Cannot read the node", e);
		}

		if (validate) {
			validateOnParse(doc);
		}

		return doc;
	}

	/**
	 * private helper method for throwing an exception if validation
	 * was used within a parsing request.
	 */
	private static void validateOnParse(final XmlObject doc) throws XMLHandlingException {
		if (!validationGlobally) {
			return;
		}
		final String errorString = createErrorMessage(validate(doc));
		if (errorString.length() > 0) {
			throw new XMLHandlingException(errorString);
		}
	}

	private static String createErrorMessage(final Collection<XmlError> errors) {
		final StringBuilder errorBuilder = new StringBuilder();
		for (final XmlError xmlError : errors) {
			errorBuilder.append(xmlError.getMessage()).append(";");
		}

		if (!errors.isEmpty()) {
			errorBuilder.deleteCharAt(errorBuilder.length() - 1);
		}
		return errorBuilder.toString();
	}

	/**
	 * Validates an xml doc. If the validation fails, the exception contains a
	 * detailed list of errors.
	 *
	 * @param doc the document to validate
	 * @throws XMLHandlingException thrown if the XML is incorrect
	 */
	public static void strictValidate(final XmlObject doc) throws XMLHandlingException {
		if (!validationGlobally) {
			return;
		}

		final List<XmlError> validationErrors = new ArrayList<XmlError>();
		final XmlOptions validationOptions = new XmlOptions();
		validationOptions.setErrorListener(validationErrors);

		final boolean isValid = doc.validate(validationOptions);

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
	public static Collection<XmlError> validate(final XmlObject doc) {
		final Set<XmlError> validationErrors = new HashSet<XmlError>();
		if (!validationGlobally) {
			return validationErrors;
		}

		// Create an XmlOptions instance and set the error listener.
		final List<XmlError> allValidationErrors = new ArrayList<XmlError>();
		final XmlOptions validationOptions = new XmlOptions();
		validationOptions.setErrorListener(allValidationErrors);

		// Validate the XML document
		final boolean isValid = doc.validate(validationOptions);

		// Create Exception with error message if the xml document is invalid
		if (!isValid) {

			/*
			 * check if we have special validation cases which could
			 * let the message pass anyhow
			 */
			filterValidationErrors(validationErrors, allValidationErrors);

		}
		return validationErrors;
	}

	private static void filterValidationErrors(final Set<XmlError> validationErrors, final List<XmlError> allValidationErrors) {
	    if (laxValidationCases.isEmpty()) {
            validationErrors.addAll(allValidationErrors);
            return;
        }

	    for (final XmlError validationError : allValidationErrors) {
	    	boolean shouldPass = false;
			for (final LaxValidationCase lvc : laxValidationCases) {
				if (lvc.shouldPass(validationError)) {
					shouldPass = true;
					break;
				} else if (lvc instanceof DependentLaxValidationCase &&
						((DependentLaxValidationCase)lvc).shouldPass(validationError,allValidationErrors)) {
					shouldPass = true;
					break;
				}
			}
			if (!shouldPass) {
				validationErrors.add(validationError);
			}
		}
	}

	public void sosValidateExample(final XmlObject xb_doc) throws OwsExceptionReport {
		/*
		 * this is just an example :-)
		 */
		XMLBeansParser.registerLaxValidationCase(GMLAbstractFeatureCase.getInstance());

		/*
		 * get errors. if empty, do not throw exception
		 */
		final Collection<XmlError> exs = XMLBeansParser.validate(xb_doc);

		String message = null;
		String parameterName = null;
		for (final XmlError error : exs) {
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
					final String[] messAndAttribute = message
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
					final String[] messAndAttribute = message.split(" ");
					parameterName = messAndAttribute[10];
				}

				// mandatory attribute is missing -->
				// missingParameterValue
				else if (message.startsWith("Expected attribute")) {
					exCode = ExceptionCode.MissingParameterValue;

					// get attribute name
					final String[] messAndAttribute = message
							.split("attribute: ");
					if (messAndAttribute.length == 2) {
						final String[] attrAndRest = messAndAttribute[1]
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
					final String[] messAndElements = message.split(" '");
					if (messAndElements.length >= 2) {
						final String elements = messAndElements[1];
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
					final String[] messAndElements = message.split(" '");
					if (messAndElements.length >= 2) {
						final String elements = messAndElements[1];
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
					final OwsExceptionReport se = new OwsExceptionReport();
					se.addCodedException(ExceptionCode.InvalidRequest,
							null, "[XmlBeans validation error:] " + message);
					//						LOGGER.error("The request is invalid!", se);
					throw se;
				}

				// create service exception
				final OwsExceptionReport se = new OwsExceptionReport();
				se.addCodedException(exCode, parameterName,
						"[XmlBeans validation error:] " + message);
				//					LOGGER.error("The request is invalid!", se);
				throw se;
			}
		}
	}


}
