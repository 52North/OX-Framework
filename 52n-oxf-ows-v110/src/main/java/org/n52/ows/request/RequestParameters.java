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

public abstract class RequestParameters implements Map<String, String> {

    private Map<String, String> parameters;

    protected RequestParameters() {
        this(new HashMap<String, String>());
    }

    protected RequestParameters(Map<String, String> parameters) {
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

    /**
     * Puts a required parameter to the map doing a non-null check beforehand.
     * 
     * @param key
     *        the parameter's name.
     * @param value
     *        the non-null value.
     * @return the previous value associated with key, or <code>null</code> if there was no mapping for key.
     * @throws IllegalArgumentException
     *         if the <code>key</code>'s associated <code>value</code> is <code>null</code> or empty.
     */
    public String putNonEmpty(String key, String value) {
        if (isEmptyString(value)) {
            String format = "Parameter '%s' is required and may not be null or empty!";
            throw new IllegalArgumentException(String.format(format, key));
        }
        return put(key, value);
    }

    /**
     * Simple delegate to {@link Map#put(Object, Object)}.
     * 
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
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

    /**
     * Checks if value of the named parameter is empty.
     * 
     * @param parameterName
     *        the parameter name.
     * @return <code>true</code> if parameter value is <code>null</code> or empty, <code>false</code>
     *         otherwise.
     */
    protected boolean isEmptyValue(String parameterName) {
        String value = parameters.get(parameterName);
        return isEmptyString(value);
    }

    protected boolean isEmptyString(String value) {
        return value == null || value.isEmpty();
    }

}
