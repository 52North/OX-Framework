/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
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
    public ObservationParameters createObservationParametersFor(QName type);

    /**
     * A fallback which is called when no observation type matches in
     * {@link #createObservationParametersFor(QName)}. Can be overridden by implementors who wants to extend
     * already known observation types with custom types.
     * 
     * @param type
     *        which extended Observation type to create.
     * @return an observation parameters assembly to create InsertObservation request.
     */
    public ObservationParameters createExtendedObservationFor(QName type);
}
