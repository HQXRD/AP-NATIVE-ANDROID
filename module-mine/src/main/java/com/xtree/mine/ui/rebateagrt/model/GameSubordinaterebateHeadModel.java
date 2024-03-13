package com.xtree.mine.ui.rebateagrt.model;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.lxj.xpopup.XPopup;
import com.xtree.base.mvvm.recyclerview.BindHead;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.widget.DateTimePickerDialog;
import com.xtree.mine.R;
import com.xtree.mine.vo.StatusVo;

import java.util.Calendar;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/3/11.
 * Describe: 游戏场馆下级返水头数据
 */
public class GameSubordinaterebateHeadModel extends BindModel implements BindHead {

    public interface onCallBack {
        void selectStartDate(ObservableField<String> startDate);
        void selectEndDate(ObservableField<String> endDate);
        void check(String userName,String startDate,String endDate);
    }

    public GameSubordinaterebateHeadModel() {
        initData();
    }

    public GameSubordinaterebateHeadModel(GameSubordinaterebateHeadModel.onCallBack onCallBack) {
        this.onCallBack = onCallBack;
        initData();
    }

    private onCallBack onCallBack = null;

    //开始时间
    public ObservableField<String> startDate = new ObservableField<>();

    //结束时间
    public ObservableField<String> endDate = new ObservableField<>();

    //搜索用户名
    public ObservableField<String> userName = new ObservableField<>("");

    public void setOnCallBack(GameSubordinaterebateHeadModel.onCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1); // 昨天
        startDate.set(TimeUtils.longFormatString(calendar.getTimeInMillis(), "yyyy-MM-dd"));
        endDate.set(TimeUtils.longFormatString(System.currentTimeMillis(), "yyyy-MM-dd"));
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

    public void check() {
        if (onCallBack != null) {
            onCallBack.check(userName.get(), startDate.get(), endDate.get());
        }
    }
}
