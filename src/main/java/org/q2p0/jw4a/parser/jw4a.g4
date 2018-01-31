grammar jw4a; //TODO: Change grammar to Jw4a

//TODO WARNING: Test with Classes that don't belong to any package
//TODO: Create one TODO.TXT file for big goals

@parser::header{

    import org.q2p0.jw4a.ReflectionManager;
    import org.q2p0.jw4a.CLParameters;

    import org.q2p0.jw4a.abstractDesc.*;
    import org.q2p0.jw4a.abstractDesc.nodes.parameter.*;
    import org.q2p0.jw4a.abstractDesc.JObjectsTree.*;

    import java.lang.StringBuilder;
}

@parser::members{

    ReflectionManager reflection = ReflectionManager.GetInstance();
    WrappersDesc wrappersDescription = new WrappersDesc();


}

wrappers returns [WrappersDesc desc]:
    package_description+
    { $desc = wrappersDescription; }
;

package_description: dotted_string BRACKET_OPEN class_description* BRACKET_CLOSE;

class_description: ID BRACKET_OPEN method* BRACKET_CLOSE;

method: ( dotted_string | PRIMITIVE_TYPE | VOID ) ID PARENTHESIS_OPEN parameter* PARENTHESIS_CLOSE SEMICOLON;

// parameter returns : ( dotted_string | PRIMITIVE_TYPE ) ID ;
parameter returns [AbstractParameterDesc param]: //
    (
        dotted_string
        {
            ArrayList<PairClassApi> references = reflection.existClass( $dotted_string.text );

            if( references != null ) {
              ClassNode classNode = (ClassNode) wrappersDescription.packageThree.addNode( $dotted_string.text, references );
              ClassParameterDesc classparam = new ClassParameterDesc();
              classparam.classNode = classNode;
              $param = classparam;
            } else {
                //TODO: An class parameter has not been found eanywhere, show the error.
            }
        }
    |
        PRIMITIVE_TYPE
        {

        }
    )
    ID
    {
        //$param.id = $ID.text;
    }
;

dotted_string : (ID DOT)* ID;

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
