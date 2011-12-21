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
 
 Created on: 09.03.2006
 *********************************************************************************/

package org.n52.oxf.serviceAdapters.wms.caps;

import java.util.Locale;

import org.n52.oxf.owsCommon.capabilities.Dataset;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.owsCommon.capabilities.IDiscreteValueDomain;
import org.n52.oxf.owsCommon.capabilities.ITime;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class WMSLayer extends Dataset {

    /**
     * 
     * required
     */
    protected Style[] styles;

    /**
     * 
     */
    public WMSLayer(String title,
                    String identifier,
                    IBoundingBox[] boundingBoxes,
                    String[] outputFormats,
                    String[] availableCRSs,
                    Style[] styles,
                    String fees,
                    Locale[] language,
                    String pointOfContactString,
                    IDiscreteValueDomain<ITime> temporalDomain,
                    String abstractDescription,
                    String[] keywords) {
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

        this.styles = styles;
    }

    /**
     * 
     * @return
     */
    public Style[] getStyles() {
        return styles;
    }

}