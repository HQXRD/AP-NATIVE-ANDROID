package com.xtree.base.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * 读取剪切板工具类
 */
public class ClipboardUtil {
    private ClipboardUtil(){

    }

    public static String   getText(Context context){
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData !=null && clipData.getItemCount() >0){
            return clipData.getItemAt(0).coerceToText(context).toString();
        }
        return "";
    }
}
