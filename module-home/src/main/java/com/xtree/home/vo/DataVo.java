package com.xtree.home.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class DataVo<T> implements Parcelable {
    public int count;
    public List<T> list;

    protected DataVo(Parcel in) {
        count = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DataVo> CREATOR = new Creator<DataVo>() {
        @Override
        public DataVo createFromParcel(Parcel in) {
            return new DataVo(in);
        }

        @Override
        public DataVo[] newArray(int size) {
            return new DataVo[size];
        }
    };

    @Override
    public String toString() {
        return "DataVo{" +
                "count=" + count +
                ", list=" + list.toArray() +
                '}';
    }
}
