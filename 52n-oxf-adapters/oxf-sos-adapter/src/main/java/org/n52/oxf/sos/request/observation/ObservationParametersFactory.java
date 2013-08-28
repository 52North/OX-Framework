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

package org.n52.oxf.sos.request.observation;

import javax.xml.namespace.QName;

public interface ObservationParametersFactory {

    /**
     * Creates an observation parameter assembly for well known observation types (as Count, or Measurement).
     * If given observation type does not match, {@link #createExtendedObservationFor(QName)} is called as a
     * fallback to let implementation specific factories create custom observation types.
     * 
     * @param type
     *        the observation type to create parameter assembly for.
     * @return an observation parameters assembly to create InsertObservation request.
     */
    ObservationParameters createObservationParametersFor(QName type);

    /**
     * A fallback which is called when no observation type matches in
     * {@link #createObservationParametersFor(QName)}. Can be overridden by implementors who wants to extend
     * already known observation types with custom types.
     * 
     * @param type
     *        which extended Observation type to create.
     * @return an observation parameters assembly to create InsertObservation request.
     */
    ObservationParameters createExtendedObservationFor(QName type);
}
