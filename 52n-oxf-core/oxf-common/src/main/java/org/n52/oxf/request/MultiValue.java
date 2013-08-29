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

package org.n52.oxf.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <b>This class is test only yet!</b>
 */
public class MultiValue {

    private final List<String> values = new ArrayList<String>();

    /**
     * @param value
     *        the value to add. <code>null</code>s are hold as an empty string.
     * @return <code>true</code> if the list of values has been changed, <code>false</code> otherwise.
     */
    public boolean addValue(String value) {
        if (value == null) {
            value = "";
        }
        if (values.contains(value)) {
        	// we don't add the same value twice
            return false;
        }
        return values.add(value);
    }

    /**
     * TODO change to return the list of removed elements
     * @param value
     *        the value to remove.
     * @return <code>true</code> if the list of values has been changed, <code>false</code> otherwise.
     */
    public boolean removeValue(final String value) {
        return values.remove(value);
    }

    /**
     * @return a read-only collection containing all added values preserving the order they were added.
     */
    public Collection<String> getValues() {
        return Collections.unmodifiableCollection(values);
    }
    
    /**
     * @return the amount of values.
     */
    public int size() {
        return values.size();
    }

    /**
     * @param value
     *        the value to check.
     * @return <code>true</code> if the value is contained by this instance.
     */
    public boolean contains(final String value) {
        return values.contains(value);
    }

    /**
     * @return <code>true</code> if at least one value is was added to this instance, <code>false</code>
     *         otherwise.
     */
    public boolean hasValues() {
        return values.size() > 0;
    }

    /**
     * Removes all values contained by this instance.
     */
    public void removeAll() {
        values.clear();
    }
    
}
