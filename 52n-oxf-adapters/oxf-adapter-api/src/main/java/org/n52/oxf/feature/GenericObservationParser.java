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

package org.n52.oxf.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import net.opengis.gml.FeatureCollectionDocument2;
import net.opengis.gml.FeaturePropertyType;
import net.opengis.gml.x32.AbstractTimeObjectType;
import net.opengis.gml.x32.CodeWithAuthorityType;
import net.opengis.gml.x32.TimePositionType;
import net.opengis.gml.x32.impl.MeasureTypeImpl;
import net.opengis.gml.x32.impl.TimeInstantTypeImpl;
import net.opengis.om.x20.OMObservationType;
import net.opengis.samplingSpatial.x20.SFSpatialSamplingFeatureDocument;
import net.opengis.samplingSpatial.x20.SFSpatialSamplingFeatureType;
import net.opengis.swe.x101.DataArrayDocument;
import net.opengis.swe.x20.AbstractDataComponentType;
import net.opengis.swe.x20.DataArrayType;
import net.opengis.swe.x20.DataRecordType.Field;
import net.opengis.swe.x20.QuantityType;
import net.opengis.swe.x20.TextEncodingType;
import net.opengis.swe.x20.TextType;
import net.opengis.swe.x20.TimeType;
import net.opengis.waterml.x20.MonitoringPointDocument;
import net.opengis.waterml.x20.MonitoringPointType;
import net.opengis.waterml.x20.TimeValuePairPropertyType;
import net.opengis.waterml.x20.TimeValuePairType;
import net.opengis.waterml.x20.TimeseriesDocument;
import net.opengis.waterml.x20.TimeseriesObservationType;
import net.opengis.waterml.x20.TimeseriesType;
import net.opengis.waterml.x20.TimeseriesType.Point;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.feature.dataTypes.OXFMeasureType;
import org.n52.oxf.feature.dataTypes.OXFPhenomenonPropertyType;
import org.n52.oxf.feature.dataTypes.OXFScopedName;
import org.n52.oxf.feature.sos.SOSFoiStore;
import org.n52.oxf.owsCommon.capabilities.ITime;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.util.XmlBeansHelper;
import org.n52.oxf.valueDomains.time.TimeFactory;
import org.w3c.dom.Node;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class GenericObservationParser {

	private static final Logger LOGGER = LoggingHandler.getLogger(GenericObservationParser.class);
	
    /**
     * supports O&M 1.0
     * 
     * @param featureCollection
     *        the values contained in the generic observation will be added to this collection.
     * @param xb_genericObs
     *        the generic observation bean which shall be parsed.
     */
    public static void addElementsFromGenericObservation(OXFFeatureCollection features,
                                                         net.opengis.om.x10.ObservationType xb_genericObs) throws OXFException {
        try {
            if (xb_genericObs.getProcedure() == null) {
                LOGGER.debug("No observation to parse.");
                return;
            }
            
            //
            // parsing the procedure:
            //
            String procedure = xb_genericObs.getProcedure().getHref();
    
            //
            // parsing the featureOfInterest:
            //
            Map<String, OXFFeature> fois = new HashMap<String, OXFFeature>();
            FeaturePropertyType xb_foiProp = xb_genericObs.getFeatureOfInterest();
    
            XmlCursor c = xb_foiProp.newCursor();
            boolean isFeatureColl = c.toChild(new QName("http://www.opengis.net/gml", "FeatureCollection"));
    
            if (isFeatureColl) {
                FeatureCollectionDocument2 xb_featureColl = FeatureCollectionDocument2.Factory.parse(c.getDomNode());

                for (FeaturePropertyType xb_featureMember : xb_featureColl.getFeatureCollection().getFeatureMemberArray()) {

                    // the feature to add:
                    OXFFeature feature = new SOSFoiStore().parseFoi(xb_featureMember);
                    fois.put(feature.getID(), feature);
                }
            }

            Map<String, String> uoms = new HashMap<String, String>();
            List<String> definitions = new ArrayList<String>();
            List<String> types = new ArrayList<String>();
            List<String> names = new ArrayList<String>();
            
            XmlCursor cursor = xb_genericObs.getResult().newCursor();
            net.opengis.swe.x101.DataArrayDocument dataArray = parseFieldsForSWECommon101(uoms, definitions, types, names, cursor);
            
            // TODO Spec-Too-Flexible-Problem --> 3 encoding types are possible:
            net.opengis.swe.x101.TextBlockDocument.TextBlock xb_textBlock = dataArray.getDataArray1().getEncoding().getTextBlock();
    
            String decimalSeparator = xb_textBlock.getDecimalSeparator();
            String token = xb_textBlock.getTokenSeparator();
            String block = xb_textBlock.getBlockSeparator();
            String resultText = dataArray.getDataArray1().getValues().getDomNode().getFirstChild().getNodeValue();
            parseTextBlock(features, resultText, decimalSeparator, token, block, definitions, types, names, fois, uoms, procedure);
            
        } catch (Exception e) {
            throw new OXFException("Could not parse omObservationType.", e);
        }                                      
    }

    private static net.opengis.swe.x101.DataArrayDocument parseFieldsForSWECommon101(Map<String, String> uomMap,
                                                                                    List<String> definitionList,
                                                                                    List<String> typeList,
                                                                                    List<String> nameList,
                                                                                    XmlCursor cResult) throws XmlException {
        cResult.toChild(new QName("http://www.opengis.net/swe/1.0.1", "DataArray"));
        net.opengis.swe.x101.DataArrayDocument dataArray = DataArrayDocument.Factory.parse(cResult.getDomNode());

        net.opengis.swe.x101.AbstractDataRecordType dataRecord = dataArray.getDataArray1().getElementType().getAbstractDataRecord();
        
        // 1. in case of 'SimpleDataRecord':
        if (dataRecord instanceof net.opengis.swe.x101.SimpleDataRecordType) {
        	net.opengis.swe.x101.SimpleDataRecordType xb_simpleDataRecord = (net.opengis.swe.x101.SimpleDataRecordType) dataRecord;
        	net.opengis.swe.x101.AnyScalarPropertyType[] fields = xb_simpleDataRecord.getFieldArray();
            for (net.opengis.swe.x101.AnyScalarPropertyType anyScalar : fields) {
                if (anyScalar.getName() != null){
                    nameList.add(anyScalar.getName());
                } else {
                    nameList.add("");
                }
                if (anyScalar.isSetTime()) {
                    definitionList.add(anyScalar.getTime().getDefinition());
                    typeList.add("time");
                }
                else if (anyScalar.isSetText()) {
                    definitionList.add(anyScalar.getText().getDefinition());
                    typeList.add("text");
                }
                else if (anyScalar.isSetQuantity()) {
                    String quantityURN = anyScalar.getQuantity().getDefinition();
                    definitionList.add(quantityURN);
                    typeList.add("quantity");
   
                    String uomURN = anyScalar.getQuantity().getUom().getHref();
                    uomMap.put(quantityURN, uomURN);
                }
                else if (anyScalar.isSetCategory()) {
                    definitionList.add(anyScalar.getCategory().getDefinition());
                    typeList.add("category");
                }
                else if (anyScalar.isSetBoolean()) {
                    definitionList.add(anyScalar.getBoolean().getDefinition());
                    typeList.add("boolean");
                }
                else if (anyScalar.isSetCount()) {
                    definitionList.add(anyScalar.getCount().getDefinition());
                    typeList.add("count");
                }
                // ... TODO there are more possibilities...
            }
        }
        
        // 2. in case of 'DataRecord':
        else if (dataRecord instanceof net.opengis.swe.x101.DataRecordType) {
        	net.opengis.swe.x101.DataRecordType xb_dataRecord = (net.opengis.swe.x101.DataRecordType) dataRecord;
        	net.opengis.swe.x101.DataComponentPropertyType[] fields = xb_dataRecord.getFieldArray();
        	for (net.opengis.swe.x101.DataComponentPropertyType field : fields) {
            	if (field.getName() != null){
            		nameList.add(field.getName());
            	} else{
            		nameList.add("");
            	}
                if (field.isSetTime()) {
                    definitionList.add(field.getTime().getDefinition());
                    typeList.add("time");
                }
                else if (field.isSetText()) {
                    definitionList.add(field.getText().getDefinition());
                    typeList.add("text");
                }
                else if (field.isSetQuantity()) {
                    String quantityURN = field.getQuantity().getDefinition();
                    definitionList.add(quantityURN);
                    typeList.add("quantity");
   
                    String uomURN = field.getQuantity().getUom().getCode();
                    uomMap.put(quantityURN, uomURN);
                }
                else if (field.isSetCategory()) {
                    definitionList.add(field.getCategory().getDefinition());
                    typeList.add("category");
                }
                else if (field.isSetBoolean()) {
                    definitionList.add(field.getBoolean().getDefinition());
                    typeList.add("boolean");
                }
                else if (field.isSetBoolean()) {
                    definitionList.add(field.getCount().getDefinition());
                    typeList.add("count");
                }
                else {
                	LOGGER.warn("Could not parse following resultData: "+ field.toString());
                }
                // ... TODO there are more possibilities...
            }
        }
        return dataArray;
    }
    
    public static void addElementsFromTimeSeries(
			OXFFeatureCollection featureCollection,
			TimeseriesObservationType xb_timeseriesObsType) throws OXFException {
    	
        if (xb_timeseriesObsType.getProcedure() == null) {
            LOGGER.debug("No timeseries to parse.");
            return;
        }
        
    	// procedure
    	String procedure = xb_timeseriesObsType.getProcedure().getTitle();

    	// featureOfInterest
    	String foiID = null;
    	net.opengis.gml.x32.FeaturePropertyType foiType = xb_timeseriesObsType.getFeatureOfInterest();
    	XmlCursor cFoi = foiType.newCursor();
    	cFoi.toFirstChild();
    	try {
			XmlObject foi = XmlObject.Factory.parse(cFoi.getObject().getDomNode());
			if (foi instanceof MonitoringPointDocument) {
				MonitoringPointDocument monPointDoc = (MonitoringPointDocument) foi;
				MonitoringPointType monPoint = monPointDoc.getMonitoringPoint();
				CodeWithAuthorityType identifier = monPoint.getIdentifier();
				foiID = identifier.getStringValue();
			}
		} catch (XmlException e) {
			throw new OXFException(e);
		}
    	
    	OXFSamplingPointType pointType = new OXFSamplingPointType();
    	OXFFeature foi = new OXFFeature(foiID, pointType);
    	
    	// phenomenonURN
    	String phenomenonURN = xb_timeseriesObsType.getObservedProperty().getTitle();
    	
    	// result
    	XmlCursor cResult = xb_timeseriesObsType.getResult().newCursor();
    	cResult.toFirstChild();
    	try {
			XmlObject xb_timeseries = XmlObject.Factory.parse(cResult.getObject().getDomNode());
			if (xb_timeseries instanceof TimeseriesDocument) {
		     	TimeseriesType timeseries = ((TimeseriesDocument) xb_timeseries).getTimeseries();
				TimeValuePairPropertyType defaultTimeValuePair = timeseries.getDefaultTimeValuePair();
				String uomURN = defaultTimeValuePair.getTimeValuePair().getUnitOfMeasure().getUom();
				Point[] pointArray = timeseries.getPointArray();
				for (Point point : pointArray) {
			    	OXFMeasurementType oxf_measurementType = new OXFMeasurementType();
			        OXFFeature feature = new OXFFeature("anyID", oxf_measurementType);
			        
					TimeValuePairType timeValuePair = point.getTimeValuePair();
					
					String timeString = timeValuePair.getTime().getStringValue();
					OXFMeasureType resultValue = new OXFMeasureType(uomURN, timeValuePair.getValue());
					
			        oxf_measurementType.initializeFeature(feature,
			                new String[] {phenomenonURN},
			                "anyDescription",
			                null,// featureOfInterestValue.getGeometry(),
			                TimeFactory.createTime(timeString),
			                procedure,
			                new OXFPhenomenonPropertyType(phenomenonURN, uomURN),
			                foi,
			                resultValue);
			        
			        featureCollection.add(feature);
				}
			}
			
		} catch (XmlException e) {
			throw new OXFException(e);
		}
	}

    public static void addElementsFromGenericObservation(OXFFeatureCollection features, OMObservationType omObservation) throws OXFException {
        try {
            Map<String, OXFFeature> fois = new HashMap<String, OXFFeature>();
            String procedure = omObservation.getProcedure().getHref();
            if (isSFSpatialSamplingFeature(omObservation)) {
                Node node = omObservation.getFeatureOfInterest().getDomNode(); // foi is abstract
                XmlObject featureObject = XmlBeansHelper.parseXmlObjectFromDomNode(node, "SF_SpatialSamplingFeature");
                SFSpatialSamplingFeatureDocument featureDocument = (SFSpatialSamplingFeatureDocument) featureObject;
                SFSpatialSamplingFeatureType spatialSamplingFeatureType = featureDocument.getSFSpatialSamplingFeature();
                OXFFeature feature = new SOSFoiStore().parseFoi(spatialSamplingFeatureType);
                fois.put(feature.getID(), feature);
            }
    
            XmlObject result = omObservation.getResult();
            if (result instanceof MeasureTypeImpl) {
				features.add(createFeature(omObservation, procedure));
			} else {
				Map<String, String> uoms = new HashMap<String, String>();
	            List<String> definitions = new ArrayList<String>();
	            List<String> types = new ArrayList<String>();
	            List<String> names = new ArrayList<String>();
	            net.opengis.swe.x20.DataArrayType dataArray = parseFieldsForSWECommon200(uoms, definitions, types, names, result.newCursor());
	            TextEncodingType xb_textBlock = (TextEncodingType) dataArray.getEncoding().getAbstractEncoding();
	            String decimalSeparator = xb_textBlock.getDecimalSeparator();
	            String token = xb_textBlock.getTokenSeparator();
	            String block = xb_textBlock.getBlockSeparator();
	            String resultText = dataArray.getValues().getDomNode().getFirstChild().getNodeValue();
	            parseTextBlock(features, resultText, decimalSeparator, token, block, definitions, types, names, fois, uoms, procedure);
			}
        }
        catch (Exception e) {
            throw new OXFException("Parsing observationType failed.", e);
        }
    }

    private static OXFFeature createFeature(OMObservationType omObservation, String procedure) {
    	MeasureTypeImpl result = (MeasureTypeImpl) omObservation.getResult();
    	OXFMeasurementType oxf_measurementType = new OXFMeasurementType();
        OXFFeature feature = new OXFFeature(omObservation.getFeatureOfInterest().getHref(), oxf_measurementType);
        AbstractTimeObjectType abstractTimeObject = omObservation.getPhenomenonTime().getAbstractTimeObject();
        ITime time = null;
        if (abstractTimeObject instanceof TimeInstantTypeImpl) {
        	TimePositionType timePosition = ((TimeInstantTypeImpl) abstractTimeObject).getTimePosition();
        	time = TimeFactory.createTime(timePosition.getStringValue());
		}
        String urn = omObservation.getObservedProperty().getHref();
        String uom = result.getUom();
        OXFPhenomenonPropertyType phenPropType = new OXFPhenomenonPropertyType(urn, uom);
        OXFMeasureType resultValue = new OXFMeasureType(uom, result.getDoubleValue());
    	
        oxf_measurementType.initializeFeature(feature,
                                              null,
                                              "anyDescription",
                                              null,// featureOfInterestValue.getGeometry(),
                                              time,
                                              procedure,
                                              phenPropType,
                                              feature,
                                              resultValue);
    	return feature;
	}

	private static DataArrayType parseFieldsForSWECommon200(Map<String, String> uoms,
                                                                List<String> definitions,
                                                                List<String> types,
                                                                List<String> names,
                                                                XmlCursor cResult) throws Exception {
        boolean hasDataArray = cResult.toChild(new QName("http://www.opengis.net/swe/2.0", "DataArray"));
        if (!hasDataArray) {
            throw new OXFException("No DataArray@http://www.opengis.net/swe/2.0 representing data structure.");
        }
        
        net.opengis.swe.x20.DataArrayType dataArrayType = (net.opengis.swe.x20.DataArrayType) cResult.getObject();
        AbstractDataComponentType dataComponent = dataArrayType.getElementType().getAbstractDataComponent();

        // 1. in case of 'DataRecord':
        if (dataComponent instanceof net.opengis.swe.x20.DataRecordType) {
            net.opengis.swe.x20.DataRecordType dataRecord = (net.opengis.swe.x20.DataRecordType) dataComponent;
           
            Field[] fields = dataRecord.getFieldArray();
                
            for (Field field : fields) {
                if (field.getName() != null){
                    names.add(field.getName());
                } else {
                    names.add("");
                }
                AbstractDataComponentType dataComponentType = field.getAbstractDataComponent();
                if (dataComponentType instanceof TimeType) {
                    TimeType time = (TimeType) dataComponentType;
                    //time.getUom().getHref();
                    definitions.add(time.getDefinition());
                    types.add("time");
                } else if (dataComponentType instanceof TextType) {
                    TextType text = (TextType) dataComponentType;
                    definitions.add(text.getDefinition());
                    types.add("text");
                } else if (dataComponentType instanceof QuantityType) {
                    QuantityType quantity = (QuantityType) dataComponentType;
                    String definition = quantity.getDefinition();
                    definitions.add(definition);
                    types.add("quantity");
                    
                    String uomURN = quantity.getUom().getCode();
                    uoms.put(definition, uomURN);
                }
                
                // ... TODO there are more possibilities...
            }
        }
        return dataArrayType;
    }

    private static boolean isSFSpatialSamplingFeature(OMObservationType omObservation) {
        XmlCursor c = omObservation.getFeatureOfInterest().newCursor();
        return c.toChild(new QName("http://www.opengis.net/samplingSpatial/2.0", "SF_SpatialSamplingFeature"));
    }

    /**
     * 
     * @param featureCollection
     *        the collection where the single observed values shall be added to.
     * @param result
     *        the result's text block.
     */
    private static void parseTextBlock(OXFFeatureCollection featureCollection,
                                       String result,
                                       String decimalSeparator,
                                       String tokenSeparator,
                                       String blockSeparator,
                                       List<String> definitions,
                                       List<String> types,
                                       List<String> names,
                                       Map<String, OXFFeature> fois,
                                       Map<String, String> uoms,
                                       String procedure) {

        String[] blocks = result.split(blockSeparator);

        for (String block : blocks) {
            String[] tokens = block.split(tokenSeparator);

            if (tokens.length > 0) {
                String time = tokens[0];

                OXFFeature foi = null;

                //
                // for each phenomenon: add an observation to the collection
                //
                for (int i = 0; i < definitions.size(); i++) {

                    if (definitions.get(i).equals("urn:ogc:data:time:iso8601") 
                            || definitions.get(i).equals("http://www.opengis.net/def/property/OGC/0/SamplingTime") 
                            || definitions.get(i).equals("http://www.opengis.net/def/uom/ISO-8601/0/Gregorian")) {
                        // do nothing
                    }
                    
                    //else if(nameList.get(i).equalsIgnoreCase("SamplingTime")) {
                    //  //dn -> DLR time urn
                    //}
                    else if (definitions.get(i).equals("urn:ogc:data:feature") 
                            || definitions.get(i).equals("http://www.opengis.net/def/property/OGC/0/FeatureOfInterest")) {
                        String foiID = tokens[i];
                        foi = fois.get(foiID);
                    }
                    else if (types.get(i).equals("quantity")) {
                        String phenomenonURN = definitions.get(i);
                        String phenomenValue = tokens[i];
                        String uomURN = uoms.get(phenomenonURN);

                        OXFMeasurementType oxf_measurementType = new OXFMeasurementType();
                        OXFFeature feature = new OXFFeature("anyID", oxf_measurementType);

                        OXFMeasureType resultValue = null;
                        if (phenomenValue.equalsIgnoreCase("nodata")) {
                            resultValue = new OXFMeasureType(uomURN, Double.NaN);
                        }
                        else {
                            phenomenValue = phenomenValue.replace(decimalSeparator, ".");
                            resultValue = new OXFMeasureType(uomURN, Double.valueOf(phenomenValue));
                        }

                        if (foi == null) { // if no foi is listed in encoded result
                            Iterator<OXFFeature> iterator = fois.values().iterator();
                            if (iterator.hasNext()) {
                                foi = iterator.next();
                            } else {
                                // TODO change Exception type to checked Exception
                                throw new RuntimeException("no corresponding foi found in encoded result.");
                            }
                        }
                        oxf_measurementType.initializeFeature(feature,
                                                              new String[] {names.get(i)},
                                                              "anyDescription",
                                                              null,// featureOfInterestValue.getGeometry(),
                                                              TimeFactory.createTime(time),
                                                              procedure,
                                                              new OXFPhenomenonPropertyType(phenomenonURN, uomURN),
                                                              foi,
                                                              resultValue);
                        featureCollection.add(feature);
                    }
                    else if (types.get(i).equals("category")) {
                        String phenomenonURN = definitions.get(i);
                        String phenomenValue = tokens[i];

                        OXFCategoryObservationType oxf_categoryType = new OXFCategoryObservationType();
                        OXFFeature feature = new OXFFeature("anyID", oxf_categoryType);

                        OXFScopedName resultValue = new OXFScopedName("anyCode", phenomenValue);

                        oxf_categoryType.initializeFeature(feature, new String[] {names.get(i)}, "anyDescription", null,// featureOfInterestValue.getGeometry(),
                                                           TimeFactory.createTime(time),
                                                           procedure,
                                                           new OXFPhenomenonPropertyType(phenomenonURN),
                                                           foi,
                                                           resultValue);
                        featureCollection.add(feature);
                    }
                    else if (types.get(i).equals("boolean")) {
                        String phenomenonURN = definitions.get(i);
                        String phenomenValue = tokens[i];

                        OXFTruthObservationType oxf_TruthType = new OXFTruthObservationType();
                        OXFFeature feature = new OXFFeature("anyID", oxf_TruthType);

                        Boolean resultValue = Boolean.parseBoolean(phenomenValue);

                        oxf_TruthType.initializeFeature(feature, new String[] {names.get(i)}, "anyDescription", null,// featureOfInterestValue.getGeometry(),
                                                           TimeFactory.createTime(time),
                                                           procedure,
                                                           new OXFPhenomenonPropertyType(phenomenonURN),
                                                           foi,
                                                           resultValue);
                        
                        featureCollection.add(feature);
                    }
                    else if (types.get(i).equals("count")) {
                        String phenomenonURN = definitions.get(i);
                        String phenomenValue = tokens[i];

                        OXFCountObservationType oxf_CountType = new OXFCountObservationType();
                        OXFFeature feature = new OXFFeature("anyID", oxf_CountType);

                        Number resultValue = Integer.parseInt(phenomenValue);

                        oxf_CountType.initializeFeature(feature, new String[] {names.get(i)}, "anyDescription", null,// featureOfInterestValue.getGeometry(),
                                                           TimeFactory.createTime(time),
                                                           procedure,
                                                           new OXFPhenomenonPropertyType(phenomenonURN),
                                                           foi,
                                                           resultValue);
                        featureCollection.add(feature);
                    }
                    else if (types.get(i).equals("time")) {
                        String phenomenonURN = definitions.get(i);
                        String phenomenValue = tokens[i];

                        OXFTemporalObservationType oxf_TimeType = new OXFTemporalObservationType();
                        OXFFeature feature = new OXFFeature("anyID", oxf_TimeType);

                        ITime resultValue = TimeFactory.createTime(phenomenValue);

                        oxf_TimeType.initializeFeature(feature, new String[] {names.get(i)}, "anyDescription", null,// featureOfInterestValue.getGeometry(),
                                                           TimeFactory.createTime(time),
                                                           procedure,
                                                           new OXFPhenomenonPropertyType(phenomenonURN),
                                                           foi,
                                                           resultValue);
                        featureCollection.add(feature);
                    }
                }
            }
        }
    }

}