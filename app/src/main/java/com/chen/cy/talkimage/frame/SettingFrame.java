package com.chen.cy.talkimage.frame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.views.BaseFrame;

/**
 * 设置页面
 * Created by C-y on 2015/10/9.
 */
public class SettingFrame extends BaseFrame{
    private View contact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contact = inflater.inflate(R.layout.frame_setting, container, false);
        return contact;
    }
}
