
package org.n52.ows.request;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents an assembly of request parameters (key-value pairs). Multiple parameter values are allowed, i.e.
 * one key can be associated to one or more parameter values. <code>null</code> is an allowed value but
 * discouraged to use as the semantics of a null reference are unclear!
 */
public interface RequestParameters {

    /**
     * Performs a check if this parameter assembly is valid according to a defined schema.
     * 
     * @return <code>true</code> if the assembly is valie, <code>false</code> otherwise.
     */
    public boolean isValid();

    /**
     * @return <code>true</code> if there are no key-value pairs available, <code>false</code> if the assembly
     *         contains at least one key-value pair.
     */
    public boolean isEmpty();

    /**
     * Checks if the given parameter is present in the assembly.
     * 
     * @param parameter
     *        the parameter name to check.
     * @return <code>true</code> if the passed parameter has one or more associated value(s),
     *         <code>false</code> if parameter is not present.
     */
    public boolean contains(String parameter);

    /**
     * Checks if the assembly contains the given value. It does not play a role to which parameter the value
     * may associated with.
     * 
     * @param value
     *        the value to check.
     * @return <code>true</code> if the passed value is available. <code>false</code> if no parameter is
     *         associated with the given value.
     */
    public boolean containsValue(String value);

    /**
     * Checks if the assembly has exact one parameter value associated to the given key. If the value is not
     * present at all the method will return <code>false</code>. This method is not threadsafe, so do not
     * expect it as a reliable check; other threads may have added new values in the meantime.
     * 
     * @param parameter
     *        the parameter name to check.
     * @return <code>true</code> if there is exact one value associated with the given parameter,
     *         <code>false</code> if there are more than one parameter value, or the parameter is not present.
     */
    public boolean isSingleValue(String parameter);

    /**
     * Checks if the assembly has more than one parameter value associated to the given key. If the value is
     * not present at all the method will return <code>false</code>. This method is not threadsafe, so do not
     * expect it as a reliable check; other threads may have added new values in the meantime.
     * 
     * @param parameter
     *        the parameter name to check.
     * @return <code>true</code> if more than one value is associated with the given parameter,
     *         <code>false</code> if there is only one parameter value, or the parameter is not present.
     */
    public boolean hasMultipleValues(String parameter);

    /**
     * Gets the first parameter value for the given parameter. It is not guaranteed that, if multiple
     * parameters are associated with the given parameter, always the same value is returned. It is meant as
     * convenient method for parameters which have a cardinality of one, or it is clear that only one
     * parameter value is associated with the parameter.<br>
     * <br>
     * <b>Note:</b> If the given parameter is not contained by the assembly <code>null</code> will be
     * returned! This would have the same semantics as when a parameter key exists but a <code>null</code>
     * value is associated with it. Ensure if the parameter exists beforehand to be clear about the meaning of
     * <code>null</code>:
     * 
     * <pre>
     * {@code 
     *      if (parameters.contains("key")) {
     *          String nullableParameterValue = parameters.getSingleValue("key");
     *      } else {
     *          // parameter not contained by the assembly
     *      }
     * }
     * </pre>
     * 
     * @param parameter
     *        the parameter name to check.
     * @return the first parameter value associated to the given parameter.
     * @see RequestParameters#contains(String) to ensure if the parameter is contained by assembly.
     */
    public String getSingleValue(String parameter);

    /**
     * Returns all parameter keys available in this parameter assembly wrapped via
     * {@link Collections#unmodifiableCollection(Collection)}.
     * 
     * @return a read-only collection to all parameter keys added to this assembly.
     */
    public Collection<String> getAvailableKeys();

    /**
     * Get read-only access to all parameter values associated with the given parameter. If the parameter is
     * not present in the assembly, the returned {@link Iterable} is one from an emtpy {@link Collection}.
     * 
     * @param parameter
     *        the parameter to check.
     * @return read-only access on the parameter values associated with the given parameter.
     */
    public Iterable<String> getAllValues(String parameter);

    /**
     * Merge the given parameter assembly with this instance.
     * 
     * @param parameters
     *        the parameter assembly to merge with.
     * @return <code>true</code> if the assembly has changed, <code>false</code> otherwise.
     */
    public boolean mergeWith(RequestParameters parameters);

    /**
     * Adds a new parameter value to the given parameter. A parameter can have multiple parameter values
     * associated with it. No replacement takes place when adding different values to exactly the same
     * parameter key. If you want to remove a particular parameter value use {@link #remove(String, String)}.
     * 
     * @param parameter
     *        the parameter key.
     * @param value
     *        the value to associate the with parameter.
     * @return <code>true</code> if the assembly has changed (size has increased).
     */
    public boolean addParameterValue(String parameter, String value);

    /**
     * Adds a bulk of parameter values to the given parameter. No replacements take place. If you want to
     * remove a particular parameter value use {@link #remove(String, String)}.
     * 
     * @param parameter
     *        the parameter key.
     * @param values
     *        the values to associate with the parameter.
     * @return <code>true</code> if the assembly has changed (size has increased).
     */
    public boolean addBulkParameterValues(String parameter, String... values);

    /**
     * Adds a bulk of parameter values to the given parameter. No replacements take place. If you want to
     * remove a particular parameter value use {@link #remove(String, String)}.
     * 
     * @param parameter
     *        the parameter key.
     * @param values
     *        the values to associate with the parameter.
     * @return <code>true</code> if the assembly has changed (size has increased).
     */
    public boolean addBulkParameterValues(String parameter, Iterable<String> values);

    /**
     * Removes a particular parameter value for the given parameter.
     * 
     * @param parameter
     *        the parameter to remove the value from.
     * @param value
     *        the value to remove.
     * @return <code>true</code> if the assembly has changed, <code>false</code> otherwise.
     */
    public boolean remove(String parameter, String value);

    /**
     * Removes all parameter values associated with the given parameter.
     * 
     * @param parameter
     *        the parameter to remove all values from.
     * @return all parameters which were associated with the given parameter. Modifying the returned
     *         collection will not have any effect on the parameter assembly.
     */
    public Collection<String> removeValues(String parameter);

    /**
     * Performs a complete clean of this assembly instance.
     */
    public void removeAll();

}
