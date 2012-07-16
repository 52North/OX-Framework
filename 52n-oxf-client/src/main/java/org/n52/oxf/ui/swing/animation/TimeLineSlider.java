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


package org.n52.oxf.ui.swing.animation;

import javax.swing.JSlider;

import org.n52.oxf.render.AnimatedVisualization;
import org.n52.oxf.ui.swing.AnimatedMapCanvas;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.IEventListener;
import org.n52.oxf.util.OXFEvent;
import org.n52.oxf.util.OXFEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class TimeLineSlider extends JSlider implements IEventListener {

    private static final long serialVersionUID = 2715721823808398280L;

    private int numberOfFrames = 1;

    private int currentFrame = -1;

    public TimeLineSlider(AnimatedMapCanvas map) {
        super();
        
        map.addEventListener(this);
        map.getImageBuilder().addEventListener(this);
        
        setMinimum(0);
        setMaximum(numberOfFrames);
        
        setPaintTicks(true);
        //setPaintLabels(true);
        setMajorTickSpacing(10);
    }


    public void eventCaught(OXFEvent evt) throws OXFEventException {

        if (evt.getName().equals(EventName.ANIMATED_OVERLAY_READY)) {
            numberOfFrames = ((AnimatedVisualization)evt.getValue()).numberOfFrames();
            
            setMaximum(numberOfFrames);
            
        }
        
        else if (evt.getName().equals(EventName.ANIMATION_STEPPED)) {
            currentFrame = (Integer) evt.getValue();

            setValue(currentFrame);
            
        }
    }
    

}