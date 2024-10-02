package com.xtree.mine.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginResultVo implements Parcelable {
    public String captcha;//后台组织的验证码
    public String token; // "eyJ0eXAi***E2NTY1Ng",

    public String token_type; // "bearer",
    public String expires_in; // 604800
    public CookieVo cookie; // { }
    public int twofa_required; // 0-默认; 1-谷歌验证
    //public Object user_agency_model; // 注册接口返回的 { "user_agency_model": "2" }
    public boolean samewith_securitypwd; // false
    public ContactsVo contacts; // { } 异地登录 验证
    public String userName; // 登录名, 自己加的
    public String userpass;
    public Object inviteCodeSourceMsg ;//null 不显示弹窗，有数据就显示

    public static class ContactsVo implements Parcelable {
        public String phone; // "132****1233",
        public String email; // "x*****a@outlook.com",

        protected ContactsVo(Parcel in) {
            phone = in.readString();
            email = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(phone);
            dest.writeString(email);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ContactsVo> CREATOR = new Creator<ContactsVo>() {
            @Override
            public ContactsVo createFromParcel(Parcel in) {
                return new ContactsVo(in);
            }

            @Override
            public ContactsVo[] newArray(int size) {
                return new ContactsVo[size];
            }
        };
    }

    protected LoginResultVo(Parcel in) {
        token = in.readString();
        token_type = in.readString();
        expires_in = in.readString();
        cookie = in.readParcelable(CookieVo.class.getClassLoader());
        twofa_required = in.readInt();
        samewith_securitypwd = in.readByte() != 0;
        contacts = in.readParcelable(ContactsVo.class.getClassLoader());
        userName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeString(token_type);
        dest.writeString(expires_in);
        dest.writeParcelable(cookie, flags);
        dest.writeInt(twofa_required);
        dest.writeByte((byte) (samewith_securitypwd ? 1 : 0));
        dest.writeParcelable(contacts, flags);
        dest.writeString(userName);
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
                "captcha='" + captcha + '\'' +
                ", token='" + token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", cookie=" + cookie +
                ", twofa_required=" + twofa_required +
                ", samewith_securitypwd=" + samewith_securitypwd +
                ", contacts=" + contacts +
                ", userName='" + userName + '\'' +
                ", userpass='" + userpass + '\'' +
                '}';
    }
}
