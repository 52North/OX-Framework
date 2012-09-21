package org.n52.oxf.sos.adapter.wrapper.builder;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.n52.oxf.OXFException;
import org.n52.oxf.xml.XMLConstants;

import net.opengis.gml.MeasureType;
import net.opengis.om.x10.CategoryObservationType;
import net.opengis.om.x10.MeasurementType;
import net.opengis.om.x10.ObservationType;
import net.opengis.sos.x10.ObservationTemplateDocument.ObservationTemplate;
import net.opengis.swe.x10.ScopedNameType;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

/**
 * Template generating class. Everything needed for generating the template is
 * the type and additional type specific parameters
 * 
 * @author Eric
 */
public class ObservationTemplateBuilder {

	private Map<String, String> parameters = new HashMap<String, String>();
	
	private QName observationType;
	
	/**
	 * Hidden public constructor.
	 */
	private ObservationTemplateBuilder() {
	}
	
	/**
	 * Type specific template builder generator for measurements.
	 * 
	 * @param uom unit of measurement
	 * @return instance of measurement template builder 
	 */
	public static ObservationTemplateBuilder createObservationTemplateBuilderForTypeMeasurement(String uom) {
		ObservationTemplateBuilder builder = new ObservationTemplateBuilder();
		builder.observationType = XMLConstants.QNAME_OM_1_0_MEASUREMENT;
		builder.parameters.put(REGISTER_SENSOR_CODESPACE_PARAMETER, uom);
		return builder;
	}
	
	/**
	 * Type specific template builder generator for category observations.
	 * 
	 * @param codeSpace
	 * @return instance of category observation template builder
	 */
	public static ObservationTemplateBuilder createObservationTemplateBuilderForTypeCategory(String codeSpace) {
		ObservationTemplateBuilder builder = new ObservationTemplateBuilder();
		builder.observationType = XMLConstants.QNAME_OM_1_0_CATEGORY_OBSERVATION;
		builder.parameters.put(REGISTER_SENSOR_UOM_PARAMETER, codeSpace);
		return builder;
	}
	
	public void setDefaultValue(String defaultValue) {
		parameters.put(REGISTER_SENSOR_DEFAULT_RESULT_VALUE, defaultValue);
	}
	
	/**
	 * Generator core of the template.
	 * 
	 * @return type specific observation template
	 * @throws OXFException
	 */
	public String generateObservationTemplate() throws OXFException {
		ObservationTemplate obsTemp = ObservationTemplate.Factory.newInstance();
		ObservationType ot = obsTemp.addNewObservation();
		ot.addNewSamplingTime();
		ot.addNewProcedure();
		ot.addNewObservedProperty();
		ot.addNewFeatureOfInterest();
		
		if (observationType.equals(XMLConstants.QNAME_OM_1_0_CATEGORY_OBSERVATION)){
			ScopedNameType snt = ScopedNameType.Factory.newInstance();
			snt.setCodeSpace(parameters.get(REGISTER_SENSOR_CODESPACE_PARAMETER));
			ot = (ObservationType) ot.substitute(XMLConstants.QNAME_OM_1_0_CATEGORY_OBSERVATION, CategoryObservationType.type);
			ot.addNewResult().set(snt);
		} else if (observationType.equals(XMLConstants.QNAME_OM_1_0_MEASUREMENT)){
			MeasureType mt2 = MeasureType.Factory.newInstance();
			double defaultValue = Double.valueOf(parameters.get(REGISTER_SENSOR_DEFAULT_RESULT_VALUE));
			mt2.setDoubleValue(defaultValue); // default value required by our SOS
			mt2.setUom(parameters.get(REGISTER_SENSOR_UOM_PARAMETER));
			ot = (ObservationType) ot.substitute(XMLConstants.QNAME_OM_1_0_MEASUREMENT, MeasurementType.type);
			ot.addNewResult().set(mt2);
		} else{
			throw new OXFException("Observation type '" + observationType + "' not supported.");
		}
		
		
		return obsTemp.toString();
	}
	
}