package org.n52.oxf.swes.request.builder;

import org.n52.ows.request.ParameterMap;

/**
 * Parameter container 
 */
public class DescribeSensorParameterMap extends ParameterMap {
    
    public static String DESCRIBE_SENSOR_VERSION_PARAMETER = "version";
    public static String DESCRIBE_SENSOR_PROCEDURE_PARAMETER = "procedure";
    public static String DESCRIBE_SENSOR_OUTPUT_FORMAT = "outputFormat";
    public static final String OUTPUT_FORMAT_SENSORML = "text/xml;subtype=\"sensorML/1.0.1\"";
    
    /**
     * Assembles mandatory parameters from method parameter list.
     * 
     * @param sensorId
     * @param outputFormat
     */
    public DescribeSensorParameterMap(String sensorId, String outputFormat) {
        if (sensorId == null || outputFormat == null) {
            throw new NullPointerException("The parameters \"sensorId\" and \"outputFormat\" are mandatory. They cannot be left empty!");
        }
        parameters.put(DESCRIBE_SENSOR_PROCEDURE_PARAMETER, sensorId);
        parameters.put(DESCRIBE_SENSOR_OUTPUT_FORMAT, outputFormat);
    }
    
    public void setProcedureId(String procedureId) {
        parameters.put(DESCRIBE_SENSOR_PROCEDURE_PARAMETER, procedureId);
    }
    
    public void setOutputFormat(String outputFormat) {
        parameters.put(DESCRIBE_SENSOR_OUTPUT_FORMAT, outputFormat);
    }

    @Override
    public boolean isValid() {
        boolean hasValidProcedure = checkRequiredParameter(DESCRIBE_SENSOR_PROCEDURE_PARAMETER);
        boolean hasValidOutputFormat = checkRequiredParameter(DESCRIBE_SENSOR_OUTPUT_FORMAT);
        return hasValidProcedure && hasValidOutputFormat;
    }

}
