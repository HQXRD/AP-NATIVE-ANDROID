package com.xtree.base.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    public static String key = "wnIem4HOB2RKzhiqpaqbZuxtp7T36afAHH88BUht/2Y=";
    public static final String json = "{" +
            "    \"H5\": [" +
            "      \"https://h51.y7d8c5.com\"," +
            "      \"https://h52.u5d2u3.com\"," +
            "      \"https://h52.c3h4z1.com\"," +
            "      \"https://h52.p3g6t7.com\"," +
            "      \"https://h53.k6y1y7.com\"," +
            "      \"https://h53.v4h6o3.com\"," +
            "      \"https://h53.h7y7p2.com\"," +
            "      \"https://h54.y7g2n9.com\"," +
            "      \"https://h55.y7t9j5.com\"" +
            "    ]," +
            "    \"api\": [" +
            "      \"https://app.zgntcpgxds.com:16801\"," +
            "      \"http://43.128.11.58:16804\"," +
            "      \"http://43.137.39.194:16803\"," +
            "      \"http://101.43.19.60:16803\"," +
            "      \"http://139.186.248.198:16803\"," +
            "      \"https://app1.g7c5e4.com\"," +
            "      \"https://app1.l1x7u3.com\"," +
            "      \"https://app1.d7u3z2.com\"," +
            "      \"https://app1.n6g1c2.com\"," +
            "      \"https://app2.y9s6f4.com\"," +
            "      \"https://app2.e7y4m2.com\"," +
            "      \"https://app2.u6z2r8.com\"," +
            "      \"https://app2.z3w1j8.com\"," +
            "      \"https://app3.g7d3w3.com\"," +
            "      \"https://app3.n9f4b7.com\"," +
            "      \"https://app3.o7o9x1.com\"," +
            "      \"https://app3.a8y7u9.com\"," +
            "      \"https://app4.f4w3f2.com\"," +
            "      \"https://app4.e4a9z6.com\"," +
            "      \"https://app5.s6r9x3.com\"" +
            "    ]" +
            "  }";
    public static String encrypt(){
        try {
            return encryptData(json, new SecretKeySpec(Base64.decode(key, Base64.DEFAULT), "AES"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(){
        try {
            String json = "DkyWc3OH9L4Hyl6gb+53Kuzp3Nu6Uk47jL1trCb1fiN7DI5OyVe6qbBd6xFxXr1NklfbhG9h9Cvn4nX//iW+DzVWWvmsaE68ORCuNWB6lIwlSDWmdBFvWXzHDhd957zgbPb5oRN4hKY02+sMXkwaVjdVOzvCBc6JWDqZ5bVFTlPz5JMT3esMHNYImtpK2QesbBs1sHI+OT22EAoNj04DR066tBiHpcYO8eJ3VFJX7ClbCNhZRTA554DF5U9EmO8ZqxbyPg6mC7QnjeTgJngXJosaU9gE+UfHtx4ukb5Tth7vdF+nI8Gul3wR0GEW6VuJKm33SBceoCiAfhYblKkgVDQn3RJWTBL4qYu78q/72XW4iBovwypsFkAiT04hkgjWYWiqXI0cCZ7qD+HijECDet0TRCdc4ITNqNh9Ap0f98J5pGGbbfepwYsNyEAOu6Y3JMJ8q+MlNYuu8wQCgM3jbvC/yerTVEmZAxlbLa8yD2Kbhmm95cm8e+dgcjX78zYdD/xMJDaNxXmOKJ+iYXN7PfqkJPFIN069czRGmv6cAzqRl2xsHvJh1NKlQfV3QXX9y9PSc8xrGv+hFCQvw5xHBSUJVItV5+dVry+lNLObUbFbCNhZRTA554DF5U9EmO8Z9PeDY34N3sE37vZD3/LdZu6cX7sRQDOuLu718KYbTKpLdqgWEw3Oxc1NOuB8yb19T3CfPlCrfoBUVQQJ9OjkmI3Z7nSzFyR/U8OwpJCYX+IMtblgR6Szvi2/05oJzdxsqvjlQR3Pxd8i2yZ1vdklo4bUog4CATzkpXBR+kbL/pS3gCbiBBOZMwewhrXjNmx4mturQ6pY0AKdSeUQBHormGd1HLNRfh9hCF7aBIUzyZ7dks5LVFqRnRYE09eY38ZddAedSISXa8cIjrE0pGkueqpWnvTiY7DTZ2Rg9jvvzSQ7g82ZsdCT2H7TzNrqRjbNvnssG/6YCfB1El0qpoJyzEXy103BAh77l5FyJZ/Xsc2N2e50sxckf1PDsKSQmF/iBRnwQTmWi8V2vVN46YpDOY2bYytfJ0UgH34iNLrucek=";
            CfLog.e("-----------" + json);
            return decryptData(json, new SecretKeySpec(Base64.decode(key, Base64.DEFAULT), "AES"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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

        return new String(decryptedBytes);
    }
}
