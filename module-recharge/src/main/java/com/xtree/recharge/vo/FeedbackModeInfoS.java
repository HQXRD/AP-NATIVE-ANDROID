package com.xtree.recharge.vo;

/**
 * 反馈页面可选择付款方式
 */
public class FeedbackModeInfoS {
    public int id ;
    public String name ;

    public String getName() {
        return name;
    }

    public FeedbackModeInfoS(int id , String name)
    {
        this.id = id ;
        this.name = name;
    }

    @Override
    public String toString() {
        return "FeedbackModeInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
