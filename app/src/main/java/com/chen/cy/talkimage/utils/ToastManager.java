package com.chen.cy.talkimage.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by C-y on 2015/10/30.
 */
public class ToastManager {
    private Context context;

    public ToastManager(Context context){
        this.context = context;
    }

    public static ToastManager getInstance(Context context){
        return new ToastManager(context);
    }

    public void makeToast(String string){
        Toast.makeText(context, string, Toast.LENGTH_SHORT);
    }

}
