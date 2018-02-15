package org.q2p0.jw4a.ast.nodes;

import org.q2p0.jw4a.ast.JObjectsTree.ClassNode;
import org.q2p0.jw4a.ast.nodes.method.AST_Method;

import java.util.ArrayList;
import java.util.Map;

//TODO: Store available APIs
public class AST_Class {

    public String id;
    public ClassNode cn;

    public Map< Integer, Class > apiClasses;

    public ArrayList<AST_Method> methods;

    public AST_Class(String id, ClassNode cn, Map< Integer, Class > apiClasses) {
        this.id = id;
        this.cn = cn;
        this.apiClasses = apiClasses;
    }


}
