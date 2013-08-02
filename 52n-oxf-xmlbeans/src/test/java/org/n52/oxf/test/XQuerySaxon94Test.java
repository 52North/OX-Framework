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

package org.n52.oxf.test;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Assert;
import org.junit.Test;
import org.n52.oxf.xmlbeans.tools.XmlUtil;

public class XQuerySaxon94Test {

	@Test
	public void testXQuery() throws XmlException, IOException {
	    String namespaceDeclaration = 
	            "declare namespace muse-wsa='http://ws.apache.org/muse/addressing';"+
	            		"declare namespace wsa='http://www.w3.org/2005/08/addressing'; ";

	        String queryExpression =
	            "let $e := $this//wsa:ReferenceParameters " +
	            "return " +
	                "for $z in $e/muse-wsa:ResourceId " +
	                "return $z";
		
		XmlObject xo = readObject();
		xo = XmlUtil.execQuery(namespaceDeclaration + queryExpression, xo)[0];
		String inner = XmlUtil.stripText(XmlObject.Factory.parse(xo.getDomNode().getFirstChild().getFirstChild()));
		Assert.assertTrue(inner.trim().equals("Subscription-2"));
	}

	private XmlObject readObject() throws XmlException, IOException {
		return XmlObject.Factory.parse(getClass().getResourceAsStream("XPathSaxon94Text.xml"));
	}
	
}
