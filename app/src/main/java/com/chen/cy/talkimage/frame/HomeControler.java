package com.chen.cy.talkimage.frame;

import android.content.Context;
import android.util.Log;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.chen.cy.talkimage.entity.ImageTalkItem;
import com.chen.cy.talkimage.entity.MyUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by C-y on 2015/10/27.
 */
public class HomeControler {
    private Context context;
    private MyUser myUser;
    public HomeControler(Context context){
        this.context = context;
        myUser = BmobUser.getCurrentUser(context, MyUser.class);
    }

    public static HomeControler getInstance(Context context){
        return new HomeControler(context);
    }

    public void uploadFils(String[] files, final UploadBatchListener uploadBatchListener){
        BmobProFile.getInstance(context).uploadBatch(files, new UploadBatchListener() {

            @Override
            public void onSuccess(boolean isFinish,String[] fileNames,String[] urls,BmobFile[] files) {
                // isFinish ：批量上传是否完成
                // fileNames：文件名数组
                // urls        : url：文件地址数组
                // files     : BmobFile文件数组，`V3.4.1版本`开始提供，用于兼容新旧文件服务。
                //注：若上传的是图片，url(s)并不能直接在浏览器查看（会出现404错误），需要经过`URL签名`得到真正的可访问的URL地址,当然，`V3.4.1`版本可直接从BmobFile中获得可访问的文件地址。
                Log.i("bmob", "onProgress :" + isFinish + "---" + fileNames + "---" + urls + "----" + files);
                if (isFinish){
                    uploadBatchListener.onSuccess(isFinish, fileNames, urls, files);
                }
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                // curIndex    :表示当前第几个文件正在上传
                // curPercent  :表示当前上传文件的进度值（百分比）
                // total       :表示总的上传文件数
                // totalPercent:表示总的上传进度（百分比）
                Log.i("bmob","onProgress :"+curIndex+"---"+curPercent+"---"+total+"----"+totalPercent);
                uploadBatchListener.onProgress(curIndex, curPercent, total, totalPercent);
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
                Log.i("bmob","批量上传出错："+statuscode+"--"+errormsg);
                uploadBatchListener.onError(statuscode, errormsg);
            }
        });
    }

    public void addImageTalkItem(ImageTalkItem imageTalkItem, final SaveListener saveListener){
        imageTalkItem.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                saveListener.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                saveListener.onFailure(i, s);
            }
        });
    }
}
