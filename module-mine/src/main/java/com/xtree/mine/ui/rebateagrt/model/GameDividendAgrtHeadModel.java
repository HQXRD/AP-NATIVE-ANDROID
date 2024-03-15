package com.xtree.mine.ui.rebateagrt.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindHead;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.widget.FilterView;
import com.xtree.mine.R;
import com.xtree.mine.vo.StatusVo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/3/14.
 * Describe:
 */
public class GameDividendAgrtHeadModel extends BindModel implements BindHead {

    public interface OnCallBack {
        void sort(String title, ObservableField<StatusVo> sort, List<FilterView.IBaseVo> list);
        void cyclicality(String title, ObservableField<StatusVo> cycly, List<FilterView.IBaseVo> list);
        void status(String title, ObservableField<StatusVo> statu, List<FilterView.IBaseVo> list);
        void check();
    }

    public ObservableField<StatusVo> sortData = new ObservableField<>();
    public ObservableField<StatusVo> cyclyData = new ObservableField<>();
    public ObservableField<StatusVo> statuData = new ObservableField<>();
    public ObservableField<String> userNameData = new ObservableField<>();

    //分页索引
    public int p = 1;
    //page count
    public int pn = 20;
    public int type = 1;

    private OnCallBack onCallBack;

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public GameDividendAgrtHeadModel() {
        initData();
    }

    public GameDividendAgrtHeadModel(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
        initData();
    }

    private List<FilterView.IBaseVo> statusList = new ArrayList<FilterView.IBaseVo>();

    private List<FilterView.IBaseVo> sortList = new ArrayList<FilterView.IBaseVo>() {
        {
            // 0-所有状态
            add(new StatusVo("bet_desc", BaseApplication.getInstance().getString(R.string.txt_bet_desc)));
            add(new StatusVo("bet_asc", BaseApplication.getInstance().getString(R.string.txt_bet_asc)));
            add(new StatusVo("self_money_desc", BaseApplication.getInstance().getString(R.string.txt_self_money_desc)));
            add(new StatusVo("self_money_asc", BaseApplication.getInstance().getString(R.string.txt_self_money_asc)));
            add(new StatusVo("profitloss_desc", BaseApplication.getInstance().getString(R.string.txt_profitloss_desc)));
            add(new StatusVo("profitloss_asc", BaseApplication.getInstance().getString(R.string.txt_profitloss_asc)));
        }
    };

    private List<FilterView.IBaseVo> cyclytList = new ArrayList<FilterView.IBaseVo>();

    private void initData() {
        sortData.set(new StatusVo(sortList.get(0).getShowId(), sortList.get(0).getShowName()));
    }

    public void sort() {
        if (onCallBack != null) {
            onCallBack.sort("排序", sortData, sortList);
        }
    }

    public void cycly() {
        if (onCallBack != null) {
            onCallBack.cyclicality("周期", cyclyData, cyclytList);
        }
    }

    public void status() {
        if (onCallBack != null) {
            onCallBack.status("状态", statuData, statusList);
        }
    }

    public void check() {
        if (onCallBack != null) {
            p = 1;
            onCallBack.check();
        }
    }

    public List<FilterView.IBaseVo> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<FilterView.IBaseVo> statusList) {
        if (statusList != null && statusList.size() > 0) {
            //插入一条所有状态
            statusList.add(0,new StatusVo(0, BaseApplication.getInstance().getString(R.string.txt_all_status)));
            statuData.set(new StatusVo(statusList.get(0).getShowId(), statusList.get(0).getShowName()));
            this.statusList = statusList;
        }
    }

    public List<FilterView.IBaseVo> getSortList() {
        return sortList;
    }

    public void setSortList(List<FilterView.IBaseVo> sortList) {
        this.sortList = sortList;
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
