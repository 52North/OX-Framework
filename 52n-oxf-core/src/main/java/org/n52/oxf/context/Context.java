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
 
 Created on: 08.06.2005
 *********************************************************************************/

package org.n52.oxf.context;

import java.util.List;

import org.n52.oxf.serialization.IContextSerializableXML;
import org.n52.oxf.util.IEventEmitter;
import org.n52.oxf.util.IEventListener;
import org.n52.oxf.util.OXFEventSupport;

/**
 * 
 * @see <a href=LayerContext.html>LayerContext</a>
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public abstract class Context implements IEventEmitter, IEventListener, IContextSerializableXML {

    /**
     * classes which want to listen to this class must be added to this PropertyChangeSupport.
     */
    protected OXFEventSupport eventSupport;

    // --- Context Elements:

    /**
     * The current version of the context according to the WMC Spec of the OGC.
     */
    public static final String CONTEXT_VERSION = "1.1.0";
    
    /**
     * The id attribute should be unique and represent a unique identifier of the Context. This attribute is
     * required
     */
    protected String id;

    /**
     * The element title contains a human readable title of the Context. This element is required
     */
    protected String title;

    /**
     * The element keywordList contains one or more Keyword elements which allow search across context
     * collections. This element is optional.
     */
    protected List<String> keywordList;

    /**
     * The element abstract contains a description for the Context document describing its content. This
     * element is optional.
     */
    protected String abstractDescription;

    public Context(String id, String title) {
        this.id = id;
        this.title = title;
        eventSupport = new OXFEventSupport(this);
    }
    public Context(String id, String title, String abstractDescription, List<String> keywords) {
        this(id, title);
        this.abstractDescription = abstractDescription;
        this.keywordList = keywords;
    }
    public String getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAbstractDescription() {
        return abstractDescription;
    }

    public List<String> getKeywordList() {
        return keywordList;
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