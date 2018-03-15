import org.junit.Rule;
import org.junit.Test;
import org.q2p0.jw4a.ExitErrorCodes;
import org.q2p0.jw4a.reflection.ReflectionHelper;

import org.junit.contrib.java.lang.system.ExpectedSystemExit;

public class ReflectionHelperTest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void android_jar_not_found() {
        exit.expectSystemExitWithStatus( ExitErrorCodes.ANDROID_JAR_NOT_FOUND );
        ReflectionHelper reflectionHelper = new ReflectionHelper(
            new ReflectionPathsStub("/not/exist/path", null, null),19, 21
        );
    }

}