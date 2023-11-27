package com.xtree.home.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginResultVo implements Parcelable {

    public String token; // "eyJ0eXAi***E2NTY1Ng",

    public String token_type; // "bearer",
    public String expires_in; // 604800
    public CookieVo cookie; // { }
    public String twofa_required; // 0
    public boolean samewith_securitypwd; // false

    protected LoginResultVo(Parcel in) {
        token = in.readString();
        token_type = in.readString();
        expires_in = in.readString();
        cookie = in.readParcelable(CookieVo.class.getClassLoader());
        twofa_required = in.readString();
        samewith_securitypwd = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeString(token_type);
        dest.writeString(expires_in);
        dest.writeParcelable(cookie, flags);
        dest.writeString(twofa_required);
        dest.writeByte((byte) (samewith_securitypwd ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoginResultVo> CREATOR = new Creator<LoginResultVo>() {
        @Override
        public LoginResultVo createFromParcel(Parcel in) {
            return new LoginResultVo(in);
        }

        @Override
        public LoginResultVo[] newArray(int size) {
            return new LoginResultVo[size];
        }
    };

    @Override
    public String toString() {
        return "LoginResultVo{" +
                "token='" + token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", cookie=" + cookie +
                ", twofa_required='" + twofa_required + '\'' +
                ", samewith_securitypwd=" + samewith_securitypwd +
                '}';
    }

}
