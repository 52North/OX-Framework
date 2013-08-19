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

package org.n52.oxf.adapter;

import java.util.ArrayList;
import java.util.List;

import org.n52.oxf.OXFException;
import org.n52.oxf.ows.capabilities.ITime;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.valueDomains.IntegerDiscreteValueDomain;
import org.n52.oxf.valueDomains.OpenValueDomain;
import org.n52.oxf.valueDomains.StringValueDomain;
import org.n52.oxf.valueDomains.time.TemporalValueDomain;

/**
 * Holds all the <code>ParameterShell</code>s of an AbstractServiceLayer that should be used in an operation
 * execution.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class ParameterContainer {

    List<ParameterShell> parameterShells = new ArrayList<ParameterShell>();

    /**
     * @return <code>true</code> if the container contained the specified element.
     */
    public boolean removeParameterShell(final ParameterShell parameterShell) {
        return parameterShells.remove(parameterShell);
    }
    
    /**
     * @param parameterName a CommonName or ServiceSidedName of a parameter.
     * @return <tt>true</tt> if the container contained a shell identified by 
     * 		   <tt>parameterName</tt> (ignoring case) and the according shell is successfully
     * 			removed.
     */
    public boolean removeParameterShell(final String parameterName) {
    	boolean returnValue = true;
    	boolean found = false;
    	// collect shells to remove
    	final ArrayList<ParameterShell> shellsToRemove = new ArrayList<ParameterShell>();
    	for (final ParameterShell shell : parameterShells)
    	{
			if (isParameterNameEqual(parameterName, shell))
			{
				found = true;
				shellsToRemove.add(shell);
			}
		}
    	// remove all found shells
    	for (final ParameterShell shell : shellsToRemove) {
    		final boolean tmp = parameterShells.remove(shell);
    		if (!tmp && returnValue) {
    			returnValue = false;
    		}
		}
		return found?returnValue:false;
    	
    }

	private boolean isParameterNameEqual(final String parameterName,
			final ParameterShell shell)
	{
		return shell.getParameter().getCommonName().equalsIgnoreCase(parameterName) || 
				shell.getParameter().getServiceSidedName().equalsIgnoreCase(parameterName);
	}

    public void addParameterShell(final ParameterShell parameterShell) {
        parameterShells.add(parameterShell);
    }

    /**
     * Adds a new ParameterShell with a 'required' {@link Parameter} with name <code>parameterName</code> and
     * a {@link StringValueDomain}omain containing only the specified <code>parameterValue</code>(s).
     */
    public void addParameterShell(final String parameterName, final String... parameterValue) throws OXFException {
        final Parameter parameter = new Parameter(parameterName, true, new StringValueDomain(parameterValue), parameterName);
        addParameterShell(new ParameterShell(parameter, parameterValue));
    }

    /**
     * Adds a new ParameterShell with a 'required' {@link Parameter} with name <code>parameterName</code> and
     * a {@link TemporalValueDomain} containing only the specified <code>parameterValue</code>(s). 
     */
    public void addParameterShell(final String parameterName, final ITime... parameterValue) throws OXFException {
        final Parameter parameter = new Parameter(parameterName, true, new TemporalValueDomain(parameterValue), parameterName);
        addParameterShell(new ParameterShell(parameter, parameterValue));
    }

    /**
     * Adds a new ParameterShell with a 'required' {@link Parameter} with name <code>parameterName</code> and
     * an {@link IntegerDiscreteValueDomain}eValueDomain containing only the specified <code>parameterValue</code>(s).
     */
    public void addParameterShell(final String parameterName, final Integer... parameterValue) throws OXFException {
        final Parameter parameter = new Parameter(parameterName, true, new IntegerDiscreteValueDomain(parameterValue), parameterName);
        addParameterShell(new ParameterShell(parameter, parameterValue));
    }

    /**
     * Adds a new ParameterShell with a 'required' {@link Parameter} with name <code>parameterName</code> and
     * an {@link OpenValueDomain} containing only the specified <code>parameterValue</code>(s).
     */
    public void addParameterShell(final String parameterName, final ParameterContainer... parameterValue) throws OXFException {
        final Parameter parameter = new Parameter(parameterName, true, new OpenValueDomain(), parameterName);
        addParameterShell(new ParameterShell(parameter, parameterValue));

    }

    /**
     * @param commonName
     * @return the ParameterShell whose parameter has the specified commonName (ignoring case). If there isn't
     *         such a ParameterShell <code>null</code> will be returned.
     */
    public ParameterShell getParameterShellWithCommonName(final String commonName) {
        for (final ParameterShell ps : parameterShells) {
            final Parameter parameter = ps.getParameter();
            if (parameter.getCommonName() != null  && parameter.getCommonName().equalsIgnoreCase(commonName)) {
                return ps;
            }
        }
        return null;
    }

    /**
     * @param serviceSidedName
     * @return the ParameterShell whose parameter has the specified serviceSidedName (ignoring case). If there
     *         isn't such a ParameterShell <code>null</code> will be returned.
     */
    public ParameterShell getParameterShellWithServiceSidedName(final String serviceSidedName) {
        for (final ParameterShell ps : parameterShells) {
            if (ps.getParameter().getServiceSidedName() != null
                    && ps.getParameter().getServiceSidedName().equalsIgnoreCase(serviceSidedName)) {
                return ps;
            }
        }
        return null;
    }

    /**
     * @param serviceSidedName
     * @return <code>true</code> if this ParameterContainer contains a ParameterShell with a Parameter whose
     *         serviceSidedName is as specified (ignoring case). Otherwise <code>false</code> will be
     *         returned.
     */
    public boolean containsParameterShellWithServiceSidedName(final String serviceSidedName) {
        for (final ParameterShell ps : parameterShells) {
            if (ps.getParameter().getServiceSidedName() != null
                    && ps.getParameter().getServiceSidedName().equalsIgnoreCase(serviceSidedName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param commonName
     * @return <code>true</code> if this ParameterContainer contains a ParameterShell with a Parameter whose
     *         commonName is as specified. Otherwise <code>false</code> will be returned.
     */
    public boolean containsParameterShellWithCommonName(final String commonName) {
        for (final ParameterShell ps : parameterShells) {
            if (ps.getParameter().getCommonName() != null
                    && ps.getParameter().getCommonName().equalsIgnoreCase(commonName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * searches the ParameterShell whose Parameter has the commonName and sets the newValue to this Parameter.
     * 
     * @param commonName
     * @param newValue
     * @throws OXFException
     *         if there is no ParameterShell whose parameter has the specified commonName.<br>
     *         or if the newValue is not contained in the valueDomain of the parameter.
     */
    public void setParameterValue(final String commonName, final Object newValue) throws OXFException {
        final ParameterShell ps = getParameterShellWithCommonName(commonName);
        if (ps != null) {
            ps.setSpecifiedValue(newValue);
        }
        else {
            throw new OXFException("There is no ParameterShell whose parameter has the commonName '" + commonName
                    + "'.");
        }
    }

    /**
     * searches the ParameterShell whose Parameter has the commonName and sets the newValues to this
     * Parameter.
     * 
     * @param commonName
     * @param newValues
     * @throws OXFException
     *         if there is no ParameterShell whose parameter has the specified commonName.<br>
     *         or if the one of the newValues is not contained in the valueDomain of the parameter.
     */
    public void setParameterValueArray(final String commonName, final Object[] newValues) throws OXFException {
        final ParameterShell ps = getParameterShellWithCommonName(commonName);
        if (ps != null) {
            ps.setSpecifiedValueArray(newValues);
        }
        else {
            throw new OXFException("There is no ParameterShell whose parameter has the commonName '" + commonName
                    + "'.");
        }
    }

    public List<ParameterShell> getParameterShells() {
        return parameterShells;
    }

    /**
     * 
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ParameterContainer:\n[");

        for (final ParameterShell ps : parameterShells) {
            sb.append("ServiceSidedName: ");
            sb.append(ps.getParameter().getServiceSidedName());
            sb.append("  CommonName: ");
            sb.append(ps.getParameter().getCommonName());
            sb.append("  Value: ");
            sb.append(ps.getSpecifiedValue());
            sb.append("\n");
        }
        sb.append("]");

        return sb.toString();
    }

}