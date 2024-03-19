package com.xtree.mine.vo.request;

import com.xtree.base.utils.UuidUtil;

import java.util.List;

/**
 * Created by KAKA on 2024/3/18.
 * Describe:
 */
public class RebateAgrtCreateRequest {

    private String users;
    private List<String> min_bet;
    private List<String> min_player;
    private List<String> ratio;
    private String tag = "create";
    private String nonce;

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public List<String> getMin_bet() {
        return min_bet;
    }

    public void setMin_bet(List<String> min_bet) {
        this.min_bet = min_bet;
    }

    public List<String> getMin_player() {
        return min_player;
    }

    public void setMin_player(List<String> min_player) {
        this.min_player = min_player;
    }

    public List<String> getRatio() {
        return ratio;
    }

    public void setRatio(List<String> ratio) {
        this.ratio = ratio;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getNonce() {
        return UuidUtil.getID16();
    }

}
