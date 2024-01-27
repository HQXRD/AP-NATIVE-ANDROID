package com.xtree.mine.vo;

import java.util.ArrayList;

/**
 * 提现详细页面model 数据
 */
public class ChannelDetailVo
{

    public int  typenum ;
    public String name ;
    public int channel_amount_use ;
    /**
     * 可选金额展示 1为展示 ；0为不展示
     */
    public int fixamount_list_status ;
    /**
     * 可选择金额
     */
    public ArrayList<String> fixamount_list ;
    public int fixamount_list_status1 ;
    public ArrayList<Object> fixamount_list1 ;
    public int fixamount_rule_status ;
    public ArrayList<Object> fixamount_rule ;
    public int earnest_money_pl ;
    public ArrayList<Object> didvided_list ;
    public int opfast_min_money ;
    public int opfast_max_money ;
    public int  thiriframe_use ;
    public int thiriframe_status ; //为1 且 thiriframe_url内容不空 显示网页
    public String thiriframe_url ;
    public String thiriframe_msg ;
    public String  fee_ratio ;
    //单笔最低提现金额: + min_money ,最高: max_money元
    public String min_money ;//单笔最低金额
    public String max_money ;//单笔最高提现
    /*

      typenum:6,
    name:"天下提现",
    channel_amount_use:0,
    fixamount_list_status:1,
    fixamount_list:Array,
    fixamount_list_status1:0,
    fixamount_list1:[],
    fixamount_rule_status:0,
    fixamount_rule:[],
    earnest_money_pl:0,
    didvided_list:[],
    opfast_min_money:0,
    opfast_max_money:0,
    thiriframe_use:0,
    thiriframe_status:0,
    thiriframe_url:"",
    thiriframe_msg:"",
    fee_ratio:"",
    min_money:"100.00",
    max_money:"10000.00",
     */

}
