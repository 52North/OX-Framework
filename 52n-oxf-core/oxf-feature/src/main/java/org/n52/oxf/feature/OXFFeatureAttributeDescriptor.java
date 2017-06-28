/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.feature;


public class OXFFeatureAttributeDescriptor /*implements org.opengis.feature.FeatureAttributeDescriptor*/ {

    protected String name;
    protected DataType dataType;
    protected Class objectClass;
    protected int minOccurrences = 0;
    protected int maxOccurrences = Integer.MAX_VALUE;
    protected String documentation = "";
    

    public class Attribute { // TODO integrate inner class
        private String name;
        private DataType type;

        public Attribute(String name, DataType type) {
            this.setName(name);
            this.type = type;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        DataType getType() {
            return type;
        }

        void setType(DataType type) {
            this.type = type;
        }

    }
    
    /**
     * 
     * @param name
     * @param dataType
     * @param objectClass
     */
    public OXFFeatureAttributeDescriptor(String name, DataType dataType, Class objectClass) {
        this.name = name;
        this.dataType = dataType;
        this.objectClass = objectClass;
    }

    /**
     * 
     * @param name
     * @param dataType
     * @param objectClass
     * @param minOccurrences
     * @param maxOccurrences
     * @param documentation
     */
    public OXFFeatureAttributeDescriptor(String name,
                                         DataType dataType,
                                         Class objectClass,
                                         int minOccurrences,
                                         int maxOccurrences,
                                         String documentation) {
        this(name, dataType, objectClass);
        this.minOccurrences = minOccurrences;
        this.maxOccurrences = maxOccurrences;
        this.documentation = documentation;
    }

    /**
     * Returns a documentation of this attribute.
     */
    public String getDocumentation() {
        return documentation;
    }

    // inherited methods:

    /**
     * Returns the name of this attribute. This is the name that can be used to retrieve the value of an
     * attribute and usually maps to either an XML element name or a column name in a relational database.
     */
    public String getName() {
        return name;
    }

    /**
     * This returns the Java {@link Class} object that class or interface that this attribute can be cast to.<br>
     * <br>
     * For attributes whose maximum cardinality is greater than one, this should return the []-type of the
     * class.
     */
    public Class getObjectClass() {
        return objectClass;
    }

    /**
     * Returns the minimum number of occurrences of this attribute on a given feature. The vast majority of
     * data sources and data consumers will only function with this value being zero or one. If the minimum
     * number of occurrences is zero, this is equivalent, in SQL terms, to the attribute being nillable.
     */
    public int getMinimumOccurrences() {
        return minOccurrences;
    }

    /**
     * Returns the maximum number of occurrences of this attribute on a given feature. The vast majority of
     * data sources and data consumers will only function with this value being one. A value of
     * {@link Integer#MAX_VALUE} indicates that the maximum number of occurrences is unbounded.
     */
    public int getMaximumOccurrences() {
        return maxOccurrences;
    }

    /**
     * Returns a constant from {@link DataType}. The return value of this method indicates how the return
     * values of {@link #getSize}, {@link #getPrecision}, and {@link #getObjectClass} should be interpreted.
     * <br>
     * <br>
     * For attributes whose maximum cardinality is greater than one, this should return the data type of the
     * individual elements of the collection.
     */
    public DataType getDataType() {
        return dataType;
    }

    // unsupported methods:

    /** not supported */
    public boolean isPrimaryKey() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /** not supported */
    public int getPrecision() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /** not supported */
    public int getSize() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    
}