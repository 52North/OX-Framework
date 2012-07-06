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
package org.n52.oxf.owsCommon.capabilities;

import java.util.*;
import org.n52.oxf.*;

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
     * is compliant to iso 639
     * 
     * optional
     */
    private Locale[] language;

    /**
     * only a String represenation of the point of contact element.
     * 
     * optional
     */
    private String pointOfContactString;

    /**
     * this constructor has all REQUIRED attributes as its parameters. The other attributes will stay null.
     * 
     * @param title
     * @param identifier
     * @param boundingBoxes
     * @throws OXFException
     */
    public Dataset(String title, String identifier, IBoundingBox[] boundingBoxes) {
        super(title);
        setIdentifier(identifier);
        setBoundingBoxes(boundingBoxes);
    }

    /**
     * this constructor has ALL attributes of the class as its parameters.
     * 
     * @param title
     * @param identifier
     * @param boundingBoxes
     * @param outputFormats
     * @param availableCRSs
     * @param fees
     * @param language
     * @param pointOfContactString
     * @throws OXFException
     */
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
     * @param boundingBoxes
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
     * @param identifier
     * @throws OXFException
     *         if the identifier is empty.
     * @throws IllegalArgumentException
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