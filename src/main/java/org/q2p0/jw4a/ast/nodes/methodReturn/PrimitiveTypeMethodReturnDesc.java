package org.q2p0.jw4a.ast.nodes.methodReturn;

import org.q2p0.jw4a.ast.nodes.AST_PrimitiveType;

public class PrimitiveTypeMethodReturnDesc extends AbstractMethodReturnDesc{
    public AST_PrimitiveType primitiveType;

    public PrimitiveTypeMethodReturnDesc(AST_PrimitiveType primitiveType) {
        this.primitiveType = primitiveType;
    }
}
