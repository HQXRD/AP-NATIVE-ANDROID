package com.xtree.base.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

public class QrcodeUtil {
    static int WIDTH = 300;
    static int HEIGHT = 300;

    /**
     * @param content 要转换成二维码的文字
     * @return 二维码图片
     */
    public static Bitmap getQrcode(String content) {
        return getQrcode(content, false);
    }

    public static Bitmap getQrcode(String content, boolean isNoWhiteBorder) {
        return getQrcode(content, WIDTH, HEIGHT, isNoWhiteBorder);
    }

    /**
     * @param content         要转换成二维码的文字
     * @param width           宽度
     * @param height          高度
     * @param isNoWhiteBorder 是否去掉白色边框, true:去掉; false:否(默认)
     * @return 二维码图片
     */
    public static Bitmap getQrcode(String content, int width, int height, boolean isNoWhiteBorder) {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();

        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix endCode = null;
        try {
            endCode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            CfLog.e(e.toString());
        }

        if (isNoWhiteBorder) {
            endCode = updateBit(endCode, 0);
        }
        int newWidth = endCode.getWidth();
        int newHeight = endCode.getHeight();

        int[] colors = new int[newWidth * newHeight];

        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                if (endCode.get(i, j)) {
                    colors[i * newWidth + j] = Color.BLACK;
                } else {
                    colors[i * newWidth + j] = Color.WHITE;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(colors, newWidth, newHeight, Bitmap.Config.RGB_565);

        return bitmap;
    }

    private static BitMatrix updateBit(BitMatrix matrix, int margin) {
        int tempM = margin * 2;
        int[] res = matrix.getEnclosingRectangle();
        int resWidth = res[2] + tempM;
        int resHeight = res[3] + tempM;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth - margin; i++) {
            for (int j = margin; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + res[0], j - margin + res[1])) {
                    resMatrix.set(i, j);
                }
            }
        }

        return resMatrix;
    }

}
