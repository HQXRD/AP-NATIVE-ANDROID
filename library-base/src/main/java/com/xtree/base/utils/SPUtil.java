package com.xtree.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

public class SPUtil {
    private SharedPreferences sp;
    public static final String KEY_USER = "key_user";
    private String token = null;

    private static SPUtil spu = null;

    public static SPUtil get(Context ctx) {
        if (spu == null) {
            spu = new SPUtil(ctx, "setting");
        }
        //return new SPUtil(ctx, "setting");
        return spu;
    }

    private SPUtil(Context context, String fileName) {
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public void clearAll() {
        CfLog.i("***********************************");
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }

    public void clear(String key) {
        CfLog.i("***********************************");
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.commit();
    }

    /*public void setUser(UserVo vo)
    {
        CfLog.i(KEY_USER + ": " + new Gson().toJson(vo));
        sp.edit().putString(KEY_USER, new Gson().toJson(vo)).commit();
        token = vo.token;
        CfLog.d("token: " + token);
    }*/

    /*public UserVo getUser()
    {
        UserVo vo = null;
        String json = sp.getString(KEY_USER, null);
        if (!TextUtils.isEmpty(json))
        {
            vo = new Gson().fromJson(json, UserVo.class);
        }

        if (null == vo)
        {
            vo = new UserVo();
        }

        CfLog.d(KEY_USER + ": " + new Gson().toJson(vo));
        return vo;
    }*/

    /*public String getToken()
    {
        //CfLog.d("token: " + getUser().token);
        //return getUser().token;

        if (TextUtils.isEmpty(token))
        {
            //CfLog.d("token: " + getUser().token);
            token = getUser().token;
            return getUser().token;
        }

        return token;
    }*/


    public void putObject(String key, Object obj) {
        String value = new Gson().toJson(obj);
        CfLog.i(key + ": " + value);
        sp.edit().putString(key, value).commit();
    }

    public <T> T getObject(String key, Class<T> clazz) {
        T vo = null;
        String json = sp.getString(key, null);
        if (!TextUtils.isEmpty(json)) {
            vo = new Gson().fromJson(json, clazz);
        }

        CfLog.d(key + ": " + new Gson().toJson(vo));
        return vo;
    }

    public <T> T getObject(String key, Class<T> clazz, T def) {
        T vo;
        String json = sp.getString(key, null);
        if (!TextUtils.isEmpty(json)) {
            vo = new Gson().fromJson(json, clazz);
        } else {
            return def;
        }

        CfLog.d(key + ": " + new Gson().toJson(vo));
        return vo;
    }

    public String get(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public boolean get(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public float get(String key, float defValue) {
        return sp.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public long get(String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    public long get(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    public void put(String key, String value) {
        CfLog.i(key + ": " + value);
        if (value == null) {
            sp.edit().remove(key).commit();
        } else {
            sp.edit().putString(key, value).commit();
        }
    }

    public void put(String key, boolean value) {
        CfLog.i(key + ": " + value);
        sp.edit().putBoolean(key, value).commit();
    }

    public void put(String key, float value) {
        CfLog.i(key + ": " + value);
        sp.edit().putFloat(key, value).commit();
    }

    public void put(String key, long value) {
        CfLog.i(key + ": " + value);
        sp.edit().putLong(key, value).commit();
    }

    public void put(String key, int value) {
        CfLog.i(key + ": " + value);
        sp.edit().putInt(key, value).commit();
    }

    public void putLong(String key, long value) {
        CfLog.i(key + ": " + value);
        sp.edit().putLong(key, value).commit();
    }

    public void putInt(String key, int value) {
        CfLog.i(key + ": " + value);
        sp.edit().putInt(key, value).commit();
    }

}
