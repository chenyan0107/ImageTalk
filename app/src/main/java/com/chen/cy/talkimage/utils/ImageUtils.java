package com.chen.cy.talkimage.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.chen.cy.talkimage.views.BaseFrame;
import com.chen.cy.talkimage.views.BaseSwipeBackActivity;

import java.io.File;

/**
 * Created by C-y on 2015/10/22.
 */
public class ImageUtils {

    /* 头像文件 */
    public static final String IMAGE_FILE_NAME = "item_image.jpg";
    /* 请求识别码 */
    public static final int CODE_GALLERY_REQUEST = 0xa0;
    public static final int CODE_CAMERA_REQUEST = 0xa1;
    public static final int CODE_RESULT_REQUEST = 0xa2;

    public static final int RESULT_CANCELED = 0;

    // 裁剪后图片的宽(X)和高(Y)
    public static int output_X = 1120;
    public static int output_Y = 630;

    //剪裁时的比例
    public static int cropX = 16;
    public static int cropY = 9
            ;
    public Uri imageUri;

    public static ImageUtils getInstance(){
        return new ImageUtils();
    }

    public ImageUtils(){
        createData();
    }

    private void createData() {
        File temp = new File("/sdcard/italk/");//创建自已项目 文件夹
        if (!temp.exists()) {
            temp.mkdir();
        }
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "italk/" + IMAGE_FILE_NAME));
        Log.e("ImageUtl", "imageUri=" + imageUri);
    }

    //点击选择头像
    public void chooseImage(Fragment context) {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        context.startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }


    public void chooseImage(BaseSwipeBackActivity context) {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        context.startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri,BaseFrame context) {
        Log.e("ImageUtl","imageUri="+imageUri);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        //设置拉伸，去黑边
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", cropX);
        intent.putExtra("aspectY", cropY);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将裁切的结果输出到指定的Uri
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//裁切成的图片的格式
        intent.putExtra("noFaceDetection", true); // no face detection
        context.startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri,BaseSwipeBackActivity context) {
        Log.e("ImageUtl","imageUri="+imageUri);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");
        //设置拉伸，去黑边
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", cropX);
        intent.putExtra("aspectY", cropY);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将裁切的结果输出到指定的Uri
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//裁切成的图片的格式
        intent.putExtra("noFaceDetection", true); // no face detection
        context.startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 设置截图的宽
     * @param output_X
     */
    public static void setOutput_X(int output_X) {
        ImageUtils.output_X = output_X;
    }

    /**
     * 设置截图的高
     * @param output_Y
     */
    public static void setOutput_Y(int output_Y) {
        ImageUtils.output_Y = output_Y;
    }

    /**
     * 设置剪切的宽比例
     * @param cropX
     */
    public static void setCropX(int cropX) {
        ImageUtils.cropX = cropX;
    }

    /**
     * 设置剪切的高比例
     * @param cropY
     */
    public static void setCropY(int cropY) {
        ImageUtils.cropY = cropY;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}
