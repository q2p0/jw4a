package org.q2p0.jw4a.ast.nodes;

import org.q2p0.jw4a.ast.AST_TreeComparableNode;
import org.q2p0.jw4a.util.HashMapSetGet;
import org.q2p0.jw4a.util.SetGet;

import java.util.*;

public class AST_Package implements AST_TreeComparableNode {

    // Constructor & public fields.

    public String id;
    public String packagePath;
    public AST_Package parentPackage;
    public AST_Package(AST_Package parentPackage, String id) {

        this.id = id;
        this.parentPackage = parentPackage;

        if(parentPackage != null)
            this.packagePath = parentPackage.packagePath != null ? String.join(".", parentPackage.packagePath, id) : id;

        this.subPackages = new HashMapSetGet<>();
        this.classes = new HashMapSetGet<>();
    }

    public SetGet< AST_Package > subPackages;
    public SetGet< AST_Class > classes; //TODO: Change to subClasses

    // HashCode & Equals with (packagePath) field.

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AST_Package that = (AST_Package) o;
        return Objects.equals(packagePath, that.packagePath);
    }

    @Override public int hashCode() {
        return Objects.hash(packagePath);
    }

    // TreeEquals

    @Override public boolean treeEquals(AST_TreeComparableNode node) {
        if( !equals(node) ) return false;
        AST_Package p = (AST_Package) node;
        if( !Objects.equals( id, p.id) ) return false;
        if( !AST_TreeComparableNode.treeEquals( subPackages, p.subPackages ) ) return false;
        if( !AST_TreeComparableNode.treeEquals( classes, p.classes ) ) return false;
        return true;
    }

    // One line descriptive string 4 development & AST_TreePrint.
    @Override public String toString() {
        return id!=null? id : "";
    }

}
