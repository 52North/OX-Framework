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
 
 Created on: 25.07.2005
 *********************************************************************************/

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