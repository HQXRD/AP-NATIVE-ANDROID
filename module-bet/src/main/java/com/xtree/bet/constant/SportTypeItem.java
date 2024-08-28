package com.xtree.bet.constant;

public class SportTypeItem {
    public String id;
    public int position;
    public String code;
    public String name;
    public int order;
    public int iconId;
    public int menuId;
    //彩种赛事数量
    public int num;
    public boolean isSelected;

    public SportTypeItem() {
    }

    public SportTypeItem(String id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public SportTypeItem(String id, String code, String name, Integer order) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.order = order;
    }
    public SportTypeItem(String id, String code, String name, Integer order, Integer iconId) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.order = order;
        this.iconId = iconId;
    }

    public SportTypeItem(String id, String code, String name, Integer order, Integer iconId, int menuId) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.order = order;
        this.iconId = iconId;
        this.menuId = menuId;
    }

}