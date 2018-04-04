import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import static org.junit.Assert.assertTrue;
import org.q2p0.jw4a.ExitErrorCodes;
import org.q2p0.jw4a.ast.nodes.AST_Package;
import org.q2p0.jw4a.reflection.ReflectionPaths;
import org.q2p0.jw4a.parser.Jw4aParserCaller;

public class ParserTest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void different_packagedef_sametree() {

        ReflectionPaths reflectionPaths = new ReflectionPathsStub( ReflectionPathsStub.REAL_ANDROIDHOME_RESOURCES_PATH, null, null);

        CharStream jw4aLists_1 = CharStreams.fromString(
            "@GLOBAL_API [19];" +
            "java.lang {" +
            "    class StringBuilder{" +
            "        java.lang.StringBuilder append(char c);" +
            "        java.lang.StringBuilder append(boolean c);" +
            "    }" +
            "}"
        );

        CharStream jw4aLists_2 = CharStreams.fromString(
            "@GLOBAL_API [19];" +
            "java.lang {" +
            "    class StringBuilder{" +
            "        java.lang.StringBuilder append(char c);" +
            "    }" +
            "}" +
            "java{" +
            "    lang{" +
            "        class StringBuilder{" +
            "            java.lang.StringBuilder append(boolean c);" +
            "        }" +
            "    }" +
            "}"
        );

        AST_Package ast1 = new Jw4aParserCaller( jw4aLists_1, reflectionPaths).parse();
        AST_Package ast2 = new Jw4aParserCaller( jw4aLists_2, reflectionPaths).parse();

        assertTrue("different_packagedef_sametree", ast1.treeEquals(ast2) ) ;

    }

    @Test
    public void method_exist_only_api_1_error() {

        exit.expectSystemExitWithStatus( ExitErrorCodes.PARSER_ERROR_METHOD_NOT_FOUND );

        ReflectionPaths reflectionPaths = new ReflectionPathsStub( ReflectionPathsStub.FAKE_ANDROIDHOME_RESOURCES_PATH, null, null);

        CharStream jw4aLists_1 = CharStreams.fromString(
            "@GLOBAL_API [1-2];" +
            "people {" +
            "    class Person{" +
            "        int onlyonapi1();" +
            "    }" +
            "}"
        );

        AST_Package ast1 = new Jw4aParserCaller( jw4aLists_1, reflectionPaths).parse();

    }

    @Test
    public void method_exist_only_api_1_modifier() {



        ReflectionPaths reflectionPaths = new ReflectionPathsStub( ReflectionPathsStub.FAKE_ANDROIDHOME_RESOURCES_PATH, null, null);

        CharStream jw4aLists_1 = CharStreams.fromString(
                "@GLOBAL_API [1-2];" +
                "people {" +
                "    class Person{" +
                "        @API[1] int onlyonapi1();" +
                "    }" +
                "}"
        );

        AST_Package ast1 = new Jw4aParserCaller( jw4aLists_1, reflectionPaths).parse();

        assertTrue(ast1 != null ); //TODO: Better test with tree

    }

    @Test
    public void class_not_found_lower_apis_error() {

        exit.expectSystemExitWithStatus( ExitErrorCodes.PARSER_ERROR_CLASS_NOT_FOUND );

        ReflectionPaths reflectionPaths = new ReflectionPathsStub( ReflectionPathsStub.FAKE_ANDROIDHOME_RESOURCES_PATH, null, null);

        CharStream jw4aLists = CharStreams.fromString(
        "@GLOBAL_API [1-3];" +
                "people {" +
                "    class Mike{" +
                "        void method();" +
                "    }" +
                "}"
        );

        AST_Package ast1 = new Jw4aParserCaller( jw4aLists, reflectionPaths).parse();
    }

    @Test
    public void class_not_found_lower_api_modifier() {

        ReflectionPaths reflectionPaths = new ReflectionPathsStub( ReflectionPathsStub.FAKE_ANDROIDHOME_RESOURCES_PATH, null, null);

        CharStream jw4aLists = CharStreams.fromString(
        "@GLOBAL_API [1-3];" +
            "people {" +
            "    @API[3-] class Mike{" +
            "        void method();" +
            "    }" +
            "}"
        );

        AST_Package ast1 = new Jw4aParserCaller( jw4aLists, reflectionPaths).parse();

        assertTrue(ast1 != null ); //TODO: Better test with tree
    }

}
