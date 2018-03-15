package org.q2p0.jw4a.parser;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.ResourceBundle;

public class Jw4aParseException extends ParseCancellationException {

    private String detailMessage;

    public Jw4aParseException(String message, int line, int charPositionInLine){
        fillInStackTrace();
        StringBuilder sb = new StringBuilder();
        ResourceBundle bundle = ResourceBundle.getBundle("org.q2p0.jw4a.parser.Jw4aParseException");
        sb.append( String.format( bundle.getString("line"), line, charPositionInLine) );
        sb.append( ' ' );
        sb.append( message );
        detailMessage = sb.toString();
    }

    public Jw4aParseException(String message, Token token){
        this(message, token.getLine(), token.getCharPositionInLine());
    }

    //TODO: If needed Ctor for RuleContext

    @Override public String getMessage() {
        return detailMessage;
    }

}
