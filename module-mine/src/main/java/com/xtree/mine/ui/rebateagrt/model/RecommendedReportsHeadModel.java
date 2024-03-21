package com.xtree.mine.ui.rebateagrt.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindHead;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.widget.FilterView;
import com.xtree.mine.R;
import com.xtree.mine.vo.StatusVo;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/3/15.
 * Describe:
 */
public class RecommendedReportsHeadModel extends BindModel implements BindHead {

    public interface OnCallBack {
        void cyclicality(String title, ObservableField<StatusVo> cycly, List<FilterView.IBaseVo> list);
        void check();
    }

    public ObservableField<StatusVo> cyclyData = new ObservableField<>();

    //分页索引
    public int p = 1;
    //page count
    public int pn = 20;
    public String type = "1";

    private OnCallBack onCallBack;

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public RecommendedReportsHeadModel() {
    }

    public RecommendedReportsHeadModel(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    private List<FilterView.IBaseVo> cyclytList = new ArrayList<FilterView.IBaseVo>();

    public void cycly() {
        if (onCallBack != null) {
            onCallBack.cyclicality("周期", cyclyData, cyclytList);
        }
    }

    public void check() {
        if (onCallBack != null) {
            p = 1;
            onCallBack.check();
        }
    }

    public List<FilterView.IBaseVo> getCyclytList() {
        return cyclytList;
    }

    public void setCyclytList(List<FilterView.IBaseVo> cyclytList) {
        if (cyclytList != null && cyclytList.size() > 0) {
            cyclyData.set(new StatusVo(cyclytList.get(0).getShowId(), cyclytList.get(0).getShowName()));
            this.cyclytList = cyclytList;
        }
    }

    @Override
    public boolean getItemHover() {
        return true;
    }

    @Override
    public void setItemHover(boolean b) {

    }
}
