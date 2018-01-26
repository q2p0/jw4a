package org.q2p0.jw4a;

public class Jw4a {

    public static void main(String[] args) {

        CLParameters clparser = new CLParameters();
        clparser.parseArgs( args );

        //TODO: Next step
    }

    /**
     * Get the name of the compiled jar file.
     */
    static String getProgName() {
        return new java.io.File(Jw4a.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath()).getName();
    }

}
