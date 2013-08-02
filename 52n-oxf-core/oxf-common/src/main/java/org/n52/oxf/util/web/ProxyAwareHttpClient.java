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
package org.n52.oxf.util.web;

import java.net.ProxySelector;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;

public class ProxyAwareHttpClient extends HttpClientDecorator {

    /**
     * Decorates the given {@link HttpClient} to be proxy aware. This decorated instance will use the standard
     * JRE proxy selector to obtain proxy information.
     * 
     * @param httpclient
     *        the {@link HttpClient} to decorate with proxy awareness.
     * @see ProxySelectorRoutePlanner
     */
    public ProxyAwareHttpClient(HttpClient httpclient) {
        super(httpclient);
        decorateProxyAwareness();
    }

    private void decorateProxyAwareness() {
        DefaultHttpClient clientToDecorate = getHttpClientToDecorate();
        clientToDecorate.setRoutePlanner(createProxyPlanner());
    }

    private ProxySelectorRoutePlanner createProxyPlanner() {
        DefaultHttpClient decoratedHttpClient = getHttpClientToDecorate();
        ClientConnectionManager connectionManager = decoratedHttpClient.getConnectionManager();
        SchemeRegistry schemeRegistry = connectionManager.getSchemeRegistry();
        ProxySelector defaultProxySelector = ProxySelector.getDefault();
        return new ProxySelectorRoutePlanner(schemeRegistry, defaultProxySelector);
    }

}
