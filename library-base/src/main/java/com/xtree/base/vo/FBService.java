package com.xtree.base.vo;

import android.os.Parcel;

public class FBService implements BaseBean{
    private String token;
    private FBServiceFoward forward;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeParcelable(this.forward, flags);
    }

    public void readFromParcel(Parcel source) {
        this.token = source.readString();
        this.forward = source.readParcelable(FBServiceFoward.class.getClassLoader());
    }

    public FBService() {
    }

    protected FBService(Parcel in) {
        this.token = in.readString();
        this.forward = in.readParcelable(FBServiceFoward.class.getClassLoader());
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
