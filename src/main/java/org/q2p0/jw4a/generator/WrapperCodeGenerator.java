package org.q2p0.jw4a.generator;

import org.q2p0.jw4a.ast.Description;
import org.q2p0.jw4a.ast.JObjectsTree.ClassNode;
import org.q2p0.jw4a.ast.JObjectsTree.PairClassApi;
import org.q2p0.jw4a.ast.nodes.AST_Class;

import java.util.ArrayList;

public class WrapperCodeGenerator implements CodeGenerator{

    @Override public void generate(Description description) {

        for( AST_Class classdesc : description.classDescriptions ) {

            System.out.println( "Class : " + classdesc.id );

            ClassNode cn = classdesc.cn;
            ArrayList<PairClassApi> pcaArray = cn.pairClassApi;

            /*
            for( PairClassApi pca : pcaArray ) {
                System.out.println( pca._api );
            }
            */

        }

    }

}
