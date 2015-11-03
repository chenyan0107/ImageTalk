package com.chen.cy.talkimage.network.xvolley.toolbox;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.chen.cy.talkimage.baseUtils.L;
import com.chen.cy.talkimage.network.xvolley.rop.RopError;

/**
 * Created by xu on 2015/8/7.
 */
public class VolleyUtils {


    public static String getVolleyErrorMsg(VolleyError error) {

        if (error instanceof RopError) {      //返回结果code不为0
            return error.getMessage();
        } else if (error instanceof ParsedResultNullError) {  //结果为空
            return "返回结果为空";
        } else if (error instanceof NetworkError) {  //网络错误
//            return "您的网络有问题哦~";
            return "连接服务器失败~";
        } else if (error instanceof TimeoutError) { //请求超时
            return "连接服务器超时";
        } else if (error instanceof ParseError) {  //解析数据失败
            return "解析服务器返回数据出错";
        } else if (error instanceof ServerError) { //服务器返回5xx 4xx等
            return "很抱歉，服务器出错了";
        } else if (error instanceof AuthFailureError) {
            return "认证失败";
        }

        return "很抱歉，出错了~";
    }


    //    public static void logNetMsg(String msg) {
//        String tag = "msokNet";
//        L.v(tag, msg);
//    }
    public static void logNetMsg(Request request, String msg) {
        String tag = "-Volley";
        StringBuilder sb = new StringBuilder();
        if (request != null) {
            sb.append("request-").append(request.hashCode()).append("  ");
        }
        sb.append(msg).append("\n      ");
        //由于logcat字数限制，当一条文本过长时，拆成多条信息log
        if (sb.length() > 3000) {
            for (int i = 0; i < sb.length(); i += 3000) {
                int end = i + 3000;
                if (end > sb.length()) {
                    end = sb.length();
                }
                L.v(tag, sb.substring(i, end) + "\n        \n        \n           \n         ");
            }
        } else {
            L.v(tag, sb.toString());
        }
    }

}
