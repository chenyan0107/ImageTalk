package com.chen.cy.talkimage.media;

import android.content.Context;
import android.os.Environment;

import com.chen.cy.talkimage.application.BaseApplication;
import com.chen.cy.talkimage.baseUtils.AppUtils;
import com.chen.cy.talkimage.baseUtils.SDCardUtils;

import java.io.File;

/**
 * Created by xu on 2015/8/7.
 * <p/>
 * 统一文件管理
 * <p/>
 * 所有的获取文件保存目录，获取生成临时文件名等 统一在这里定义
 */
public class FileManager {


    private static final String BASE_EXTERNAL_DIR_PATH = new File(
            Environment.getExternalStorageDirectory(), "italk")
            .getAbsolutePath();

    static {
        File baseExternalDir = new File(BASE_EXTERNAL_DIR_PATH);
        if (!baseExternalDir.exists())
            baseExternalDir.mkdirs();
    }

    private static Context mContext = BaseApplication.getInstance();


    /**
     * 获取缓存目录
     *
     * @return
     */

    public static File getCacheDir() {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return mContext.getExternalCacheDir();
        }
        return mContext.getCacheDir();
    }


    /**
     * 获取缓存目录下的文件夹
     */

    public static File getCacheDir(String name) {


        File file = new File(getCacheDir(), name);

        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }


    // 获取图片下载文件夹
    public static File getImageSaveDir() {

        File imageSaveDir = new File(BASE_EXTERNAL_DIR_PATH, "image");
        if (!imageSaveDir.exists())
            imageSaveDir.mkdirs();

        return imageSaveDir;
    }

    // 获取自动更新app下载文件夹
    public static File getAppSaveDir() {
        File imageSaveDir = new File(BASE_EXTERNAL_DIR_PATH, "app");
        if (!imageSaveDir.exists())
            imageSaveDir.mkdirs();
        return imageSaveDir;
    }

    public static void deleteOldApk() {
        if (!SDCardUtils.isSDCardEnable())
            return;
        File appSaveDir = getAppSaveDir();
        for (File f : appSaveDir.listFiles()) {
            String regex = "txmpApp_\\d+\\.apk";
            if (f.getName().matches(regex)) {
                String verCode = f.getName().replace("italkApp_", "")
                        .replace(".apk", "");
                if (Integer.parseInt(verCode) <= AppUtils.getVersionCode(mContext)) {
                    f.delete();
                }
            }
        }
    }

    public static String getAppSaveFileName(int versionCode) {
        return "italkApp_" + versionCode + ".apk";
    }


}
