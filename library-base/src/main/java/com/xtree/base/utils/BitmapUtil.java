package com.xtree.base.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Bitmap工具类
 */
public class BitmapUtil {

    /**
     * Base64格式转换成 bitMap
     * @param base64Str
     * @return
     */
    public static final Bitmap base64StrToBitmap(  String base64Str){
        if (base64Str.startsWith("data:image/") && base64Str.contains(",")) {
            base64Str = base64Str.split(",")[1];
        }

        byte[] decodedString = Base64.decode(base64Str, Base64.DEFAULT);
        InputStream inputStream = new ByteArrayInputStream(decodedString);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }
}
