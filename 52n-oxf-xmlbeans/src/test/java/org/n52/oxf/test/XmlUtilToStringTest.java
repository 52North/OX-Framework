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

import junit.framework.Assert;
import net.opengis.gml.x32.DirectPositionListType;
import net.opengis.gml.x32.LineStringDocument;
import net.opengis.gml.x32.LineStringType;

import org.junit.Test;
import org.n52.oxf.xmlbeans.tools.XmlUtil;

public class XmlUtilToStringTest {

	@Test
	public void testToString() {
		LineStringDocument lsd = LineStringDocument.Factory.newInstance();
		LineStringType ls = lsd.addNewLineString();
		ls.setId("test");
		DirectPositionListType pl = ls.addNewPosList();
		pl.setStringValue("52 7 53 8");
		pl.setSrsName("urn:ogc:def:crs:EPSG::4326");
		String xml = XmlUtil.toString(ls.getDomNode());
		Assert.assertTrue("Unexpected xml string.",
				xml.contains("srsName=\"urn:ogc:def:crs:EPSG::4326\""));
	}
	
}
