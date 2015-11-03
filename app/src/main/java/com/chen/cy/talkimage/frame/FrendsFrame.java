package com.chen.cy.talkimage.frame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.views.BaseFrame;

/**
 * 好友页面
 * Created by C-y on 2015/10/9.
 */
public class FrendsFrame extends BaseFrame{
    private View contact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contact = inflater.inflate(R.layout.frame_frends, container, false);
        return contact;
    }
}
