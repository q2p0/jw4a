package org.q2p0.jw4a.reflection;

import java.util.List;

//TODO: As subinterface of ReflectionPathProviders
public interface ReflectionPaths {
    String getAndroidHome();
    List<String> getClassPaths();
    List<String> getJarPaths();
}
