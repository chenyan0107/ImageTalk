package com.chen.cy.talkimage.network.xvolley;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by xu on 2015/8/7.
 */
public class XStringRequest extends XRequest<String> {


    public XStringRequest(int method, String url, Map<String, String> params, XNetCallback<String> callBack) {
        super(method, url, params, callBack);
    }

    @Override
    public String doParse(NetworkResponse response) throws VolleyError {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }

        return parsed;
    }
}
