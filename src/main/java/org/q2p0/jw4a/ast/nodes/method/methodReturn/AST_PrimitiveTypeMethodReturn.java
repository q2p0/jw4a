package org.q2p0.jw4a.ast.nodes.method.methodReturn;

import org.q2p0.jw4a.ast.nodes.AST_PrimitiveType;

public class AST_PrimitiveTypeMethodReturn extends AST_AbstractMethodReturn {

    public AST_PrimitiveType primitiveType;

    public AST_PrimitiveTypeMethodReturn(AST_PrimitiveType primitiveType) {
        this.primitiveType = primitiveType;
    }

    @Override public String toString() {
        return primitiveType.toString();
    }

}
