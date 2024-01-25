package com.xtree.base.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 *  图片上传工具类
 */
public class ImageUploadUtil {

    /**
     * 图片上传 通过图片名称生产Base64 字符串
     * @param imageFile
     * @return
     */
    public static String bitmapToString(String imageFile)
    {
        Bitmap bitmap = getSmailBitmap(imageFile);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream() ;
        //压缩
        bitmap.compress(Bitmap.CompressFormat.JPEG , 40, byteArrayOutputStream) ;
        byte[] b  = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(b ,Base64.DEFAULT);
    }

    public static Bitmap getSmailBitmap(String imageFile)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options() ;
        options.inJustDecodeBounds = true ;
        BitmapFactory.decodeFile(imageFile,options);
        options.inSampleSize = calculateInSampleSize(options ,480 , 800);
        options.inJustDecodeBounds = false ;

        return BitmapFactory.decodeFile(imageFile , options);
    }

    public static  int calculateInSampleSize(BitmapFactory.Options options , int reqWidth , int reqHeight)
    {
        final  int height = options.outHeight;;
        final  int width = options.outWidth ;
        int inSampleSize = 1;
        if (height >reqHeight  || width >reqWidth)
        {
            final  int heightRatio = Math.round((float) height /(float) reqHeight);
            final  int widthRatio = Math.round((float) width /(float) reqWidth);
            inSampleSize = heightRatio<widthRatio ?heightRatio:widthRatio ;
        }

        return inSampleSize;
    }
}
