package org.q2p0.jw4a.ast.JObjectsTree;

import java.util.ArrayList;

//TODO: Unneded class delete, or maybe when JW4A support nested classes
public abstract class AbstractNode {
    private String id;
    AbstractNode parent;
    AbstractNode(String id, AbstractNode parent) { this.id = id; this.parent = parent; }
    public abstract AbstractNode addNode(String s, ArrayList<PairClassApi> pca);
}
