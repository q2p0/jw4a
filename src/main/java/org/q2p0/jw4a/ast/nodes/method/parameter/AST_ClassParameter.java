package org.q2p0.jw4a.ast.nodes.method.parameter;

import org.q2p0.jw4a.ast.nodes.AST_Class;

public class AST_ClassParameter extends AST_AbstractParameter {

    public AST_Class astClass;

    public AST_ClassParameter(AST_Class astClass) {
        this.astClass = astClass;
    }

    @Override public String toString() {
        return astClass.id + " " + id;
    }

}
