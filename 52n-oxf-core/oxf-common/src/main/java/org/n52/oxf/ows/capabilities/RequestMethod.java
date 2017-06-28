/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
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
	 * @deprecated Use {@link #RequestMethod(OnlineResource, Set)} using type {@link Constraint}.
	 *
	 * @param onlineResouce the <b>required</b> online resource
	 * @param constraints the <b>optional</b> constraints that are relevant for this request method.
	 */
	@Deprecated
	public RequestMethod(final OnlineResource onlineResouce, final String[] constraints){
		setOnlineResource(onlineResouce);
		setConstraints(constraints);
	}

	/**
	 * @param onlineResource the <b>required</b> online resource
	 * @param constraints the <b>optional</b> constraints that are relevant for this request method.
	 */
	public RequestMethod(final OnlineResource onlineResource, final Set<Constraint> constraints) {
		setOnlineResource(onlineResource);
		setOwsConstraints(constraints);
	}

	/**
	 * this constructor has all required attributes as its parameters.
	 * @param onlineResource the <b>required</b> online resource
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
     *
     * @return the constrains as String[]
     */
    @Deprecated
	public String[] getConstraints() {
        return constraints;
    }

    /**
     * @deprecated Use {@link #setOwsConstraints(Set)} and type {@link Constraint} instead.
     *
     * @param constraints a String[] containing constraints to set
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
	 * @param owsConstraint the constraint to add
	 * @return <tt>true</tt>, if the constraint is added. <tt>false</tt>, if adding failed or constraint already contained.
	 */
	public boolean addOwsConstraint(final Constraint owsConstraint) {
		if (owsConstraint == null) {
			return false;
		}
		if (owsConstraints == null) {
			owsConstraints = new HashSet<Constraint>();
		}
		return owsConstraints.add(owsConstraint);
	}

	/**
	 * Replaces the constraints set with the given one if it's not null.
	 * @param owsConstraints the constrains to set
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
