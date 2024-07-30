package com.xtree.recharge.ui.fragment.guide.extransfer;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.binioter.guideview.Component;
import com.xtree.recharge.R;

/**
 * 充值 确认付款 上传凭证 顶部 注意剩余时间 引导页面
 */

public class ExTransferPayeeUpComponent implements Component {

    @Override
    public View getView(LayoutInflater inflater) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.guide_ex_transfer_payee_up_item, null);

        //上一步
        return ll;
    }

    @Override public int getAnchor() {
        return Component.ANCHOR_BOTTOM;
    }

    @Override public int getFitPosition() {
        return Component.FIT_START;
    }

    @Override public int getXOffset() {
        return 0;
    }

    @Override public int getYOffset() {
        return 0;
    }
}
