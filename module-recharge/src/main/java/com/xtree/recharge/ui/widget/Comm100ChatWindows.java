package com.xtree.recharge.ui.widget;

import android.content.Context;
import android.view.View;

import com.xtree.base.widget.FloatingWindows;
import com.xtree.recharge.R;

/**
 * Created by KAKA on 2024/6/12.
 * Describe: comm100 聊天
 */
public class Comm100ChatWindows extends FloatingWindows {

    private int siteId = 65000194;
    private String planId = "1e906220-bcfb-4f17-a5eb-bf7e9ab74be9";

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
        onCreate(R.layout.layout_commchat_flowview);
    }

    public interface OnClickListener {
        void onClick(View view, String url);
    }

    private OnClickListener onClickListener = null;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void initData() {
        setIcon(R.mipmap.icon_kefu_float);

        View cancleView = secondaryLayout.findViewById(R.id.commchat_flowview_cancle);

        if (cancleView != null) {
            cancleView.setOnClickListener(v -> hideTip());
        }

        if (floatView != null) {
            floatView.setOnClickListener(v -> {

                String chatUrl = "https://psowoexvd.n2vu8zpu2f6.com/chatWindow.aspx?planId=" + planId + "&siteId=" + siteId + "&orderid=";

                if (onClickListener != null) {
                    onClickListener.onClick(v, chatUrl);
                }
            });
        }
    }

    /**
     * 显示提示
     */
    public void showTip() {
        setBottomLocation();
    }

    /**
     * 隐藏提示
     */
    public void hideTip() {
        removeSecond();
    }
}
