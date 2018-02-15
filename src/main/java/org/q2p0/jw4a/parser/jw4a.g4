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

    import java.util.HashMap;
    import java.util.Map;
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
        ClassNode classNode = null;
        ArrayList<AST_Method> methods = new ArrayList<AST_Method>();
    }
    ID
    {
        AST_Class ast_class = description.addClass( $_package, $ID.text );
    }
    BRACKET_OPEN
    (
        method[ ast_class ]
        {
            if( $method.value != null )
                methods.add( $method.value );
        }
    )*
    BRACKET_CLOSE
    {
        //TODO: if classNode is null
        //TODO: if methods is empty
        //TODO: GetPrevious descriptions or show an error
    }
;

// method: ( dotted_string | PRIMITIVE_TYPE | VOID ) ID PARENTHESIS_OPEN parameter* PARENTHESIS_CLOSE SEMICOLON;
method [AST_Class belongsClass] returns [AST_Method value]:
    {
        AST_AbstractMethodReturn returnDesc = null;
    }
    (
        dotted_string
            {
                //TODO: Search on reflectionManager
                //TODO: Show an message: Class has not been found, no wrappers will be constructed for line, colum class description
                //TODO: Rule must return null
                ClassNode classNode = (ClassNode) description.packageTree.addNode( $dotted_string.text );
                returnDesc = new AST_ClassMethodReturn( classNode );
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
            //TODO: Search on reflectionManager
            //TODO: Show an message: Class has not been found, no wrappers will be constructed for line, colum class description
            //TODO: Rule must return null
            ClassNode classNode = (ClassNode) description.packageTree.addNode( $dotted_string.text );
            $value = new AST_ClassParameter( classNode );
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
