package org.q2p0.jw4a.ast.nodes.method;

import org.q2p0.jw4a.ast.nodes.AST_Class;
import org.q2p0.jw4a.ast.nodes.method.methodReturn.AST_AbstractMethodReturn;
import org.q2p0.jw4a.ast.nodes.method.parameter.AST_AbstractParameter;
import org.q2p0.jw4a.ast.nodes.method.parameter.AST_ClassParameter;
import org.q2p0.jw4a.ast.nodes.method.parameter.AST_PrimitiveParameter;
import org.q2p0.jw4a.util.CollectionUtil;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class AST_Method {

    // Constructor & public fields.

    public AST_AbstractMethodReturn returnDesc;
    public String id;
    public List<AST_AbstractParameter> parameters;
    public AST_Method(AST_AbstractMethodReturn returnDesc, String id, List<AST_AbstractParameter> parameters) {
        this.returnDesc = returnDesc;
        this.id = id;
        this.parameters = parameters;
    }

    // One line descriptive string 4 development & AST_TreePrint.
    @Override public String toString() {
        return toString(null);
    }
    public String toString(Set<Integer> apiRanges) {
        StringBuilder builder = new StringBuilder();

        builder.append( this.returnDesc );
        builder.append( ' ' );
        builder.append( id );
        if( apiRanges != null ) {
            builder.append("[");
            builder.append( CollectionUtil.toRangeString(apiRanges) );
            builder.append("]");
        }
        builder.append( "(" );

        List<String> parametersStr = parameters.stream().map( s->String.valueOf(s) ).collect( Collectors.toList() );
        builder.append( String.join(", ", parametersStr) );

        builder.append(");");

        return builder.toString();
    }

    // HashCode & Equals with (id, parameters) fields.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AST_Method method = (AST_Method) o;
        return Objects.equals(id, method.id) &&
                Objects.equals(parameters, method.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parameters);
    }

    // Get an array of Class objects for find method on reflection

    public Class[] getParameterArray(int api){

        Class classParameters[] = new Class[parameters.size()];
        for(int i=0; i<classParameters.length; i++) {
            AST_AbstractParameter ast_abstractParameter = parameters.get(i);
            if( ast_abstractParameter instanceof AST_ClassParameter ){
                AST_ClassParameter ast_classParameter = (AST_ClassParameter) ast_abstractParameter;
                AST_Class ast_class = ast_classParameter.astClass;
                Class refClass = ast_class.apiReflectionClasses.get( api );
                if(refClass==null)
                    throw new RuntimeException("Unimplemented class parameter don't have an reflection class object");
                classParameters[i] = refClass;
            } else if( ast_abstractParameter instanceof AST_PrimitiveParameter ) {
                AST_PrimitiveParameter ast_primitiveParameter = (AST_PrimitiveParameter) ast_abstractParameter;
                classParameters[i] = ast_primitiveParameter.primitiveType.toClass();
            } else {
                throw new RuntimeException("Unimplemented AST_AbstractParameter method parameter check");
            }
        }

        return classParameters;

    }


}
