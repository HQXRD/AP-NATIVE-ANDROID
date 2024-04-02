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

    //开始时间
    public ObservableField<String> startDate = new ObservableField<>();
    //结束时间
    public ObservableField<String> endDate = new ObservableField<>();
    //状态
    public ObservableField<StatusVo> state = new ObservableField<>();
    //昨日分红
    public ObservableField<String> yesterdayRebate = new ObservableField<>();
    //显示温馨提示
    public ObservableField<Boolean> tipVisible = new ObservableField<>(true);
    //返水比例提示
    public ObservableField<String> ratioTip = new ObservableField<>();
    //场馆类型
    private RebateAreegmentTypeEnum typeEnum;
    //分页索引
    public int p = 1;
    //page count
    public int pn = 20;
    private List<FilterView.IBaseVo> listStatus = new ArrayList<FilterView.IBaseVo>() {
        {
            // 0-所有状态
            add(new StatusVo(0, BaseApplication.getInstance().getString(R.string.txt_all_status)));
            // 1-已到账
            add(new StatusVo(1, BaseApplication.getInstance().getString(R.string.txt_received)));
            // 2-未到账
            add(new StatusVo(2, BaseApplication.getInstance().getString(R.string.txt_unreceived)));
        }
    };

    private List<FilterView.IBaseVo> dayStatus = new ArrayList<FilterView.IBaseVo>() {
        {
            // 0-所有状态
            add(new StatusVo(0, BaseApplication.getInstance().getString(R.string.txt_all_status)));
            // 1-已到账
            add(new StatusVo(1, BaseApplication.getInstance().getString(R.string.txt_received)));
            // 2-未到账
            add(new StatusVo(2, BaseApplication.getInstance().getString(R.string.txt_unreceived)));
            // 3-无分红
            add(new StatusVo(3, BaseApplication.getInstance().getString(R.string.txt_nodividend)));
        }
    };
    private onCallBack onCallBack = null;

    public GameRebateAgrtHeadModel() {
        initData();
    }

    public GameRebateAgrtHeadModel(onCallBack onCallBack) {
        this.onCallBack = onCallBack;
        initData();
    }

    public void setOnCallBack(GameRebateAgrtHeadModel.onCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1); // 昨天
        startDate.set(TimeUtils.longFormatString(calendar.getTimeInMillis(), "yyyy-MM-dd"));
        endDate.set(TimeUtils.longFormatString(System.currentTimeMillis(), "yyyy-MM-dd"));
        state.set(new StatusVo(0, BaseApplication.getInstance().getString(R.string.txt_all_status)));
    }

    public RebateAreegmentTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(RebateAreegmentTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
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
            switch (typeEnum) {
                //日分红
                case DAYREBATE:
                    onCallBack.selectStatus(state, dayStatus);
                    break;
                default:
                    onCallBack.selectStatus(state, listStatus);
                    break;
            }
        }
    }

    public void check() {
        if (onCallBack != null) {
            p = 1;
            onCallBack.check(state.get(), startDate.get(), endDate.get());
        }
    }

    public void showTip() {
        if (onCallBack != null) {
            onCallBack.showTip();
        }
    }

    public String formatAmout(String amout) {

        return amout;
    }

    public interface onCallBack {
        void selectStartDate(ObservableField<String> startDate);

        void selectEndDate(ObservableField<String> endDate);

        void selectStatus(ObservableField<StatusVo> state, List<FilterView.IBaseVo> listStatus);

        void check(StatusVo state, String startDate, String endDate);

        void showTip();
    }
}
