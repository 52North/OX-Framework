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
package org.n52.oxf.render;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.renderable.ParameterBlock;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;
import javax.media.jai.widget.ImageCanvas;
import javax.swing.JFrame;

import org.n52.oxf.OXFException;
import org.n52.oxf.layer.IContextLayer;
import org.n52.oxf.render.jai.MosaikDescriptor;
import org.n52.oxf.render.jai.TransparencyDescriptor;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.IEventEmitter;
import org.n52.oxf.util.IEventListener;
import org.n52.oxf.util.OXFEvent;
import org.n52.oxf.util.OXFEventException;
import org.n52.oxf.util.OXFEventSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.PNGEncodeParam;

/**
 * The OverlayEngine overlays images with the <code>overlayImages(ArrayList<PlanarImage> images)</code>-method.<br>
 * A GUI wich uses the OX-framework can listen to this OverlayEngine and can react on the
 * <code>EventName.STATIC_OVERLAY_READY</code> PropertyChangeEvent which will be fired when the overlay of the
 * images is ready.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class OverlayEngine implements IEventEmitter, IEventListener {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OverlayEngine.class);

    static {
        MosaikDescriptor.register();
        TransparencyDescriptor.register();
    }

    /**
     * classes which want to listen to this class must be added to this OXFEventSupport.
     */
    protected OXFEventSupport eventSupport;

    public OverlayEngine() {
        eventSupport = new OXFEventSupport(this);
    }

    /**
     * 
     * @param visualizationList
     *        restriction: all contained <code>AnimatedVisualization</code>s has to consist of the same
     *        number of frames
     * @return either an <code>AnimatedVisualization</code> or a <code>StaticVisualization</code>
     *         depending on whether there is an <code>AnimatedVisualization</code> contained in the
     *         visualizationList or not.
     */
    public IVisualization overlayVisualizations(List<IVisualization> visualizationList) {

        //
        // proof whether all animatedImages contain the same number of images:
        //
        int numberOfFrames = 1;
        for (IVisualization visualization : visualizationList) {
            if (visualization instanceof AnimatedVisualization) {
                AnimatedVisualization animatedVis = (AnimatedVisualization) visualization;
                if (numberOfFrames == 1) {
                    numberOfFrames = animatedVis.numberOfFrames();
                }
                if (animatedVis.numberOfFrames() != numberOfFrames) {
                    throw new IllegalArgumentException("All animatedImages has to contain the same number of frames.");
                }
            }
        }

        //
        // overlay each frame and add the resulting overlay to a List:
        //
        
        // List containing the resulting overlay images for each frame
        List<Image> listOfOverlaidFrames = new Vector<Image>();
        for (int i = 0; i < numberOfFrames; i++) {

            // List of all images which has to be overlaid for the i. frame:
            List<Image> imagesToOverlay = new Vector<Image>();

            for (IVisualization visualization : visualizationList) {
                if (visualization instanceof StaticVisualization) {
                    imagesToOverlay.add((Image) visualization.getRendering());
                }
                else if (visualization instanceof AnimatedVisualization) {
                    Image frame = ((AnimatedVisualization) visualization).getFrame(i);
                    imagesToOverlay.add(frame);
                }
                else {
                    LOGGER.error("unsupported visualization-type.");
                }
            }
            listOfOverlaidFrames.add(overlayImages(imagesToOverlay).getAsBufferedImage());
        }

        if (listOfOverlaidFrames.size() > 1) {
            return new AnimatedVisualization(listOfOverlaidFrames);
        }
        else {
            return new StaticVisualization(listOfOverlaidFrames.get(0));
        }

    }

    /**
     * overlays the images contained in the imageList.<br>
     * An assumption is that all images have the same width and height.<br>
     * The color White will be interpreted as transparent.
     * 
     * @param imageList
     *        the sorted list of images. (image n will be above image n+1)
     * @return
     */
    private PlanarImage overlayImages(List<Image> imageList) {
        ParameterBlock mosaikPB = new ParameterBlock();
        mosaikPB.add(Color.WHITE); // desired background color of the destination image
        mosaikPB.add(null); // null for the union of all source images
        mosaikPB.add(false); // compute new tile layout?

        for (Image image : imageList) {
            ParameterBlock transPB = new ParameterBlock();
            transPB.add(Color.WHITE);
            transPB.addSource(image);

            RenderedOp transparentImage = JAI.create("52nTransparency", transPB);
            mosaikPB.addSource(transparentImage);
        }

        PlanarImage resultImage = JAI.create("52nMosaik", mosaikPB);

        return resultImage;
    }

    /**
     * @throws OXFException
     * 
     */
    public void eventCaught(OXFEvent event) throws OXFEventException {

        // reaction on events of the LayerContext...
        if (event.getName().equals(EventName.ALL_LAYERS_READY_TO_OVERLAY)) {
            ArrayList<IContextLayer> layerList = (ArrayList<IContextLayer>) event.getValue();

            IVisualization resultVisualization = null;
            
            if (layerList.size() == 0) {
                LOGGER.info("no layers found to overlay.");
                // overlayImage will stay null.
            }
            else {
                if (layerList.size() == 1) {
                    resultVisualization = layerList.get(0).getLayerVisualization();
                    
                }
                else {
                    List<IVisualization> visualizationList = new ArrayList<IVisualization>();
                    for (IContextLayer layer : layerList) {
                        visualizationList.add(layer.getLayerVisualization());
                    }

                    resultVisualization = overlayVisualizations(visualizationList);
                }
            }
            

            if (resultVisualization instanceof AnimatedVisualization) {
                eventSupport.fireEvent(EventName.ANIMATED_OVERLAY_READY, resultVisualization);
            }
            else {
                eventSupport.fireEvent(EventName.STATIC_OVERLAY_READY, resultVisualization);
            }
        }
    }

    // ----- OXFEventEmitting:

    /**
     * Add an IEventListener to the OXFEventSupport.
     */
    public void addEventListener(IEventListener listener) {
        eventSupport.addOXFEventListener(listener);
    }

    /**
     * Remove an IEventListener from the OXFEventSupport.
     */
    public void removeEventListener(IEventListener listener) {
        eventSupport.removeOXFEventListener(listener);

    }

}