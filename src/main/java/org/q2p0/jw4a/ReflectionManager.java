package org.q2p0.jw4a;

import org.q2p0.jw4a.abstractDesc.JObjectsTree.PairClassApi;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class ReflectionManager {

    // SINGLETON

    private static ReflectionManager instance;
    public static ReflectionManager GetInstance() {
        if(instance==null)
            instance = new ReflectionManager();
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

    //TODO WARNING: User use jw4a for custom classes only
    private ReflectionManager() {

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

    public ArrayList<PairClassApi> existClass(String name, int minApi, int maxApi) {

        ArrayList<PairClassApi> returnedArray = new ArrayList<PairClassApi>();

        //TODO: TRY TO FIND IN USER_LOADERS AND IF FOUND

            //TODO: Check minApi & maxApi is inside MINAPI_CL && MAXAPI_CL range

            for(int api = minApi; api <= maxApi; api++ ) {

                URLClassLoader classLoader = ANDROID_LOADERS[ api - MINAPI_CL ];
                Class _class = null;
                try { _class = Class.forName( name, false, classLoader);
                } catch (ClassNotFoundException e) { /*_class will be null*/ }

                if( _class != null ) {
                    PairClassApi refClassApi = new PairClassApi();
                    refClassApi._class = _class;
                    refClassApi._api = api;
                    returnedArray.add( refClassApi );
                } else {
                    System.err.println("WARNING: The class " + name + " has been not found on Android.jar API Level " + api);
                    //TODO: Add message showing how use anotations to avoid the warning
                }
            }

            if( returnedArray.size() == 0 )
                returnedArray = null;

        //TODO: }

        return returnedArray;
    }

    public ArrayList<PairClassApi>  existClass(String name) {
        return existClass( name, MINAPI_CL, MAXAPI_CL);
    }

}
