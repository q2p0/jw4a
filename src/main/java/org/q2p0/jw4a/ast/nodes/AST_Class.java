package org.q2p0.jw4a.ast.nodes;

import org.q2p0.jw4a.ast.nodes.method.AST_Method;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AST_Class {

    // Constructor & public fields.

    public String id;
    public AST_Package ast_package; //TODO: When nested classes will come to jw4a a refactor is required, nested classes are not inside a package
    public AST_Class( AST_Package ast_package, String id ) {
        this.id = id;
        this.ast_package = ast_package;
    }

    public Map< Integer, Class > apiReflectionClasses;
    public List<AST_Method> methods = new ArrayList<>(); //TODO: Change to Map< Integer, AST_Method>
    //TODO: public Map< Integer, AST_Class > superClass;

    // HashCode & Equals with (id, ast_package) field.

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AST_Class ast_class = (AST_Class) o;
        return Objects.equals(id, ast_class.id) &&
                Objects.equals(ast_package, ast_class.ast_package);
    }

    @Override public int hashCode() {
        return Objects.hash(id, ast_package);
    }

    // One line descriptive string 4 development & AST_TreePrint.
    @Override public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Class ");
        builder.append(id);
        builder.append(" [");
        Stream<String> apis = apiReflectionClasses.keySet().stream().sorted().map( s->String.valueOf(s) );
        builder.append( String.join(",", apis.collect( Collectors.toList() )));
        builder.append("]");
        return builder.toString();
    }
}
