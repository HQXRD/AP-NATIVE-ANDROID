package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class VideoInfo implements BaseBean {
    public String ds;
    public String flvUrl;
    public String muUrl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ds);
        dest.writeString(this.flvUrl);
        dest.writeString(this.muUrl);
    }

    public void readFromParcel(Parcel source) {
        this.ds = source.readString();
        this.flvUrl = source.readString();
        this.muUrl = source.readString();
    }

    public VideoInfo() {
    }

    protected VideoInfo(Parcel in) {
        this.ds = in.readString();
        this.flvUrl = in.readString();
        this.muUrl = in.readString();
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
