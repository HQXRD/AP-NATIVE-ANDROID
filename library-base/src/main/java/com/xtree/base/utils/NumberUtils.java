package com.xtree.base.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {
    /**
     * 保留小数点后scale位
     * @param value
     * @param scale
     * @return
     */
    public static String format(double value, int scale) {

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, RoundingMode.HALF_UP);
        return bd.toString();
    }
}
