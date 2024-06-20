package com.xtree.base.vo;

import android.os.Parcel;

public class TopSpeedDomain implements BaseBean {
    public String url;
    public long speedSec;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeLong(this.speedSec);
    }

    public void readFromParcel(Parcel source) {
        this.url = source.readString();
        this.speedSec = source.readLong();
    }

    public TopSpeedDomain() {
    }

    protected TopSpeedDomain(Parcel in) {
        this.url = in.readString();
        this.speedSec = in.readLong();
    }

    public static final Creator<TopSpeedDomain> CREATOR = new Creator<TopSpeedDomain>() {
        @Override
        public TopSpeedDomain createFromParcel(Parcel source) {
            return new TopSpeedDomain(source);
        }

        @Override
        public TopSpeedDomain[] newArray(int size) {
            return new TopSpeedDomain[size];
        }
    };
}
