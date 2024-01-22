package com.xtree.mine.vo;

import androidx.annotation.NonNull;

/**
 * 绑定谷歌动态口令
 */
public class BindGoogleVO {
    public String message ;


    public BindGoogleVO() {
    }

    public BindGoogleVO(String message) {
        this.message = message;
    }

    @NonNull
    @Override
    public String toString() {
        return "BindGoogleVO { " +
                "message='" + message +
                '}';
    }
}
