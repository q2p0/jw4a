package org.q2p0.jw4a;

import org.apache.commons.cli.*;

import java.io.File;
import java.util.Map;
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

    //TODO: Add custom user java classes path parameters

    void parseArgs(String[] args ) {

        Option android_home_option = Option.builder(ANDROID_HOME_KEY).longOpt("android-home")
                .desc("Path where the Android SDK is installed. If not specified, '" + Jw4a.getProgName() +
                      "' will try to obtain it from the environment ANDROID_HOME variable.")
                .hasArg().required(false).build();

        Option definitions_file = Option.builder(DEFINITION_FILE_KEY).longOpt("definitions-file")
                .desc("Path to file that contains the definitions for JNI Java Helpers that will be created. " +
                      "If not defined '" + DEFINITION_FILE_DEFAULT + "' will be used.")
                .hasArg().required(false).build();

        Option api_level = Option.builder(API_LEVEL_KEY).longOpt("api-levels")
                .desc("Range of API levels that will be used to build the JNI Java Helpers. Can be a concrete API "
                +"'-"+API_LEVEL_KEY+" 21' or a range '-"+API_LEVEL_KEY+" 21-26'.")
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

        String apiLevel = cmd.getOptionValue(API_LEVEL_KEY);
        if( apiLevel == null ) {
            System.err.println("WARNING: Api levels don't specified. No classes will be searched on the Android system.");
        } else {

            androidHome = cmd.getOptionValue(ANDROID_HOME_KEY);
            if( androidHome == null ) {
                Map<String, String> env = System.getenv();
                for (String envName : env.keySet())
                    if( envName.equals("ANDROID_HOME") ) {
                        androidHome = env.get(envName);
                        break;
                    }
                if( androidHome == null ) {
                    System.err.println("ERROR: Api levels specified but ANDROID_HOME not specified" +
                                       "or ANDROID_HOME environment variable not defined.");
                    System.exit(-3);
                }
            }
            //TODO: Check android home is a directory

            Pattern p = Pattern.compile("(\\d+)(-\\d+)?");
            Matcher m = p.matcher(apiLevel);
            if( m.matches() ) {

                String minApiStr = m.group(1);
                String maxApiStr = m.group(2);

                try {
                    minApi = Integer.parseInt(minApiStr);
                    if (maxApiStr != null) {
                        maxApiStr = maxApiStr.substring(1);
                        maxApi = Integer.parseInt(maxApiStr);
                    }
                } catch (java.lang.NumberFormatException e) {
                    System.err.println("ERROR: Incorrect API levels");
                    e.printStackTrace();
                    System.exit(-5);
                }

                //TODO: Check that the SDK has the needed android jars

            } else {
                System.err.println("ERROR: Api levels must be a concrete value '21' or a range '21-26'");
                System.exit(-4);
            }
        }
    }
}
