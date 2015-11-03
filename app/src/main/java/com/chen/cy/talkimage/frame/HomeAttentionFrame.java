package com.chen.cy.talkimage.frame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.views.BaseFrame;

import butterknife.ButterKnife;

/**
 * 主页页面
 * Created by C-y on 2015/10/9.
 */
public class HomeAttentionFrame extends BaseFrame {

    public static HomeAttentionFrame newInstance() {
        return new HomeAttentionFrame();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_attention, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
