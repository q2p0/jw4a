package org.q2p0.jw4a.ast.nodes.method.parameter;

import org.q2p0.jw4a.ast.nodes.AST_Class;

import java.util.Objects;

public class AST_ClassParameter extends AST_AbstractParameter {

    public AST_Class astClass;

    public AST_ClassParameter(AST_Class astClass) {
        this.astClass = astClass;
    }

    @Override public String toString() {
        return astClass.id + " " + id;
    }

    // HashCode with (AST_ClassParameter.class, astClass) fields & Equals with (astClass) field.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AST_ClassParameter that = (AST_ClassParameter) o;
        return Objects.equals(astClass, that.astClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(AST_ClassParameter.class, astClass);
    }
}
