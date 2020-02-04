package org.q2p0.jw4a.generator;

import org.q2p0.jw4a.ast.nodes.AST_Class;
import org.q2p0.jw4a.ast.nodes.AST_Package;
import org.q2p0.jw4a.ast.nodes.AST_PrimitiveType;
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
import java.util.Iterator;
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
        return BuildClassNameForCpp(ast_class, false);
    }
    private String BuildClassNameForCpp(AST_Class ast_class, boolean includeNamespace)
    {
        StringBuilder sb = new StringBuilder();
        if(includeNamespace)
        {
            AST_Package ast_package = ast_class.ast_package;
            while (ast_package.parentPackage != null){
                sb.insert(0, ast_package.id + "::");
                ast_package = ast_package.parentPackage;
            }
        }
        sb.append("jw4a_" + ast_class.id);
        return sb.toString();
    }

    private void BuildClassHolders(AST_Class ast_class, int level) {
        PrintTabs(headerWriter, level);
        headerWriter.println("class " + BuildClassNameForCpp(ast_class) + ";");
    }

    private void BuildClassDeclarations(AST_Class ast_class, int level) {
        PrintTabs(headerWriter, level);
        headerWriter.print("class " + BuildClassNameForCpp(ast_class) + " : public ");

        if(!ast_class.id.equals("Object")) {
            Map<Integer, AST_Class> superClasses = ast_class.superClass;
            if (superClasses.size() != 1)
                throw new RuntimeException("Assert superClasses size != 1");
            Map.Entry<Integer, AST_Class> superClass = superClasses.entrySet().iterator().next();
            headerWriter.println(BuildClassNameForCpp(superClass.getValue(), true));
        }
        else
        {
            headerWriter.println("_jobject");
        }

        PrintTabs(headerWriter, level);
        headerWriter.println("{");

        CreateMethodIDs4Class( ast_class, level + 1);

        PrintTabs(headerWriter, level);
        headerWriter.println("};");
    }

    // TODO: RENAME
    private String buildPackageForFindClass(AST_Class ast_class){
        StringBuilder stringBuilder = new StringBuilder();
        AST_Package ast_package = ast_class.ast_package;
        while (ast_package.parentPackage != null){
            stringBuilder.insert(0, ast_package.id + "/");
            ast_package = ast_package.parentPackage;
        }
        stringBuilder.append(ast_class.id);
        String returnedValue = stringBuilder.toString();
        return  returnedValue;
    }

    //TODO: Manage Android Integer 4 APIs
    private void CreateMethodIDs4Class(AST_Class ast_class, int level) {

        Set<Map.Entry<Integer, Set<AST_Method>>> methods = ast_class.methods.entrySet();
        if(methods.size() > 0){
            Map.Entry<Integer, Set<AST_Method>> me = methods.iterator().next();
            Set<AST_Method> set = me.getValue();
            if(set.size() > 0)
            {
                String className = BuildClassNameForCpp(ast_class);

                // Header
                PrintTabs(headerWriter, level);
                headerWriter.println("public:");
                PrintTabs(headerWriter, level+1);
                headerWriter.println("static void StaticInit(JNIEnv * pEnv);");

                // Source
                PrintTabs(sourceWriter, level);
                sourceWriter.print("void " );
                sourceWriter.print(className);
                sourceWriter.print("::");
                sourceWriter.println("StaticInit(JNIEnv * pEnv)");
                PrintTabs(sourceWriter, level);
                sourceWriter.println("{");
                PrintTabs(sourceWriter, level+1);
                sourceWriter.println("jclass " + className + "_JC = pEnv -> FindClass(\"" + buildPackageForFindClass(ast_class) + "\");");


                // Header & Source StaticInit
                PrintTabs(headerWriter, level);
                headerWriter.println("private:");

                for(AST_Method m : set)
                {
                    String methodID = buildIDForMethod(ast_class, m);

                    // Header
                    PrintTabs(headerWriter, level+1);
                    headerWriter.print("static jmethodID ");
                    headerWriter.print(methodID);
                    headerWriter.println(";");

                    // Source
                    PrintTabs(sourceWriter, level+1);
                    sourceWriter.print(methodID + " = pEnv -> GetMethodID(" + className + "_JC,\"" + m.id + "\",");
                    sourceWriter.println( "\"" + buildMethodSignature(m) + "\");");
                }

                PrintTabs(sourceWriter, level+1);
                sourceWriter.println("pEnv->DeleteLocalRef(" + className +"_JC);");
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

    private String buildMethodSignature(AST_Method m) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        Iterator<AST_AbstractParameter> it = m.parameters.iterator();
        while(it.hasNext()){
            AST_AbstractParameter param = it.next();
            sb.append(buildJNISignatureForParameter(param));
        }
        sb.append(")");
        sb.append(buildJNISignatureForReturn(m.returnDesc));
        return sb.toString();
    }

    private String buildJNISignatureForParameter(AST_AbstractParameter param) {
        if(param instanceof AST_PrimitiveParameter)
        {
            AST_PrimitiveType primi = ((AST_PrimitiveParameter) param).primitiveType;
            return buildJNISignatureForPrimitive(primi);
        }
        else if(param instanceof AST_ClassParameter)
        {
            AST_Class classp = ((AST_ClassParameter) param).astClass;
            return buildJNISignatureForClass(classp);
        }
        else
        {
            throw new RuntimeException("Unimplemented AST_AbstractParameter on method WrapperCodeGenerator::buildJNISignatureForParameter");
        }
    }

    private String buildJNISignatureForReturn(AST_AbstractMethodReturn returnDesc) {
        if(returnDesc instanceof AST_PrimitiveTypeMethodReturn)
        {
            AST_PrimitiveType primi = ((AST_PrimitiveTypeMethodReturn) returnDesc).primitiveType;
            return buildJNISignatureForPrimitive(primi);
        }
        else if(returnDesc instanceof AST_ClassMethodReturn)
        {
            AST_Class classp = ((AST_ClassMethodReturn) returnDesc).astClass;
            return buildJNISignatureForClass(classp);
        }
        else  if(returnDesc instanceof AST_VoidMethodReturn)
        {
            return  "V";
        }
        else
        {
            throw new RuntimeException("Unimplemented AST_AbstractMethodReturn on method WrapperCodeGenerator::buildJNISignatureForReturn");
        }
    }

    private String buildJNISignatureForPrimitive(AST_PrimitiveType primi) {
        switch (primi)
        {
            case BOOLEAN: return "Z";
            case BYTE: return "B";
            case CHAR: return "C";
            case DOUBLE: return "D";
            case FLOAT: return "F";
            case INT: return "I";
            case LONG: return "J";
            case SHORT: return "S";
            //object       L
            //void         V
            //array        [
            default:
                throw new RuntimeException("Unimplemented AST_PrimitiveParameter on method WrapperCodeGenerator::buildJNISignatureForPrimitive");
        }
    }

    private String buildJNISignatureForClass(AST_Class classp) {
        StringBuilder sb = new StringBuilder();
        sb.append("L");
        sb.append(buildPackageForFindClass(classp));
        sb.append(";");
        return sb.toString();
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
