package com.xtree.base.vo;

import android.os.Parcel;

public class FBServiceFoward implements BaseBean{
    private String apiServerAddress;
    private String apiEmbeddedServerAddress;
    private String pushServerAddress;
    private String pcAddress;
    private String h5Address;
    private String virtualAddress;

    public String getApiServerAddress() {
        return apiServerAddress;
    }

    public void setApiServerAddress(String apiServerAddress) {
        this.apiServerAddress = apiServerAddress;
    }

    public String getApiEmbeddedServerAddress() {
        return apiEmbeddedServerAddress;
    }

    public void setApiEmbeddedServerAddress(String apiEmbeddedServerAddress) {
        this.apiEmbeddedServerAddress = apiEmbeddedServerAddress;
    }

    public String getPushServerAddress() {
        return pushServerAddress;
    }

    public void setPushServerAddress(String pushServerAddress) {
        this.pushServerAddress = pushServerAddress;
    }

    public String getPcAddress() {
        return pcAddress;
    }

    public void setPcAddress(String pcAddress) {
        this.pcAddress = pcAddress;
    }

    public String getH5Address() {
        return h5Address;
    }

    public void setH5Address(String h5Address) {
        this.h5Address = h5Address;
    }

    public String getVirtualAddress() {
        return virtualAddress;
    }

    public void setVirtualAddress(String virtualAddress) {
        this.virtualAddress = virtualAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.apiServerAddress);
        dest.writeString(this.apiEmbeddedServerAddress);
        dest.writeString(this.pushServerAddress);
        dest.writeString(this.pcAddress);
        dest.writeString(this.h5Address);
        dest.writeString(this.virtualAddress);
    }

    public void readFromParcel(Parcel source) {
        this.apiServerAddress = source.readString();
        this.apiEmbeddedServerAddress = source.readString();
        this.pushServerAddress = source.readString();
        this.pcAddress = source.readString();
        this.h5Address = source.readString();
        this.virtualAddress = source.readString();
    }

    public FBServiceFoward() {
    }

    protected FBServiceFoward(Parcel in) {
        this.apiServerAddress = in.readString();
        this.apiEmbeddedServerAddress = in.readString();
        this.pushServerAddress = in.readString();
        this.pcAddress = in.readString();
        this.h5Address = in.readString();
        this.virtualAddress = in.readString();
    }

    public static final Creator<FBServiceFoward> CREATOR = new Creator<FBServiceFoward>() {
        @Override
        public FBServiceFoward createFromParcel(Parcel source) {
            return new FBServiceFoward(source);
        }

        @Override
        public FBServiceFoward[] newArray(int size) {
            return new FBServiceFoward[size];
        }
    };
}
