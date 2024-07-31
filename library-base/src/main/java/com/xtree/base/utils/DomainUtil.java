package com.xtree.base.utils;

import android.text.TextUtils;

import com.xtree.base.global.SPKeyGlobal;

import me.xtree.mvvmhabit.utils.SPUtils;

public class DomainUtil {

    private static String apiUrl = "https://app.zgntcpgxds.com:16801"; // API 用
    private static String h5Url = "https://h55.y7t9j5.com"; // 网页用

    /**
     * 获取域名, API 使用
     *
     * @return url
     */
    public static String getApiUrl() {
        //apiUrl = "https://pre-dsport.oxldkm.com"; // 测试/内网 环境
        //apiUrl = "https://app1.vcchgk.com"; // 生产环境
        String url = SPUtils.getInstance().getString(SPKeyGlobal.KEY_API_URL, apiUrl);

        //有调试域名优先使用调试域名
        String debugUrl = SPUtils.getInstance().getString(SPKeyGlobal.DEBUG_APPLY_DOMAIN);
        if (!TextUtils.isEmpty(debugUrl)) {
            return debugUrl;
        }

        return url;
    }

    public static void setApiUrl(String url) {
        CfLog.i("url: " + url);

        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
            apiUrl = url;
            SPUtils.getInstance().put(SPKeyGlobal.KEY_API_URL, apiUrl);
        }
    }

    /**
     * 获取域名, 网页/图片 使用
     * 有 / 结尾
     *
     * @return url
     */
    public static String getH5Domain() {
        String url = SPUtils.getInstance().getString(SPKeyGlobal.KEY_H5_URL, h5Url);
        String debugUrl = SPUtils.getInstance().getString(SPKeyGlobal.DEBUG_APPLY_DOMAIN);
        if (!TextUtils.isEmpty(debugUrl)) {
            return debugUrl+ "/";
        }
        return url + "/";
    }

    /**
     * 获取域名, 网页/图片 使用
     *
     * @return url
     */
    public static String getH5Domain2() {
        String url = SPUtils.getInstance().getString(SPKeyGlobal.KEY_H5_URL, h5Url);
        String debugUrl = SPUtils.getInstance().getString(SPKeyGlobal.DEBUG_APPLY_DOMAIN);
        if (!TextUtils.isEmpty(debugUrl)) {
            return debugUrl;
        }
        return url; //.substring(0, domainUrl.length() - 1);
    }

    public static void setH5Url(String url) {
        CfLog.i("url: " + url);
        // 设置域名，此处做各种判断
        if (!TextUtils.isEmpty(url) && url.startsWith("http") && url.length() > 10) {
            if (url.endsWith("/")) {
                h5Url = url.substring(0, url.length() - 1);
            } else {
                h5Url = url;
            }
            SPUtils.getInstance().put(SPKeyGlobal.KEY_H5_URL, h5Url);
            CfLog.i("domainUrl: " + h5Url);
        }
    }

}
