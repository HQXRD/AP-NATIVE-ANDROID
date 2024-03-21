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
 * Created by KAKA on 2024/3/11.
 * Describe: 游戏场馆下级契约头数据
 */
public class GameSubordinateagrtHeadModel extends BindModel implements BindHead {

    public interface onCallBack {
        void selectStatus(ObservableField<StatusVo> state, List<FilterView.IBaseVo> listStatus);
        void check(StatusVo state,String searchName);
    }

    //状态
    public ObservableField<StatusVo> state = new ObservableField<>(new StatusVo(0, BaseApplication.getInstance().getString(R.string.txt_all_status)));
    //查找名称
    public ObservableField<String> serachName = new ObservableField<>();

    //分页索引
    public int p = 1;
    //page count
    public int pn = 20;

    public List<FilterView.IBaseVo> listStatus = new ArrayList<FilterView.IBaseVo>() {
        {
            // 0-所有状态
            add(new StatusVo(0, BaseApplication.getInstance().getString(R.string.txt_all_status)));
            // -1-未创建
            add(new StatusVo(-1, BaseApplication.getInstance().getString(R.string.txt_uncreated)));
            // 1-已签订
            add(new StatusVo(1, BaseApplication.getInstance().getString(R.string.txt_signed)));
            // 1-已终止
            add(new StatusVo(4, BaseApplication.getInstance().getString(R.string.txt_discontinued)));
            // 1-已更新
            add(new StatusVo(8, BaseApplication.getInstance().getString(R.string.txt_updated)));
        }
    };

    private onCallBack onCallBack = null;

    public void setOnCallBack(onCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public GameSubordinateagrtHeadModel() {
    }

    public GameSubordinateagrtHeadModel(onCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public void selectStatus() {
        if (onCallBack != null) {
            onCallBack.selectStatus(state, listStatus);
        }
    }
    public void check() {
        if (onCallBack != null) {
            p = 1;
            onCallBack.check(state.get(), serachName.get());
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
