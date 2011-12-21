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
 
 Created on: 15.8.2005
 *********************************************************************************/

package org.n52.oxf.render.jai;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Vector;

import javax.media.jai.ImageLayout;
import javax.media.jai.OpImage;

import org.apache.log4j.Logger;
import org.n52.oxf.util.LoggingHandler;

/**
 * 
 * @author <a href="mailto:jaeger@52north.org">Andreas Jaeger</a>
 */
class MosaikOpImage extends OpImage {

    private final static Logger LOGGER = LoggingHandler.getLogger(MosaikOpImage.class);

    private ArrayList icmColorMappings;
    private int bgRGB;

    /*########################################################################*/
    /*###########                   Constructors                   ###########*/
    /*########################################################################*/

    public MosaikOpImage(Color destBGColor, Rectangle bounds
                , boolean computeTileLayout, Vector sources, RenderingHints rh) {
        super(sources, null, null, true);
        bgRGB = destBGColor.getRGB();
        setImageLayout(destBGColor, bounds, computeTileLayout, sources);
        LOGGER.debug("Tile mode is " + (computeTileLayout? "ON" : "OFF"));
    }

    private void setImageLayout(Color destBGColor
                , Rectangle bounds, boolean computeTileLayout, Vector sources) {
        icmColorMappings = new ArrayList(2 * sources.size());
        ColorModel cm = null;
        SampleModel sm = null;
        
        // calculate bounds (if not set) as union of all source images
        if (bounds == null) {
            RenderedImage src = (RenderedImage) sources.get(0);
            int minx = src.getMinX();
            int miny = src.getMinY();
            int maxx = minx + src.getWidth() - 1;
            int maxy = miny + src.getHeight() - 1;
            for (int i = sources.size(); --i >= 1;) {
                src = (RenderedImage) sources.get(i);
                minx = Math.min(src.getMinX(), minx);
                miny = Math.min(src.getMinY(), miny);
                maxx = Math.max(src.getMinX() + src.getWidth(), maxx);
                maxy = Math.max(src.getMinY() + src.getHeight(), maxy);
            }
            bounds = new Rectangle(minx, miny, (maxx - minx) + 1, (maxy - miny) + 1);
        }
        // calculate tile layout
        Rectangle tiles;
        if (computeTileLayout && sources.size() != 0) {
            RenderedImage src = (RenderedImage) sources.get(0);
            tiles = new Rectangle(src.getMinX(), src.getMinY(), src.getWidth(), src.getHeight());
        } else {
            tiles = bounds;    
        }
        // determine destination color model, make sure we can represent everything
        // 1) we use a grayscale image if and only if 
        // - there is at least one source image
        // - all source images are single component grayscale images
        // (the maximum bitdepth of the input image is used for the output)
        // 2) we use ICM if and only if
        // - there are no sources (in which case we create a single bit image)
        // - all sources are ICM
        // - all sources have BIT_MASK or OPAQUE transparency type
        // - the sum of the entries needed to represent the source image pixels in the dest color map does not exceed 256
        // (note that completely transparent pixels of the source images are all mapped to 0 in the target color map)
        
        int i = sources.size();
        while (--i >= 0) {
            ColorModel srcCM = ((RenderedImage) sources.get(i)).getColorModel();
            if (srcCM.getColorSpace().getType() != ColorSpace.TYPE_GRAY || srcCM.getNumComponents() != 1) {
                cm = null;
                break;
            }
            if (cm == null || srcCM.getComponentSize(0) > cm.getComponentSize(0)) cm = srcCM;
        }
        if (cm == null) {
            int transIndex = (bgRGB & 0xff000000) == 0 ? 0 : -1; // include additional transparency info (only for ICM dest) for stupid software that ignores transparency info in the palette entries
    
            if (sources.size() == 0) {
                cm = new IndexColorModel(1, 2, new int[]{bgRGB, 0x000000}, 0, true, transIndex, DataBuffer.TYPE_BYTE);
            } else {
                int[] cmap = new int[256];
                cmap[0] = destBGColor.getRGB(); // make sure bg is mapped at pos 0, so we never have to initialize our image with bg as long as we can use an ICM
                int mapSize = 1;
                i = sources.size();
                while (--i >= 0) {
                    ColorModel srcCM = ((RenderedImage) sources.get(i)).getColorModel();
                    if (cmap == null || ! (srcCM instanceof IndexColorModel) || srcCM.getTransparency() == Transparency.TRANSLUCENT) {
                        cmap = null;
                        icmColorMappings = null;
                        break;
                    }
                    int m = ((IndexColorModel) srcCM).getMapSize();
                    int[] icmColorMapping = new int[m];
                    icmColorMappings.add(srcCM);
                    icmColorMappings.add(icmColorMapping);
                    while (--m >= 0) {
                        int pixVal = srcCM.getRGB(m);
                        if (srcCM.getAlpha(m) == 0) continue; // we won't look up (fully!) transparent pixels while computing the image, so the entry value does not matter (it points to zero, which is the bg color, anyway)
                        boolean ok = false;
                        for (int c = mapSize; --c >= 0;) {
                            if (pixVal == cmap[c]) {
                                icmColorMapping[m] = c;
                                ok = true;
                                break;
                            }
                        }
                        if (! ok) {
                            if (mapSize > 255) {
                                cmap = null;
                                icmColorMappings = null;
                                break;
                            } else {
                                icmColorMapping[m] = mapSize;
                                cmap[mapSize++] = pixVal;
                            }
                        }
                    }
                }
    
                if (cmap == null) {
                    cm = ColorModel.getRGBdefault();
                } else {
                    int bits = (mapSize <= 2 ? 1 : (mapSize <= 4 ? 2 : (mapSize <= 16 ? 4 : 8)));
                    cm = new IndexColorModel(bits, mapSize, cmap, 0, true, transIndex, DataBuffer.TYPE_BYTE);
                }
            }
        }
        sm = cm.createCompatibleSampleModel(bounds.width, bounds.height);
        setImageLayout(
            new ImageLayout(
                  bounds.x, bounds.y, bounds.width, bounds.height,
                  tiles.x, tiles.y, tiles.width, tiles.height,
                sm,
                cm
            )
        );
    }

    /*########################################################################*/
    /*##########                  Computing pixels                 ###########*/
    /*########################################################################*/

    public Rectangle mapSourceRect(Rectangle srcRect, int sourceIndex) {
        Rectangle ret = getBounds().intersection(srcRect);
        return ret.isEmpty() ? null : ret;
    }

    public Rectangle mapDestRect(Rectangle destRect, int sourceIndex) {
        RenderedImage src = (RenderedImage) getSourceImage(sourceIndex);
        Rectangle srcRect = new Rectangle(src.getMinX(), src.getMinY(), src.getWidth(), src.getHeight());
        Rectangle ret = destRect.intersection(srcRect);
        boolean isEmpty = ret.isEmpty();
        LOGGER.debug("SourceRectangle " + ret + "for Tile " + sourceIndex);
        return ret.isEmpty() ? new Rectangle(src.getMinX(), src.getMinY(), 1, 1) : ret; // specifying null intersection is interpreted as: whole source must be calculated, whereas we need: source can be ignored ==> so we try and make the source as small as possible
    }

    protected void computeRect(Raster[] sources, WritableRaster dest, Rectangle destRect) {
        ColorModel destCM = getColorModel();
        Object srcData = null;
        Object destData = null;
        Object composeData = null;

        // paint background transparent if necessary: this is never the case with ICM because background is mapped at pos 0
        if (destCM instanceof DirectColorModel // we could not use an ICM: so paint each line with bg, use moderately sized buffer
                    && bgRGB != 0) {
            Object bg = destCM.getDataElements(bgRGB, null);
            for (int i = destRect.width; --i >= 0;)
                for (int j = destRect.height; --j >= 0;)
                    dest.setDataElements(i + destRect.x, j + destRect.y, bg);
        }

        // paint sources
        for (int s = sources.length; --s >= 0;) {
            if (sources[s]==null) {
                LOGGER.debug("Source " + s + " is null while computing destRect");
              	continue;
            }
            // calc area of interest: in actual fact, this seems to be superfluous, the parent appears to calculate the correct views onto the sources using mapDestRect and mapSourceRect
            Raster src = sources[s];
            Rectangle r = intersect(destRect, src.getBounds());
            if (r == null) continue;

            // three cases: ICM --> ICM, ICM --> sRGB, anything --> sRGB
            ColorModel srcCM = getSourceImage(s).getColorModel();
            if (srcCM instanceof IndexColorModel) {
                if (destCM instanceof IndexColorModel) {
                    int[] icmColorMapping = null;
                    for (int i = 0; i < icmColorMappings.size(); i += 2) {
                        if (icmColorMappings.get(i) == srcCM) {
                            icmColorMapping = (int[]) icmColorMappings.get(i + 1);
                            break;
                        }
                    }
                    for (int i = r.x + r.width; --i >= r.x;) {
                        for (int j = r.y + r.height; --j >= r.y;) {
                            int entry = src.getSample(i, j, 0);
                            if (srcCM.getAlpha(entry) != 0)
                                dest.setSample(i, j, 0, icmColorMapping[entry]);
                        }
                    }
                } else {
                    for (int i = r.x + r.width; --i >= r.x;) {
                        for (int j = r.y + r.height; --j >= r.y;) {
                            int entry = src.getSample(i, j, 0);
                            int alpha = srcCM.getAlpha(entry);
                            if (alpha != 0) {
                                composeData = destCM.getDataElements(srcCM.getRGB(entry), composeData);
                                if (alpha == 255) dest.setDataElements(i, j, composeData);
                                else {
                                    destData = dest.getDataElements(i, j, destData);
                                    dest.setDataElements(i, j, compose(composeData, destData));
                                }
                            }
                        }
                    }
                }
            } else {
                for (int i = r.x + r.width; --i >= r.x;) {
                    for (int j = r.y + r.height; --j >= r.y;) {
                        srcData = src.getDataElements(i, j, srcData);
                        int alpha = srcCM.getAlpha(srcData);
                        if (alpha != 0) {
                            composeData = destCM.getDataElements(srcCM.getRGB(srcData), composeData);
                            if (alpha == 255) dest.setDataElements(i, j, composeData);
                            else {
                                destData = dest.getDataElements(i, j, destData);
                                dest.setDataElements(i, j, compose(composeData, destData));
                            }
                        }
                    }
                }
            }
        }
    }

    /*########################################################################*/
    /*##########                    Helpers                        ###########*/
    /*########################################################################*/

    private Rectangle intersect(Rectangle r1, Rectangle r2) {
        int xl = Math.max(r1.x, r2.x);
        int xr = Math.min(r1.x + r1.width, r2.x + r2.width);
        int yl = Math.max(r1.y, r2.y);
        int yr = Math.min(r1.y + r1.height, r2.y + r2.height);
        if (((xr - xl) <= 0) || ((yr - yl) <= 0)) return null;
        return new Rectangle(xl, yl, xr - xl, yr - yl);
    }

    /**
     * Porter-Duff composite src over destination.
     */
    private Object compose(Object composeData, Object destData) {
        int[] ret = (int[]) destData;
        int s = ((int[]) composeData)[0];
        int d = ret[0];
        int as = (s & 0xff000000) >>> 24;
        int ad = (d & 0xff000000) >>> 24;
        int iret = 0;
        for (int i = 24; i >= 0; i -= 8) {
            int cs = (s & (0xff << i)) >>> i;
            int cd = (d & (0xff << i)) >>> i;
            iret |= Math.min(255, (cs + (cd * (255 - as)) / 255)) << i;
        }
        ret[0] = iret;
        return ret;
    }

}