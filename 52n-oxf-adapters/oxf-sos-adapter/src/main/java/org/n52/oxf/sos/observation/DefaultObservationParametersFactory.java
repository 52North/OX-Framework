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