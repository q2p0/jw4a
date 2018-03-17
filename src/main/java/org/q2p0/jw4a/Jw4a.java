package org.q2p0.jw4a;

import org.antlr.v4.runtime.*;
import org.q2p0.jw4a.ast.AST_TreePrint;
import org.q2p0.jw4a.ast.nodes.AST_Package;
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

        AST_Package root = new Jw4aParserCaller( inputCharStream, clparser.getReflectionPaths() ).parse();

        AST_TreePrint.print( root, 2 );

        //TODO: Get the AST and call CodeGenerator
        System.out.println( );
        System.out.println( "Parser ends successfully");
    }


}
