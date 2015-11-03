package com.chen.cy.talkimage.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by C-y on 2015/10/19.
 */
public class Comment extends BmobObject{
    private MyUser commentUser;
    private ImageTalkItem commentItItem;
    private String commentText;

    public MyUser getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(MyUser commentUser) {
        this.commentUser = commentUser;
    }

    public ImageTalkItem getCommentItItem() {
        return commentItItem;
    }

    public void setCommentItItem(ImageTalkItem commentItItem) {
        this.commentItItem = commentItItem;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentUser=" + commentUser +
                ", commentItItem=" + commentItItem +
                ", commentText='" + commentText + '\'' +
                '}';
    }
}
