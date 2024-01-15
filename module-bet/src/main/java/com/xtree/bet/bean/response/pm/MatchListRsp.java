package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class MatchListRsp implements BaseBean {
    private String cto;
    private int pages;
    public List<MatchInfo> data = new ArrayList<>();

    public int getPages() {
        return pages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cto);
        dest.writeInt(this.pages);
        dest.writeTypedList(this.data);
    }

    public void readFromParcel(Parcel source) {
        this.cto = source.readString();
        this.pages = source.readInt();
        this.data = source.createTypedArrayList(MatchInfo.CREATOR);
    }

    public MatchListRsp() {
    }

    protected MatchListRsp(Parcel in) {
        this.cto = in.readString();
        this.pages = in.readInt();
        this.data = in.createTypedArrayList(MatchInfo.CREATOR);
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
