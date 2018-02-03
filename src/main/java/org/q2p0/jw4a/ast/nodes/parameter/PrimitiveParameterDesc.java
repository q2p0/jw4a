package org.q2p0.jw4a.ast.nodes.parameter;

import org.q2p0.jw4a.ast.nodes.AST_PrimitiveType;

public class PrimitiveParameterDesc extends AbstractParameterDesc{
    public AST_PrimitiveType primitiveType;

    public PrimitiveParameterDesc(AST_PrimitiveType primitiveType) {
        this.primitiveType = primitiveType;
    }
}
