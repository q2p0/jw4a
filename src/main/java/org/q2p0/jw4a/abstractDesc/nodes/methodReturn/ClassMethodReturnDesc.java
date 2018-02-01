package org.q2p0.jw4a.abstractDesc.nodes.methodReturn;

import org.q2p0.jw4a.abstractDesc.JObjectsTree.ClassNode;

public class ClassMethodReturnDesc extends AbstractMethodReturnDesc{
    public ClassNode classNode;
    public ClassMethodReturnDesc(ClassNode classNode) {
        this.classNode = classNode;
    }
}
