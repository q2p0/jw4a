package org.q2p0.jw4a.ast.branchparams;

public class BP_ApiRange {

    public BP_ApiRange(int minApi, int maxApi) {
        assert ( minApi <= maxApi );
        this.minApi = minApi;
        this.maxApi = maxApi;
    }

    public BP_ApiRange(BP_ApiRange parentRange, int minApi, int maxApi) {
        assert ( minApi <= maxApi );
        this.maxApi = maxApi;
        this.minApi = minApi;
        if( minApi == Integer.MIN_VALUE ) minApi = parentRange.minApi;
        if( maxApi == Integer.MAX_VALUE ) maxApi = parentRange.maxApi;
        if( minApi < parentRange.minApi ) throw new RuntimeException();
        if( maxApi > parentRange.maxApi ) throw new RuntimeException();
    }

    public int minApi;
    public int maxApi;
}
