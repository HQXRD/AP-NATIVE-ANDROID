package com.xtree.mine.ui.rebateagrt.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindHead;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.widget.FilterView;
import com.xtree.mine.R;
import com.xtree.mine.vo.StatusVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/4/1.
 * Describe: 佣金报表查看数据model
 */
public class CommissionsReports2HeadModel extends BindModel implements BindHead {

    public ObservableField<String> sortData = new ObservableField<>("month");
    public ObservableField<StatusVo> statuData = new ObservableField<>();
    public ObservableField<String> userNameData = new ObservableField<>();
    public String startTime = "";
    public String endTime = "";
    public ObservableField<String> dateData = new ObservableField<>();
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

    public CommissionsReports2HeadModel() {
        initData();
    }

    public CommissionsReports2HeadModel(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
        initData();
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public void initData() {
        //init data
        curMonth();
        setStatusList(statusList);
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

    /**
     * 设置当前月份查询
     */
    public void curMonth() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(TimeUtils.FORMAT_YY_MM_DD);
        SimpleDateFormat sdfym = new SimpleDateFormat(TimeUtils.FORMAT_YY_MM);
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        startTime = sdfym.format(cal.getTime());
        endTime = sdfym.format(new Date(System.currentTimeMillis()));
        dateData.set(sdf.format(cal.getTime()) + "~" + sdf.format(cal.getTime()));
    }
    /**
     * 设置上个月份查询
     */
    public void lastMonth() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(TimeUtils.FORMAT_YY_MM_DD);
        SimpleDateFormat sdfym = new SimpleDateFormat(TimeUtils.FORMAT_YY_MM);
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        startTime = sdfym.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        endTime = sdfym.format(cal.getTime());
        dateData.set(sdf.format(cal.getTime()) + "~" + sdf.format(cal.getTime()));
    }
    /**
     * 设置上上个月份查询
     */
    public void beforeLastMonth() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(TimeUtils.FORMAT_YY_MM_DD);
        SimpleDateFormat sdfym = new SimpleDateFormat(TimeUtils.FORMAT_YY_MM);
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -2);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        startTime = sdfym.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        endTime = sdfym.format(cal.getTime());
        dateData.set(sdf.format(cal.getTime()) + "~" + sdf.format(cal.getTime()));
    }


    @Override
    public boolean getItemHover() {
        return true;
    }

    @Override
    public void setItemHover(boolean b) {

    }

    public interface OnCallBack {
        void status(String title, ObservableField<StatusVo> statu, List<FilterView.IBaseVo> list);

        void check();
    }
}
