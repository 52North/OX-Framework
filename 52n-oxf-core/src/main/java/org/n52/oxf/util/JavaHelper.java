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
 
 Created on: 19.07.2007
 *********************************************************************************/

package org.n52.oxf.util;

import java.awt.Color;
import java.io.File;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class JavaHelper {

    /**
     * transforms the oArray to a String[]
     */
    public static String[] toStringArray(Object[] oArray) {
        String[] sArray = new String[oArray.length];
        for (int i = 0; i < oArray.length; i++) {
            sArray[i] = (String) oArray[i];
        }
        return sArray;
    }

    /**
     * adds the elements of the fromArray to the toArray.
     */
    public static byte[] addArrayElements(byte[] toArray, byte[] fromArray) {

        byte[] newToArray = new byte[toArray.length + fromArray.length];

        System.arraycopy(toArray, 0, newToArray, 0, toArray.length);

        System.arraycopy(fromArray, 0, newToArray, toArray.length, fromArray.length);

        return newToArray;
    }
    
    /**
     * adds the elements of the fromArray to the toArray.
     */
    public static String[] addArrayElements(String[] toArray, String[] fromArray) {

        String[] newToArray = new String[toArray.length + fromArray.length];

        System.arraycopy(toArray, 0, newToArray, 0, toArray.length);

        System.arraycopy(fromArray, 0, newToArray, toArray.length, fromArray.length);

        return newToArray;
    }

    /**
     * transforms a hexadecimal to a Color object. <br>
     * <br>
     * e.g.: '#FF6600' --> orange
     * 
     * @param hexString
     *        7 character hex-string; something like "#FF6600"
     * @return
     */
    public static Color transformToColor(String hexString) {
        int red = Integer.parseInt(hexString.substring(1, 3), 16);
        int green = Integer.parseInt(hexString.substring(3, 5), 16);
        int blue = Integer.parseInt(hexString.substring(5, 7), 16);
        return new Color(red, green, blue);
    }

    /**
     * genreates a random file name with the specified parent path, midPart and postFix.
     */
    public static File genRndFile(String parentPath, String midPart, String postfix) {
        String rndPart = "" + System.currentTimeMillis();
        midPart += rndPart;
        return genFile(parentPath, midPart, postfix);
    }

    /**
     * genreates a file name with the specified parent path, midPart and postFix.
     */
    public static File genFile(String parentPath, String midPart, String postfix) {
        // make path dir if not existing:
        File dir = new File(parentPath);
        if ( !dir.exists()) {
            dir.mkdir();
        }
        return new File(parentPath, normalize(midPart) + "." + postfix);
    }
    
    /**
     * @return a normalized String for use in a file path, i.e. all [\,/,:,*,?,",<,>] characters are
     *         replaced by '_'.
     */
    public static String normalize(String toNormalize) {
        return toNormalize.replaceAll("[\\\\,/,:,\\*,?,\",<,>]", "_");
    }

    /**
     * deletes all files in the specified directory which are older than olderThanTimeMillis.
     * 
     * @param dirToClean
     * @param olderThanTimeMillis
     */
    public static void cleanUpDir(String dirToClean, int olderThanTimeMillis) {
        File path = new File(dirToClean);

        for (File file : path.listFiles()) {
            if (file.lastModified() < System.currentTimeMillis() - olderThanTimeMillis) {
                file.delete();
            }
        }
    }

    /**
     * deletes all files in the specified directory which are older than olderThanTimeMillis and have the
     * defined postFix.
     * 
     * @param dirToClean
     * @param olderThanTimeMillis
     * @param postFix
     */
    public static void cleanUpDir(String dirToClean, int olderThanTimeMillis, String postFix) {
        File path = new File(dirToClean);

        for (File file : path.listFiles()) {

            String name = file.getName();
            if (name.length() > 4) {
                if (name.substring(name.length() - 4).equalsIgnoreCase("." + postFix)) {
                    if (file.lastModified() < System.currentTimeMillis() - olderThanTimeMillis) {
                        file.delete();
                    }
                }
            }
        }
    }

    /*
     * test
     */
    public static void main(String[] args) {

        System.out.println(transformToColor("#FF6600").toString());
    }
}