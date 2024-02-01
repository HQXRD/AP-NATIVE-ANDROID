package com.xtree.mine.vo;

public class QuestionVo {

    // 失败/不通过,返回错误信息 msg_detail,msg_type,message
    //public String msg_detail;
    public int msg_type; // 1 (异常/失败的时候) 1,2-失败, 3-成功
    public String message; // "缺少 Token"

    public String content = "";

    @Override
    public String toString() {
        return "QuestionVo{" +
                "msg_type=" + msg_type +
                ", message='" + message + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
