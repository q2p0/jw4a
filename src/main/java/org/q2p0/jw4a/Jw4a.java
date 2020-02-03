package org.q2p0.jw4a;

import org.antlr.v4.runtime.*;
import org.q2p0.jw4a.ast.AST_TreePrint;
import org.q2p0.jw4a.ast.nodes.AST_Package;
import org.q2p0.jw4a.generator.CodeGenerator;
import org.q2p0.jw4a.generator.WrapperCodeGenerator;
import org.q2p0.jw4a.parser.Jw4aParserCaller;
import org.q2p0.jw4a.parserOptions.CommandLineParameterParserOptions;

import java.io.IOException;

public class Jw4a { //TODO: Rename to Jw4aMain

    public static void main(String[] args) {

        CommandLineParameterParserOptions clparser = new CommandLineParameterParserOptions();
        clparser.parseArgs( args );

        CharStream inputCharStream = null;
        try { inputCharStream = CharStreams.fromFileName( clparser.definitionFile ); }
        catch (IOException e) {
            //TODO: Show an error
            e.printStackTrace();
            return; //TODO: Add an error exit code
        }

        AST_Package ast_root = null;
        try {
            ast_root =  new Jw4aParserCaller( inputCharStream, clparser.getReflectionPaths() ).parse();
        }catch (Exception e){
            System.err.println( e.getMessage() );
            System.exit(ExitErrorCodes.PARSER_ERROR);
        }

        AST_TreePrint.print( ast_root, 2 );

        CodeGenerator codeGenerator = new WrapperCodeGenerator();
        codeGenerator.generate(ast_root, clparser.outputDirectory);

        //TODO: Get the AST and call CodeGenerator
        System.out.println( );
        System.out.println( "Parser ends successfully");
    }
}
