package com.xtree.base.widget;

import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * H5调用安卓的方法
 */
public class WebAppInterface {
    String homeType = "goHome";
    String rechargeType = "goRecharge";
    String withdrawType = "goWithdraw";
    String customServiceType = "goCustomService";
    String vipType = "goVip";

    private Context context;

    public WebAppInterface(Context context) {
        this.context = context;
    }

    // JavaScript 调用原生功能的方法，方法名为 nativeFunction
    @JavascriptInterface
    public void nativeFunction(String type) {
        // 在这里处理 JavaScript 调用，并执行相应的原生功能
        if (type.equals(homeType)) {

        } else if (type.equals(rechargeType)) {

        } else if (type.equals(withdrawType)) {

        } else if (type.equals(customServiceType)) {

        } else if (type.equals(vipType)) {

        }
    }

}
