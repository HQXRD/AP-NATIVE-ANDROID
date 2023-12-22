package com.xtree.bet.bean.ui;

import android.os.Parcel;

import com.xtree.bet.bean.response.BtResultInfo;

import java.util.HashMap;
import java.util.Map;

public class BtResultFb implements BtResult {
    private BtResultInfo btResultInfo;
    private static Map<String, String> statusMap = new HashMap<>();

    static {
        statusMap.put("0", "未确认");
        statusMap.put("1", "确认中");
        statusMap.put("2", "已拒单");
        statusMap.put("3", "已取消");
        statusMap.put("4", "已接单");
        statusMap.put("5", "已结算");
    }

    public BtResultFb(BtResultInfo btResultInfo){
        this.btResultInfo = btResultInfo;
    }

    @Override
    public int getStatus() {
        return btResultInfo.st;
    }

    @Override
    public String getStatusDesc() {
        int status = btResultInfo.st;
        return statusMap.get(String.valueOf(status));
    }

    @Override
    public String getId() {
        return btResultInfo.id;
    }

    @Override
    public boolean isSuccessed() {
        return !(btResultInfo.st == 2 || btResultInfo.st == 3);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.btResultInfo, flags);
    }

    public void readFromParcel(Parcel source) {
        this.btResultInfo = source.readParcelable(BtResultInfo.class.getClassLoader());
    }

    protected BtResultFb(Parcel in) {
        this.btResultInfo = in.readParcelable(BtResultInfo.class.getClassLoader());
    }

    public static final Creator<BtResultFb> CREATOR = new Creator<BtResultFb>() {
        @Override
        public BtResultFb createFromParcel(Parcel source) {
            return new BtResultFb(source);
        }

        @Override
        public BtResultFb[] newArray(int size) {
            return new BtResultFb[size];
        }
    };
}
