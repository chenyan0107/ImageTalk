package com.chen.cy.talkimage.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bmob.btp.callback.UploadBatchListener;
import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.application.BaseApplication;
import com.chen.cy.talkimage.baseUtils.ScreenUtils;
import com.chen.cy.talkimage.entity.ImageTalkItem;
import com.chen.cy.talkimage.entity.ItemMsg;
import com.chen.cy.talkimage.entity.MyUser;
import com.chen.cy.talkimage.frame.HomeControler;
import com.chen.cy.talkimage.frame.HomeHotFrame;
import com.chen.cy.talkimage.utils.ImageUtils;
import com.chen.cy.talkimage.utils.MyBmobUtils;
import com.chen.cy.talkimage.utils.RecordUtils;
import com.chen.cy.talkimage.utils.ToastManager;
import com.chen.cy.talkimage.views.RoundImageView;
import com.chen.cy.talkimage.views.SendingProgressView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnClickListener {

    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int VIEW_TYPE_LOADER = 2;
    private ArrayList<ItemMsg> mDatas;
    private LayoutInflater mInflater;

    private int loadingViewSizeX = ScreenUtils.dpToPx(130);
    private int loadingViewSizeY = ScreenUtils.dpToPx(130);

    private Context context;

    private boolean showLoadingView = false;

    private Uri imageUri;
    private File recordFile;
    private MyUser myUser;

    private SendingProgressView sendingProgressView;
    private int progress;

    private ImageLoader imageLoader;
    private int loadCount;
    private boolean loadedEnd;

    private final Map<Integer, Integer> likesCount = new HashMap<>();
    private final Map<RecyclerView.ViewHolder, AnimatorSet> likeAnimations = new HashMap<>();
    private final ArrayList<Integer> likedPositions = new ArrayList<>();

    private OnItemClickLitener onItemClickLitener;



    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public HomeAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mDatas = new ArrayList<>();
    }

    public HomeAdapter(Context context, ArrayList<ItemMsg> datas) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        myUser = BmobUser.getCurrentUser(context, MyUser.class);
        imageLoader = ImageLoader.getInstance();
        loadCount = 1;
        loadedEnd = false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("bmob", "onCreateViewHolder ");
        if (viewType == TYPE_ITEM) {
            return addOtherView(parent);
        } else if (viewType == TYPE_FOOTER) {
            return addFootView(parent);
        } else if (viewType == VIEW_TYPE_LOADER) {
            final View view = LayoutInflater.from(context).inflate(R.layout.item_home_list, parent, false);
            final MyViewHolder myViewHolder = new MyViewHolder(view);
            View bgView = new View(context);
            bgView.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            ));
            bgView.setBackgroundColor(0x77ffffff);
            myViewHolder.itemImgRoot.addView(bgView);
            myViewHolder.vProgressBg = bgView;

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(loadingViewSizeX, loadingViewSizeY);
            params.gravity = Gravity.CENTER;
            SendingProgressView sendingProgressView = new SendingProgressView(context);
            sendingProgressView.setLayoutParams(params);
            myViewHolder.itemImgRoot.addView(sendingProgressView);
            myViewHolder.vSendingProgress = sendingProgressView;
            this.sendingProgressView = sendingProgressView;
            Log.e("progressTest", "onCreateViewHolder ");
            return myViewHolder;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else if (showLoadingView && position == 0) {
            return VIEW_TYPE_LOADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            Log.e("bmob", "onBindViewHolder"+((MyViewHolder) holder).itemHomeTitle.getText());
            setViewHolder((MyViewHolder) holder, position);
        } else if (getItemViewType(position) == TYPE_FOOTER) {
            setFootViewHolder((MyFootViewHolder) holder, position);
        } else if (getItemViewType(position) == VIEW_TYPE_LOADER) {
            final MyViewHolder myView = (MyViewHolder) holder;
            bindLoadingFeedItem(myView);
        }
    }

    private void bindLoadingFeedItem(final MyViewHolder holder) {
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "italk/" + ImageUtils.IMAGE_FILE_NAME));
        recordFile = new File(Environment.getExternalStorageDirectory().toString(), "italk/rec/" + RecordUtils.RECORD_FILE_NAME);//创建自已项目 文件夹
        Log.e("progressTest", "bindLoadingFeedItem ");
        imageLoader.displayImage(imageUri.getPath(), holder.itemHomeImg);
        imageLoader.displayImage(myUser.getUserImg().getUrl(), holder.itemHomeHead);
        holder.itemHomeName.setText(myUser.getUserIntName());
        holder.itemHomeTime.setText("");
        holder.itemPraiseText.setText("0");
        holder.itemSpeakText.setText("0");
        holder.vSendingProgress.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                holder.vSendingProgress.getViewTreeObserver().removeOnPreDrawListener(this);
                holder.vSendingProgress.simulateProgress();
                return true;
            }
        });
        holder.vSendingProgress.setOnLoadingFinishedListener(new SendingProgressView.OnLoadingFinishedListener() {
            @Override
            public void onLoadingFinished() {
//                holder.vSendingProgress.animate().scaleY(0).scaleX(0).setDuration(200).setStartDelay(100);
                holder.vProgressBg.animate().alpha(0.f).setDuration(200).setStartDelay(100)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                holder.vSendingProgress.setScaleX(1);
                                holder.vSendingProgress.setScaleY(1);
                                holder.vProgressBg.setAlpha(1);
                                for (int i = 0; i < likedPositions.size(); i++) {
                                    likedPositions.set(i, likedPositions.get(i) + 1);
                                }
                                notifyItemChanged(0);
                            }
                        })
                        .start();
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(final RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.e("isEnd", "loadedEnd=" + loadedEnd);
        if ((holder.getItemViewType() == TYPE_FOOTER) && loadedEnd == false) {
            loadMore((MyFootViewHolder) holder);
        }
    }

    private void loadMore(final MyFootViewHolder holder) {
        MyBmobUtils.SearchData(context, new FindListener<ImageTalkItem>() {
            @Override
            public void onSuccess(final List<ImageTalkItem> list) {
                if (list.size() == 0) {
                    holder.pullToRefreshLoadProgress.setVisibility(View.GONE);
                    holder.pullToRefreshLoadmoreText.setText("已经加载完了");
                    loadedEnd = true;
                    onViewAttachedToWindow(holder);
                } else {
                    int oldItem = mDatas.size();
                    for (final ImageTalkItem imageTalkItem : list) {
                        final ItemMsg item = new ItemMsg();
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
                        mDatas.add(mDatas.size(), item);
                    }
                    notifyItemRangeInserted(oldItem, list.size());
                    loadCount++;
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        }, loadCount * MyBmobUtils.loadCount);
    }

    //设置item内的内容
    private void setViewHolder(final MyViewHolder holder, final int position) {
        final int pos = holder.getLayoutPosition();
        imageLoader.displayImage(mDatas.get(pos).getHeadImgId(), holder.itemHomeHead, BaseApplication.options);
        holder.itemHomeName.setText(mDatas.get(pos).getName());
        holder.itemHomeTime.setText(mDatas.get(pos).getTime());
        holder.itemHomeImg.setTag(mDatas.get(pos).getImg());
        imageLoader.displayImage(mDatas.get(pos).getImg(), holder.itemHomeImg, BaseApplication.options);
        holder.itemPraiseText.setText(mDatas.get(pos).getPraiseText());
        holder.itemSpeakText.setText(mDatas.get(pos).getSpeakeText());
        holder.itemHomeTitle.setText(mDatas.get(pos).getTitle());
        if (mDatas.get(pos).getIsPraised() == ItemMsg.PRAISED_NULL){
            MyBmobUtils.ComparisonParise(context, mDatas.get(pos).getImageTalkItem(), new CountListener() {
                @Override
                public void onSuccess(int i) {
                    likedPositions.add(pos);
                    updateHeartButton(holder, false);
                    mDatas.get(pos).setIsPraised(ItemMsg.PRAISED_YES);
                }

                @Override
                public void onFailure(int i, String s) {
                    updateHeartButton(holder, false);
                    mDatas.get(pos).setIsPraised(ItemMsg.PRAISED_NO);
                }
            });
        }else{
            updateHeartButton(holder, false);
        }

        holder.itemSpeakImg.setTag(pos);
        holder.itemPraiseImg.setTag(holder);
        holder.itemHomeImg.setTag(pos);
        likesCount.put(holder.getPosition(), Integer.parseInt(mDatas.get(pos).getPraiseText()));
        if (likeAnimations.containsKey(holder)) {
            likeAnimations.get(holder).cancel();
        }
        resetLikeAnimationState(holder);
    }

    private void setFootViewHolder(final MyFootViewHolder holder, final int position) {
        if (loadedEnd == false) {
            holder.pullToRefreshLoadProgress.setVisibility(View.VISIBLE);
            holder.pullToRefreshLoadmoreText.setText("正在拼命加载...");
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size() + 1;
    }


    public void addData(final int position, final ItemMsg item) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDatas.add(position, item);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = position;
                mHandler.sendMessage(msg);
            }
        }).start();
    }


    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public RecyclerView.ViewHolder addFootView(ViewGroup parent) {
        MyFootViewHolder holder = new MyFootViewHolder(mInflater.inflate(
                R.layout.listview_footer, parent, false));
        return holder;
    }

    public MyViewHolder addOtherView(ViewGroup parent) {
        MyViewHolder holder = new MyViewHolder(mInflater.inflate(
                R.layout.item_home_list, parent, false));
        holder.itemPraiseImg.setOnClickListener(this);
        holder.itemSpeakImg.setOnClickListener(this);
        holder.itemHomeImg.setOnClickListener(this);
        return holder;
    }

    public void removeFootView() {

    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_home_list.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_home_root)
        CardView itemHomeRootLayout;
        @Bind(R.id.item_home_head)
        RoundImageView itemHomeHead;
        @Bind(R.id.item_home_name)
        TextView itemHomeName;
        @Bind(R.id.item_home_time)
        TextView itemHomeTime;
        @Bind(R.id.item_home_img)
        ImageView itemHomeImg;
        @Bind(R.id.item_home_bottom_praise_img)
        ImageView itemPraiseImg;
        @Bind(R.id.item_home_bottom_praise_text)
        TextView itemPraiseText;
        @Bind(R.id.item_home_bottom_speak_img)
        ImageView itemSpeakImg;
        @Bind(R.id.item_home_bottom_speak_text)
        TextView itemSpeakText;
        @Bind(R.id.item_home_img_root)
        FrameLayout itemImgRoot;
        @Bind(R.id.item_home_title)
        TextView itemHomeTitle;

        SendingProgressView vSendingProgress;
        View vProgressBg;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class MyFootViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.pull_to_refresh_load_progress)
        ProgressBar pullToRefreshLoadProgress;
        @Bind(R.id.pull_to_refresh_loadmore_text)
        TextView pullToRefreshLoadmoreText;

        public MyFootViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    notifyItemInserted(Integer.parseInt(msg.obj.toString()));
                    break;
                default:
                    break;
            }
        }
    };

    private void updateFile(final String title) {
        final String[] files = {imageUri.getPath(), recordFile.getAbsolutePath()};
        final HomeControler homeControler = HomeControler.getInstance(context);
        homeControler.uploadFils(files, new UploadBatchListener() {
            @Override
            public void onSuccess(boolean b, String[] strings, String[] strings1, final BmobFile[] bmobFiles) {
                Log.e("progressTest", "onSuccess ");
                final ImageTalkItem imageTalkItem = new ImageTalkItem();
                imageTalkItem.setItUser(myUser);
                imageTalkItem.setItImage(bmobFiles[0]);
                imageTalkItem.setItAudio(bmobFiles[1]);
                imageTalkItem.setIsHot(false);
                imageTalkItem.setItTitl(title);
                imageTalkItem.setCommentCount("0");
                imageTalkItem.setPraiseCount("0");
                homeControler.addImageTalkItem(imageTalkItem, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        if (sendingProgressView != null) {
                            sendingProgressView.changeState(SendingProgressView.STATE_DONE_STARTED);
                            showLoadingView = false;
                            mDatas.get(0).setImageTalkItem(imageTalkItem);
                            mDatas.get(0).setTime(imageTalkItem.getCreatedAt());
                            mDatas.get(0).setName(imageTalkItem.getItUser().getUserIntName());
                            mDatas.get(0).setImg(imageTalkItem.getItImage().getUrl());
                            mDatas.get(0).setHeadImgId(imageTalkItem.getItUser().getUserImg().getUrl());
                            mDatas.get(0).setTitle(imageTalkItem.getItTitl());
                            mDatas.get(0).setSpeakeText(imageTalkItem.getCommentCount());
                            mDatas.get(0).setPraiseText(imageTalkItem.getPraiseCount());
                        }
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
                if (sendingProgressView != null) {
                    sendingProgressView.changeState(SendingProgressView.STATE_PROGRESS_STARTED);
                    int temp = (100 / i2) * (i - 1) + (i1 / i2);
                    sendingProgressView.setCurrentProgress(temp);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


    public void showLoadingView(String title) {
        myUser = BmobUser.getCurrentUser(context, MyUser.class);
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "italk/" + ImageUtils.IMAGE_FILE_NAME));
        recordFile = new File(Environment.getExternalStorageDirectory().toString(), "italk/rec/" + RecordUtils.RECORD_FILE_NAME);//创建自已项目 文件夹
        if (imageUri != null && recordFile != null) {
            progress = 0;
            showLoadingView = true;
            ItemMsg item;
            item = new ItemMsg();
            item.setHeadImgId(myUser.getUserImg().getUrl());
            item.setName(myUser.getUserIntName());
//            item.setImg(imageUri.getPath());
            item.setTitle(title);
            item.setTime("");
            item.setPraiseText("0");
            item.setSpeakeText("0");
            mDatas.add(0, item);
            notifyItemInserted(0);
            HomeHotFrame.ScrollTop();
            updateFile(title);
        }
    }

    private void updateHeartButton(final MyViewHolder holder, boolean animated) {
        if (animated) {
            if (!likeAnimations.containsKey(holder)) {
                AnimatorSet animatorSet = new AnimatorSet();
                likeAnimations.put(holder, animatorSet);

                ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.itemPraiseImg, "rotation", 0f, 360f);
                rotationAnim.setDuration(300);
                rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

                ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.itemPraiseImg, "scaleX", 0.2f, 1f);
                bounceAnimX.setDuration(300);
                bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

                ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.itemPraiseImg, "scaleY", 0.2f, 1f);
                bounceAnimY.setDuration(300);
                bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
                bounceAnimY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        holder.itemPraiseImg.setImageResource(R.mipmap.ic_heart_red);
                    }
                });

                animatorSet.play(rotationAnim);
                animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        resetLikeAnimationState(holder);
                    }
                });

                animatorSet.start();
            }
        } else {
            if (likedPositions.contains(holder.getPosition())) {
                holder.itemPraiseImg.setImageResource(R.mipmap.ic_heart_red);
            } else {
                holder.itemPraiseImg.setImageResource(R.mipmap.ic_heart_outline_grey);
            }
        }
    }

    private void resetLikeAnimationState(MyViewHolder holder) {
        likeAnimations.remove(holder);
    }

    private void updateLikesCounter(MyViewHolder holder, boolean animated) {
        int currentLikesCount = likesCount.get(holder.getPosition()) + 1;
        holder.itemPraiseText.setText(currentLikesCount+"");
        likesCount.put(holder.getPosition(), currentLikesCount);
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        if (viewId == R.id.item_home_bottom_praise_img) {
            final MyViewHolder holder = (MyViewHolder) view.getTag();
            if (!likedPositions.contains(holder.getPosition())) {
                likedPositions.add(holder.getPosition());
                updateLikesCounter(holder, true);
                updateHeartButton(holder, true);
                MyBmobUtils.AddPrise(context, mDatas.get(holder.getAdapterPosition()).getImageTalkItem(), new UpdateListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        likedPositions.remove(likedPositions.size());
                        updateHeartButton(holder, false);
                        int currentLikesCount = likesCount.get(holder.getPosition()) - 1;
                        holder.itemPraiseText.setText(currentLikesCount+"");
                        likesCount.put(holder.getPosition(), currentLikesCount);
                        ToastManager.getInstance(context).makeToast("点赞失败");
                    }
                });
            }
        }
        else if(viewId == R.id.item_home_bottom_speak_img){
            Log.e("bmob","dianle ");
            if (onItemClickLitener != null) {
                onItemClickLitener.onCommentClick(view, (Integer) view.getTag());
            }
        }
        else if (viewId == R.id.item_home_img){
            if (onItemClickLitener != null) {
                onItemClickLitener.onImageClick(view, (Integer) view.getTag());
            }
        }
    }


    public interface OnItemClickLitener {
        void onLikeClick(View view, int position);

        void onCommentClick(View view, int position);

        void onImageClick(View view, int position);

        void onHeadImageClick(View view, int position);

        void onPlayMusicClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickLitener onItemClickListener) {
        this.onItemClickLitener = onItemClickListener;
    }

    public ArrayList<ItemMsg> getmDatas() {
        return mDatas;
    }
}