package com.xtree.recharge.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.comm100.livechat.VisitorClientInterface;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.widget.FloatingWindows;
import com.xtree.recharge.R;

import me.xtree.mvvmhabit.base.ContainerActivity;

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
        onCreate(R.layout.layout_commchat_flowview);
    }

    public interface OnClickListener {
        void onClick(View view);
    }

    private OnClickListener onClickListener = null;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void initData() {
        setIcon(R.mipmap.icon_kefu_float);

        if (floatView != null) {
            floatView.setOnClickListener(v -> {

                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }

                int siteId = 65000194;
                String planId = "1e906220-bcfb-4f17-a5eb-bf7e9ab74be9";
                String chatUrl="https://psowoexvd.n2vu8zpu2f6.com/chatWindow.aspx?planId=" + planId + "&siteId=" + siteId;
                VisitorClientInterface.setChatUrl(chatUrl);

                Intent intent = new Intent(getContext(), ContainerActivity.class);
                intent.putExtra(ContainerActivity.ROUTER_PATH, RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_CHAT);
                getContext().startActivity(intent);
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
