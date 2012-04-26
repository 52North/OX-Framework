package org.n52.ows.exception;

public enum OwsExceptionCode {

    OPERATION_NOT_SUPPORTED("OperationNotSupported"),
    MISSING_PARAMETER_VALUE("MissingParameterValue"),
    INVALID_PARAMETER_VALUE("InvalidParamterValue"),
    VERSION_NEGOTIATION_FAILED("VersionNegotiationFailed"),
    INVALID_UPDATE_SEQUENCE("InvalidUpdateSequence"),
    OPTION_NOT_SUPPORTED("OptionNotSupported"),
    NO_APPLICABLE_CODE("NoApplicableCode");
    
    private String exceptionCode;
    
    private OwsExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    
}
