
package org.n52.oxf.sos.request.v100;

import org.n52.ows.request.RequestParameters;

/**
 * Assembles all parameters needed for a RegisterSensor request. This request is SOS 1.0.0 specific. 
 */
public class RegisterSensorParameters extends RequestParameters {

    public static String REGISTER_SENSOR_ML_DOC_PARAMETER = "sensorMLDoc";
    public static String REGISTER_SENSOR_OBSERVATION_TEMPLATE = "observationTemplate";
    public static String REGISTER_SENSOR_OBSERVATION_TYPE_CATEGORY = "category";
    public static String REGISTER_SENSOR_OBSERVATION_TYPE = "type";

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
        putNonEmpty(REGISTER_SENSOR_ML_DOC_PARAMETER, sensorDescription);
        putNonEmpty(REGISTER_SENSOR_OBSERVATION_TEMPLATE, observationTemplate);
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
        putNonEmpty(REGISTER_SENSOR_ML_DOC_PARAMETER, sensorDescription);
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
        putNonEmpty(REGISTER_SENSOR_OBSERVATION_TEMPLATE, observationTemplate);
    }

    @Override
    public boolean isValid() {
        boolean invalidSmlParameterValue = isEmptyValue(REGISTER_SENSOR_ML_DOC_PARAMETER);
        boolean invalidTemplateValue = isEmptyValue(REGISTER_SENSOR_OBSERVATION_TEMPLATE);
        return ! (invalidSmlParameterValue || invalidTemplateValue);
    }

}