package com.xtree.recharge.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class ProcessingDataVo implements Parcelable {

    public boolean depProcessCnt1; // false,
    public boolean depProcessCnt3; // true,
    public int userProcessCount; // 6, 您已经连续充值6次
    public int jsonProcessPendCount; // 0,
    public int jsonProcessFailCount; // 0

    @Override
    public String toString() {
        return "ProcessingDataVo{" +
                "depProcessCnt1=" + depProcessCnt1 +
                ", depProcessCnt3=" + depProcessCnt3 +
                ", userProcessCount=" + userProcessCount +
                ", jsonProcessPendCount=" + jsonProcessPendCount +
                ", jsonProcessFailCount=" + jsonProcessFailCount +
                '}';
    }

    protected ProcessingDataVo(Parcel in) {
        depProcessCnt1 = in.readByte() != 0;
        depProcessCnt3 = in.readByte() != 0;
        userProcessCount = in.readInt();
        jsonProcessPendCount = in.readInt();
        jsonProcessFailCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (depProcessCnt1 ? 1 : 0));
        dest.writeByte((byte) (depProcessCnt3 ? 1 : 0));
        dest.writeInt(userProcessCount);
        dest.writeInt(jsonProcessPendCount);
        dest.writeInt(jsonProcessFailCount);
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
