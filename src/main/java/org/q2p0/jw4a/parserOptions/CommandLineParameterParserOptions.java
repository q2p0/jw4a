package org.q2p0.jw4a.parserOptions;

import org.apache.commons.cli.*;
import org.q2p0.jw4a.ExitErrorCodes;
import org.q2p0.jw4a.reflection.ReflectionPaths;
import org.q2p0.jw4a.reflection.ReflectionPathsProvider;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.q2p0.jw4a.util.Application.getProgName;

// COMMAND LINE ARGUMENTS PARSER
// Responsibilities:
// - Define the available arguments & default values.
// - Parse and store the arguments values.
// - Check the correction of the arguments and show an error in case of failure.

public class CommandLineParameterParserOptions implements ReflectionPathsProvider {

    private static final String DEFINITION_FILE_KEY = "df";
    private static final String DEFINITION_FILE_DEFAULT = "Jw4aLists.txt";
    public String definitionFile;


    private static final String ANDROID_HOME_KEY = "ah";
    private String androidHome;

    private static final String OUTPUT_DIRECTORY = "o";
    private static final String OUTPUT_DIRECTORY_DEFAULT = "jw4a_classes";
    private String outputDirectory;

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

        Option output = Option.builder(OUTPUT_DIRECTORY).longOpt("output")
                .desc("Path to the directory where the jw4a will be created. " +
                "If not defined, '" + OUTPUT_DIRECTORY_DEFAULT + "' " + "will be used.")
                .hasArg().required(false).build();

        Options options = new Options()
                .addOption( android_home_option )
                .addOption( definitions_file )
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

        // ANDROID_HOME

        androidHome = cmd.getOptionValue(ANDROID_HOME_KEY);
        if( androidHome == null ) {
            Map<String, String> env = System.getenv();
            for (String envName : env.keySet())
                if( envName.equals("ANDROID_HOME") ) {
                    androidHome = env.get(envName);
                    break;
                }
        }
        if( androidHome != null ) {
            if( androidHome.charAt( androidHome.length() - 1 ) == File.separatorChar )
                androidHome = androidHome.substring( 0, androidHome.length() - 2 );
            //TODO: Check that exist & is an directory
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

    @Override public ReflectionPaths getReflectionHelperOptions() {
        return new ReflectionPaths() {
            @Override public String getAndroidHome() { return androidHome; }
            @Override
            public List<String> getClassPaths() {
                throw new NotImplementedException();
            }
            @Override
            public List<String> getJarPaths() {
                throw new NotImplementedException();
            }
        };
    }
}
