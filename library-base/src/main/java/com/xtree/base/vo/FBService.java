package com.xtree.base.vo;

import android.os.Parcel;

import java.util.List;

public class FBService implements BaseBean{
    private String token;
    private FBServiceFoward forward;
    private List<String> domains;
    public boolean isDisabled;//true = 禁用，false = 启用

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public FBServiceFoward getForward() {
        return forward;
    }

    public void setForward(FBServiceFoward forward) {
        this.forward = forward;
    }

    public List<String> getDomains() {
        return domains;
    }

    public void setDomains(List<String> domains) {
        this.domains = domains;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeParcelable(this.forward, flags);
        dest.writeStringList(this.domains);
    }

    public void readFromParcel(Parcel source) {
        this.token = source.readString();
        this.forward = source.readParcelable(FBServiceFoward.class.getClassLoader());
        this.domains = source.createStringArrayList();
    }

    public FBService() {
    }

    protected FBService(Parcel in) {
        this.token = in.readString();
        this.forward = in.readParcelable(FBServiceFoward.class.getClassLoader());
        this.domains = in.createStringArrayList();
    }

    public static final Creator<FBService> CREATOR = new Creator<FBService>() {
        @Override
        public FBService createFromParcel(Parcel source) {
            return new FBService(source);
        }

        @Override
        public FBService[] newArray(int size) {
            return new FBService[size];
        }
    };
}
