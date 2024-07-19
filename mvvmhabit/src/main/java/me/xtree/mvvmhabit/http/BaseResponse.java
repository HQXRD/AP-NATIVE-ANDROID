package me.xtree.mvvmhabit.http;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 该类仅供参考，实际业务返回的固定字段, 根据需求来定义
 */
public class BaseResponse<T> {
    private int status = -1;
    private int code = -1;
    private boolean success;
    @SerializedName(value = "message", alternate = {"msg", "sMsg"})
    @Expose
    private String message = ""; // msg, sMsg
    private AuthVo authorization;
    public int timestamp; // 1700702751
    private T data;
//    //原数据
//    @SerializedName("data")
//    private Object data;
//    //转换数据
//    private T dataBean;
    private String dataString;

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

//        public T getData() {
//        if (dataBean == null) {
//            Gson gson = new Gson();
//            String json = gson.toJson(data);
//
//            ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
//            Class<T> actualTypeArgument = (Class<T>) parameterizedType.getActualTypeArguments()[0];
//
//            ResultParameterType jsonType = new ResultParameterType(actualTypeArgument);
//
//            try {
//                dataBean = new Gson().fromJson(json, jsonType);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
////            dataBean = gson.fromJson(json, new TypeToken<T>() {
////            }.getType());
//        }
//        return dataBean;
//    }
//
//    public void setData(T data) {
//        this.dataBean = data;
//    }

    public String getDataString() {
//        if (dataString == null) {
//            dataString = new Gson().toJson(data);
//        }
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
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
