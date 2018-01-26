grammar jw4a;

//@header {
//    TODO: 4 Future usage
//}

definitions: description+;

description: entity BRACKET_OPEN method* BRACKET_CLOSE;

method: ( entity | PRIMITIVE_TYPE | VOID ) ID PARENTHESIS_OPEN parameter* PARENTHESIS_CLOSE;

parameter: ( entity | PRIMITIVE_TYPE ) ID;

entity: package_expr ID;

package_expr : (ID DOT)+;

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



