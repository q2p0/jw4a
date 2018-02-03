package org.q2p0.jw4a.ast.nodes.methodReturn;

import org.q2p0.jw4a.ast.JObjectsTree.ClassNode;

public class ClassMethodReturnDesc extends AbstractMethodReturnDesc{
    public ClassNode classNode;
    public ClassMethodReturnDesc(ClassNode classNode) {
        this.classNode = classNode;
    }
}
