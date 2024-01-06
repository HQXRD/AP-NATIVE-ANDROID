package me.xtree.mvvmhabit.http;

/**
 * 该类仅供参考，实际业务返回的固定字段, 根据需求来定义
 */
public class PMBaseResponse<T> {
    private String code;
    private String msg = "";
    public long ts; // 1700702751
    private T data;

    @Override
    public String toString() {
        String dataStr;
        if(data != null){
            dataStr = ", data=" + data;
        }else{
            dataStr = ", data=null";
        }
        return "BaseResponse{" +
                "code=" + code +
                ", message='" + msg + '\'' +
                ", timestamp=" + ts +
                dataStr +
                '}';
    }

    public int getCode() {
        return Integer.valueOf(code);
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

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
