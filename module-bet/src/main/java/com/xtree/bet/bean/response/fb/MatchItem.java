package com.xtree.bet.bean.response.fb;

import android.annotation.SuppressLint;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.xtree.base.vo.BaseBean;

@SuppressLint("ParcelCreator")
public class MatchItem implements BaseBean {
    private String name;
    private int type;
    private int matchCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }

    public MatchItem(){

    }

    protected MatchItem(Parcel in) {
        name = in.readString();
        type = in.readInt();
        matchCount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(type);
        parcel.writeInt(matchCount);
    }
}
