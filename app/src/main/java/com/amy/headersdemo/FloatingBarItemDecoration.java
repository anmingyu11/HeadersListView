/**
 * Copyright 2017 ChenHao Dendi
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amy.headersdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Map;

public class FloatingBarItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private int mTitleHeight;
    private Paint mBackgroundPaint;
    private Paint mTextPaint;
    private int mTextHeight;
    private int mTextBaselineOffset;
    private int mTextStartMargin;
    /**
     * Integer means the related position of the Recyclerview#getViewAdapterPosition()
     * (the position of the view in original adapter's list)
     * String means the title to be drawn
     */
    private Map<Integer, String> mList;

    public FloatingBarItemDecoration(Context context, Map<Integer, String> list) {
        this.mContext = context;
        Resources resources = mContext.getResources();
        this.mList = list;
        setHeight(R.dimen.item_decoration_title_height);

        mBackgroundPaint = new Paint();
        setBackGroundColor(R.color.item_decoration_title_background);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        setTextColor(R.color.item_decoration_title_fontColor);
        setTextSize(R.dimen.item_decoration_title_fontSize);

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTextHeight = (int) (fm.bottom - fm.top);
        mTextBaselineOffset = (int) fm.bottom;
        mTextStartMargin = resources.getDimensionPixelOffset(R.dimen.item_decoration_title_start_margin);
    }

    public void updateHeaderList(Map<Integer, String> list) {
        this.mList = list;
    }

    public void setTextSize(@DimenRes final int textSize) {
        mTextPaint.setTextSize(mContext.getResources().getDimensionPixelSize(
                textSize
        ));
    }

    public void setTextColor(@ColorRes final int colorId) {
        mTextPaint.setColor(
                ContextCompat.getColor(mContext, colorId)
        );
    }

    public void setBackGroundColor(@ColorRes final int colorId) {
        mBackgroundPaint.setColor(ContextCompat.getColor(
                mContext, colorId
        ));
    }

    public void setHeight(@DimenRes final int dimenId) {
        mTitleHeight = mContext.getResources().getDimensionPixelSize(dimenId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //LogUtil.e("getItemOffsets");
/*        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        if (mList.containsKey(position)) {
            outRect.set(0, mTitleHeight, 0, 0);
        }*/
    }

    private boolean mLastMovingAnimationRunning = false;

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
/*

        BaseItemAnimator baseItemAnimator = (BaseItemAnimator) parent.getItemAnimator();

        boolean itemMovingAnimationRunning = baseItemAnimator.isAnimationMoving();

        if (itemMovingAnimationRunning && !mLastMovingAnimationRunning) {
            //Animator start
            doOnDraw(c, parent);
        } else if (itemMovingAnimationRunning && mLastMovingAnimationRunning) {
            //Animator running
*/
/*            float transY = baseItemAnimator.getViewTranslationY();
            float height = baseItemAnimator.getViewHeight();
            float dY = transY > 0 ? transY - height : height + transY;
            c.translate(0, dY);
 *//*

            doOnDraw(c, parent);
        } else if (!itemMovingAnimationRunning && mLastMovingAnimationRunning) {
            //Animator end
            doOnDraw(c, parent);
        } else {
            //Animator not running
            doOnDraw(c, parent);
        }

        mLastMovingAnimationRunning = itemMovingAnimationRunning;
*/

        super.onDraw(c, parent, state);
    }

    private void doOnDraw(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = params.getViewAdapterPosition();
            if (!mList.containsKey(position)) {
                continue;
            }
            drawTitleArea(c, left, right, child, params, position);
        }
    }

    private void drawTitleArea(Canvas c, int left, int right, View child,
                               RecyclerView.LayoutParams params, int position) {
        final int rectBottom = child.getTop() - params.topMargin;
        c.drawRect(left,
                rectBottom - mTitleHeight,
                right,
                rectBottom, mBackgroundPaint);
        c.drawText(mList.get(position),
                child.getPaddingLeft() + mTextStartMargin,
                rectBottom - (mTitleHeight - mTextHeight) / 2 - mTextBaselineOffset,
                mTextPaint);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final int position = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        View child = parent.findViewHolderForAdapterPosition(position).itemView;
        String initial = getTag(position);
        if (initial == null) {
            return;
        }

        boolean flag = false;
        if (getTag(position + 1) != null && !initial.equals(getTag(position + 1))) {
            if (child.getHeight() + child.getTop() < mTitleHeight) {
                c.save();
                flag = true;
                int dy = child.getHeight() + child.getTop() - mTitleHeight;
                c.translate(0, child.getHeight() + child.getTop() - mTitleHeight);
            }
        }

        c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(),
                parent.getRight() - parent.getPaddingRight(),
                parent.getPaddingTop() + mTitleHeight,
                mBackgroundPaint);
        c.drawText(initial,
                child.getPaddingLeft() + mTextStartMargin,
                parent.getPaddingTop() + mTitleHeight - (mTitleHeight - mTextHeight) / 2 - mTextBaselineOffset,
                mTextPaint);

        if (flag) {
            c.restore();
        }
    }

    private String getTag(int position) {
        while (position >= 0) {
            if (mList.containsKey(position)) {
                return mList.get(position);
            }
            position--;
        }
        return null;
    }
}

