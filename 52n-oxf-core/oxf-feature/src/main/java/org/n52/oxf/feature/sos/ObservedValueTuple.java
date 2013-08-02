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


package org.n52.oxf.feature.sos;

import org.n52.oxf.ows.capabilities.ITime;

/**
 * Associates a tuple of observedProperties with a time (-position or -period). 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class ObservedValueTuple {
    
    private Object[] values;
    private String[] phenNames;
    private ITime time;

    public ObservedValueTuple(int dimension, String[] phenNames, ITime time) {
        values = new Object[dimension];
        this.phenNames = phenNames;
        this.time = time;
    }

    public void setValue(int index, Object value) {
        values[index] = value;
    }

    public Object getValue(int index) {
        return values[index];
    }

    public int dimension() {
        return values.length;
    }

    public String[] getPhenomenonNames() {
        return phenNames;
    }

    public ITime getTime() {
        return time;
    }
    
    public String toString() {
        String res = "";
        for (Object value : values) {
            res += value.toString() + ", ";
        }
        return res;
    }
}