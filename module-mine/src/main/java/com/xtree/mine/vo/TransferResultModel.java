package com.xtree.mine.vo;

/**
 * Created by KAKA on 2024/4/3.
 * Describe: 转账结果数据模型
 */
public class TransferResultModel {
    //转出场馆
    public String from = "";
    //接收场馆
    public String to = "";
    //金额
    public String money = "";
    //失败原因
    public String errorMsg = "";
    //1成功 0失败
    public int status = 0;
}
