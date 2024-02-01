package com.xtree.bet.bean.ui;

import com.xtree.bet.bean.response.fb.PlayTypeInfo;

import java.util.ArrayList;
import java.util.List;

public class CategoryFb implements Category{
    private List<PlayType> playTypeList = new ArrayList<>();
    private String name;
    public CategoryFb(String name){
        this.name = name;
    }

    @Override
    public String getId() {
        return null;
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
