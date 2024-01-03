package com.xtree.base.utils;

import net.sourceforge.pinyin4j.PinyinHelper;

public class StringUtils {
    public static String[] getPinYinInitials(String chinese) {
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chinese.charAt(0));
        String[] initials = new String[pinyinArray.length];

        for (int i = 0; i < pinyinArray.length; i++) {
            initials[i] = pinyinArray[i].substring(0, 1).toUpperCase();
        }

        return initials;
    }
}
