package com.xtree.service.message;

public class MessageType {

    public enum Socket {
        HEART(1),  // 心跳
        STOP(2),   // 停止
        MESSAGE(3); // 消息

        private final int code;

        // 构造方法
        Socket(int code) {
            this.code = code;
        }

        // 根据整数值获取枚举常量
        public static Socket fromCode(int code) {
            for (Socket type : Socket.values()) {
                if (type.code == code) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown code: " + code);
        }

        // 获取枚举对应的整数值
        public int getCode() {
            return code;
        }
    }

    public enum Input {
        CONNECT(1),
        LINK(2),
        LOGOUT(3);
        // Define input-related properties or methods here
        private final int code;

        // 构造方法
        Input(int code) {
            this.code = code;
        }

        // 根据整数值获取枚举常量
        public static Input fromCode(int code) {
            for (Input type : Input.values()) {
                if (type.code == code) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown code: " + code);
        }

        // 获取枚举对应的整数值
        public int getCode() {
            return code;
        }
    }

    public enum Output {
        // Define output-related properties or methods here
        OBTAIN_LINK(1),
        ; //

        private final int code;

        // 构造方法
        Output(int code) {
            this.code = code;
        }

        // 根据整数值获取枚举常量
        public static Output fromCode(int code) {
            for (Output type : Output.values()) {
                if (type.code == code) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown code: " + code);
        }

        // 获取枚举对应的整数值
        public int getCode() {
            return code;
        }
    }
}
