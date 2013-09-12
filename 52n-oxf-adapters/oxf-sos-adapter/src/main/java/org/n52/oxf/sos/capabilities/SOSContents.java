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

import java.util.List;

import org.n52.oxf.ows.capabilities.Contents;
import org.n52.oxf.ows.capabilities.Dataset;

/**
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class SOSContents extends Contents {

    public SOSContents() {
        super();
    }

    public SOSContents(final List<ObservationOffering> observationOfferingList) {
        super();

        for (final ObservationOffering offering : observationOfferingList) {
            super.addDataIdentication(offering);
        }
    }

    /**
     * override this superclass method to avoid adding Dataset objects.
     */
    @Override
    public void addDataIdentication(final Dataset dataID) {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     */
    @Override
    public ObservationOffering getDataIdentification(final int i) {
        return (ObservationOffering) super.getDataIdentification(i);
    }

    /**
     * 
     * @param identifier
     * @return the <code>ObservationOffering</code> with the specified identifier/name or <code>null</code> if
     *         there is no dataset with the specified identifier/name.
     */
    @Override
    public ObservationOffering getDataIdentification(final String identifier) {
        return (ObservationOffering) super.getDataIdentification(identifier);
    }

}