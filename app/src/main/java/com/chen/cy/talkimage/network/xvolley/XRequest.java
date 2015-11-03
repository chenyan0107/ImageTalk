package com.chen.cy.talkimage.network.xvolley;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.chen.cy.talkimage.baseUtils.NetUtils;
import com.chen.cy.talkimage.network.xvolley.toolbox.ParsedResultNullError;
import com.chen.cy.talkimage.network.xvolley.toolbox.VolleyUtils;

import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by xu on2015/8/6.
 */
public abstract class XRequest<T> extends Request<T> {


    public static final int DEFAULT_TIMEOUT_MS = 15000;
    public static final int DEFAULT_MAX_RETRIES = 1;
    public static final float DEFAULT_BACKOFF_MULT = 1.0F;

    private Map<String, String> mParams;
    private XNetCallback<T> mCallBack;
    private NetworkResponse mNetworkResponse;
    private RequestQueue mRequestQueue;

    /**
     * 是否此request成功后有下一步动作
     */
    private boolean mNeedLoadingStillAfterSucess = false;


    public XRequest(int method, String url, Map<String, String> params, XNetCallback<T> callBack) {
        super(method, url, null);
        this.mParams = params;
        this.mCallBack = callBack;
        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
    }


    public XNetCallback<T> getCallBack() {
        return mCallBack;
    }

    public void setCallBack(XNetCallback<T> callBack) {
        this.mCallBack = callBack;

    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }


    /**
     * 该Request被添加到 RequestQueue执行的方法，在其中执行 mCallBack.onStart();
     */
    @Override
    public void setRequestQueue(RequestQueue requestQueue) {
        this.mRequestQueue = requestQueue;
        VolleyUtils.logNetMsg(this, "url : \n" + getUrlWithParams()
//                        + "\n url no encode : \n" + getUrlWithParamsNoUrlEncode()
        );
        if (mCallBack != null) {
            mCallBack.onStart(this);

            if (!NetUtils.isConnected()) {
                mCallBack.onFinish(this);
                mCallBack.onNoNetwork(this);
            }
        }
        super.setRequestQueue(requestQueue);
    }


    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        mNetworkResponse = networkResponse;

        VolleyUtils.logNetMsg(this, "response data : \n" + new String(networkResponse.data));

        try {
            T result = doParse(networkResponse);
            //通用情况下不用业务代码内不用考虑返回结果为空情况
            if (result != null) {
                return Response.success(result, parseCacheEntry(networkResponse));
            } else {
                return Response.error(new ParsedResultNullError(networkResponse));
            }
        } catch (VolleyError error) {
            return Response.error(error);
        }
    }

    /**
     * 根据Response解析缓存entry，在原有基础上
     */

    protected Cache.Entry parseCacheEntry(NetworkResponse networkResponse) {
        if (shouldCache())
            return HttpHeaderParser.parseCacheHeaders(networkResponse);
        return null;
    }

    /**
     * 将返回结果解析为需要的数据，
     *
     * @param networkResponse
     * @return
     * @throws VolleyError 解析出现错误时，请抛volleyError
     */

    public abstract T doParse(NetworkResponse networkResponse) throws VolleyError;


    @Override
    protected void deliverResponse(T data) {
        if (mCallBack != null) {
            mCallBack.onFinish(this);
            mCallBack.onSuccess(this, data);
        }
    }


    @Override
    public void deliverError(VolleyError error) {
        // 所有错误日志统一在这里print
        error.printStackTrace();
        if (mCallBack != null) {
            mCallBack.onFinish(this);

            if (interceptError(error)) return;
            mCallBack.onError(this, error);
            //改为start时判断network
            //这里将NoConnectionError单独处理，其他错误集中处理
//            if (error instanceof NoConnectionError) {
//                mCallBack.onNoNetwork(this);
//            } else {
//                mCallBack.onError(this, error);
//            }
        }
    }

    public boolean interceptError(VolleyError error) {
        return false;
    }

    @Override
    public String getCacheKey() {
        return getUrlWithParams();
    }

//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        Map<String, String> headers = new HashMap<String, String>();
//        return headers;
//    }


    public String getUrlWithParams() {
        byte[] body = null;
        try {
            body = getBody();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        if (body == null) {
            return getUrl();
        }
        return getUrl() + "?" + new String(body);
    }

    private String getUrlWithParamsNoUrlEncode() {
        byte[] body = null;
        try {
            body = getBody();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        if (body == null) {
            return getUrl();
        }
        return getUrl() + "?" + URLDecoder.decode(new String(body));
    }


    @Override
    public void cancel() {
        super.cancel();
        if (mCallBack != null) {
            mCallBack.onFinish(this);
            mCallBack.onCancel(this);
        }
    }

    /**
     * 设置是否此request成功后有下一步动作
     *
     * @param needLoadingStillAfterSucess
     */

    public void setNeedLoadingStillAfterSucess(boolean needLoadingStillAfterSucess) {
        mNeedLoadingStillAfterSucess = needLoadingStillAfterSucess;
    }

    public boolean needLoadingStillAfterSucess() {
        return mNeedLoadingStillAfterSucess;
    }

    public NetworkResponse getNetworkResponse() {
        return mNetworkResponse;
    }


    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}
