package org.q2p0.jw4a.ast;

import org.q2p0.jw4a.ast.JObjectsTree.PackageNode;
import org.q2p0.jw4a.ast.nodes.AST_Class;

import java.util.HashMap;
import java.util.Map;

public class Description {

    public PackageNode packageTree = new PackageNode(null, null);
    public Map<String, AST_Class> classDescriptions = new HashMap<String, AST_Class>();

}
