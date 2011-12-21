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
 
 Created on: 30.08.2005
 *********************************************************************************/

package org.n52.oxf.util;

import java.io.*;
import org.apache.log4j.*;
import org.apache.log4j.varia.*;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class LoggingHandler {
    
    private static LoggingOutputStream out = new LoggingOutputStream();

    private static WriterAppender writerAppender = new WriterAppender(new PatternLayout("%5p [%c] %m%n"), out);
    
    /**
     * 
     * @param clazz
     * @return
     */
    public static Logger getLogger(Class clazz) {
        Logger logger = Logger.getLogger(clazz);
        
        logger.addAppender(writerAppender);

        return logger;
    }

    /**
     * 
     * @return the LoggingOutputStream to which the messages will be logged.<br>
     *         This LoggingOutputStream can be used for example to print the logging-statements into a separate
     *         message-concole of a GUI.
     */
    public static LoggingOutputStream getOutputStream() {
        return out;
    }
    
    /**
     * 
     * @param level - valid values: OFF, FATAL, ERROR, WARN, INFO, DEBUG and ALL
     */
    public static void setLevel(Level level){
        LevelRangeFilter filter = new LevelRangeFilter();
        filter.setLevelMin(level);
        
        writerAppender.clearFilters();
        writerAppender.addFilter(filter);
    }
    
    public static void divertSystemErr(boolean divert){
        if(divert){
            System.setErr( new PrintStream(out) );
        }
        else {
            System.setErr( System.err );
        }
    }
    
    public static void divertSystemOut(boolean divert){
        if(divert){
            System.setOut( new PrintStream(out) );
        }
        else {
            System.setOut( System.err );
        }
    }
}