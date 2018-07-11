package org.q2p0.jw4a.parser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.ResourceBundle;

import static org.q2p0.jw4a.util.ResourceBundle.getBundle4Class;

public class Jw4aParseException extends ParseCancellationException {

    ResourceBundle resources = getBundle4Class( Jw4aParseException.class );

    // Exception Message
    private String detailMessage;
    @Override public String getMessage() {
        return detailMessage;
    }

    // Init 4 Token constructors
    public void initToken(String message, int line, int charPositionInLine){
        fillInStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append( String.format( resources.getString("error_at"), line, charPositionInLine) );
        sb.append( '\n' );
        sb.append( message );
        detailMessage = sb.toString();
    }

    // Init 4 ParserRuleContext constructors
    public void initRule(String message, ParserRuleContext rule){
        fillInStackTrace();
        StringBuilder sb = new StringBuilder();
        Token startToken = rule.getStart();
        Token endToken = rule.getStop();
        sb.append( String.format( resources.getString("error_from_to"), startToken.getLine(), startToken.getStartIndex(), endToken.getLine(), endToken.getStopIndex() ) );
        sb.append( '\n' );
        sb.append( message );
        detailMessage = sb.toString();
    }

    // ParserErrorListener constructor
    public Jw4aParseException(String message, int line, int charPositionInLine){
        initToken(message, line, charPositionInLine);
    }

    // Token constructors

    public Jw4aParseException(String message, Token token){
        initToken(message, token.getLine(), token.getCharPositionInLine());
    }

    public Jw4aParseException(Throwable cause, Token token) {
        super(cause);
        initToken(cause.getMessage(), token.getLine(), token.getCharPositionInLine());
    }

    // Rule constructors

    //TODO: Maybe passing ParserRuleContext is to tricky on grammar and must be pass first and las token.
    public Jw4aParseException(Throwable cause, ParserRuleContext rule) {
        super(cause);
        initRule(cause.getMessage(), rule);
    }

}
