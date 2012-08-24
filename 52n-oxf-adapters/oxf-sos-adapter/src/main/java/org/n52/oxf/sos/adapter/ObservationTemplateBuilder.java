package org.n52.oxf.sos.adapter;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.xml.XMLConstants;

import net.opengis.gml.MeasureType;
import net.opengis.om.x10.CategoryObservationType;
import net.opengis.om.x10.MeasurementType;
import net.opengis.om.x10.ObservationType;
import net.opengis.sos.x10.ObservationTemplateDocument.ObservationTemplate;
import net.opengis.swe.x10.ScopedNameType;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

public class ObservationTemplateBuilder {

	private Map<String, String> parameters = new HashMap<String, String>();
	
	private QName observationType;
	
	public ObservationTemplateBuilder(QName observationType) {
		this.observationType = observationType;
	}
	
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
			mt2.setDoubleValue(0.0); // default value required by our SOS
			mt2.setUom(parameters.get(REGISTER_SENSOR_UOM_PARAMETER));
			ot = (ObservationType) ot.substitute(XMLConstants.QNAME_OM_1_0_MEASUREMENT, MeasurementType.type);
			ot.addNewResult().set(mt2);
		} else{
			throw new OXFException("Observation type '" + observationType + "' not supported.");
		}
		
		return obsTemp.toString();
	}
	
	public void addCategoryObservationCodeSpace(String codespace) {
		if (parameters.get(REGISTER_SENSOR_CODESPACE_PARAMETER) != null) {
			parameters.remove(REGISTER_SENSOR_CODESPACE_PARAMETER);
		}
		parameters.put(REGISTER_SENSOR_CODESPACE_PARAMETER, codespace);
	}
	
	public void addMeasurementUom(String uom) {
		if (parameters.get(REGISTER_SENSOR_UOM_PARAMETER) != null) {
			parameters.remove(REGISTER_SENSOR_UOM_PARAMETER);
		}
		parameters.put(REGISTER_SENSOR_UOM_PARAMETER, uom);
	}
	
}