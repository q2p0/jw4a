grammar jw4a; //TODO: Change grammar to Jw4a

//TODO WARNING: Test with Classes that don't belong to any package
//TODO: Create one TODO.TXT file for big goals
//TODO: Compile and check on WINDOWS SO

@parser::header{

    import org.q2p0.jw4a.ast.*;
    import org.q2p0.jw4a.ast.nodes.*;
    import org.q2p0.jw4a.ast.nodes.method.*;
    import org.q2p0.jw4a.ast.nodes.method.parameter.*;
    import org.q2p0.jw4a.ast.nodes.method.methodReturn.*;

    import org.q2p0.jw4a.generator.*;

}

@parser::members{
    AST_Builder astBuilder = new AST_Builder();
    CodeGenerator codeGenerator = new WrapperCodeGenerator();
}

wrappers :
    package_description[ astBuilder.root ]*
    {
        System.out.println();
        codeGenerator.generate( astBuilder );
    }
;

// package_description: dotted_string BRACKET_OPEN ( package_description | class_description )* BRACKET_CLOSE;
package_description [AST_Package parentPackage]:
    dotted_string //TODO: root package
    {
        AST_Package _package = astBuilder.getOrAddPackage( parentPackage, $dotted_string.text );
    }
    BRACKET_OPEN
    (
            package_description[ _package ]
        |
            class_description[ _package ]
    )*
    BRACKET_CLOSE
;

// class_description: CLASS ID BRACKET_OPEN method* BRACKET_CLOSE;
class_description [AST_Package _package] :
    CLASS ID
    {
        AST_Class ast_class = astBuilder.getOrAddClass( $_package, $ID.text );
    }
    BRACKET_OPEN
    {
        ArrayList<AST_Method> methods = new ArrayList<AST_Method>();
    }
    (
        method[ ast_class ]
        {
            ast_class.methods.add( $method.value ); //TODO: Don't add same signature methods
        }
    )*
    BRACKET_CLOSE
;

// method: ( dotted_string | PRIMITIVE_TYPE | VOID ) ID PARENTHESIS_OPEN parameter* PARENTHESIS_CLOSE SEMICOLON;
method [AST_Class belongsClass] returns [AST_Method value]:
    {
        AST_AbstractMethodReturn returnDesc = null;
    }
    (
        dotted_string
            {
                AST_Class parameterClass = astBuilder.getOrAddClass( $dotted_string.text );
                returnDesc = new AST_ClassMethodReturn( parameterClass );
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
        List<AST_AbstractParameter> parameters = null;
    }
    (
        parameters
            {
                parameters = $parameters.value;
            }
    )?
    PARENTHESIS_CLOSE
    {
        $value = new AST_Method( returnDesc, id, parameters);
    }
    SEMICOLON
;

// parameters: parameter ( COMMA parameter )*
parameters returns [List<AST_AbstractParameter> value]:
    {
        $value = new ArrayList<AST_AbstractParameter>();
    }
    p1=parameter
    {
        $value.add( $p1.value );
    }
    (
        COMMA
        p2=parameter
        {
            $value.add( $p2.value );
        }
    )*
;

// parameter : ( dotted_string | PRIMITIVE_TYPE ) ID ;
parameter returns [AST_AbstractParameter value]: //
    (
        dotted_string
        {
            AST_Class parameterClass = astBuilder.getOrAddClass( $dotted_string.text );
            $value = new AST_ClassParameter( parameterClass );
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

dotted_string : ID (DOT ID)* ;

CLASS : 'class';

PRIMITIVE_TYPE : 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' | 'char' | 'boolean';
VOID: 'void';

COMMA: ',';
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
