package com.chen.cy.talkimage.network.xvolley.toolbox;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

/**
 * 项目名称：minshengok
 * 类描述： 解析的结果为空
 *
 * 创建人：xu
 * 创建时间：2015/9/8 15:35
 * 修改人：xu
 * 修改时间：2015/9/8 15:35
 * 修改备注：
 */
public class ParsedResultNullError extends VolleyError {

    public ParsedResultNullError(NetworkResponse networkResponse) {
        super(networkResponse);
    }

}
