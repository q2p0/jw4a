package org.q2p0.jw4a.ast;

import org.q2p0.jw4a.ReflectionManager;
import org.q2p0.jw4a.ast.JObjectsTree.ClassNode;
import org.q2p0.jw4a.ast.JObjectsTree.PackageNode;
import org.q2p0.jw4a.ast.nodes.AST_Class;

import java.util.HashMap;
import java.util.Map;

public class Description {

    //TODO: Delete this and make AST responsible of store the package tree
    public PackageNode packageTree = new PackageNode(null, null);

    public Map<String, AST_Class> classDescriptions = new HashMap<String, AST_Class>();

    public AST_Class addClass( String _package, String classID ) {

        assert ( _package == null || !_package.isEmpty() );

        String fullClassPath = _package!=null? _package+"."+classID : classID ;

        AST_Class newASTClass = classDescriptions.get( fullClassPath );

        if( newASTClass == null ) {

            ReflectionManager reflection = ReflectionManager.GetInstance();

            Map<Integer, Class> references = reflection.getClasses( fullClassPath );
            if( references == null ) {
                throw new RuntimeException("Unimplemented");
                //TODO: Show an message: Class has not been found, no wrappers will be constructed for line, colum class description
            }
            ClassNode classNode = (ClassNode) packageTree.addNode( fullClassPath );

            AST_Class ast_class = new AST_Class( classID, classNode, references );
            classDescriptions.put( fullClassPath, ast_class );

        }

        return newASTClass;

    }
}
