package com.chen.cy.talkimage.utils;

import android.content.Context;
import android.widget.Toast;

import com.chen.cy.talkimage.entity.MyUser;

import cn.bmob.v3.listener.SaveListener;



public class UserManger {

    public static void login(final Context context, String loginUsername, String LoginPassword){
        MyUser user = new MyUser();
        user.setUsername(loginUsername);
        user.setPassword(LoginPassword);
        user.login(context, new SaveListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(context, "登录失败"+s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
