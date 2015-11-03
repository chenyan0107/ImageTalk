package com.chen.cy.talkimage.frame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.activity.ImageTalkActivity;
import com.chen.cy.talkimage.activity.ItemInsert.RecordActivity;
import com.chen.cy.talkimage.activity.MainActivity;
import com.chen.cy.talkimage.activity.other.CommentActivity;
import com.chen.cy.talkimage.adapter.HomeAdapter;
import com.chen.cy.talkimage.adapter.HomeAdapter.OnItemClickLitener;
import com.chen.cy.talkimage.entity.ImageTalkItem;
import com.chen.cy.talkimage.entity.ItemMsg;
import com.chen.cy.talkimage.entity.MyUser;
import com.chen.cy.talkimage.message.entity.MyPublishEvent;
import com.chen.cy.talkimage.utils.CheckUtils;
import com.chen.cy.talkimage.utils.ImageUtils;
import com.chen.cy.talkimage.utils.MyBmobUtils;
import com.chen.cy.talkimage.views.BaseFrame;
import com.chen.cy.talkimage.views.MyRecycleView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import de.greenrobot.event.EventBus;

/**
 * 主页页面
 * Created by C-y on 2015/10/9.
 */
public class HomeHotFrame extends BaseFrame implements OnItemClickLitener {
    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";

    private MyUser myUser;

    private MainActivity mainActivity;
    @Bind(R.id.rvToDoList)
    MyRecycleView mRecyclerView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @Bind(R.id.loadView)
    View loadView;

    private HomeAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private View contact;

    private ArrayList<ItemMsg> mData;
    private ItemMsg tempItem;
    private String imageUri;

    public static boolean fabInScreen = true;

    private EventBus eventBus;

    public static RecyclerView tempRecycler;

    public static HomeHotFrame newInstance() {
        return new HomeHotFrame();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contact = inflater.inflate(R.layout.frame_home_hot, container, false);
        ButterKnife.bind(this, contact);
        mainActivity = (MainActivity) getActivity();
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        tempRecycler = mRecyclerView;
        init();
        return contact;
    }

    private void init() {
        initView();
        initAdapter();
        initData();
    }

    private void initData() {
        myUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
    }

    private void initView() {
        swipeLayout.setColorSchemeResources(R.color.support_color);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initAdapter();
                        mHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });
        mainActivity.getFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.getInstance().chooseImage(HomeHotFrame.this);
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void initAdapter() {
        mData = new ArrayList<>();
        getIntData(new FindListener<ImageTalkItem>() {
            @Override
            public void onSuccess(List<ImageTalkItem> list) {
                loadView.setVisibility(View.GONE);
                mAdapter = new HomeAdapter(getActivity(), mData);
                Log.e("bmob", "mData=" + mData);
                //添加动画
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                //添加分割线（纵向）
                //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
                //添加分割线(网格)
                //mRecyclerView.addItemDecoration(new DividerGradItemDecoration(this));
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(HomeHotFrame.this);
                //设置Rec样式
                //LinearLayoutManager 线性管理器，支持横向、纵向。
                //GridLayoutManager 网格布局管理器
                //StaggeredGridLayoutManager 瀑布就式布局管理器
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                //mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
                //mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,        StaggeredGridLayoutManager.VERTICAL));
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (dy > 10 && fabInScreen) {
                            mainActivity.TranslationFabOutScreen();
                            fabInScreen = false;
                        } else if (dy < -10 && !fabInScreen) {
                            mainActivity.TranslationFabInScreen();
                            fabInScreen = true;
                        }
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == ImageUtils.RESULT_CANCELED) {
            Toast.makeText(getActivity(), "取消", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case ImageUtils.CODE_GALLERY_REQUEST:
                ImageUtils imageUtils = ImageUtils.getInstance();
                imageUtils.setOutput_X(1120);
                imageUtils.setOutput_Y(630);
                imageUtils.setCropX(16);
                imageUtils.setCropY(9);
                ImageUtils.getInstance().cropRawPhoto(intent.getData(), this);
                break;

            case ImageUtils.CODE_CAMERA_REQUEST:
                if (CheckUtils.hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            ImageUtils.IMAGE_FILE_NAME);
                    imageUri = Uri.fromFile(tempFile).toString();
                    ImageUtils.getInstance().cropRawPhoto(Uri.fromFile(tempFile), this);
                } else {
                    Toast.makeText(getActivity(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;

            case ImageUtils.CODE_RESULT_REQUEST:
                if (intent != null) {
//                    setImageToHeadView(intent);
                    addItem(intent);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    private void addItem(Intent intent) {
        Intent intent1 = new Intent(getActivity(), RecordActivity.class);
        intent1.putExtra("img", imageUri);
        startActivity(intent1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    swipeLayout.setRefreshing(false);
                    mRecyclerView.smoothScrollToPosition(0);
                    //swipeRefreshLayout.setEnabled(false);
                    break;
                default:
                    break;
            }
        }
    };

    public void onEventMainThread(MyPublishEvent event) {
        if (event.getMsg().equals(ACTION_SHOW_LOADING_ITEM)) {
            showFeedLoadingItemDelayed(event.getTitle());
        }
    }

    private void showFeedLoadingItemDelayed(final String title) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("bmob", "showFeedLoadingItemDelayed()");
                mAdapter.showLoadingView(title);
//                mRecyclerView.smoothScrollToPosition(0);
            }
        }, 0);
    }

    public static void ScrollTop() {
        tempRecycler.smoothScrollToPosition(0);
    }

    public void getIntData(final FindListener<ImageTalkItem> findListener) {
        MyBmobUtils.SearchData(getActivity(), new FindListener<ImageTalkItem>() {
            @Override
            public void onSuccess(List<ImageTalkItem> list) {
                for (ImageTalkItem imageTalkItem : list) {
                    ItemMsg item = new ItemMsg();
                    item.setHeadImgId(imageTalkItem.getItUser().getUserImg().getUrl());
                    item.setTitle(imageTalkItem.getItTitl());
                    item.setName(imageTalkItem.getItUser().getUserIntName());
                    item.setImg(imageTalkItem.getItImage().getUrl());
                    item.setRecordFile(imageTalkItem.getItAudio().getUrl());
                    item.setTime(imageTalkItem.getCreatedAt());
                    item.setPraiseText(imageTalkItem.getPraiseCount());
                    item.setSpeakeText(imageTalkItem.getCommentCount());
                    item.setImageTalkItem(imageTalkItem);
                    item.setIsPraised(ItemMsg.PRAISED_NULL);
                    Log.e("searchTest", "item=" + item.toString());
                    mData.add(item);
                }
                findListener.onSuccess(list);
            }

            @Override
            public void onError(int i, String s) {
                findListener.onError(i, s);
            }
        }, 0);
    }

    @Override
    public void onLikeClick(View view, int position) {

    }

    @Override
    public void onCommentClick(View view, int position) {
        final Intent intent = new Intent(getActivity(), CommentActivity.class);
        int[] startingLocation = new int[2];
        view.getLocationOnScreen(startingLocation);
        Bundle bundle = new Bundle();
        bundle.putSerializable("id", mAdapter.getmDatas().get(position).getImageTalkItem());
        intent.putExtra(CommentActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void onImageClick(View view, int position) {
        final Intent intent = new Intent(getActivity(), ImageTalkActivity.class);
        int[] startingLocation = new int[2];
        view.getLocationOnScreen(startingLocation);
        Bundle bundle = new Bundle();
        bundle.putSerializable("id", mAdapter.getmDatas().get(position).getImageTalkItem());
        intent.putExtra(CommentActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onHeadImageClick(View view, int position) {

    }

    @Override
    public void onPlayMusicClick(View view, int position) {

    }
}
