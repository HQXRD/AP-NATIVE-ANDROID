package com.xtree.recharge.vo;

/**
 * 反馈页面 已存在的反馈信息
 */
public class OrderFeedbackVo {

    public String id;
    public String userpay_mode;
    public String userpay_virtual_protocol ;
    public String third_orderid ;
    public String userpay_amount ;
    public String userpay_time ;
    public String add_time ;
    public String cash_time ;
    public String order_status ;
    public String order_status_text ;
    public int order_edit_status ;



    public OrderFeedbackVo(String id , String userpay_mode , String  userpay_virtual_protocol ,String third_orderid ,
                           String userpay_amount, String userpay_time ,String add_time,
                           String cash_time, String order_status , String order_status_text , int order_edit_status)
    {
        this.id = id ;
        this.userpay_mode = userpay_mode ;
        this.userpay_virtual_protocol = userpay_virtual_protocol ;
        this.third_orderid =third_orderid ;
        this.userpay_amount = userpay_amount ;
        this.userpay_time = userpay_time ;
        this.add_time = add_time ;
        this.cash_time = cash_time ;
        this.order_status  = order_status ;
        this.order_status_text = order_status_text ;
        this.order_edit_status = order_edit_status ;
    }

    @Override
    public String toString() {
        return "OrderFeedbackVo{" +
                "id='" + id + '\'' +
                ", userpay_mode='" + userpay_mode + '\'' +
                ", userpay_virtual_protocol='" + userpay_virtual_protocol + '\'' +
                ", third_orderid='" + third_orderid + '\'' +
                ", userpay_amount='" + userpay_amount + '\'' +
                ", userpay_time='" + userpay_time + '\'' +
                ", add_time='" + add_time + '\'' +
                ", cash_time='" + cash_time + '\'' +
                ", order_status='" + order_status + '\'' +
                ", order_status_text='" + order_status_text + '\'' +
                ", order_edit_status=" + order_edit_status +
                '}';
    }
}
