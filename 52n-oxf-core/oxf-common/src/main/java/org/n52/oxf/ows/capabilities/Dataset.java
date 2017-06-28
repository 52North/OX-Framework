/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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

import java.util.Locale;

/**
 * This holds metadata about data provided by the service.
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 * @author <a href="mailto:broering@52north.org">Arne Broering </a>
 */
public class Dataset extends Description {

    /**
     * for example the name of the dataset.
     * 
     * required
     */
    private String identifier;

    /**
     * optional
     */
    private String[] outputFormats;

    /**
     * optional
     */
    private String[] availableCRSs;

    /**
     * one is required
     */
    private IBoundingBox[] boundingBoxes;

    /**
     * holds all possible time values for this dataset.
     * 
     * optional
     */
    private IDiscreteValueDomain<ITime> temporalDomain;

    /**
     * optional
     */
    private String fees;

    /**
     * is compliant to ISO 639
     * 
     * optional
     */
    private Locale[] language;

    /**
     * only a String representation of the point of contact element.
     * 
     * optional
     */
    private String pointOfContactString;

    /*
     * this constructor has all REQUIRED attributes as its parameters. The other attributes will stay null.
     */
    public Dataset(String title, String identifier, IBoundingBox[] boundingBoxes) {
        super(title);
        setIdentifier(identifier);
        setBoundingBoxes(boundingBoxes);
    }

    public Dataset(String title,
                   String identifier,
                   IBoundingBox[] boundingBoxes,
                   String[] outputFormats,
                   String[] availableCRSs,
                   String fees,
                   Locale[] language,
                   String pointOfContactString,
                   IDiscreteValueDomain<ITime> temporalDomain,
                   String abstractDescription,
                   String[] keywords) {
        super(title, abstractDescription, keywords);
        setIdentifier(identifier);
        setBoundingBoxes(boundingBoxes);
        setOutputFormats(outputFormats);
        setAvailableCRSs(availableCRSs);
        setFees(fees);
        setLanguage(language);
        setPointOfContactString(pointOfContactString);
        setTemporalDomain(temporalDomain);
    }

    public String toString() {
        return this.getIdentifier();
    }

    /**
     * @return a XML representation of this Dataset-section.
     */
    public String toXML() {
        String res = "<Dataset identifier=\"" + identifier + "\">";

        res += "<PointOfContact>";
        res += pointOfContactString;
        res += "</PointOfContact>";

        if (temporalDomain != null)
            res += temporalDomain.toXML();

        res += "<AvailableCRSs>";
        if (availableCRSs != null) {
            for (String crs : availableCRSs) {
                res += "<CRS>";
                res += crs;
                res += "</CRS>";
            }
        }
        res += "</AvailableCRSs>";

        res += "<OutputFormats>";
        if (outputFormats != null) {
            for (String format : outputFormats) {
                res += "<Format>";
                res += format;
                res += "</Format>";
            }
        }
        res += "</OutputFormats>";

        res += "<BBoxes>";
        if (boundingBoxes != null) {
            for (IBoundingBox bBox : boundingBoxes) {
                res += bBox.toXML();
            }
        }
        res += "</BBoxes>";

        res += "<Languages>";
        if (language != null) {
            for (Locale locale : language) {
                res += "<Language>";
                res += locale.toString();
                res += "</Language>";
            }
        }
        res += "</Languages>";

        res += "</Dataset>";

        return res;
    }

    public String[] getAvailableCRSs() {
        return availableCRSs;
    }

    protected void setAvailableCRSs(String[] availableCRS) {
        this.availableCRSs = availableCRS;
    }

    public IBoundingBox[] getBoundingBoxes() {
        return boundingBoxes;
    }

    /**
     * @param boundingBox the bounding box to set
     * @throws IllegalArgumentException
     *         if the boundingBoxes[] is empty.
     */
    protected void setBoundingBoxes(IBoundingBox[] boundingBox) throws IllegalArgumentException {
        if (boundingBox.length >= 1) {
            this.boundingBoxes = boundingBox;
        }
        else {
            throw new IllegalArgumentException("The parameter 'boundingBox' is illegal.");
        }
    }

    public String getFees() {
        return fees;
    }

    protected void setFees(String fees) {
        this.fees = fees;
    }

    public String getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier  the identifier to set
     * @throws IllegalArgumentException
     *         if the identifier is empty.
     */
    protected void setIdentifier(String identifier) throws IllegalArgumentException {
        if (identifier != null && !identifier.equals("")) {
            this.identifier = identifier;
        }
        else {
            throw new IllegalArgumentException("The parameter 'identifier' is illegal.");
        }
    }

    public Locale[] getLanguage() {
        return language;
    }

    protected void setLanguage(Locale[] language) {
        this.language = language;
    }

    public String[] getOutputFormats() {
        return outputFormats;
    }

    protected void setOutputFormats(String[] outputFormat) {
        this.outputFormats = outputFormat;
    }

    public String getPointOfContactString() {
        return pointOfContactString;
    }

    protected void setPointOfContactString(String pointOfContactString) {
        this.pointOfContactString = pointOfContactString;
    }

    public IDiscreteValueDomain<ITime> getTemporalDomain() {
        return temporalDomain;
    }

    protected void setTemporalDomain(IDiscreteValueDomain<ITime> temporalDomain) {
        this.temporalDomain = temporalDomain;
    }

}