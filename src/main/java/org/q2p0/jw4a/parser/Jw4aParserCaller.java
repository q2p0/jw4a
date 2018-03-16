package org.q2p0.jw4a.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.q2p0.jw4a.reflection.ReflectionPaths;

public class Jw4aParserCaller {

    private CharStream input;
    private ReflectionPaths paths;

    public Jw4aParserCaller(CharStream input, ReflectionPaths paths) {
        this.input = input;
        this.paths = paths;
    }

    //TODO: Return AST
    public void parse() {

        Jw4aLexer lexer = new Jw4aLexer( this.input );
        CommonTokenStream tokenStream = new CommonTokenStream( lexer );
        Jw4aParser parser = new Jw4aParser( tokenStream );

        // Stop the parse process at first syntax error.
        parser.removeErrorListeners();
        parser.addErrorListener(new ParserErrorListener());

        try {
            parser.wrappers( this.paths );
        }catch (Exception e){
            System.err.println( e.getMessage() );
            return; //TODO: Add an error exit code
        }
    }

}
