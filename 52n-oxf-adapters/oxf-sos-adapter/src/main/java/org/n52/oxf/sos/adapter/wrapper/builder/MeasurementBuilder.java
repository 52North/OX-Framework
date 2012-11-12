package org.n52.oxf.sos.adapter.wrapper.builder;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_TYPE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_MEASUREMENT;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE;

import org.n52.oxf.sos.request.observation.MeasurementObservationParameters;
import org.n52.oxf.sos.request.observation.ObservationParametersFactory;
import org.n52.oxf.xml.XMLConstants;

/**
 * MeasurementObservationParameters assembles parameters, which are necessary to describe a measurement.
 * 
 * @author Eric
 * @deprecated use {@link ObservationParametersFactory} or {@link MeasurementObservationParameters}
 */
@Deprecated
public class MeasurementBuilder extends ObservationBuilder {
			
		/**
		 * Type specific constructor for measurements. It adds the type as parameter to the list.
		 */
		public MeasurementBuilder() {
			type = XMLConstants.QNAME_OM_1_0_MEASUREMENT_OBSERVATION;
			parameters.put(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_MEASUREMENT);
		}
		
		/**
		 * Adds or replaces the value for the parameter "uom" to the parameter list.
		 * 
		 * @param uom
		 */
		public void addUom(String uom) {
			if (parameters.get(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE) != null) {
				parameters.remove(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE);
			}
			parameters.put(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE, uom);
		}
		
		public void addObservationValue(String observationValue) {
			if (parameters.get(INSERT_OBSERVATION_VALUE_PARAMETER) != null) {
				parameters.remove(INSERT_OBSERVATION_VALUE_PARAMETER);
			}
			parameters.put(INSERT_OBSERVATION_VALUE_PARAMETER, observationValue);
		}

}