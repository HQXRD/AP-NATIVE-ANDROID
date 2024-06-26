package com.xtree.bet.bean.ui;

import java.util.ArrayList;
import java.util.List;

public class PlayGroupPm implements PlayGroup{

    private List<PlayType> originalPlayTypeList = new ArrayList<>();

    private List<PlayType> playTypeList;

    public PlayGroupPm(List<PlayType> originalPlayTypeList) {
        this.originalPlayTypeList = originalPlayTypeList;
        this.playTypeList = originalPlayTypeList;
    }

    public PlayGroupPm() {

    }

    public void addPlayType(PlayType playType) {
        originalPlayTypeList.add(playType);
    }

    public List<PlayType> getPlayTypeList() {
        return playTypeList;
    }

    public List<PlayGroup> getPlayGroupList(String sportId) {

        List<PlayGroup> playGroupList = new ArrayList<>();
        PlayGroupPm playGroup = null;
        for (int i = 0; i < playTypeList.size(); i++) {
            if (i % 3 == 0) {
                playGroup = new PlayGroupPm();
            }
            playGroup.addPlayType(playTypeList.get(i));
            if (playGroup.originalPlayTypeList.size() == 3) {
                playGroupList.add(playGroup);
            }
        }
        return playGroupList;
    }

    public List<PlayType> getOriginalPlayTypeList() {
        return originalPlayTypeList;
    }
}
