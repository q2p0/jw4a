package org.q2p0.jw4a.ast.nodes;

import org.q2p0.jw4a.ast.JObjectsTree.ClassNode;

import java.util.ArrayList;

public class AST_Class {
    public String id;
    public ClassNode cn;
    public ArrayList<AST_Method> methods;
    public AST_Class(String id, ClassNode cn, ArrayList<AST_Method> methods) {
        this.id = id;
        this.cn = cn;
        this.methods = methods;
    }
}
