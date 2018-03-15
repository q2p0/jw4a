package org.q2p0.jw4a.reflection;

import org.q2p0.jw4a.ExitErrorCodes;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class ReflectionHelper {

    // COMMAND LINE ARGUMENTS

    private final String ANDROID_HOME;
    private final int    MINAPI_CL;
    private final int    MAXAPI_CL;

    // ClassLoaders to search for definitions inside Android

    final private URLClassLoader ANDROID_LOADERS[];
    //TODO: final private URLClassLoader USER_LOADERS[];

    // CTOR

    public ReflectionHelper(ReflectionPaths params, int minApi, int maxApi ) {

        super();

        assert (minApi<=maxApi);

        ANDROID_HOME = params.getAndroidHome();
        MINAPI_CL   = minApi;
        MAXAPI_CL   = maxApi;

        ANDROID_LOADERS = new URLClassLoader[ MAXAPI_CL - MINAPI_CL + 1 ];

        // Check that the SDK has the needed android jars
        for(int i=MINAPI_CL; i<=MAXAPI_CL; i++ ) {

            String android_jar_path = ANDROID_HOME + "/platforms/android-" + i + "/android.jar";
            File jarFile = new File( android_jar_path );
            if(!jarFile.exists() || jarFile.isDirectory()) {
                System.err.println("ERROR: Android.jar file for API " + i + " don't exist or is a directory.");
                //TODO: Show command line call to install this lost android.jar file
                System.exit(ExitErrorCodes.ANDROID_JAR_NOT_FOUND );
            }

            try { ANDROID_LOADERS[i-MINAPI_CL] = new URLClassLoader ( new URL[]{ jarFile.toURI().toURL() }); }
            catch (MalformedURLException e) { e.printStackTrace(); }
        }

    }

    public Map< Integer, Class > getClasses(String name, int minApi, int maxApi) {

        Map< Integer, Class > returnedHash = new HashMap<>();

        for(int api = minApi; api <= maxApi; api++ ) {

            URLClassLoader classLoader = ANDROID_LOADERS[ api - MINAPI_CL ];
            Class _class = null;
            try { _class = Class.forName( name, false, classLoader);
            } catch (ClassNotFoundException e) { /*_class will be null*/ }

            if( _class != null ) {
                returnedHash.put( api, _class );
            } else {
                System.err.println("WARNING: The class " + name + " has been not found on Android.jar API Level " + api);
                //TODO: Add message showing how use anotations to avoid the warning
            }
        }

        if( returnedHash.size() == 0 )
            returnedHash = null;

        return returnedHash;
    }

    public Map< Integer, Class > getClasses(String name ) {
        return getClasses(name, MINAPI_CL, MAXAPI_CL);
    }

    public Map<Integer, Class> getSuperClasses(Map <Integer, Class> inheritClasses){

        Map<Integer, Class> superClasses = new HashMap<>( inheritClasses.size() );

        for (Map.Entry<Integer, Class> entry : inheritClasses.entrySet()) {
            int api = entry.getKey();
            Class _class = entry.getValue();
            Class superClass = _class.getSuperclass();
            if( superClass != null )
                superClasses.put( api, superClass );
        }

        return superClasses;

    }

}

        /*
            TODO: Old command line minmaxapi parsing

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
        */
