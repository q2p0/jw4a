package org.q2p0.jw4a.abstractDesc.nodes.methodReturn;

import org.q2p0.jw4a.abstractDesc.nodes.PrimitiveTypeDesc;

public class PrimitiveTypeMethodReturnDesc extends AbstractMethodReturnDesc{
    public PrimitiveTypeDesc primitiveType;

    public PrimitiveTypeMethodReturnDesc(PrimitiveTypeDesc primitiveType) {
        this.primitiveType = primitiveType;
    }
}
