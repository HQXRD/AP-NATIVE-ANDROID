package com.xtree.base.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.xtree.base.global.Constant;

import me.xtree.mvvmhabit.utils.SPUtils;

public class AppUtil {

    /**
     * 跳转到客服
     *
     * @param ctx Context
     */
    public static void goCustomerService(Context ctx) {

        goBrowser(ctx, DomainUtil.getDomain2() + Constant.URL_CUSTOMER_SERVICE);
    }

    public static void goBrowser(Context ctx, String url) {
        if (ctx == null || url == null || url.isEmpty()) {
            return;
        }

        if (url.startsWith("/")) {
            url = DomainUtil.getDomain2() + url;
        }

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ctx.startActivity(intent);
    }

    /**
     * 今日是否弹窗
     *
     * @return true:默认弹提示, false:今日不弹提示
     */
    public static boolean isTipToday(String key) {
        String cacheDay = SPUtils.getInstance().getString(key, "");
        String today = TimeUtils.getCurDate();
        return !today.equals(cacheDay);
    }
}