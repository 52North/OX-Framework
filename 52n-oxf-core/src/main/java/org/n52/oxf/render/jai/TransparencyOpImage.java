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
package org.n52.oxf.render.jai;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Map;
import java.util.Vector;

import javax.media.jai.ImageLayout;
import javax.media.jai.PointOpImage;

/**
 * 
 * @author <a href="mailto:jaeger@52north.org">Andreas Jaeger</a>
 */
class TransparencyOpImage extends PointOpImage {

	private Color transColor;
	
    public TransparencyOpImage(Color transColor, Vector sources, RenderingHints rh) {
        super(
            (RenderedImage) sources.get(0),
            createImageLayout((RenderedImage) sources.get(0), transColor),
            createConfiguration((RenderedImage) sources.get(0)),
            true
        );
		this.transColor = new Color(transColor.getRGB() & 0x00ffffff, true);
    }

    private static ImageLayout createImageLayout(RenderedImage source, Color transColor) {
        ColorModel targetCM;
        if (source.getColorModel() instanceof IndexColorModel) {
            IndexColorModel icm = (IndexColorModel) source.getColorModel();
            int mapSize = icm.getMapSize();
            int[] cmap = new int[mapSize];
            icm.getRGBs(cmap);
            // replace all background entries with transparent background entries
            int cmpColor = transColor.getRGB() & 0x00ffffff; // make sure cmpColor is fully transparent
            for(int i = mapSize; --i >= 0;) {
                if ((cmap[i] & 0x00ffffff) == cmpColor) cmap[i] = cmpColor;
            }
            targetCM = new IndexColorModel(
                icm.getPixelSize(), icm.getMapSize(), cmap, 0, true, -1, icm.getTransferType());            
        } else {
            targetCM = ColorModel.getRGBdefault();
        }
        
        return new ImageLayout(
            source.getMinX(),
            source.getMinY(),
            source.getWidth(),
            source.getHeight(),
            source.getTileGridXOffset(),
            source.getTileGridYOffset(),
            source.getTileWidth(),
            source.getTileHeight(),
            targetCM.createCompatibleSampleModel(source.getWidth(), source.getHeight()),
            targetCM
        );
    }

    private static Map createConfiguration(RenderedImage source) {
        return null;
    }

    protected boolean isColormapOperation() {
        return getColorModel() instanceof IndexColorModel;
    }

    /*########################################################################*/
    /*##########                  Computing pixels                 ###########*/
    /*########################################################################*/

    public void computeRect(Raster[] sources, WritableRaster dest, Rectangle destRect) {
        ColorModel srcCM = getSourceImage(0).getColorModel();
        ColorModel destCM = getColorModel();
        Object srcData = null;
        Object destData = null;
        		
        for (int i = destRect.x + destRect.width; --i >= destRect.x;) {
            for (int j = destRect.y + destRect.height; --j >= destRect.y;) {
                srcData = sources[0].getDataElements(i, j, srcData);
                int rgb = srcCM.getRGB(srcData);
        		if (srcCM.getRed(srcData) == transColor.getRed() &&
        			srcCM.getGreen(srcData) == transColor.getGreen() &&
        			srcCM.getBlue(srcData) == transColor.getBlue()) {
					destData = destCM.getDataElements(transColor.getRGB(), destData);
        		} else {
        			destData = destCM.getDataElements(rgb, destData);
        		}
      			dest.setDataElements(i, j, destData);
            }
        }
    }

    public Raster getTile(int x, int y) {
        if (isColormapOperation()) 
            return getSourceImage(0).getTile(x, y);
        return super.getTile(x, y);
    }

}