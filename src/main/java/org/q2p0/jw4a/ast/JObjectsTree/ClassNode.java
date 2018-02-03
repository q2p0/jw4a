package org.q2p0.jw4a.ast.JObjectsTree;

import java.util.ArrayList;

public class ClassNode extends AbstractNode {

    public ArrayList<PairClassApi> pairClassApi;

    public ClassNode(String id, ArrayList<PairClassApi> pca) {
        super(id);
        pairClassApi = pca;
    }

    @Override public AbstractNode addNode(String s, ArrayList<PairClassApi> pca) {
        throw new RuntimeException("Subclasses are still not supported");
    }

}
