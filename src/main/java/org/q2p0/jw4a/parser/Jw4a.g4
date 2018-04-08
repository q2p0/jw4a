grammar Jw4a;

//TODO WARNING: Test with Classes that don't belong to any package
//TODO: Change Jw4aLists extension from txt to jw4a
//TODO: Add custom user java classes path parameters

@parser::header{

    import java.util.ResourceBundle;

    import org.q2p0.jw4a.reflection.*;
    import org.q2p0.jw4a.ast.*;
    import org.q2p0.jw4a.ast.branchparams.*;
    import org.q2p0.jw4a.ast.nodes.*;
    import org.q2p0.jw4a.ast.nodes.method.*;
    import org.q2p0.jw4a.ast.nodes.method.parameter.*;
    import org.q2p0.jw4a.ast.nodes.method.methodReturn.*;

    import org.q2p0.jw4a.generator.*;

    import static org.q2p0.jw4a.util.ResourceBundle.getBundle4Class;

}

@parser::members{
    static ResourceBundle resources = getBundle4Class( Jw4aParser.class );
    AST_Builder astBuilder;
}

wrappers [ ReflectionPaths paths ] returns [ AST_Package root ]:
    //TODO: When more global directives where added : https://stackoverflow.com/questions/45221716/specifying-a-grammar-rule-of-appearing-in-any-order-but-only-at-most-once
    global_api[ paths ] //TODO: Optional only if ANDROID_HOME has been defined.
    {
        BP_BranchParams globalParams = new BP_BranchParams();
        globalParams.apiRange = $global_api.bt_range;
    }
    package_description[ astBuilder.getRoot(), globalParams ]*
    {
        $root = astBuilder.getRoot();
    }
;

global_api [ ReflectionPaths paths ] returns [ BP_ApiRange bt_range ] : '@GLOBAL_API' closed_range SEMICOLON {
    ReflectionHelper reflectionHelper = new ReflectionHelper( $paths, $closed_range.min, $closed_range.max );
    astBuilder = new AST_Builder( reflectionHelper );
    $bt_range = new BP_ApiRange( $closed_range.min, $closed_range.max );
};

// package_description: dotted_string BRACE_OPEN ( package_description | class_description )* BRACE_CLOSE;
package_description [AST_Package parentPackage, BP_BranchParams branchParams]:
    //TODO: package branch options
    (
        api[branchParams.apiRange] { $branchParams.apiRange = $api.apiRange; }
    )?
    dotted_string //TODO: root package
    {
        AST_Package _package = astBuilder.getOrAddPackage( parentPackage, $dotted_string.text );
    }
    BRACE_OPEN
    (
            package_description[ _package, $branchParams ]
        |
            class_description[ _package, $branchParams ]
    )*
    BRACE_CLOSE
;

// class_description: CLASS ID BRACE_OPEN method* BRACE_CLOSE;
class_description [AST_Package _package, BP_BranchParams branchParams] :
    //TODO: class branch options
    (
        api[branchParams.apiRange] { $branchParams.apiRange = $api.apiRange; }
    )?
    CLASS ID
    {
        AST_Class ast_class = astBuilder.getOrAddClass( $_package, $ID.text, $branchParams.apiRange );
    }
    BRACE_OPEN
    {
        ArrayList<AST_Method> methods = new ArrayList<AST_Method>();
    }
    (
        method[ ast_class, $branchParams ]
    )*
    BRACE_CLOSE
;

// method: ( dotted_string | PRIMITIVE_TYPE | VOID ) ID PARENTHESIS_OPEN parameter* PARENTHESIS_CLOSE SEMICOLON;
method [AST_Class classOwner, BP_BranchParams branchParams] :
    //TODO: method branch options
    (
        api[branchParams.apiRange] { $branchParams.apiRange = $api.apiRange; }
    )?
    {
        AST_AbstractMethodReturn returnDesc = null;
    }
    (
        dotted_string
            {
                AST_Class parameterClass = astBuilder.getOrAddClass( $dotted_string.text,  $branchParams.apiRange );
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
        parameters[ $branchParams ]
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
parameters [ BP_BranchParams branchParams ] returns [List<AST_AbstractParameter> value]:
    {
        $value = new ArrayList<AST_AbstractParameter>();
    }
    p1=parameter[ $branchParams ]
    {
        $value.add( $p1.value );
    }
    (
        COMMA
        p2=parameter[ $branchParams ]
        {
            $value.add( $p2.value );
        }
    )*
;

// parameter : ( dotted_string | PRIMITIVE_TYPE ) ID ;
parameter [ BP_BranchParams branchParams ] returns [AST_AbstractParameter value]: //
    (
        dotted_string
        {
            AST_Class parameterClass = astBuilder.getOrAddClass( $dotted_string.text, $branchParams.apiRange );
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

api [ BP_ApiRange parentRange ] returns [ BP_ApiRange apiRange ] :
API_MODIFIER range
{
    try {
        $apiRange = new BP_ApiRange( parentRange, $range.min, $range.max );
    } catch ( RuntimeException e ) {
        throw new Jw4aParseException( resources.getString("parent_range_error"), $API_MODIFIER );
    }
}
;

range returns [int min, int max] :
(
    closed_range
    {
        $min = $closed_range.min;
        $max = $closed_range.max;
    }
|
    BRACKET_OPEN
    (
        INTEGER '-'
        {
            $min = Integer.parseInt($INTEGER.text);
            $max = Integer.MAX_VALUE;
        }
    |
        '-' INTEGER
        {
            $min = Integer.MIN_VALUE;
            $max = Integer.parseInt($INTEGER.text);
        }
    )
    BRACKET_CLOSE
)
;


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

API_MODIFIER : '@API';

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
