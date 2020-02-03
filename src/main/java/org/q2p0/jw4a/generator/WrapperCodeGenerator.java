package org.q2p0.jw4a.generator;

import org.q2p0.jw4a.ast.AST_Builder;
import org.q2p0.jw4a.ast.nodes.AST_Package;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;

public class WrapperCodeGenerator implements CodeGenerator{

    @Override public void generate(AST_Package ASTBuilder, String outputDirectory) {

        CreateFiles(outputDirectory);

        headerWriter.println("#ifndef __JW4A__");
        headerWriter.println("#define __JW4A__");
        headerWriter.println();

        headerWriter.println("#include <jni.h>");
        headerWriter.println();

        BuildNamespaces(ASTBuilder, 0);

        headerWriter.println();
        headerWriter.println("#endif");

        try {
            headerWriter.flush();
            headerWriter.close();
            sourceWriter.flush();
            sourceWriter.close();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    private void BuildNamespaces(AST_Package root, int level)
    {
        PrintTabs(headerWriter, level);
        headerWriter.println("namespace " + (root.id != null ? root.id : "j4wa"));
        PrintTabs(headerWriter, level);
        headerWriter.println("{");

        for(AST_Package ast_package : root.subPackages.values())
            BuildNamespaces(ast_package, level+1);

        PrintTabs(headerWriter, level);
        headerWriter.println("} //" + (root.id != null ? root.id : "j4wa"));
    }

    private void CreateFiles(String outputDirectory)
    {
        final String HEADER_FILE_STR = "jw4a.h";
        final String SOURCE_FILE_STR = "jw4a.cpp";

        try {
            headerFile = new File(outputDirectory + File.separator + HEADER_FILE_STR);
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

        try {
            sourceFile = new File(outputDirectory + File.separator + SOURCE_FILE_STR);
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

        try {
            headerWriter = new PrintStream(headerFile);
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

        try {
            sourceWriter = new FileWriter(sourceFile);
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    private File headerFile = null;
    private File sourceFile = null;
    private PrintStream headerWriter = null;
    private FileWriter sourceWriter = null;
    private final String TAB = "    ";
    private final void PrintTabs(PrintStream ps, int c){
        for(int i = 0; i<c; i++)
            ps.print(TAB);
    }
}
