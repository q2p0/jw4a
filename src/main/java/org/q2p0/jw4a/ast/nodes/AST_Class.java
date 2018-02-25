package org.q2p0.jw4a.ast.nodes;

import org.q2p0.jw4a.ast.nodes.method.AST_Method;
import org.q2p0.jw4a.util.CollectionUtil;

import java.util.*;
import java.util.stream.Collectors;

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
    public Map< Integer, AST_Class > superClass = new HashMap<>();

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
        builder.append( CollectionUtil.toRangeString(superClass.keySet()) );
        builder.append("]");

        Set<AST_Class> superClassesSet = new HashSet<>( superClass.values() );
        AST_Class superClassesArray[] = superClassesSet.toArray( new AST_Class[superClassesSet.size()] );
        if( superClassesArray.length > 0 ) {
            builder.append(" extends");
            for(int i=0; i<superClassesArray.length; i++) {

                builder.append(" ");
                builder.append(superClassesArray[i].id );
                builder.append(" [");

                final int lambda_i = i;
                List<Integer> apis = superClass.entrySet().stream().filter(m->superClassesArray[lambda_i].equals(m.getValue())).map(m->m.getKey()).collect(Collectors.toList());
                String apis_str = CollectionUtil.toRangeString( apis );
                builder.append( apis_str );

                builder.append("]");
            }
        }

        return builder.toString();
    }
}
