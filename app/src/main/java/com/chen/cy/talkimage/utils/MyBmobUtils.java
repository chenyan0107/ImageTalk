package com.chen.cy.talkimage.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.DeleteFileListener;
import com.bmob.btp.callback.UploadListener;
import com.chen.cy.talkimage.entity.Comment;
import com.chen.cy.talkimage.entity.CommentItemMsg;
import com.chen.cy.talkimage.entity.ImageTalkItem;
import com.chen.cy.talkimage.entity.MyUser;
import com.chen.cy.talkimage.entity.Praise;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by C-y on 2015/10/13.
 */
public class MyBmobUtils {
    public static int loadCount = 10;

    public static MyBmobUtils getInstance(){
        return new MyBmobUtils();
    }

    public MyBmobUtils(){
        createFile();
    }


    private void createFile(){
        File temp = new File("/sdcard/italk/");//创建自已项目 文件夹
        if (!temp.exists()) {
            temp.mkdir();
        }
    }
    /**
     * 删除之前图片
     */
    public static void deleteFile(Context context, final String fileName, final DeleteFileListener deleteFileListener){
        BmobProFile.getInstance(context).deleteFile(fileName, new DeleteFileListener() {

            @Override
            public void onError(int errorcode, String errormsg) {
                // TODO Auto-generated method stub
                Log.i("bmob", "删除文件失败：" + fileName);
                Log.i("bmob", "删除文件失败：" + errormsg + "(" + errorcode + ")");
                deleteFileListener.onError(errorcode, errormsg);
            }

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Log.i("bmob", "删除文件成功");
                deleteFileListener.onSuccess();
            }
        });
    }

    /**
     * 上传图片
     * @param path
     * @param context
     * @param myUser
     * @param uploadListener
     */
    public void uploadImage(String path, final Context context,final MyUser myUser, final UploadListener uploadListener){
        BTPFileResponse response = BmobProFile.getInstance(context).upload(path, new UploadListener() {
            @Override
            public void onSuccess(String s, String s1, BmobFile bmobFile) {
                uploadListener.onSuccess(s, s1, bmobFile);
            }

            @Override
            public void onProgress(int i) {
                uploadListener.onProgress(i);
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
                Log.i("bmob", "文件上传失败：" + errormsg);
                Toast.makeText(context, "头像上传失败" + errormsg, Toast.LENGTH_SHORT).show();
                uploadListener.onError(statuscode,errormsg);
            }
        });
    }

    /**
     * 上传头像到bmob
     * @param path
     */
    public void uploadImg(String path, final Context context,final MyUser myUser, final UploadListener uploadListener){
        final BTPFileResponse response = BmobProFile.getInstance(context).upload(path, new UploadListener() {

            @Override
            public void onSuccess(String fileName, String url, BmobFile file) {
                Log.i("bmob", "文件上传成功：" + fileName + ",可访问的文件地址：" + file.getUrl());
                Log.e("bmob", "Uri=" + file.getUrl());
                Log.e("bmob", "SubSTRING=" + myUser.getUserImageFileName());
                deleteFile(context, myUser.getUserImageFileName(), new DeleteFileListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
                MyUser newUser = new MyUser();
                newUser.setUserImageFileName(fileName);
                newUser.setUserImg(file);
                updateUser(myUser, newUser, context, new UpdateListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
                uploadListener.onSuccess(fileName, url, file);
            }

            @Override
            public void onProgress(int progress) {
                // TODO Auto-generated method stub
                Log.i("bmob", "onProgress :" + progress);
                uploadListener.onProgress(progress);
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
                Log.i("bmob", "文件上传失败：" + errormsg);
                Toast.makeText(context, "头像上传失败" + errormsg, Toast.LENGTH_SHORT).show();
                uploadListener.onError(statuscode,errormsg);
            }
        });
    }

    /**
     * 更新后台用户
     * @param myUser
     */
    public void updateUser(MyUser myUser, MyUser newUser, final Context context, final UpdateListener myUpdateListner) {
        newUser.update(context, myUser.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
                myUpdateListner.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(context, "更新失败" + s, Toast.LENGTH_SHORT).show();
                Log.e("bmob", "更新失败" + s);
                myUpdateListner.onFailure(i, s);
            }
        });
    }

    /**
     * 查询数据
     */
    public static void SearchData(final Context context, final FindListener<ImageTalkItem> findListener, int limit){
        MyUser myUser = BmobUser.getCurrentUser(context, MyUser.class);
        BmobQuery<ImageTalkItem> query = new BmobQuery<ImageTalkItem>();
        //查询playerName叫“比目”的数据
//        query.addWhereEqualTo("itUser", myUser);
        query.include("itUser");// 希望在查询帖子信息的同时也把发布人的信息查询出来
//        query.addWhereEqualTo("playerName", "比目");
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.order("-createdAt");
        query.setLimit(loadCount); // 限制最多10条数据结果作为一页
        query.setSkip(limit);
        //执行查询方法
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 先从缓存获取数据，如果没有，再从网络获取。
        query.findObjects(context, new FindListener<ImageTalkItem>() {
            @Override
            public void onSuccess(List<ImageTalkItem> object) {
                // TODO Auto-generated method stub
                Log.e("bmob", "查询成功,object=" + object.toString());
                findListener.onSuccess(object);
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                findListener.onError(code, msg);
                Log.e("bmob", "查询失败,code="+code+"/msg="+msg);
            }
        });
    }


    private interface loadBmobDataListener<T>{
        void onSuccess(List<T> list, boolean isParise);
        void onError(String code, String msg);
    }

    /**
     * 点赞后的操作
     * @param context
     * @param imageTalkItem
     * @param updateListener
     */
    public static void AddPrise(final Context context, final ImageTalkItem imageTalkItem, final UpdateListener updateListener) {
        MyUser myUser = BmobUser.getCurrentUser(context, MyUser.class);
        Praise praise = new Praise();
        praise.setPraiseUser(myUser);
        praise.setPraiseItItem(imageTalkItem);
        praise.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                imageTalkItem.setPraiseCount((Integer.parseInt(imageTalkItem.getPraiseCount()) + 1) + "");
                imageTalkItem.update(context, imageTalkItem.getObjectId(), new UpdateListener() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        Log.i("bmob", "更新成功：");
                        updateListener.onSuccess();
                        synchronized (updateListener) {

                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        Log.i("bmob", "更新失败：" + msg);
                        updateListener.onFailure(code, msg);
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

    }

    /**
     *
     */
    public static void ComparisonParise(Context context, ImageTalkItem imageTalkItem, final CountListener countListener) {
        MyUser myUser = BmobUser.getCurrentUser(context, MyUser.class);
        BmobQuery<Praise> query = new BmobQuery<>();
        query.addWhereEqualTo("praiseUser", myUser);
        query.addWhereEqualTo("praiseItItem", imageTalkItem);
        query.count(context, Praise.class, new CountListener() {
            @Override
            public void onSuccess(int count) {
                if (count == 0) {
                    Log.e("bmob", "没有点赞");
                    countListener.onFailure(400, "没有");
                } else {
                    Log.e("bmob", "点赞了");
                    countListener.onSuccess(count);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                countListener.onFailure(code, msg);
            }
        });
    }

    public static void GetCommentData(Context context, ImageTalkItem imageTalkIte, final FindListener<Comment> findListener){
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("commentItItem",imageTalkIte);
        query.setLimit(20);
        query.include("commentUser");
        query.findObjects(context, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                findListener.onSuccess(list);
            }

            @Override
            public void onError(int i, String s) {
                findListener.onError(i, s);
            }
        });
    }


    public static void addComment(final Context context, CommentItemMsg commentItemMsg, final ImageTalkItem commentItItem, final SaveListener saveListener) {
        MyUser myUser = BmobUser.getCurrentUser(context, MyUser.class);
        Comment comment = new Comment();
        comment.setCommentItItem(commentItItem);
        comment.setCommentText(commentItemMsg.getComment());
        comment.setCommentUser(myUser);
        comment.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                commentItItem.setPraiseCount((Integer.parseInt(commentItItem.getPraiseCount()) + 1) + "");
                commentItItem.update(context, commentItItem.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        saveListener.onSuccess();
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                saveListener.onFailure(i, s);
            }
        });
    }
}
