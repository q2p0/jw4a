package org.q2p0.jw4a.util;

import org.q2p0.jw4a.Jw4a;

public class Application {
    /**
     * Get the name of the compiled jar file.
     */
    public static String getProgName() {
        return new java.io.File(Jw4a.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath()).getName();
    }

}
