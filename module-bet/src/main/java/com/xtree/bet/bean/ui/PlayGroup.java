package com.xtree.bet.bean.ui;

import com.stx.xhb.androidx.entity.BaseBannerInfo;

import java.util.ArrayList;
import java.util.List;

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
        List<PlayGroup> playGroupList = new ArrayList<>();
        PlayGroup playGroup = null;
        for(int i = 0; i < playTypeList.size(); i ++){
            if(i % 3 == 0){
                playGroup = new PlayGroup();
            }
            playGroup.addPlayType(playTypeList.get(i));
            if(playGroup.playTypeList.size() == 3){
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
