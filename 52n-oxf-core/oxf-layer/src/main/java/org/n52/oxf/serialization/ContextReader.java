package org.n52.oxf.serialization;

import java.io.BufferedReader;

import org.n52.oxf.OXFException;
import org.n52.oxf.context.LayerContext;

public interface ContextReader {

    public LayerContext readFrom(BufferedReader reader) throws OXFException;

}
