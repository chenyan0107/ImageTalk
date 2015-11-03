package com.chen.cy.talkimage.entity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by C-y on 2015/10/10.
 */
public class MyUser extends BmobUser{
    private BmobFile userImg;
    private String userIntName;
    private String userSignature;
    private Boolean userSex;
    private String userAddress;
    private String userImageFileName;

    public BmobFile getUserImg() {
        return userImg;
    }

    public void setUserImg(BmobFile userImg) {
        this.userImg = userImg;
    }

    public String getUserIntName() {
        return userIntName;
    }

    public void setUserIntName(String userIntName) {
        this.userIntName = userIntName;
    }

    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }

    public Boolean getUserSex() {
        return userSex;
    }

    public void setUserSex(Boolean userSex) {
        this.userSex = userSex;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserImageFileName() {
        return userImageFileName;
    }

    public void setUserImageFileName(String userImageFileName) {
        this.userImageFileName = userImageFileName;
    }

    @Override
    public String toString() {
        return "MyUser{" +
                "userUsername=" + getUsername() + '\'' +
                "userImg=" + userImg + '\''  +
                ", userIntName='" + userIntName + '\'' +
                ", userSignature='" + userSignature + '\'' +
                '}';
    }
}
