package com.xtree.mine.vo;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class SettingsVo implements Parcelable {
    public String register_captcha_switch;//1：注册页面需要显示验证码 0 注册页面不需要显示验证码
    public String public_key; // "-----BEGIN PUBLIC KEY-----MIGf***AQAB-----END PUBLIC KEY-----"
    public String barrage_api_url; // "wss://www.shenbofilm.com/wss/"
    public String promption_code; // "kygprka"
    public String default_promption_code; // "kygprb"
    public String customer_service_url; // "https://www.mjooh9i.com/service/chatlink.html"
    // ["https://www.mjooh9i.com/service/chatlink.html", "https://www.5trfg4g.com/service/chatlink.html"]
    public String[] customer_service_urls; //
    public String[] x9_customer_service_url; // ["https://www.hikefu01.net/im/index?appid=0NYEsh"]

    protected SettingsVo(Parcel in) {
        public_key = in.readString();
        barrage_api_url = in.readString();
        promption_code = in.readString();
        default_promption_code = in.readString();
        customer_service_url = in.readString();
        customer_service_urls = in.createStringArray();
        x9_customer_service_url = in.createStringArray();
    }

    public static final Creator<SettingsVo> CREATOR = new Creator<SettingsVo>() {
        @Override
        public SettingsVo createFromParcel(Parcel in) {
            return new SettingsVo(in);
        }

        @Override
        public SettingsVo[] newArray(int size) {
            return new SettingsVo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(public_key);
        parcel.writeString(barrage_api_url);
        parcel.writeString(promption_code);
        parcel.writeString(default_promption_code);
        parcel.writeString(customer_service_url);
        parcel.writeStringArray(customer_service_urls);
        parcel.writeStringArray(x9_customer_service_url);
    }

    @Override
    public String toString() {
        return "SettingsVo{" +
                "public_key='" + public_key + '\'' +
                ", barrage_api_url='" + barrage_api_url + '\'' +
                ", promption_code='" + promption_code + '\'' +
                ", default_promption_code='" + default_promption_code + '\'' +
                ", customer_service_url='" + customer_service_url + '\'' +
                ", customer_service_urls=" + Arrays.toString(customer_service_urls) +
                ", x9_customer_service_url=" + Arrays.toString(x9_customer_service_url) +
                '}';
    }
}
