package com.xtree.mine.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class CookieVo implements Parcelable {

    public String sessid; // "d8dfcb27***5df7b17c",
    public String cookie_name; // "_sessionHandler",
    public String cookie_path; // "/",
    public int expires_in; // 0

    protected CookieVo(Parcel in) {
        sessid = in.readString();
        cookie_name = in.readString();
        cookie_path = in.readString();
        expires_in = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sessid);
        dest.writeString(cookie_name);
        dest.writeString(cookie_path);
        dest.writeInt(expires_in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CookieVo> CREATOR = new Creator<CookieVo>() {
        @Override
        public CookieVo createFromParcel(Parcel in) {
            return new CookieVo(in);
        }

        @Override
        public CookieVo[] newArray(int size) {
            return new CookieVo[size];
        }
    };

    @Override
    public String toString() {
        return "CookieVo{" +
                "sessid='" + sessid + '\'' +
                ", cookie_name='" + cookie_name + '\'' +
                ", cookie_path='" + cookie_path + '\'' +
                ", expires_in=" + expires_in +
                '}';
    }
}
