package com.chen.cy.talkimage.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by C-y on 2015/10/19.
 */
public class Praise extends BmobObject{
    private MyUser praiseUser;
    private ImageTalkItem praiseItItem;

    public MyUser getPraiseUser() {
        return praiseUser;
    }

    public void setPraiseUser(MyUser praiseUser) {
        this.praiseUser = praiseUser;
    }

    public ImageTalkItem getPraiseItItem() {
        return praiseItItem;
    }

    public void setPraiseItItem(ImageTalkItem praiseItItem) {
        this.praiseItItem = praiseItItem;
    }

    @Override
    public String toString() {
        return "Praise{" +
                "praiseUser=" + praiseUser +
                ", praiseItItem=" + praiseItItem +
                '}';
    }
}
