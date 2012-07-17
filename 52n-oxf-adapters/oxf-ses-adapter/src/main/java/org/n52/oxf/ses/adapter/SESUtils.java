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
package org.n52.oxf.ses.adapter;

import org.apache.xmlbeans.XmlCursor;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;

/**
 * @author <a href="mailto:ehjuerrens@uni-muenster.de">Eike Hinderk J&uuml;rrens</a>
 * @version 03.08.2009
 */
public class SESUtils {
    
    
    /**
     * Adds a default set of namespaces to the SOAP-Envelope document:
     * <ul>
     *  <li>wsa: http://www.w3.org/2005/08/addressing</li>
     *  <li>wsbn: http://docs.oasis-open.org/wsn/br-2</li>
     *  <li>wsnt: http://docs.oasis-open.org/wsn/b-2</li>
     *  <li>wsrf: http://docs.oasis-open.org/wsrf/rl-2</li>
     *  <li>gml: http://www.opengis.net/gml</li>
     *  <li>ses: http://www.opengis.net/ses/0.0</li>
     *  <li>xsi: http://www.w3.org/2001/XMLSchema-instance</li>
     *  <li>SOAP-ENV: http://www.w3.org/2003/05/soap-envelope</li>
     *  <li>sml: http://www.opengis.net/sensorML/1.0.1</li>
     *  <li>xlink: http://www.w3.org/1999/xlink</li>
     * </ul>
     * @param env the {@link EnvelopeDocument} to which the namespace set is added
     */
    public static void addNamespacesToEnvelope_000(Envelope env){
        XmlCursor obsCursor = env.newCursor();
        obsCursor.toFirstContentToken();
        obsCursor.insertNamespace("wsa", "http://www.w3.org/2005/08/addressing");
        obsCursor.insertNamespace("wsbn","http://docs.oasis-open.org/wsn/br-2");
        obsCursor.insertNamespace("wsnt", "http://docs.oasis-open.org/wsn/b-2");
        obsCursor.insertNamespace("wsrf","http://docs.oasis-open.org/wsrf/rl-2");
        obsCursor.insertNamespace("gml","http://www.opengis.net/gml");
        obsCursor.insertNamespace("ses", "http://www.opengis.net/ses/0.0");
        obsCursor.insertNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        obsCursor.insertNamespace("SOAP-ENV","http://www.w3.org/2003/05/soap-envelope");
        obsCursor.insertNamespace("sml","http://www.opengis.net/sensorML/1.0.1");
        obsCursor.insertNamespace("xlink","http://www.w3.org/1999/xlink");
    }
}
