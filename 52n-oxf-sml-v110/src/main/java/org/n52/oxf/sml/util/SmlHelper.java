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

package org.n52.oxf.sml.util;

/**
 * Copyright (C) 2012
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

import net.opengis.sensorML.x101.AbstractProcessType;
import net.opengis.sensorML.x101.ComponentType;
import net.opengis.sensorML.x101.IdentificationDocument.Identification;
import net.opengis.sensorML.x101.IdentificationDocument.Identification.IdentifierList;
import net.opengis.sensorML.x101.IdentificationDocument.Identification.IdentifierList.Identifier;
import net.opengis.sensorML.x101.InterfaceDocument.Interface;
import net.opengis.sensorML.x101.InterfacesDocument.Interfaces;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sensorML.x101.SensorMLDocument.SensorML.Member;
import net.opengis.sensorML.x101.SystemType;
import net.opengis.sensorML.x101.TermDocument.Term;
import net.opengis.swe.x101.DataComponentPropertyType;
import net.opengis.swe.x101.DataRecordType;
import net.opengis.swes.x20.InsertSensorType.ProcedureDescription;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.xmlbeans.tools.XmlUtil;

/**
 * A helper class to conveniently read SensorML documents. <br/>
 * <br/>
 * An instance is read into an internal cache for performance reasons. That means that once read, the helper
 * can be reused on the same instance. One could decide to hold the helper instance on caller side instead of
 * the sensorML document. If needed the cache can be cleared by {@link #clearCachedSml()}.
 */
public class SmlHelper {

    public SensorMLDocument cachedDocument;

    // XXX think of how and where to separate those constants
    
    public static final String UNIQUE_ID_NAME = "uniqueID";
    
    public static final String LONG_NAME_NAME = "longName";

    public static final String SHORT_NAME_NAME = "shortName";

    public static final String OGC_UNIQUE_ID_URN = "urn:ogc:def:identifier:OGC:uniqueID";
    
    public static final String OGC_LONG_NAME_URN = "urn:ogc:def:identifier:OGC:1.0:longName";

    public static final String OGC_SHORT_NAME_URN = "urn:ogc:def:identifier:OGC:1.0:shortName";

    /**
     * @param smlContainer
     *        the xml container containing the SensorML description
     * @return the procedure description parsed as SensorML
     * @throws XmlException
     *         if parsing to SensorML fails.
     */
    public SensorMLDocument getSmlFrom(XmlObject smlContainer) throws XmlException {
        return (SensorMLDocument) XmlUtil.getXmlAnyNodeFrom(smlContainer, "SensorML");
    }

    /**
     * Parses the passed procedure description into SensorML and caches the result for later use. If a new
     * SensorML description shall be cachedDocument the cachedDocument instance can be cleared by
     * {@link #clearCachedSml()}.
     * 
     * @param smlContainer
     *        the xml container containing the SensorML description
     * @return the procedure description parsed as SensorML
     * @throws XmlException
     *         if parsing to SensorML fails.
     * 
     * @see #getSmlFrom(ProcedureDescription)
     * @see #clearCachedSml()
     */
    public SensorMLDocument getCachedSmlFrom(XmlObject smlContainer) throws XmlException {
        return cachedDocument = (cachedDocument == null) ? getSmlFrom(smlContainer) : cachedDocument;
    }

    /**
     * Clears the cachedDocument SensorML description.
     */
    public void clearCachedSml() {
        cachedDocument = null;
    }

    public SystemType asSystem(AbstractProcessType process) {
        if (isSystem(process)) {
            return (SystemType) process;
        }
        else {
            throw new IllegalArgumentException("Process type is not describing a sml:system.");
        }
    }

    public ComponentType asComponent(AbstractProcessType process) {
        if (isComponent(process)) {
            return (ComponentType) process;
        }
        else {
            throw new IllegalArgumentException("Process type is not describing a sml:component.");
        }
    }

    public String findUniqueIdFrom(Identification smlIdentification) {
        return getIdValueFrom(UNIQUE_ID_NAME, OGC_UNIQUE_ID_URN, smlIdentification);
    }

    /**
     * Gets the first Identification entry of the first SensorML member.
     * 
     * @param smlDoc
     *        the sensor description document.
     * @return the first Identification of the first member found in the sensor description, or
     *         <code>null</code> if not found.
     */
    public Identification getIdentification(SensorMLDocument smlDoc) {
        Member[] members = smlDoc.getSensorML().getMemberArray();
        if (members.length > 0) {
            if (members[0].isSetProcess()) {
                AbstractProcessType process = members[0].getProcess();
                Identification[] identificationArray = process.getIdentificationArray();
                if (identificationArray.length > 0) {
                    return identificationArray[0];
                }
            }
        }
        return null;
    }

    /**
     * Finds the value from a given sml:identification that matches either <code>name</code>.
     * 
     * @param name
     *        the name to search for (mandatory)
     * @param identifier
     *        the sml:identifier to search
     * @return <code>true</code> if <code>name</code> or <code>definition</code> could be found in the
     *         sml:identifier
     */
    public String getIdValueFrom(String name, Identification smlIdentification) {
        return getIdValueFrom(name, null, smlIdentification.getIdentifierList());
    }

    /**
     * Finds the value from a given sml:identification that matches either <code>name</code> or
     * <code>definition</code>.
     * 
     * @param name
     *        the name to search for (mandatory)
     * @param definition
     *        the definition to search for (optional)
     * @param identifier
     *        the sml:identifier to search
     * @return <code>true</code> if <code>name</code> or <code>definition</code> could be found in the
     *         sml:identifier
     */
    public String getIdValueFrom(String name, String definition, Identification smlIdentification) {
        return getIdValueFrom(name, definition, smlIdentification.getIdentifierList());
    }

    private String getIdValueFrom(String name, String definition, IdentifierList in) {
        for (Identifier smlIdentifier : in.getIdentifierArray()) {
            if (isSoughtIdentifier(smlIdentifier, name, definition)) {
                return smlIdentifier.getTerm().getValue();
            }
        }
        return null;
    }

    private boolean isSoughtIdentifier(Identifier identifier, String name, String definition) {
        boolean nameDeclared = isTermWithName(identifier, name);
        boolean definitionDeclared = isTermWithDefinition(identifier.getTerm(), definition);
        return nameDeclared || ( (definition != null) && definitionDeclared);
    }

    private boolean isTermWithName(Identifier identifier, String name) {
        return identifier.isSetName() && identifier.getName().equals(name);
    }

    private boolean isTermWithDefinition(Term term, String definition) {
        return term.isSetDefinition() && term.getDefinition().equals(definition);
    }

    public boolean isComponent(AbstractProcessType process) {
        return process.schemaType() == ComponentType.type;
    }

    public boolean isSystem(AbstractProcessType process) {
        return process.schemaType() == SystemType.type;
    }

    public Interface getServiceInterface(String name, Interfaces interfaces) {
        if (interfaces.isSetInterfaceList()) {
            for (Interface smlInterface : interfaces.getInterfaceList().getInterfaceArray()) {
                if (smlInterface.getName().equals(name)) {
                    return smlInterface;
                }
            }
        }
        return null;
    }

    public String getStringValue(DataComponentPropertyType field) {
        if ( !isText(field)) {
            throw new IllegalArgumentException("No Text field");
        }
        return field.getText().getValue();
    }

    public DataComponentPropertyType getField(String name, DataRecordType dataRecord) {
        for (DataComponentPropertyType field : dataRecord.getFieldArray()) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    public boolean isScalar(DataComponentPropertyType field) {
        return isNumerical(field) || isBoolean(field) || isCategory(field) || isText(field);
    }

    private boolean isBoolean(DataComponentPropertyType field) {
        return field.isSetBoolean();
    }

    private boolean isCategory(DataComponentPropertyType field) {
        return field.isSetCategory();
    }

    private boolean isText(DataComponentPropertyType field) {
        return field.isSetText();
    }

    private boolean isNumerical(DataComponentPropertyType field) {
        return isCount(field) || isQuantity(field) || isTime(field);
    }

    private boolean isCount(DataComponentPropertyType field) {
        return field.isSetCount();
    }

    private boolean isQuantity(DataComponentPropertyType field) {
        return field.isSetQuantity();
    }

    private boolean isTime(DataComponentPropertyType field) {
        return field.isSetTime();
    }

}
