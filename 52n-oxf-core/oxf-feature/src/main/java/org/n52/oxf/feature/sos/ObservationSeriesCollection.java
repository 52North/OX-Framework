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
package org.n52.oxf.feature.sos;

import static org.n52.oxf.feature.OXFAbstractObservationType.FEATURE_OF_INTEREST;
import static org.n52.oxf.feature.OXFAbstractObservationType.OBSERVED_PROPERTY;
import static org.n52.oxf.feature.OXFAbstractObservationType.PROCEDURE;
import static org.n52.oxf.feature.OXFAbstractObservationType.SAMPLING_TIME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.n52.oxf.feature.OXFAbstractObservationType;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.OXFMeasurementType;
import org.n52.oxf.feature.dataTypes.IObservationResult;
import org.n52.oxf.feature.dataTypes.OXFPhenomenonPropertyType;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.n52.oxf.valueDomains.time.TimePosition;

public class ObservationSeriesCollection {

    /**
     * associates a feature-id (String) with a Map<ITimePosition, ObservedValueTuple>.
     */
    private Map<String, Map<ITimePosition, ObservedValueTuple>> featureMap;

    private Set<ITimePosition> timeSet;

    private Comparable[] maximumValues;
    private Comparable[] minimumValues;

    private String[] observedPropertyArray;

    public ObservationSeriesCollection(OXFFeatureCollection observationCollection,
                                   Set<OXFFeature> featuresSet,
                                   String[] observedProperties,
                                   boolean onlyCompleteTuples) {

        String[] featureIDArray = new String[featuresSet.size()];
        Iterator iter = featuresSet.iterator();
        int i = 0;
        while (iter.hasNext()) {
            featureIDArray[i] = ((OXFFeature) iter.next()).getID();
            i++;
        }
        timeSet = new HashSet<>();

        if (observedProperties != null) {
            observedPropertyArray = observedProperties.clone();
            featureMap = findObservedValueTuples4FOI(observationCollection,
                    featureIDArray,
                    observedProperties,
                    onlyCompleteTuples);
        }

    }

    /**
     *
     */
    public ObservationSeriesCollection(OXFFeatureCollection observationCollection,
                                   String[] featureIDArray,
                                   String[] observedProperties,
                                   boolean onlyCompleteTuples) {

        timeSet = new HashSet<>();
        if (observedProperties != null) {
            observedPropertyArray = observedProperties.clone();
            featureMap = findObservedValueTuples4FOI(observationCollection,
                    featureIDArray,
                    observedProperties,
                    onlyCompleteTuples);
        }
    }

    public ObservationSeriesCollection(OXFFeatureCollection observationCollection,
                                       String[] featureIDArray,
                                       String[] observedProperties,
                                       String[] procedureNames,
                                       boolean onlyCompleteTuples) {
        if (observationCollection == null) {
            throw new NullPointerException("No observation given.");
        }
        timeSet = new HashSet<>();
        featureMap = findObservedValueTuples4FOI(observationCollection,
                                                 featureIDArray,
                                                 observedProperties,
                                                 procedureNames,
                                                 onlyCompleteTuples);
    }

    /**
     *
     * @param indexOfObservedProperty
     * @return
     */
    public Comparable getMaximum(int indexOfObservedProperty) {
        return maximumValues[indexOfObservedProperty];
    }
    public Comparable getMaximum(String observedPropertyName) {
        for (int i = 0; i < observedPropertyArray.length; i++) {
            if (observedPropertyArray[i].equals(observedPropertyName)) {
                return maximumValues[i];
            }
        }
        return null;
    }

    /**
     *
     * @param indexOfObservedProperty
     * @return
     */
    public Comparable getMinimum(int indexOfObservedProperty) {
        return minimumValues[indexOfObservedProperty];
    }

    public Comparable getMinimum(String observedPropertyName) {
        for (int i=0; i<observedPropertyArray.length; i++) {
            if (observedPropertyArray[i].equals(observedPropertyName)) {
                return minimumValues[i];
            }
        }
        return null;
    }

    public List<ObservedValueTuple> getAllTuples() {
        List<ObservedValueTuple> res = new ArrayList<>();

        for (Map<ITimePosition, ObservedValueTuple> featureID : featureMap.values()) {
            res.addAll(featureID.values());
        }

        return res;
    }

    public Map<ITimePosition, ObservedValueTuple> getAllTuples(OXFFeature foi) {
        return featureMap.get(foi.getID());
    }

    public Map<ITimePosition, ObservedValueTuple> getAllTuples(String foiID) {
        return featureMap.get(foiID);
    }

    public ObservedValueTuple getTuple(OXFFeature feature, ITimePosition timePos) {
        for (Map.Entry<String, Map<ITimePosition, ObservedValueTuple>> featureEntry : featureMap.entrySet()) {
            if (feature.getID().equals(featureEntry.getKey())) {
                Map<ITimePosition, ObservedValueTuple> timeTupleMap = featureEntry.getValue();
                for (Map.Entry<ITimePosition, ObservedValueTuple> timeEntry : timeTupleMap.entrySet()) {
                    if (timePos.equals(timeEntry.getKey())) {
                        return timeEntry.getValue();
                    }
                }
            }
        }
        return null;
    }

    public ITimePosition[] getSortedTimeArray() {
        ITimePosition[] timeArray = new ITimePosition[timeSet.size()];
        timeSet.toArray(timeArray);
        Arrays.sort(timeArray);
        return timeArray;
    }

    protected Map<String, Map<ITimePosition, ObservedValueTuple>> findObservedValueTuples4FOI(OXFFeatureCollection observationCollection,
                                                                                              String[] featureIDArray,
                                                                                              String[] observedPropertyNames,
                                                                                              boolean onlyCompleteTuples) {
        Map<String, Map<ITimePosition, ObservedValueTuple>> resultMap = new HashMap<>();

        //
        // firstly, initialize maximum- and minimum-arrays:
        //
        maximumValues = new Comparable[observedPropertyNames.length];
        minimumValues = new Comparable[observedPropertyNames.length];

        //
        // now iterate over features:
        //
        for (String featureID : featureIDArray) {
            Map<String, ObservedValueTuple> tupleMap = new HashMap<>();

            for (OXFFeature observation : observationCollection) {

                Object foiAttribute = observation.getAttribute(FEATURE_OF_INTEREST);
                if (foiAttribute != null) {
                    OXFFeature featureOfInterest = (OXFFeature) foiAttribute;

                    // System.out.println("\"" + featureOfInterest.getID() + "\",");
                    if (featureOfInterest.getID().equals(featureID)) {

                        ITimePosition time = (ITimePosition) observation.getAttribute(SAMPLING_TIME);
                        String timeString = time.toISO8601Format();

                        ObservedValueTuple tuple = new ObservedValueTuple(observedPropertyNames.length,
                                                                          observedPropertyNames,
                                                                          time);
                        if (tupleMap.containsKey(timeString)) {
                            tuple = tupleMap.get(timeString);
                        }

                        IObservationResult measureResult = (IObservationResult) observation.getAttribute(OXFMeasurementType.RESULT);
                        Object result = measureResult.getValue();

                        OXFPhenomenonPropertyType observedProperty = (OXFPhenomenonPropertyType) observation.getAttribute(OBSERVED_PROPERTY);

                        String procedure = (String)observation.getAttribute(PROCEDURE);

                        for (int i = 0; i < observedPropertyNames.length; i++) {
                            if (observedProperty.getURN().equals(observedPropertyNames[i])) {
                                tuple.setValue(i, result);
                            }
                        }
                        tupleMap.put(timeString, tuple);

                    }
                }
            }

            Map<ITimePosition, ObservedValueTuple> tupleMap_corrected = new HashMap<>();
            for (Map.Entry<String, ObservedValueTuple> tupleEntry : tupleMap.entrySet()) {
                ObservedValueTuple tuple = tupleEntry.getValue();

                boolean completeTuple = true;

                // check if all values are setted
                for (int i = 0; i < tuple.dimension(); i++) {
                    if (tuple.getValue(i) == null) {
                        completeTuple = false;
                        break;
                    }
                }

                if (onlyCompleteTuples == false) {
                    completeTuple = true;
                }

                if (completeTuple) {
                    ITimePosition time = new TimePosition(tupleEntry.getKey());

                    tupleMap_corrected.put(time, tuple);

                    boolean contained = false;
                    for (ITimePosition timePos : timeSet) {
                        if (timePos.equals(time)) {
                            contained = true;
                            break;
                        }
                    }
                    if ( !contained) {
                        timeSet.add(time);
                    }

                    for (int i = 0; i < tuple.dimension(); i++) {
                        if (tuple.getValue(i) instanceof Comparable) {
                            Comparable c = (Comparable) tuple.getValue(i);
                            if (maximumValues[i] == null || c.compareTo(maximumValues[i]) > 0) {
                                maximumValues[i] = c;
                            }
                            if (minimumValues[i] == null || c.compareTo(minimumValues[i]) < 0) {
                                minimumValues[i] = c;
                            }
                        }
                    }
                }
            }
            if (tupleMap_corrected.size() > 0) {
                resultMap.put(featureID, tupleMap_corrected);
            }
        }

        return resultMap;
    }


    protected Map<String, Map<ITimePosition, ObservedValueTuple>> findObservedValueTuples4FOI(OXFFeatureCollection observationCollection,
                                                                                              String[] featureIDArray,
                                                                                              String[] observedPropertyNames,
                                                                                              String[] procedureNames,
                                                                                              boolean onlyCompleteTuples) {
        Map<String, Map<ITimePosition, ObservedValueTuple>> resultMap = new HashMap<>();

        //
        // firstly, initialize maximum- and minimum-arrays:
        //
        maximumValues = new Comparable[observedPropertyNames.length];
        minimumValues = new Comparable[observedPropertyNames.length];

        //
        // now iterate over features:
        //
        for (String featureID : featureIDArray) {
            Map<String, ObservedValueTuple> tupleMap = new HashMap<>();

            for (OXFFeature observation : observationCollection) {

                Object foiAttribute = observation.getAttribute(OXFAbstractObservationType.FEATURE_OF_INTEREST);
                if (foiAttribute != null) {
                    OXFFeature featureOfInterest = (OXFFeature) foiAttribute;

                    if (featureOfInterest.getID().equals(featureID)) {

                        ITimePosition time = (ITimePosition) observation.getAttribute(OXFAbstractObservationType.SAMPLING_TIME);
                        String timeString = time.toISO8601Format();

                        ObservedValueTuple tuple = new ObservedValueTuple(observedPropertyNames.length,
                                                                          observedPropertyNames,
                                                                          time);
                        if (tupleMap.containsKey(timeString)) {
                            tuple = tupleMap.get(timeString);
                        }

                        IObservationResult measureResult = (IObservationResult) observation.getAttribute(OXFMeasurementType.RESULT);
                        Object result = measureResult.getValue();

                        OXFPhenomenonPropertyType observedProperty = (OXFPhenomenonPropertyType) observation.getAttribute(OXFAbstractObservationType.OBSERVED_PROPERTY);

                        String procedure = (String)observation.getAttribute(OXFAbstractObservationType.PROCEDURE);

                        for (int i = 0; i < observedPropertyNames.length; i++) {
                            if (observedProperty.getURN().equals(observedPropertyNames[i])) {
                                if (procedure.equals(procedureNames[i])){
                                    tuple.setValue(i, result);
                                }
                            }
                        }
                        tupleMap.put(timeString, tuple);
                    }
                }
            }

            Map<ITimePosition, ObservedValueTuple> tupleMap_corrected = new HashMap<>();
            for (Map.Entry<String, ObservedValueTuple> tupleEntry : tupleMap.entrySet()) {
                ObservedValueTuple tuple = tupleEntry.getValue();

                boolean completeTuple = true;

                // check if all values are setted
                for (int i = 0; i < tuple.dimension(); i++) {
                    if (tuple.getValue(i) == null) {
                        completeTuple = false;
                        break;
                    }
                }

                if (onlyCompleteTuples == false) {
                    completeTuple = true;
                }

                if (completeTuple) {
                    ITimePosition time = new TimePosition(tupleEntry.getKey());

                    tupleMap_corrected.put(time, tuple);

                    boolean contained = false;
                    for (ITimePosition timePos : timeSet) {
                        if (timePos.equals(time)) {
                            contained = true;
                            break;
                        }
                    }
                    if ( !contained) {
                        timeSet.add(time);
                    }

                    for (int i = 0; i < tuple.dimension(); i++) {
                        if (tuple.getValue(i) instanceof Comparable) {
                            Comparable c = (Comparable) tuple.getValue(i);
                            if (maximumValues[i] == null || c.compareTo(maximumValues[i]) > 0) {
                                maximumValues[i] = c;
                            }
                            if (minimumValues[i] == null || c.compareTo(minimumValues[i]) < 0) {
                                minimumValues[i] = c;
                            }
                        }
                    }
                }
            }
            if (tupleMap_corrected.size() > 0) {
                resultMap.put(featureID, tupleMap_corrected);
            }
        }
        return resultMap;
    }
}
