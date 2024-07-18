package com.xtree.base.utils;

import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by KAKA on 2024/7/18.
 * Describe: HMACSHA256 加密工具
 */
public class HmacSHA256Utils {

    //接口使用的秘钥
    public static final String HMAC_KEY = "vVdKN0TqbXgZm64d";
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    public static String calculateHmacSHA256(String key, String data) {
        try {
            Mac hmacSHA256 = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
            hmacSHA256.init(secretKey);
            byte[] hmacData = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hmacData); // 转换为十六进制字符串
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
