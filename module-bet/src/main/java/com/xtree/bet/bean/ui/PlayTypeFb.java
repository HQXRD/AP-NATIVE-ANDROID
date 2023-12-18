package com.xtree.bet.bean.ui;

import com.xtree.bet.bean.OptionDataListInfo;
import com.xtree.bet.bean.OptionInfo;
import com.xtree.bet.bean.PlayTypeInfo;
import com.xtree.bet.constant.Constants;
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
        for (OptionDataListInfo optionDataListInfo : playTypeInfo.mks) {
            optionLists.add(new OptionListFb(optionDataListInfo));
        }
        return optionLists;
    }

    @Override
    public List<Option> getOptionList() {
        List<Option> optionList = new ArrayList<>();
        if(playTypeInfo.mks != null) {
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

    @Override
    public int getPlayPeriod() {
        return playTypeInfo.pe;
    }

    @Override
    public Object getXBannerUrl() {
        return null;
    }

    @Override
    public String getXBannerTitle() {
        return null;
    }
}
