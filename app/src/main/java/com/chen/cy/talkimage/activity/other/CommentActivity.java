package com.chen.cy.talkimage.activity.other;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.adapter.CommentAdapter;
import com.chen.cy.talkimage.baseUtils.ScreenUtils;
import com.chen.cy.talkimage.entity.Comment;
import com.chen.cy.talkimage.entity.CommentItemMsg;
import com.chen.cy.talkimage.entity.ImageTalkItem;
import com.chen.cy.talkimage.entity.MyUser;
import com.chen.cy.talkimage.utils.MyBmobUtils;
import com.chen.cy.talkimage.views.BaseSwipeBackActivity;
import com.chen.cy.talkimage.views.SendCommentButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class CommentActivity extends BaseSwipeBackActivity implements SendCommentButton.OnSendClickListener, CommentAdapter.OnCommentItemClick{

    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";

    @Bind(R.id.comment_toolbar)
    Toolbar commentToolbar;
    @Bind(R.id.comment_list)
    RecyclerView commentList;
    @Bind(R.id.comment_edit)
    EditText commentEdit;
    @Bind(R.id.comment_send_btn)
    SendCommentButton commentSendBtn;
    @Bind(R.id.comment_lladd)
    LinearLayout commentLladd;
    @Bind(R.id.comment_root)
    LinearLayout commentRoot;
    @Bind(R.id.comment_toolbar_root)
    RelativeLayout commentToolbarRoot;
    @Bind(R.id.item_comment_load)
    View loadView;

    private int drawingStartLocation;
    private CommentAdapter commentAdapter;

    private MyUser myUser;
    private ArrayList<CommentItemMsg> mData;
    private ImageTalkItem imageTalkItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        init();
        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
        if (savedInstanceState == null) {
            commentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    commentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }
    }

    private void init() {
        initData();
        initToolbar();
        initView();
        setupSendCommentButton();
    }

    private void initData() {
        myUser = BmobUser.getCurrentUser(this, MyUser.class);
        mData = new ArrayList<>();
        imageTalkItem = (ImageTalkItem)getIntent().getSerializableExtra("id");
        Log.e("bmob", imageTalkItem.toString());
    }

    private void initView() {
        setupComments();
    }

    private void setupComments() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentList.setLayoutManager(linearLayoutManager);
        commentList.setHasFixedSize(true);
        MyBmobUtils.GetCommentData(this, imageTalkItem, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                loadView.setVisibility(View.GONE);
                for (Comment comment : list) {
                    CommentItemMsg commentItemMsg = new CommentItemMsg();
                    commentItemMsg.setImg(comment.getCommentUser().getUserImg().getUrl());
                    commentItemMsg.setComment(comment.getCommentText());
                    commentItemMsg.setTime(comment.getCreatedAt());
                    commentItemMsg.setName(comment.getCommentUser().getUserIntName());
                    mData.add(commentItemMsg);
                }
                commentAdapter = new CommentAdapter(CommentActivity.this, mData);
                commentList.setAdapter(commentAdapter);
                commentAdapter.setOnCommentItemClick(CommentActivity.this);
                commentList.setOverScrollMode(View.OVER_SCROLL_NEVER);
                commentList.setOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void initToolbar() {
        commentToolbar.setNavigationIcon(R.mipmap.back);
        commentToolbar.setTitle("");
        setSupportActionBar(commentToolbar);
        commentToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void startIntroAnimation() {
        ViewCompat.setElevation(commentToolbarRoot, 0);
        commentRoot.setScaleY(0.1f);
        commentRoot.setPivotY(drawingStartLocation);
        commentLladd.setTranslationY(200);

        commentRoot.animate()
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ViewCompat.setElevation(commentToolbarRoot, ScreenUtils.dpToPx(8));
                        animateContent();
                    }
                })
                .start();
    }

    private void animateContent() {
//        commentAdapter.updateItems();
        commentLladd.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .start();
    }

    @Override
    public void onBackPressed() {
        ViewCompat.setElevation(commentToolbarRoot, 0);
        commentRoot.animate()
                .translationY(ScreenUtils.getScreenHeight(this))
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CommentActivity.super.onBackPressed();
                        overridePendingTransition(0, 0);
                    }
                })
                .start();
    }

    private void setupSendCommentButton() {
        commentSendBtn.setOnSendClickListener(this);
    }

    @Override
    public void onSendClickListener(View v) {
        if (validateComment()) {
            final CommentItemMsg data = new CommentItemMsg();
            data.setImg(myUser.getUserImg().getUrl());
            data.setComment(commentEdit.getText().toString());
            data.setTime("刚刚");
            data.setName(myUser.getUserIntName());
            MyBmobUtils.addComment(CommentActivity.this, data, imageTalkItem, new SaveListener() {
                @Override
                public void onSuccess() {
                    commentAdapter.addItem(data);
                    commentAdapter.setAnimationsLocked(false);
                    commentAdapter.setDelayEnterAnimation(false);
//                    if (commentAdapter.getItemCount() != 0 && (commentList.getChildAt(0) != null)) {
//                        commentList.smoothScrollBy(0, commentList.getChildAt(0).getHeight() * commentAdapter.getItemCount());
//                    }
                    commentEdit.setText(null);
                    commentSendBtn.setCurrentState(SendCommentButton.STATE_DONE);
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }
    }

    private boolean validateComment() {
        if (TextUtils.isEmpty(commentEdit.getText())) {
            commentSendBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return false;
        }

        return true;
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onAvatorClick(View view, int position) {

    }
}
