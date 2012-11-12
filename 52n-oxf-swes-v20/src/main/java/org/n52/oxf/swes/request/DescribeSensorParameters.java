
package org.n52.oxf.swes.request;

import org.n52.ows.request.RequestParameters;

/**
 * Assembles all parameters needed for a DescribeSensor request. 
 */
public class DescribeSensorParameters extends RequestParameters {

    public static String DESCRIBE_SENSOR_PROCEDURE_PARAMETER = "procedure";
    public static String DESCRIBE_SENSOR_OUTPUT_FORMAT = "outputFormat";
    public static final String OUTPUT_FORMAT_SENSORML = "text/xml;subtype=\"sensorML/1.0.1\"";

    /**
     * Creates DescribeSensor request parameters. It uses {@link #OUTPUT_FORMAT_SENSORML} for
     * the required OutputFormat parameter by default.
     * 
     * @param procedure
     *        the procedure which sensor description shall requested.
     */
    public DescribeSensorParameters(String procedure) {
        this(procedure, OUTPUT_FORMAT_SENSORML);
    }

    /**
     * Creates DescribeSensor request parameters.
     * 
     * @param procedure
     *        the procedure which sensor description shall requested.
     * @param outputFormat
     *        the output format of the sensor description.
     */
    public DescribeSensorParameters(String procedure, String outputFormat) {
        putNonEmpty(DESCRIBE_SENSOR_PROCEDURE_PARAMETER, procedure);
        putNonEmpty(DESCRIBE_SENSOR_OUTPUT_FORMAT, outputFormat);
    }

    /**
     * Overrides required Procedure parameter.
     * 
     * @param procedureId
     *        the procedure which sensor description shall requested.
     */
    public void setProcedureId(String procedureId) {
        putNonEmpty(DESCRIBE_SENSOR_PROCEDURE_PARAMETER, procedureId);
    }

    /**
     * Overrides required OutputFormat parameter.
     * 
     * @param outputFormat
     *        the output format of the sensor description.
     */
    public void setOutputFormat(String outputFormat) {
        putNonEmpty(DESCRIBE_SENSOR_OUTPUT_FORMAT, outputFormat);
    }

    @Override
    public boolean isValid() {
        boolean invalidProcedureValue = isEmptyValue(DESCRIBE_SENSOR_PROCEDURE_PARAMETER);
        boolean invalidOutputFormatValue = isEmptyValue(DESCRIBE_SENSOR_OUTPUT_FORMAT);
        return ! (invalidProcedureValue || invalidOutputFormatValue);
    }

}
