package com.xtree.bet.bean.response;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class MatchListRsp implements BaseBean {
    private int current;
    private int size;
    private int total;
    private int pageTotal;
    public List<MatchInfo> records = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.current);
        dest.writeInt(this.size);
        dest.writeInt(this.total);
        dest.writeInt(this.pageTotal);
        dest.writeTypedList(this.records);
    }

    public void readFromParcel(Parcel source) {
        this.current = source.readInt();
        this.size = source.readInt();
        this.total = source.readInt();
        this.pageTotal = source.readInt();
        this.records = source.createTypedArrayList(MatchInfo.CREATOR);
    }

    public MatchListRsp() {
    }

    protected MatchListRsp(Parcel in) {
        this.current = in.readInt();
        this.size = in.readInt();
        this.total = in.readInt();
        this.pageTotal = in.readInt();
        this.records = in.createTypedArrayList(MatchInfo.CREATOR);
    }

    public static final Creator<MatchListRsp> CREATOR = new Creator<MatchListRsp>() {
        @Override
        public MatchListRsp createFromParcel(Parcel source) {
            return new MatchListRsp(source);
        }

        @Override
        public MatchListRsp[] newArray(int size) {
            return new MatchListRsp[size];
        }
    };
}
