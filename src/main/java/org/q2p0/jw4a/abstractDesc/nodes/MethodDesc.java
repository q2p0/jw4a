package org.q2p0.jw4a.abstractDesc.nodes;

import org.q2p0.jw4a.abstractDesc.nodes.methodReturn.AbstractMethodReturnDesc;
import org.q2p0.jw4a.abstractDesc.nodes.parameter.AbstractParameterDesc;

import java.util.ArrayList;

public class MethodDesc {
    public AbstractMethodReturnDesc returnDesc;
    public String id;
    public ArrayList<AbstractParameterDesc> parameters;

    public MethodDesc(AbstractMethodReturnDesc returnDesc, String id, ArrayList<AbstractParameterDesc> parameters) {
        this.returnDesc = returnDesc;
        this.id = id;
        this.parameters = parameters;
    }
}
