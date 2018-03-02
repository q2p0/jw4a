package org.q2p0.jw4a.util;

import java.util.*;

public class CollectionUtil {

    /**
     * Get an set of integers and return an string of ranges.
     * {} -> ""
     * {3} -> "3"
     * {1,2,3,4} -> "1-4"
     * {1,2,4,7,8} -> "1-2,4,7-8"
     * @param apis The set of integers.
     * @return An String of ranges.
     */
    public static String toRangeString( Collection<Integer> apis ) {

        Integer a[] = apis.stream().sorted().toArray(Integer[]::new);

        StringBuilder stringBuilder = new StringBuilder();

        final int n = a.length;
        for (int i = 0; i < n; ++i) {
            final int start = i;
            while(i < n-1 && a[i] >= a[i+1]-1)
                ++i;
            stringBuilder.append( a[start] );
            if ( ! a[start].equals(a[i]) )
                stringBuilder.append('-').append(a[i]);
            if (i < n-1)
                stringBuilder.append(',');
        }

        return stringBuilder.toString();

    }

    /**
     * Reverse an MultiSetMap changing the roles between key and value.
     * @param input Original MultiSetMap that will be reversed.
     * @param <K> The key on the original MultiSetMap and the value on the reverse MultiSetMap.
     * @param <V> The value on the original MultiSetMap and the key on the reverse MultiSetMap.
     * @return The reversed MultiSetMap
     */
    static public <K,V> Map<V,Set<K>> reverseMultiSetMap( Map<K,Set<V>> input ){

        Set<V> methodSet = new HashSet<>();
        for( Set<V> methodList : input.values() )
            methodSet.addAll( methodList );

        Map<V, Set<K> > reverse = new HashMap<>( methodSet.size() );

        for( V method : methodSet)
            for( K i : input.keySet() )
                if( input.get(i).contains(method) )
                    reverse.computeIfAbsent( method, k->new HashSet<>() ).add( i );

        return reverse;

    }
}
