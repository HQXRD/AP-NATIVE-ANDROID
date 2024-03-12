package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindHead;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.widget.FilterView;
import com.xtree.base.widget.impl.FilterViewOnClickListerner;
import com.xtree.mine.R;
import com.xtree.mine.vo.StatusVo;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/3/9.
 * Describe: 游戏场馆返水头数据
 */
public class GameRebateAgrtHeadModel extends BindModel implements BindHead {

    public FilterViewOnClickListerner filterViewOnClickListerner = null;

    public GameRebateAgrtHeadModel() {
    }

    public GameRebateAgrtHeadModel(FilterViewOnClickListerner filterViewOnClickListerner) {
        this.filterViewOnClickListerner = filterViewOnClickListerner;
    }

    public List<FilterView.IBaseVo> listStatus = new ArrayList<FilterView.IBaseVo>() {
        {
            // 0-所有状态
            add(new StatusVo(0, BaseApplication.getInstance().getString(R.string.txt_all_status)));
            // 1-已到账
            add(new StatusVo(1, BaseApplication.getInstance().getString(R.string.txt_received)));
            // 2-未到账
            add(new StatusVo(2, BaseApplication.getInstance().getString(R.string.txt_unreceived)));
        }
    };

    @Override
    public boolean getItemHover() {
        return true;
    }

    @Override
    public void setItemHover(boolean b) {

    }
}
