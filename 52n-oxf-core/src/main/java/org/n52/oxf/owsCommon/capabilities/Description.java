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

package org.n52.oxf.owsCommon.capabilities;

import org.n52.oxf.*;

/**
 * According to the OWS Common this holds some general parameters, which are needed by ServiceIdentification
 * and Dataset.
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster </a>
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public abstract class Description {

    /**
     * Title of the service / the data<br>
     * ('data' means e.g. a layer-description). <br>
     * <br>
     * One (mandatory) value for title is required. <br>
     */
    private String title;

    /**
     * Refers to the 'abstract' element in the ows common spec. <br>
     * <br>
     * Zero or one (optional) value for abstractDescription is possible. <br>
     */
    private String abstractDescription;

    /**
     * Unordered list of one or more commonly used or formalised word(s) or phrase(s) used to describe the
     * service / the data. <br>
     * <br>
     * Zero or more (optional) values for keywords are possible. <br>
     */
    private String[] keywords;

    /**
     * this constructor has all required attributes as its parameters. The other attributes will be set on
     * default values.
     * 
     * @param title
     * @throws OXFException
     */
    public Description(String title) {
        setTitle(title);
        abstractDescription = "";
        keywords = new String[] {""};
    }

    /**
     * this constructor has all required attributes as its parameters. The other attributes will be set on
     * default values.
     * 
     * @param title
     * @throws OXFException
     */
    public Description(String title, String abstractDescription, String[] keywords) {
        setTitle(title);
        setAbstractDescription(abstractDescription);
        setKeywords(keywords);
    }

    /**
     * sets the title.
     * 
     * @param title
     * @throws IllegalArgumentException
     *         if the title is empty.
     */
    protected void setTitle(String title) throws IllegalArgumentException {
        if (title != null && !title.equals("")) {
            this.title = title;
        }
        else {
            throw new IllegalArgumentException("The parameter 'title' is illegal.");
        }
    }

    /**
     * 
     * @param abstractDescription
     */
    protected void setAbstractDescription(String abstractDescription) {
        this.abstractDescription = abstractDescription;
    }

    /**
     * 
     * @param keywords
     */
    protected void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    /**
     * @return Returns the abstractDescription.
     */
    public String getAbstractDescription() {
        return abstractDescription;
    }

    /**
     * @return Returns the keywords.
     */
    public String[] getKeywords() {
        return keywords;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }
}