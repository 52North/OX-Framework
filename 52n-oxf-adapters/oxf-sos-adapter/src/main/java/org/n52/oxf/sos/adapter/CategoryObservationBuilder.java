package org.n52.oxf.sos.adapter;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import org.n52.oxf.xml.XMLConstants;

/**
 * CategoryObservationBuilder assembles parameters, which are necessary to describe a category observation.
 * 
 * @author Eric
 */
public class CategoryObservationBuilder extends ObservationBuilder {

		/**
		 * Type specific constructor for category observations. It adds the type as parameter to the list.
		 */
		public CategoryObservationBuilder() {
			type = XMLConstants.QNAME_OM_1_0_CATEGORY_OBSERVATION;
			parameters.put(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_CATEGORY);
		}
		
		/**
		 * Adds or replaces the value for the parameter "resultCodespace" to the parameter list.
		 * 
		 * @param resultCodespace
		 */
		public void addResultCodespace(String resultCodespace) {
			if (parameters.get(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE) != null) {
				parameters.remove(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE);
			}
			parameters.put(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE, resultCodespace);
		}
}