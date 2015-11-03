package com.chen.cy.talkimage.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.adapter.CommentAdapter;
import com.chen.cy.talkimage.entity.Comment;
import com.chen.cy.talkimage.entity.CommentItemMsg;
import com.chen.cy.talkimage.entity.ImageTalkItem;
import com.chen.cy.talkimage.entity.MyUser;
import com.chen.cy.talkimage.utils.MyBmobUtils;
import com.chen.cy.talkimage.utils.MyMediaUtils;
import com.chen.cy.talkimage.views.BaseSwipeBackActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

public class ImageTalkActivity extends BaseSwipeBackActivity implements CommentAdapter.OnCommentItemClick{

    private static final int PROCESSING = 1;
    private static final int FAILURE = -1;

    @Bind(R.id.image_talk_toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.image_talk_appbar)
    AppBarLayout imageTalkAppbar;
    @Bind(R.id.image_talk_fab)
    FloatingActionButton fab;
    @Bind(R.id.image_talk_progress)
    ProgressBar imageTalkProgress;
    @Bind(R.id.image_talk_list)
    RecyclerView imageTalkList;
    @Bind(R.id.image_talk_img)
    ImageView imageTalkImg;
    @Bind(R.id.image_talk_comment)
    TextView imageTalkComment;

    private CommentAdapter commentAdapter;

    private MyUser myUser;
    private ArrayList<CommentItemMsg> mData;
    private ImageTalkItem imageTalkItem;

    private ImageLoader imageLoader;

    private MyMediaUtils myMediaUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_image_talk);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initToolbar();
        initView();
        initData();
    }

    private void initData() {
        myUser = BmobUser.getCurrentUser(this, MyUser.class);
        mData = new ArrayList<>();
        imageLoader = ImageLoader.getInstance();
        imageTalkItem = (ImageTalkItem)getIntent().getSerializableExtra("id");
        myMediaUtils = new MyMediaUtils(imageTalkProgress);

        imageTalkComment.setText("评论 "+imageTalkItem.getCommentCount());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        imageTalkList.setLayoutManager(linearLayoutManager);
        imageTalkList.setHasFixedSize(true);
        imageLoader.displayImage(imageTalkItem.getItImage().getUrl(), imageTalkImg);
        MyBmobUtils.GetCommentData(this, imageTalkItem, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
//                loadView.setVisibility(View.GONE);
                for (Comment comment : list) {
                    CommentItemMsg commentItemMsg = new CommentItemMsg();
                    commentItemMsg.setImg(comment.getCommentUser().getUserImg().getUrl());
                    commentItemMsg.setComment(comment.getCommentText());
                    commentItemMsg.setTime(comment.getCreatedAt());
                    commentItemMsg.setName(comment.getCommentUser().getUserIntName());
                    mData.add(commentItemMsg);
                }
                Log.e("bmob", mData.toString());
                commentAdapter = new CommentAdapter(ImageTalkActivity.this, mData);
                imageTalkList.setAdapter(commentAdapter);
                commentAdapter.setOnCommentItemClick(ImageTalkActivity.this);
                imageTalkList.setOverScrollMode(View.OVER_SCROLL_NEVER);
                imageTalkList.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                            commentAdapter.setAnimationsLocked(true);
                        }
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void initView() {
        imageTalkProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progress_custom));
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onAvatorClick(View view, int position) {

    }

    @OnClick(R.id.image_talk_fab)
    void fabClick(){
        fab.setImageResource(R.mipmap.pause);
        playMusic();
    }

    private void playMusic() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                myMediaUtils.playUrl(imageTalkItem.getItAudio().getUrl());
            }
        }).start();
    }

//    private final class UIHandler extends Handler {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case PROCESSING: // 更新进度
//                    imageTalkProgress.setProgress(msg.getData().getInt("size"));
//                    float num = (float) imageTalkProgress.getProgress()
//                            / (float) imageTalkProgress.getMax();
//                    int result = (int) (num * 100); // 计算进度
//                    if (imageTalkProgress.getProgress() == imageTalkProgress.getMax()) { // 下载完成
//                        Toast.makeText(getApplicationContext(), "下载完成",
//                                Toast.LENGTH_LONG).show();
//                    }
//                    break;
//                case FAILURE: // 下载失败
//                    Toast.makeText(getApplicationContext(), "下载失败",
//                            Toast.LENGTH_LONG).show();
//                    break;
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myMediaUtils != null) {
            myMediaUtils.stop();
            myMediaUtils = null;
        }
    }
}
