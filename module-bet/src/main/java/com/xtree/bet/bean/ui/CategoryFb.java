package com.xtree.bet.bean.ui;

import com.xtree.bet.bean.response.PlayTypeInfo;

import java.util.ArrayList;
import java.util.List;

public class CategoryFb implements Category{
    private List<PlayType> playTypeList = new ArrayList<>();
    private PlayTypeInfo playTypeInfo;
    private String name;
    public CategoryFb(PlayTypeInfo playTypeInfo, String name){
        this.playTypeInfo = playTypeInfo;
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<PlayType> getPlayTypeList() {
        return playTypeList;
    }

    @Override
    public void addPlayTypeList(PlayType playType) {
        playTypeList.add(playType);
    }

    @Override
    public int getSort() {
        return 0;
    }
}
