package com.fatdaruma.diagonalgridlayoutmanager;

/*
 * Copyright (c) 2016 Recruit Marketing Partners Co., Ltd. All rights reserved.
 * Created by jmatsu on 2017/02/04.
 */

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class DiagonalScrollableGridLayoutManager extends GridLayoutManager {
    private static final String TAG = DiagonalScrollableGridLayoutManager.class.getSimpleName();

    private static final int BACKWARD = -1;
    private static final int STOPPED = 0;
    private static final int FORWARD = 1;

    @IntDef({BACKWARD, STOPPED, FORWARD})
    private @interface Direction {
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        RecyclerView.LayoutParams params = super.generateDefaultLayoutParams();
        params.width = 200;
        params.height = 200;
        return params;
    }

    private float scale = 1f;
    private int offset;

    /**
     * To emulate movement of paired up orientation
     */
    private OrientationHelper orientationHelper;

    public DiagonalScrollableGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initOrientationHelper();
    }

    public DiagonalScrollableGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
        initOrientationHelper();
    }

    public DiagonalScrollableGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
        initOrientationHelper();
    }

    private void initOrientationHelper() {
        orientationHelper = OrientationHelper.createOrientationHelper(this, isVertical() ? HORIZONTAL : VERTICAL);
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
        initOrientationHelper();
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    protected int getRecyclerViewWidth() {
        return super.getWidth();
    }

    protected int getRecyclerViewHeight() {
        return super.getHeight();
    }

    private boolean isVertical() {
        return getOrientation() == VERTICAL;
    }

    @Override
    public int getHeight() {
        if (isVertical()) {
            return getRecyclerViewHeight();
        } else {
            return getRecyclerViewHeight() * (int) (getSpanCount() / scale);
        }
    }

    @Override
    public int getWidth() {
        if (isVertical()) {
            return getRecyclerViewWidth() * (int) (getSpanCount() / scale);
        } else {
            return getRecyclerViewWidth();
        }
    }

    @Override
    public void offsetChildrenVertical(int dy) {
        super.offsetChildrenVertical(dy);
        if (!isVertical()) {
            offset += dy;
        }
    }

    @Override
    public void offsetChildrenHorizontal(int dx) {
        super.offsetChildrenHorizontal(dx);
        if (isVertical()) {
            offset += dx;
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (isVertical()) {
            return scrollBy(dx);
        } else {
            return super.scrollHorizontallyBy(dx, recycler, state);
        }
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (isVertical()) {
            return super.scrollVerticallyBy(dy, recycler, state);
        } else {
            return scrollBy(dy);
        }
    }

    @Override
    public void layoutDecoratedWithMargins(View child, int left, int top, int right, int bottom) {
        if (isVertical()) {
            super.layoutDecoratedWithMargins(child, left + offset, top, right + offset, bottom);
        } else {
            super.layoutDecoratedWithMargins(child, left, top + offset, right, bottom + offset);
        }
    }

    @Direction
    private int getDirection(int delta) {
        if (delta == 0) {
            return STOPPED;
        }

        return delta > 0 ? FORWARD : BACKWARD;
    }

    private int getHiddenLength() {
        if (isVertical()) {
            return getWidth() - getRecyclerViewWidth();
        } else {
            return getHeight() - getRecyclerViewHeight();
        }
    }

    private int scrollBy(int delta) {
        @Direction int direction = getDirection(delta);

        if (getChildCount() == 0 || direction == STOPPED) {
            return 0;
        }

        int scrollFrom = direction == FORWARD ? getHiddenLength() : 0;
        int scrollByCandidate = scrollFrom + this.offset * direction;

        if (scrollByCandidate > 0) {
            final int scrollBy;

            if (Math.abs(delta) > scrollByCandidate) {
                scrollBy = direction * scrollByCandidate;
            } else {
                scrollBy = delta;
            }

            orientationHelper.offsetChildren(-1 * scrollBy); // revert scrollBy
            return scrollBy;
        } else {
            return 0;
        }
    }
}
