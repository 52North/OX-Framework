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
package org.n52.oxf.xmlbeans.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.n52.oxf.xmlbeans.parser.XMLHandlingException;
import org.n52.oxf.xmlbeans.parser.XMLBeansParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class provides helper methods and best practices
 * for regularly occurring issues with XMLBeans handling.
 * 
 * @author matthes rieke <m.rieke@52north.org>
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
@SuppressWarnings("deprecation")
public class XMLBeansTools {

	private final static XmlOptions OMIT_XML_FRAGMENTS = new XmlOptions();
	private final static XmlOptions PRETTYPRINT;
	private final static XmlOptions FAST;

	static {
		OMIT_XML_FRAGMENTS.setSaveOuter();
		
		PRETTYPRINT = new XmlOptions(OMIT_XML_FRAGMENTS);
		PRETTYPRINT.setSavePrettyPrint();
		PRETTYPRINT.setUseDefaultNamespace();

		FAST = new XmlOptions(OMIT_XML_FRAGMENTS);
		FAST.setUseDefaultNamespace();
	}

	/**
	 * Extracts an element from an xml doc. This is only useful for abstract or
	 * choice types. It is done via reflection
	 * 
	 * @param doc
	 *            The xml element
	 * @param element
	 *            the name of the Method, for example <code>getUom</code>
	 * @return the return value of the Method or null if the Method does not exist.
	 * @throws XMLHandlingException thrown if the XML is incorrect
	 */
	public static String getElement(XmlObject doc, String element) throws XMLHandlingException {
		try {
			for (Method method : doc.getClass().getMethods()) {
				if (method.getName().equals(element)) {
					return method.invoke(doc, (Object[]) null).toString();
				}
			}

			return null;
		} catch (Exception e) {
			throw new XMLHandlingException("cannot extract "+  element +" from " + doc.schemaType().getName().toString(),e);
		}

	}


	/**
	 * @param doc XmlBeans object
	 * @return the XmlObject as a String
	 * @throws XMLHandlingException thrown if the XML is incorrect
	 */
	public static String objectToString(XmlObject doc) throws XMLHandlingException {
		return objectToString(doc, true);
	}

	/**
	 * @param doc XmlBeans object
	 * @param validate validate the document?
	 * @return the XmlObject as a String
	 * @throws XMLHandlingException thrown if the XML is incorrect
	 */
	public static String objectToString(XmlObject doc, boolean validate) throws XMLHandlingException {
		return objectToString(doc, validate, false);
	}

	/**
	 * @param doc XmlBeans object
	 * @param validate validate the object?
	 * @param prettyprint Output in pretty print mode?
	 * @return the XmlObject as a String
	 * @throws XMLHandlingException thrown if the XML is incorrect
	 */
	public static String objectToString(XmlObject doc, boolean validate, boolean prettyprint) throws XMLHandlingException {
		if (validate) {
			XMLBeansParser.validate(doc);
		}
		if (prettyprint) {
			return doc.xmlText(PRETTYPRINT);
		}
		return doc.xmlText(FAST);
	}


	/**
	 * Writes a document with the given {@link PrintWriter}
	 * 
	 * @param doc the document to write
	 * @param out the print writer
	 * @throws IOException thrown if an IO error occurred
	 */
	public static void writeObject(XmlObject doc, PrintWriter out) throws IOException {
		doc.save(out, PRETTYPRINT);
	}

	/**
	 * Writes a document with the given {@link PrintWriter}
	 * 
	 * @param doc the document to write
	 * @param out the print writer
	 * @param validate validate the document?
	 * @throws IOException thrown if an IO error occurred
	 * @throws XMLHandlingException thrown if the XML is incorrect
	 */
	public static void writeObject(XmlObject doc, PrintWriter out, boolean validate) throws IOException, XMLHandlingException {
		if (validate) {
			XMLBeansParser.validate(doc);
		}
		writeObject(doc, out);
	}


	/**
	 * Builds an rg.w3c.dom.Node from a {@link XmlObject}.
	 * 
	 * ATTENTION: This solution looks strange but it is 
	 * necessary to do it this way. When trying to call
	 * getDomNode() on the incoming XmlObject you will
	 * get an Exception (DOM Level 3 not implemented).
	 * 
	 * @param input the event document
	 * 
	 * @return a org.w3c.dom.Node representation
	 */
	public static Node getDomNode(XmlObject input) throws XMLHandlingException {
		try {
			//XPath expression to get the root node
			String xQuery = "/*";

			//get the root node
			XmlObject xmlObject = input.selectPath(xQuery)[0];

			//get the DOM Node
			return xmlObject.getDomNode();
		}
		catch (Throwable t) {
			throw new XMLHandlingException(t.getMessage(), t);
		}
	}

	/**
	 * Use this method if you have an instance of an abstract
	 * xml element (substitution groups, etc..) with an xsi:type
	 * attribute defining the instance of the abstract element.
	 * It replaces the abstract element with the instance element.
	 * 
	 * @param xobj the abstract element
	 * @param newInstance the new {@link QName} of the instance
	 */
	public static void replaceXsiTypeWithInstance(XmlObject xobj, QName newInstance) {
		XmlCursor cursor = xobj.newCursor();
		cursor.setName(newInstance);
		cursor.removeAttribute(new QName("http://www.w3.org/2001/XMLSchema-instance",
				"type"));
	}

	/**
	 * Strips out the text of an xml-element and returns as a String.
	 * @param elems array of elements
	 * @return the string value of the first element
	 * @throws XMLHandlingException tranformations failed.
	 */
	public static String stripText(XmlObject[] elems) {
		if (elems != null && elems.length > 0) {
			return stripText(elems[0]);
		}
		return null;
	}

	/**
	 * @param elem the text-containing element
	 * @return the text value
	 * @throws XMLHandlingException transformations failed.
	 */
	public static String stripText(XmlObject elem) {
		if (elem != null) {
			Node child = elem.getDomNode().getFirstChild();
			if (child != null) {
				return toString(child).trim();
			}
		}
		return null;
	}


	/**
	 * Transform this {@link Node} into a {@link String} representation.
	 * 
	 * NOTE: This methods makes use of a deprecated serializer API (xerces).
	 * However, some components rely on this serialization (e.g. SES). Please
	 * do not change until further notice.
	 * 
	 * @param xml the xml Node 
	 * @return a String representation of the given xml Node
	 * @throws XMLHandlingException if transformation failed.
	 */
	private static String toString(Node xml) {
		 short type = xml.getNodeType();
	        
	        if (type == Node.TEXT_NODE)
	            return xml.getNodeValue();
	        
	        //
	        // NOTE: This serialization code is not part of JAXP/DOM - it is 
	        //       specific to Xerces and creates a Xerces dependency for 
	        //       this class.
	        //
	        XMLSerializer serializer = new XMLSerializer();
	        serializer.setNamespaces(true);
	        
	        OutputFormat formatter = new OutputFormat();        
	        formatter.setOmitXMLDeclaration(false);        
	        formatter.setIndenting(true);        
	        serializer.setOutputFormat(formatter);
	        
	        StringWriter writer = new StringWriter();
	        serializer.setOutputCharStream(writer);
	        
	        try
	        {
	            if (type == Node.DOCUMENT_NODE)
	                serializer.serialize((Document)xml);
	            
	            else
	                serializer.serialize((Element)xml);
	        }
	        
	        //
	        // we are using a StringWriter, so this "should never happen". the 
	        // StringWriter implementation writes to a StringBuffer, so there's 
	        // no file I/O that could fail.
	        //
	        // if it DOES fail, we re-throw with a more serious error, because 
	        // this a very common operation.
	        //
	        catch (IOException error)
	        {
	            throw new RuntimeException(error.getMessage(), error);
	        }
	        
	        return writer.toString();
//		if (domImpl == null) throw new RuntimeException("Could not access XML LS Serializer");
//		LSSerializer serializer = domImpl.createLSSerializer();
//		DOMConfiguration cfg = serializer.getDomConfig();
//		
//		if (cfg.canSetParameter("format-pretty-print", Boolean.TRUE)) {
//			cfg.setParameter("format-pretty-print", Boolean.TRUE);
//		}
//		
//		LSOutput lso = domImpl.createLSOutput();
//		lso.setEncoding("UTF-8");
//		StringWriter sw = new StringWriter();
//		lso.setCharacterStream(sw);
//		serializer.write(xml, lso);
//		
//		return sw.toString();


		//		DOMSource ds = new DOMSource(xml);
		//		
		//		StringWriter writer = new StringWriter();
		//		StreamResult result = new StreamResult(writer);
		//		
		//		TransformerFactory tf = TransformerFactory.newInstance();
		//	
		//		/*
		//		 * a mighty Transformer!
		//		 */
		//		Transformer transformer;
		//		try {
		//			transformer = tf.newTransformer();
		//		} catch (TransformerConfigurationException e) {
		//			throw new RuntimeException(e);
		//		}
		//		
		//		transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		//		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		//		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		//		
		//		try {
		//			transformer.transform(ds, result);
		//		} catch (TransformerException e) {
		//			throw new RuntimeException(e);
		//		}
		//		return writer.getBuffer().toString();

	}

}
