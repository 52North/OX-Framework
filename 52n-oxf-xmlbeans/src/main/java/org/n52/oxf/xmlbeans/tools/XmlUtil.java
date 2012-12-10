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
package org.n52.oxf.xmlbeans.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.n52.oxf.xmlbeans.parser.XMLBeansParser;
import org.n52.oxf.xmlbeans.parser.XMLHandlingException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class provides helper methods and best practices for regularly occurring issues with XMLBeans
 * handling.
 * 
 * @author matthes rieke <m.rieke@52north.org>
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class XmlUtil {

	public final static XmlOptions NO_XML_FRAGMENTS = new XmlOptions().setSaveOuter();
	public final static XmlOptions PRETTYPRINT = new XmlOptions(NO_XML_FRAGMENTS);
	public final static XmlOptions FAST = new XmlOptions(NO_XML_FRAGMENTS);

	static {
		PRETTYPRINT.setSavePrettyPrint();
		PRETTYPRINT.setSavePrettyPrintIndent(2);
		PRETTYPRINT.setUseDefaultNamespace();
		FAST.setUseDefaultNamespace();
	}

	/**
	 * @param xml
	 *        the node containing xml
	 * @param nodeName
	 *        the node's name of the DOM node
	 * @return an XmlBeans {@link XmlObject} representation of the body, or <code>null</code> if node could
	 *         not be found.
	 * @throws XmlException
	 *         if parsing to XML fails
	 */
	public static XmlObject getXmlAnyNodeFrom(XmlObject xml, String nodeName) throws XmlException {
		Node bodyNode = XmlUtil.getDomNode(xml, nodeName);
		return bodyNode == null ? null : XmlObject.Factory.parse(bodyNode);
	}

	/**
	 * Mechanism to find an xml element by its name in a given {@link XmlObject}.
	 * <br><br>
	 * XMLBeans has no accessor for type #any, that is why this method can be used to get #any content as {@link Node}.
	 * 
	 * @param xmlobj
	 *        the xml object which contains the &lt;name&gt; element.
	 * @param name
	 *        the xml element which shall be found.
	 * @return a DOM {@link Node} which can further be parsed .
	 * @throws Exception
	 *         if DOM node of <code>xmlobj</code> is <code>null</code>.
	 */
	public static Node getDomNode(XmlObject xmlobj, String name) throws XmlException {
		Node domNode = xmlobj.getDomNode();
		if (domNode == null)
			throw new XmlException("No DOM node found where to extract" + name + " element.");

		name = stripPrefix(name);

		// You don't have a get accessor for the <any>
		// element's children, so use DOM to identify the correct element while
		// looping through the <any> element's child list.
		NodeList childList = domNode.getChildNodes();

		// Find the element of name: <name>.
		for (int i = 0; i < childList.getLength(); i++) {
			Node node = childList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getLocalName().equals(name)) {
					return node;
				}
			}
		}

		return null; // not found
	}

	private static String stripPrefix(String name) {
		return name.substring(name.indexOf(":") + 1);
	}


	/**
	 * Extracts an element from an xml doc. This is only useful for abstract or choice types. It is done via
	 * reflection
	 * 
	 * @param doc
	 *        The xml element
	 * @param element
	 *        the name of the Method, for example <code>getUom</code>
	 * @return the return value of the Method or null if the Method does not exist.
	 * @throws XMLHandlingException
	 *         thrown if the XML is incorrect
	 */
	public static String getElement(XmlObject doc, String element) throws XMLHandlingException {
		try {
			for (Method method : doc.getClass().getMethods()) {
				if (method.getName().equals(element)) {
					return method.invoke(doc, (Object[]) null).toString();
				}
			}

			return null;
		}
		catch (Exception e) {
			throw new XMLHandlingException("cannot extract '" + element + "' from "
					+ doc.schemaType().getName().toString(), e);
		}
	}

	/**
	 * @param doc
	 *        XmlBeans object
	 * @return the XmlObject as a String
	 * @throws XMLHandlingException
	 *         thrown if the XML is incorrect
	 */
	public static String objectToString(XmlObject doc) throws XMLHandlingException {
		return objectToString(doc, true);
	}

	/**
	 * @param doc
	 *        XmlBeans object
	 * @param validate
	 *        validate the document?
	 * @return the XmlObject as a String
	 * @throws XMLHandlingException
	 *         thrown if the XML is incorrect
	 */
	public static String objectToString(XmlObject doc, boolean validate) throws XMLHandlingException {
		return objectToString(doc, validate, false);
	}

	/**
	 * @param doc
	 *        XmlBeans object
	 * @param validate
	 *        validate the object?
	 * @param prettyprint
	 *        Output in pretty print mode?
	 * @return the XmlObject as a String
	 * @throws XMLHandlingException
	 *         thrown if the XML is incorrect
	 */
	public static String objectToString(XmlObject doc, boolean validate, boolean prettyprint) throws XMLHandlingException {
		if (validate) {
			XMLBeansParser.strictValidate(doc);
		}
		if (prettyprint) {
			return doc.xmlText(PRETTYPRINT);
		}
		return doc.xmlText(FAST);
	}

	/**
	 * Writes a document with the given {@link PrintWriter}
	 * 
	 * @param doc
	 *        the document to write
	 * @param out
	 *        the print writer
	 * @throws IOException
	 *         thrown if an IO error occurred
	 */
	public static void writeObject(XmlObject doc, PrintWriter out) throws IOException {
		doc.save(out, PRETTYPRINT);
	}

	/**
	 * Writes a document with the given {@link PrintWriter}
	 * 
	 * @param doc
	 *        the document to write
	 * @param out
	 *        the print writer
	 * @param validate
	 *        validate the document?
	 * @throws IOException
	 *         thrown if an IO error occurred
	 * @throws XMLHandlingException
	 *         thrown if the XML is incorrect
	 */
	public static void writeObject(XmlObject doc, PrintWriter out, boolean validate) throws IOException,
	XMLHandlingException {
		if (validate) {
			XMLBeansParser.strictValidate(doc);
		}
		writeObject(doc, out);
	}

	/**
	 * Builds an org.w3c.dom.Node from a {@link XmlObject}.
	 * 
	 * @param input
	 *        the event document
	 * 
	 * @return a org.w3c.dom.Node representation
	 */
	public static Node getDomNode(XmlObject input) throws XMLHandlingException {
		/*
		 * This solution looks strange but it is necessary to do it this way. When trying to call
		 * getDomNode() on the incoming XmlObject you will get an Exception (DOM Level 3 not implemented).
		 */
		try {
			XmlObject rootNode = getRootNode(input);
			return rootNode.getDomNode();
		}
		catch (Throwable t) {
			throw new XMLHandlingException(t.getMessage(), t);
		}
	}

	private static XmlObject getRootNode(XmlObject input) {
		return input.selectPath("/*")[0];
	}

	/**
	 * @see #qualifySubstitutionGroup(XmlObject, QName, SchemaType) with SchemaType=null.
	 */
	public static XmlObject qualifySubstitutionGroup(XmlObject xobj, QName newInstance) {
		return qualifySubstitutionGroup(xobj, newInstance, null);
	}

	/**
	 * Qualifies a valid member of a substitution group. This method tries to use the
	 * built-in {@link XmlObject#substitute(QName, SchemaType)} and if succesful returns
	 * a valid substitution which is usable (not disconnected). If it fails, it uses
	 * low-level {@link XmlCursor} manipulation to qualify the substitution group. Note
	 * that if the latter is the case the resulting document is disconnected and should
	 * no longer be manipulated. Thus, use it as a final step after all markup is included.
	 * 
	 * If newType is null, this method will skip {@link XmlObject#substitute(QName, SchemaType)}
	 * and directly use {@link XmlCursor}. This can be used, if you are sure that the substitute
	 * is not in the list of (pre-compiled) valid substitutions (this is the case if a schema
	 * uses another schema's type as a base for elements. E.g. om:Observation uses gml:_Feature
	 * as the base type).
	 * 
	 * @param xobj
	 * 		the abstract element
	 * @param newInstance
	 * 		the new {@link QName} of the instance
	 * @param newType the new schemaType. if null, cursors will be used and the resulting object
	 * 		will be disconnected.
	 * @return if successful applied {@link XmlObject#substitute(QName, SchemaType)} a living object with a
	 * 		type == newType is returned. Otherwise null is returned as you can no longer manipulate the object.
	 */
	public static XmlObject qualifySubstitutionGroup(XmlObject xobj, QName newInstance, SchemaType newType) {
		XmlObject substitute = null;
		if (newType != null) {
			substitute = xobj.substitute(newInstance, newType);
			if (substitute != null && substitute.schemaType() == newType &&
					substitute.getDomNode().getLocalName().equals(newInstance.getLocalPart())) {
				return substitute;
			}
		}

		XmlCursor cursor = xobj.newCursor();
		cursor.setName(newInstance);
		QName qName = new QName("http://www.w3.org/2001/XMLSchema-instance", "type");
		cursor.removeAttribute(qName);
		cursor.dispose();

		return null;
	}

	/**
	 * Strips out the text of an xml-element and returns as a String.
	 * 
	 * @param elems
	 *        array of elements
	 * @return the string value of the first element
	 */
	public static String stripText(XmlObject[] elems) {
		if (elems != null && elems.length > 0) {
			return stripText(elems[0]);
		}
		return null;
	}

	/**
	 * @param elem
	 *        the text-containing element
	 * @return the text value
	 */
	public static String stripText(XmlObject elem) {
		if (elem != null) {
			NodeList children = elem.getDomNode().getChildNodes();
			Node child;
			for (int i = 0; i < children.getLength(); i++) {
				child = children.item(i);
				if (child.getNodeType() == Node.TEXT_NODE) {
					return toString(child).trim();
				}			
			}

		}
		return null;
	}

	public static QName getElementType(XmlObject xml) {
		return xml == null ? null : xml.schemaType().getDocumentElementName();
	}

	public static String getTextContentFromXml(XmlObject xmlElement) {
		Node node = xmlElement.getDomNode();
		return node.getFirstChild().getFirstChild().getNodeValue();
	}

	public static String getTextContentFromAnyNode(XmlObject xmlAnyElement) {
		Node node = xmlAnyElement.getDomNode();
		return node.getFirstChild().getNodeValue();
	}

	public static XmlObject setTextContent(XmlObject xmlObject, String content) {
		XmlCursor cursor = xmlObject.newCursor();
		cursor.toFirstChild();
		cursor.setTextValue(content);
		cursor.dispose();
		return xmlObject;
	}

	/**
	 * Transform this {@link Node} into a {@link String} representation.
	 * 
	 * @param xml
	 *        the xml Node
	 * @return a String representation of the given xml Node
	 */
	public static String toString(Node xml) {
		short type = xml.getNodeType();

		if (type == Node.TEXT_NODE)
			return xml.getNodeValue();

		StringWriter buffer = new StringWriter();
		try {
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(new DOMSource(xml),
					new StreamResult(buffer));
		} catch (Exception e) {
		}

		return buffer.toString();

	}



}
