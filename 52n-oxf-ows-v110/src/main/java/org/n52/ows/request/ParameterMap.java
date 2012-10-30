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
package org.n52.ows.request;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class ParameterMap implements Map<String, String> {

    protected Map<String, String> parameters;
    
    protected ParameterMap() {
        this(new HashMap<String, String>());
    }
    
    protected ParameterMap(Map<String, String> parameters) {
        this.parameters = parameters;
    }
    
    public abstract boolean isValid();

    public int size() {
        return parameters.size();
    }

    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    public boolean containsKey(Object key) {
        return parameters.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return parameters.containsValue(value);
    }

    public String get(Object key) {
        return parameters.get(key);
    }

    public String put(String key, String value) {
        return parameters.put(key, value);
    }

    public String remove(Object key) {
        return parameters.remove(key);
    }

    public void putAll(Map< ? extends String, ? extends String> m) {
        parameters.putAll(m);
    }

    public void clear() {
        parameters.clear();
    }

    public Set<String> keySet() {
        return parameters.keySet();
    }

    public Collection<String> values() {
        return parameters.values();
    }

    public Set<java.util.Map.Entry<String, String>> entrySet() {
        return parameters.entrySet();
    }

    public boolean equals(Object o) {
        return parameters.equals(o);
    }

    public int hashCode() {
        return parameters.hashCode();
    }

    protected boolean checkRequiredParameter(String parameterName) {
        String value = parameters.get(parameterName);
        return value == null || value.isEmpty();
    }
    
    
    
}
