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

package org.n52.oxf.ui.swing;

import java.io.*;
import java.util.*;
import javax.swing.filechooser.FileFilter;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class MyFileFilter extends FileFilter {
    
    private Hashtable filters = null;
    private String description = null;
    
    public MyFileFilter(String extension, String description) {
        this.filters = new Hashtable();
        
        if (extension != null){
            addExtension(extension);
        }
        this.description = description;
    }

    
    public boolean accept(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                return true;
            }
            String extension = getExtension(file);
            if (extension != null && filters.get(getExtension(file)) != null) {
                return true;
            }
        }
        return false;
    }

    
    public String getExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
        }
        return null;
    }

    
    public void addExtension(String extension) {
        if (filters == null) {
            filters = new Hashtable(5);
        }
        filters.put(extension.toLowerCase(), this);
        description = null;
    }

    
    public String getDescription() {
        return description;
    }

}