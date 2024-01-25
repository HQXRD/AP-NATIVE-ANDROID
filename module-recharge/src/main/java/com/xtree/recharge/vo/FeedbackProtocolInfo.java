package com.xtree.recharge.vo;

/**
 * 虚拟币支付协议
 */
public class FeedbackProtocolInfo {

    public int id ;
    public String name ;

    public FeedbackProtocolInfo(int id , String name)
    {
        this.id = id ;
        this.name = name;
    }

    @Override
    public String toString() {
        return "FeedbackProtocolInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
