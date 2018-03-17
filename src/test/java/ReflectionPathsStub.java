import org.q2p0.jw4a.reflection.ReflectionPaths;

import java.util.List;

public class ReflectionPathsStub implements ReflectionPaths {

    static final String RESOURCES_PATH = "src/test/resources";
    static final String REAL_ANDROIDHOME_RESOURCES_PATH = RESOURCES_PATH + "/real_android.jar";
    static final String FAKE_ANDROIDHOME_RESOURCES_PATH = RESOURCES_PATH + "/fake-android.jar";

    public ReflectionPathsStub(String androidHome, List<String> classPaths, List<String> jarPaths) {
        this.androidHome = androidHome;
        this.classPaths = classPaths;
        this.jarPaths = jarPaths;
    }

    String androidHome;
    List<String> classPaths;
    List<String> jarPaths;

    @Override
    public String getAndroidHome() {
        return androidHome;
    }

    @Override
    public List<String> getClassPaths() {
        return classPaths;
    }

    @Override
    public List<String> getJarPaths() {
        return jarPaths;
    }

}
