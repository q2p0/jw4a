package org.q2p0.jw4a;

import org.antlr.v4.runtime.*;
import org.q2p0.jw4a.ast.AST_Builder;
import org.q2p0.jw4a.parserOptions.CommandLineParameterParserOptions;
import org.q2p0.jw4a.parser.ParserErrorListener;
import org.q2p0.jw4a.parser.jw4aLexer;
import org.q2p0.jw4a.parser.jw4aParser;
import org.q2p0.jw4a.reflection.ReflectionHelper;

import java.io.IOException;

public class Jw4a {

    //TODO: Change Jw4aLists extension from txt to jw4a

    public static void main(String[] args) {

        System.out.println();

        //TODO: Change to parameter initialization singleton
        CommandLineParameterParserOptions clparser = new CommandLineParameterParserOptions();
        //TODO: Add an method that return an Class that describe the command line arguments instead of passing the CLParameters class
        clparser.parseArgs( args );
        //ReflectionHelperOptions params = ;

        CharStream inputCharStream = null;
        try { inputCharStream = CharStreams.fromFileName( clparser.definitionFile ); }
        catch (IOException e) { e.printStackTrace(); return;}

        jw4aLexer lexer = new jw4aLexer( inputCharStream );
        CommonTokenStream tokenStream = new CommonTokenStream( lexer );
        jw4aParser parser = new jw4aParser( tokenStream );

        // Stop the parse process at first syntax error.
        parser.removeErrorListeners();
        parser.addErrorListener(new ParserErrorListener());

        try {
            ReflectionHelper reflectionHelper = new ReflectionHelper( clparser.getReflectionHelperOptions() );
            AST_Builder ast_builder = new AST_Builder( reflectionHelper );
            parser.wrappers( ast_builder );
        }catch (Exception e){
            //TODO: Show pre-error message.
            System.err.println( e.getMessage() );
            return;
        }

        //TODO: Get the AST and call CodeGenerator
        System.out.println( );
        System.out.println( "Parser ends successfully");
    }


}
