package com.xtree.base.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    /**
     * 获取 MD5值
     * https://blog.51cto.com/u_16175513/7882991
     *
     * @param input 原始字符串
     * @return MD5 值
     */
    public static String generateMd5(String input) {
        try {
            // 步骤1：导入Java的MessageDigest库
            // java.security.MessageDigest是Java的Message Digest类，它提供了MD5算法的实现。

            // 步骤2：创建MessageDigest对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 步骤3：将待加密字符串转换为字节数组
            byte[] bytes = input.getBytes();

            // 步骤4：使用MessageDigest对象更新字节数组
            md.update(bytes);

            // 步骤5：通过MessageDigest对象生成MD5哈希值
            byte[] md5Bytes = md.digest();

            // 步骤6：将MD5哈希值转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte md5Byte : md5Bytes) {
                sb.append(Integer.toString((md5Byte & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
