package com.xtree.live.ui.main.model;

import com.xtree.base.mvvm.recyclerview.BindHead;
import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/9/9.
 * Describe: 直播门户页Banner数据模型
 */
public class LiveBannerHeadModel extends BindModel implements BindHead {

    @Override
    public boolean getItemHover() {
        return false;
    }

    @Override
    public void setItemHover(boolean b) {

    }
}
