package com.xtree.mine.ui.rebateagrt.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindHead;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.widget.FilterView;
import com.xtree.mine.R;
import com.xtree.mine.vo.StatusVo;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/4/1.
 * Describe: 彩票分红报表头数据
 */
public class LotteryDividendReportsHeadModel extends BindModel implements BindHead {

    public ObservableField<StatusVo> agentData = new ObservableField<>();
    public ObservableField<StatusVo> cyclyData = new ObservableField<>();
    public ObservableField<StatusVo> statuData = new ObservableField<>();
    public ObservableField<String> userNameData = new ObservableField<>();
    //分页索引
    public int p = 1;
    //page count
    public int pn = 20;
    private OnCallBack onCallBack;
    private List<FilterView.IBaseVo> statusList = new ArrayList<FilterView.IBaseVo>() {
        {
            // 0-所有状态
            add(new StatusVo("", "全部"));
            add(new StatusVo("20", "待审核"));
            add(new StatusVo("41", "拒绝"));
            add(new StatusVo("42", "取消分红"));
            add(new StatusVo("61", "已派发"));
            add(new StatusVo("62", "已结清"));
        }
    };
    private List<FilterView.IBaseVo> agentList = new ArrayList<FilterView.IBaseVo>();
    private List<FilterView.IBaseVo> cyclytList = new ArrayList<FilterView.IBaseVo>();

    public LotteryDividendReportsHeadModel() {
        initData();
    }

    public LotteryDividendReportsHeadModel(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
        initData();
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    private void initData() {
        setStatusList(statusList);
    }

    public void agent() {
        if (ClickUtil.isFastClick()) {
            return;
        }
        if (onCallBack != null) {
            onCallBack.agent(BaseApplication.getInstance().getString(R.string.txt_sort), agentData, agentList);
        }
    }

    public void cycly() {
        if (ClickUtil.isFastClick()) {
            return;
        }
        if (onCallBack != null) {
            onCallBack.cyclicality(BaseApplication.getInstance().getString(R.string.txt_cycle), cyclyData, cyclytList);
        }
    }

    public void status() {
        if (ClickUtil.isFastClick()) {
            return;
        }
        if (onCallBack != null) {
            onCallBack.status(BaseApplication.getInstance().getString(R.string.txt_status), statuData, statusList);
        }
    }

    public void check() {
        if (ClickUtil.isFastClick()) {
            return;
        }
        if (onCallBack != null) {
            p = 1;
            onCallBack.check();
        }
    }

    public List<FilterView.IBaseVo> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<FilterView.IBaseVo> statusList) {
        if (statusList != null) {
            if (statuData.get() == null) {
                statuData.set(new StatusVo(statusList.get(0).getShowId(), statusList.get(0).getShowName()));
            }
            this.statusList = statusList;
        }
    }

    public List<FilterView.IBaseVo> getAgentList() {
        return agentList;
    }

    public void setAgentList(List<FilterView.IBaseVo> agentList) {
        if (agentList != null && agentList.size() > 0) {
            if (agentData.get() == null) {
                agentData.set(new StatusVo(agentList.get(0).getShowId(), agentList.get(0).getShowName()));
            }
            this.agentList = agentList;
        }
    }

    public List<FilterView.IBaseVo> getCyclytList() {
        return cyclytList;
    }

    public void setCyclytList(List<FilterView.IBaseVo> cyclytList) {
        if (cyclytList != null && cyclytList.size() > 0) {
            if (cyclyData.get() == null) {
                cyclyData.set(new StatusVo(cyclytList.get(0).getShowId(), cyclytList.get(0).getShowName()));
            }
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

    public interface OnCallBack {
        void agent(String title, ObservableField<StatusVo> sort, List<FilterView.IBaseVo> list);

        void cyclicality(String title, ObservableField<StatusVo> cycly, List<FilterView.IBaseVo> list);

        void status(String title, ObservableField<StatusVo> statu, List<FilterView.IBaseVo> list);

        void check();
    }
}
