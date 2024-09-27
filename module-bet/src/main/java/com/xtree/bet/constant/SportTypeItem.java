package com.xtree.bet.constant;

public class SportTypeItem {
    public int id;
    public String code;
    public String name;
    public int order;
    public int iconId;
    public int menuId;
    //彩种赛事数量
    public int num;

    public SportTypeItem() {
    }

    public SportTypeItem(int id, String code, String name, Integer iconId) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.iconId = iconId;
    }
    public SportTypeItem(int id, String code, String name, Integer order, Integer iconId) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.order = order;
        this.iconId = iconId;
    }

    public SportTypeItem(int id, String code, String name, Integer order, Integer iconId, int menuId) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.order = order;
        this.iconId = iconId;
        this.menuId = menuId;
    }

}