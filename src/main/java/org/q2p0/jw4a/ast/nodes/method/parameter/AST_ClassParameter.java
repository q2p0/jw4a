package org.q2p0.jw4a.ast.nodes.method.parameter;

import org.q2p0.jw4a.ast.JObjectsTree.ClassNode;

public class AST_ClassParameter extends AST_AbstractParameter {
    public ClassNode classNode;

    public AST_ClassParameter(ClassNode classNode) {
        this.classNode = classNode;
    }
}
