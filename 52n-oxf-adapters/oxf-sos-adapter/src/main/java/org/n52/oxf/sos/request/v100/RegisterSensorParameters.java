
package org.n52.oxf.sos.request.v100;

import org.n52.oxf.request.MultimapRequestParameters;

/**
 * Assembles all parameters needed for a RegisterSensor request. This request is SOS 1.0.0 specific. 
 */
public class RegisterSensorParameters extends MultimapRequestParameters {
    
    private static final String REQUEST_PARAMETER = "request";

    static final String REGISTER_SENSOR_ML_DOC_PARAMETER = "sensorMLDoc";
    
    static final String REGISTER_SENSOR_OBSERVATION_TEMPLATE = "observationTemplate";
    
    static final String REGISTER_SENSOR_OBSERVATION_TYPE_CATEGORY = "category";
    
    static final String REGISTER_SENSOR_OBSERVATION_TYPE = "type";

    /**
     * Creates RegisterSensor parameters.
     * 
     * @param sensorDescription
     *        the sensor's description document.
     * @param observationTemplate
     *        the sensor's observation template.
     * @throws IllegalArgumentException
     *         if passed arguments are <code>null</code> or empty.
     */
    public RegisterSensorParameters(String sensorDescription, String observationTemplate) {
        addNonEmpty(REQUEST_PARAMETER, "RegisterSensor");
        addNonEmpty(REGISTER_SENSOR_ML_DOC_PARAMETER, sensorDescription);
        addNonEmpty(REGISTER_SENSOR_OBSERVATION_TEMPLATE, observationTemplate);
    }

    /**
     * Overrides required sensorDescription parameter.
     * 
     * @param sensorDescription
     *        the new sensorDescription.
     * @throws IllegalArgumentException
     *         if passed arguments are <code>null</code> or empty.
     */
    public void setSensorDescription(String sensorDescription) {
        addNonEmpty(REGISTER_SENSOR_ML_DOC_PARAMETER, sensorDescription);
    }

    /**
     * Overrides required observationTemplate parameter.
     * 
     * @param observationTemplate
     *        the new observationTemplate.
     * @throws IllegalArgumentException
     *         if passed arguments are <code>null</code> or empty.
     */
    public void setObservationTemplate(String observationTemplate) {
        addNonEmpty(REGISTER_SENSOR_OBSERVATION_TEMPLATE, observationTemplate);
    }

    public boolean isValid() {
        boolean invalidSmlParameterValue = isEmptyValue(REGISTER_SENSOR_ML_DOC_PARAMETER);
        boolean invalidTemplateValue = isEmptyValue(REGISTER_SENSOR_OBSERVATION_TEMPLATE);
        return ! (invalidSmlParameterValue || invalidTemplateValue);
    }

}