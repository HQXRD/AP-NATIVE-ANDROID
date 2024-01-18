package com.xtree.bet.bean.response.fb;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class MatchListRsp implements BaseBean {
    private int current;
    private int size;
    private int total;
    private int pages;
    public List<MatchInfo> records = new ArrayList<>();

    public int getPages(){
        if(size == 0){
            return 1;
        }
        int totalPage = total / size;
        if (total % size != 0) {
            totalPage++;
        }
        return totalPage;
    }

    public int getTotal() {
        return total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.current);
        dest.writeInt(this.size);
        dest.writeInt(this.total);
        dest.writeInt(this.pages);
        dest.writeTypedList(this.records);
    }

    public void readFromParcel(Parcel source) {
        this.current = source.readInt();
        this.size = source.readInt();
        this.total = source.readInt();
        this.pages = source.readInt();
        this.records = source.createTypedArrayList(MatchInfo.CREATOR);
    }

    public MatchListRsp() {
    }

    protected MatchListRsp(Parcel in) {
        this.current = in.readInt();
        this.size = in.readInt();
        this.total = in.readInt();
        this.pages = in.readInt();
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
