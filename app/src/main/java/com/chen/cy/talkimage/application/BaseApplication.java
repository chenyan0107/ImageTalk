package com.chen.cy.talkimage.application;

import android.app.Application;

import com.chen.cy.talkimage.views.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.Bmob;

/**
 * Created by C-y on 2015/10/19.
 */
public class BaseApplication extends Application {

    private static BaseApplication mApplication;
    private static List<BaseActivity> activitylist = new LinkedList<>();


    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.ic_stub)            //加载图片时的图片
//                .showImageForEmptyUri(R.drawable.ic_empty)         //没有图片资源时的默认图片
//                .showImageOnFail(R.drawable.ic_error)              //加载失败时的图片
            .cacheInMemory(true)                               //启用内存缓存
            .cacheOnDisk(true)                                 //启用外存缓存
            .considerExifParams(true)                          //启用EXIF和JPEG图像格式
//            .displayer(new RoundedBitmapDisplayer(20))         //设置显示风格这里是圆角矩形
            .build();

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        /**
         * 初始化 Bmob SDK
         * 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
         */
        Bmob.initialize(this, "23d44345606b136ec883445e7c5ffc60");
    }

    public static BaseApplication getInstance() {
        return mApplication;
    }

    public static List<BaseActivity> getActivityList() {
        return activitylist;
    }


    public static void clearAllActivity() {
        for (BaseActivity activity : activitylist) {
            if (!activity.isFinishing())
                activity.finishNoAnimation();

        }

        activitylist.clear();
    }

    public static <T> void closeActivity(Class<T> clazz) {
        List<BaseActivity> activities = new ArrayList<BaseActivity>();
        for (int i = 0; i < activitylist.size(); i++) {
            if (activitylist.get(i).getClass() == clazz) {
                activities.add(activitylist.get(i));
            }

        }
        if (!activities.isEmpty()) {
            for (BaseActivity activity : activities) {
                activity.finishNoAnimation();

            }
            activitylist.remove(activities);
        }

    }


    public static void exit() {
        clearAllActivity();
//        XVolley.destroy();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
