package org.q2p0.jw4a.util;

import java.util.HashMap;

public class HashMapSetGet<K> extends HashMap<K, K> implements SetGet<K>{

    @Override public void add(K element) throws RuntimeException {
        if( this.get(element) != null )
            //T0D0: Own RuntimeException if SetGet interface is implemented by another class.
            throw new RuntimeException( "The element " + element + " already exists on " + getClass().getSimpleName() );
        this.put( element, element);
    }

    @Override public K addOrGet(K element) {
        K previous = this.get(element);
        if( previous == null ) {
            previous = element;
            this.put( previous, previous);
        }
        return previous;
    }

}
