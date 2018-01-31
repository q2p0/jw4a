package org.q2p0.jw4a.abstractDesc.JObjectsTree;

import java.util.ArrayList;
import java.util.HashMap;

public class PackageNode extends AbstractNode {

    private HashMap<String, PackageNode> subPackages = new HashMap<String, PackageNode>();
    private HashMap<String, ClassNode> subClasses = new HashMap<String, ClassNode>();

    public PackageNode(String id) { super(id); }

    //TODO: Rename to addClass
    @Override public AbstractNode addNode(String s, ArrayList<PairClassApi> pca) {

        AbstractNode returnedAbstractNode = null;

        if( s==null || s.isEmpty() )
            throw new RuntimeException("PackageNode.addNode incorrect usage");

        int firstDot = s.indexOf('.');
        if( firstDot == -1 ) {
            returnedAbstractNode = subClasses.get( s );
            if( returnedAbstractNode == null ) {
                returnedAbstractNode = new ClassNode( s, pca);
                subClasses.put( s , (ClassNode) returnedAbstractNode);
            }
        } else {
            String head = s.substring( 0, firstDot);
            String tail = s.substring( firstDot + 1);
            PackageNode packageNode = subPackages.get( head );
            if( packageNode == null ) {
                packageNode = new PackageNode( head );
                subPackages.put( head, packageNode );
            }
            returnedAbstractNode = packageNode.addNode( tail, pca );
        }

        return returnedAbstractNode;
    }
}