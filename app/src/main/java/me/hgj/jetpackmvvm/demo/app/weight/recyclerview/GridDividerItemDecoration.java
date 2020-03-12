package me.hgj.jetpackmvvm.demo.app.weight.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
  * @Author:         hegaojian
  * @CreateDate:     2019/10/11 14:29
 */
public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private int mDividerWidth;//您所需指定的间隔宽度，主要为第一列和最后一列与父控件的间隔；行间距，列间距将动态分配
    private int mFirstRowTopMargin = 0; //第一行顶部是否需要间隔
    private boolean isNeedSpace = false;//第一列和最后一列是否需要指定间隔(默认不指定)
    private boolean isLastRowNeedSpace = false;//最后一行是否需要间隔(默认不需要)
    int spanCount = 0;
    private Context mContext;

    /**
     * @param dividerWidth 间隔宽度
     * @param isNeedSpace  第一列和最后一列是否需要间隔
     */
    public GridDividerItemDecoration(Context context, int dividerWidth, boolean isNeedSpace) {
        this(context, dividerWidth, 0, isNeedSpace, false);
    }

    /**
     * @param dividerWidth      间隔宽度
     * @param isNeedSpace       第一列和最后一列是否需要间隔
     * @param firstRowTopMargin 第一行顶部是否需要间隔(根据间隔大小判断)
     */
    public GridDividerItemDecoration(Context context, int dividerWidth, int firstRowTopMargin, boolean isNeedSpace) {
        this(context, dividerWidth, firstRowTopMargin, isNeedSpace, false);
    }

    /**
     * @param dividerWidth       间隔宽度
     * @param firstRowTopMargin  第一行顶部是否需要间隔
     * @param isNeedSpace        第一列和最后一列是否需要间隔
     * @param isLastRowNeedSpace 最后一行是否需要间隔
     */
    public GridDividerItemDecoration(Context context, int dividerWidth, int firstRowTopMargin, boolean isNeedSpace, boolean isLastRowNeedSpace) {
        this(context, dividerWidth, firstRowTopMargin, isNeedSpace, isLastRowNeedSpace, Color.WHITE);
    }

    /**
     * @param dividerWidth       间隔宽度
     * @param firstRowTopMargin  第一行顶部是否需要间隔
     * @param isNeedSpace        第一列和最后一列是否需要间隔
     * @param isLastRowNeedSpace 最后一行是否需要间隔
     */
    public GridDividerItemDecoration(Context context, int dividerWidth, int firstRowTopMargin, boolean isNeedSpace, boolean isLastRowNeedSpace, @ColorInt int color) {
        mDividerWidth = dividerWidth;
        this.isNeedSpace = isNeedSpace;
        this.mContext = context;
        this.isLastRowNeedSpace = isLastRowNeedSpace;
        this.mFirstRowTopMargin = firstRowTopMargin;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int top = 0;
        int left = 0;
        int right = 0;
        int bottom = 0;

        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        int maxAllDividerWidth = getMaxDividerWidth(view); //

        int spaceWidth = 0;//首尾两列与父布局之间的间隔
        if (isNeedSpace)
            spaceWidth = mDividerWidth;

        int eachItemWidth = maxAllDividerWidth / spanCount;//每个Item left+right
        int dividerItemWidth = (maxAllDividerWidth - 2 * spaceWidth) / (spanCount - 1);//item与item之间的距离

        left = itemPosition % spanCount * (dividerItemWidth - eachItemWidth) + spaceWidth;
        right = eachItemWidth - left;
        bottom = mDividerWidth;
        if (mFirstRowTopMargin > 0 && isFirstRow(parent, itemPosition, spanCount, childCount))//第一行顶部是否需要间隔
            top = mFirstRowTopMargin;
        if (!isLastRowNeedSpace && isLastRow(parent, itemPosition, spanCount, childCount)) {//最后一行是否需要间隔
            bottom = 0;
        }

        outRect.set(left, top, right, bottom);
    }

    /**
     * 获取Item View的大小，若无则自动分配空间
     * 并根据 屏幕宽度-View的宽度*spanCount 得到屏幕剩余空间
     *
     * @param view
     * @return
     */
    private int getMaxDividerWidth(View view) {
        int itemWidth = view.getLayoutParams().width;
        int itemHeight = view.getLayoutParams().height;

        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels > mContext.getResources().getDisplayMetrics().heightPixels
                ? mContext.getResources().getDisplayMetrics().heightPixels : mContext.getResources().getDisplayMetrics().widthPixels;

        int maxDividerWidth = screenWidth - itemWidth * spanCount;
        if (itemHeight < 0 || itemWidth < 0 || (isNeedSpace && maxDividerWidth <= (spanCount - 1) * mDividerWidth)) {
            view.getLayoutParams().width = getAttachCloumnWidth();
            view.getLayoutParams().height = getAttachCloumnWidth();

            maxDividerWidth = screenWidth - view.getLayoutParams().width * spanCount;
        }
        return maxDividerWidth;
    }

    /**
     * 根据屏幕宽度和item数量分配 item View的width和height
     *
     * @return
     */
    private int getAttachCloumnWidth() {
        int itemWidth = 0;
        int spaceWidth = 0;
        try {
            int width = mContext.getResources().getDisplayMetrics().widthPixels > mContext.getResources().getDisplayMetrics().heightPixels
                    ? mContext.getResources().getDisplayMetrics().heightPixels : mContext.getResources().getDisplayMetrics().widthPixels;
            if (isNeedSpace)
                spaceWidth = 2 * mDividerWidth;
            itemWidth = (width - spaceWidth) / spanCount - 40;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemWidth;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        draw(c, parent);
    }

    //绘制item分割线
    private void draw(Canvas canvas, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

            //画水平分隔线
            int left = child.getLeft();
            int right = child.getRight();
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mDividerWidth;
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
            //画垂直分割线
            top = child.getTop();
            bottom = child.getBottom() + mDividerWidth;
            left = child.getRight() + layoutParams.rightMargin;
            right = left + mDividerWidth;
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    /**
     * 判读是否是第一列
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isFirstColumn(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if (pos % spanCount == 0) {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if (pos % spanCount == 0) {// 第一列
                    return true;
                }
            } else {

            }
        }
        return false;
    }

    /**
     * 判断是否是最后一列
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isLastColumn(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0) {// 最后一列
                    return true;
                }
            } else {

            }
        }
        return false;
    }

    /**
     * 判读是否是最后一行
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isLastRow(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int lines = childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
            return lines == pos / spanCount + 1;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {

        }
        return false;
    }

    /**
     * 判断是否是第一行
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isFirstRow(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos / spanCount + 1) == 1) {
                return true;
            } else {
                return false;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {

        }
        return false;
    }

    /**
     * 获取列数
     *
     * @param parent
     * @return
     */
    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }
}
