package org.q2p0.jw4a.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.q2p0.jw4a.ExitErrorCodes;
import org.q2p0.jw4a.ast.nodes.AST_Package;
import org.q2p0.jw4a.reflection.ReflectionPaths;

public class Jw4aParserCaller {

    private CharStream input;
    private ReflectionPaths paths;

    public Jw4aParserCaller(CharStream input, ReflectionPaths paths) {
        this.input = input;
        this.paths = paths;
    }

    public AST_Package parse() {

        // Prepare the lexer and parser
        Jw4aLexer lexer = new Jw4aLexer( this.input );
        CommonTokenStream tokenStream = new CommonTokenStream( lexer );
        Jw4aParser parser = new Jw4aParser( tokenStream );

        // Stop the parse process at first syntax error.
        parser.removeErrorListeners();
        parser.addErrorListener(new ParserErrorListener());

        // Parse the input & return the AST_Tree
        return parser.wrappers( this.paths ).root;
    }

}
