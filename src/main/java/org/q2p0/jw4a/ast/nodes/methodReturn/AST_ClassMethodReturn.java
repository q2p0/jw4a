package org.q2p0.jw4a.ast.nodes.methodReturn;

import org.q2p0.jw4a.ast.JObjectsTree.ClassNode;

public class AST_ClassMethodReturn extends AST_AbstractMethodReturn {
    public ClassNode classNode;
    public AST_ClassMethodReturn(ClassNode classNode) {
        this.classNode = classNode;
    }
}
