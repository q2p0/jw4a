package org.q2p0.jw4a.util;

/**
 * in: "string1.string2.string3"
 * head: "string1"
 * tail: "string2.string3"
 * last: "string3"
 * init: "string1.string2"
 *
 * in: "string1"
 * head: "string1"
 * tail: <null>
 * last: "string1"
 * init: <null>
 */

public class DottedString {

    static public Boolean isAtomic( String dottedString ) {
        assert ( dottedString == null || !dottedString.trim().isEmpty() );
        return dottedString.indexOf('.') != -1;
    }

    static public String head( String dottedString ){
        assert ( dottedString == null || !dottedString.trim().isEmpty() );
        int firstDot = dottedString.indexOf('.');
        return firstDot!=-1 ? dottedString.substring(0,firstDot) : dottedString;
    }

    static public String tail( String dottedString ){
        assert ( dottedString == null || !dottedString.trim().isEmpty() );
        int firstDot = dottedString.indexOf('.');
        return firstDot!=-1 ? dottedString.substring(firstDot+1) : null;
    }

    static public String last( String dottedString ){
        assert ( dottedString == null || !dottedString.trim().isEmpty() );
        int lastDot = dottedString.lastIndexOf('.');
        return lastDot!=-1 ? dottedString.substring(lastDot+1) : dottedString;
    }

    static public String init( String dottedString ){
        assert ( dottedString == null || !dottedString.trim().isEmpty() );
        int lastDot = dottedString.lastIndexOf('.');
        return lastDot!=-1 ? dottedString.substring(0, lastDot) : null;
    }

}
