package org.q2p0.jw4a.util;

import java.util.Set;

public interface SetGet<K> {

    /**
     * Add an element to the set
     * @param element
     * @throws RuntimeException
     */
    void add(K element) throws RuntimeException;

    /**
     * Get the element inside the set that is equal to the element passed as parameter
     * @param element The key element used to find the stored element
     * @return The element or null if set do not contain the element
     */
    K get(K element);

    /**
     * Add an element to the set if it does not exist. If there is already an parameter
     * equals to the parameter @element the set does not change.
     * @param element The element to be added if it does not exist.
     * @return The element passed as parameter or previous one inside the set with the same key.
     */
    K addOrGet(K element);

    /**
     * Returns a Set view of the keys contained in this map.
     * @return a set view of the keys contained in this map
     */
    Set<K> keySet();

}
