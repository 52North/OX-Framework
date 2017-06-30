/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
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
import java.io.IOException;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class JavaHelper {

    /**
     * transforms the oArray to a String[]
     *
     * @param oArray the Object[] that should be converted to String[] via Object[n].toString()
     *
     * @return a new String[] containing Object[].length String objects.
     */
    public static String[] toStringArray(Object[] oArray) {
        String[] sArray = new String[oArray.length];
        for (int i = 0; i < oArray.length; i++) {
            sArray[i] = oArray[i].toString();
        }
        return sArray;
    }

    /**
     * Adds the elements of the fromArray to the toArray.
     *
     * @param toArray the array to add to
     * @param fromArray the array to add
     *
     * @return Returns a new array containing the all elements from both arrays.
     * First, the elements of <code>toArray</code> and than the elements
     * of <code>fromArray</code>.
     */
    public static byte[] addArrayElements(byte[] toArray, byte[] fromArray) {

        byte[] newToArray = new byte[toArray.length + fromArray.length];

        System.arraycopy(toArray, 0, newToArray, 0, toArray.length);

        System.arraycopy(fromArray, 0, newToArray, toArray.length, fromArray.length);

        return newToArray;
    }

    /**
     * Adds the elements of the fromArray to the toArray.
     *
     * @param toArray the array to add to
     * @param fromArray the array to add
     *
     * @return Returns a new array containing the all elements from both arrays.
     * First, the elements of <code>toArray</code> and than the elements
     * of <code>fromArray</code>.
     */
    public static String[] addArrayElements(String[] toArray, String[] fromArray) {

        String[] newToArray = new String[toArray.length + fromArray.length];

        System.arraycopy(toArray, 0, newToArray, 0, toArray.length);

        System.arraycopy(fromArray, 0, newToArray, toArray.length, fromArray.length);

        return newToArray;
    }

    /**
     * Transforms a hexadecimal to a Color object. <br>
     * <br>
     * e.g.: '#FF6600' &rarr; orange
     *
     * @param hexString
     *        7 character hex-string; something like "#FF6600"
     * @return The Color object represented by the hexString given.
     */
    public static Color transformToColor(String hexString) {
        int red = Integer.parseInt(hexString.substring(1, 3), 16);
        int green = Integer.parseInt(hexString.substring(3, 5), 16);
        int blue = Integer.parseInt(hexString.substring(5, 7), 16);
        return new Color(red, green, blue);
    }

    /**
     * Generates a random file name with the specified parent path, midPart and postFix.
     *
     * @param parentPath the directory path
     * @param midPart this part will be used as filename part after a generated random part
     * @param postfix a postfix that will be added after midPart using "."
     *
     * @return a new File
     * @throws java.io.IOException when the directory for the file could not be generated
     *
     * @see #genFile(java.lang.String, java.lang.String, java.lang.String)
     * @see System#currentTimeMillis()
     */
    public static File genRndFile(String parentPath, String midPart, String postfix) throws IOException {
        String rndPart = "" + System.currentTimeMillis();
        midPart += rndPart;
        return genFile(parentPath, midPart, postfix);
    }

    /**
     * Generates a file name with the specified parent path, midPart and postFix.
     *
     * @param parentPath the directory path
     * @param midPart this part will be used as filename
     * @param postfix a postfix that will be added after midPart using "."
     *
     * @return a new File
     *
     * @throws IOException when the directory for the file could not be generated.
     */
    public static File genFile(String parentPath, String midPart, String postfix) throws IOException {
        // make path dir if not existing:
        File dir = new File(parentPath);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                throw new IOException("Could not create directory '" +
                        parentPath +
                        "'.");
            }
        }
        return new File(parentPath, normalize(midPart) + "." + postfix);
    }

    /**
     * @param toNormalize String to normalize.
     *
     * @return Returns a normalized String for use in a file path, i.e. all [\,/,:,*,?,",&lt;,&gt;] characters are
     *         replaced by '_'.
     */
    public static String normalize(String toNormalize) {
        return toNormalize.replaceAll("[\\\\,/,:,\\*,?,\",<,>]", "_");
    }

    /**
     * deletes all files in the specified directory which are older than olderThanTimeMillis.
     *
     * @param dirToClean the directory to clean
     * @param olderThanTimeMillis the timestamp that will be compared with {@link File#lastModified()}
     * 			of each file in the given folder. Older files will be deleted.
     *
     * @throws IOException when a file in the given directory could not be deleted.
     */
    public static void cleanUpDir(String dirToClean, int olderThanTimeMillis) throws IOException {
        File path = new File(dirToClean);

        if (path.isDirectory()) {
            final File[] files = path.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                if (file != null && file.lastModified() < System.currentTimeMillis() - olderThanTimeMillis) {
                    if (!file.delete()) {
                        throw new IOException("Could not delete file '" +
                                file.getAbsolutePath() +
                                "'.");
                    }
                }
            }
        }
    }

    /**
     * deletes all files in the specified directory which are older than olderThanTimeMillis and have the
     * defined postFix.
     *
     * @param dirToClean the directory to clean
     * @param olderThanTimeMillis the timestamp that will be compared with {@link File#lastModified()}
     * 			of each file in the given folder. Older files will be deleted.
     * @param postFix Only files ending with <code>postfix</code> will be deleted
     *
     * @throws IOException when a file in the given directory could not be deleted.
     */
    public static void cleanUpDir(String dirToClean, int olderThanTimeMillis, String postFix) throws IOException {
        File path = new File(dirToClean);

        if (path.isFile()) {
            final File[] files = path.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                if (file != null) {
                    String name = file.getName();
                    if (name.length() > 4) {
                        if (name.substring(name.length() - 4).equalsIgnoreCase("." + postFix)) {
                            if (file.lastModified() < System.currentTimeMillis() - olderThanTimeMillis) {
                                if (!file.delete()) {
                                    throw new IOException("Could not delete file '" +
                                            file.getAbsolutePath() +
                                            "'.");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
