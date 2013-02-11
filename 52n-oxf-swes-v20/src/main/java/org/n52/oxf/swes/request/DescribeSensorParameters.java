
package org.n52.oxf.swes.request;

import org.n52.oxf.request.MultimapRequestParameters;

/**
 * Assembles all parameters needed for a DescribeSensor request. 
 */
public class DescribeSensorParameters extends MultimapRequestParameters {

    private final String REQUEST_PARAMETER = "request";
    
    static final String PROCEDURE_PARAMETER = "procedure";
    
    static final String OUTPUT_FORMAT_PARAMETER = "outputFormat";
    
    static final String OUTPUT_FORMAT_SENSORML = "text/xml;subtype=\"sensorML/1.0.1\"";

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
        addNonEmpty(REQUEST_PARAMETER, "DescribeSensor");
        addNonEmpty(PROCEDURE_PARAMETER, procedure);
        addNonEmpty(OUTPUT_FORMAT_PARAMETER, outputFormat);
    }

    /**
     * Overrides required Procedure parameter.
     * 
     * @param procedureId
     *        the procedure which sensor description shall requested.
     */
    public void setProcedureId(String procedureId) {
        addNonEmpty(PROCEDURE_PARAMETER, procedureId);
    }

    /**
     * Overrides required OutputFormat parameter.
     * 
     * @param outputFormat
     *        the output format of the sensor description.
     */
    public void setOutputFormat(String outputFormat) {
        addNonEmpty(OUTPUT_FORMAT_PARAMETER, outputFormat);
    }

    public boolean isValid() {
        boolean invalidProcedureValue = isEmptyValue(PROCEDURE_PARAMETER);
        boolean invalidOutputFormatValue = isEmptyValue(OUTPUT_FORMAT_PARAMETER);
        return ! (invalidProcedureValue || invalidOutputFormatValue);
    }

}
