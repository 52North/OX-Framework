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
package org.n52.oxf.ows.capabilities;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.n52.oxf.ows.Constraint;

/**
 *  Specifies the type of request of a specific operation. Only HTTP is supported.
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 */
public abstract class RequestMethod {
    
	/**
	 * required
	 */
	private OnlineResource onlineResource;
	
	/**
	 * optional
	 */
    private String[] constraints;
    
    private Set<Constraint> owsConstraints;
	
	/**
	 * this constructor has all attributes as its parameters.
	 * @param onlineResource
	 * @param type
	 * @param constraints
	 * @deprecated Use {@link #RequestMethod(OnlineResource, Set)} using type {@link Constraint}.
	 */
	@Deprecated
	public RequestMethod(final OnlineResource onlineResouce, final String[] constraints){
		setOnlineResource(onlineResouce);
		setConstraints(constraints);
	}
	
	/**
	 * @param onlineResource
	 * @param constraints the constraints that are relevant for this request method.
	 */
	public RequestMethod(final OnlineResource onlineResource, final Set<Constraint> constraints) {
		setOnlineResource(onlineResource);
		setOwsConstraints(constraints);
	}
	
	/**
	 * this constructor has all required attributes as its parameters.
	 * @param onlineResource
	 * @param type
	 */
	public RequestMethod(final OnlineResource onlineResource){
		setOnlineResource(onlineResource);
	}
	
    /**
     * @return a XML representation.
     */
    public abstract String toXML();
	
    /**
     * @deprecated Use {@link #getOwsConstraints()} and type {@link Constraint} instead
     */
    @Deprecated
	public String[] getConstraints() {
        return constraints;
    }
    
    /**
     * @deprecated Use {@link #setOwsConstraints(Set)} and type {@link Constraint} instead.
     */
    @Deprecated
    protected void setConstraints(final String[] constraints) {
        this.constraints = constraints;
    }

    public OnlineResource getOnlineResource() {
        return onlineResource;
    }
    
    protected void setOnlineResource(final OnlineResource onlineResource) {
        this.onlineResource = onlineResource;
    }
    
    // TODO Eike: add tests for new methods 

	/**
	 * @return an unmodifiable view of the constraints for this {@link RequestMethod} or an empty {@link Set} if not set.
	 */
	public Set<Constraint> getOwsConstraints()
	{
		if (owsConstraints == null) {
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(owsConstraints);
	}

	/**
	 * Adds a single constraint to the set of constraints.
	 * @param constraint
	 * @return <tt>true</tt>, if the constraint is added. <tt>false</tt>, if adding failed or constraint already contained.
	 */
	public boolean addOwsConstraint(final Constraint constraint) {
		if (constraint == null) {
			return false;
		}
		if (owsConstraints == null) {
			owsConstraints = new HashSet<Constraint>();
		}
		return owsConstraints.add(constraint);
	}
	
	/**
	 * Replaces the constraints set with the given one if it's not null.
	 * @param owsConstraints
	 * @return <tt>true</tt>, if owsConstraints is set, else <tt>false</tt>.
	 */
	public boolean setOwsConstraints(final Set<Constraint> owsConstraints)
	{
		if (owsConstraints == null) {
			return false;
		}
		this.owsConstraints = owsConstraints;
		return true;
	}

	@Override
	public String toString()
	{
		return String.format("RequestMethod [onlineResource=%s, constraints=%s, owsConstraints=%s]",
				onlineResource,
				Arrays.toString(constraints),
				owsConstraints);
	}
	
}