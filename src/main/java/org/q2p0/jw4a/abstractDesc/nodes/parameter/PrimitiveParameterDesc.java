package org.q2p0.jw4a.abstractDesc.nodes.parameter;

import org.q2p0.jw4a.abstractDesc.nodes.PrimitiveTypeDesc;

public class PrimitiveParameterDesc extends AbstractParameterDesc{
    public PrimitiveTypeDesc primitiveType;

    public PrimitiveParameterDesc(PrimitiveTypeDesc primitiveType) {
        this.primitiveType = primitiveType;
    }
}
