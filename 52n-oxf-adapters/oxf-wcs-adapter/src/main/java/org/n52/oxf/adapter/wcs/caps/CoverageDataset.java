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

package org.n52.oxf.adapter.wcs.caps;

import java.util.Locale;

import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.capabilities.Dataset;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.owsCommon.capabilities.IDiscreteValueDomain;
import org.n52.oxf.owsCommon.capabilities.ITime;

public class CoverageDataset extends Dataset {
	
    // new in Version 1.1.1 of WCS
    
    private String gridBaseCRS;
    private String gridType;
    private double[] gridOrigin;
    private double[] gridOffset;
    private String gridCS;
	
    private String rpcLink;
	
    /**
     * this constructor has ALL attributes of the class as its parameters.(Also WCS 1.1.1)
     * 
     * @param title
     * @param identifier
     * @param boundingBoxes
     * @param outputFormats
     * @param availableCRSs
     * @param fees
     * @param language
     * @param pointOfContactString
     * @throws OXFException
     */
    	public CoverageDataset(String title,
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
                   String gridBaseCRS,
                   String gridType,
                   double[] gridOrigin,
                   double[] gridOffset,
                   String gridCS,
                   String rpcLink) {
        super(title,identifier,boundingBoxes, outputFormats,availableCRSs,fees,language,pointOfContactString,temporalDomain, abstractDescription, keywords);
        setGridBaseCRS(gridBaseCRS);
        setGridType(gridType);
        setGridOffset(gridOffset);
        setGridOrigin(gridOrigin);
        setGridCS(gridCS);
        this.rpcLink = rpcLink;
    }
    
    	public String getGridBaseCRS() {
    		return gridBaseCRS;
    	}

    	protected void setGridBaseCRS(String gridBaseCRS) {
    		this.gridBaseCRS = gridBaseCRS;
    	}

    	public String getGridType() {
    		return gridType;
    	}

    	protected void setGridType(String gridType) {
    		this.gridType = gridType;
    	}

    	public double[] getGridOrigin() {
    		return gridOrigin;
    	}

    	protected void setGridOrigin(double[] gridOrigin) {
    		this.gridOrigin = gridOrigin;
    	}

    	public double[] getGridOffset() {
    		return gridOffset;
    	}

    	protected void setGridOffset(double[] gridOffset) {
    		this.gridOffset = gridOffset;
    	}

    	public String getGridCS() {
    		return gridCS;
    	}

    	protected void setGridCS(String gridCS) {
    		this.gridCS = gridCS;
    	}
    	
    	public String getRpcLink () {
    	    return rpcLink;
    	}
}
