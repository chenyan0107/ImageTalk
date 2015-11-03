package com.chen.cy.talkimage.utils;

import android.os.Environment;

/**
 * Created by C-y on 2015/10/12.
 */
public abstract class CheckUtils {
    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }
}
