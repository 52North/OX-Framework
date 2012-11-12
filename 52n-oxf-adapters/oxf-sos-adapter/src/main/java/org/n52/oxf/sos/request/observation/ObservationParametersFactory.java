
package org.n52.oxf.sos.request.observation;

import javax.xml.namespace.QName;

public interface ObservationParametersFactory {

    /**
     * Creates an observation parameter assembly for well known observation types (as Count, or Measurement).
     * If given observation type does not match, {@link #createExtendedObservationFor(QName)} is called as a
     * fallback to let implementation specific factories create custom observation types.
     * 
     * @param type
     *        the observation type to create parameter assembly for.
     * @return an observation parameters assembly to create InsertObservation request.
     */
    public ObservationParameters createObservationParametersFor(QName type);

    /**
     * A fallback which is called when no observation type matches in
     * {@link #createObservationParametersFor(QName)}. Can be overridden by implementors who wants to extend
     * already known observation types with custom types.
     * 
     * @param type
     *        which extended Observation type to create.
     * @return an observation parameters assembly to create InsertObservation request.
     */
    public ObservationParameters createExtendedObservationFor(QName type);
}
