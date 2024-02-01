package com.xtree.bet.bean.ui;


import com.xtree.bet.bean.response.pm.PlayTypeInfo;

import java.util.ArrayList;
import java.util.List;

public class CategoryPm implements Category{
    private List<PlayType> playTypeList = new ArrayList<>();
    /**
     * marketName : 热门
     * orderNo : 1
     * plays : [128,1,2,131,3,4,68,132,5,6,135,7,136,137,12,13,333,14,15,335,17,18,19,20,27,28,32,33,225,34,101,102,104,237,114,126,127]
     * id : 48
     */

    /**
     * 玩法集名称
     */
    private String marketName;
    /**
     * 排序编号
     */
    private int orderNo;
    /**
     * 玩法集id
     */
    private String id;
    /**
     * 玩法id
     */
    private List<Integer> plays;

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

    public String getName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getPlays() {
        return plays;
    }

    public void setPlays(List<Integer> plays) {
        this.plays = plays;
    }
}
