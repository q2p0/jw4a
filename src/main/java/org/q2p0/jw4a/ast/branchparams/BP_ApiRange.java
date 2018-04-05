package org.q2p0.jw4a.ast.branchparams;

public class BP_ApiRange implements BP_Interface {
    public BP_ApiRange(int minApi, int maxApi) {
        assert ( minApi <= maxApi );
        this.minApi = minApi;
        this.maxApi = maxApi;
    }
    public int minApi;
    public int maxApi;
}
