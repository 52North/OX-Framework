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

package org.n52.oxf.util;

import java.io.*;
import org.n52.oxf.*;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class LoggingOutputStream extends OutputStream implements IEventEmitter {

    /**
     * classes which want to listen to this class must be added to this OXFEventSupport.
     */
    protected static OXFEventSupport eventSupport = new OXFEventSupport(LoggingOutputStream.class);

    StringBuffer buffer = new StringBuffer();

    public LoggingOutputStream() {
    }

    /**
     * @return the buffered byte[]; but only the valid bytes in the array.
     */
    public StringBuffer getBuffer() {
        return buffer;
    }

    public void write(int b) throws java.io.IOException {
        buffer.append((char) b);
        if (b == '\n') {
            try {
                 eventSupport.fireEvent(EventName.LOG_MESSAGE, buffer);
            }
            catch (OXFEventException e) {
                throw new OXFRuntimeException(e);
            }
            buffer.delete(0, buffer.length());
        }
    }


    public void addEventListener(IEventListener listener) {
        eventSupport.addOXFEventListener(listener);
    }

    public void removeEventListener(IEventListener listener) {
        eventSupport.removeOXFEventListener(listener);
    }
}