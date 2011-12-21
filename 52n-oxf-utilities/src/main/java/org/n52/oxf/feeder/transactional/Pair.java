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
 
 Created on: 22.07.2007
 *********************************************************************************/

package org.n52.oxf.feeder.transactional;

/**
 * General pair of two elements of any type.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 * @param <T1>
 * @param <T2>
 */
public class Pair<T1, T2> {

    T1 elem1;
    T2 elem2;

    /**
     * 
     * @param elem1
     * @param elem2
     */
    public Pair(T1 elem1, T2 elem2) {
        this.elem1 = elem1;
        this.elem2 = elem2;
    }

    /**
     * @return the elem1
     */
    public T1 getElem1() {
        return elem1;
    }

    /**
     * @param elem1
     *        the elem1 to set
     */
    public void setElem1(T1 elem1) {
        this.elem1 = elem1;
    }

    /**
     * @return the elem2
     */
    public T2 getElem2() {
        return elem2;
    }

    /**
     * @param elem2
     *        the elem2 to set
     */
    public void setElem2(T2 elem2) {
        this.elem2 = elem2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pair <");
        sb.append(elem1);
        sb.append(", ");
        sb.append(elem2);
        sb.append(">");
        return sb.toString();
    }

}
