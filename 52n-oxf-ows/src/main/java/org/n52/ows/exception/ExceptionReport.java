package org.n52.ows.exception;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class ExceptionReport {

    private List<OwsException> exceptionList = new ArrayList<OwsException>();
    
    public ExceptionReport(OwsException exception) {
        this.addOwsException(exception);
    }
    
    public void addOwsException(OwsException exception) {
        this.exceptionList.add(exception);
    }
    
    public void addAllOwsExceptions(Collection<OwsException> exceptions) {
        this.exceptionList.addAll(exceptions);
    }
    
}
