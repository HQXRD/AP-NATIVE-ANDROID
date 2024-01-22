package com.xtree.mine.vo;

import androidx.annotation.NonNull;

/**
 * 谷歌动态口令
 */
public class GooglePswVO {
    public String secret ;


    public GooglePswVO() {
    }

    public GooglePswVO(String secret) {
        this.secret = secret;
    }

    @NonNull
    @Override
    public String toString() {
        return "GooglePSWVO { " +
                "secret='" + secret +
                '}';
    }
}
