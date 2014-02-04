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
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Map;
import java.util.Vector;

import javax.media.jai.ImageLayout;
import javax.media.jai.PointOpImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:jaeger@52north.org">Andreas Jaeger</a>
 */
class CMConvertOpImage extends PointOpImage {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CMConvertOpImage.class);

	private Color transColor;
	
    public CMConvertOpImage(ColorModel targetCM, Vector sources, RenderingHints rh) {
        super(
            (RenderedImage) sources.get(0),
            createImageLayout((RenderedImage) sources.get(0), targetCM),
            createConfiguration((RenderedImage) sources.get(0)),
            true
        );
    }

    private static ImageLayout createImageLayout(RenderedImage source, ColorModel targetCM) {
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

    /*########################################################################*/
    /*##########                  Computing pixels                 ###########*/
    /*########################################################################*/

    public void computeRect(Raster[] sources, WritableRaster dest, Rectangle destRect) {
        LOGGER.debug("Start computing region: " + destRect);
        ColorModel srcCM = getSourceImage(0).getColorModel();
        ColorModel destCM = getColorModel();
        Object srcData = null;
        Object destData = null;
        		
        for (int i = destRect.x + destRect.width; --i >= destRect.x;) {
            for (int j = destRect.y + destRect.height; --j >= destRect.y;) {
                srcData = sources[0].getDataElements(i, j, srcData);
                destData = destCM.getDataElements(srcCM.getRGB(srcData), destData);
      			dest.setDataElements(i, j, destData);
            }
        }
        LOGGER.debug("Region computed: " + destRect);
    }

}