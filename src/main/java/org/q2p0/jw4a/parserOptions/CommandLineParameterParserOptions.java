package org.q2p0.jw4a.parserOptions;

import org.apache.commons.cli.*;
import org.q2p0.jw4a.ExitErrorCodes;
import org.q2p0.jw4a.reflection.ReflectionHelperOptions;
import org.q2p0.jw4a.reflection.ReflectionHelperOptionsProvider;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.q2p0.jw4a.util.Application.getProgName;

// COMMAND LINE ARGUMENTS PARSER
// Responsibilities:
// - Define the available arguments & default values.
// - Parse and store the arguments values.
// - Check the correction of the arguments and show an error in case of failure.

public class CommandLineParameterParserOptions implements ReflectionHelperOptionsProvider { //TODO: Rename to CLArgumentsParser

    //TODO: Add custom user java classes path parameters

    private static final String DEFINITION_FILE_KEY = "df";
    private static final String DEFINITION_FILE_DEFAULT = "Jw4aLists.txt";
    public String definitionFile;


    private static final String ANDROID_HOME_KEY = "ah";
    public String androidHome;

    //TODO: Refactor to JW4A global expression.
    @Deprecated private static final String API_LEVEL_KEY = "al";
    @Deprecated public int minApi, maxApi;

    private static final String OUTPUT_DIRECTORY = "o";
    private static final String OUTPUT_DIRECTORY_DEFAULT = "jw4a_classes";
    String outputDirectory;

    // Command line args parse method
    public void parseArgs(String[] args ) {

        Option android_home_option = Option.builder(ANDROID_HOME_KEY).longOpt("android-home")
                .desc("Path where the Android SDK is installed. If not specified, '" + getProgName() +
                      "' will try to obtain it from the environment ANDROID_HOME variable.")
                .hasArg().required(false).build();

        Option definitions_file = Option.builder(DEFINITION_FILE_KEY).longOpt("definitions-file")
                .desc("Path to file that contains the definitions for JNI Java Helpers that will be created. " +
                      "If not defined '" + DEFINITION_FILE_DEFAULT + "' will be used.")
                .hasArg().required(false).build();

        Option api_level = Option.builder(API_LEVEL_KEY).longOpt("api-levels")
                .desc("Range of API levels that will be used to build the JNI Java Helpers. Can be a concrete API "
                +"'-"+API_LEVEL_KEY+" 21' or a range '-"+API_LEVEL_KEY+" 21-26'.")
                .hasArg().required(true).build(); //TODO: Make it not required when user can search definition only for his custom classes

        Option output = Option.builder(OUTPUT_DIRECTORY).longOpt("output")
                .desc("Path to the directory where the jw4a will be created. " +
                "If not defined, '" + OUTPUT_DIRECTORY_DEFAULT + "' " + "will be used.")
                .hasArg().required(false).build();

        Options options = new Options()
                .addOption( android_home_option )
                .addOption( definitions_file )
                .addOption( api_level )
                .addOption( output );

        CommandLine cmd = null;
        CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse( options, args);
        } catch (Exception e) {
            System.err.println("ERROR: Incorrect usage");
            new HelpFormatter().printHelp( getProgName() + " [Options]", options );
            System.exit(ExitErrorCodes.APACHE_CLI );
        }

        // Definitions file

        definitionFile = cmd.getOptionValue( DEFINITION_FILE_KEY );
        if( definitionFile == null )
            definitionFile = DEFINITION_FILE_DEFAULT;
        File f = new File(definitionFile);
        if(!f.exists() || f.isDirectory()) {
            System.err.println("ERROR: Definitions file don't exist or is a directory.");
            System.exit(ExitErrorCodes.INCORRECT_JW4ALISTS );
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
                    System.err.println("ERROR: Api levels specified but ANDROID_HOME not specified " +
                                       "or ANDROID_HOME environment variable not defined.");
                    System.exit( ExitErrorCodes.APILEVELS_WITHOUT_ANDROID_HOME );
                }
            }
            if( androidHome.charAt( androidHome.length() - 1 ) == File.separatorChar )
                androidHome = androidHome.substring( 0, androidHome.length() - 2 );
            //TODO: Check that exist & is an directory

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
                    } else
                        maxApi = minApi;
                } catch (java.lang.NumberFormatException e) {
                    System.err.println("ERROR: Incorrect API levels");
                    e.printStackTrace();
                    System.exit(ExitErrorCodes.APILEVELS_PARSEINT);
                }

                if( maxApi < minApi && maxApi != 0 ) {
                    System.err.println("ERROR: Incorrect API levels. Correct range is 'a-b' where a<b");
                    System.exit(ExitErrorCodes.APILEVELS_INVERTED_RANGE);
                }

            } else {
                System.err.println("ERROR: Api levels must be a concrete value '21' or a range '21-26'");
                System.exit(ExitErrorCodes.APILEVELS_REGEX_FAIL);
            }

        }

        // Output directory

        outputDirectory = cmd.getOptionValue( OUTPUT_DIRECTORY );
        if( outputDirectory == null )
            outputDirectory = OUTPUT_DIRECTORY_DEFAULT;
        File od = new File(outputDirectory);
        if( od.exists() ) {
            //TODO: Delete
            //TODO: Show deletion warning message
        }
        if( !od.exists() ) {
            try{ od.mkdir(); }
            catch(SecurityException se){
                //TODO: Error
            }
        }
    }

    @Override public ReflectionHelperOptions getReflectionHelperOptions() {
        return new ReflectionHelperOptions() {
            @Override public String getAndroidHome() { return androidHome; }
            @Override public int getMinApi() { return minApi; }
            @Override public int getMaxApi() { return maxApi; }
        };
    }
}
