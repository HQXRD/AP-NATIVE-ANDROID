package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class AnimationInfo implements BaseBean {
    public String path;
    public String styleName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.styleName);
    }

    public void readFromParcel(Parcel source) {
        this.path = source.readString();
        this.styleName = source.readString();
    }

    public AnimationInfo() {
    }

    protected AnimationInfo(Parcel in) {
        this.path = in.readString();
        this.styleName = in.readString();
    }

    public static final Creator<AnimationInfo> CREATOR = new Creator<AnimationInfo>() {
        @Override
        public AnimationInfo createFromParcel(Parcel source) {
            return new AnimationInfo(source);
        }

        @Override
        public AnimationInfo[] newArray(int size) {
            return new AnimationInfo[size];
        }
    };
}
