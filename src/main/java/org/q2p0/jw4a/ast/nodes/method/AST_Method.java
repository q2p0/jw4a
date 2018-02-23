package org.q2p0.jw4a.ast.nodes.method;

import org.q2p0.jw4a.ast.nodes.method.methodReturn.AST_AbstractMethodReturn;
import org.q2p0.jw4a.ast.nodes.method.parameter.AST_AbstractParameter;

import java.util.List;
import java.util.stream.Collectors;

public class AST_Method {

    // Constructor & public fields.

    public AST_AbstractMethodReturn returnDesc;
    public String id;
    public List<AST_AbstractParameter> parameters;
    public AST_Method(AST_AbstractMethodReturn returnDesc, String id, List<AST_AbstractParameter> parameters) {
        this.returnDesc = returnDesc;
        this.id = id;
        this.parameters = parameters;
    }

    // One line descriptive string 4 development & AST_TreePrint.
    @Override public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append( this.returnDesc );
        builder.append( ' ' );
        builder.append( id );
        builder.append( "(" );

        List<String> parametersStr = parameters.stream().map( s->String.valueOf(s) ).collect( Collectors.toList() );
        builder.append( String.join(", ", parametersStr) );

        builder.append(");");

        return builder.toString();

    }
}
