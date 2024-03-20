package com.xtree.base.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.xtree.base.global.Constant;

public class AppUtil {

    /**
     * 跳转到客服
     *
     * @param ctx Context
     */
    public static void goCustomService(Context ctx) {

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

}