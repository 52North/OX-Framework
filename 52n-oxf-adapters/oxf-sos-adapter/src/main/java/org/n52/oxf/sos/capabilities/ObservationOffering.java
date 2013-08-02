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

package org.n52.oxf.sos.capabilities;

import java.util.Locale;

import org.n52.oxf.ows.capabilities.Dataset;
import org.n52.oxf.ows.capabilities.IBoundingBox;
import org.n52.oxf.ows.capabilities.IDiscreteValueDomain;
import org.n52.oxf.ows.capabilities.ITime;
import org.n52.oxf.valueDomains.filter.FilterValueDomain;

/**
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class ObservationOffering extends Dataset {

    private String[] procedures;

    private String[] foiIDs;

    /**
     * These are the observables/phenomena that can be requested in this offering. <br>
     * <br>
     * required.
     */
    private String[] observedProperties;

    /**
     * This indicates the schema type of the result element that will be returned from a call to
     * GetObservation for this offering. The base O&M Observation type is defined as anyType so this element
     * is necessary to show the actual types that can be returned for this offering. <br>
     * <br>
     * optional.
     */
    private String[] resultModels;

    /**
     * Indicates what modes of response are supported for this offering. The value of "resultTemplate" is used
     * to retrieve an observation template that will later be used in calls to GetResult. The other options
     * allow results to appear inline in a resultTag (inline), external to the observation element
     * (out-of-band) or as a MIME attachment (attached). <br>
     * <br>
     * optional.
     */
    private String[] responseModes;

    /**
     * 
     */
    private FilterValueDomain result;
    
    /**
     * this constructor has all REQUIRED attributes as its parameters. The other attributes will stay null.
     * 
     * @param title
     * @param identifier
     * @param boundingBoxes
     * @param procedures
     * @param featureOfInterest
     * @param observedProperties
     */
    public ObservationOffering(String title,
                               String identifier,
                               IBoundingBox[] boundingBoxes,
                               String[] procedures,
                               String[] foiIDs,
                               String[] observedProperties) {
        super(title, identifier, boundingBoxes);
        
        setProcedures(procedures);
        setFeatureOfInterest(foiIDs);
        setObservedProperties(observedProperties);
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
     * @param temporalDomain
     * @param abstractDescription
     * @param keywords
     * @param procedures
     * @param featureOfInterest
     * @param observedProperties
     * @param resultModels
     * @param responseMode
     */
    public ObservationOffering(String title,
                               String identifier,
                               IBoundingBox[] boundingBoxes,
                               String[] outputFormats,
                               String[] availableCRSs,
                               String fees,
                               Locale[] language,
                               String pointOfContactString,
                               IDiscreteValueDomain<ITime> temporalDomain,
                               String abstractDescription,
                               String[] keywords,
                               String[] procedures,
                               String[] foiIDs,
                               String[] observedProperties,
                               String[] resultModels,
                               String[] responseModes,
                               FilterValueDomain result) {
        super(title,
              identifier,
              boundingBoxes,
              outputFormats,
              availableCRSs,
              fees,
              language,
              pointOfContactString,
              temporalDomain,
              abstractDescription,
              keywords);

        setProcedures(procedures);
        setFeatureOfInterest(foiIDs);
        setObservedProperties(observedProperties);
        setResultModels(resultModels);
        setResponseModes(responseModes);
        setResult(result);
    }
    
    public String[] getFeatureOfInterest() {
        return foiIDs;
    }

    protected void setFeatureOfInterest(String[] foiIDs) {
        this.foiIDs = foiIDs;
    }

    public String[] getObservedProperties() {
        return observedProperties;
    }

    protected void setObservedProperties(String[] observedProperties) {
        this.observedProperties = observedProperties;
    }

    public String[] getProcedures() {
        return procedures;
    }

    protected void setProcedures(String[] procedures) {
        this.procedures = procedures;
    }

    public String[] getResponseModes() {
        return responseModes;
    }

    protected void setResponseModes(String[] responseModes) {
        this.responseModes = responseModes;
    }

    public String[] getResultModels() {
        return resultModels;
    }

    protected void setResultModels(String[] resultModels) {
        this.resultModels = resultModels;
    }

    public FilterValueDomain getResult() {
        return result;
    }

    public void setResult(FilterValueDomain result) {
        this.result = result;
    }
}