package org.n52.oxf.serialization;

import org.n52.oxf.context.ContextCollection;
import org.n52.oxf.context.LayerContext;

public interface ContextWriter {

    public void saveContextFile(String filePath);
    
    public void write(LayerContext layerContext);

    public void write(ContextCollection contextCollection);

}

