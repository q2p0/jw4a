grammar jw4a;

//TODO: Change grammar name to Jw4a
//TODO WARNING: Test with Classes that don't belong to any package
//TODO: Change Jw4aLists extension from txt to jw4a
//TODO: Add custom user java classes path parameters

@parser::header{

    import java.util.ResourceBundle;

    import org.q2p0.jw4a.reflection.*;
    import org.q2p0.jw4a.ast.*;
    import org.q2p0.jw4a.ast.nodes.*;
    import org.q2p0.jw4a.ast.nodes.method.*;
    import org.q2p0.jw4a.ast.nodes.method.parameter.*;
    import org.q2p0.jw4a.ast.nodes.method.methodReturn.*;

    import org.q2p0.jw4a.generator.*;

}

@parser::members{
    ResourceBundle resources = ResourceBundle.getBundle("org.q2p0.jw4a.parser.Jw4a");
    AST_Builder astBuilder;
    CodeGenerator codeGenerator = new WrapperCodeGenerator(); //TODO: Out of parser, after AST transformations.
}

wrappers [ ReflectionPaths paths ]:
    global_api[ paths ] //TODO: Optional only if ANDROID_HOME has been defined.
    package_description[ astBuilder.root ]*
    {
        AST_TreePrint.print( astBuilder.root, 2 );
        codeGenerator.generate( astBuilder );
    }
;

global_api [ ReflectionPaths paths ] : '@GLOBAL_API' closed_range SEMICOLON {
    ReflectionHelper reflectionHelper = new ReflectionHelper( $paths, $closed_range.min, $closed_range.max );
    astBuilder = new AST_Builder( reflectionHelper );
};

// package_description: dotted_string BRACE_OPEN ( package_description | class_description )* BRACE_CLOSE;
package_description [AST_Package parentPackage]:
    dotted_string //TODO: root package
    {
        AST_Package _package = astBuilder.getOrAddPackage( parentPackage, $dotted_string.text );
    }
    BRACE_OPEN
    (
            package_description[ _package ]
        |
            class_description[ _package ]
    )*
    BRACE_CLOSE
;

// class_description: CLASS ID BRACE_OPEN method* BRACE_CLOSE;
class_description [AST_Package _package] :
    CLASS ID
    {
        AST_Class ast_class = astBuilder.getOrAddClass( $_package, $ID.text );
    }
    BRACE_OPEN
    {
        ArrayList<AST_Method> methods = new ArrayList<AST_Method>();
    }
    (
        method[ ast_class ]
    )*
    BRACE_CLOSE
;

// method: ( dotted_string | PRIMITIVE_TYPE | VOID ) ID PARENTHESIS_OPEN parameter* PARENTHESIS_CLOSE SEMICOLON;
method [AST_Class classOwner] :
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
        astBuilder.addMethodToClass( $classOwner, new AST_Method( returnDesc, id, parameters) );
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

//TODO: UNDEFINED MIN RANGE
//TODO: UNDEFINED MAX RANGE
closed_range returns [int min, int max] :
BRACKET_OPEN
(
    INTEGER
    { $min = $max = Integer.parseInt($INTEGER.text); }
|
    imin=INTEGER { $min = Integer.parseInt($imin.text); }
    '-'
    imax=INTEGER { $max = Integer.parseInt($imax.text); }
    {
        if( $min > $max ) {
            throw new Jw4aParseException( resources.getString("range_error"), $BRACKET_OPEN );
        }
    }
)
BRACKET_CLOSE
;

INTEGER : [0-9]+;

CLASS : 'class';

PRIMITIVE_TYPE : 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' | 'char' | 'boolean';
VOID: 'void';

COMMA: ',';
DOT: '.';
BRACE_OPEN: '{';
BRACE_CLOSE: '}';
BRACKET_OPEN: '[';
BRACKET_CLOSE: ']';
SEMICOLON: ';';
PARENTHESIS_OPEN: '(';
PARENTHESIS_CLOSE: ')';

ID: [a-zA-Z_][a-zA-Z0-9_]*;
WS: [ \t\n\r]+ -> skip;

COMMENT : '/*' .*? '*/' -> skip;
LINE_COMMENT : '//' ~[\r\n]* -> skip;
