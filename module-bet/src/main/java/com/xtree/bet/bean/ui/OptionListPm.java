package com.xtree.bet.bean.ui;

import android.os.Parcel;
import android.text.TextUtils;


import com.xtree.bet.bean.response.pm.OptionDataListInfo;
import com.xtree.bet.bean.response.pm.OptionInfo;
import com.xtree.bet.bean.response.pm.PlayTypeInfo;

import java.util.ArrayList;
import java.util.List;

public class OptionListPm implements OptionList {
    OptionDataListInfo optionDataListInfo;
    PlayTypeInfo playTypeInfo;

    public OptionListPm(OptionDataListInfo optionDataListInfo, PlayTypeInfo playTypeInfo){
        this.optionDataListInfo = optionDataListInfo;
        this.playTypeInfo = playTypeInfo;
    }

    public long getId() {
        if(optionDataListInfo == null || TextUtils.isEmpty(optionDataListInfo.hid)){
            return 0;
        }
        return Long.valueOf(optionDataListInfo.hid);
    }

    @Override
    public int getMatchType() {
        if(optionDataListInfo == null || TextUtils.isEmpty(optionDataListInfo.hid)){
            return 0;
        }
        return optionDataListInfo.hmt;
    }

    /**
     * 玩法销售状态
     */
    public boolean isOpen() {
        return optionDataListInfo.hs == 0;
    }

    /**
     * 是否支持串关，0 不可串关，1 可串关
     */
    public boolean isAllowCrossover() {
        return playTypeInfo.hids == 1;
    }

    /**
     * 是否为最优线，带线玩法可用该字段进行排序，从小到大
     * 代表优先级，比如让分玩法有-0.5 -0.25 0几个让球方式，这个属性就代码了它们的优先级
     */
    public int getSort() {
        return 0;
    }

    /**
     * line值，带线玩法的线，例如大小球2.5线，部分玩法展示可用该字段进行分组展示
     */
    public String getLine() {
        return null;
    }

    /**
     * 玩法选项
     */
    @Override
    public List<Option> getOptionList() {
        List<Option> optionList = new ArrayList<>();
        for (OptionInfo optionInfo : optionDataListInfo.ol) {
            optionList.add(new OptionPm(optionInfo, optionDataListInfo, playTypeInfo));
        }
        return optionList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.optionDataListInfo, flags);
    }

    public void readFromParcel(Parcel source) {
        this.optionDataListInfo = source.readParcelable(OptionDataListInfo.class.getClassLoader());
    }

    protected OptionListPm(Parcel in) {
        this.optionDataListInfo = in.readParcelable(OptionDataListInfo.class.getClassLoader());
    }

    public static final Creator<OptionListPm> CREATOR = new Creator<OptionListPm>() {
        @Override
        public OptionListPm createFromParcel(Parcel source) {
            return new OptionListPm(source);
        }

        @Override
        public OptionListPm[] newArray(int size) {
            return new OptionListPm[size];
        }
    };
}
