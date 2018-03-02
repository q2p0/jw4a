package org.q2p0.jw4a;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class ReflectionHelper {

    // SINGLETON

    private static ReflectionHelper instance;
    public static ReflectionHelper GetInstance() {
        if(instance==null)
            instance = new ReflectionHelper();
        return instance;
    }

    // COMMAND LINE ARGUMENTS

    final String ANDROID_HOME;
    final int    MINAPI_CL;
    final int    MAXAPI_CL;

    // ClassLoaders to search for definitions inside Android

    final private URLClassLoader ANDROID_LOADERS[];
    //TODO: final private URLClassLoader USER_LOADERS[];

    // CTOR

    //TODO: WARNING: User use jw4a for custom classes only
    private ReflectionHelper() {

        super();

        CLParameters clParameters = CLParameters.GetInstance();

        ANDROID_HOME = clParameters.androidHome;
        MINAPI_CL   = clParameters.minApi;
        MAXAPI_CL   = clParameters.maxApi;

        ANDROID_LOADERS = new URLClassLoader[ MAXAPI_CL - MINAPI_CL + 1 ];

        // Check that the SDK has the needed android jars
        for(int i=MINAPI_CL; i<=MAXAPI_CL; i++ ) {

            String android_jar_path = ANDROID_HOME + "/platforms/android-" + i + "/android.jar";
            File jarFile = new File( android_jar_path );
            if(!jarFile.exists() || jarFile.isDirectory()) {
                System.err.println("ERROR: Android.jar file for API" + i + "don't exist or is a directory.");
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