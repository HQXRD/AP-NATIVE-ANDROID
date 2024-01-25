package com.xtree.recharge.vo;
/**
 * 反馈页面图片上传
* */
public class FeedbackImageUploadVo {


    public String url;
    public String domainurl;
    //public String desc;

    public FeedbackImageUploadVo(String url, String domainurl) {
        this.domainurl = domainurl;
        this.url = url;
    }

    @Override
    public String toString() {
        return "FeedbackImageUploadVo{" +
                "domainurl='" + domainurl + '\'' +
                ", url='" + url + '\'' +
                //", desc='" + desc + '\'' +
                '}';
    }
}
