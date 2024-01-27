package com.xtree.bet.bean.ui;

import android.os.Parcel;
import android.text.TextUtils;

import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.response.pm.OptionDataListInfo;
import com.xtree.bet.bean.response.pm.OptionInfo;
import com.xtree.bet.bean.response.pm.PlayTypeInfo;
import com.xtree.bet.constant.SPKey;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

public class PlayTypePm implements PlayType{
    private PlayTypeInfo playTypeInfo;

    public PlayTypePm(PlayTypeInfo playTypeInfo){
        this.playTypeInfo = playTypeInfo;
    }

    @Override
    public String getId() {
        return playTypeInfo.hpid;
    }

    @Override
    public String getMarketId() {
        return String.valueOf(playTypeInfo.hid);
    }

    /**
     * 获取玩法类型，如 亚盘、大小球等
     * @return
     */
    @Override
    public int getPlayType() {
        return Integer.valueOf(playTypeInfo.hpid);
    }
    /**
     * 获取玩法名称
     * @return
     */
    @Override
    public String getPlayTypeName() {
        if(!TextUtils.isEmpty(playTypeInfo.hpn)){
            return playTypeInfo.hpn;
        }else{
            return playTypeInfo.hps;
        }
    }

    @Override
    public String setPlayTypeName(String playTypeName) {
        return playTypeInfo.hpn = playTypeName;
    }

    @Override
    public List<OptionList> getOptionLists() {
        List<OptionList> optionLists = new ArrayList<>();
        if(playTypeInfo != null && playTypeInfo.hl != null) {
            for (OptionDataListInfo optionDataListInfo : playTypeInfo.hl) {
                optionLists.add(new OptionListPm(optionDataListInfo, playTypeInfo));
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
        int sportId = SPUtils.getInstance().getInt(SPKey.BT_SPORT_ID);
        int length = playTypeInfo.hpn.contains("独赢") && sportId == 0 || sportId == 9 ? 3 : 2;

        if(playTypeInfo != null && playTypeInfo.hl != null && !playTypeInfo.hl.isEmpty()) {
            for (int i = 0; i < length; i++) {
                OptionInfo optionInfo;
                try{
                    optionInfo = playTypeInfo.hl.get(0).ol.get(i);
                }catch (Exception e){
                    optionInfo = null;
                }

                if(optionInfo == null){
                    optionList.add(null);
                }else{
                    optionList.add(new OptionPm(optionInfo, playTypeInfo.hl.get(0), playTypeInfo));
                }
            }
        }else{
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
        if(playTypeInfo != null && playTypeInfo.ol != null && !playTypeInfo.ol.isEmpty()) {
            for (OptionInfo optionInfo : playTypeInfo.ol) {
                OptionDataListInfo optionDataListInfo = new OptionDataListInfo();
                optionDataListInfo.hs = playTypeInfo.hs;
                optionDataListInfo.hmt = 3;
                optionList.add(new OptionPm(optionInfo, optionDataListInfo, playTypeInfo));
            }
        }
        return optionList;
    }

    @Override
    public int getPlayPeriod() {
        return Integer.valueOf(playTypeInfo.hpid);
    }


    /**
     * 获取盘口组标签集合
     * @return
     */
    @Override
    public List<String> getTags() {
        return null;
    }
    /**
     * 获取所属玩法集ID
     * @return
     */
    @Override
    public String getCategoryId() {
        return playTypeInfo.hlid;
    }

    @Override
    public String getMatchDeadLine() {
        return TimeUtils.longFormatString(Long.valueOf(playTypeInfo.hmed), TimeUtils.FORMAT_YY_MM_DD_HH_MM_1);
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

    protected PlayTypePm(Parcel in) {
        this.playTypeInfo = in.readParcelable(PlayTypeInfo.class.getClassLoader());
    }

    public static final Creator<PlayTypePm> CREATOR = new Creator<PlayTypePm>() {
        @Override
        public PlayTypePm createFromParcel(Parcel source) {
            return new PlayTypePm(source);
        }

        @Override
        public PlayTypePm[] newArray(int size) {
            return new PlayTypePm[size];
        }
    };
}
