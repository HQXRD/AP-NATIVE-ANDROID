package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class VideoAnimationInfo implements BaseBean {
    public String animationUrl;
    public String referUrl;
    public String serverTime;
    public List<VideoInfo> videoUrlVOList = new ArrayList<>();
    public List<VideoInfo> animation3Url = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.animationUrl);
        dest.writeString(this.referUrl);
        dest.writeString(this.serverTime);
        dest.writeTypedList(this.videoUrlVOList);
    }

    public void readFromParcel(Parcel source) {
        this.animationUrl = source.readString();
        this.referUrl = source.readString();
        this.serverTime = source.readString();
        this.videoUrlVOList = source.createTypedArrayList(VideoInfo.CREATOR);
    }

    public VideoAnimationInfo() {
    }

    protected VideoAnimationInfo(Parcel in) {
        this.animationUrl = in.readString();
        this.referUrl = in.readString();
        this.serverTime = in.readString();
        this.videoUrlVOList = in.createTypedArrayList(VideoInfo.CREATOR);
    }

    public static final Creator<VideoAnimationInfo> CREATOR = new Creator<VideoAnimationInfo>() {
        @Override
        public VideoAnimationInfo createFromParcel(Parcel source) {
            return new VideoAnimationInfo(source);
        }

        @Override
        public VideoAnimationInfo[] newArray(int size) {
            return new VideoAnimationInfo[size];
        }
    };
}
