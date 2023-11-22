package com.xtree.base.net;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * 定义移动端 应答报文中特有的属性
 */
public class AppTextMessageResponse<T> implements Serializable {

    private String code;
    private String msg;
    private T data;

    private static final long serialVersionUID = 4576543547832198559L;

    public AppTextMessageResponse() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "AppTextMessageResponse [ " +
                "code=" + code
                + ", msg=" + msg
                + ", data=" + new Gson().toJson(data) +
                " ]";
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return "0".equals(code);
    }

}
