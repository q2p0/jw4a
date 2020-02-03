package org.q2p0.jw4a.ast;

import org.q2p0.jw4a.ast.branchparams.BP_ApiRange;
import org.q2p0.jw4a.reflection.ReflectionHelper;
import org.q2p0.jw4a.ast.nodes.AST_Class;
import org.q2p0.jw4a.ast.nodes.AST_Package;
import org.q2p0.jw4a.ast.nodes.method.AST_Method;
import org.q2p0.jw4a.reflection.ReflectionHelperException;
import org.q2p0.jw4a.util.DottedString;
import org.q2p0.jw4a.util.HashMapSetGet;
import org.q2p0.jw4a.util.SetGet;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;

public class AST_Builder {

    public AST_Builder(ReflectionHelper reflectionHelper) {
        this.reflectionHelper = reflectionHelper;
    }

    final private ReflectionHelper reflectionHelper;
    final private AST_Package root = new AST_Package(null, null);
    public AST_Package getRoot() { return root; }

    public SetGet< AST_Class > astClassCache = new HashMapSetGet<>();
    //TODO: Change BP_ApiRange apiRange with BP_BranchParams and better BP_BranchParams
    public AST_Class getOrAddClass( String fullClassPath, BP_ApiRange apiRange ) throws ReflectionHelperException {

        String packagePath = DottedString.init( fullClassPath );
        String classID = DottedString.last( fullClassPath );

        AST_Package ownerPackage = root;
        if( packagePath != null )
            ownerPackage = getOrAddPackage( packagePath );

        return getOrAddClass( ownerPackage, classID, apiRange );
    }
    //TODO: Change BP_ApiRange apiRange with BP_BranchParams and better BP_BranchParams
    public AST_Class getOrAddClass(AST_Package _package, String classID, BP_ApiRange apiRange ) throws ReflectionHelperException {

        // If AST_Class was not previously stored.
        AST_Class key = new AST_Class(_package, classID);
        AST_Class value = astClassCache.get( key );
        if( value == null ) {

            // Add it to AST_Package and astClassCache
            value = key;
            _package.subClasses.add( value );
            astClassCache.add( value );

            // Find reflection references between minApi and maxApi
            String fullClassPath = String.join(".", _package.packagePath, classID);
            Map<Integer, Class> references = reflectionHelper.getClasses( fullClassPath, apiRange.minApi, apiRange.maxApi );
            if( references == null )
                throw new RuntimeException("Unimplemented situation, no API references"); //TODO: Show at least one descriptive message
            value.apiReflectionClasses = references;

            // Find all his base classes between minApi and maxApi
            Map<Integer, Class> superClasses = reflectionHelper.getSuperClasses( references );
            for (Map.Entry<Integer, Class> superClass : superClasses.entrySet()) {
                String superClassPath = superClass.getValue().getName();
                AST_Class astSuperClass = getOrAddClass( superClassPath, apiRange );
                value.superClass.put(superClass.getKey(), astSuperClass);
            }

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

    public void addMethodToClass(AST_Class ownerClass, AST_Method method) {

        for (Map.Entry<Integer, Class> entry : ownerClass.apiReflectionClasses.entrySet()) {

            int api = entry.getKey();
            Class _class = entry.getValue();

            Class classParameters[] = method.getParameterArray(api);

            try {

                Method m = _class.getDeclaredMethod(method.id, classParameters.length > 0 ? classParameters : null );
                ownerClass.methods.computeIfAbsent( api, k->new HashSet<>() ).add( method );

            } catch (NoSuchMethodException e) {

                AST_Class superClass = ownerClass.superClass.get( api );
                if(superClass != null)
                    addMethodToClass(superClass,  method);

            }

            //TODO: Count references & check with @API
        }
    }
}
