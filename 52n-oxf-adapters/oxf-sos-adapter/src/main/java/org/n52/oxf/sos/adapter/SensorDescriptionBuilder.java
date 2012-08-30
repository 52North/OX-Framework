package org.n52.oxf.sos.adapter;

import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlCursor;

import net.opengis.sensorML.x101.IoComponentPropertyType;
import net.opengis.sensorML.x101.OutputsDocument.Outputs;
import net.opengis.sensorML.x101.OutputsDocument.Outputs.OutputList;
import net.opengis.sensorML.x101.PositionDocument.Position;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sensorML.x101.SystemDocument;
import net.opengis.sensorML.x101.SystemType;
import net.opengis.swe.x101.CategoryDocument.Category;
import net.opengis.swe.x101.PositionType;
import net.opengis.swe.x101.QuantityDocument.Quantity;
import net.opengis.swe.x101.VectorPropertyType;
import net.opengis.swe.x101.VectorType;
import net.opengis.swe.x101.VectorType.Coordinate;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

/**
 * Sensor description generating class. All data that describes the sensor is
 * assembled in this class. The output is a ready to use SensorML document,
 * which will be directly passed to the request. To add further sensor
 * describing information just add certain methods in this class.
 * 
 * @author Eric
 */
public class SensorDescriptionBuilder {
	
	private SystemDocument systemDocument = SystemDocument.Factory.newInstance();
	private SystemType system = SystemType.Factory.newInstance();
	
	private Map<String, String> parameters = new HashMap<String, String>();
	
	/**
	 * Creates an sensor describing document builder.
	 * 
	 * @param sensorId
	 * @param sensorObservationType
	 */
	public SensorDescriptionBuilder(String sensorId, String sensorObservationType) {
		system.setId(sensorId);
		parameters.put(REGISTER_SENSOR_OBSERVATION_TYPE, sensorObservationType);
	}
	
	/**
	 * Return, if all necessary parameters are set, a ready to use SensorML document.
	 * 
	 * @return ready to use SensorML document
	 */
	public String generateSensorDescription() {
    	system.setOutputs(createOutputsFromParameters());
    	systemDocument.addNewSystem().set(system);    	
    	SensorMLDocument sensorMLDocument = SensorMLDocument.Factory.newInstance();
    	sensorMLDocument.addNewSensorML().addNewMember().set(systemDocument);
    	
    	// Get SensorML version by checking the class of SystemDocument - FROM OLD VERSION!!!
    	if (systemDocument instanceof net.opengis.sensorML.x101.SystemDocument) {
    		XmlCursor c = sensorMLDocument.getSensorML().newCursor();
    		c.toNextToken();
    		c.insertAttributeWithValue("version","1.0.1");
    		c.dispose();
    	}
    	
		return sensorMLDocument.toString();
	}
	
	/**
	 * Sets the position describing parameters.
	 * 
	 * @param name
	 * @param positionFixed
	 * @param latitude
	 * @param longitude
	 */
	public void setPosition(String name, boolean positionFixed, double latitude, double longitude) {
		Position pos = Position.Factory.newInstance();
    	pos.setName(name);
    	PositionType posType = pos.addNewPosition();
    	posType.setFixed(new Boolean(positionFixed));
    	posType.setReferenceFrame("urn:ogc:crs:epsg:4326");
    	VectorPropertyType vecPropType =  posType.addNewLocation();
    	VectorType vecType = vecPropType.addNewVector();
    	Coordinate cordLatitude = vecType.addNewCoordinate();
    	cordLatitude.setName("latitude");
    	cordLatitude.addNewQuantity().setValue(new Double (latitude));
    	Coordinate cordLongitude = vecType.addNewCoordinate();
    	cordLongitude.setName("longitude");
    	cordLongitude.addNewQuantity().setValue(new Double (longitude));
    	system.setPosition(pos);
	}
	
	/**
	 * sets the observed property parameter.
	 * 
	 * @param observedProperty
	 */
	public void setObservedProperty(String observedProperty) {
		parameters.put(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER, observedProperty);
	}
	
	/**
	 * Sets the unit of measurement paramter.
	 * 
	 * @param uom
	 */
	public void setUnitOfMeasurement(String uom) {
		parameters.put(REGISTER_SENSOR_UOM_PARAMETER, uom);
	}
	
	/**
	 * Internal method for generating outputs.
	 * 
	 * @return outputs
	 */
	private Outputs createOutputsFromParameters() {
		Outputs outputs = Outputs.Factory.newInstance();
    	OutputList outputList = outputs.addNewOutputList();
    	IoComponentPropertyType IoCompProp = outputList.addNewOutput();
    	
    	String obsType = ((String) parameters.get(REGISTER_SENSOR_OBSERVATION_TYPE));
    	
    	if (obsType.equals(REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT)){
    		Quantity quantity = IoCompProp.addNewQuantity();
    		quantity.setDefinition((String) parameters.get(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER));
    		quantity.addNewUom().setCode((String) parameters.get(REGISTER_SENSOR_UOM_PARAMETER));
    	} else if (obsType.equals(REGISTER_SENSOR_OBSERVATION_TYPE_CATEGORY)) {
    		Category category = IoCompProp.addNewCategory();
    		category.setDefinition((String) parameters.get(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER));
    		category.addNewCodeSpace();
    	}
    	
    	// mandatory for validation
    	String name = parameters.get(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER);
    	String[] nameSplit = name.split(":");
    	IoCompProp.setName(nameSplit[nameSplit.length-1]);

    	return outputs;
	}
}