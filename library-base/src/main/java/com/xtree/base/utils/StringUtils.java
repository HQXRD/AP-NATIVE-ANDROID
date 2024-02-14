package com.xtree.base.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import com.xtree.base.R;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

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

    public  static  boolean isNumber(String str)
    {
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        String bigStr ;
        try {
            bigStr = new BigDecimal(str).toString();
        }catch (Exception exception)
        {
            return false;//
        }
        Matcher isNumber = pattern.matcher(bigStr);
        if (!isNumber.matches())
        {
            return  false;
        }
        return true ;
    }
    /** float 数据格式转换成逗号分割的String*/
    public static String formatToSeparate(float data)
    {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return df.format(data);
        
    }
    public static void copy(String txt) {
        CfLog.d(txt);
        ClipboardManager cm = (ClipboardManager) Utils.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData cd = ClipData.newPlainText("txt", txt);
        cm.setPrimaryClip(cd);
        ToastUtils.showLong(R.string.txt_copied);
    }

    public static boolean isStartHttp(final String txt)
    {
        return  txt.startsWith("https:") ;
    }
}
