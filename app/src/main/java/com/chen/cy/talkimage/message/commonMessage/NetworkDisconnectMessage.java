package com.chen.cy.talkimage.message.commonMessage;

import com.chen.cy.talkimage.message.IMessage;

/**
 * Created by yang on 2015/7/24.
 */
public class NetworkDisconnectMessage implements IMessage {
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
