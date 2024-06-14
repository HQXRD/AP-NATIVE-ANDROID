package com.xtree.recharge.ui.widget;

import android.content.Context;
import android.content.Intent;

import com.comm100.livechat.ChatActivity;
import com.comm100.livechat.VisitorClientInterface;
import com.xtree.base.R;
import com.xtree.base.widget.FloatingWindows;

/**
 * Created by KAKA on 2024/6/12.
 * Describe: comm100 聊天
 */
public class Comm100ChatWindows extends FloatingWindows {

    /**
     * onepay 客服配置
     * siteid
     * 65000194
     *
     * campaign id
     * 1e906220-bcfb-4f17-a5eb-bf7e9ab74be9
     */

    public Comm100ChatWindows(Context context) {
        super(context);
        onCreate(0);
    }

    @Override
    public void initData() {
        setIcon(R.mipmap.icon_kefu_float);
        if (floatView != null) {
            floatView.setOnClickListener(v -> {
                int siteId = 65000194;
                String planId = "1e906220-bcfb-4f17-a5eb-bf7e9ab74be9";
                String chatUrl="https://psowoexvd.n2vu8zpu2f6.com/chatWindow.aspx?planId=" + planId + "&siteId=" + siteId;
                VisitorClientInterface.setChatUrl(chatUrl);
                Intent intent = new Intent(getContext(), ChatActivity.class);
                getContext().startActivity(intent);
            });
        }
    }
}
