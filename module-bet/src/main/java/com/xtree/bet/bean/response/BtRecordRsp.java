package com.xtree.bet.bean.response;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class BtRecordRsp implements BaseBean {
    private int current;
    private int size;
    private int total;
    private int totalType;
    public List<BtResultInfo> records = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.current);
        dest.writeInt(this.size);
        dest.writeInt(this.total);
        dest.writeInt(this.totalType);
        dest.writeTypedList(this.records);
    }

    public void readFromParcel(Parcel source) {
        this.current = source.readInt();
        this.size = source.readInt();
        this.total = source.readInt();
        this.totalType = source.readInt();
        this.records = source.createTypedArrayList(BtResultInfo.CREATOR);
    }

    public BtRecordRsp() {
    }

    protected BtRecordRsp(Parcel in) {
        this.current = in.readInt();
        this.size = in.readInt();
        this.total = in.readInt();
        this.totalType = in.readInt();
        this.records = in.createTypedArrayList(BtResultInfo.CREATOR);
    }

    public static final Creator<BtRecordRsp> CREATOR = new Creator<BtRecordRsp>() {
        @Override
        public BtRecordRsp createFromParcel(Parcel source) {
            return new BtRecordRsp(source);
        }

        @Override
        public BtRecordRsp[] newArray(int size) {
            return new BtRecordRsp[size];
        }
    };
}
