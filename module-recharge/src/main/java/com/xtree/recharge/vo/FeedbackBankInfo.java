package com.xtree.recharge.vo;

/**
 * 反馈页面 微信状态支付渠道
 */
public class FeedbackBankInfo
{
    public static  int id ;
    public static String name ;

    public FeedbackBankInfo(int id, String name)
    {
        this.id = id ;
        this.name = name;
    }

    @Override
    public String toString() {
        return "FeedbackBankInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
