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