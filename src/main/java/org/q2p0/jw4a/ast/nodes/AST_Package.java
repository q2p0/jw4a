package org.q2p0.jw4a.ast.nodes;

import org.q2p0.jw4a.util.DottedString;
import org.q2p0.jw4a.util.HashMapSetGet;
import org.q2p0.jw4a.util.SetGet;

import java.util.*;

public class AST_Package {

    public String id;
    public String packagePath;
    public AST_Package parentPackage;

    public SetGet< AST_Package > subPackages;
    public SetGet< AST_Class > classes; //TODO: Change to AST_PackageSons

    public AST_Package(AST_Package parentPackage, String id) {

        this.id = id;
        this.parentPackage = parentPackage;

        if(parentPackage != null)
            this.packagePath = parentPackage.packagePath != null ? String.join(".", parentPackage.packagePath, id) : id;

        this.subPackages = new HashMapSetGet<>();
        this.classes = new HashMapSetGet<>();
    }

    // HashCode & Equals with (packagePath) field.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AST_Package that = (AST_Package) o;
        return Objects.equals(packagePath, that.packagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packagePath);
    }
}
