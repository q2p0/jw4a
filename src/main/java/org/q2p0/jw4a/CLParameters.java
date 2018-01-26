package org.q2p0.jw4a;

import org.apache.commons.cli.*;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse the arguments from the command line
 */
public class CLParameters {

    private static final String DEFINITION_FILE_KEY = "df";
    private static final String DEFINITION_FILE_DEFAULT = "Jw4aLists.txt";
    String definitionFile;

    private static final String ANDROID_HOME_KEY = "ah";
    String androidHome;

    private static final String API_LEVEL_KEY = "al";
    int minApi, maxApi;

    void ParseArgs( String[] args ) {

        Option android_home_option = Option.builder(ANDROID_HOME_KEY).longOpt("android-home")
                .desc("Path where the Android SDK is installed. If not specified, '" + Jw4a.getProgName() + "' will try to obtain it from the environment ANDROID_HOME variable.")
                .hasArg().required(false).build();

        Option definitions_file = Option.builder(DEFINITION_FILE_KEY).longOpt("definitions-file")
                .desc("Path to file that contains the definitions for JNI Java Helpers that will be created. If not defined '" + DEFINITION_FILE_DEFAULT + "' will be used.")
                .hasArg().required(false).build();

        Option api_level = Option.builder(API_LEVEL_KEY).longOpt("api-levels")
                .desc("Range of API levels that will be used to build the JNI Java Helpers. Can be a concrete API '-a 21' or a range '-a 21-26'.")
                .hasArg().required(false).build();

        Options options = new Options()
                .addOption( android_home_option )
                .addOption( definitions_file )
                .addOption( api_level );

        CommandLine cmd = null;
        CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse( options, args);
        } catch (Exception e) {
            System.err.println("ERROR: Incorrect usage");
            new HelpFormatter().printHelp( Jw4a.getProgName() + " [Options]", options );
            System.exit(-1);
        }

        // Definitions file

        definitionFile = cmd.getOptionValue( DEFINITION_FILE_KEY );
        if( definitionFile == null )
            definitionFile = DEFINITION_FILE_DEFAULT;
        File f = new File(definitionFile);
        if(!f.exists() || f.isDirectory()) {
            System.err.println("ERROR: Definitions file don't exist or is a directory.");
            System.exit(-2 );
        }

        // ANDROID_HOME & API_LEVEL

        androidHome = cmd.getOptionValue(ANDROID_HOME_KEY);
        //TODO: If ANDROID_HOME=null try to get the environment variable
        //TODO: Take sure ANDROID_HOME is a directory, else error
        String API_LEVEL = cmd.getOptionValue(API_LEVEL_KEY);
        if( API_LEVEL == null ) {
            System.err.println("WARNING: Api levels don't specified. No classes will be searched on the Android system.");
        } else if(androidHome != null){
            System.err.println("ERROR: Api levels specified but ANDROID_HOME not specified");
            System.exit(-3);
        } else {
            Pattern p = Pattern.compile("(\\d+)(-\\d+)?");
            Matcher m = p.matcher(API_LEVEL);
            if( m.matches() ) {
                //m.find();
                //m
            } else {

            }
        }



















        System.out.println("HELLO");

    }


}
