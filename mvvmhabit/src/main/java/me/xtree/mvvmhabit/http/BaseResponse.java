package me.xtree.mvvmhabit.http;

/**
 * Created by goldze on 2017/5/10.
 * 该类仅供参考，实际业务返回的固定字段, 根据需求来定义，
 */
public class BaseResponse<T> {
    private int status = -1;
    private int code = -1;
    private String message = ""; // msg, sMsg
    private AuthVo authorization;
    public int timestamp; // 1700702751
    private T data;

    public class AuthVo {
        public String token; // "eyJ0eXAiOi***NTViMg"
        public String token_type; // "bearer"
        public int expires_in; // 604800 (7 day)

        @Override
        public String toString() {
            return "AuthVo{" +
                    "token='" + token + '\'' +
                    ", token_type='" + token_type + '\'' +
                    ", expires_in=" + expires_in +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", data=" + data +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isOk() {
        return status == 10000;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuthVo getAuthorization() {
        return authorization;
    }

    public void setAuthorization(AuthVo authorization) {
        this.authorization = authorization;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

}
