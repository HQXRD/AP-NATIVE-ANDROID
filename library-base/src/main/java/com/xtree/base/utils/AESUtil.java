package com.xtree.base.utils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    public static SecretKey getRSAKeyPair() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();

        CfLog.e("getAlgorithm() : " + secretKey.getAlgorithm() + " getFormat() : " + secretKey.getFormat() + " getEncoded() : " + new String(secretKey.getEncoded()));
        return secretKey;
    }

    // 本地端生加密
    public static String encryptData(String data, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(data.getBytes());

        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }

    // 本地端生解密
    public static String decryptData(String data, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] encryptedBytes = Base64.decode(data, Base64.DEFAULT);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes);
    }

    public static String decryptData(String data, String secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.decode(secretKey, Base64.DEFAULT), "AES"));

        byte[] encryptedBytes = Base64.decode(data, Base64.DEFAULT);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        CfLog.d(new String(decryptedBytes, StandardCharsets.UTF_8));
        return new String(decryptedBytes);
    }
}
