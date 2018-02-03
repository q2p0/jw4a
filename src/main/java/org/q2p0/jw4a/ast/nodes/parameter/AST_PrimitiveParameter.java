package org.q2p0.jw4a.ast.nodes.parameter;

import org.q2p0.jw4a.ast.nodes.AST_PrimitiveType;

public class AST_PrimitiveParameter extends AST_AbstractParameter {
    public AST_PrimitiveType primitiveType;

    public AST_PrimitiveParameter(AST_PrimitiveType primitiveType) {
        this.primitiveType = primitiveType;
    }
}
