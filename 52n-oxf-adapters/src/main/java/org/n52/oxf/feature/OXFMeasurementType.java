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
 
 Created on: 29.01.2006
 *********************************************************************************/

package org.n52.oxf.feature;

import java.util.List;

import net.opengis.gml.MeasureType;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.feature.dataTypes.OXFMeasureType;
import org.n52.oxf.feature.dataTypes.OXFPhenomenonPropertyType;
import org.n52.oxf.owsCommon.capabilities.ITime;
import org.n52.oxf.util.LoggingHandler;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class OXFMeasurementType extends OXFAbstractObservationType {

    private static Logger LOGGER = LoggingHandler.getLogger(OXFMeasurementType.class);

    public static final String RESULT = "result";

    /**
     * 
     */
    public OXFMeasurementType() {
        super();

        typeName = "OXFMeasurementType";
        featureAttributeDescriptors = generateAttributeDescriptors();
    }

    /**
     * 
     */
    protected List<OXFFeatureAttributeDescriptor> generateAttributeDescriptors() {

        List<OXFFeatureAttributeDescriptor> attributeDescriptors = super.generateAttributeDescriptors();

        OXFFeatureAttributeDescriptor result = new OXFFeatureAttributeDescriptor(RESULT,
                                                                                 DataType.OBJECT,
                                                                                 OXFMeasureType.class,
                                                                                 1,
                                                                                 1,
                                                                                 "");
        attributeDescriptors.add(result);

        return attributeDescriptors;
    }

    /**
     * supports O&M 1.0:
     */
    public void initializeFeature(OXFFeature feature, net.opengis.om.x10.MeasurementType xb_measurement) throws OXFException {
        super.initializeFeature(feature, xb_measurement);

        // create the RESULT-attribute:

        XmlCursor c = xb_measurement.getResult().newCursor();
        try {
            MeasureType xb_result = MeasureType.Factory.parse(c.getDomNode());
            
            OXFMeasureType measureResult = new OXFMeasureType(xb_result.getUom(),
                                                              xb_result.getDoubleValue());
            feature.setAttribute(RESULT, measureResult);
        }
        catch (XmlException e) {
            throw new OXFException(e);
        }
    }

    /**
     * 
     * @param feature
     * @param nameValue
     * @param descriptionValue
     * @param locationValue
     * @param timeValue
     * @param procedureValue
     * @param observedPropertyValue
     * @param featureOfInterestValue
     * @param resultValue
     */
    public void initializeFeature(OXFFeature feature,
                                  String[] nameValue,
                                  String descriptionValue,
                                  Geometry locationValue,
                                  ITime timeValue,
                                  String procedureValue,
                                  OXFPhenomenonPropertyType observedPropertyValue,
                                  OXFFeature featureOfInterestValue,
                                  OXFMeasureType resultValue) {
        super.initializeFeature(feature,
                                nameValue,
                                descriptionValue,
                                locationValue,
                                timeValue,
                                procedureValue,
                                observedPropertyValue,
                                featureOfInterestValue);

        feature.setAttribute(RESULT, resultValue);

    }
}