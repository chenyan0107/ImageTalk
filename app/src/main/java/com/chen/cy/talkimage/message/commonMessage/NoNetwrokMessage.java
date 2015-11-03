package com.chen.cy.talkimage.message.commonMessage;

import com.chen.cy.talkimage.message.IMessage;

/**
 * 主动发起网络请求时，网络模块检查没有网络会发送这个message
 * Created by yang on 2015/7/24.
 */
public class NoNetwrokMessage implements IMessage {
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
