package com.chen.cy.talkimage.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;

import com.chen.cy.talkimage.application.BaseApplication;

/**
 * 所有Activity均继承自此类
 * Created by C-y on 2015/10/9.
 */
public abstract class BaseActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.getActivityList().remove(this);
    }

    public void finishNoAnimation() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
