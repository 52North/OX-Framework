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
 
 Created on: 12.06.2006
 *********************************************************************************/

package org.n52.oxf.ui.swing.animation;

import org.apache.log4j.*;
import org.n52.oxf.util.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class AnimationPanelController implements IEventListener {

    private static Logger LOGGER = LoggingHandler.getLogger(AnimationPanelController.class);
    
    private AnimationPanel view;

    /**
     * 
     */
    public AnimationPanelController(AnimationPanel view) {
        super();

        this.view = view;
    }

    public void actionPerformed_playButton() {
        view.getMap().startAnimation();
    }

    public void actionPerformed_pauseButton() {
        view.getMap().pauseAnimation();
    }

    public void actionPerformed_stopButton() {
        view.getMap().stopAnimation();
    }
    
    public void actionPerformed_stepFwdButton() {
        view.getMap().stepToNextFrame();
    }

    public void actionPerformed_stepBwdButton() {
        view.getMap().stepToPreviousFrame();
    }
    
    public void actionPerformed_fpmComboBox() {
        view.getMap().setFramesPerMinute((Integer)view.getFramesPerMinuteComboBox().getSelectedItem());
    }

    public void eventCaught(OXFEvent evt) throws OXFEventException {
        LOGGER.info("event caught: " + evt);
        
        if (evt.getName().equals(EventName.ANIMATED_OVERLAY_READY)) {
            view.enableButtons(true);
        }
        else if (evt.getName().equals(EventName.STATIC_OVERLAY_READY)) {
            view.enableButtons(false);
        }
    }
}