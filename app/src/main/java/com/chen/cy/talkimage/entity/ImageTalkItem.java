package com.chen.cy.talkimage.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by C-y on 2015/10/19.
 */
public class ImageTalkItem extends BmobObject implements Serializable{
    private String itTitle;
    private BmobFile itAudio;
    private BmobFile itImage;
    private MyUser itUser;
    private Boolean isHot;
    private String praiseCount;
    private String commentCount;

    public String getItTitl() {
        return itTitle;
    }

    public void setItTitl(String itTitle) {
        this.itTitle = itTitle;
    }

    public BmobFile getItAudio() {
        return itAudio;
    }

    public void setItAudio(BmobFile itAudio) {
        this.itAudio = itAudio;
    }

    public BmobFile getItImage() {
        return itImage;
    }

    public void setItImage(BmobFile itImage) {
        this.itImage = itImage;
    }

    public MyUser getItUser() {
        return itUser;
    }

    public void setItUser(MyUser itUser) {
        this.itUser = itUser;
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    public String getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(String praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return "ImageTalkItem{" +
                "itTitl='" + itTitle + '\'' +
                ", itAudio=" + itAudio +
                ", itImage=" + itImage +
                ", itUser=" + itUser +
                '}';
    }
}
