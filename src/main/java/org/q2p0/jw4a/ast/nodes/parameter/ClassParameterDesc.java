package org.q2p0.jw4a.ast.nodes.parameter;

import org.q2p0.jw4a.ast.JObjectsTree.ClassNode;

public class ClassParameterDesc extends AbstractParameterDesc {
    public ClassNode classNode;

    public ClassParameterDesc(ClassNode classNode) {
        this.classNode = classNode;
    }
}
