package com.chen.cy.talkimage.activity.ItemInsert;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.activity.MainActivity;
import com.chen.cy.talkimage.utils.ImageUtils;
import com.chen.cy.talkimage.views.BaseSwipeBackActivity;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InsertItalkActivity extends BaseSwipeBackActivity {

    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";

    @Bind(R.id.insert_italk_toolbar)
    Toolbar insertItalkToolbar;
    @Bind(R.id.insert_italk_titleeidt)
    EditText insertItalkTitleeidt;
    @Bind(R.id.insert_italk_img)
    ImageView insertItalkImg;
    @Bind(R.id.insert_italk_time)
    TextView insertItalkTime;
    @Bind(R.id.item_home_root)
    CardView itemHomeRoot;

    private Uri imageUri;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_italk);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initData();
        initToolbar();
        initView();
    }

    private void initView() {
        insertItalkImg.setImageDrawable(Drawable.createFromPath(imageUri.getPath()));
        insertItalkTime.setText(time);
    }

    private void initToolbar() {
        insertItalkToolbar.setNavigationIcon(R.mipmap.back);
        insertItalkToolbar.setTitle("");
        insertItalkToolbar.setTitleTextAppearance(this, R.style.ToolbarTitleAppearance);
        setSupportActionBar(insertItalkToolbar);
        insertItalkToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "italk/" + ImageUtils.IMAGE_FILE_NAME));
        time = getIntent().getStringExtra("time");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.record_next:
                Intent intent = new Intent(InsertItalkActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setAction(MainActivity.ACTION_SHOW_LOADING_ITEM);
                intent.putExtra("title",insertItalkTitleeidt.getText().toString());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
