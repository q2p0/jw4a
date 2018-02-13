package org.q2p0.jw4a.ast.JObjectsTree;

public class ClassNode extends AbstractNode {

    public ClassNode(String id, AbstractNode parent) {
        super(id, parent);
    }

    @Override public AbstractNode addNode(String s) {
        throw new RuntimeException("Subclasses are still not supported");
    }

}
