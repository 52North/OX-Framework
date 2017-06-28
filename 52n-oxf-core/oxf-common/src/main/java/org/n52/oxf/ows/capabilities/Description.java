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


/**
 * According to the OWS Common this holds some general parameters, which are needed by ServiceIdentification
 * and Dataset.
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster </a>
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public abstract class Description {

    /**
     * Title of the service / the data<br>
     * ('data' means e.g. a layer-description). <br>
     * <br>
     * One (mandatory) value for title is required. <br>
     */
    private String title;

    /**
     * Refers to the 'abstract' element in the ows common spec. <br>
     * <br>
     * Zero or one (optional) value for abstractDescription is possible. <br>
     */
    private String abstractDescription;

    /**
     * Unordered list of one or more commonly used or formalised word(s) or phrase(s) used to describe the
     * service / the data. <br>
     * <br>
     * Zero or more (optional) values for keywords are possible. <br>
     */
    private String[] keywords;

    /**
     * this constructor has all required attributes as its parameters. The other attributes will be set on
     * default values.
     * 
     * @param title the title to set
     */
    public Description(String title) {
        setTitle(title);
        abstractDescription = "";
        keywords = new String[] {""};
    }

    /**
     * this constructor has all required attributes as its parameters. The other attributes will be set on
     * default values.
     * 
     * @param title the title to set
     * @param abstractDescription the abstract description to set
     * @param keywords the keywords array to set
     */
    public Description(String title, String abstractDescription, String[] keywords) {
        setTitle(title);
        setAbstractDescription(abstractDescription);
        setKeywords(keywords);
    }

    /**
     * sets the title.
     * 
     * @param title the title to set
     * @throws IllegalArgumentException
     *         if the title is empty.
     */
    protected void setTitle(String title) throws IllegalArgumentException {
        if (title != null && !title.isEmpty()) {
            this.title = title;
        }
        else {
            throw new IllegalArgumentException("The parameter 'title' is null or empty.");
        }
    }

    protected void setAbstractDescription(String abstractDescription) {
        this.abstractDescription = abstractDescription;
    }

    protected void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public String getAbstractDescription() {
        return abstractDescription;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public String getTitle() {
        return title;
    }
}