package com.xtree.base.utils;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAEncrypt {
    /**
     * RSA算法
     */
    public static final String RSA = "RSA";
    /**加密方式，android的 */
    // public static final String TRANSFORMATION = "RSA/None/NoPadding";
    /**
     * 加密方式，标准jdk的
     */
    public static final String TRANSFORMATION = "RSA/None/PKCS1Padding";

    public static String encrypt(String str, String publicKey) {

        try {
            byte[] input = encryptByPublicKey(str.getBytes("UTF-8"), publicKey.getBytes("UTF-8"));
            String ss = String.valueOf(input);
            CfLog.e("ss: " + ss);
            return Base64.encodeToString(input, Base64.DEFAULT);

        } catch (Exception e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 使用公钥加密
     * https://cloud.tencent.com/developer/article/1730404
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        // 加密数据
        Cipher cp = Cipher.getInstance(TRANSFORMATION);
        cp.init(Cipher.ENCRYPT_MODE, pubKey);
        return cp.doFinal(data);
    }

    /**
     * 使用私钥解密
     */
    public static byte[] decryptByPrivateKey(byte[] encrypted, byte[] privateKey) throws Exception {
        // 得到私钥对象
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);
        // 解密数据
        Cipher cp = Cipher.getInstance(TRANSFORMATION);
        cp.init(Cipher.DECRYPT_MODE, keyPrivate);
        byte[] arr = cp.doFinal(encrypted);
        return arr;
    }

    /**
     * 加密
     * https://blog.csdn.net/qy20115549/article/details/83105736
     *
     * @param str       明文
     * @param publicKey 公钥
     * @return 密文
     */
    public static String encrypt2(String str, String publicKey) {
        byte[] decoded = Base64.decode(publicKey, Base64.DEFAULT);
        try {
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            // RSA加密
            //Cipher cipher = Cipher.getInstance("RSA");
            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding"); // RSA PKCS1Padding
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] input = cipher.doFinal(str.getBytes("UTF-8"));
            //byte[] input = cipher.doFinal(str.getBytes());
            return Base64.encodeToString(input, Base64.DEFAULT);
        } catch (Exception e) {
            CfLog.e(e.toString());
            e.printStackTrace();
        }
        return "";
    }
}
