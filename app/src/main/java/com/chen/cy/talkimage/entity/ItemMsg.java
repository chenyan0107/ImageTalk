package com.chen.cy.talkimage.entity;

/**
 * 每条ITEM的实体
 * Created by C-y on 2015/10/9.
 */
public class ItemMsg {

    public static int PRAISED_YES = 0;
    public static int PRAISED_NO = 1;
    public static int PRAISED_NULL = 2;

    private String headImgId;
    private String name;
    private String time;
    private String Img;
    private String praiseText;
    private String speakeText;
    private String recordFile;
    private String title;
    private ImageTalkItem imageTalkItem;
    private int isPraised;

    public String getSpeakeText() {
        return speakeText;
    }

    public void setSpeakeText(String speakeText) {
        this.speakeText = speakeText;
    }

    public String getHeadImgId() {
        return headImgId;
    }

    public void setHeadImgId(String headImgId) {
        this.headImgId = headImgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getPraiseText() {
        return praiseText;
    }

    public void setPraiseText(String praiseText) {
        this.praiseText = praiseText;
    }

    public String getRecordFile() {
        return recordFile;
    }

    public void setRecordFile(String recordFile) {
        this.recordFile = recordFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ImageTalkItem getImageTalkItem() {
        return imageTalkItem;
    }

    public void setImageTalkItem(ImageTalkItem imageTalkItem) {
        this.imageTalkItem = imageTalkItem;
    }

    public int getIsPraised() {
        return isPraised;
    }

    public void setIsPraised(int isPraised) {
        this.isPraised = isPraised;
    }

    @Override
    public String toString() {
        return "ItemMsg{" +
                "headImgId='" + headImgId + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", Img='" + Img + '\'' +
                ", praiseText='" + praiseText + '\'' +
                ", speakeText='" + speakeText + '\'' +
                '}';
    }
}
