package org.q2p0.jw4a.ast;

import org.q2p0.jw4a.ReflectionManager;
import org.q2p0.jw4a.ast.nodes.AST_Class;
import org.q2p0.jw4a.ast.nodes.AST_Package;
import org.q2p0.jw4a.util.DottedString;
import org.q2p0.jw4a.util.HashMapSetGet;
import org.q2p0.jw4a.util.SetGet;

import java.util.Map;

public class Description { //TODO: Rename to AST_TreeBuilder

    public AST_Package root = new AST_Package(null, null);

    public SetGet< AST_Class > astClassCache = new HashMapSetGet<>();
    public AST_Class getOrAddClass( String fullClassPath ) {

        String packagePath = DottedString.init( fullClassPath );
        String classID = DottedString.last( fullClassPath );

        AST_Package ownerPackage = root;
        if( packagePath != null )
            ownerPackage = getOrAddPackage( packagePath );

        return getOrAddClass( ownerPackage, classID );
    }
    public AST_Class getOrAddClass(AST_Package _package, String classID ) {

        AST_Class key = new AST_Class(_package, classID);
        AST_Class value = astClassCache.get( key );

        if( value == null ) {

            value = key;
            _package.classes.add( value );
            astClassCache.add( value );

            String fullClassPath = String.join(".", _package.packagePath, classID);
            Map<Integer, Class> references = ReflectionManager.GetInstance().getClasses( fullClassPath );
            if( references == null )
                throw new RuntimeException("Unimplemented situation"); //TODO: Show at least one descriptive message
            value.apiReflectionClasses = references;

        }

        return value;
    }

    //TODO: Create an astPackageCache, like astClassCache
    public AST_Package getOrAddPackage( String dottedString ) { return getOrAddPackage( root, dottedString ); }
    public AST_Package getOrAddPackage( AST_Package parentPackage, String dottedString ) {

        String head = DottedString.head( dottedString );
        String tail = DottedString.tail( dottedString );

        AST_Package returnPackage = parentPackage.subPackages.addOrGet( new AST_Package( parentPackage, head) );
        if( tail != null )
            returnPackage = getOrAddPackage( returnPackage , tail);

        return returnPackage;

    }

}
