package com.xtree.base.utils;

import android.view.View;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class StringUtils {
    public static String[] getPinYinInitials(String chinese) {
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chinese.charAt(0));
        String[] initials = new String[pinyinArray.length];

        for (int i = 0; i < pinyinArray.length; i++) {
            initials[i] = pinyinArray[i].substring(0, 1).toUpperCase();
        }

        return initials;
    }

    public static  boolean isInteger(String value)
    {
        try {
            Integer.parseInt(value);
            return true ;
        }catch (Exception e)
        {
            return  false;
        }
    }
    public static  boolean isDouble(String value)
    {
        try {
            Double.parseDouble(value);
            if (value.contains(".")) return true ;
            return  false;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
