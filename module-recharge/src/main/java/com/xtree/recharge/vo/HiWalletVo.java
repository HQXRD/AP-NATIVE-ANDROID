package com.xtree.recharge.vo;

public class HiWalletVo {

    public String login_url; // "https://h5.hicnyt-pre.com/signup?qy=1&platformCode=AS&platformUserId=5228471&userEmail="
    public boolean is_registered; // false
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
