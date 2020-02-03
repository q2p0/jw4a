package org.q2p0.jw4a.generator;

import org.q2p0.jw4a.ast.nodes.AST_Class;
import org.q2p0.jw4a.ast.nodes.AST_Package;
import org.q2p0.jw4a.ast.nodes.method.AST_Method;
import org.q2p0.jw4a.ast.nodes.method.methodReturn.AST_AbstractMethodReturn;
import org.q2p0.jw4a.ast.nodes.method.methodReturn.AST_ClassMethodReturn;
import org.q2p0.jw4a.ast.nodes.method.methodReturn.AST_PrimitiveTypeMethodReturn;
import org.q2p0.jw4a.ast.nodes.method.methodReturn.AST_VoidMethodReturn;
import org.q2p0.jw4a.ast.nodes.method.parameter.AST_AbstractParameter;
import org.q2p0.jw4a.ast.nodes.method.parameter.AST_ClassParameter;
import org.q2p0.jw4a.ast.nodes.method.parameter.AST_PrimitiveParameter;

import java.awt.*;
import java.io.File;
import java.io.PrintStream;
import java.util.Map;
import java.util.Set;

public class WrapperCodeGenerator implements CodeGenerator{

    @Override public void generate(AST_Package ASTBuilder, String outputDirectory) {

        CreateFiles(outputDirectory);

        headerWriter.println("#ifndef __JW4A__");
        headerWriter.println("#define __JW4A__");
        headerWriter.println();

        headerWriter.println("#include <jni.h>");
        headerWriter.println();

        sourceWriter.println("#include \"jw4a.h\"");

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
        boolean subnamespaces = true;

        if(root.id != null || subnamespaces) {

            // Header
            PrintTabs(headerWriter, level);
            headerWriter.println("namespace " + (root.id != null ? root.id : "j4wa"));
            PrintTabs(headerWriter, level);
            headerWriter.println("{");

            // Source file
            PrintTabs(sourceWriter, level);
            sourceWriter.println("namespace " + (root.id != null ? root.id : "j4wa"));
            PrintTabs(sourceWriter, level);
            sourceWriter.println("{");
        }

        for(AST_Package ast_package : root.subPackages.values())
            BuildNamespaces(ast_package, level+1);

        if(root.subClasses.values().size()>0){

            PrintTabs(headerWriter, level + 1);
            headerWriter.println("// Class holders");
            for(AST_Class ast_class : root.subClasses.values())
                BuildClassHolders(ast_class, level+1);
            headerWriter.println();

            PrintTabs(headerWriter, level + 1);
            headerWriter.println("// Class declaration");
            for(AST_Class ast_class : root.subClasses.values())
                BuildClassDeclarations(ast_class, level+1);
        }

        if(root.id != null || subnamespaces) {

            PrintTabs(headerWriter, level);
            headerWriter.println("} //" + (root.id != null ? root.id : "j4wa"));

            PrintTabs(sourceWriter, level);
            sourceWriter.println("} //" + (root.id != null ? root.id : "j4wa"));
        }
    }

    private String BuildClassNameForCpp(AST_Class ast_class)
    {
        return "jw4a_" + ast_class.id;
    }

    private void BuildClassHolders(AST_Class ast_class, int level) {
        PrintTabs(headerWriter, level);
        if(!ast_class.id.equals("Object"))
            headerWriter.println("class " + BuildClassNameForCpp(ast_class) + ";");
    }

    private void BuildClassDeclarations(AST_Class ast_class, int level) {
        if(!ast_class.id.equals("Object")) {
            PrintTabs(headerWriter, level);
            headerWriter.print("class " + BuildClassNameForCpp(ast_class) + " : ");
            Map<Integer, AST_Class> superClasses = ast_class.superClass;
            if (superClasses.size() != 1)
                throw new RuntimeException("Assert superClasses size != 1");
            Map.Entry<Integer, AST_Class> superClass = superClasses.entrySet().iterator().next();
            headerWriter.println("public " + (!superClass.getValue().id.equals("Object") ? BuildClassNameForCpp(superClass.getValue()) : "_jobject"));
            PrintTabs(headerWriter, level);
            headerWriter.println("{");

            CreateMethodIDs4Class( ast_class, level + 1);

            PrintTabs(headerWriter, level);
            headerWriter.println("};");
        }
    }

    //TODO: Manage Android Integer 4 APIs
    private void CreateMethodIDs4Class(AST_Class ast_class, int level) {

        Set<Map.Entry<Integer, Set<AST_Method>>> methods = ast_class.methods.entrySet();
        if(methods.size() > 0){
            Map.Entry<Integer, Set<AST_Method>> me = methods.iterator().next();
            Set<AST_Method> set = me.getValue();
            if(set.size() > 0)
            {
                // Header
                PrintTabs(headerWriter, level);
                headerWriter.println("public:");
                PrintTabs(headerWriter, level+1);
                headerWriter.println("static void StaticInit(JNIEnv * pEnv);");

                // Source
                PrintTabs(sourceWriter, level);
                sourceWriter.print("void " );
                sourceWriter.print(BuildClassNameForCpp(ast_class));
                sourceWriter.print("::");
                sourceWriter.println("StaticInit(JNIEnv * pEnv)");
                PrintTabs(sourceWriter, level);
                sourceWriter.println("{");

                // Header & Source StaticInit
                PrintTabs(headerWriter, level);
                headerWriter.println("private:");
                for(AST_Method m : set)
                {
                    PrintTabs(headerWriter, level+1);
                    headerWriter.print("static jmethodID ");
                    headerWriter.print(buildIDForMethod(ast_class, m));
                    headerWriter.println(";");
                }

                PrintTabs(sourceWriter, level);
                sourceWriter.println("}");
            }
            else
            {
                throw new RuntimeException("No methods 2");
            }
        }
        /*
        else
        {
            throw new RuntimeException("No methods 1");
        }
        */
    }

    private String buildIDForMethod(AST_Class ast_class, AST_Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(method.id);
        builder.append("_").append(buildIDForMethodReturn(method.returnDesc));
        for(AST_AbstractParameter ap : method.parameters)
            builder.append("_").append(buildIDForParameter(ap));
        return  builder.toString();
    }

    private String buildIDForParameter(AST_AbstractParameter ap) {
        StringBuilder builder = new StringBuilder();
        if(ap instanceof AST_PrimitiveParameter)
        {
            builder.append("p");
            builder.append(((AST_PrimitiveParameter) ap).primitiveType.toString());
        }
        else if(ap instanceof AST_ClassParameter)
        {
            builder.append("c");
            builder.append(((AST_ClassParameter) ap).astClass.id); // TODO: Also add the path
        }
        else
        {
            throw new RuntimeException("Unimplemented AST_AbstractParameter on method WrapperCodeGenerator::buildIDForParameter");
        }

        return builder.toString();
    }

    private String buildIDForMethodReturn(AST_AbstractMethodReturn amr){
        StringBuilder builder = new StringBuilder();
        if(amr instanceof AST_VoidMethodReturn)
        {
            builder.append("void");
        }
        else if(amr instanceof AST_PrimitiveTypeMethodReturn)
        {
            builder.append("p");
            builder.append(((AST_PrimitiveTypeMethodReturn) amr).primitiveType.toString());
        }
        else if(amr instanceof AST_ClassMethodReturn)
        {
            builder.append("c");
            builder.append(((AST_ClassMethodReturn) amr).astClass.id); // TODO: Also add the path
        }
        else
        {
            throw new RuntimeException("Unimplemented AST_AbstractMethodReturn on method WrapperCodeGenerator::buildIDForMethodReturn");
        }
        return builder.toString();
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
            sourceWriter = new PrintStream(sourceFile);
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    private File headerFile = null;
    private File sourceFile = null;
    private PrintStream headerWriter = null;
    private PrintStream sourceWriter = null;
    private final String TAB = "    ";
    private final void PrintTabs(PrintStream ps, int c){
        for(int i = 0; i<c; i++)
            ps.print(TAB);
    }
}
