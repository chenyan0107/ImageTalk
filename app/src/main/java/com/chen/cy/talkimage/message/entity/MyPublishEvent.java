package com.chen.cy.talkimage.message.entity;

/**
 * Created by C-y on 2015/10/27.
 */
public class MyPublishEvent {
    private String mMsg;
    private String title;
    public MyPublishEvent(String msg, String title) {
        // TODO Auto-generated constructor stub
        this.mMsg = msg;
        this.title = title;
    }
    public String getMsg(){
        return mMsg;
    }

    public String getTitle() {
        return title;
    }
}
