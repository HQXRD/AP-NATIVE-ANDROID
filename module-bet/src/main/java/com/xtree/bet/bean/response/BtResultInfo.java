package com.xtree.bet.bean.response;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class BtResultInfo implements BaseBean {
    /**
     * 单后订单状态code，由于系统为异步处理订单，下单后订单处于未确认状态
     */
    public int st;
    /**
     * 订单ID，返回为字符串
     */
    public String id;
    /**
     * 订单选项集合
     */
    public List<BtResultOptionInfo> ops = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.st);
        dest.writeString(this.id);
        dest.writeList(this.ops);
    }

    public void readFromParcel(Parcel source) {
        this.st = source.readInt();
        this.id = source.readString();
        this.ops = new ArrayList<BtResultOptionInfo>();
        source.readList(this.ops, BtResultOptionInfo.class.getClassLoader());
    }

    public BtResultInfo() {
    }

    protected BtResultInfo(Parcel in) {
        this.st = in.readInt();
        this.id = in.readString();
        this.ops = new ArrayList<BtResultOptionInfo>();
        in.readList(this.ops, BtResultOptionInfo.class.getClassLoader());
    }

    public static final Creator<BtResultInfo> CREATOR = new Creator<BtResultInfo>() {
        @Override
        public BtResultInfo createFromParcel(Parcel source) {
            return new BtResultInfo(source);
        }

        @Override
        public BtResultInfo[] newArray(int size) {
            return new BtResultInfo[size];
        }
    };
}
