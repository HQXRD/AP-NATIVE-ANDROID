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
 * Created by KAKA on 2024/3/9.
 * Describe: 游戏场馆返水头数据
 */
public class GameRebateAgrtHeadModel extends BindModel implements BindHead {

    public interface onCallBack {
        void selectStartDate(ObservableField<String> startDate);
        void selectEndDate(ObservableField<String> endDate);
        void selectStatus(ObservableField<StatusVo> state, List<FilterView.IBaseVo> listStatus);
        void check(StatusVo state, String startDate, String endDate);
    }

    //开始时间
    public ObservableField<String> startDate = new ObservableField<>();

    //结束时间
    public ObservableField<String> endDate = new ObservableField<>();

    //状态
    public ObservableField<StatusVo> state = new ObservableField<>();

    //昨日分红
    public ObservableField<String> yesterdayRebate = new ObservableField<>();

    private onCallBack onCallBack = null;

    //分页索引
    public int p = 1;
    //page count
    public int pn = 20;

    public void setOnCallBack(GameRebateAgrtHeadModel.onCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public GameRebateAgrtHeadModel() {
        initData();
    }

    public GameRebateAgrtHeadModel(onCallBack onCallBack) {
        this.onCallBack = onCallBack;
        initData();
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

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1); // 昨天
        startDate.set(TimeUtils.longFormatString(calendar.getTimeInMillis(), "yyyy-MM-dd"));
        endDate.set(TimeUtils.longFormatString(System.currentTimeMillis(), "yyyy-MM-dd"));
        state.set(new StatusVo(0, BaseApplication.getInstance().getString(R.string.txt_all_status)));
    }

    @Override
    public boolean getItemHover() {
        return true;
    }

    @Override
    public void setItemHover(boolean b) {

    }

    public void selectStartDate() {
        if (onCallBack != null) {
            onCallBack.selectStartDate(startDate);
        }
    }

    public void selectEndDate() {
        if (onCallBack != null) {
            onCallBack.selectEndDate(endDate);
        }
    }

    public void selectStatus() {
        if (onCallBack != null) {
            onCallBack.selectStatus(state, listStatus);
        }
    }

    public void check() {
        if (onCallBack != null) {
            p = 1;
            onCallBack.check(state.get(), startDate.get(), endDate.get());
        }
    }

    public String formatAmout(String amout) {

        return amout;
    }
}
