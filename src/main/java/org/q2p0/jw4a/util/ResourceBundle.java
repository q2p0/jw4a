package org.q2p0.jw4a.util;

public class ResourceBundle {
    public static java.util.ResourceBundle getBundle4Class(Class c){
        return java.util.ResourceBundle.getBundle(c.getName());
    }
}
