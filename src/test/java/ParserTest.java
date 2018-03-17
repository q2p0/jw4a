import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;
import org.q2p0.jw4a.ast.nodes.AST_Package;
import org.q2p0.jw4a.reflection.ReflectionHelper;
import org.q2p0.jw4a.reflection.ReflectionPaths;
import org.q2p0.jw4a.parser.Jw4aParserCaller;

import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void different_packagedef_sametree() {

        ReflectionPaths reflectionPaths = new ReflectionPathsStub( ReflectionPathsStub.REAL_ANDROIDHOME_RESOURCES_PATH, null, null);

        CharStream jw4aLists_1 = CharStreams.fromString(
            "@GLOBAL_API [19];\n" +
            "java.lang {\n" +
            "    class StringBuilder{\n" +
            "        java.lang.StringBuilder append(char c);\n" +
            "        java.lang.StringBuilder append(boolean c);\n" +
            "    }\n" +
            "}"
        );

        CharStream jw4aLists_2 = CharStreams.fromString(
            "@GLOBAL_API [19];\n" +
            "java.lang {\n" +
            "    class StringBuilder{\n" +
            "        java.lang.StringBuilder append(char c);\n" +
            "    }\n" +
            "}\n" +
            "java{\n" +
            "    lang{\n" +
            "        class StringBuilder{\n" +
            "            java.lang.StringBuilder append(boolean c);\n" +
            "        }\n" +
            "    }\n" +
            "}"
        );

        AST_Package ast1 = new Jw4aParserCaller( jw4aLists_1, reflectionPaths).parse();
        AST_Package ast2 = new Jw4aParserCaller( jw4aLists_2, reflectionPaths).parse();

        assertTrue("different_packagedef_sametree", ast1.treeEquals(ast2) ) ;

    }

}
