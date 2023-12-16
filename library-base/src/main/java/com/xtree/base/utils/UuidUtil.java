package com.xtree.base.utils;

import java.util.UUID;

public class UuidUtil {

    /**
     * 获取UUID 32位,不带横线
     *
     * @return
     */
    public static String getID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取UUID 16位,不带横线
     * @return
     */
    public static String getID24() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 24);
    }
    /**
     * 获取UUID 16位,不带横线
     * @return
     */
    public static String getID16() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }


}
