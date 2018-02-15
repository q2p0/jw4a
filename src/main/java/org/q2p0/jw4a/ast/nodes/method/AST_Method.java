package org.q2p0.jw4a.ast.nodes.method;

import org.q2p0.jw4a.ast.nodes.method.methodReturn.AST_AbstractMethodReturn;
import org.q2p0.jw4a.ast.nodes.method.parameter.AST_AbstractParameter;

import java.util.ArrayList;

public class AST_Method {
    public AST_AbstractMethodReturn returnDesc;
    public String id;
    public ArrayList<AST_AbstractParameter> parameters;

    public AST_Method(AST_AbstractMethodReturn returnDesc, String id, ArrayList<AST_AbstractParameter> parameters) {
        this.returnDesc = returnDesc;
        this.id = id;
        this.parameters = parameters;
    }
}
