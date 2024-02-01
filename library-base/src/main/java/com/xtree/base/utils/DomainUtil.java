package com.xtree.base.utils;

import android.text.TextUtils;

public class DomainUtil {

    private static String domainUrl = "https://www.weres.bar/";

    public static String getDomain() {

        //domainUrl = "https://app1.dhtjf656.com/";
        //domainUrl = "https://ap3sport.oxldkm.com/"; // 测试/内网 环境
        //domainUrl = "https://www.weres.bar/"; // 生产环境
        return domainUrl;
    }

    public static String getDomain2() {
        return domainUrl.substring(0, domainUrl.length() - 1);
    }

    public static void setDomainUrl(String url) {
        CfLog.i("url: " + url);
        // 设置域名，此片做各种判断
        if (!TextUtils.isEmpty(url) && url.startsWith("http") && url.length() > 10) {
            if (!url.endsWith("/")) {
                domainUrl = url + "/"; // 必须要 "/"为后缀
            } else {
                domainUrl = url;
            }
            CfLog.i("domainUrl: " + domainUrl);
        }
    }

}
