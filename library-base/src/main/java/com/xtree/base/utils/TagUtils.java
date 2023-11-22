package com.xtree.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.xtree.base.BuildConfig;
import com.google.gson.Gson;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TagUtils {

    public static String CHANNEL_NAME = ""; // 本包的渠道名,关于页显示当前安装包的渠道名用的。
    public static String MEDIA_SOURCE = ""; // 旧包的渠道名,就是升级到最新官方安装包前的渠道名。

    public static String MIXPANEL_TOKEN = "******";
    public static String USER_ID = "";

    public static void initMixpanel(Context ctx) {
        String channelName = CHANNEL_NAME; // 渠道号
        JSONObject props = new JSONObject();
        try {
            props.put("name", channelName);
        } catch (JSONException e) {
        }

        TagUtils.MIXPANEL_TOKEN = MIXPANEL_TOKEN;
        initMixpanel(ctx, props);
    }

    public static void initMixpanel(Context ctx, JSONObject props) {
        CfLog.i("******");
        MixpanelAPI.getInstance(ctx, MIXPANEL_TOKEN, props, true);
        if (TagUtils.USER_ID != null && !TagUtils.USER_ID.isEmpty()) {
            MixpanelAPI.getInstance(ctx, MIXPANEL_TOKEN, true).identify(TagUtils.USER_ID);
        } else {
            /*SharedPreferences sp = ctx.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            String dvcId = sp.getString("dvcId", "");
            if (dvcId.isEmpty()) {
                dvcId = TimeUtils.getCurDate() + "-" + UUID.randomUUID().toString().substring(0, 4);
                sp.edit().putString("dvcId", dvcId).commit();
            }*/
            String dvcId = getDevId(ctx);
            CfLog.i("dvcId: " + dvcId);
            MixpanelAPI.getInstance(ctx, MIXPANEL_TOKEN, true).identify(dvcId);
        }
    }

    public static void logEvent(Context ctx, String event) {
        CfLog.e(ctx.getClass().getSimpleName() + ", event: " + event);
        tagAppsFlyer(ctx, event, getMap(null, null));
        tagMixpanel(ctx, event, null);
    }

    /*public static void logEvent(Context ctx, String event, Object value) {
        CfLog.e(ctx.getClass().getSimpleName() + ", event: " + event + ", value: " + value);
        AppsFlyerLib.getInstance().logEvent(ctx, event, getMap(event, value));

        tagMixpanel(ctx, event, value);
    }*/

    public static void logEvent(Context ctx, String event, String key, Object value) {
        CfLog.e(ctx.getClass().getSimpleName() + ", event: " + event + ", key: " + key + ", value: " + value);
        tagAppsFlyer(ctx, event, getMap(key, value));

        tagMixpanel(ctx, event, key, value);
    }

    public static void logEvent(Context ctx, String event, HashMap<String, Object> map) {
        CfLog.e(ctx.getClass().getSimpleName() + ", event: " + event + ", map: " + new Gson().toJson(map));
        if (!map.containsKey("uid")) {
            map.put("uid", USER_ID);
        }
        tagAppsFlyer(ctx, event, map);

        tagMixpanel(ctx, event, getJson(map));
    }

    /**
     * 注册/登录成功事件，带uerId
     *
     * @param ctx   Context
     * @param event event
     * @param uid   userId
     */
    public static void loginEvent(Context ctx, String event, String uid) {
        CfLog.e(ctx.getClass().getSimpleName() + ", event: " + event + ", uid: " + uid);
        USER_ID = uid;
        //HashMap<String, Object> map = new HashMap<>();
        //map.put("uid", uid);
        tagAppsFlyer(ctx, event, getMap("uid", uid));

        MixpanelAPI.getInstance(ctx, MIXPANEL_TOKEN, true).identify(uid);

        tagMixpanel(ctx, event, "uid", uid);
    }

    /**
     * 日常打点,每天最多只打一次点,统计日活.
     *
     * @param ctx
     */
    public static synchronized void tagDailyEvent(Context ctx) {
        CfLog.d(ctx.getClass().getSimpleName() + ", event: tagDaily");

        int date = ctx.getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getInt("lastTagDate", 0);
        int curDate = Integer.parseInt(TimeUtils.getCurDate());
        if (date != curDate) {
            CfLog.d("event: tagDaily, " + curDate);
            ctx.getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().putInt("lastTagDate", curDate).commit();
            tagAppsFlyer(ctx, "tagDaily", null);
            tagMixpanel(ctx, "tagDaily", null);
        }
    }

    private static void tagAppsFlyer(Context ctx, String event, HashMap<String, Object> map) {
        //AppsFlyerLib.getInstance().logEvent(ctx, event, map);
    }

    private static void tagMixpanel(Context ctx, String event, String key, Object value) {
        JSONObject props = getJson(key, value);
        tagMixpanel(ctx, event, props);
    }

    private static void tagMixpanel(Context ctx, String event, JSONObject props) {
        if (props == null) {
            props = new JSONObject();
        }
        if (!props.has("uid") && !TextUtils.isEmpty(USER_ID)) {
            try {
                props.put("uid", USER_ID);
            } catch (JSONException e) {
            }
        }
        MixpanelAPI.getInstance(ctx, MIXPANEL_TOKEN, true).track(event, props);
    }

    private static HashMap getMap(String key, Object value) {
        HashMap<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(key)) {
            map.put(key, value);
        }
        if (!TextUtils.isEmpty(USER_ID)) {
            map.put("uid", USER_ID);
        }
        if (map.isEmpty()) {
            return null;
        }
        return map;
    }

    private static JSONObject getJson(String key, Object value) {
        JSONObject props = new JSONObject();
        try {
            props.put(key, value);
            if (!TextUtils.isEmpty(USER_ID)) {
                props.put("uid", USER_ID);
            }
        } catch (JSONException e) {
        }
        return props;
    }

    private static JSONObject getJson(HashMap<String, Object> map) {
        JSONObject props = new JSONObject(map);
        if (!TextUtils.isEmpty(USER_ID)) {
            try {
                props.put("uid", USER_ID);
            } catch (JSONException e) {
            }
        }

        return props;
    }

    /**
     * 打开时间，取粗略的时间(用户喜欢在什么时间打开直播间或者APP <br>
     * 参数 open_time:  HHmm ，HH 00-23 , mm 分钟数 取15或30的倍数，00,15,30,45
     */
    public static String getOpenTime() {
        int periodTime = 30;
        String h = new SimpleDateFormat("HH").format(new Date());
        String m = new SimpleDateFormat("mm").format(new Date());
        String m2;

        if (Integer.valueOf(m) < periodTime) {
            m2 = "00";
        } else {
            m2 = String.valueOf(Integer.valueOf(m) / periodTime * periodTime);
        }
        CfLog.i(h + m2 + ",  (" + h + m + ")");
        return h + m2;
    }

    /**
     * 获取间隔的时间，返回 *s, *m
     * 停留时间，秒或分钟，5s,10s,20s,30s,40s,50s, 1m,2m,3m,4m,5m,
     * 10m,15m,20m,30m,40m...... 100m,110m, 130m，超过120分钟全部按130m算。
     */
    public static String getStayTime(long lastTime) {
        long seconds = (System.currentTimeMillis() - lastTime) / 1000;
        CfLog.i("dur: " + seconds + " seconds, (" + seconds / 60 + "m " + seconds % 60 + "s)");
        long minutes;
        String dur;
        if (seconds < 10) {
            dur = "5s";
        } else if (seconds < 60) {
            seconds = seconds / 10 * 10; // seconds 10,20,30,40,50
            dur = seconds + "s";
        } else if (seconds < 7200) {
            minutes = seconds / 60; // minutes
            if (minutes < 5) {
                // 分钟数不变 minutes 1,2,3...
            } else if (minutes <= 20) {
                minutes = minutes / 5 * 5; // minutes 5,10,15,20
            } else if (minutes < 120) {
                minutes = minutes / 10 * 10; // minutes 30,40,50,...100,110
            }
            dur = minutes + "m";
        } else {
            dur = "130m";
        }
        return dur;
    }

    /**
     * 获得设备的AndroidId
     *
     * @param ctx 上下文
     * @return 设备的AndroidId
     */
    private static String getDevId(Context ctx) {

        SharedPreferences sp = ctx.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String dvcId = sp.getString("dvcId", "");
        if (!dvcId.isEmpty()) {
            return dvcId;
        }
//        if (dvcId.isEmpty()) {
//            dvcId = TimeUtils.getCurDate() + "-" + UUID.randomUUID().toString().substring(0, 4);
//            sp.edit().putString("dvcId", dvcId).commit();
//        }

        String str = "";
        try {
            str = getAndroidId(ctx);
            CfLog.i("AndroidId: " + str); // AndroidId: e676****0c4654f3
            str = str.substring(0, 4) + "-" + str.substring(str.length() - 4);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //dvcId = TimeUtils.getCurDate() + "-" + str;
        dvcId = str;
        sp.edit().putString("dvcId", dvcId).commit();
        return dvcId;
    }

    /**
     * 获得设备的AndroidId
     *
     * @param context 上下文
     * @return 设备的AndroidId
     */
    private static String getAndroidId(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获得设备序列号（如：WTK7N16923005607）, 个别设备无法获取
     *
     * @return 设备序列号
     */
    private static String getSERIAL() {
        try {
            return Build.SERIAL;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

}
