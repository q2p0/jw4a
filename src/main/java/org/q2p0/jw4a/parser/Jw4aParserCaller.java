package org.q2p0.jw4a.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.q2p0.jw4a.ast.AST_TreePrint;
import org.q2p0.jw4a.ast.nodes.AST_Package;
import org.q2p0.jw4a.reflection.ReflectionPaths;

public class Jw4aParserCaller {

    private CharStream input;
    private ReflectionPaths paths;

    public Jw4aParserCaller(CharStream input, ReflectionPaths paths) {
        this.input = input;
        this.paths = paths;
    }

    //TODO: Return AST
    public AST_Package parse() {

        Jw4aLexer lexer = new Jw4aLexer( this.input );
        CommonTokenStream tokenStream = new CommonTokenStream( lexer );
        Jw4aParser parser = new Jw4aParser( tokenStream );

        // Stop the parse process at first syntax error.
        parser.removeErrorListeners();
        parser.addErrorListener(new ParserErrorListener());

        try {
            //AST_Package p = parser.wrappers( this.paths ).root;
            //AST_TreePrint.print( p, 2 );
            return parser.wrappers( this.paths ).root;
        }catch (Exception e){
            System.err.println( e.getMessage() );
            return null; //TODO: Add an error exit code
        }
    }

}
