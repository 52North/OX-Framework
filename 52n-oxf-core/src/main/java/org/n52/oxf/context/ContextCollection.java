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

import java.util.*;
import org.n52.oxf.serialization.*;

/**
 * Context Collections represent a list of context documents. Context collections could be used in several
 * ways:
 * <li> A particular Viewer Client could use a Collection to construct a menu of default start-up views. </li>
 * <li> A Collection of related contexts could serve as a script for a demonstration. </li>
 * <li> A user could create a Collection to "bookmark" public or user-specific contexts. The creation of such
 * a Collection might be managed by the Viewer Client itself. </li>
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class ContextCollection implements IContextSerializableXML {

    /**
     * The published specification version number contains three positive integers, separated by decimal
     * point, in the form �x.y.z�. Each context collection specification is numbered independently. This
     * attribute is required.
     */
    protected String version;

    
    protected ArrayList<Context> contexts;

    /**
     * 
     * 
     */
    public ContextCollection() {
        contexts = new ArrayList<Context>();
        version = "1.1.0";
    }

    /**
     * @param context
     */
    public void addContext(Context context) {
        contexts.add(context);
    }

    /**
     * @return
     */
    public Iterator<Context> getContextIterator() {
        return contexts.iterator();
    }

    /**
     * @param id
     * @return the context with the specified id.
     */
    public Context getContextByID(String id) {
        for (Context c : contexts) {
            if (c.getID().equals(id)) {
                return c;
            }
        }
        return null;
    }

    /**
     * @return
     */
    public List<LayerContext> getLayerContexts() {
        List<LayerContext> res = new ArrayList<LayerContext>();

        for (Context c : contexts) {
            if (c instanceof LayerContext) {
                res.add((LayerContext) c);
            }
        }
        return res;
    }

    public void serializeToContext(StringBuffer sb) {
        // TODO has to be implemented
    }
}