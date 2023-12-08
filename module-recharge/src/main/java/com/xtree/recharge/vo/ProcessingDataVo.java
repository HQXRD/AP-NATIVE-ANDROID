package com.xtree.recharge.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class ProcessingDataVo implements Parcelable {

    public boolean depProcessCnt1; // false,
    public boolean depProcessCnt3; // true,
    public String userProcessCount; // 6, 您已经连续充值6次
    public String jsonProcessPendCount; // 0,
    public String jsonProcessFailCount; // 0

    protected ProcessingDataVo(Parcel in) {
        depProcessCnt1 = in.readByte() != 0;
        depProcessCnt3 = in.readByte() != 0;
        userProcessCount = in.readString();
        jsonProcessPendCount = in.readString();
        jsonProcessFailCount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (depProcessCnt1 ? 1 : 0));
        dest.writeByte((byte) (depProcessCnt3 ? 1 : 0));
        dest.writeString(userProcessCount);
        dest.writeString(jsonProcessPendCount);
        dest.writeString(jsonProcessFailCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProcessingDataVo> CREATOR = new Creator<ProcessingDataVo>() {
        @Override
        public ProcessingDataVo createFromParcel(Parcel in) {
            return new ProcessingDataVo(in);
        }

        @Override
        public ProcessingDataVo[] newArray(int size) {
            return new ProcessingDataVo[size];
        }
    };
}
