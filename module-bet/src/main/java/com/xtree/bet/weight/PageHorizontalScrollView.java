package com.xtree.bet.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class PageHorizontalScrollView extends HorizontalScrollView {
    /**
     * 子视图个数
     */
    private int subChildCount = 1;
    private int downX = 0;
    private int currentPage = 0;

    private OnPageSelectedListener onPageSelectedListener;

    private OnScrollListener onScrollListener;

    public void setChildCount(int subChildCount) {
        this.subChildCount = subChildCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setOnPageSelectedListener(OnPageSelectedListener onPageSelectedListener) {
        this.onPageSelectedListener = onPageSelectedListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     * 构造方法
     * @author caizhiming
     */
    public PageHorizontalScrollView(Context context, AttributeSet attrs,
                                    int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public PageHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public PageHorizontalScrollView(Context context) {
        super(context);
        init();
    }
    private void init() {
        setHorizontalScrollBarEnabled(false);//设置原有的滚动无效
    }

    /**
     * 触摸监听时间
     * @author caizhiming
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if(downX == 0){
                    downX = (int)ev.getX();
                    if(onScrollListener != null) {
                        onScrollListener.onScrolling();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (Math.abs((ev.getX() - downX)) > getWidth() / 6) {

                    if (ev.getX() - downX > 0) {
                        smoothScrollToPrePage();
                    } else {
                        smoothScrollToNextPage();
                    }
                } else {
                    smoothScrollToCurrent();
                }
                downX = 0;
                if(onScrollListener != null) {
                    onScrollListener.onScrolled();
                }
                return true;
            }
        }
        return super.onTouchEvent(ev);
    }
    /**
     * 滑动到当前页
     * @author caizhiming
     */
    private void smoothScrollToCurrent() {
        if(currentPage == 0) {
            smoothScrollTo(0, 0);
        }else {
            smoothScrollTo(getWidth(), 0);
        }
        if(onPageSelectedListener != null){
            onPageSelectedListener.onPageSelected(currentPage);
        }
    }
    /**
     * 滑动到下一页
     * @author caizhiming
     */
    private void smoothScrollToNextPage() {
        if (currentPage < subChildCount - 1) {
            currentPage++;
            smoothScrollTo(getWidth(), 0);
            if(onPageSelectedListener != null){
                onPageSelectedListener.onPageSelected(currentPage);
            }
        }
    }
    /**
     * 滑动到上一页
     * @author caizhiming
     */
    private void smoothScrollToPrePage() {
        if (currentPage > 0) {
            currentPage--;
            smoothScrollTo(0, 0);
            if(onPageSelectedListener != null){
                onPageSelectedListener.onPageSelected(currentPage);
            }
        }
    }

    public interface OnPageSelectedListener{
        void onPageSelected(int currentPage);
    }

    public interface OnScrollListener{
        void onScrolled();
        void onScrolling();
    }
}
