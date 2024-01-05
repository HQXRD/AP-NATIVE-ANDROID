package com.xtree.bet.bean.ui;

import android.os.Parcel;

import com.xtree.bet.bean.response.fb.OptionDataListInfo;
import com.xtree.bet.bean.response.fb.OptionInfo;
import com.xtree.bet.bean.response.fb.PlayTypeInfo;
import com.xtree.bet.constant.SPKey;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

public class PlayTypeFb implements PlayType{
    private PlayTypeInfo playTypeInfo;

    public PlayTypeFb(PlayTypeInfo playTypeInfo){
        this.playTypeInfo = playTypeInfo;
    }

    /**
     * 获取玩法类型，如 亚盘、大小球等
     * @return
     */
    @Override
    public int getPlayType() {
        return playTypeInfo.mty;
    }
    /**
     * 获取玩法名称
     * @return
     */
    @Override
    public String getPlayTypeName() {
        return playTypeInfo.nm;
    }

    @Override
    public String setPlayTypeName(String playTypeName) {
        return playTypeInfo.nm = playTypeName;
    }

    @Override
    public List<OptionList> getOptionLists() {
        List<OptionList> optionLists = new ArrayList<>();
        if(playTypeInfo != null && playTypeInfo.mks != null) {
            for (OptionDataListInfo optionDataListInfo : playTypeInfo.mks) {
                optionLists.add(new OptionListFb(optionDataListInfo));
            }
        }
        return optionLists;
    }
    /**
     * 获取投注玩法列表
     * @return
     */
    @Override
    public List<Option> getOptionList() {
        List<Option> optionList = new ArrayList<>();
        if(playTypeInfo != null && playTypeInfo.mks != null && !playTypeInfo.mks.isEmpty()) {
            for (OptionInfo optionInfo : playTypeInfo.mks.get(0).op) {
                optionList.add(new OptionFb(optionInfo));
            }
        }else{
            int sportId = SPUtils.getInstance().getInt(SPKey.BT_SPORT_ID);
            int length = playTypeInfo.nm.contains("独赢") && sportId == 0 || sportId == 9 ? 3 : 2;
            for (int i = 0; i < length; i++) {
                optionList.add(null);
            }
        }
        return optionList;
    }

    /**
     * 获取冠军赛事投注玩法列表
     * @return
     */
    @Override
    public List<Option> getChampionOptionList() {
        List<Option> optionList = new ArrayList<>();
        if(playTypeInfo != null && playTypeInfo.mks != null && !playTypeInfo.mks.isEmpty()) {
            for(OptionDataListInfo optionDataListInfo : playTypeInfo.mks) {
                for (OptionInfo optionInfo : optionDataListInfo.op) {
                    optionList.add(new OptionFb(optionInfo, optionDataListInfo));
                }
            }
        }
        return optionList;
    }

    @Override
    public int getPlayPeriod() {
        return playTypeInfo.pe;
    }


    /**
     * 获取盘口组标签集合
     * @return
     */
    @Override
    public List<String> getTags() {
        return playTypeInfo.tps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.playTypeInfo, flags);
    }

    public void readFromParcel(Parcel source) {
        this.playTypeInfo = source.readParcelable(PlayTypeInfo.class.getClassLoader());
    }

    protected PlayTypeFb(Parcel in) {
        this.playTypeInfo = in.readParcelable(PlayTypeInfo.class.getClassLoader());
    }

    public static final Creator<PlayTypeFb> CREATOR = new Creator<PlayTypeFb>() {
        @Override
        public PlayTypeFb createFromParcel(Parcel source) {
            return new PlayTypeFb(source);
        }

        @Override
        public PlayTypeFb[] newArray(int size) {
            return new PlayTypeFb[size];
        }
    };
}
