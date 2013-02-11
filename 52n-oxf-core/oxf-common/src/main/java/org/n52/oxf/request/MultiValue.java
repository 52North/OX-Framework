
package org.n52.oxf.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <b>This class is test only yet!</b>
 */
public class MultiValue {

    private List<String> values = new ArrayList<String>();

    /**
     * @param value
     *        the value to add. <code>null</code>s are hold as an empty string.
     * @return <code>true</code> if the list of values has been changed, <code>false</code> otherwise.
     */
    public boolean addValue(String value) {
        if (value == null) {
            value = "";
        }
        if (values.contains(value)) {
            return false; // no duplicates
        }
        return values.add(value);
    }

    /**
     * @param value
     *        the value to remove.
     * @return <code>true</code> if the list of values has been changed, <code>false</code> otherwise.
     */
    public boolean removeValue(String value) {
        return values.remove(value);
    }

    /**
     * @return a read-only collection containing all added values preserving the order they were added.
     */
    public Collection<String> getValues() {
        return Collections.unmodifiableCollection(values);
    }
    
    /**
     * @return the amount of values.
     */
    public int size() {
        return values.size();
    }

    /**
     * @param value
     *        the value to check.
     * @return <code>true</code> if the value is contained by this instance.
     */
    public boolean contains(String value) {
        return values.contains(value);
    }

    /**
     * @return <code>true</code> if at least one value is was added to this instance, <code>false</code>
     *         otherwise.
     */
    public boolean hasValues() {
        return values.size() > 0;
    }

    /**
     * Removes all values contained by this instance.
     */
    public void removeAll() {
        values.clear();
    }
    
}
