package com.xtree.base.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.widget.TextView;

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
        } else if (!url.startsWith("http")) {
            url = DomainUtil.getDomain2() + "/" + url;
        } else {
            // 正常 url
        }
        CfLog.i("url: " + url);
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
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

    /**
     * 检测手机号是否规范 (HQAP2-3552) <br/>
     * 1开头共11位，不能是10、11或12开头
     *
     * @param num 手机号
     * @return true:是 false:否
     */
    public static boolean isPhone(String num) {
        String regex = "^1[3456789]\\d{9}$"; // 手机号
        return num.matches(regex);
    }

    /**
     * 检测QQ号是否规范
     * QQ号码至少包含5位数字的连续数字。
     *
     * @param num QQ
     * @return true:是 false:否
     */
    public static boolean isQQ(String num) {
        String regex = "^\\d{5,}$"; // QQ
        return num.matches(regex);
    }

    /**
     * 检测微信号是否规范
     * 微信号必须以字母、下划线或中划线开头，长度在6到20位之间，可以包含数字、字母、下划线和减号。
     *
     * @param num 微信
     * @return true:是 false:否
     */
    public static boolean isWX(String num) {
        String regex = "^[a-zA-Z_-][a-zA-Z0-9_-]{5,19}$"; // 微信
        return num.matches(regex);
    }

    /**
     * 检测邮箱是否规范 (HQAP2-3552)  <br/>
     * 依据Email正则表达式 ^\w+(-+.\w+)*@\w+(-.\w+)*.\w+(-.\w+)*$
     *
     * @param num 邮箱
     * @return true:是 false:否
     */
    public static boolean isEmail(String num) {
        String regex = "^[\\w]+([-+.][\\w]+)*@[\\w]+([-.][\\w]+)*\\.[\\w]+([-.][\\w]+)*$"; // 邮箱
        return num.matches(regex);
    }
    public static void setTypeFaceDin(Context ctx, TextView tvw) {
        AssetManager am = ctx.getAssets();
        Typeface tf = Typeface.createFromAsset(am, "fonts/D-DIN-PRO-500-Medium.ttf");
        tvw.setTypeface(tf);
    }


}