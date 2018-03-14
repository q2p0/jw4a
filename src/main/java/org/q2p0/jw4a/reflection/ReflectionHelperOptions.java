package org.q2p0.jw4a.reflection;

public interface ReflectionHelperOptions { //TODO: Rename to ReflectionHelperPaths

    String getAndroidHome();

    @Deprecated int getMinApi();
    @Deprecated int getMaxApi();

    //TODO: ArrayList<String> getClassPaths();
    //TODO: ArrayList<String> getJarPaths();

}
