package org.q2p0.jw4a.ast;

import org.q2p0.jw4a.ast.nodes.AST_Class;
import org.q2p0.jw4a.ast.nodes.AST_Package;
import org.q2p0.jw4a.ast.nodes.method.AST_Method;

/**
 * Prints an description of the AST_Tree
 */
public class AST_TreePrint {

    static final int NESTING = 2;

    static public void print( AST_Package root, int level ){

        String nesting = new String(new char[level]).replace('\0', ' ');

        System.out.println( nesting + root.toString() + "{" );

        for(AST_Package p : root.subPackages.keySet() )
            print( p, level + NESTING);

        for(AST_Class c : root.classes.keySet() )
            print( c, level + NESTING);

        System.out.println( nesting + "}" );

    }

    static public void print( AST_Class _class, int level ){

        String nesting = new String(new char[level]).replace('\0', ' ');

        System.out.print( nesting + _class.toString() );

        if( _class.methods.size() > 0 ) {

            String nesting_member = nesting + new String(new char[NESTING]).replace('\0', ' ');

            System.out.println(" {");
            for (AST_Method m : _class.methods)
                System.out.println(nesting_member + m.toString());
            System.out.println(nesting + "}");

        } else {

            System.out.println("{}");

        }

    }

}
