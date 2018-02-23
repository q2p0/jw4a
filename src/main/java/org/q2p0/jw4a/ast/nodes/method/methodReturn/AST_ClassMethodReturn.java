package org.q2p0.jw4a.ast.nodes.method.methodReturn;

import org.q2p0.jw4a.ast.nodes.AST_Class;

public class AST_ClassMethodReturn extends AST_AbstractMethodReturn {

    public AST_Class astClass;

    public AST_ClassMethodReturn(AST_Class astClass) {
        this.astClass = astClass;
    }

    @Override public String toString() {
        return this.astClass.id;
    }

}
