package com.xtree.bet.bean.response;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class VideoInfo implements BaseBean {
    public String flvSD;
    public String m3u8SD;
    public String web;
    public String m3u8HD;
    public String flvHD;
    public boolean have;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.flvSD);
        dest.writeString(this.m3u8SD);
        dest.writeString(this.web);
        dest.writeString(this.m3u8HD);
        dest.writeString(this.flvHD);
        dest.writeByte(this.have ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.flvSD = source.readString();
        this.m3u8SD = source.readString();
        this.web = source.readString();
        this.m3u8HD = source.readString();
        this.flvHD = source.readString();
        this.have = source.readByte() != 0;
    }

    public VideoInfo() {
    }

    protected VideoInfo(Parcel in) {
        this.flvSD = in.readString();
        this.m3u8SD = in.readString();
        this.web = in.readString();
        this.m3u8HD = in.readString();
        this.flvHD = in.readString();
        this.have = in.readByte() != 0;
    }

    public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
        @Override
        public VideoInfo createFromParcel(Parcel source) {
            return new VideoInfo(source);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };
}
