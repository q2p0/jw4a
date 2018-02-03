package org.q2p0.jw4a.ast.nodes;

import org.q2p0.jw4a.ast.nodes.methodReturn.AbstractMethodReturnDesc;
import org.q2p0.jw4a.ast.nodes.parameter.AbstractParameterDesc;

import java.util.ArrayList;

public class AST_Method {
    public AbstractMethodReturnDesc returnDesc;
    public String id;
    public ArrayList<AbstractParameterDesc> parameters;

    public AST_Method(AbstractMethodReturnDesc returnDesc, String id, ArrayList<AbstractParameterDesc> parameters) {
        this.returnDesc = returnDesc;
        this.id = id;
        this.parameters = parameters;
    }
}
