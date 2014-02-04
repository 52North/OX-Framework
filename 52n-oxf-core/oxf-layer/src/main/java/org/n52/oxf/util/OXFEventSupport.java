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
package org.n52.oxf.util;

import java.util.*;
import org.n52.oxf.*;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class OXFEventSupport {

    private List<IEventListener> eventListenerList;
    
    private Object owner;
    
    /**
     * 
     */
    public OXFEventSupport(Object owner) {
        eventListenerList = new ArrayList<IEventListener>();
        this.owner = owner;
    }

    /**
     * 
     * @param listener
     */
    public void addOXFEventListener(IEventListener listener) {
        if(listener == null){
            return;
        } else {
            eventListenerList.add(listener);
        }
    }

    /**
     * 
     * @param listener
     */
    public void removeOXFEventListener(IEventListener listener){
        if (eventListenerList.contains(listener)) {
            eventListenerList.remove(listener);
        }
    }
    
    /**
     * 
     * @param eventName
     * @param eventValue
     * @throws OXFException
     */
    public void fireEvent(EventName eventName, Object eventValue) throws OXFEventException {
        for(IEventListener oel : eventListenerList){
            oel.eventCaught(new OXFEvent(owner, eventName, eventValue));
        }
    }
    
    /**
     * 
     * @param e
     * @throws OXFException
     */
    public void fireEvent(OXFEvent e) throws OXFEventException {
        for(IEventListener oel : eventListenerList){
            oel.eventCaught(e);
        }
    }
    
    public String toString(){
        String res = "";
        
        int i=0;
        for(IEventListener listener : eventListenerList){
            res += "listener " + i + ": " + listener.getClass();
        }
        
        return res;
    }
    
    public String printListenerList(){
        String res = "";
        for(IEventListener listener : eventListenerList){
            res += "" + listener.getClass() + "\n";
        }
        return res;
    }
}