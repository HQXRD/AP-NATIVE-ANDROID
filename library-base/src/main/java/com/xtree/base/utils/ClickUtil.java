package com.xtree.base.utils;

public class ClickUtil {
    // 两次点击按钮之间的点击间隔不能少于1500毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1500;
    //上一次点击的时间
    private static long lastClickTime;
    private static long lastClickTime2;

    /**
     * 限制快速点击
     */
    public static boolean isFastClick() {
        long curClickTime = System.currentTimeMillis();
        if (Math.abs((curClickTime - lastClickTime)) < MIN_CLICK_DELAY_TIME) {
            return true;
        }
        lastClickTime = curClickTime;
        return false;
    }
    /**
     * 限制投注快速点击（可设置时间），只有投注按钮可用
     */
    public static boolean isFastClick(long time) {
        long curClickTime = System.currentTimeMillis();
        if (Math.abs((curClickTime - lastClickTime2)) < time) {
            return true;
        }
        lastClickTime2 = curClickTime;
        return false;
    }

}
