package com.chen.cy.talkimage.network.xvolley.toolbox;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.support.annotation.NonNull;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.chen.cy.talkimage.application.BaseApplication;
import com.chen.cy.talkimage.baseUtils.AppUtils;
import com.chen.cy.talkimage.media.FileManager;
import com.chen.cy.talkimage.network.xvolley.XBasicNetwork;
import com.chen.cy.talkimage.network.xvolley.XHurlStack;

import java.io.File;

/**
 *
 * Created by xu on2015/8/6.
 * RequestQueue对象占用资源很多，统一管理，不允许在外部调用新建RequestQUeue
 */

public class XVolley {

    private static final String DEFAULT_CACHE_DIR = "data";

    private static class HttpRequestqQueueHolder {
        private static RequestQueue requestqQueue = createHttpRequestQueue();
    }

    private static class HttpsRequestqQueueHolder {
        private static RequestQueue requestqQueue = createHttpsRequestQueue();
    }

    private static class NetDateCacheHolder {
        private static Cache cache = createNetDateCache();
    }


    /**
     * 获取requestQueue对象，对外公开的方法
     *
     * @return
     */

    public static RequestQueue getHttpRequsetQueue() {

        return HttpRequestqQueueHolder.requestqQueue;
    }

    public static RequestQueue getHttpsRequsetQueue() {

        return HttpRequestqQueueHolder.requestqQueue;
    }


    /**
     * 获取网络数据缓存
     */

    protected static Cache getNetDateCache() {
        return NetDateCacheHolder.cache;
    }


    public static String getUserAgent() {
        Context context = BaseApplication.getInstance();
        return "italk-android/" + AppUtils.getVersionName(context);
    }


    @NonNull
    private static Cache createNetDateCache() {
        Context context = BaseApplication.getInstance();
        File cacheDir = FileManager.getCacheDir(DEFAULT_CACHE_DIR);
        return new DiskBasedCache(cacheDir);
    }


    /**
     * 创建requestQueue
     */

    private static RequestQueue createHttpRequestQueue() {


        HttpStack stack;

        if (Build.VERSION.SDK_INT >= 9) {
            stack = new XHurlStack();
        } else {
            // Prior to Gingerbread, HttpUrlConnection was unreliable.
            // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
            stack = new HttpClientStack(AndroidHttpClient.newInstance(getUserAgent()));
        }
        Network network = new XBasicNetwork(stack);
        RequestQueue queue = new RequestQueue(getNetDateCache(), network);
        queue.start();
        return queue;
//        return Volley.newRequestQueue(BaseApplication.getInstance());

    }

    private static RequestQueue createHttpsRequestQueue() {

        return createHttpRequestQueue();

    }


    public static void init() {


    }

    /**
     * 当程序退出时调用
     * 销毁所有的requestQueue
     */

    public static void destroy() {
        HttpRequestqQueueHolder.requestqQueue.stop();
        HttpsRequestqQueueHolder.requestqQueue.stop();
        HttpRequestqQueueHolder.requestqQueue = null;
        HttpsRequestqQueueHolder.requestqQueue = null;

        NetDateCacheHolder.cache = null;
    }
}