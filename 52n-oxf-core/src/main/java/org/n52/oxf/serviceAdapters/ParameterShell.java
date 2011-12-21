/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 26.06.2005
 *********************************************************************************/

package org.n52.oxf.serviceAdapters;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.util.LoggingHandler;

/**
 * This class associates a <a href=Parameter.html>Parameter</a> with one or multiple values dependend on the
 * used constructor.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class ParameterShell {

    private static Logger LOGGER = LoggingHandler.getLogger(ParameterShell.class);

    private Parameter parameter;

    private Object specifiedValue = null;

    private Object[] specifiedValueArray = null;

    /**
     * @param parameter
     * @param specifiedValue
     *        must be an element of the valueDomain of the parameter. <br>
     *        Attention: it should be only ONE value of the value domain (e.g.: FORMAT=jpeg). If you want to
     *        specify multiple values (e.g.: COVERAGE=c1,c2,c3) for the parameter please use the constructor
     *        <code>ParameterShell(Parameter parameter, Object[] specifiedValueArray)</code> .
     * @throws OXFException
     *         if the specifiedValue is not contained in the valueDomain of the parameter.
     * @throws NullPointerException
     *         if the specifiedValue is <code>null</code>.
     */
    public ParameterShell(Parameter parameter, Object specifiedValue) throws OXFException {
        if (parameter == null) {
            throw new IllegalArgumentException("Parameter '" + parameter + "' is null.");
        }
        else if (specifiedValue == null) {
            throw new IllegalArgumentException("The specified value for parameter '" + parameter + "' is null.");
        }
        this.parameter = parameter;
        setSpecifiedValue(specifiedValue);
    }

    /**
     * @param parameter
     * @param specifiedValueArray
     *        multiple values which are all elements of the valueDomain of the parameter. Use this constructor
     *        if you want to associate the parameter with multiple values (e.g.: COVERAGE=c1,c2,c3).
     * @throws OXFException
     *         if one value of the specifiedValueArray is not contained in the valueDomain of the parameter.
     */
    public ParameterShell(Parameter parameter, Object[] specifiedValueArray) throws OXFException {
        if (parameter == null) {
            throw new IllegalArgumentException("Parameter is null.");
        }
        else if (specifiedValueArray == null) {
            throw new IllegalArgumentException("The specifiedValueArray for parameter '" + parameter + "' is null.");
        }
        this.parameter = parameter;
        setSpecifiedValueArray(specifiedValueArray);
    }

    public Parameter getParameter() {
        return parameter;
    }

    /**
     * @return true if the parameter is associated with just one specified value.
     */
    public boolean hasSingleSpecifiedValue() {
        return specifiedValue != null;
    }

    /**
     * @return true if the parameter is associated with multiple specified values.
     */
    public boolean hasMultipleSpecifiedValues() {
        return specifiedValueArray != null;
    }

    /**
     * @return the specifiedValue
     * @throws UnsupportedOperationException
     *         if this ParameterShell <code>hasMultipleSpecifiedValues()</code>.
     */
    public Object getSpecifiedValue() {
        if (hasMultipleSpecifiedValues()) {
            throw new UnsupportedOperationException("This ParameterShell instance has got multiple values. The associated Parameter is: "
                    + parameter.toXML());
        }
        return specifiedValue;
    }

    /**
     * @return the specifiedValueArray
     * @throws UnsupportedOperationException
     *         if this ParameterShell <code>hasSingleSpecifiedValue()</code> .
     */
    public Object[] getSpecifiedValueArray() {
        if (hasSingleSpecifiedValue()) {
            throw new UnsupportedOperationException("This ParameterShell instance has got a single value. The associated Parameter is: "
                    + parameter.toXML());
        }
        return specifiedValueArray;
    }

    // arneb: setSpecifiedValue(s) now without checking whether the value is
    // contained or not. Instead there
    // is just a LOGGER.warn statement.

    /**
     * sets the specifiedValue attribute (single-mode). The specifiedValueArray attribute (multiple-mode) will
     * be set on <code>null</code>.
     * 
     * @param specifiedValue
     *        must be an element of the valueDomain of the parameter. <br>
     *        Attention: it should be only ONE value of the value domain (e.g.: FORMAT=jpeg). If you want to
     *        specify multiple values (e.g.: COVERAGE=c1,c2,c3) for the parameter please use the method
     *        <code>setSpecifiedValues(Object[] specifiedValueArray)</code>.
     * @throws OXFException
     *         if the specifiedValue is not contained in the valueDomain of the parameter.
     */
    public void setSpecifiedValue(Object specifiedValue) throws OXFException {
        if (specifiedValue == null) {
            // TODO: should we warn here or throw an exception???
            LOGGER.warn(new OXFException(new IllegalArgumentException("specifiedValue has to be != null")));
        }
        else if (!parameter.getValueDomain().containsValue(specifiedValue)) {
            LOGGER.warn("The specifiedValue '" + specifiedValue.toString()
                    + "' is not contained in the valueDomain of the parameter '" + parameter.getServiceSidedName()
                    + "'");
        }
        this.specifiedValue = specifiedValue;
        this.specifiedValueArray = null;
    }

    /**
     * sets the specifiedValueArray attribute (multiple-mode). The specifiedValue attribute (single-mode) will
     * be set on <code>null</code>.
     * 
     * @param specifiedValueArray
     *        multiple values which are all elements of the valueDomain of the parameter. Use this method if
     *        you want to associate the parameter with multiple values (e.g.: COVERAGE=c1,c2,c3).
     * @throws OXFException
     *         if one value of the specifiedValueArray is not contained in the valueDomain of the parameter.
     */
    public void setSpecifiedValueArray(Object[] specifiedValueArray) throws OXFException {
        if (specifiedValueArray == null) {
            throw new OXFException(new IllegalArgumentException("specifiedValueArray has to be != null"));
        }
        for (Object obj : specifiedValueArray) {
            if (!this.parameter.getValueDomain().containsValue(obj)) {
                LOGGER.warn("One of the specifiedValueArray is not contained in the valueDomain of the parameter '"
                        + parameter.getServiceSidedName() + "'");
            }
        }
        this.specifiedValueArray = specifiedValueArray;
        this.specifiedValue = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ParemeterShell [");
        sb.append(this.parameter);
        if (this.specifiedValue != null) {
            sb.append(" - specifiedValue = ");
            sb.append(this.specifiedValue);
        }
        if (this.specifiedValueArray != null) {
            sb.append(" - specifiedValueArray = ");
            sb.append(Arrays.toString(this.specifiedValueArray));
        }
        sb.append("]");
        return sb.toString();
    }
}