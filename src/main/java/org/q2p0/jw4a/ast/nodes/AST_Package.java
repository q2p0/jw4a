package org.q2p0.jw4a.ast.nodes;

public class AST_Package {

    public String id;

    public AST_Package parentPackage;

    public AST_Package(AST_Package parentPackage) {
        this.parentPackage = parentPackage;
    }

}
