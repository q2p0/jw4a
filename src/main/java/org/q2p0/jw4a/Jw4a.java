package org.q2p0.jw4a;

import org.antlr.v4.runtime.*;
import org.q2p0.jw4a.parserOptions.CommandLineParameterParserOptions;
import org.q2p0.jw4a.parser.ParserErrorListener;
import org.q2p0.jw4a.parser.Jw4aLexer;
import org.q2p0.jw4a.parser.Jw4aParser;

import java.io.IOException;

public class Jw4a {

    public static void main(String[] args) {

        CommandLineParameterParserOptions clparser = new CommandLineParameterParserOptions();
        clparser.parseArgs( args );

        //TODO: Add input interface
        CharStream inputCharStream = null;
        try { inputCharStream = CharStreams.fromFileName( clparser.definitionFile ); }
        catch (IOException e) { e.printStackTrace(); return;}

        Jw4aLexer lexer = new Jw4aLexer( inputCharStream );
        CommonTokenStream tokenStream = new CommonTokenStream( lexer );
        Jw4aParser parser = new Jw4aParser( tokenStream );

        // Stop the parse process at first syntax error.
        parser.removeErrorListeners();
        parser.addErrorListener(new ParserErrorListener());

        try {
            parser.wrappers( clparser.getReflectionHelperOptions() );
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
