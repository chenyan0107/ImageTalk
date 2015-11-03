package com.chen.cy.talkimage.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chen.cy.talkimage.R;
import com.chen.cy.talkimage.entity.CommentItemMsg;
import com.chen.cy.talkimage.views.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by C-y on 2015/10/30.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context context;
    private int itemsCount = 0;
    private int lastAnimatedPosition = -1;
    private int avatarSize;
    private ArrayList<CommentItemMsg> mDatas;

    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;

    private ImageLoader imageLoader;

    private OnCommentItemClick onCommentItemClick;
    public CommentAdapter(Context context) {
        this.context = context;
        avatarSize = context.getResources().getDimensionPixelSize(R.dimen.comment_avatar_size);
        this.mDatas = new ArrayList<>();
        imageLoader = ImageLoader.getInstance();
    }

    public CommentAdapter(Context context, ArrayList<CommentItemMsg> mDatas) {
        this.context = context;
        avatarSize = context.getResources().getDimensionPixelSize(R.dimen.comment_avatar_size);
        this.mDatas = mDatas;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        runEnterAnimation(viewHolder.itemView, position);
        CommentViewHolder holder = (CommentViewHolder) viewHolder;
        imageLoader.displayImage(mDatas.get(position).getImg(), holder.itemCommentAvatar);
        holder.itemCommentText.setText(mDatas.get(position).getComment());
        holder.itemCommentTime.setText(mDatas.get(position).getTime());
        holder.itemCommentName.setText(mDatas.get(position).getName());
        holder.itemCommentAvatar.setTag(position);
        holder.itemCommentRoot.setTag(position);
        holder.itemCommentAvatar.setOnClickListener(this);
        holder.itemCommentRoot.setOnClickListener(this);
    }

    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void updateItems() {
        itemsCount = 10;
        notifyDataSetChanged();
    }

    public void addItem(CommentItemMsg data) {
        mDatas.add(data);
        notifyItemInserted(mDatas.size());
    }

    public void setAnimationsLocked(boolean animationsLocked) {
        this.animationsLocked = animationsLocked;
    }

    public void setDelayEnterAnimation(boolean delayEnterAnimation) {
        this.delayEnterAnimation = delayEnterAnimation;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.item_comment_avatar){
            onCommentItemClick.onAvatorClick(v, (Integer)v.getTag());
        }
        else if (id == R.id.item_comment_root){
            onCommentItemClick.onItemClick(v, (Integer)v.getTag());
        }
    }


    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_comment_avatar)
        RoundImageView itemCommentAvatar;
        @Bind(R.id.item_comment_text)
        TextView itemCommentText;
        @Bind(R.id.item_comment_time)
        TextView itemCommentTime;
        @Bind(R.id.item_comment_name)
        TextView itemCommentName;
        @Bind(R.id.item_comment_root)
        RelativeLayout itemCommentRoot;

        public CommentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public interface OnCommentItemClick{
        void onItemClick(View view, int position);
        void onAvatorClick(View view, int position);
    }

    public void setOnCommentItemClick(OnCommentItemClick onCommentItemClick) {
        this.onCommentItemClick = onCommentItemClick;
    }

    public ArrayList<CommentItemMsg> getmDatas() {
        return mDatas;
    }
}
