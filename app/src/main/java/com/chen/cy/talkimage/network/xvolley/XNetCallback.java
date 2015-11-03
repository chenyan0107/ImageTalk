package com.chen.cy.talkimage.network.xvolley;

import com.android.volley.VolleyError;

/**
 * Created by xu on 2015/8/6.
 */
public interface XNetCallback<T> {
    /**
     * 在执行请求前执行
     */
    void onStart(XRequest request);

    /**
     * 请求结束后执行，包括执行成功和失败
     */
    void onFinish(XRequest request);

    /**
     * 成功返回并解析数据
     */
    void onSuccess(XRequest request, T data);

    /**
     * 包括所有请求失败情况
     * 网络请求出错，响应码4xx， 5xx，解析数据出错，以及包括code不等于0的情况
     */

    void onError(XRequest request, VolleyError volleyError);

    void onNoNetwork(XRequest request);// 无网络

    void onCancel(XRequest request);

}
