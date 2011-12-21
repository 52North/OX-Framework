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
 
 Created on: 30.03.2005
 *********************************************************************************/

package org.n52.oxf.valueDomains.spatial;

import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.owsCommon.capabilities.IRangeValueDomain;

/**
 * This represents a standard BoundingBox. Does not any operations like zoom etc..
 * 
 * @see <a href=BoundingBox2D.html>BoundingBox2D</a>
 * @see <a href=BoundingBox3D.html>BoundingBox3D</a>
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class BoundingBox implements IBoundingBox, IRangeValueDomain<IBoundingBox> {

    public static final String ERROR_INPUT_COORDINATES = "input coordinates have a different dimension";
    public static final String ERROR_NUM_OF_COORINDATE_DIFFER = "The dimension is not homogenous in upperRight and lowerLeft";

    /**
     * stores the coordinates of the lowerCorner. Order: { min_x, min_y (min_z) } (respectively: {Easting,
     * Northing, (Height)} )
     */
    protected double[] lowerCorner;

    /**
     * stores the coordinates of the upperCorner. Order: { max_x, max_y (max_z) } (respectively: {Easting,
     * Northing, (Height)} )
     */
    protected double[] upperCorner;

    protected String crs;
    protected int dimensions;

    /**
     * this constructor has all required attributes as its parameters.
     * 
     * @param lowerLeft
     *        the lowerCorner
     * @param upperRight
     *        the upperCorner
     * @throws IllegalArgumentException
     *         if the dimensions of lowerLeft and upperRight are not the same.
     */
    public BoundingBox(double[] lowerLeft, double[] upperRight) {
        if (lowerLeft.length != upperRight.length) {
            throw new IllegalArgumentException(ERROR_NUM_OF_COORINDATE_DIFFER + 
                    ": uR: " + (upperRight!=null?upperRight.length:"NULL") +
                    "; lL: " + (lowerLeft!=null?lowerLeft.length:"NULL"));
        }
        setDimensions(upperRight.length);

        setLowerCorner(lowerLeft);
        setUpperCorner(upperRight);
    }

    /**
     * this constructor has all attributes as its parameters.
     * 
     * @param crs
     *        a EPSG String for instance
     * @param lowerLeft
     *        the lowerCorner
     * @param upperRight
     *        the upperCorner
     * @throws IllegalArgumentException
     *         if the dimensions of lowerLeft and upperRight are not the same.
     */
    public BoundingBox(String crs, double[] lowerLeft, double[] upperRight) {
        if (lowerLeft.length != upperRight.length) {
            throw new IllegalArgumentException(ERROR_NUM_OF_COORINDATE_DIFFER + 
                    ": uR: " + (upperRight!=null?upperRight.length:"NULL") +
                    "; lL: " + (lowerLeft!=null?lowerLeft.length:"NULL"));
        }
        setDimensions(upperRight.length);

        setLowerCorner(lowerLeft);
        setUpperCorner(upperRight);

        setCRS(crs);
    }

    /**
     * @return Returns the cRS.
     */
    public String getCRS() {
        return crs;
    }

    /**
     * @param crs
     *        The cRS to set.
     */
    protected void setCRS(String crs) {
        this.crs = crs;
    }

    /**
     * 
     */
    public double[] getMaxValue() {
        return getUpperCorner();
    }

    /**
     * 
     */
    public double[] getMinValue() {
        return getLowerCorner();
    }

    /**
     * @return Returns the lowerCorner.
     */
    public double[] getLowerCorner() {
        return lowerCorner;

    }

    /**
     * @param lowerCorner
     *        The lowerCorner to set.
     * @exception OXFException
     *            if the length of the coordinate tupel is not equal to the dimension count. This only
     *            happens, if the dimension number is set!
     */
    protected void setLowerCorner(double[] lowerCornerArray) {
        if (lowerCornerArray.length == dimensions) {
            this.lowerCorner = lowerCornerArray;
        }
        else {
            throw new IllegalArgumentException(ERROR_INPUT_COORDINATES);
        }
    }

    /**
     * @return Returns the upperCorner.
     */
    public double[] getUpperCorner() {
        return upperCorner;
    }

    /**
     * @param upperCorner
     *        The upperCorner to set.
     * @exception OXFException
     *            if the length of the coordinate tupel is not equal to the dimension count. This only
     *            happens, if the dimension number is set!
     */
    protected void setUpperCorner(double[] upperCornerArray) {
        if (upperCornerArray.length == dimensions) {
            this.upperCorner = upperCornerArray;
        }
        else {
            throw new IllegalArgumentException(ERROR_INPUT_COORDINATES);
        }
    }

    /**
     * @return Returns the dimensions.
     */
    public int getDimensions() {
        return dimensions;
    }

    /**
     * @param dimensions
     *        The dimensions to set.
     */
    protected void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    // ----- ValueDomain-methods:

    /**
     * checks if the parameter bBox is contained in (or equal to) this BoundingBox.
     */
    public boolean containsValue(IBoundingBox bBox) {
        for (int i = 0; i < getDimensions(); i++) {
            if (this.getLowerCorner()[i] > bBox.getLowerCorner()[i]) {
                return false;
            }
            else if (this.getUpperCorner()[i] < bBox.getUpperCorner()[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return a XML representation of this Dataset-section.
     */
    public String toXML() {
        String res = "<BoundingBox>";

        res += toKVPString();

        res += "</BoundingBox>";
        return res;
    }

    /**
     * 
     * @return a KeyValuePair-representation of this BBOX in the standard OGC form: <br>
     *         if it's a 2D BBOX: "minx,miny,maxx,maxy"<br>
     *         if it's a 3D BBOX: "minx,miny,maxx,maxy,minz,maxz"<br>
     */
    public String toKVPString() {
        String res = "";

        res += lowerCorner[0] + "," + lowerCorner[1] + "," + upperCorner[0] + "," + upperCorner[1];

        if (lowerCorner.length >= 3) {
            res += "," + lowerCorner[2] + "," + upperCorner[2];
        }
        return res;
    }

    @Override
    public String toString() {
        return toKVPString() + "," + this.crs;
    }

    public String getDomainDescription() {
        return "BoundingBox value domain...";
    }

    public BoundingBox produceValue(String... stringArray) {
        return null;
    }

    /**
     * 
     * @param bbox
     * @return true if the crs, dimensions and upper- and lowerCorner values of both BoundingBoxes are
     *         completely equal. false otherwise.
     */
    public boolean equals(BoundingBox bbox) {
        if (this.getCRS() != bbox.getCRS()) {
            return false;
        }

        if (this.getDimensions() != bbox.getDimensions()) {
            return false;
        }

        for (int i = 0; i < getLowerCorner().length; i++) {
            if (getLowerCorner()[i] != bbox.getLowerCorner()[i]) {
                return false;
            }
        }

        for (int i = 0; i < getUpperCorner().length; i++) {
            if (getUpperCorner()[i] != bbox.getUpperCorner()[i]) {
                return false;
            }
        }

        return true;
    }
    
    /**
     * difference in x direction, i.e. {lower,upper}Corner[0]
     * 
     * @return
     */
    public double getWidth() {
        return upperCorner[0] - lowerCorner[0];
    }
    
    /**
     * difference in y direction, i.e. {lower,upper}Corner[1]
     * 
     * @return
     */
    public double getHeight() {
        return upperCorner[1] - lowerCorner[1];
    }
    
    /**
     * difference in z direction, i.e. {lower,upper}Corner[2]
     * 
     * @return
     */
    public double getDepth() {
        return upperCorner[2] - lowerCorner[2];
    }
}