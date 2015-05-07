/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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