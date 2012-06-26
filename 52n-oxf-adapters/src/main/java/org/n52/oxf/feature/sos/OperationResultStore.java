package org.n52.oxf.feature.sos;

import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.xmlbeans.parser.XMLBeansParser;
import org.n52.oxf.xmlbeans.parser.XMLHandlingException;

public abstract class OperationResultStore {
    
    protected XmlObject xmlObject;
    protected String version;
    
    /**
     * @deprecated Use argument constructor {@link OperationResultStore#OperationResultStore(OperationResult)} with {@link #unmarshalFeatures(OperationResult)}
     */
    protected OperationResultStore() {
        // for backward compatibility .. TODO remove when deprecated contructor is going to be removed
    }
    
    protected OperationResultStore(OperationResult operationResult) throws OXFException {
        try {
            this.xmlObject = XMLBeansParser.parse(operationResult.getIncomingResultAsStream());
            this.version = getVersion(operationResult);
        }
        catch (XMLHandlingException e) {
            throw new OXFException("Could not parse OperationResult.", e);
        }
    }

    protected String getVersion(OperationResult operationResult) {
        ParameterContainer parameters = operationResult.getUsedParameters();
        ParameterShell shell = parameters.getParameterShellWithCommonName(Parameter.COMMON_NAME_VERSION);
        return (String) shell.getSpecifiedValue();
    }

}
