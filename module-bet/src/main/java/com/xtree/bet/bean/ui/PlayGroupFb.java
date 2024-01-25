package com.xtree.bet.bean.ui;

import com.xtree.bet.bean.response.fb.PlayTypeInfo;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.constant.SPKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

public class PlayGroupFb implements PlayGroup {

    private List<PlayType> originalPlayTypeList = new ArrayList<>();

    private List<PlayType> playTypeList;

    public PlayGroupFb(List<PlayType> originalPlayTypeList) {
        this.originalPlayTypeList = originalPlayTypeList;
    }

    public PlayGroupFb() {

    }

    public void addPlayType(PlayType playType) {
        originalPlayTypeList.add(playType);
    }

    public List<PlayType> getPlayTypeList() {
        return playTypeList;
    }

    public List<PlayGroup> getPlayGroupList(String sportId) {
        String[] playTypeIds = FBConstants.getPlayTypeId(sportId);
        String[] playTypeNames = FBConstants.getPlayTypeName(sportId);


        playTypeList = new ArrayList<>(playTypeIds.length);
        for (int i = 0; i < playTypeIds.length; i++) {
            playTypeList.add(null);
        }
        List<String> stringList = Arrays.asList(playTypeIds);
        for (PlayType playtype : originalPlayTypeList) {
            String str = playtype.getPlayType() + "/" + playtype.getPlayPeriod();
            if (stringList.contains(str)) {
                playtype.setPlayTypeName(playTypeNames[stringList.indexOf(str)]);
                playTypeList.set(stringList.indexOf(str), playtype);
            }
        }
        for (int i = 0; i < playTypeIds.length; i++) {
            if (playTypeList.get(i) == null) {
                PlayTypeInfo playTypeInfo = new PlayTypeInfo();
                playTypeInfo.nm = playTypeNames[i];
                playTypeList.set(i, new PlayTypeFb(playTypeInfo));
            }
        }

        List<PlayGroup> playGroupList = new ArrayList<>();
        PlayGroupFb playGroup = null;
        for (int i = 0; i < playTypeList.size(); i++) {
            if (i % 3 == 0) {
                playGroup = new PlayGroupFb();
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

    public void setOriginalPlayTypeList(List<PlayType> originalPlayTypeList) {
        this.originalPlayTypeList = originalPlayTypeList;
    }
}
