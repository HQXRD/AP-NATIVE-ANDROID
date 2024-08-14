package com.xtree.base.vo;

import android.os.Parcel;

import java.util.List;

public class PMService implements BaseBean{
    private String userId;
    private String domain;
    private List<String> loginUrlArr;
    private String apiDomain;
    private String imgDomain;
    private String token;
    private String url;
    private String loginUrl;
    public boolean isDisabled;//true = 禁用，false = 启用

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<String> getLoginUrlArr() {
        return loginUrlArr;
    }

    public void setLoginUrlArr(List<String> loginUrlArr) {
        this.loginUrlArr = loginUrlArr;
    }

    public String getApiDomain() {
        return apiDomain;
    }

    public void setApiDomain(String apiDomain) {
        this.apiDomain = apiDomain;
    }

    public String getImgDomain() {
        return imgDomain;
    }

    public void setImgDomain(String imgDomain) {
        this.imgDomain = imgDomain;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.domain);
        dest.writeStringList(this.loginUrlArr);
        dest.writeString(this.apiDomain);
        dest.writeString(this.imgDomain);
        dest.writeString(this.token);
        dest.writeString(this.url);
        dest.writeString(this.loginUrl);
    }

    public void readFromParcel(Parcel source) {
        this.userId = source.readString();
        this.domain = source.readString();
        this.loginUrlArr = source.createStringArrayList();
        this.apiDomain = source.readString();
        this.imgDomain = source.readString();
        this.token = source.readString();
        this.url = source.readString();
        this.loginUrl = source.readString();
    }

    public PMService() {
    }

    protected PMService(Parcel in) {
        this.userId = in.readString();
        this.domain = in.readString();
        this.loginUrlArr = in.createStringArrayList();
        this.apiDomain = in.readString();
        this.imgDomain = in.readString();
        this.token = in.readString();
        this.url = in.readString();
        this.loginUrl = in.readString();
    }

    public static final Creator<PMService> CREATOR = new Creator<PMService>() {
        @Override
        public PMService createFromParcel(Parcel source) {
            return new PMService(source);
        }

        @Override
        public PMService[] newArray(int size) {
            return new PMService[size];
        }
    };
}
