package com.xtree.base.utils;

import android.text.TextUtils;

public class DomainUtil {

    private static String apiUrl = "https://app.zgntcpgxds.com:16801"; // API 用
    private static String domainUrl = "https://h55.y7t9j5.com"; // 网页用

    /**
     * 获取域名, API 使用
     *
     * @return url
     */
    public static String getApiUrl() {
        //apiUrl = "https://pre-dsport.oxldkm.com"; // 测试/内网 环境
        //apiUrl = "https://app1.vcchgk.com"; // 生产环境
        return apiUrl;
    }

    public static void setApiUrl(String url) {
        CfLog.i("url: " + url);
        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
            apiUrl = url;
        }
    }

    /**
     * 获取域名, 网页/图片 使用
     * 有 / 结尾
     *
     * @return url
     */
    public static String getDomain() {

        return domainUrl + "/";
    }

    /**
     * 获取域名, 网页/图片 使用
     *
     * @return url
     */
    public static String getDomain2() {
        return domainUrl; //.substring(0, domainUrl.length() - 1);
    }

    public static void setDomainUrl(String url) {
        CfLog.i("url: " + url);
        // 设置域名，此处做各种判断
        if (!TextUtils.isEmpty(url) && url.startsWith("http") && url.length() > 10) {
            if (url.endsWith("/")) {
                domainUrl = url.substring(0, url.length() - 1);
            } else {
                domainUrl = url;
            }

            CfLog.i("domainUrl: " + domainUrl);
        }
    }

}
