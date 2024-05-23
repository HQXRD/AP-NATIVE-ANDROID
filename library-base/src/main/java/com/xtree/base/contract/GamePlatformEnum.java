package com.xtree.base.contract;

/**
 * Created by KAKA on 2024/4/4.
 * Describe: 游戏场馆枚举
 */
public enum GamePlatformEnum {
    //空
    NULL("", ""),
    CENTER("中心钱包", "lottery"),
    PT("PT娱乐", "pt"),
    BBIN("BBIN娱乐", "bbin"),
    AG("AG街机捕鱼", "ag"),
    DBEGAME("DB电竞", "obgdj"),
    YY("云游棋牌", "yy"),
    DB("DB棋牌", "obgqp"),
    WALI("瓦力棋牌", "wali"),
    ;

    private String name;
    private String code;

    GamePlatformEnum(String platformName, String platformCode) {
        name = platformName;
        code = platformCode;
    }

    public static GamePlatformEnum checkCode(String code) {
        switch (code) {
            case "pt":
                return PT;
            case "bbin":
                return BBIN;
            case "ag":
                return AG;
            case "obgdj":
                return DBEGAME;
            case "yy":
                return YY;
            case "obgqp":
                return DB;
            case "lottery":
                return CENTER;
            case "wali":
                return WALI;
            default:
                return NULL;
        }
    }

    public static GamePlatformEnum checkName(String name) {
        switch (name) {
            case "PT娱乐":
                return PT;
            case "BBIN娱乐":
                return BBIN;
            case "AG街机捕鱼":
                return AG;
            case "DB电竞":
                return DBEGAME;
            case "云游棋牌":
                return YY;
            case "DB棋牌":
                return DB;
            case "中心钱包":
                return CENTER;
            case "瓦力棋牌":
                return WALI;
            default:
                return null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
