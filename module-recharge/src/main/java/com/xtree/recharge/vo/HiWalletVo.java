package com.xtree.recharge.vo;

public class HiWalletVo {

    public String login_url; // "https://h5.hicnyt-pre.com/signup?qy=1&platformCode=AS&platformUserId=5228471&userEmail="
    public boolean is_registered = true; // 新加字段; false:未注册,要弹窗; true:已注册,不用弹窗,进入下一步; (默认true不弹窗)
    public String download_url; // "https://www.oeikdj.com/"

    @Override
    public String toString() {
        return "HiWalletVo { " +
                "login_url='" + login_url + '\'' +
                ", is_registered=" + is_registered +
                ", download_url='" + download_url + '\'' +
                '}';
    }

}
