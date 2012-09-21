package org.n52.oxf.sos.adapter.wrapper.builder;

import java.util.ArrayList;

import javax.xml.namespace.QName;

import net.opengis.gml.MetaDataPropertyType;
import net.opengis.gml.TimePeriodType;
import net.opengis.sensorML.x101.ComponentsDocument.Components.ComponentList;
import net.opengis.sensorML.x101.ComponentsDocument.Components.ComponentList.Component;
import net.opengis.sensorML.x101.InputsDocument.Inputs.InputList;
import net.opengis.sensorML.x101.InterfaceDocument.Interface;
import net.opengis.sensorML.x101.OutputsDocument.Outputs.OutputList;
import net.opengis.sensorML.x101.ResponsiblePartyDocument.ResponsibleParty;
import net.opengis.sensorML.x101.ClassificationDocument.Classification.ClassifierList;
import net.opengis.sensorML.x101.ContactInfoDocument.ContactInfo.Address;
import net.opengis.sensorML.x101.IdentificationDocument.Identification.IdentifierList.Identifier;
import net.opengis.sensorML.x101.IoComponentPropertyType;
import net.opengis.sensorML.x101.SystemDocument;
import net.opengis.sensorML.x101.SystemType;
import net.opengis.sensorML.x101.CapabilitiesDocument.Capabilities;
import net.opengis.sensorML.x101.ClassificationDocument.Classification.ClassifierList.Classifier;
import net.opengis.sensorML.x101.IdentificationDocument.Identification.IdentifierList;
import net.opengis.sensorML.x101.KeywordsDocument.Keywords.KeywordList;
import net.opengis.sensorML.x101.PositionDocument.Position;
import net.opengis.sensorML.x101.TermDocument.Term;
import net.opengis.swe.x101.AbstractDataRecordType;
import net.opengis.swe.x101.DataComponentPropertyType;
import net.opengis.swe.x101.DataRecordType;
import net.opengis.swe.x101.EnvelopeType;
import net.opengis.swe.x101.PositionType;
import net.opengis.swe.x101.UomPropertyType;
import net.opengis.swe.x101.VectorType;
import net.opengis.swe.x101.BooleanDocument.Boolean;
import net.opengis.swe.x101.CategoryDocument.Category;
import net.opengis.swe.x101.QuantityDocument.Quantity;
import net.opengis.swe.x101.TextDocument.Text;
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
	
//	private SystemDocument systemDocument = SystemDocument.Factory.newInstance();
//	private SystemType system = SystemType.Factory.newInstance();
//	
//	private Map<String, String> parameters = new HashMap<String, String>();
//	
//	/**
//	 * Creates an sensor describing document builder.
//	 * 
//	 * @param sensorId
//	 * @param sensorObservationType
//	 */
//	public SensorDescriptionBuilder(String sensorId, String sensorObservationType) {
//		system.setId(sensorId);
//		parameters.put(REGISTER_SENSOR_OBSERVATION_TYPE, sensorObservationType);
//	}
//	
//	/**
//	 * Return, if all necessary parameters are set, a ready to use SensorML document.
//	 * 
//	 * @return ready to use SensorML document
//	 */
//	public String generateSensorDescription() {
//    	system.setOutputs(createOutputsFromParameters());
//    	systemDocument.addNewSystem().set(system);    	
//    	SensorMLDocument sensorMLDocument = SensorMLDocument.Factory.newInstance();
//    	sensorMLDocument.addNewSensorML().addNewMember().set(systemDocument);
//    	
//    	// Get SensorML version by checking the class of SystemDocument - FROM OLD VERSION!!!
//    	if (systemDocument instanceof net.opengis.sensorML.x101.SystemDocument) {
//    		XmlCursor c = sensorMLDocument.getSensorML().newCursor();
//    		c.toNextToken();
//    		c.insertAttributeWithValue("version","1.0.1");
//    		c.dispose();
//    	}
//    	
//		return sensorMLDocument.toString();
//	}
//	
//	/**
//	 * Sets the position describing parameters.
//	 * 
//	 * @param name
//	 * @param positionFixed
//	 * @param latitude
//	 * @param longitude
//	 */
//	public void setPosition(String name, boolean positionFixed, double latitude, double longitude) {
//		Position pos = Position.Factory.newInstance();
//    	pos.setName(name);
//    	PositionType posType = pos.addNewPosition();
//    	posType.setFixed(new Boolean(positionFixed));
//    	posType.setReferenceFrame("urn:ogc:crs:epsg:4326");
//    	VectorPropertyType vecPropType =  posType.addNewLocation();
//    	VectorType vecType = vecPropType.addNewVector();
//    	Coordinate cordLatitude = vecType.addNewCoordinate();
//    	cordLatitude.setName("latitude");
//    	cordLatitude.addNewQuantity().setValue(new Double (latitude));
//    	Coordinate cordLongitude = vecType.addNewCoordinate();
//    	cordLongitude.setName("longitude");
//    	cordLongitude.addNewQuantity().setValue(new Double (longitude));
//    	system.setPosition(pos);
//	}
//	
//	/**
//	 * sets the observed property parameter.
//	 * 
//	 * @param observedProperty
//	 */
//	public void setObservedProperty(String observedProperty) {
//		parameters.put(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER, observedProperty);
//	}
//	
//	/**
//	 * Sets the unit of measurement paramter.
//	 * 
//	 * @param uom
//	 */
//	public void setUnitOfMeasurement(String uom) {
//		parameters.put(REGISTER_SENSOR_UOM_PARAMETER, uom);
//	}
//	
//	/**
//	 * Internal method for generating outputs.
//	 * 
//	 * @return outputs
//	 */
//	private Outputs createOutputsFromParameters() {
//		Outputs outputs = Outputs.Factory.newInstance();
//    	OutputList outputList = outputs.addNewOutputList();
//    	IoComponentPropertyType IoCompProp = outputList.addNewOutput();
//    	
//    	String obsType = ((String) parameters.get(REGISTER_SENSOR_OBSERVATION_TYPE));
//    	
//    	if (obsType.equals(REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT)){
//    		Quantity quantity = IoCompProp.addNewQuantity();
//    		quantity.setDefinition((String) parameters.get(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER));
//    		quantity.addNewUom().setCode((String) parameters.get(REGISTER_SENSOR_UOM_PARAMETER));
//    	} else if (obsType.equals(REGISTER_SENSOR_OBSERVATION_TYPE_CATEGORY)) {
//    		Category category = IoCompProp.addNewCategory();
//    		category.setDefinition((String) parameters.get(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER));
//    		category.addNewCodeSpace();
//    	}
//    	
//    	// mandatory for validation
//    	String name = parameters.get(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER);
//    	String[] nameSplit = name.split(":");
//    	IoCompProp.setName(nameSplit[nameSplit.length-1]);
//
//    	return outputs;
//	}
//	
	
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
	
	private String[] validTime;
	private ArrayList<String> keywords;
	private ArrayList<String[]> identifier, classifier; // {name, definition, value}
	private ArrayList<String[]> inputs; // {name, definition}
	private ArrayList<String[]> outputsMeasurement; // {name, definition, offeringUri, offeringName, uom }
	private ArrayList<String[]> outputsCategory; // {name, definition, offeringUri, offeringName, codespace }
	private ArrayList<String[]> componentXlink; // {name, xlink}
	private ArrayList<String[]> componentInline; // {name, description}

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
		outputsCategory = new ArrayList<String[]>();
		componentXlink = new ArrayList<String[]>();
		componentInline = new ArrayList<String[]>();
	}
	
	public void addKeyword(String keyword) {
		keywords.add(keyword);
	}
	
	public void addIdentifier(String name, String definition, String value) {
		if (entryExists(definition, identifier)) {
			removeEntry(definition, identifier);
		}
		identifier.add(new String[] { name, definition, value });
	}

	public void setIdentifierUniqeId(String value) {
		if (entryExists(UNIQUE_ID_DEF, identifier)) {
			removeEntry(UNIQUE_ID_DEF, identifier);
		}
		addIdentifier(UNIQUE_ID_NAME, UNIQUE_ID_DEF, value);
	}

	public void setIdentifierLongName(String value) {
		if (entryExists(LONG_NAME_DEF, identifier)) {
			removeEntry(LONG_NAME_DEF, identifier);
		}
		addIdentifier(LONG_NAME_NAME, LONG_NAME_DEF, value);
	}

	public void setIdentifierShortName(String value) {
		if (entryExists(SHORT_NAME_DEF, identifier)) {
			removeEntry(SHORT_NAME_DEF, identifier);
		}
		addIdentifier(SHORT_NAME_NAME, SHORT_NAME_DEF, value);
	}

	public void setIdentifierParentSystemUniqueId(String value) {
		if (entryExists(PARENT_SYSTEM_UNIQUE_ID_DEF, identifier)) {
			removeEntry(PARENT_SYSTEM_UNIQUE_ID_DEF, identifier);
		}
		addIdentifier(PARENT_SYSTEM_UNIQUE_ID_NAME, PARENT_SYSTEM_UNIQUE_ID_DEF, value);
	}
	
	public void addClassifier(String name, String definition, String value) {
		if (entryExists(definition, classifier)) {
			removeEntry(definition, classifier);
		}
		classifier.add(new String[]{name, definition, value});
	}
	
	public void setClassifierIntendedApplication(String value) {
		if (entryExists(INTENDED_APPLICATION_DEF, classifier)) {
			removeEntry(INTENDED_APPLICATION_DEF, classifier);
		}
		addClassifier(INTENDED_APPLICATION_NAME, INTENDED_APPLICATION_DEF, value);
	}
	
	public void setClassifierSensorType(String value) {
		if (entryExists(SENSOR_TYPE_DEF, classifier)) {
			removeEntry(SENSOR_TYPE_DEF, classifier);
		}
		addClassifier(SENSOR_TYPE_NAME, SENSOR_TYPE_DEF, value);
	}
	
	public void setValidTime(String begin, String end) {
		validTime[0] = begin;
		validTime[1] = end;
	}
	
	public void setCapabilityCollectingStatus(String statusName, boolean isCollecting) {
		this.statusName = statusName;
		this.isCollecting = isCollecting;
	}
	
	public void setCapabilityBbox(String lcEastingUom, double lcEastingValue,
			String lcNorthingUom, double lcNorthingValue, String ucEastingUom,
			double ucEastingValue, String ucNorthingUom, double ucNorthingValue) {
		this.lcEastingUom = lcEastingUom; // lower corner
		this.lcEastingValue = lcEastingValue;
		this.lcNorthingUom = lcNorthingUom;
		this.lcNorthingValue = lcNorthingValue;
		this.ucEastingUom = ucEastingUom; // upper corner
		this.ucEastingValue = ucEastingValue;
		this.ucNorthingUom = ucNorthingUom;
		this.ucNorthingValue = ucNorthingValue;
	}
	
	public void addFeatureOfInterest(String foiName, String foiUri) {
		this.foiName = foiName;
		this.foiUri = foiUri;
	}
	
	public void setContact(String id, String individualName,
			String organizationName, String deliveryPoint, String city,
			String postalCode, String country, String email) {
		this.id = id;
		this.individualName = individualName;
		this.organizationName = organizationName;
		this.deliveryPoint = deliveryPoint;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;
		this.email = email;
	}

	public void setPosition(String positionName, String referenceFrame,
			String vectorId, String eastingUom, double eastingValue,
			String northingUom, double northingValue, String altitudeUom,
			double altiValue) {
		this.positionName = positionName;
		this.referenceFrame = referenceFrame;
		this.vectorId = vectorId;
		this.eastingUom = eastingUom;
		this.eastingValue = eastingValue;
		this.northingUom = northingUom;
		this.northingValue = northingValue;
		this.altitudeUom = altitudeUom;
		this.altitudeValue = altiValue;
	}
	
	public void setInterface(String iName, String serviceUrl, String serviceType,
			String sensorId) {
		this.iName = iName;
		this.serviceUrl = serviceUrl;
		this.serviceType = serviceType;
		this.sensorId = sensorId;
	}
	
	public void addInput(String name, String definition) {
		inputs.add(new String[] { name, definition });
	}
	
	public void addOutputMeasurement(String name, String definition, String offeringUri, String offeringName, String uom) {
		outputsMeasurement.add(new String[] { name, definition, offeringUri, offeringName, uom});
	}
	
	public void addOutputCategory(String name, String definition, String offeringUri, String offeringName, String codespace) {
		outputsMeasurement.add(new String[] { name, definition, offeringUri, offeringName, codespace});
	}

	public void addComponentXlink(String name, String url) {
		componentXlink.add(new String[] { name, url });
	}
	
	public void addComponentInline(String name, String description) {
		componentInline.add(new String[] { name, description });
	}
	
	private boolean entryExists(String definition, ArrayList<String[]> list) {
		boolean exists = false;
		for (String[] id : list) {
			if (id[1].equals(definition)) {
				exists = true;
				break;
			}
		}
		return exists;
	}
	
	private void removeEntry(String definition, ArrayList<String[]> list) {
		for (String[] id : list) {
			if (id[1].equals(definition)) {
				list.remove(id);
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
				&& eastingUom != null && northingUom != null
				&& altitudeUom != null) {
			addPosition();
		}
		if (iName != null && serviceUrl != null && serviceType != null
				&& sensorId != null) {
			addInterface();
		}
		if (!inputs.isEmpty()) {
			addInputs();
		}
		if (!outputsMeasurement.isEmpty() || !outputsCategory.isEmpty()) {
			addOutputs();
		}
		if (!componentXlink.isEmpty() || !componentInline.isEmpty()) {
			addComponents();
		}
		
		return sysDoc;
	}
	
	private void addKeywords() {
		KeywordList keywordList = system.addNewKeywords().addNewKeywordList();
		for (String keyword : keywords) {
			keywordList.addKeyword(keyword);
		}
	}
	
	private void addIdentifier() {
		IdentifierList identifierList = system.addNewIdentification().addNewIdentifierList();
		
		Identifier forId;
		Term forTerm;
		for (String[] id : identifier) {
			forId = identifierList.addNewIdentifier();
			forId.setName(id[0]);
			forTerm = forId.addNewTerm();
			forTerm.setDefinition(id[1]);
			forTerm.setValue(id[2]);
		}
	}
	
	private void addClassifier() {
		ClassifierList classifierList = system.addNewClassification().addNewClassifierList();

		Classifier forClassifier;
		Term forTerm;
		for (String[] classifier : this.classifier) {
			forClassifier = classifierList.addNewClassifier();
			forClassifier.setName(classifier[0]);
			forTerm = forClassifier.addNewTerm();
			forTerm.setDefinition(classifier[1]);
			forTerm.setValue(classifier[2]);
		}
	}
	
	private void addValidTime() {
		TimePeriodType period = system.addNewValidTime().addNewTimePeriod();
		period.addNewBeginPosition().setStringValue(validTime[0]);
		period.addNewEndPosition().setStringValue(validTime[1]);
	}
	
	private void addCapabilities() {
		Capabilities caps = system.addNewCapabilities();
		DataRecordType dataRecordType;
	    DataComponentPropertyType f;
	    Boolean b;
	    Text t;
	    AbstractDataRecordType adrt = caps.addNewAbstractDataRecord();
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
	    DataComponentPropertyType observedBBoxField = dataRecordType.addNewField();
		observedBBoxField.setName("observedBBOX");
		AbstractDataRecordType aDRT = observedBBoxField.addNewAbstractDataRecord();
		EnvelopeType envelope = (EnvelopeType) aDRT.substitute(SWE101_ENVELOPE, EnvelopeType.type);
		envelope.setDefinition(OGC_DISCOVERY_OBSERVED_BBOX_DEFINITION);
		envelope.addNewLowerCorner().setVector(getLowerCornerOfObservedBBox());
		envelope.addNewUpperCorner().setVector(getUpperCornerOfObservedBBox());
	    
	    caps.setAbstractDataRecord(dataRecordType);
	}
	
	private VectorType getLowerCornerOfObservedBBox() {
		VectorType positionVector = VectorType.Factory.newInstance();
		Coordinate easting = positionVector.addNewCoordinate();
		easting.setName("easting");
		Quantity eastingValue = easting.addNewQuantity();
		eastingValue.setAxisID("x");
		eastingValue.addNewUom().setCode(lcEastingUom);
		eastingValue.setValue(lcEastingValue);
		
		Coordinate northing = positionVector.addNewCoordinate();
		northing.setName("northing");
		Quantity northingValue = northing.addNewQuantity();
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
		ResponsibleParty respParty = system.addNewContact().addNewResponsibleParty();
		respParty.setId(id);
		respParty.setIndividualName(individualName);
		respParty.setOrganizationName(organizationName);
		Address address = respParty.addNewContactInfo().addNewAddress();
		address.setDeliveryPointArray(new String[] {deliveryPoint});
		address.setCity(city);
		address.setPostalCode(postalCode);
		address.setCountry(country);
		address.setElectronicMailAddress(email);
	}
	
	private void addPosition() {
		Position p = system.addNewPosition();
		p.setName(positionName);
		
	    PositionType pT;
	    pT = p.addNewPosition();
	    pT.setReferenceFrame(EPSG_CODE_PREFIX + referenceFrame);	    
	    
	    VectorType vT = pT.addNewLocation().addNewVector();
	    vT.setId(vectorId);
	    
	    // Easting
	    Coordinate easting = vT.addNewCoordinate();
	    easting.setName("easting");
	    Quantity eastQuantity = easting.addNewQuantity();
	    eastQuantity.setAxisID("x");
	    UomPropertyType eastUom = eastQuantity.addNewUom();
	    eastUom.setCode(eastingUom);
	    eastQuantity.setValue(eastingValue);

	    // Northing
	    Coordinate northing = vT.addNewCoordinate();
	    northing.setName("northing");
	    Quantity northQuantity = northing.addNewQuantity();
	    northQuantity.setAxisID("y");
	    UomPropertyType northUom = northQuantity.addNewUom();
	    northUom.setCode(northingUom);
	    northQuantity.setValue(northingValue);

	    // Altitude
	    Coordinate altitude = vT.addNewCoordinate();
	    altitude.setName("altitude");
	    Quantity altiQuantity = altitude.addNewQuantity();
	    altiQuantity.setAxisID("z");
	    UomPropertyType altiUom = altiQuantity.addNewUom();
	    altiUom.setCode(altitudeUom);
	    altiQuantity.setValue(altitudeValue);
	}
	
	private void addInterface() {
		Interface  interFace = system.addNewInterfaces().addNewInterfaceList().addNewInterface();
		interFace.setName(iName);
		AbstractDataRecordType interfaceDataRecord = interFace
				.addNewInterfaceDefinition().addNewServiceLayer()
				.addNewAbstractDataRecord();
		
		DataRecordType interfaceDr = (DataRecordType) interfaceDataRecord
				.substitute(SWE101_DATARECORD, DataRecordType.type);
		interfaceDr.setDefinition(SERVICE_INTERFACE);
		
		DataComponentPropertyType serviceUrlField = interfaceDr.addNewField();
		serviceUrlField.setName(SERVICE_URL);
		serviceUrlField.addNewText().setValue(serviceUrl);
		
		DataComponentPropertyType serviceTypeField = interfaceDr.addNewField();
		serviceTypeField.setName(SERVICE_TYPE);
		serviceTypeField.addNewText().setValue(serviceType);
		
		DataComponentPropertyType serviceSpecificSensorIdField = interfaceDr.addNewField();
		serviceSpecificSensorIdField.setName(SERVICE_SPECIFIC_SENSOR_ID);
		serviceSpecificSensorIdField.addNewText().setValue(sensorId);
	}
	
	private void addInputs() {
		InputList inputList = system.addNewInputs().addNewInputList();
		IoComponentPropertyType input;
		for (String[] inputValues : inputs) {
			input = inputList.addNewInput();
			input.setName(inputValues[0]);
			input.addNewObservableProperty().setDefinition(inputValues[1]);
		}
	}

	private void addOutputs() {
		OutputList outputList = system.addNewOutputs().addNewOutputList();
		IoComponentPropertyType output;

		for (String[] outputValues : outputsMeasurement) { // sos 1.0 52n implementation
			output = outputList.addNewOutput();
			output.setName(outputValues[0]);
			Quantity quantity = output.addNewQuantity();
			quantity.setDefinition(outputValues[1]);
			addOfferingMetadata(quantity.addNewMetaDataProperty(), outputValues[2], outputValues[3]);
			quantity.addNewUom().setCode(outputValues[4]);
		}
		
		for (String[] outputValues : outputsCategory) { // sos 1.0 52n implementation
			output = outputList.addNewOutput();
			output.setName(outputValues[0]);
			Category category = output.addNewCategory();
			category.setDefinition(outputValues[1]);
			addOfferingMetadata(category.addNewMetaDataProperty(), outputValues[2], outputValues[3]);
			category.addNewCodeSpace();
		}
	}
	
	private void addOfferingMetadata(MetaDataPropertyType addNewMetaDataProperty, String uri, String name) {
		XmlCursor c = addNewMetaDataProperty.newCursor();
		c.toNextToken();
		c.beginElement(SWE10_OFFERING);
		c.insertElementWithText(SWE10_ID, uri);
		c.insertElementWithText(SWE10_NAME, name);
		c.dispose();
	}
	
	private void addComponents() {
		ComponentList compList = system.addNewComponents().addNewComponentList();
		// add xlink components
		Component comp;
		for (String[] compValues : componentXlink) {
			comp = compList.addNewComponent();
			comp.setName(compValues[0]);
			comp.setHref(compValues[1]);
		}
		
		// add inline components
		for (String[] compValues : componentInline) {
			comp = compList.addNewComponent();
			comp.setName(compValues[0]);
			// TODO set inline description
			// AbstractComponentType inlineComp = (AbstractComponentType) comp.substitute(SWE101_COMPONENT, ComponentType.type);
		}
	}
}