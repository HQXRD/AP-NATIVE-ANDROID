package com.xtree.recharge.vo;

import java.util.ArrayList;
import java.util.List;

public class RechargePayVo {

    public String status; // "success",
    public String msg; // "ok",
    public String paycode; // "hiwallet",
    public String payname; // "CNYT快付",
    //public String orderNo; // "3bb740bc",
    //public String relorderNO; // "3bb740bc",
    public String money; // "90000.00",
    public boolean isredirect; // true,
    public String redirecturl; // "https://www.hiwalletapp.com/qrcode?callBackUrl=https%3A%2F%2Fadmin.jiuzhouxi.com%2Fhighadmin%2Fdepositapi%2Fhiwallet%2F25&createTime=1701854026&currencyType=1&account=CNYT0DE08BA99B821000&orderNumber=3bb740bc&amount=90000.00&sign=B20F6473765691FA515D65225017952B",
    //public boolean isqrcode; // false,
    //public String qrcodeurl; // null,
    //public boolean isbank; // false,
    //public boolean iszskh; // false,
    //public String postscript; // null,
    public String bankcode; // "hiwallet",
    public String bankname; // "CNYT快付",
    public String bankacctcard; // null,
    public String bankacctname; // null,
    //public String provice; // null,
    public boolean bankcardneedcopy; // false,
    public String maxexpiretime; // "10",
    //public String bank_url; // "",
    //public String help_url; // "",
    //public boolean upamount; // false,
    //public String fee; // null,
    //public String feeamount; // null,
    //public boolean isusdt; // false,
    //public String udtType; // null,
    //public String rate; // null,
    //public String rateamount; // null,
    //public boolean nflashtime; // false,
    //public String flashtime; // null,
    //public String loss_bank; // null,
    public String toBankName; // null,
    public String toBankNameDetail; // null,
    //public String alipayName; // "",
    public boolean isRedirectMode; // true
    public boolean direct_enable; // true
    public Object domain_list; // [ "https:\/\/go.gobizas.net",  "https:\/\/go.sjhbiz.net" ]
    public List<String> domainList = new ArrayList<>(); // [ "https:\/\/go.gobizas.net",  "https:\/\/go.sjhbiz.net" ]

}
