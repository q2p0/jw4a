package org.q2p0.jw4a.ast;

import org.q2p0.jw4a.ReflectionManager;
import org.q2p0.jw4a.ast.nodes.AST_Class;
import org.q2p0.jw4a.ast.nodes.AST_Package;
import org.q2p0.jw4a.util.DottedString;
import org.q2p0.jw4a.util.HashMapSetGet;
import org.q2p0.jw4a.util.SetGet;

import java.util.Map;

public class AST_Builder {

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

        // If AST_Class was not previously stored.
        AST_Class key = new AST_Class(_package, classID);
        AST_Class value = astClassCache.get( key );
        if( value == null ) {

            // Add it to AST_Package and astClassCache
            value = key;
            _package.classes.add( value );
            astClassCache.add( value );

            // Find reflection references between minApi and maxApi
            String fullClassPath = String.join(".", _package.packagePath, classID);
            Map<Integer, Class> references = ReflectionManager.GetInstance().getClasses( fullClassPath );
            if( references == null )
                throw new RuntimeException("Unimplemented situation"); //TODO: Show at least one descriptive message
            value.apiReflectionClasses = references;

            //// TODO: Find all his base classes between minApi and maxApi
            //for (Map.Entry<Integer, Class> entry : references.entrySet()) {
            //    int api = entry.getKey();
            //    Class _class = entry.getValue();
            //    Class superClass = _class.getSuperclass();
            //    if( superClass != null ) {
            //        String superClassPath = superClass.getName();
            //        AST_Class astSuperClass = getOrAddClass( superClassPath );
            //        value.superClass.put(api, astSuperClass);
            //    }
            //}
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
