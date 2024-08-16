package com.xtree.mine.vo.request;

import com.xtree.base.utils.UuidUtil;

import java.util.HashSet;

/**
 * Created by KAKA on 2024/3/20.
 * Describe:
 */
public class DividendAgrtSendRequest {

    private HashSet<String> userid;
    private String type;
    private String cycle_id;
    private String nonce;

    public HashSet<String> getUserid() {
        return userid;
    }

    public void setUserid(HashSet<String> userid) {
        this.userid = userid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCycle_id() {
        return cycle_id;
    }

    public void setCycle_id(String cycle_id) {
        this.cycle_id = cycle_id;
    }

    public String getNonce() {
        return UuidUtil.getID16();
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
