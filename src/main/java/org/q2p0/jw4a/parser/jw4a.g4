grammar jw4a; //TODO: Change grammar to Jw4a

//TODO WARNING: Test with Classes that don't belong to any package
//TODO: Create one TODO.TXT file for big goals
//TODO: Compile and check on WINDOWS SO

@parser::header{

    import org.q2p0.jw4a.ReflectionManager;
    import org.q2p0.jw4a.CLParameters;

    import org.q2p0.jw4a.ast.*;
    import org.q2p0.jw4a.ast.nodes.*;
    import org.q2p0.jw4a.ast.nodes.parameter.*;
    import org.q2p0.jw4a.ast.nodes.methodReturn.*;
    import org.q2p0.jw4a.ast.JObjectsTree.*;

    import org.q2p0.jw4a.generator.*;

    import java.lang.StringBuilder;

}

@parser::members{

    ReflectionManager reflection = ReflectionManager.GetInstance();
    Description description = new Description();
    CodeGenerator codeGenerator = new WrapperCodeGenerator();
}

wrappers :
    package_description*
    {
        System.out.println();
        codeGenerator.generate( description );
    }
;

// package_description: dotted_string BRACKET_OPEN ( package_description | class_description )* BRACKET_CLOSE;
package_description:
    dotted_string
    BRACKET_OPEN
    (
            package_description
            {
                if("TRUE".equals("TRUE"))
                    throw new RuntimeException("Implement subpackages");
            }
        |
            class_description[ $dotted_string.text ]
    )*
    BRACKET_CLOSE
;

// class_description: CLASS ID BRACKET_OPEN method* BRACKET_CLOSE;
class_description [String _package] :
    CLASS
    {
        AST_Class cd = null;
        ClassNode classNode = null;
        ArrayList<AST_Method> methods = new ArrayList<AST_Method>();
    }
    ID
    {
        String classID = $ID.text;
        String fullClassPath = $_package + "." + $ID.text;
        ArrayList<PairClassApi> references = reflection.existClass( fullClassPath );
        if( references != null ) {
            classNode = (ClassNode) description.packageTree.addNode( fullClassPath, references );
        } else {
            //TODO: Show an message: Class has not been found, no wrappers will be constructed for line, colum class description
            //TODO: Rule must return null
        }
    }
    BRACKET_OPEN
    (
        method
        {
            if( $method.value != null )
                methods.add( $method.value );
        }
    )*
    BRACKET_CLOSE
    {
        //TODO: if classNode is null
        //TODO: if methods is empty
        cd = new AST_Class( classID, classNode, methods );
        description.classDescriptions.add( cd );
    }
;

// method: ( dotted_string | PRIMITIVE_TYPE | VOID ) ID PARENTHESIS_OPEN parameter* PARENTHESIS_CLOSE SEMICOLON;
method returns [AST_Method value]:
    {
        AST_AbstractMethodReturn returnDesc = null;
    }
    (
        dotted_string
            {
                ArrayList<PairClassApi> references = reflection.existClass( $dotted_string.text );
                if( references != null ) {
                    ClassNode classNode = (ClassNode) description.packageTree.addNode( $dotted_string.text, references );
                    returnDesc = new AST_ClassMethodReturn( classNode );
                } else {
                    //TODO: Show an message: Class has not been found, no wrappers will be constructed for line, colum class description
                    //TODO: Rule must return null
                }
            }
        |
        PRIMITIVE_TYPE
            {
                returnDesc = new AST_PrimitiveTypeMethodReturn( AST_PrimitiveType.parse($PRIMITIVE_TYPE.text) );
            }
        |
        VOID
            {
                returnDesc = new AST_VoidMethodReturn();
            }
    )
    ID
    {
        String id = $ID.text;
    }
    PARENTHESIS_OPEN
    {
        ArrayList<AST_AbstractParameter> parameters = new ArrayList<AST_AbstractParameter>();
    }
    (
        parameter
        {
            parameters.add( $parameter.value );
        }
    )*
    PARENTHESIS_CLOSE
    {
        //TODO: If returnDesc != null
        //TODO: parameters != null
        $value = new AST_Method( returnDesc, id, parameters);
    }
    SEMICOLON
;

// parameter : ( dotted_string | PRIMITIVE_TYPE ) ID ;
parameter returns [AST_AbstractParameter value]: //
    (
        dotted_string
        {
            ArrayList<PairClassApi> references = reflection.existClass( $dotted_string.text );
            if( references != null ) {
                ClassNode classNode = (ClassNode) description.packageTree.addNode( $dotted_string.text, references );
                $value = new AST_ClassParameter( classNode );
            } else {
                //TODO: Show an message: Class has not been found, no wrappers will be constructed for line, colum class description
                //TODO: Rule must return null
            }
        }
    |
        PRIMITIVE_TYPE
        {
            $value = new AST_PrimitiveParameter( AST_PrimitiveType.parse($PRIMITIVE_TYPE.text) );
        }
    )
    ID
    {
        $value.id = $ID.text;
    }
;

dotted_string : (ID DOT)* ID;

CLASS : 'class';

PRIMITIVE_TYPE : 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' | 'char' | 'boolean';
VOID: 'void';

DOT: '.';
BRACKET_OPEN: '{';
BRACKET_CLOSE: '}';
SEMICOLON: ';';
PARENTHESIS_OPEN: '(';
PARENTHESIS_CLOSE: ')';

ID: [a-zA-Z_][a-zA-Z0-9_]*;
WS: [ \t\n\r]+ -> skip;

COMMENT : '/*' .*? '*/' -> skip;
LINE_COMMENT : '//' ~[\r\n]* -> skip;
