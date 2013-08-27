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
package org.n52.oxf.sos.adapter.wrapper.builder;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.gml.MetaDataPropertyType;
import net.opengis.gml.TimePeriodType;
import net.opengis.sensorML.x101.CapabilitiesDocument.Capabilities;
import net.opengis.sensorML.x101.ClassificationDocument.Classification.ClassifierList;
import net.opengis.sensorML.x101.ClassificationDocument.Classification.ClassifierList.Classifier;
import net.opengis.sensorML.x101.ComponentsDocument.Components.ComponentList;
import net.opengis.sensorML.x101.ComponentsDocument.Components.ComponentList.Component;
import net.opengis.sensorML.x101.ContactInfoDocument.ContactInfo.Address;
import net.opengis.sensorML.x101.IdentificationDocument.Identification.IdentifierList;
import net.opengis.sensorML.x101.IdentificationDocument.Identification.IdentifierList.Identifier;
import net.opengis.sensorML.x101.InputsDocument.Inputs.InputList;
import net.opengis.sensorML.x101.InterfaceDocument.Interface;
import net.opengis.sensorML.x101.IoComponentPropertyType;
import net.opengis.sensorML.x101.KeywordsDocument.Keywords.KeywordList;
import net.opengis.sensorML.x101.OutputsDocument.Outputs.OutputList;
import net.opengis.sensorML.x101.PositionDocument.Position;
import net.opengis.sensorML.x101.ResponsiblePartyDocument.ResponsibleParty;
import net.opengis.sensorML.x101.SystemDocument;
import net.opengis.sensorML.x101.SystemType;
import net.opengis.sensorML.x101.TermDocument.Term;
import net.opengis.swe.x101.AbstractDataRecordType;
import net.opengis.swe.x101.BooleanDocument.Boolean;
import net.opengis.swe.x101.CountDocument.Count;
import net.opengis.swe.x101.DataComponentPropertyType;
import net.opengis.swe.x101.DataRecordType;
import net.opengis.swe.x101.EnvelopeType;
import net.opengis.swe.x101.PositionType;
import net.opengis.swe.x101.QuantityDocument.Quantity;
import net.opengis.swe.x101.TextDocument.Text;
import net.opengis.swe.x101.UomPropertyType;
import net.opengis.swe.x101.VectorType;
import net.opengis.swe.x101.VectorType.Coordinate;

import org.apache.xmlbeans.XmlCursor;

/**
 * Sensor description generating class. All data that describes the sensor is
 * assembled in this class. The output is a ready to use SensorML document,
 * which will be directly passed to the request. To add further sensor
 * describing information just add certain methods in this class. For now this
 * class implements the discovery profile.
 * 
 * @author Eric
 */
public class SensorDescriptionBuilder {
	
	public static final QName SWE101_DATARECORD = new QName("http://www.opengis.net/swe/1.0.1", "DataRecord");
	public static final QName SWE101_FIELD = new QName("http://www.opengis.net/swe/1.0.1", "Field");
	public static final QName SWE101_COMPONENT = new QName("http://www.opengis.net/swe/1.0.1", "Component");
	public static final QName SWE10_OFFERING = new QName("http://www.opengis.net/sos/1.0", "offering");
	public static final QName SWE101_ENVELOPE = new QName ("http://www.opengis.net/swe/1.0.1","Envelope");
	public static final QName SWE10_ID = new QName("http://www.opengis.net/sos/1.0", "id");
	public static final QName SWE10_NAME = new QName("http://www.opengis.net/sos/1.0", "name");
	
	public static final String UNIQUE_ID_NAME = "uniqueID";
	public static final String UNIQUE_ID_DEF = "urn:ogc:def:identifier:OGC:1.0:uniqueID";
	public static final String LONG_NAME_NAME = "longName";
	public static final String LONG_NAME_DEF = "urn:ogc:def:identifier:OGC:1.0:longName";
	public static final String SHORT_NAME_NAME = "shortName";
	public static final String SHORT_NAME_DEF = "urn:ogc:def:identifier:OGC:1.0:shortName";
	public static final String PARENT_SYSTEM_UNIQUE_ID_NAME = "parentSystemUniqueID";
	public static final String PARENT_SYSTEM_UNIQUE_ID_DEF = "urn:ogc:def:identifier:OGC:1.0:parentSystemUniqueID";
	public static final String INTENDED_APPLICATION_NAME = "intendedApplication";
	public static final String INTENDED_APPLICATION_DEF = "urn:ogc:def:classifier:OGC:1.0:application";
	public static final String SENSOR_TYPE_NAME = "sensorType";
	public static final String SENSOR_TYPE_DEF = "urn:ogc:def:classifier:OGC:1.0:sensorType";
	public static final String EPSG_CODE_PREFIX = "urn:ogc:def:crs:EPSG::";
	public static final String SERVICE_INTERFACE = "urn:ogc:def:interface:OGC:1.0:SWEServiceInterface";
	public static final String SERVICE_URL = "urn:ogc:def:interface:OGC:1.0:ServiceURL";
	public static final String SERVICE_TYPE = "urn:ogc:def:interface:OGC:1.0:ServiceType";
	public static final String SERVICE_SPECIFIC_SENSOR_ID = "urn:ogc:def:interface:OGC:1.0:ServiceSpecificSensorID";
	public static final String OGC_DISCOVERY_OBSERVED_BBOX_DEFINITION = "urn:ogc:def:property:OGC:1.0:observedBBOX";
	
	public static final String SOS_OBSERVATION_TYPE_TEXT = "TEXT";
	
	private SystemDocument sysDoc;
	private SystemType system;
	
	private final String[] validTime;
	private final List<String> keywords;
	private final List<String[]> identifier, classifier; // {name, definition, value}
	private final List<String[]> inputs; // {name, definition}
	private final List<String[]> outputsMeasurement; // {name, definition, offeringUri, offeringName, uom }
	private final List<String[]> outputsText; // {name, definition, offeringUri, offeringName }
	private final List<String[]> outputsBoolean; // {name, definition, offeringUri, offeringName }
	private final List<String[]> outputsCount; // {name, definition, offeringUri, offeringName }
	private final List<String[]> componentXlink; // {name, xlink}
	private final List<String[]> componentInline; // {name, description}

	// capabilities data
	private String statusName;
	private boolean isCollecting;
	private String lcEastingUom, lcNorthingUom, ucEastingUom, ucNorthingUom;
	private double lcEastingValue, lcNorthingValue, ucEastingValue, ucNorthingValue;
	private String foiName, foiUri;
	// contact data
	private String id, individualName, organizationName, deliveryPoint, city,
			postalCode, country, email;
	// position data
	private String positionName, referenceFrame, vectorId, eastingUom,
			northingUom, altitudeUom;
	private double eastingValue, northingValue, altitudeValue;
	// interface data
	private String iName, serviceUrl, serviceType, sensorId;

	public SensorDescriptionBuilder() {
		validTime = new String[2];
		keywords = new ArrayList<String>();
		identifier = new ArrayList<String[]>();
		classifier = new ArrayList<String[]>();
		inputs = new ArrayList<String[]>();
		outputsMeasurement = new ArrayList<String[]>();
		outputsText = new ArrayList<String[]>();
		outputsBoolean = new ArrayList<String[]>();
		outputsCount = new ArrayList<String[]>();
		componentXlink = new ArrayList<String[]>();
		componentInline = new ArrayList<String[]>();
	}
	
	public void addKeyword(final String keyword) {
		keywords.add(keyword);
	}
	
	public void addIdentifier(final String name, final String definition, final String value) {
		if (entryExists(definition, identifier)) {
			removeEntry(definition, identifier);
		}
		identifier.add(new String[] { name, definition, value });
	}

	public void setIdentifierUniqeId(final String value) {
		if (entryExists(UNIQUE_ID_DEF, identifier)) {
			removeEntry(UNIQUE_ID_DEF, identifier);
		}
		addIdentifier(UNIQUE_ID_NAME, UNIQUE_ID_DEF, value);
	}

	public void setIdentifierLongName(final String value) {
		if (entryExists(LONG_NAME_DEF, identifier)) {
			removeEntry(LONG_NAME_DEF, identifier);
		}
		addIdentifier(LONG_NAME_NAME, LONG_NAME_DEF, value);
	}

	public void setIdentifierShortName(final String value) {
		if (entryExists(SHORT_NAME_DEF, identifier)) {
			removeEntry(SHORT_NAME_DEF, identifier);
		}
		addIdentifier(SHORT_NAME_NAME, SHORT_NAME_DEF, value);
	}

	public void setIdentifierParentSystemUniqueId(final String value) {
		if (entryExists(PARENT_SYSTEM_UNIQUE_ID_DEF, identifier)) {
			removeEntry(PARENT_SYSTEM_UNIQUE_ID_DEF, identifier);
		}
		addIdentifier(PARENT_SYSTEM_UNIQUE_ID_NAME, PARENT_SYSTEM_UNIQUE_ID_DEF, value);
	}
	
	public void addClassifier(final String name, final String definition, final String value) {
		if (entryExists(definition, classifier)) {
			removeEntry(definition, classifier);
		}
		classifier.add(new String[]{name, definition, value});
	}
	
	public void setClassifierIntendedApplication(final String value) {
		if (entryExists(INTENDED_APPLICATION_DEF, classifier)) {
			removeEntry(INTENDED_APPLICATION_DEF, classifier);
		}
		addClassifier(INTENDED_APPLICATION_NAME, INTENDED_APPLICATION_DEF, value);
	}
	
	public void setClassifierSensorType(final String value) {
		if (entryExists(SENSOR_TYPE_DEF, classifier)) {
			removeEntry(SENSOR_TYPE_DEF, classifier);
		}
		addClassifier(SENSOR_TYPE_NAME, SENSOR_TYPE_DEF, value);
	}
	
	public void setValidTime(final String begin, final String end) {
		validTime[0] = begin;
		validTime[1] = end;
	}
	
	public void setCapabilityCollectingStatus(final String statusName, final boolean isCollecting) {
		this.statusName = statusName;
		this.isCollecting = isCollecting;
	}
	
	public void setCapabilityBbox(final String lcEastingUom, final double lcEastingValue,
			final String lcNorthingUom, final double lcNorthingValue, final String ucEastingUom,
			final double ucEastingValue, final String ucNorthingUom, final double ucNorthingValue) {
		this.lcEastingUom = lcEastingUom; // lower corner
		this.lcEastingValue = lcEastingValue;
		this.lcNorthingUom = lcNorthingUom;
		this.lcNorthingValue = lcNorthingValue;
		this.ucEastingUom = ucEastingUom; // upper corner
		this.ucEastingValue = ucEastingValue;
		this.ucNorthingUom = ucNorthingUom;
		this.ucNorthingValue = ucNorthingValue;
	}
	
	public void addFeatureOfInterest(final String foiName, final String foiUri) {
		this.foiName = foiName;
		this.foiUri = foiUri;
	}
	
	public void setContact(final String id, final String individualName,
			final String organizationName, final String deliveryPoint, final String city,
			final String postalCode, final String country, final String email) {
		this.id = id;
		this.individualName = individualName;
		this.organizationName = organizationName;
		this.deliveryPoint = deliveryPoint;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;
		this.email = email;
	}

	public void setPosition(final String positionName, final String referenceFrame,
			final String vectorId, final String eastingUom, final double eastingValue,
			final String northingUom, final double northingValue, final String altitudeUom,
			final double altiValue) {
		this.positionName = positionName;
		this.referenceFrame = referenceFrame;
		this.vectorId = vectorId;
		this.eastingUom = eastingUom;
		this.eastingValue = eastingValue;
		this.northingUom = northingUom;
		this.northingValue = northingValue;
		// optional values
		this.altitudeUom = altitudeUom;
		altitudeValue = altiValue;
	}
	
	public void setInterface(final String iName, final String serviceUrl, final String serviceType,
			final String sensorId) {
		this.iName = iName;
		this.serviceUrl = serviceUrl;
		this.serviceType = serviceType;
		this.sensorId = sensorId;
	}
	
	public void addInput(final String name, final String definition) {
		inputs.add(new String[] { name, definition });
	}
	
	public void addOutputMeasurement(final String name, final String definition, final String offeringUri, final String offeringName, final String uom) {
		outputsMeasurement.add(new String[] { name, definition, offeringUri, offeringName, uom});
	}
	
	public void addOutputText(final String name, final String definition, final String offeringUri, final String offeringName) {
		outputsText.add(new String[] { name, definition, offeringUri, offeringName});
	}
	
	public void addOutputBoolean(final String name, final String definition, final String offeringUri, final String offeringName) {
		outputsBoolean.add(new String[] { name, definition, offeringUri, offeringName});
	}
	
	public void addOutputCount(final String name, final String definition, final String offeringUri, final String offeringName) {
		outputsCount.add(new String[] { name, definition, offeringUri, offeringName});
	}

	public void addComponentXlink(final String name, final String url) {
		componentXlink.add(new String[] { name, url });
	}
	
	public void addComponentInline(final String name, final String description) {
		componentInline.add(new String[] { name, description });
	}
	
	private boolean entryExists(final String definition, final List<String[]> identifiers) {
		boolean exists = false;
		for (final String[] id : identifiers) {
			if (id[1].equals(definition)) {
				exists = true;
				break;
			}
		}
		return exists;
	}
	
	private void removeEntry(final String definition, final List<String[]> identifiers) {
		for (final String[] id : identifiers) {
			if (id[1].equals(definition)) {
				identifiers.remove(id);
				break;
			}
		}
	}
	
	public SystemDocument buildSensorDescription() {
		sysDoc = SystemDocument.Factory.newInstance();
		system = sysDoc.addNewSystem();
		if (!keywords.isEmpty()) {
			addKeywords();
		}
		if (!identifier.isEmpty()) {
			addIdentifier();
		}
		if (!classifier.isEmpty()) {
			addClassifier();
		}
		if (validTime[0] != null && validTime[1] != null) {
			addValidTime();
		}
		if (foiName != null && foiUri != null && lcEastingUom != null
				&& lcNorthingUom != null) {
			addCapabilities();
		}
		if (id != null && individualName != null && organizationName != null
				&& deliveryPoint != null && city != null && postalCode != null
				&& country != null && email != null) {
			addContact();
		}
		if (positionName != null && referenceFrame != null && vectorId != null
				&& eastingUom != null && northingUom != null) {
			addPosition();
		}
		if (iName != null && serviceUrl != null && serviceType != null
				&& sensorId != null) {
			addInterface();
		}
		if (!inputs.isEmpty()) {
			addInputs();
		}
		if (!outputsMeasurement.isEmpty() || !outputsText.isEmpty() ||
				!outputsBoolean.isEmpty() || !outputsCount.isEmpty()) {
			addOutputs();
		}
		if (!componentXlink.isEmpty() || !componentInline.isEmpty()) {
			addComponents();
		}
		
		return sysDoc;
	}
	
	private void addKeywords() {
		final KeywordList keywordList = system.addNewKeywords().addNewKeywordList();
		for (final String keyword : keywords) {
			keywordList.addKeyword(keyword);
		}
	}
	
	private void addIdentifier() {
		final IdentifierList identifierList = system.addNewIdentification().addNewIdentifierList();
		
		Identifier forId;
		Term forTerm;
		for (final String[] id : identifier) {
			forId = identifierList.addNewIdentifier();
			forId.setName(id[0]);
			forTerm = forId.addNewTerm();
			forTerm.setDefinition(id[1]);
			forTerm.setValue(id[2]);
		}
	}
	
	private void addClassifier() {
		final ClassifierList classifierList = system.addNewClassification().addNewClassifierList();

		Classifier forClassifier;
		Term forTerm;
		for (final String[] classifier : this.classifier) {
			forClassifier = classifierList.addNewClassifier();
			forClassifier.setName(classifier[0]);
			forTerm = forClassifier.addNewTerm();
			forTerm.setDefinition(classifier[1]);
			forTerm.setValue(classifier[2]);
		}
	}
	
	private void addValidTime() {
		final TimePeriodType period = system.addNewValidTime().addNewTimePeriod();
		period.addNewBeginPosition().setStringValue(validTime[0]);
		period.addNewEndPosition().setStringValue(validTime[1]);
	}
	
	private void addCapabilities() {
		final Capabilities caps = system.addNewCapabilities();
		DataRecordType dataRecordType;
	    DataComponentPropertyType f;
	    Boolean b;
	    Text t;
	    final AbstractDataRecordType adrt = caps.addNewAbstractDataRecord();
	    dataRecordType = (DataRecordType) adrt.substitute(SWE101_DATARECORD, DataRecordType.type);
	    
	    // Status of the Sensor (collecting data?)
	    f =  dataRecordType.addNewField();
	    f.setName(statusName);
	    b = f.addNewBoolean();
	    b.setValue(isCollecting);
	    
	    // add foi id
	    f = dataRecordType.addNewField();
	    f.setName("FeatureOfInterestID");
	    t = f.addNewText();
	    t.setDefinition("FeatureOfInterest identifier");
	    t.setValue(foiName);
	    
	    // add foi name
	    f = dataRecordType.addNewField();
	    f.setName("FeatureOfInterestName");
	    t = f.addNewText();
	    t.setValue(foiUri);
	    
	    // add observed boundingbox
	    // TODO implement solution for moving sensors
	    final DataComponentPropertyType observedBBoxField = dataRecordType.addNewField();
		observedBBoxField.setName("observedBBOX");
		final AbstractDataRecordType aDRT = observedBBoxField.addNewAbstractDataRecord();
		final EnvelopeType envelope = (EnvelopeType) aDRT.substitute(SWE101_ENVELOPE, EnvelopeType.type);
		envelope.setDefinition(OGC_DISCOVERY_OBSERVED_BBOX_DEFINITION);
		envelope.addNewLowerCorner().setVector(getLowerCornerOfObservedBBox());
		envelope.addNewUpperCorner().setVector(getUpperCornerOfObservedBBox());
	    
	    caps.setAbstractDataRecord(dataRecordType);
	}
	
	private VectorType getLowerCornerOfObservedBBox() {
		final VectorType positionVector = VectorType.Factory.newInstance();
		final Coordinate easting = positionVector.addNewCoordinate();
		easting.setName("easting");
		final Quantity eastingValue = easting.addNewQuantity();
		eastingValue.setAxisID("x");
		eastingValue.addNewUom().setCode(lcEastingUom);
		eastingValue.setValue(lcEastingValue);
		
		final Coordinate northing = positionVector.addNewCoordinate();
		northing.setName("northing");
		final Quantity northingValue = northing.addNewQuantity();
		northingValue.setAxisID("y");
		northingValue.addNewUom().setCode(lcNorthingUom);
		northingValue.setValue(lcNorthingValue);
		
		return positionVector;
	}
	
	// TODO implement for moving sensors
	private VectorType getUpperCornerOfObservedBBox() {
		return getLowerCornerOfObservedBBox();
	}
	
	private void addContact() {
		final ResponsibleParty respParty = system.addNewContact().addNewResponsibleParty();
		respParty.setId(id);
		respParty.setIndividualName(individualName);
		respParty.setOrganizationName(organizationName);
		final Address address = respParty.addNewContactInfo().addNewAddress();
		address.setDeliveryPointArray(new String[] {deliveryPoint});
		address.setCity(city);
		address.setPostalCode(postalCode);
		address.setCountry(country);
		address.setElectronicMailAddress(email);
	}
	
	private void addPosition() {
		final Position p = system.addNewPosition();
		p.setName(positionName);
		
	    PositionType pT;
	    pT = p.addNewPosition();
	    pT.setReferenceFrame(EPSG_CODE_PREFIX + referenceFrame);	    
	    
	    final VectorType vT = pT.addNewLocation().addNewVector();
	    vT.setId(vectorId);
	    
	    // Easting
	    final Coordinate easting = vT.addNewCoordinate();
	    easting.setName("easting");
	    final Quantity eastQuantity = easting.addNewQuantity();
	    eastQuantity.setAxisID("x");
	    final UomPropertyType eastUom = eastQuantity.addNewUom();
	    eastUom.setCode(eastingUom);
	    eastQuantity.setValue(eastingValue);

	    // Northing
	    final Coordinate northing = vT.addNewCoordinate();
	    northing.setName("northing");
	    final Quantity northQuantity = northing.addNewQuantity();
	    northQuantity.setAxisID("y");
	    final UomPropertyType northUom = northQuantity.addNewUom();
	    northUom.setCode(northingUom);
	    northQuantity.setValue(northingValue);

	    // OPTIONAL: Altitude (depends on CRS)
	    if (altitudeUom != null) 
	    {
	    	final Coordinate altitude = vT.addNewCoordinate();
	    	altitude.setName("altitude");
	    	final Quantity altiQuantity = altitude.addNewQuantity();
	    	altiQuantity.setAxisID("z");
	    	final UomPropertyType altiUom = altiQuantity.addNewUom();
	    	altiUom.setCode(altitudeUom);
	    	altiQuantity.setValue(altitudeValue);
	    }
	}
	
	private void addInterface() {
		final Interface  interFace = system.addNewInterfaces().addNewInterfaceList().addNewInterface();
		interFace.setName(iName);
		final AbstractDataRecordType interfaceDataRecord = interFace
				.addNewInterfaceDefinition().addNewServiceLayer()
				.addNewAbstractDataRecord();
		
		final DataRecordType interfaceDr = (DataRecordType) interfaceDataRecord
				.substitute(SWE101_DATARECORD, DataRecordType.type);
		interfaceDr.setDefinition(SERVICE_INTERFACE);
		
		final DataComponentPropertyType serviceUrlField = interfaceDr.addNewField();
		serviceUrlField.setName(SERVICE_URL);
		serviceUrlField.addNewText().setValue(serviceUrl);
		
		final DataComponentPropertyType serviceTypeField = interfaceDr.addNewField();
		serviceTypeField.setName(SERVICE_TYPE);
		serviceTypeField.addNewText().setValue(serviceType);
		
		final DataComponentPropertyType serviceSpecificSensorIdField = interfaceDr.addNewField();
		serviceSpecificSensorIdField.setName(SERVICE_SPECIFIC_SENSOR_ID);
		serviceSpecificSensorIdField.addNewText().setValue(sensorId);
	}
	
	private void addInputs() {
		final InputList inputList = system.addNewInputs().addNewInputList();
		IoComponentPropertyType input;
		for (final String[] inputValues : inputs) {
			input = inputList.addNewInput();
			input.setName(inputValues[0]);
			input.addNewObservableProperty().setDefinition(inputValues[1]);
		}
	}

	private void addOutputs() {
		final OutputList outputList = system.addNewOutputs().addNewOutputList();
		IoComponentPropertyType output;

		for (final String[] outputValues : outputsMeasurement) { // sos 1.0 52n implementation
			output = outputList.addNewOutput();
			output.setName(outputValues[0]);
			final Quantity quantity = output.addNewQuantity();
			quantity.setDefinition(outputValues[1]);
			addOfferingMetadata(quantity.addNewMetaDataProperty(), outputValues[2], outputValues[3]);
			quantity.addNewUom().setCode(outputValues[4]);
		}
		
		for (final String[] outputValues : outputsText) { // sos 1.0 52n implementation
			output = outputList.addNewOutput();
			output.setName(outputValues[0]);
			final Text text = output.addNewText();
			text.setDefinition(outputValues[1]);
			addOfferingMetadata(text.addNewMetaDataProperty(), outputValues[2], outputValues[3]);
		}
		
		for (final String[] outputValues : outputsBoolean) { // sos 1.0 52n implementation
			output = outputList.addNewOutput();
			output.setName(outputValues[0]);
			final Boolean bool = output.addNewBoolean();
			bool.setDefinition(outputValues[1]);
			addOfferingMetadata(bool.addNewMetaDataProperty(), outputValues[2], outputValues[3]);
		}
		
		for (final String[] outputValues : outputsCount) { // sos 1.0 52n implementation
			output = outputList.addNewOutput();
			output.setName(outputValues[0]);
			final Count count = output.addNewCount();
			count.setDefinition(outputValues[1]);
			addOfferingMetadata(count.addNewMetaDataProperty(), outputValues[2], outputValues[3]);
		}
	}
	
	private void addOfferingMetadata(final MetaDataPropertyType addNewMetaDataProperty, final String uri, final String name) {
		final XmlCursor c = addNewMetaDataProperty.newCursor();
		c.toNextToken();
		c.beginElement(SWE10_OFFERING);
		c.insertElementWithText(SWE10_ID, uri);
		c.insertElementWithText(SWE10_NAME, name);
		c.dispose();
	}
	
	/*
	 * TODO add inline description setting
	 */
	private void addComponents() {
		final ComponentList compList = system.addNewComponents().addNewComponentList();
		// add xlink components
		Component comp;
		for (final String[] compValues : componentXlink) {
			comp = compList.addNewComponent();
			comp.setName(compValues[0]);
			comp.setHref(compValues[1]);
		}
		
		// add inline components
		for (final String[] compValues : componentInline) {
			comp = compList.addNewComponent();
			comp.setName(compValues[0]);
			// TODO set inline description
			// AbstractComponentType inlineComp = (AbstractComponentType) comp.substitute(SWE101_COMPONENT, ComponentType.type);
		}
	}
}