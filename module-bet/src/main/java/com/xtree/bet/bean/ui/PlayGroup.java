package com.xtree.bet.bean.ui;

import com.stx.xhb.androidx.entity.BaseBannerInfo;
import com.xtree.bet.bean.PlayTypeInfo;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.constant.SPKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

public class PlayGroup implements BaseBannerInfo {

    private List<PlayType> playTypeList = new ArrayList<>();

    public PlayGroup(List<PlayType> playTypeList){
        this.playTypeList = playTypeList;
    }

    public PlayGroup() {

    }

    public void addPlayType(PlayType playType){
        playTypeList.add(playType);
    }

    public List<PlayGroup> getPlayGroupList(){
        int sportId = SPUtils.getInstance().getInt(SPKey.BT_SPORT_ID);
        String[] playTypeIds = Constants.PLAY_TYPE_ID[sportId];
        String[] playTypeNames = Constants.PLAY_TYPE_NAME[sportId];
        
        
        List<PlayType> list = new ArrayList<>(playTypeIds.length);
        for(int i = 0; i < playTypeIds.length; i ++){
            list.add(null);
        }
        List<String> stringList = Arrays.asList(playTypeIds);
        for (PlayType playtype : playTypeList) {
            String str = playtype.getPlayType() + "/" + playtype.getPlayPeriod();
            if(stringList.contains(str)){
                playtype.setPlayTypeName(playTypeNames[stringList.indexOf(str)]);
                list.set(stringList.indexOf(str), playtype);
            }
        }
        for(int i = 0; i < playTypeIds.length; i ++){
            if(list.get(i) == null){
                PlayTypeInfo playTypeInfo = new PlayTypeInfo();
                playTypeInfo.nm = playTypeNames[i];
                list.set(i, new PlayTypeFb(playTypeInfo));
            }
        }

        List<PlayGroup> playGroupList = new ArrayList<>();
        PlayGroup playGroup = null;
            for (int i = 0; i < list.size(); i++) {
                if (i % 3 == 0) {
                    playGroup = new PlayGroup();
                }
                playGroup.addPlayType(list.get(i));
                if (playGroup.playTypeList.size() == 3) {
                    playGroupList.add(playGroup);
                }
            }
        return playGroupList;
    }

    public List<PlayType> getPlayTypeList() {
        return playTypeList;
    }

    public void setPlayTypeList(List<PlayType> playTypeList) {
        this.playTypeList = playTypeList;
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
