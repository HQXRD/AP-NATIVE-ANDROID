package com.xtree.bet.bean.response;

import android.annotation.SuppressLint;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.xtree.base.vo.BaseBean;

import java.util.List;

@SuppressLint("ParcelCreator")
public class LeagueItem implements BaseBean {

    private String[] leagueNameList = new String[]{"全部", "欧冠", "英超", "意甲", "西甲", "德甲", "法甲", "中超"};
    private String[] imgList;

    private String name;
    private String img;

    public String[] getLeagueNameList() {
        return leagueNameList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LeagueItem(){

    }

    protected LeagueItem(Parcel in) {
        name = in.readString();
        img = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(img);
    }
}
