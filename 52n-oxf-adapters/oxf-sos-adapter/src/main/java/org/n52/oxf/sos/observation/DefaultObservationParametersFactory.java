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
package org.n52.oxf.sos.observation;

import static org.n52.oxf.xml.XMLConstants.QNAME_OM_1_0_MEASUREMENT_OBSERVATION;
import static org.n52.oxf.xml.XMLConstants.QNAME_OM_1_0_TEXT;
import static org.n52.oxf.xml.XMLConstants.QNAME_OM_1_0_TRUTH_OBSERVATION;

import javax.xml.namespace.QName;

import org.n52.oxf.xml.XMLConstants;

/**
 * A default implementation of {@link ObservationParametersFactory}. By default the following observation
 * types are known and {@link ObservationParameters} implementations are available:<br>
 * <ul>
 * <li>{@link MeasurementObservationParameters} for type {@link XMLConstants#QNAME_OM_1_0_MEASUREMENT_OBSERVATION}</li>
 * <li>{@link TextObservationParameters} for type {@link XMLConstants#QNAME_OM_1_0_TEXT_OBSERVATION}</li>
 * <li>{@link BooleanObservationParameters} for type {@link XMLConstants#QNAME_OM_1_0_TRUTH_OBSERVATION}</li>
 * <li>{@link CountObservationParameters} for type {@link XMLConstants#QNAME_OM_1_0_COUNT_OBSERVATION}</li>
 * </ul>
 * The {@link DefaultObservationParametersFactory} can be subclassed to provide further types. Override
 * {@link #createExtendedObservationFor(QName)} to create other {@link QName}s or provide your own
 * implelmentation.
 */
public class DefaultObservationParametersFactory implements ObservationParametersFactory {

    public final ObservationParameters createObservationParametersFor(QName type) {
        if (QNAME_OM_1_0_MEASUREMENT_OBSERVATION.equals(type)) {
            // Create observation builder for measurements.
            return new MeasurementObservationParameters();
        }
        else if (QNAME_OM_1_0_TEXT.equals(type)) {
            // Create observation builder for texts.
            return new TextObservationParameters();
        }
        else if (QNAME_OM_1_0_TRUTH_OBSERVATION.equals(type)) {
            // Create observation builder for booleans.
            return new BooleanObservationParameters();
        }
        else if (XMLConstants.QNAME_OM_1_0_COUNT_OBSERVATION.equals(type)) {
            // Create observation builder for counts.
            return new CountObservationParameters();
        }
        else {
            return createExtendedObservationFor(type);
        }
    }

    public ObservationParameters createExtendedObservationFor(QName type) {
        throw new UnsupportedOperationException("Not supported by default implementation.");
    }
}