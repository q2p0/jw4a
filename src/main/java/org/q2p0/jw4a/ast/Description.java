package org.q2p0.jw4a.ast;

import org.q2p0.jw4a.ast.JObjectsTree.PackageNode;
import org.q2p0.jw4a.ast.nodes.AST_Class;

import java.util.ArrayList;

public class Description {

    public PackageNode packageTree = new PackageNode(null);
    public ArrayList<AST_Class> classDescriptions = new ArrayList<AST_Class>();

}
