package com.chen.cy.talkimage.network.xvolley.rop;

import com.android.volley.VolleyError;

/**
 * Created by xu on 2015/8/7.
 * <p/>
 * <p/>
 * 成功返回数据，但code不是正常
 */
public class RopError extends VolleyError {

    private RopResult ropResult;

    public RopError(RopResult ropResult) {
        super(ropResult.getMessage());
        this.ropResult = ropResult;
    }

    public RopError(String msg) {
        super(msg);
    }
}
