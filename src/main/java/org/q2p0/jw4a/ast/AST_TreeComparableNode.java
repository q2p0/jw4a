package org.q2p0.jw4a.ast;

import org.q2p0.jw4a.util.SetGet;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

// The use in the project of equals method on AST_Nodes is so that calls from collections to equals are fast and
// does not guarantee that two objects are equal because the equality of the children is not explored.

// The treeEquals method serves to check equality by exploring the children of the classes.

public interface AST_TreeComparableNode {

    boolean treeEquals( AST_TreeComparableNode node );

    static boolean treeEquals(SetGet<? extends AST_TreeComparableNode> o1, SetGet<? extends AST_TreeComparableNode> o2) {

        Map<AST_TreeComparableNode,AST_TreeComparableNode> m1 = (Map<AST_TreeComparableNode, AST_TreeComparableNode>) o1;
        Map<AST_TreeComparableNode,AST_TreeComparableNode> m2 = (Map<AST_TreeComparableNode, AST_TreeComparableNode>) o2;

        if ( o1 == o2 ) return true;
        if ( m1.size() != m2.size() ) return false;

        Iterator< Entry<AST_TreeComparableNode,AST_TreeComparableNode> > i = m1.entrySet().iterator();

        while (i.hasNext()) {

            Entry<AST_TreeComparableNode,AST_TreeComparableNode> e = i.next();
            AST_TreeComparableNode key = e.getKey();
            AST_TreeComparableNode value = e.getValue();

            if(value==null) {
                if (!(m2.get(key) == null && m2.containsKey(key)))
                    return false;
            } else {
                if (!value.treeEquals(m2.get(key)))
                    return false;
            }
        }

        return true;
    }

}
