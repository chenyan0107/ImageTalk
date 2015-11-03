package com.chen.cy.talkimage.views;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by C-y on 2015/10/14.
 */
public class MyRecycleView extends RecyclerView{

    public MyRecycleView(Context context) {
        super(context);
    }

    public MyRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isReadyForPullEnd() {
        int lastVisiblePostion = this.getChildAdapterPosition(this.getChildAt(this.getChildCount() -1));
        if (lastVisiblePostion >= this.getAdapter().getItemCount() - 1) {
            return this.getChildAt(this.getChildCount() - 1).getBottom()
                    <= this.getBottom();
        }
        return false;
    }

    public boolean isReadyForPullStart() {
        if (this.getChildCount() <= 0) return true;
        int firstVisiblePosition = this.getChildAdapterPosition(this.getChildAt(0));
        if (firstVisiblePosition == 0) {
            return this.getChildAt(0).getTop() == this.getPaddingTop();
        } else {
            return false;
        }
    }
}
