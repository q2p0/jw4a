package org.q2p0.jw4a.ast.nodes.method;

import org.q2p0.jw4a.ast.nodes.method.methodReturn.AST_AbstractMethodReturn;
import org.q2p0.jw4a.ast.nodes.method.parameter.AST_AbstractParameter;

import java.util.List;

public class AST_Method {

    public AST_AbstractMethodReturn returnDesc;
    public String id;
    public List<AST_AbstractParameter> parameters;

    public AST_Method(AST_AbstractMethodReturn returnDesc, String id, List<AST_AbstractParameter> parameters) {
        this.returnDesc = returnDesc;
        this.id = id;
        this.parameters = parameters;
    }
}
