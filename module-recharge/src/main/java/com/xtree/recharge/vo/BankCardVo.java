package com.xtree.recharge.vo;

public class BankCardVo {

    public String id;
    public String name;
    //public String desc;

    public BankCardVo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "BankCardVo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                //", desc='" + desc + '\'' +
                '}';
    }
}
