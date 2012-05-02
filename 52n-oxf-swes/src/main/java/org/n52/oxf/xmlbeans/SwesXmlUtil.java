package org.n52.oxf.xmlbeans;

import java.util.Collection;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.swes.InvalidRequestException;
import org.n52.oxf.xmlbeans.parser.XMLBeansParser;

public class SwesXmlUtil {

    /**
     * @param request the request to validate
     * @throws InvalidRequestException if request is invalid. Contains validation errors as locator.
     */
    public static void validateSwesRequest(XmlObject request) throws InvalidRequestException {
        Collection<XmlError> validationErrors = XMLBeansParser.validate(request);
        if (!validationErrors.isEmpty()) {
            String locator = createValidationMessageLocator(validationErrors);
            throw new InvalidRequestException(locator);
        }
    }

    private static String createValidationMessageLocator(Collection<XmlError> validationErrors) {
        StringBuilder errorBuilder = new StringBuilder();
        for (XmlError xmlError : validationErrors) {
            errorBuilder.append(xmlError.getMessage()).append(";");
        }
        errorBuilder.deleteCharAt(errorBuilder.length() - 1);
        return errorBuilder.toString();
    }
}
