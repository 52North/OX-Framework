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
 
 Created on: 09.06.2006
 *********************************************************************************/

package org.n52.oxf.ui.swing;

import java.awt.Image;

import org.apache.log4j.Logger;
import org.n52.oxf.render.AnimatedVisualization;
import org.n52.oxf.render.OverlayEngine;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.IEventEmitter;
import org.n52.oxf.util.IEventListener;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.util.OXFEvent;
import org.n52.oxf.util.OXFEventException;
import org.n52.oxf.util.OXFEventSupport;

/**
 * This class extends the <code>MapCanvas</code> to be animatable.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class AnimatedMapCanvas extends MapCanvas implements IEventEmitter, Runnable {

    /**
     * 
     */
    private static final long serialVersionUID = -6529826577182008488L;

    private static Logger LOGGER = LoggingHandler.getLogger(AnimatedMapCanvas.class);

    private AnimatedVisualization animatedVis = null;
    private int currentFrame = 0;
    private boolean animationRunning = false;
    private int framesPerMinute = 30;

    private Thread animator;

    private OXFEventSupport eventSupport;

    /**
     * @param imageBuilder
     */
    public AnimatedMapCanvas(OverlayEngine imageBuilder) {
        super(imageBuilder);

        eventSupport = new OXFEventSupport(this);

    }

    /**
     * 
     */
    public void startAnimation() {
        animationRunning = true;

        animator = new Thread(this);
        animator.start();
    }

    /**
     * 
     */
    public void pauseAnimation() {
        animationRunning = false;
    }

    /**
     * 
     */
    public void stopAnimation() {
        animationRunning = false;

        currentFrame = 0;

        try {
            eventSupport.fireEvent(EventName.ANIMATION_STEPPED, new Integer(currentFrame));
        }
        catch (OXFEventException e) {
            e.printStackTrace();
        }
    }

    /**
     * pauses the animation and shows a freeze image of the next animation frame.
     */
    public void stepToNextFrame() {
        pauseAnimation();
        currentFrame = computeNextFrame(currentFrame, animatedVis.numberOfFrames());
        showFreezeImage(currentFrame);

        try {
            eventSupport.fireEvent(EventName.ANIMATION_STEPPED, new Integer(currentFrame));
        }
        catch (OXFEventException e) {
            e.printStackTrace();
        }
    }

    /**
     * pauses the animation and shows a freeze image of the previous animation frame.
     */
    public void stepToPreviousFrame() {
        pauseAnimation();
        currentFrame = computePreviousFrame(currentFrame, animatedVis.numberOfFrames());
        showFreezeImage(currentFrame);

        try {
            eventSupport.fireEvent(EventName.ANIMATION_STEPPED, new Integer(currentFrame));
        }
        catch (OXFEventException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param currentFrame
     * @param numberOfFrames
     * @return
     */
    private static int computeNextFrame(int currentFrame, int numberOfFrames) {
        return ( (currentFrame + 1) % numberOfFrames);
    }

    /**
     * 
     * @param currentFrame
     * @param numberOfFrames
     * @return
     */
    private static int computePreviousFrame(int currentFrame, int numberOfFrames) {
        return (currentFrame - 1 >= 0) ? currentFrame - 1 : numberOfFrames - 1;
    }

    /**
     * 
     * @param frame
     */
    private void showFreezeImage(int frame) {
        Image freezeImage = animatedVis.getFrame(frame);
        overlayImage = freezeImage;
        repaint();
    }

    /**
     * @throws OXFEventException
     * 
     */
    public void run() {

        while (animationRunning) {
            overlayImage = animatedVis.getFrame(currentFrame);

            repaint();

            try {
                Thread.sleep(60000 / framesPerMinute);

                currentFrame = computeNextFrame(currentFrame, animatedVis.numberOfFrames());

                eventSupport.fireEvent(EventName.ANIMATION_STEPPED, new Integer(currentFrame));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (OXFEventException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void eventCaught(OXFEvent evt) throws OXFEventException {
        super.eventCaught(evt);

        LOGGER.info("event caught: " + evt);

        if (evt.getName().equals(EventName.ANIMATED_OVERLAY_READY)) {
            stopAnimation();
            animatedVis = (AnimatedVisualization) evt.getValue();
            showFreezeImage(0);
        }
    }

    /**
     * 
     * @param i
     */
    public void setFramesPerMinute(int i) {
        framesPerMinute = i;
    }

    public int getFramesPerMinute() {
        return framesPerMinute;
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