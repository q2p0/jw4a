package org.q2p0.jw4a.ast.nodes.method.parameter;

import org.q2p0.jw4a.ast.nodes.AST_PrimitiveType;

import java.util.Objects;

public class AST_PrimitiveParameter extends AST_AbstractParameter {

    public AST_PrimitiveType primitiveType;

    public AST_PrimitiveParameter(AST_PrimitiveType primitiveType) {
        this.primitiveType = primitiveType;
    }

    @Override public String toString() {
        return primitiveType.toString() + " " + id;
    }

    // HashCode with (AST_ClassParameter.class, astClass) fields & Equals with (astClass) field.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AST_PrimitiveParameter that = (AST_PrimitiveParameter) o;
        return primitiveType == that.primitiveType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(AST_PrimitiveParameter.class, primitiveType);
    }
}
