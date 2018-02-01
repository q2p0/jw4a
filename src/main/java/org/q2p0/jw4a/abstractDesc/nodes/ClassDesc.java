package org.q2p0.jw4a.abstractDesc.nodes;

import org.q2p0.jw4a.abstractDesc.JObjectsTree.ClassNode;

import java.util.ArrayList;

public class ClassDesc {
    public String id;
    public ClassNode cn;
    public ArrayList<MethodDesc> methods;
    public ClassDesc(String id, ClassNode cn, ArrayList<MethodDesc> methods) {
        this.id = id;
        this.cn = cn;
        this.methods = methods;
    }
}
